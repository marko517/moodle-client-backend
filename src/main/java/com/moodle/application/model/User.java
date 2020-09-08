package com.moodle.application.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotBlank(message = "Username cannot be blank")
	private String username;
	@NotBlank(message = "Password cannot be blank")
	private String password;
	@NotBlank(message = "Email cannot be blank")
	private String email;
	private Instant created;
	@NotBlank(message = "Token ID cannot be blank")
	private String tokenId;
}
