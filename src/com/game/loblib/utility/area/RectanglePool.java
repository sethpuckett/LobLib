package com.game.loblib.utility.area;

import com.game.loblib.utility.android.TObjectPool;

public class RectanglePool extends TObjectPool<Rectangle> {
	protected StringBuffer _tag;
	
	public RectanglePool(int size) {
		super(size);
		_tag = new StringBuffer("RectanglePool");
		fill();
	}
	
	@Override
	protected void fill() {
		int size = getSize();
		for (int x = 0; x < size; x++) {
			getAvailable().add(new Rectangle());
		}
	}
}
