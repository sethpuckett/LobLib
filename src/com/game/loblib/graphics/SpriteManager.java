package com.game.loblib.graphics;

import com.game.loblib.utility.GameSettings;
import com.game.loblib.utility.Global;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.android.FixedSizeArray;
import com.game.loblib.utility.area.Intersection;

public class SpriteManager {
	protected static final StringBuffer _tag = new StringBuffer("SpriteManager");
	// store of available sprite objects
	protected SpritePool _spritePool;
	// store of available draw calls
	protected DrawCallPool _drawCallPool;
	// store of available text draw calls
	protected TextDrawCallPool _textDrawCallPool;
	// list of sprites (from _spritePool) currently in use
	protected FixedSizeArray<Sprite> _allocatedSprites;
	// each element of this array holds a list of draw calls for a single frame
	protected FixedSizeArray<DrawCall>[] _spriteQueues;
	// each element of this array holds a list of text draw calls for a single frame
	protected FixedSizeArray<TextDrawCall>[] _textQueues;
	// first array holds spriteSets for a single frame (sprite sets break draw calls up into layers); second array holds a list of sprite sets for a particular screen 
	protected SpriteSet[][] _spriteSets;
	// used to to swap _spriteQueue, _textQueue, and _spriteSets active frame
	protected int _spriteQueueIndex;
	// time since last update; used to update animations
	protected float _updateTime = 0;

	@SuppressWarnings("unchecked")
	public SpriteManager( ) {
		_spriteQueues = new FixedSizeArray[2];
		_textQueues = new FixedSizeArray[2];
		_spriteQueues[0] = new FixedSizeArray<DrawCall>(1024);
		_spriteQueues[1] = new FixedSizeArray<DrawCall>(1024);
		_textQueues[0] = new FixedSizeArray<TextDrawCall>(32);
		_textQueues[1] = new FixedSizeArray<TextDrawCall>(32);
		_allocatedSprites = new FixedSizeArray<Sprite>(2048);
		_spritePool = new SpritePool(4096);
		_drawCallPool = new DrawCallPool(4096);
		_textDrawCallPool = new TextDrawCallPool(32);
		_spriteSets = new SpriteSet[2][GameSettings.MAX_SCREEN_LEVELS];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < GameSettings.MAX_SCREEN_LEVELS; j++) {
				_spriteSets[i][j] = new SpriteSet();
				_spriteSets[i][j] = new SpriteSet();
			}
		}
	}
	
	// Allocates and sets up a Sprite object based on provided image
	public Sprite make(int image) {
		Sprite sprite = _spritePool.allocate();
		sprite.reset();
		Global.SpriteHelper.setupSprite(sprite, image);
		_allocatedSprites.add(sprite);
		return sprite;
	}

	// Releases an allocated sprite object
	public void release(Sprite sprite) {
		_allocatedSprites.remove(sprite, false);
		_spritePool.release(sprite);
	}

	public void update(float updateTime) {
		_updateTime = updateTime;
	}
	
	// Draws a sprite to a particular layer
	public void draw(Sprite sprite, int screenLevel, int layer) {
		if (screenLevel > GameSettings.MAX_SCREEN_LEVELS)
			Logger.e(_tag, "screenLevel cannot be greater than MAX_SCREENS");
		
		// lock the current sprite queue to prevent conflicts
		synchronized (_spriteQueues[_spriteQueueIndex]) {
			sprite.update(_updateTime);
			// don't draw the sprite if it is off screen
			if (!sprite.UseCamera || Intersection.check(Global.Camera.CameraArea, sprite.Area)) {
				// make a draw call with the current sprite and add it to the sprite queue
				_spriteQueues[_spriteQueueIndex].add(makeDrawCall(sprite));
				// add the sprite queue index of the draw call to the current sprite set at the specified screenLevel
				_spriteSets[_spriteQueueIndex][screenLevel].addIndex(_spriteQueues[_spriteQueueIndex].getCount() - 1, layer);
			}
		}
	}
	
	public void drawText(String textName, String text, float x, float y, boolean centerX, boolean centerY, float width, float red, float green, float blue, float alpha) {
		// lock the current sprite queue to prevent conflicts
		synchronized (_textQueues[_spriteQueueIndex]) {
			// make a draw call with the current sprite and add it to the sprite queue
			_textQueues[_spriteQueueIndex].add(makeTextDrawCall(textName, text, x, y, centerX, centerY, width, red, green, blue, alpha));
		}
	}

	// Sends the current sprite queue/set to the renderer and prepares the next queue/set for the next frame
	public void frameComplete() {
		Global.Renderer.setDrawQueue(_spriteQueues[_spriteQueueIndex], _spriteSets[_spriteQueueIndex]);
		Global.Renderer.setTextQueue(_textQueues[_spriteQueueIndex]);
		_spriteQueueIndex = ~_spriteQueueIndex & 1;
		int count = _spriteQueues[_spriteQueueIndex].getCount();
		for (int i = 0; i < count; i++)
			_drawCallPool.release(_spriteQueues[_spriteQueueIndex].get(i));
		count = _textQueues[_spriteQueueIndex].getCount();
		for (int i = 0; i < count; i++)
			_textDrawCallPool.release(_textQueues[_spriteQueueIndex].get(i));
		_spriteQueues[_spriteQueueIndex].clear();
		_textQueues[_spriteQueueIndex].clear();
		for (int i = 0; i < GameSettings.MAX_SCREEN_LEVELS; i++)
			_spriteSets[_spriteQueueIndex][i].clear();
	}

	public void onPause() {
		// When app is paused textures need to be reloaded
		int count = _allocatedSprites.getCount();
		for (int i = 0; i < count; i++) {
			_allocatedSprites.get(i).Texture.Dirty = true;
		}
	}
	
	public void onResume() {
		// When app is unpaused re-bind textures
		int count = _allocatedSprites.getCount();
		for (int i = 0; i < count; i++) {
			Global.View.bindTexture(_allocatedSprites.get(i).Texture);
		}
	}

	// Free all textures
	public void freeTextures() {
		int count = _allocatedSprites.getCount();
		for (int i = 0; i < count; i++) {
			Sprite s = _allocatedSprites.get(i);
			s.Texture.TextureId = 0;
			s.Texture.Dirty = true;
		}
		Global.Renderer.freeTextures();
	}

	// Returns the number of currently allocated sprites
	public int allocatedSpriteCount() {
		if (_allocatedSprites != null)
			return _allocatedSprites.getCount();
		else
			return 0;
	}

	// Releases all allocated sprites
	public void flush() {
		while (_allocatedSprites.getCount() > 0) {
			Sprite sprite = _allocatedSprites.removeLast();
			_spritePool.release(sprite);
		}
	}

	// Makes a draw call using current sprite properties
	protected DrawCall makeDrawCall(Sprite sprite) {
		DrawCall dc = _drawCallPool.allocate();
		dc.Alpha = sprite.Alpha;
		dc.FrameOffset = sprite.FrameOffset;
		dc.Frames = sprite.Frames;
		dc.Height = sprite.Area.Size.Y;
		dc.PositionX = sprite.Area.Position.X;
		dc.PositionY = sprite.Area.Position.Y;
		dc.TextureId = sprite.Texture.TextureId;
		dc.Width = sprite.Area.Size.X;
		dc.UseCamera = sprite.UseCamera;
		return dc;
	}
	
	// Makes a draw call using current sprite properties
	protected TextDrawCall makeTextDrawCall(String textName, String text, float x, float y, boolean centerX, boolean centerY, float width, float red, float green, float blue, float alpha) {
		TextDrawCall dc = _textDrawCallPool.allocate();
		dc.TextName = textName;
		dc.Text = text;
		dc.PositionX = x;
		dc.PositionY = y;
		dc.CenterX = centerX;
		dc.CenterY = centerY;
		dc.Width = width;
		dc.Red = red;
		dc.Green = green;
		dc.Blue = blue;
		dc.Alpha = alpha;
		return dc;
	}
}
