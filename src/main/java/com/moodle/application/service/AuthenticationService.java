package com.moodle.application.service;

import java.time.Instant;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moodle.application.dto.AuthenticationResponse;
import com.moodle.application.dto.LoginRequest;
import com.moodle.application.dto.RefreshTokenRequest;
import com.moodle.application.dto.RegisterUser;
import com.moodle.application.model.User;
import com.moodle.application.repository.UserRepository;
import com.moodle.application.security.JWTProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthenticationService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTProvider jwtProvider;

	@Autowired
	private RefreshTokenService refreshTokenService;

	public void signup(RegisterUser registerUserDTO) {
		User user = new User();
		user.setUsername(registerUserDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
		user.setEmail(registerUserDTO.getEmail());
		user.setTokenId(registerUserDTO.getTokenId());
		user.setCreated(Instant.now());

		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);

		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTimeInMilis()))
				.username(loginRequest.getUsername())
				.build();
	}

	Optional<org.springframework.security.core.userdetails.User> getCurrentLoggedInUser() {
		org.springframework.security.core.userdetails.User principal =
				(org.springframework.security.core.userdetails.User)SecurityContextHolder
					.getContext()
					.getAuthentication()
					.getPrincipal();
		return Optional.of(principal);
	}

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) throws Exception {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTimeInMilis()))
				.username(refreshTokenRequest.getUsername())
				.build();
	}

}
