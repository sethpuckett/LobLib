package com.game.loblib.graphics;

import com.game.loblib.utility.Logger;

// Stores which layer draw calls should be applied to based on sprite queue index
public class SpriteSet {
	protected static StringBuffer _tag = new StringBuffer("SpriteSet");
	
	protected final int _layerCount = 12;
	protected final int _spritesPerLayer = 1024;
	
	protected int[][] _layers = new int[_layerCount][];
	protected int[] _layerIndexCount = new int[_layerCount];
	
	// add a sprite queue index to a particular layer
	public void addIndex(int index, int layer) {
		if (layer > _layerCount)
		{
			Logger.e(_tag, "layer out of range");
			return;
		}
		
		// initialize this layer's set if it hasn't been yet
		if (_layers[layer] == null)
			_layers[layer] = new int[_spritesPerLayer];
		
		// set the index according to the proper index count for this layer, and increment the index count
		_layers[layer][_layerIndexCount[layer]++] = index;
	}

	// clears sprite set of all draw calls
	public void clear() {
		for (int i = 0; i < _layerCount; i++)
			_layerIndexCount[i] = 0;
	}

	// returns all draw call indices for a particular layer
	public int[] getLayer(int layer) {
		if (layer > _layerCount)
		{
			Logger.e(_tag, "layer out of range");
			return null;
		}
		
		// initialize this layer's set if it hasn't been yet
		if (_layers[layer] == null)
			_layers[layer] = new int[_spritesPerLayer];
		
		return _layers[layer];
	}

	// gets the number of draw calls currently associated with a particular layer
	public int getSpriteCount(int layer) {
		if (layer > _layerCount)
		{
			Logger.e(_tag, "layer out of range");
			return 0;
		}
		
		return _layerIndexCount[layer];
	}
	
	public int getLayerCount() {
		return _layerCount;
	}
}
