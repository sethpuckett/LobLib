package com.game.loblib.exception;

// Thrown by resource pools when there is no more room for additional resources
public class PoolExhaustedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PoolExhaustedException() {
	}
	
	public PoolExhaustedException(String msg) {
		super(msg);
	}
}
