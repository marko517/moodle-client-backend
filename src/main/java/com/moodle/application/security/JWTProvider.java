package com.moodle.application.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class JWTProvider {

	private Key key;

	@Value("${jwt.expiration.time}")
	private Long jwtExpirationTimeInMilis;

	@PostConstruct
	public void init() {
		key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	public String generateToken(Authentication authentication) {
		//Return principal form the authenticate object
		User principal = (User)authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(principal.getUsername())
				.signWith(key)
				.setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMilis)))
				.compact();
	}

	//if jwt expired no user could be fetched from security context
	public String generateTokenWithUsername(String username) {
		return Jwts.builder()
				.setSubject(username)
				.signWith(key)
				.setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMilis)))
				.compact();
	}

	public boolean validateToken(String jwt) {
		Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
		return true;
	}

	public String extractUsernameFromToken(String jwt) {
		Claims claims = Jwts.parser()
						.setSigningKey(key)
						.parseClaimsJws(jwt)
						.getBody();

		return claims.getSubject();
	}
}
