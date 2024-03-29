package com.game.loblib.input;

public class MotionType {
	public static final int UNKNOWN = 				0;
	public static final int ACTION_DOWN = 			1 << 0;
	public static final int ACTION_UP = 			1 << 1;
	public static final int ACTION_MOVE = 			1 << 2;
	public static final int ACTION_POINTER_DOWN = 	1 << 3;
	public static final int ACTION_POINTER_UP = 	1 << 4;
	public static final int ACTION_CANCEL = 		1 << 5;
	
	public static final int ALL = Integer.MAX_VALUE;
}
