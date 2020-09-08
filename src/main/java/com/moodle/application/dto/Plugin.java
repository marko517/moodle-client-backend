package com.moodle.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plugin {

	private String type;
	private String name;
	private List<FileArea> fileareas;
	private String gradingstatus;
}
