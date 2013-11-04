package com.game.loblib.graphics;

import com.game.loblib.utility.android.TObjectPool;

public class TextDrawCallPool extends TObjectPool<TextDrawCall>  {
	protected StringBuffer _tag = new StringBuffer("TextDrawCallPool");
	
	public TextDrawCallPool(int size) {
		super(size);
		fill();
	}
	
	@Override
	protected void fill() {
		int size = getSize();
		for (int x = 0; x < size; x++) {
			getAvailable().add(new TextDrawCall());
		}
	}
}
