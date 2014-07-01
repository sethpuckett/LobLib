package com.game.loblib.screen;

import com.game.loblib.messaging.IMessageHandler;
import com.game.loblib.messaging.Message;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.ButtonControlType;
import com.game.loblib.utility.Global;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.AllocationGuard;
import com.game.loblib.utility.android.FixedSizeArray;

public class ScreenManager implements IMessageHandler {
	protected static final StringBuffer _tag = new StringBuffer("ScreenManager");

	protected FixedSizeArray<Screen> _screenStack =  new FixedSizeArray<Screen>(8);
	
	// Initial screen that will be loaded when game is initialized
	protected int _boostrapScreen;
	
	public void init() {
		Manager.Message.subscribe(this, MessageType.GAME_INIT);
	}
	
	// Update active screen and handle screen code (if there is one)
	public void update(float updateRatio) {
		for (int i = 0; i < _screenStack.getCount(); i++) {
			// inactive screens
			if (i < _screenStack.getCount() - 1)
				_screenStack.get(i).update(updateRatio, ScreenUpdateType.INACTIVE);
			// active screen
			else {
				_screenStack.get(i).update(updateRatio, ScreenUpdateType.ACTIVE);
				int code = _screenStack.get(i).getScreenData().getCode();
				if (code != ScreenCode.CONTINUE)
					handleCode(code);
			}	
		}
	}
	
	public void handleMessage(Message message) {
		if (message.Type == MessageType.GAME_INIT) {
			// don't create screen if renderer is not initialized yet
			if (Global.Renderer.Width > 0) {
				if (_screenStack.getCount() > 0)
					Logger.e(_tag, "screen stack should be empty on initialization");
				_screenStack.add(Global.ScreenFactory.create(_boostrapScreen));
				getActiveScreen().init();
			}
			else
				Manager.Message.subscribe(this, MessageType.SCREEN_SIZE_SET);
		}
		else if (message.Type == MessageType.SCREEN_SIZE_SET) {
			Manager.Message.unsubscribe(this, MessageType.SCREEN_SIZE_SET);
			
			if (_screenStack.getCount() > 0)
				Logger.e(_tag, "screen stack should be empty on initialization");
			_screenStack.add(Global.ScreenFactory.create(_boostrapScreen));
			getActiveScreen().init();
		}
	}

	// Handles back button presses
	public boolean onBackDown() {
		boolean ret = false;
		
		if (getActiveScreen() != null) {
			int ctl = getActiveScreen().getBackButtonControl();
			if (ctl == ButtonControlType.OVERRIDE) {
				getActiveScreen().onBackDown();
				ret = true;
			}
			else if (ctl == ButtonControlType.IGNORE)
				ret = true;
		}
		
		return ret;
	}
	
	// Handles menu button presses
	public boolean onMenuDown() {
		boolean ret = false;
		
		if (getActiveScreen() != null) {
			int ctl = getActiveScreen().getMenuButtonControl();
			if (ctl == ButtonControlType.OVERRIDE) {
				getActiveScreen().onMenuDown();
				ret = true;
			}
			else if (ctl == ButtonControlType.IGNORE)
				ret = true;
		}
		
		return ret;
	}

	// Should be overridden by child classes to handle specific screen codes
	protected void onHandleCode(int code) {
		// Nothing to do
	}
	
	// Returns the top screen on the screen stack; or null if no screens on stack
	protected Screen getActiveScreen() {
		if (_screenStack != null && _screenStack.getCount() > 0)
			return _screenStack.get(_screenStack.getCount() - 1);
		else
			return null;
	}
	
	// Ensure that transition is valid and begin transition process
	private void handleCode(int code) {
		// Disable allocation guard to perform cleanup during screen transition
		AllocationGuard.sGuardActive = false;
		Manager.Sound.close();
		Manager.Sound.init();

		switch (code) {
		case ScreenCode.PUSH:
			break;
		case ScreenCode.POP:
			break;
		case ScreenCode.TRANSITION:
			break;
		default:
			onHandleCode(code);
			break;
		}
		
		
		// Run garbage collector during downtime
		Runtime.getRuntime().gc();
		AllocationGuard.sGuardActive = true;
	}
}