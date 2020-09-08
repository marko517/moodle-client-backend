package com.moodle.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodle.application.dto.Assignments;
import com.moodle.application.dto.Courses;
import com.moodle.application.service.MoodleAssignmentManagerService;

@RestController
@RequestMapping("/api")
public class AssignmentManagementController {

	@Autowired
	private MoodleAssignmentManagerService moodleAssignmentManagementService;

	@GetMapping("/assignments/{courseId}")
	public ResponseEntity<Courses> getAssignmentsForCourse(@PathVariable Long courseId) throws Exception {
		return ResponseEntity.status(HttpStatus.OK)
				.body(moodleAssignmentManagementService.getAssignmentsForCourse(courseId));
	}

	@GetMapping("/submissions/{assignmentId}")
	public ResponseEntity<Assignments> getSubmissionsForAssignment(@PathVariable Long assignmentId) throws Exception {
		return ResponseEntity.status(HttpStatus.OK)
				.body(moodleAssignmentManagementService.getSubmissionForAssignment(assignmentId));
	}
}