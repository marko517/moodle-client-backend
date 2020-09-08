package com.moodle.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.moodle.application.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class MoodleAssignmentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodleAssignmentManagementApplication.class, args);
	}

}
