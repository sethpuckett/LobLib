package com.game.loblib.sound;

import com.game.loblib.utility.Logger;

public class MusicHelper {
	protected final static StringBuffer _tag = new StringBuffer("MusicHelper");
	
	public int getResourceId(int sound) {
		switch (sound) {
		default:
			Logger.e(_tag, "Invalid sound");
			return 0;
		}
	}
}
