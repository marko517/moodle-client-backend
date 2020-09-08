package com.moodle.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUser {

	private String username;
	private String password;
	private String email;
	private String tokenId;

}
