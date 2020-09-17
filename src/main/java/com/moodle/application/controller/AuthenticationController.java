package com.moodle.application.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodle.application.dto.AuthenticationResponse;
import com.moodle.application.dto.LoginRequest;
import com.moodle.application.dto.RefreshTokenRequest;
import com.moodle.application.dto.RegisterUser;
import com.moodle.application.service.AuthenticationService;
import com.moodle.application.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/authn")
@AllArgsConstructor
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody RegisterUser registerUser) {
		authenticationService.signup(registerUser);
		return ResponseEntity.status(HttpStatus.OK)
				.body("User registered successfully");
	}

	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authenticationService.login(loginRequest);
	}

	@PostMapping("/refresh/token")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return authenticationService.refreshToken(refreshTokenRequest);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

		return ResponseEntity.status(HttpStatus.OK)
				.body("Refresh Token Deleted Succesfully!");
	}
}
