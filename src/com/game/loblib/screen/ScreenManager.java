package com.game.loblib.screen;

import com.game.loblib.messaging.IMessageHandler;
import com.game.loblib.messaging.Message;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.ButtonControlType;
import com.game.loblib.utility.GameSettings;
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
					handleScreenData(_screenStack.get(i).getScreenData());
			}	
		}
	}
	
	public void handleMessage(Message message) {
		// when game is initialized or screen size is set for the first time load the boostrap screen
		if (message.Type == MessageType.GAME_INIT) {
			// don't create screen if renderer is not initialized yet
			if (Global.Renderer.Width > 0) {
				if (_screenStack.getCount() > 0)
					Logger.e(_tag, "screen stack should be empty on initialization");
				_screenStack.add(Global.ScreenFactory.create(_boostrapScreen));
				getActiveScreen().init(null);
			}
			else
				Manager.Message.subscribe(this, MessageType.SCREEN_SIZE_SET);
		}
		else if (message.Type == MessageType.SCREEN_SIZE_SET) {
			Manager.Message.unsubscribe(this, MessageType.SCREEN_SIZE_SET);
			
			if (_screenStack.getCount() > 0)
				Logger.e(_tag, "screen stack should be empty on initialization");
			_screenStack.add(Global.ScreenFactory.create(_boostrapScreen));
			getActiveScreen().init(null);
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
	protected void onHandleScreenData(ScreenData data) {
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
	private void handleScreenData(ScreenData data) {
		// Disable allocation guard to perform cleanup during screen transition
		AllocationGuard.sGuardActive = false;
		Manager.Sound.close();
		Manager.Sound.init();
		int screenLevel = 0;

		switch (data.getCode()) {
		case ScreenCode.PUSH:
			// add screen to top of stack and init with any input data
			screenLevel = getActiveScreen().getScreenLevel();
			if (screenLevel + 1 > GameSettings.MAX_SCREEN_LEVELS)
				Logger.e(_tag, "Max screen levels exceded");
			getActiveScreen().pause();
			_screenStack.add(Global.ScreenFactory.create(data._actionScreen));
			getActiveScreen().init(data.getInput());
			getActiveScreen().setScreenLevel(screenLevel + 1);
			break;
		case ScreenCode.POP:
			// remove top screen and pass any return data to next screen on stack
			int oldType = getActiveScreen().getType();
			getActiveScreen().close();
			_screenStack.removeLast();
			getActiveScreen().unpause();
			if (data.getResponse() != null)
				getActiveScreen().onHandleReturnData(oldType, data.getResponse());
			break;
		case ScreenCode.TRANSITION:
			// remove top screen and add new top screen and init with any input data
			screenLevel = getActiveScreen().getScreenLevel();
			getActiveScreen().close();
			_screenStack.removeLast();
			_screenStack.add(Global.ScreenFactory.create(data._actionScreen));
			getActiveScreen().init(data.getInput());
			getActiveScreen().setScreenLevel(screenLevel);
			break;
		case ScreenCode.TRANSITION_ALL:
			// remove all screens and add new top screen and init with any input data
			while (_screenStack.getCount() > 0) {
				getActiveScreen().close();
				_screenStack.removeLast();
			}
			_screenStack.add(Global.ScreenFactory.create(data._actionScreen));
			getActiveScreen().init(data.getInput());
		}
		
		// TODO: should this come before or after parent logic?
		onHandleScreenData(data);
		
		// Run garbage collector during downtime
		Runtime.getRuntime().gc();
		AllocationGuard.sGuardActive = true;
	}
}