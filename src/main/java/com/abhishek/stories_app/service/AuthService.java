package com.abhishek.stories_app.service;

import com.abhishek.stories_app.dto.AuthResponse;
import com.abhishek.stories_app.dto.LoginRequest;
import com.abhishek.stories_app.dto.RegisterRequest;
import com.abhishek.stories_app.exception.BadRequestException;
import com.abhishek.stories_app.exception.UnauthorizedException;
import com.abhishek.stories_app.mapper.UserMapper;
import com.abhishek.stories_app.model.Role;
import com.abhishek.stories_app.model.User;
import com.abhishek.stories_app.repository.UserRepository;
import com.abhishek.stories_app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public AuthResponse register(RegisterRequest req) {
		if (userRepository.existsByEmailIgnoreCase(req.email())) {
			throw new BadRequestException("Email already registered");
		}
		String username = uniqueUsernameFromEmail(req.email());
		User user =
				User.builder()
						.username(username)
						.email(req.email().trim().toLowerCase())
						.passwordHash(passwordEncoder.encode(req.password()))
						.name(req.name().trim())
						.role(Role.USER)
						.build();
		user = userRepository.save(user);
		String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
		return new AuthResponse(token, UserMapper.toResponse(user));
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest req) {
		User user =
				userRepository
						.findByEmailIgnoreCase(req.email().trim().toLowerCase())
						.orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
		if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
			throw new UnauthorizedException("Invalid email or password");
		}
		String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
		return new AuthResponse(token, UserMapper.toResponse(user));
	}

	private String uniqueUsernameFromEmail(String email) {
		int at = email.indexOf('@');
		String base =
				(at > 0 ? email.substring(0, at) : email).replaceAll("[^a-zA-Z0-9_]", "");
		if (base.isEmpty()) {
			base = "user";
		}
		if (base.length() > 80) {
			base = base.substring(0, 80);
		}
		String candidate = base;
		int n = 0;
		while (userRepository.existsByUsernameIgnoreCase(candidate)) {
			n++;
			candidate = base + "_" + n;
		}
		return candidate;
	}
}
