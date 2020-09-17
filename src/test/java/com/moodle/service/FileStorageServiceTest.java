package com.moodle.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.moodle.application.service.FileStorageService;

public class FileStorageServiceTest {

	@Autowired
	private FileStorageService fileStorageService;

	@Test
	public void uploadDifferentTypeOfFileFails() throws Exception {
		File file = new File("D:\\Diplomski\\TestFiles\\TestFile.txt");
		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile("file",
				file.getName(), "text/plain", input);

		assertThrows(RuntimeException.class, () -> {
			fileStorageService.storeFile(multipartFile);
	    });
	}

	@Test
	public void uploadWillFailIfNoFileIsProvided() throws Exception {
		assertThrows(RuntimeException.class, () -> {
			fileStorageService.storeFile(null);
	    });
	}
}
