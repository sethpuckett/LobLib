package com.game.loblib.graphics;

import com.game.loblib.utility.Logger;

// Stores which layer draw calls should be applied to based on sprite queue index
public abstract class SpriteSet {
	protected static final StringBuffer _tag = new StringBuffer("SpriteSet");
	
	protected final int _layerCount = 0;
	
	// add a sprite queue index to a particular layer
	public void addIndex(int index, int layer) {

	}

	// clears sprite set of all draw calls
	public void clear() {

	}

	// returns all draw call indices for a particular layer
	public int[] getLayer(int layer) {
		switch (layer) {
		default:
			Logger.e(_tag, "Invalid layer");
			return null;
		}
	}

	// gets the number of draw calls currently associated with a particular layer
	public int getSpriteCount(int layer) {
		switch (layer) {
		default:
			Logger.e(_tag, "Invalid layer");
			return 0;
		}
	}
	
	public int getLayerCount() {
		return _layerCount;
	}
}
