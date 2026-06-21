package com.abhishek.stories_app.security;

import com.abhishek.stories_app.config.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

@Service
public class RateLimitService {

	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

	private final RateLimitProperties properties;
	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	public RateLimitService(RateLimitProperties properties) {
		this.properties = properties;
	}

	public boolean isEnabled() {
		return properties.enabled() && !properties.rules().isEmpty();
	}

	public List<RateLimitProperties.Rule> matchingRules(String method, String path) {
		return properties.rules().stream()
				.filter(rule -> matches(rule, method, path))
				.toList();
	}

	public boolean tryConsume(RateLimitProperties.Rule rule, String clientKey) {
		String bucketKey = rule.name() + ":" + clientKey;
		Bucket bucket =
				buckets.computeIfAbsent(bucketKey, ignored -> newBucket(rule));
		return bucket.tryConsume(1);
	}

	public long retryAfterSeconds(RateLimitProperties.Rule rule) {
		return Math.max(1, rule.window().getSeconds());
	}

	private static boolean matches(RateLimitProperties.Rule rule, String method, String path) {
		if (!rule.methods().isEmpty()
				&& rule.methods().stream().noneMatch(m -> m.equalsIgnoreCase(method))) {
			return false;
		}
		return PATH_MATCHER.match(rule.pathPattern(), path);
	}

	private static Bucket newBucket(RateLimitProperties.Rule rule) {
		int capacity = Math.max(1, rule.capacity());
		int refill = Math.max(1, rule.refillAmount());
		Bandwidth limit =
				Bandwidth.builder()
						.capacity(capacity)
						.refillGreedy(refill, rule.window())
						.build();
		return Bucket.builder().addLimit(limit).build();
	}
}
