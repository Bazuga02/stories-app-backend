package com.abhishek.stories_app.config;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rate-limit")
public record RateLimitProperties(boolean enabled, List<Rule> rules) {

	public RateLimitProperties {
		if (rules == null) {
			rules = List.of();
		}
	}

	public record Rule(
			String name,
			String pathPattern,
			List<String> methods,
			int capacity,
			int refillAmount,
			Duration window) {

		public Rule {
			if (methods == null) {
				methods = List.of();
			}
		}
	}
}
