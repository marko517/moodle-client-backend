package com.moodle.application.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodle.application.dto.MoodleUsers;
import com.moodle.application.service.MoodleUserManagementService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController()
@RequestMapping("/api")
public class UserManagementController {

	@Autowired
	private MoodleUserManagementService moodleUsersManagementService;

	@GetMapping("/users/{userId}")
	private ResponseEntity<MoodleUsers> getUsersInfo(@PathVariable Long userId) throws IOException {
		return ResponseEntity.status(HttpStatus.OK)
				.body(moodleUsersManagementService.getUserInfo(userId));
	}

	@GetMapping("/users")
	public ResponseEntity<MoodleUsers> getUsersInformation() throws IOException {
		return ResponseEntity.status(HttpStatus.OK)
				.body(moodleUsersManagementService.getUsersInfo());
	}
}
