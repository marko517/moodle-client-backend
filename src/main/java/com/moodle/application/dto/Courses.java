package com.moodle.application.dto;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class Courses {

	private List<Course> courses;
}
