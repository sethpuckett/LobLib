package com.game.loblib.screen;

public class ScreenCode {
	public final static int UNKNOWN = 					0;
	public final static int CONTINUE =					1; // continue updating current screen
	public final static int TRANSITION =				2; // pop current screen and replace with another
	public final static int TRANSITION_ALL =			3; // pop all screens and replace with another
	public final static int PUSH =						4; // push new screen to top of stack
	public final static int POP =						5; // pop current screen from top of stack
}
