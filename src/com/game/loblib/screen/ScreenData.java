package com.game.loblib.screen;

public class ScreenData {
	// Indicates to screen manager that some action should be taken (e.g. push, pop)
	protected int _screenCode;
	// If a screen is to be pushed or popped this field determines the screen type
	protected int _actionScreen;
	// When the current screen is popped this value will be returned to the next screen on the stack
	protected Object _screenResponse;
	
	public int getCode() { return _screenCode; }
	public void setCode(int code) { _screenCode = code; }
	
	public int getActionScreen() { return _actionScreen; }
	public void setActionScreen(int screen) { _actionScreen = screen; }
	
	public Object getRespones() { return _screenResponse; }
	public void getRespones(Object response) { _screenResponse = response; }
}
