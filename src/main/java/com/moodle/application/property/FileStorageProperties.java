package com.moodle.application.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

	private String uploadDirectory;
}
