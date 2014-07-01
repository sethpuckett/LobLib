package com.game.loblib.screen;

import com.game.loblib.utility.Logger;

//Helper class for loading specific screens
public class ScreenFactory {
	protected static final StringBuffer _tag = new StringBuffer("ScreenFactory");
	
	public Screen create(int screenType) {
		switch (screenType) {
		default:
			Logger.e(_tag, "Invalid Screen Type");
			return null;
		}
	}
}
