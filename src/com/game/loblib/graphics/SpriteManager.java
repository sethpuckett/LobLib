package com.game.loblib.graphics;

import com.game.loblib.utility.Global;
import com.game.loblib.utility.android.FixedSizeArray;
import com.game.loblib.utility.area.Intersection;

public class SpriteManager {
	protected static final StringBuffer _tag = new StringBuffer("SpriteManager");
	protected SpritePool _spritePool;
	protected DrawCallPool _drawCallPool;
	protected FixedSizeArray<Sprite> _allocatedSprites;
	protected FixedSizeArray<DrawCall>[] _spriteQueues;
	protected SpriteSet[] _spriteSets;
	protected int _spriteQueueIndex;
	protected float _updateTime = 0;

	@SuppressWarnings("unchecked")
	public SpriteManager(SpriteSet set1, SpriteSet set2) {
		_spriteQueues = new FixedSizeArray[2];
		_spriteQueues[0] = new FixedSizeArray<DrawCall>(1024);
		_spriteQueues[1] = new FixedSizeArray<DrawCall>(1024);
		_allocatedSprites = new FixedSizeArray<Sprite>(2048);
		_spritePool = new SpritePool(2048);
		_drawCallPool = new DrawCallPool(2048);
		_spriteSets = new SpriteSet[2];
		_spriteSets[0] = set1;
		_spriteSets[1] = set2;
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
	public void draw(Sprite sprite, int layer) {
		// lock the current sprite queue to prevent conflicts
		synchronized (_spriteQueues[_spriteQueueIndex]) {
			sprite.update(_updateTime);
			// don't draw the sprite if it is off screen
			if (!sprite.UseCamera || Intersection.check(Global.Camera.CameraArea, sprite.Area)) {
				// make a draw call with the current sprite and add it to the sprite queue
				_spriteQueues[_spriteQueueIndex].add(makeDrawCall(sprite));
				// add the sprite queue index of the draw call to the current sprite set
				_spriteSets[_spriteQueueIndex].addIndex(_spriteQueues[_spriteQueueIndex].getCount() - 1, layer);
			}
		}
	}

	// Sends the current sprite queue/set to the renderer and prepares the next queue/set for the next frame
	public void frameComplete() {
		Global.Renderer.setDrawQueue(_spriteQueues[_spriteQueueIndex], _spriteSets[_spriteQueueIndex]);
		_spriteQueueIndex = ~_spriteQueueIndex & 1;
		int count = _spriteQueues[_spriteQueueIndex].getCount();
		for (int i = 0; i < count; i++)
			_drawCallPool.release(_spriteQueues[_spriteQueueIndex].get(i));
		_spriteQueues[_spriteQueueIndex].clear();
		_spriteSets[_spriteQueueIndex].clear();
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
}
