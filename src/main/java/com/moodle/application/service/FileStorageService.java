package com.moodle.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.moodle.application.exception.StorageException;
import com.moodle.application.property.FileStorageProperties;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageProperties properties) throws Exception {
		fileStorageLocation = Paths.get(properties.getUploadDirectory()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception exception) {
			throw new StorageException("Failed to create storage directory", exception);
		}
	}

	public List<String> storeFile(MultipartFile file) throws Exception {
		if (!isDirectoryEmpty()) {
			try {
				FileUtils.cleanDirectory(fileStorageLocation.toFile());
			} catch(IOException exception) {
				exception.printStackTrace();
			}
		}
		String filename = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, fileStorageLocation.resolve(filename),
						StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException exception) {
			throw new StorageException("Failed to store file " + filename, exception);
		}

		try {
			unzipFile(filename);
		} catch(ZipException exception) {
			exception.getStackTrace();
		}

		List<String> submissionDocxFiles = new ArrayList<String>();
		submissionDocxFiles = checkSubmissionForRquiredFiles("docx");

		List<String> submissionPdfFiles = new ArrayList<String>();
		submissionPdfFiles = checkSubmissionForRquiredFiles(".pdf");

		List<String> submissionTxtFiles = new ArrayList<String>();
		submissionTxtFiles = checkSubmissionForRquiredFiles(".txt");

		List<String> submissonJpgFiles = new ArrayList<String>();
		submissonJpgFiles = checkSubmissionForRquiredFiles(".jpg");

		List<String> submissionFiles = Stream.of(submissionDocxFiles, submissionPdfFiles, submissionTxtFiles, submissonJpgFiles)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

		try {
			FileUtils.cleanDirectory(fileStorageLocation.toFile());
		} catch(IOException exception) {
			throw new StorageException("Error clearing the directory");
		}
		return submissionFiles;
	}

	private boolean isDirectoryEmpty() throws IOException {
		try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fileStorageLocation)) {
	        return !directoryStream.iterator().hasNext();
	    }
	}

	private List<String> checkSubmissionForRquiredFiles(String extension) throws Exception {
		List<String> result = Files.find(fileStorageLocation, 100,
			    (p, a) -> p.toString().toLowerCase().endsWith(extension))
			    .map(path -> path.toString())
			    .collect(Collectors.toList());

		return result;
	}

	private void unzipFile(String filename) throws Exception {
		Path destinationDirectory = fileStorageLocation;
		Path zipFilePath = Paths.get(fileStorageLocation.toString() + "\\" + filename);

		new ZipFile(zipFilePath.toFile()).extractAll(destinationDirectory.toString());
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(fileStorageLocation.toFile());
	}
}
