package com.game.loblib.exception;

// Used by AllocationGuard when a memory leak is detected
public class IllegalAllocationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public IllegalAllocationException() {
	}
	
	public IllegalAllocationException(String msg) {
		super(msg);
	}
}