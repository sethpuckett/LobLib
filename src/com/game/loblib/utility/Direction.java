package com.game.loblib.utility;

import java.util.Random;

public class Direction {
	public static final int UNKNOWN = 0;
	public static final int NONE = 1;
	public static final int LEFT = 2;
	public static final int UP = 3;
	public static final int RIGHT = 4;
	public static final int DOWN = 5;
	
	public static final int LEFT_UP = 6;
	public static final int RIGHT_UP = 7;
	public static final int RIGHT_DOWN = 8;
	public static final int LEFT_DOWN = 9;
	
	public static int GetRandomDirection(Random rand) {
		return MathHelper.GetRandomValueFromSet(rand, LEFT, UP, RIGHT, DOWN, LEFT_UP, RIGHT_UP, RIGHT_DOWN, LEFT_DOWN);
	}

	public static int GetRandomCardinalDirection(Random rand) {
		return MathHelper.GetRandomValueFromSet(rand, LEFT, UP, RIGHT, DOWN);
	}
}
