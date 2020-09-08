package com.moodle.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodleUser {

	private Long id;
	private String username;
	private String firstname;
	private String lastname;
	private String fullname;
	private String email;
	private String department;
	private String city;
	private String country;
}
