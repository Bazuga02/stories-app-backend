package com.abhishek.stories_app.controller;

import com.abhishek.stories_app.dto.AuthResponse;
import com.abhishek.stories_app.dto.LoginRequest;
import com.abhishek.stories_app.dto.RegisterRequest;
import com.abhishek.stories_app.dto.UserResponse;
import com.abhishek.stories_app.service.AuthService;
import com.abhishek.stories_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

	private final AuthService authService;
	private final UserService userService;

	@PostMapping("/register")
	@Operation(summary = "Register a new user (ROLE_USER)")
	public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
		return authService.register(req);
	}

	@PostMapping("/login")
	@Operation(summary = "Login with email and password; returns JWT")
	public AuthResponse login(@Valid @RequestBody LoginRequest req) {
		return authService.login(req);
	}

	@GetMapping("/me")
	@Operation(summary = "Current user profile")
	public UserResponse me() {
		return userService.getMe();
	}
}
