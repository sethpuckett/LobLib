package com.game.loblib.graphics;

// Represents texture data loaded from resources
public class Texture {
	// Managed resource ID
	public int ResourceId = 0;
	// ID used to reference texture in-app
	public int TextureId = 0;
	// True if texture data has been unloaded and needs to be bound before use
	public boolean Dirty = true;
}
