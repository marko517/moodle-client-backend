package com.moodle.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moodle.application.service.FileStorageService;

@RestController
@RequestMapping("/api/file")
public class FileHandlerController {

	@Autowired
	private FileStorageService storageService;

	@PostMapping("/file-upload")
	public ResponseEntity<List<String>> checkSubmissionContentForFile(@RequestParam("file") MultipartFile file) throws Exception {
		return ResponseEntity.status(HttpStatus.OK)
				.body(storageService.storeFile(file));
	}
}
