package com.moodle.application.exception;

@SuppressWarnings("serial")
public class StorageException extends Exception {

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable exception) {
		super(message, exception);
	}

}
