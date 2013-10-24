package com.game.loblib.messaging;

public class MessageData<T> {
	protected T _data;
	
	public MessageData(T data) {
		_data = data;
	}
	
	public T getData() {
		return _data;
	}
}
