package com.moodle.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moodle.application.exception.MoodleAppException;
import com.moodle.application.model.RefreshToken;
import com.moodle.application.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());

		return refreshTokenRepository.save(refreshToken);
	}

	void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
		.orElseThrow(() -> new MoodleAppException("Invalid refresh token"));
	}

	public void deleteRefreshToken(String token) {
		//if no tokens found throw illegal argument exception
		refreshTokenRepository.deleteByToken(token);
	}
}
