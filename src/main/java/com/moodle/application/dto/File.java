package com.moodle.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {

	private String filename;
	private int filesize;
	private String fileurl;
	private Long timemodified;
	private String mimetype;
}
