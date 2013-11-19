package com.game.loblib.text;

public class TextData {
	protected int _id;
	protected String _name;
	protected GLText _text;
	protected String _fontName;
	protected int _size;
	protected int _padX;
	protected int _padY;
	
	public TextData(int id, String name, GLText text, String fontName, int size, int padX, int padY) {
		_id = id;
		_name = name;
		_text = text;
		_fontName = fontName;
		_size = size;
		_padX = padX;
		_padY = padY;
	}
	
	public int getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public GLText getText() {
		return _text;
	}
	
	public String getFontName() {
		return _fontName;
	}
	
	public int getSize() {
		return _size;
	}
	
	public int getPadX() {
		return _padX;
	}
	
	public int getPadY() {
		return _padY;
	}
}
