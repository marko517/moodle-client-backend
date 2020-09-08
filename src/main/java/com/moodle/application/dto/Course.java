package com.moodle.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

	private Long id;
	private String fullname;
	private List<CourseAssignments> assignments;
}
