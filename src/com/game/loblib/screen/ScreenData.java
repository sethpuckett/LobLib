package com.game.loblib.screen;

public class ScreenData {
	// Indicates to screen manager that some action should be taken (e.g. push, pop)
	protected int _screenCode;
	// If a screen is to be pushed or popped this field determines the screen type
	protected int _actionScreen;
	// When the current screen is popped this value will be returned to the next screen on the stack
	protected Object _screenResponse;
	// When pushing or transfering to a new screen this value will be passed to the init() method of the new screen
	protected Object _screenInput;
	
	public int getCode() { return _screenCode; }
	public void setCode(int code) { _screenCode = code; }
	
	public int getActionScreen() { return _actionScreen; }
	public void setActionScreen(int screen) { _actionScreen = screen; }
	
	public Object getResponse() { return _screenResponse; }
	public void setResponse(Object response) { _screenResponse = response; }
	
	public Object getInput() { return _screenInput; }
	public void setInput(Object input) { _screenInput = input; }
}
