package com.game.loblib.input;

public interface ITouchListener {
	public void onTouchEvent(GameMotionEvent event);
	public void onTouchAbort(GameMotionEvent event);
}
