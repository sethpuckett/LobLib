package com.game.loblib.messaging;

import com.game.loblib.utility.android.FixedSizeArray;

public class MessageManager {
	protected static StringBuffer _tag = new StringBuffer("MessageManager");
	protected MessagePool _pool;
	protected MessageSubscriptionPool _subscriptionPool = new MessageSubscriptionPool(128);
	protected FixedSizeArray<MessageSubscription> _subscribers = new FixedSizeArray<MessageSubscription>(128);
	protected FixedSizeArray<MessageSubscription> _addList = new FixedSizeArray<MessageSubscription>(32);
	protected FixedSizeArray<MessageSubscription> _removeList = new FixedSizeArray<MessageSubscription>(32);
	protected long[] _removeTypeList = new long[32];
	protected boolean _handlingMessage = false;
	
	public MessageManager() {
		_subscribers.setComparator(new MessageSubscriptionComparator());
		_pool = new MessagePool(32);
	}
	
	// Subscribe to the message types indicated by flags
	public void subscribe(IMessageHandler receiver, long types) {
		MessageSubscription ms = _subscriptionPool.allocate();
		ms.Receiver = receiver;
		ms.Types = types;
			
		if (!_handlingMessage)
			addSubscription(ms);
		else
			_addList.add(ms);
	}
	
	protected void addSubscription(MessageSubscription ms) {
		// If subscriber does not yet exists in _subscribers, add it
		int index = _subscribers.find(ms, false);
		if (index == -1)
			_subscribers.add(ms);
		// Otherwise update existing entry with any new subscriptions
		else {
			_subscribers.get(index).Types |= ms.Types;
			_subscriptionPool.release(ms);
		}
	}
	
	// Unsubscribe from the message types indicated by flags
	public void unsubscribe(IMessageHandler receiver, long types) {
		// allocate a temporary subscription for searching
		MessageSubscription search = _subscriptionPool.allocate();
		search.Receiver = receiver;
		search.Types = types;
		
		int index = _subscribers.find(search, false);
		if (index != -1) {
			// if manager is not currently handling messages, remove the subscriptions immediately
			if (!_handlingMessage)
				removeSubscription(index, types);
			// Otherwise add to the removeList for removal when processing is finished
			else {
				_removeList.add(_subscribers.get(index));
				_removeTypeList[_removeList.getCount() - 1] = types;
			}
		}
		
		_subscriptionPool.release(search);
	}
	
	protected void removeSubscription(int subscriberIndex, long types) {
		MessageSubscription found = _subscribers.get(subscriberIndex);
		long newTypes = found.Types & ~types;
		// no remaining types; remove subscription
		if (newTypes == 0) {
			_subscribers.remove(subscriberIndex);
			_subscriptionPool.release(found);
		}
		// update types
		else
			_subscribers.get(subscriberIndex).Types = newTypes;
	}
	
	// Send a message with no attached data
	public <T> void sendMessage(long type) {
		sendMessage(type, null, false);
	}
	
	// Send a message with attached data
	public <T> void sendMessage(long type, T data) {
		sendMessage(type, data, false);
	}
	
	// Send a message with attached data
	public <T> void sendMessage(long type, T data, boolean releaseData) {
		Message msg = allocate(type, releaseData);
		if (data != null)
			msg.setData(data);
		sendMessage(msg);
	}

	public Message allocate() {
		Message m = _pool.allocate();
		m.Type = MessageType.UNKNOWN;
		m.ReleaseData = false;
		return m;
	}
	
	public Message allocate(long type, boolean releaseData) {
		Message m = _pool.allocate();
		m.Type = type;
		m.ReleaseData = releaseData;
		return m;
	}
	
	public void release(Message m) {
		_pool.release(m);
	}

	// Clear all message subscribers
	public void flush() {
		_subscribers.clear();
		_addList.clear();
		_removeList.clear();
	}
	
	// Syncs pending subscriptions that were added/removed during processing
	protected void updateSubscriptions() {
		int subCount = _addList.getCount();
		for (int i = 0; i < subCount; i++)
			addSubscription(_addList.get(i));
		_addList.clear();
		
		int unsubCount = _removeList.getCount();
		for (int i = 0; i < unsubCount; i++)
			removeSubscription(_subscribers.find(_removeList.get(i), false), _removeTypeList[i]);
		_removeList.clear();
	}

	// Sends message to relevant subscribers
	protected void sendMessage(Message message) {
		_handlingMessage = true;
		
		int count = _subscribers.getCount();
		for (int i = 0; i < count; i++) {
			if ((_subscribers.get(i).Types & message.Type) > 0) {
				_subscribers.get(i).Receiver.handleMessage(message);
			}
		}
		
		//release data
		if (message.ReleaseData){
			message.Releaser.release(message._messageData);
		}
		//release message
		_pool.release(message);
		
		_handlingMessage = false;
		updateSubscriptions();
	}

}
