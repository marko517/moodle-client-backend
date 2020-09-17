package com.moodle.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Course>getCoursesInformation() throws Exception {
		return courseManagementService.getCourseInformation();
	}
}
