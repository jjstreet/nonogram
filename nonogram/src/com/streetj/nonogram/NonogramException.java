package com.streetj.nonogram;

public class NonogramException extends Exception {

	public NonogramException() {
		super();
	}
	
	public NonogramException(String message) {
		super(message);
	}
	
	public NonogramException(Throwable throwable) {
		super(throwable);
	}
	
	public NonogramException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
