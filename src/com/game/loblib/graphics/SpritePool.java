package com.game.loblib.graphics;

import com.game.loblib.utility.android.TObjectPool;

public class SpritePool extends TObjectPool<Sprite> {
	protected StringBuffer _tag;
	
	public SpritePool(int size) {
		super(size);
		_tag = new StringBuffer("SpritePool");
		fill();
	}
	
	@Override
	protected void fill() {
		int size = getSize();
		for (int x = 0; x < size; x++) {
			getAvailable().add(new Sprite());
		}
	}

}