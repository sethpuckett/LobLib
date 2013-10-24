package com.game.loblib.entity;

import com.game.loblib.graphics.Sprite;
import com.game.loblib.utility.area.Rectangle;
import com.game.loblib.utility.area.Vertex;

public class EntityAttributes {
	// Orientation
	public final Rectangle Area = new Rectangle();
	
	// Movement/Physics
	public final Vertex Destination = new Vertex();
	public final Vertex Direction = new Vertex();
	public float Speed = 0f;
	
	// Drawing
	public Sprite Sprite = null;
}
