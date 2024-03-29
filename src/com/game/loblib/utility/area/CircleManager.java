package com.game.loblib.utility.area;

public class CircleManager {
	protected CirclePool _pool;
	
	public CircleManager() {
		_pool = new CirclePool(64);
	}
	
	public Circle allocate() {
		Circle c = _pool.allocate();
		c.Center.Undefined = false;
		c.Center.X = 0;
		c.Center.Y = 0;
		c.Radius = 1f;
		return c;
	}
	
	public Circle allocate(float centerX, float centerY, float radius) {
		Circle c = _pool.allocate();
		c.Center.Undefined = false;
		c.Center.X = centerX;
		c.Center.Y = centerY;
		c.Radius = radius;
		return c;
	}
	
	public void release(Circle c) {
		_pool.release(c);
	}

}
