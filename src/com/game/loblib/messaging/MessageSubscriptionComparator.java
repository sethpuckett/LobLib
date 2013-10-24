package com.game.loblib.messaging;

import java.util.Comparator;

public class MessageSubscriptionComparator implements Comparator<MessageSubscription> {

	@Override
	public int compare(MessageSubscription sub1, MessageSubscription sub2) {
		if (sub1.Receiver.hashCode() == sub2.Receiver.hashCode())
			return 0;
		else if (sub1.Receiver.hashCode() < sub1.Receiver.hashCode())
			return -1;
		else
			return 1;
	}

}
