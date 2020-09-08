package com.moodle.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignments {

	private Long id;
	private Long course;
	private String name;
}
