package com.game.loblib.graphics;

import java.util.Comparator;

// Deteremines if two textures are the same
public class TextureComparator implements Comparator<Texture> {
	@Override
	public int compare(Texture t1, Texture t2) {
		return t2.ResourceId - t1.ResourceId;
	}
}
