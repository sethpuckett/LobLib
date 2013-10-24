package com.game.loblib.utility.area;

public class RectangleManager {
	protected RectanglePool _pool;
	
	public RectangleManager() {
		_pool = new RectanglePool(64);
	}
	
	public Rectangle allocate() {
		Rectangle r = _pool.allocate();
		r.Position.Undefined = false;
		r.Position.X = 0;
		r.Position.Y = 0;
		r.Size.Undefined = false;
		r.Size.X = 0;
		r.Size.Y = 0;
		return r;
	}
	
	public Rectangle allocate(float x, float y, float width, float height) {
		Rectangle r = _pool.allocate();
		r.Position.Undefined = false;
		r.Position.X = x;
		r.Position.Y = y;
		r.Size.Undefined = false;
		r.Size.X = width;
		r.Size.Y = height;
		return r;
	}
	
	public void release(Rectangle r) {
		_pool.release(r);
	}

}
