package com.abhishek.stories_app.security;

import jakarta.servlet.http.HttpServletRequest;

final class ClientIpResolver {

	private ClientIpResolver() {}

	static String resolve(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (forwarded != null && !forwarded.isBlank()) {
			String first = forwarded.split(",")[0].trim();
			if (!first.isEmpty()) {
				return first;
			}
		}
		String realIp = request.getHeader("X-Real-IP");
		if (realIp != null && !realIp.isBlank()) {
			return realIp.trim();
		}
		return request.getRemoteAddr();
	}
}
