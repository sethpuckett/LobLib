package com.game.loblib.entity;

import com.game.loblib.behavior.Behavior;
import com.game.loblib.behavior.BehaviorType;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.android.AllocationGuard;
import com.game.loblib.utility.android.FixedSizeArray;

// Game Entities contain a collection of behaviors that determine how they act in the game world
public class GameEntity extends AllocationGuard {
	protected static final StringBuffer _tag = new StringBuffer("GameEntity");

	public EntityAttributes Attributes = new EntityAttributes();
	public int ScreenLevel;
	
	protected FixedSizeArray<Behavior> _behaviors = new FixedSizeArray<Behavior>(32);
	protected long _enabledBehaviorTypes;
	protected boolean _paused = false;
	
	public GameEntity() {
	}
	
	public StringBuffer getTag() {
		return _tag;
	}
	
	public void pause() {
		if (!_paused) {
			_enabledBehaviorTypes = getEnabledBehaviors();
			disableBehaviors();
			_paused = true;
		}
		else
			Logger.w(_tag, "cannot pause; entity already paused");
	}
	
	// disables all behaviors, except those in behaviorExceptions
	public void pause(long behaviorExceptionTypes) {
		if (!_paused) {
			_enabledBehaviorTypes = getEnabledBehaviors();
			disableBehaviors(BehaviorType.ALL ^ behaviorExceptionTypes);
			_paused = true;
		}
		else
			Logger.w(_tag, "cannot pause; entity already paused");
	}
	
	public void unpause() {
		if (_paused) {
			_paused = false;
			if (_enabledBehaviorTypes > 0)
				enableBehaviors(_enabledBehaviorTypes);
		}
		else
			Logger.w(_tag, "cannot unpause; entity is not paused");
	}
	
	public void update(float updateRatio) {
		// Update all enabled behaviors
		int count = _behaviors.getCount();
		for (int i = 0; i < count; i++) {
			Behavior b = _behaviors.get(i);
			if (b.isEnabled())
				b.update(updateRatio);
		}
	}
	
	// Adds behavior if it does not already exist in entity
	public void addBehavior(Behavior b) {
		int index = _behaviors.find(b, false);
		if (index == -1) {
			_behaviors.add(b);
			b.setEntity(this);
			b.init();
		}
		else
			Logger.w(_tag, "behavior found; cannot add");
	}

	// Removes behavior if it exists in entity
	public void removeBehavior(Behavior b) {
		int index = _behaviors.find(b, false);
		if (index != -1) {
			_behaviors.remove(b, false);
			b.setEntity(null);
		}
		else
			Logger.w(_tag, "behavior not found; cannot remove");
	}
	
	// Removes all behaviors indicated by type flags
	public void removeBehaviors(long types) {
		int count = _behaviors.getCount();
		for (int i = 0; i < count; ) {
			Behavior b = _behaviors.get(i);
			if ((b.getType() & types) > 0) {
				_behaviors.remove(i);
				count--;
			}
			else
				i++;
		}
	}
	
	// Enables all behaviors
	public void enableBehaviors() {
		enableBehaviors(BehaviorType.ALL);
	}
	
	// Enables behaviors indicated by type flags
	public void enableBehaviors(long types) {
		int count = _behaviors.getCount();
		for (int i = 0; i < count; i++) {
			Behavior b = _behaviors.get(i);
			if ((b.getType() & types) > 0) {
				if (!_paused)
					b.enable();
				else
					_enabledBehaviorTypes = _enabledBehaviorTypes | b.getType();
			}
		}
	}
	
	// Disables all behaviors
	public void disableBehaviors() {
		disableBehaviors(BehaviorType.ALL);
	}
	
	// Disables behaviors indicated by type flags
	public void disableBehaviors(long types) {
		int count = _behaviors.getCount();
		for (int i = 0; i < count; i++) {
			Behavior b = _behaviors.get(i);
			if ((b.getType() & types) > 0) {
				if (!_paused)
					b.disable();
				else
					_enabledBehaviorTypes = _enabledBehaviorTypes & ~b.getType();
			}
		}
	}

	// Removes all behaviors
	public void destroyBehaviors() {
		destroyBehaviors(BehaviorType.ALL);
	}
	
	// Removes behaviors indicated by type flags
	public void destroyBehaviors(long types) {
		int count = _behaviors.getCount();
		for (int i = 0; i < count; i++) {
			Behavior b = _behaviors.get(i);
			if ((b.getType() & types) > 0)
				b.destroy();
		}
	}
	
	// Gets behavior indicated by type flag
	public Behavior getBehavior(long type) {
		int count = _behaviors.getCount();
		for (int i = 0; i < count; i++) {
			Behavior b = _behaviors.get(i);
			if (b.getType() == type)
				return b;
		}
		Logger.e(_tag, "Entity does not have a behavior of this type");
		return null;
	}

	// Returns type flags indicating all enabled behaviors
	protected long getEnabledBehaviors() {
		long enabledBehaviors = 0;
		int count = _behaviors.getCount();
		for (int i = 0; i < count; i++) {
			Behavior b = _behaviors.get(i);
			if (b.isEnabled())
				enabledBehaviors = enabledBehaviors | b.getType(); 
		}
		return enabledBehaviors;
	}
}
