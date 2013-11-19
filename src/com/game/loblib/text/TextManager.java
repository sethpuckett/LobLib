package com.game.loblib.text;

import java.util.Arrays;

import com.game.loblib.utility.Global;
import com.game.loblib.utility.Logger;

public class TextManager {
	protected static final StringBuffer _tag = new StringBuffer("SpriteManager");
	
	protected final static int MAX_TEXT = 32;
	
	protected TextData[] _textArray;
	protected int _textIndex;
	
	public TextManager() {
		_textArray = new TextData[MAX_TEXT];
		_textIndex = 0;
	}
	
	public TextData addText(String name, String assetFile, int size, int padX, int padY) {
		GLText newText = Global.Renderer.loadText(assetFile, size, padX, padY);
		TextData newTextData = new TextData(_textIndex, name, newText, assetFile, size, padX, padY);
		_textArray[_textIndex] = newTextData;
		_textIndex++;
		return newTextData;
	}
	
	public TextData getText(int id) {
		if (id < 0 || id >= _textArray.length)
			Logger.e(_tag, "text id out of range");
		return _textArray[_textIndex];
	}
	
	public TextData getText(String name) {
		if (name == null || name == "")
			Logger.e(_tag, "name cannot be null or empty");
		
		for (int i = 0; i < _textArray.length; i++) {
			if (_textArray[i] != null && _textArray[i].getName().equals(name))
				return _textArray[i];
		}
		
		Logger.e(_tag, "cannot find text with this name:" + name);
		return null;
	}
	
	public int getTextCount() {
		return _textIndex;
	}
}
