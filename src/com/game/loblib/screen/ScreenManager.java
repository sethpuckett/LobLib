package com.game.loblib.screen;

import com.game.loblib.utility.ButtonControlType;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.AllocationGuard;

public class ScreenManager {
	protected static final StringBuffer _tag = new StringBuffer("ScreenManager");
	
	protected Screen _active;
	
	public ScreenManager() {
		
	}
	
	public void init() {
		// Nothing to do
	}
	
	// Update active screen and handle screen code (if there is one)
	public void update(float updateRatio) {
		if (_active != null) {
			_active.update(updateRatio);
			int code = _active.getCode();
			if (code != ScreenCode.CONTINUE)
				handleCode(code);
		}
	}

	// Handles back button presses
	public boolean onBackDown() {
		boolean ret = false;
		
		if (_active != null) {
			int ctl = _active.getBackButtonControl();
			if (ctl == ButtonControlType.OVERRIDE) {
				_active.onBackDown();
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
		
		if (_active != null) {
			int ctl = _active.getMenuButtonControl();
			if (ctl == ButtonControlType.OVERRIDE) {
				_active.onMenuDown();
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
	
	// Ensure that transition is valid and begin transition process
	private void handleCode(int code) {
		// Disable allocation guard to perform cleanup during screen transition
		AllocationGuard.sGuardActive = false;
		Manager.Sound.close();
		Manager.Sound.init();

		onHandleCode(code);
		
		// Run garbage collector during downtime
		Runtime.getRuntime().gc();
		AllocationGuard.sGuardActive = true;
	}
}