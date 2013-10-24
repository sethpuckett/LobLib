package com.game.loblib.messaging;

import com.game.loblib.messaging.DataReleaser;
import com.game.loblib.messaging.MessageData;
import com.game.loblib.messaging.MessageType;

public class Message {
	public long Type = MessageType.UNKNOWN;
	public boolean ReleaseData = false; // if true, data will released after message is sent
	// If ReleaseData is set to false and MessageData contains a managed resource a DataReleaser should be defined to free it when no longer needed
	public DataReleaser Releaser = null;
	
	protected MessageData<?> _messageData;

	public Message() {
	}

	public <T> void setData(T data) {
		_messageData = new MessageData<T>(data);
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T)_messageData.getData();
	}
}