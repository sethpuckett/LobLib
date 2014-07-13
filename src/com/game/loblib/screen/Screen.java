package com.game.loblib.screen;

import com.game.loblib.entity.GameEntity;
import com.game.loblib.messaging.IMessageHandler;
import com.game.loblib.messaging.Message;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.sound.Sound;
import com.game.loblib.utility.ButtonControlType;
import com.game.loblib.utility.GameSettings;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.FixedSizeArray;

// Base class that all game screens should inherit from
public abstract class Screen implements IMessageHandler {
	protected final static StringBuffer _tag = new StringBuffer("Screen");
	
	protected FixedSizeArray<GameEntity> _entities = new FixedSizeArray<GameEntity>(2048);
	protected int _type = ScreenType.UNKNOWN;
	protected ScreenData _screenData = new ScreenData();
	// Music that plays on this screen
	protected int _screenMusic = Sound.UNKNOWN;
	// How to handle back button presses on this screen
	protected int _backBtnCtl = ButtonControlType.IGNORE;
	// How to handle menu button presses on this screen
	protected int _menuBtnCtl = ButtonControlType.IGNORE;
	// current screen level
	protected int _screenLevel;
	
	// these entities will not be paused when the screen is paused
	protected FixedSizeArray<GameEntity> _noPauseEntities = new FixedSizeArray<GameEntity>(2048);
	// these behaviors will not be paused when the screen is paused, regardless of the entity they are attached to
	protected long _noPauseBehaviorTypes;
	
 	public int getType() {
		return _type;
	}
	
	public ScreenData getScreenData() {
		return _screenData;
	}
	
	public final void update(float updateRatio, int screenUpdateType) {
		switch (screenUpdateType) {
		case ScreenUpdateType.ACTIVE:
			onActiveUpdate(updateRatio);
			break;
		case ScreenUpdateType.INACTIVE:
			onInactiveUpdate(updateRatio);
			break;
		default:
			Logger.e(_tag, "Bad ScreenUpdateType");
			break;
		}
	}

	public int getBackButtonControl() {
		return _backBtnCtl;
	}
	
	public int getMenuButtonControl() {
		return _menuBtnCtl;
	}
	
	@Override
	public final void handleMessage(Message message) {
		if (message.Type == MessageType.SOUND_ENABLED) {
			if (_screenMusic != Sound.UNKNOWN)
				Manager.Sound.playMusic(_screenMusic);
		}
		
		onHandleMessage(message);
	}
	
	// subscribes to messages, adds entities to entity manager, and enables all behaviors
	public final void init(Object inputData) {
		_screenData.setCode(ScreenCode.CONTINUE);
		Manager.Message.subscribe(this, MessageType.SOUND_ENABLED);
		if (_screenMusic != Sound.UNKNOWN)
			Manager.Sound.playMusic(_screenMusic);
		else
			Manager.Sound.stopMusic();
		onInit(inputData);
		int count = _entities.getCount();
		for (int i = 0; i < count; i++)
			Manager.Entity.addEntity(_entities.get(i));
		enableBehaviors();
	}
	
	public final void pause() {
		onPause();
		Manager.Message.unsubscribe(this, MessageType.SOUND_ENABLED);
		int count = _entities.getCount();
		for (int i = 0; i < count; i++) {
			if (_noPauseEntities.find(_entities.get(i), false) == -1)
				_entities.get(i).pause(_noPauseBehaviorTypes);
		}
	}
	
	public final void unpause() {
		Manager.Message.subscribe(this, MessageType.SOUND_ENABLED);
		if (_screenMusic != Sound.UNKNOWN)
			Manager.Sound.playMusic(_screenMusic);
		
		// clear screen data
		_screenData.setActionScreen(ScreenType.UNKNOWN);
		_screenData.setCode(ScreenCode.CONTINUE);
		_screenData.setInput(null);
		_screenData.setResponse(null);
		
		// unpause entities
		int count = _entities.getCount();
		for (int i = 0; i < count; i++)
			_entities.get(i).unpause();
		
		onUnpause();
	}
	
	public final void close() {
		onClose();
		Manager.Message.unsubscribe(this, MessageType.SOUND_ENABLED);
		int count = _entities.getCount();
		for (int i = 0; i < count; i++)
			Manager.Entity.freeEntity(_entities.get(i));
		_entities.clear();
	}
	
	public final int getScreenLevel() {
		return _screenLevel;
	}
	
	public final void setScreenLevel(int level) {
		if (level < 0 || level > GameSettings.MAX_SCREEN_LEVELS)
			Logger.e(_tag, "Invalid screen level");
		
		_screenLevel = level;
		for (int i = 0; i < _entities.getCount(); i++)
			_entities.get(i).ScreenLevel = level;
	}
	
	// enables all entity behaviors
	protected void enableBehaviors() {
		int count = _entities.getCount();
		for (int i = 0; i < count; i++)
			_entities.get(i).enableBehaviors();
	}

	/******************************************************************
	 * 
	 * functions below should be overriden by child classes
	 * 
	 ******************************************************************/
	
	protected abstract void onInit(Object input);
	
	public void onBackDown() {
		// no default behavior
	}
	
	public void onMenuDown() {
		// no default behavior
	}
	
	protected void onActiveUpdate(float updateRatio) {
		// No default behavior
	}
	
	protected void onInactiveUpdate(float updateRatio) {
		// No default behavior
	}

	// When screen above is popped return data is passed to this method if there is any; 'screenType' contains the type of the popped screen
	protected void onHandleReturnData(int screenType, Object returnData) {
		// No default behavior
	}
	
	protected abstract void onPause();
	
	protected abstract void onUnpause();
	
	protected abstract void onClose();
	
	protected void onHandleMessage(Message message) {
		// No default behavior
	}
}
