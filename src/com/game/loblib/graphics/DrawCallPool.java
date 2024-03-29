package com.game.loblib.graphics;

import com.game.loblib.utility.android.TObjectPool;

public class DrawCallPool extends TObjectPool<DrawCall> {
	protected StringBuffer _tag;
	
	public DrawCallPool(int size) {
		super(size);
		_tag = new StringBuffer("DrawCallPool");
		fill();
	}
	
	@Override
	protected void fill() {
		int size = getSize();
		for (int x = 0; x < size; x++) {
			getAvailable().add(new DrawCall());
		}
	}

}
