package com.game.loblib.collision;

import com.game.loblib.utility.area.IArea;

// Implementers of this interface can be used to test for collisions
public interface ICollisionSender {
	long getLayers();
	IArea getArea();
}
