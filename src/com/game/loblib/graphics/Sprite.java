package com.game.loblib.graphics;

import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.AllocationGuard;
import com.game.loblib.utility.android.FixedSizeArray;
import com.game.loblib.utility.area.Rectangle;

// Stores and manages properties related to drawing static and animated textures to the screen
public class Sprite extends AllocationGuard {
	protected static final StringBuffer _tag = new StringBuffer("Sprite");
	
	// Current animation frame
	protected int _currentFrame = 0;
	// Time until animation frame switch
	protected float _timeLeft = 0;
	// True if sprite is currently animated
	protected boolean _animationPlaying = false;
	// Array of arrays containing all animations and their frames associated with this sprite
	protected FixedSizeArray<int[]> _animationFrames;
	// The currently playing animation
	protected int _currentAnimation = 0;
	// The frame index of the currently playing animation
	protected int _currentAnimationIndex = 0;
	
	
	public Texture Texture = new Texture();
	public Rectangle Area = new Rectangle();
	public float Alpha = 1f;
	// Type of sprite (static, animated)
	public int Type = SpriteType.STATIC;
	// Determines speed of animation
	public float FrameRate = -1f;
	// Frames associated with this sprite- each frame contains 4 values: X, Y, width, height; used for static sprites and simple animations
	public int[] Frames;
	// Frame index of currently drawn frame
	public int FrameOffset = 0;
	// Number of frames associated with this sprite
	public int FrameCount = 1;
	// If false sprite position will be absolute on the screen, and not affected by camera movement
	public boolean UseCamera = true;
	
	public Sprite() {
	}
	
	public void update(float updateTime) {
		// If the sprite is not currently animated there is nothing to update
		if (Type == SpriteType.STATIC || Type == SpriteType.UNKNOWN || !_animationPlaying)
			return;
		else {
			// Update the frame based on update time, frame rate, and time left
			_timeLeft -= updateTime;
			if (_timeLeft <= 0) {
				int frameJump = 0;
				while (_timeLeft <= 0) {
					frameJump++;
					_timeLeft += (1000f / FrameRate);
				}
				
				if (_animationFrames == null)
					updateSimpleAnimation(frameJump);
				else
					updateComplexAnimation(frameJump);
				
				FrameOffset = _currentFrame * 4;
			}
		}
	}
	
	// Activate animation if sprite properties allow it
	public void animate() {
		if (Type == SpriteType.UNKNOWN || 
			Type == SpriteType.STATIC ||
			FrameRate <= 0 ||
			FrameCount < 2)
			Logger.e(_tag, "Cannot animate; invalid state");
		else {
			_animationPlaying = true;
			_timeLeft = 0f;
		}
	}
	
	// Set currently drawn frame index
	public void setFrame(int frame) {
		if (_animationFrames == null) {
			if (frame < FrameCount) {
				_currentFrame = frame;
				FrameOffset = frame * 4;
			}
			else
				Logger.e(_tag, "cannot set frame; out of range");
		}
		else {
			if (frame < _animationFrames.get(_currentAnimation).length) {
				_currentAnimationIndex = frame;
				_currentFrame = _animationFrames.get(_currentAnimation)[frame];
				FrameOffset = _currentFrame * 4;
			}
			else
				Logger.e(_tag, "cannot set frame; out of range");
		}
	}

	// Returns currently draw frame index
	public int getFrame() {
		if (_animationFrames == null)
			return _currentFrame;
		else
			return _currentAnimationIndex;
	}
	
	// Returns width of currently drawn frame
	public int getFrameWidth() {
		return Frames[FrameOffset + 2];
	}
	
	// Returns height of currently drawn frame 
	public int getFrameHeight() {
		return Frames[FrameOffset + 1];
	}
	
	// Stops currently playing animation
	public void stopAnimation() {
		_animationPlaying = false;
	}
	
	// returns true if the sprite is in a drawable state
	public boolean isValid() {
		if (!Texture.Dirty &&
			Texture.ResourceId != 0 &&
			Texture.TextureId != 0 &&
			Area.getWidth() > 0f &&
			Area.getHeight() > 0f)
			return true;
		else
			return false;
	}

	// Add a set of frames for an animation
	public void addAnimation(int[] frames) {
		if (_animationFrames == null) {
			_animationFrames = new FixedSizeArray<int[]>(8);
		}
		_animationFrames.add(frames);
	}
	
	// Set currently playing animation index
	public void setAnimation(int animation) {
		setAnimation(animation, false);
	}
	
	// Set currently playing animation index, and optionally restart the animation from the beginning
	public void setAnimation(int animation, boolean restart) {
		if (_animationFrames == null || animation >= _animationFrames.getCount()) {
			Logger.e(_tag, "Invalid animation index");
			animation = 0;
		}
		else
			_currentAnimation = animation;
		
		if (restart || _animationFrames.get(_currentAnimation).length <= _currentAnimationIndex)
			setFrame(0);		
	}

	// Reset all sprite properties
	public void reset() {
		_currentFrame = 0;
		_timeLeft = 0;
		_animationPlaying = false;
		_animationFrames = null;
		_currentAnimation = 0;
		_currentAnimationIndex = 0;
		Texture.Dirty = true;
		Texture.ResourceId = 0;
		Texture.TextureId = 0;
		Area.MaintainCenter = true;
		Area.setPosition(0, 0);
		Area.setSize(0, 0);
		Alpha = 1f;
		Type = SpriteType.STATIC;
		FrameRate = 1f;
		Frames = null;
		FrameOffset = 0;
		FrameCount = 1;
		UseCamera = true;
	}
	
	// Progresses a simple animation the number of frames indicated by frameJump
	protected void updateSimpleAnimation(int frameJump) {
		// Single animations play once and then stop on the final frame
		if (Type == SpriteType.SINGLE_ANIMATION) {
			if (_currentFrame + frameJump >= FrameCount) {
				_currentFrame = FrameCount - 1;
				_animationPlaying = false;
				Manager.Message.sendMessage(MessageType.ANIMATION_FINISHED, this);
			}
			else 
				_currentFrame += frameJump;
		}
		// Otherwise update current frame and roll back to start of animation if necessary
		else {
			_currentFrame += frameJump;
			while (_currentFrame >= FrameCount)
				_currentFrame -= FrameCount;
		}
	}
	
	// Progresses a complex animation the number of frames indicated by frameJump
	protected void updateComplexAnimation(int frameJump) {
		int[] frames = _animationFrames.get(_currentAnimation);
		if (Type == SpriteType.SINGLE_ANIMATION) {
			if (_currentAnimationIndex + frameJump >= frames.length) {
				_currentAnimationIndex = frames.length -1;
				_currentFrame = frames[_currentAnimationIndex];
				_animationPlaying = false;
				Manager.Message.sendMessage(MessageType.ANIMATION_FINISHED, this);
			}
			else {
				_currentAnimationIndex += frameJump;
				_currentFrame = frames[_currentAnimationIndex];
			}
		}
		else {
			_currentAnimationIndex += frameJump;
			while (_currentAnimationIndex >= frames.length) {
				_currentAnimationIndex -= frames.length;
			}
			_currentFrame = frames[_currentAnimationIndex];
		}
	}


}


