package com.moodle.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

	private Long id;
	private Long userid;
	private int attemptnumber;
	private Long timecreated;
	private Long timemodified;
	private String status;
	private List<Plugin> plugins;
	
}
