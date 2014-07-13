package com.game.loblib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameSettings {
	protected final static StringBuffer _tag = new StringBuffer("GameSettings");

	protected SharedPreferences _prefs;
	
	public GameSettings() {
		_prefs = Global.Context.getSharedPreferences("game", Context.MODE_PRIVATE);
	}
	
	public boolean getSoundEnabled() {
		return _prefs.getBoolean("soundEnabled", true);
	}
	
	public void setSoundEnabled(boolean enabled) {
		Editor editor = _prefs.edit();
		editor.putBoolean("soundEnabled", enabled);
		editor.commit();
	}
	
	public final static int MAX_SCREEN_LEVELS = 4;
}
