package com.moodle.application.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodle.application.dto.Course;
import com.moodle.application.service.CourseManagementService;

@RestController
@RequestMapping("/api")
public class CourseMangementController {

	@Autowired
	private CourseManagementService courseManagementService;

	@GetMapping("/courses")
	public ResponseEntity<Collection<Course>>getCoursesInformation() throws Exception {
		return ResponseEntity.status(HttpStatus.OK)
				.body(courseManagementService.getCourseInformation());
	}
}
