package com.moodle.application.exception;

@SuppressWarnings("serial")
public class StorageException extends RuntimeException {

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Exception exception) {
		super(message, exception);
	}

}
