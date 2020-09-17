package com.moodle.application.exception;

@SuppressWarnings("serial")
public class MoodleAppException extends RuntimeException{

	public MoodleAppException(String message) {
		super(message);
	}

	public MoodleAppException(String message, Exception exception) {
		super(message, exception);
	}
}
