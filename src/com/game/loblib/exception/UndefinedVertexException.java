package com.game.loblib.exception;

// Thrown when an operation is performed on an undefined vertex
public class UndefinedVertexException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public UndefinedVertexException() {
	}
	
	public UndefinedVertexException(String msg) {
		super(msg);
	}
}
