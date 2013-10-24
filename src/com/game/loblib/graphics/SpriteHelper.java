package com.game.loblib.graphics;

import com.game.loblib.utility.Logger;

import com.game.loblib.graphics.Sprite;
import com.game.loblib.utility.Global;

// Helper class for loading specific images
public class SpriteHelper {
	protected static final StringBuffer _tag = new StringBuffer("SpriteHelper");
	
	public void setupSprite(Sprite s, int image) {
		switch (image) {
		default:
			Logger.e(_tag, "Invalid Image");
			break;
		}
		Global.View.bindTexture(s.Texture);
	}
}