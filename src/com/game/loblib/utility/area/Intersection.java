package com.game.loblib.utility.area;

import com.game.loblib.exception.UndefinedVertexException;
import com.game.loblib.utility.Direction;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.MathHelper;

public class Intersection {
	protected static Vertex _tempVert1 = new Vertex();
	protected static Vertex _tempVert2 = new Vertex();
	protected static Vertex _tempVert3 = new Vertex();
	
	protected static final StringBuffer _tag = new StringBuffer("Intersection");
	
	public static boolean check(Circle c1, Circle c2) {
		try {
			if (Vertex.distanceSquared(c1.Center, c2.Center) >= Math.pow(c1.Radius + c2.Radius, 2))
				return false;
			else
				return true;
		} catch (UndefinedVertexException e) {
			Logger.e(_tag, "Undefined vertex");
			return false;
		}
	}
	
	public static boolean check(Circle c, Vertex v) {
		try {
			if (Vertex.distanceSquared(c.Center, v) <= Math.pow(c.Radius, 2))
				return true;
			else
				return false;
		} catch (UndefinedVertexException e) {
			Logger.e(_tag, "Undefined vertex");
			return false;
		}
	}
	
	public static boolean check(Rectangle r1, Rectangle r2) {
		float r1Left = r1.Position.X;
		float r1Right = r1.Position.X + r1.Size.X;
		float r1Bottom = r1.Position.Y;
		float r1Top = r1.Position.Y + r1.Size.Y; 
		float r2Left = r2.Position.X;
		float r2Right = r2.Position.X + r2.Size.X;
		float r2Bottom = r2.Position.Y;
		float r2Top = r2.Position.Y + r2.Size.Y;
		
		if (r1Left > r2Right ||
			r1Right < r2Left ||
			r1Bottom > r2Top ||
			r1Top < r2Bottom)
			return false;
		else
			return true;
		
//		if (((r1.Position.X < r2.Position.X &&
//			r1.Position.X + r1.Size.X > r2.Position.X) ||
//			(r1.Position.X < r2.Position.X + r2.Size.X &&
//			r1.Position.X + r1.Size.X > r2.Position.X + r2.Size.X)) &&
//			((r1.Position.Y < r2.Position.Y &&
//			r1.Position.Y + r1.Size.Y > r2.Position.Y) ||
//			(r1.Position.Y < r2.Position.Y + r2.Size.Y &&
//			r1.Position.Y + r1.Size.X > r2.Position.Y + r2.Size.Y)))
//			return true;
//		else 
//			return false;
	}
	
	public static boolean check(Rectangle r, Vertex v) {
		boolean intersect = false;
		if (v.X >= r.Position.X &&
			v.X <= r.Position.X + r.Size.X &&
			v.Y >= r.Position.Y &&
			v.Y <= r.Position.Y + r.Size.Y)
			intersect = true;
		return intersect;
	}
	
	public static boolean check(Rectangle r, Circle c) {
		return false; //TODO: implement
	}
	
	public static boolean check(Vertex v1, Vertex v2) {
		return false; //TODO: implement
	}

	// Checks if the line segment defined by p1 & p2 intersects the line defined by p3 & p4.
	// If true, out1 will contain the point of intersction (Undefined otherwise).
	// Coincident lines return false for simplicity
	public static boolean check(Vertex p1, Vertex p2, Vertex p3, Vertex p4, Vertex out1) {
		float denominator = (p4.Y - p3.Y) * (p2.X - p1.X) - (p4.X - p3.X) * (p2.Y - p1.Y);
		float uaNumerator = (p4.X - p3.X) * (p1.Y - p3.Y) - (p4.Y - p3.Y) * (p1.X - p3.X);
		float ubNumerator = (p2.X - p1.X) * (p1.Y - p3.Y) - (p2.Y - p1.Y) * (p1.X - p3.X);
		float ua = 0f;
		float ub = 0f;
		boolean hit = false;
		
		// not parallel or coincident
		if (!MathHelper.floatEq(denominator, 0f)) {
			ua = uaNumerator / denominator;
			ub = ubNumerator / denominator;
			if (ua > 0f && ua < 1f && ub > 0f && ub < 1f)
				hit = true;
		}
		
		if (hit) {
			out1.Undefined = false;
			out1.X = p1.X + ua * (p2.X - p1.X);
			out1.Y = p1.Y + ua * (p2.Y - p1.Y);
		}
		else
			out1.Undefined = true;
		
		return hit;
	}
	
	// Checks if the line segment defined by p1 & p2 intersects the rectangle r1.
	// If true, col will contain collision information
	// Coincident lines return false for simplicity
	public static boolean check(Vertex p1, Vertex p2, Rectangle r1, RectangleCollision col) {
		col.CollisionCount = 0;
		col.Collision1.Undefined = true;
		col.Collision2.Undefined = true;
		col.CollisionDirection1 = Direction.UNKNOWN;
		col.CollisionDirection2 = Direction.UNKNOWN;
		
		// check left wall
		_tempVert1.X = r1.getPositionX();
		_tempVert1.Y = r1.getPositionY();
		_tempVert2.X = r1.getPositionX();
		_tempVert2.Y = r1.getPositionY() + r1.getHeight();
		if (check(_tempVert1, _tempVert2, p1, p2, _tempVert3)) {
			col.CollisionCount++;
			Area.sync(col.Collision1, _tempVert3);
			col.CollisionDirection1 = Direction.LEFT;
		}
		
		// check top wall
		_tempVert1.X = r1.getPositionX();
		_tempVert1.Y = r1.getPositionY() + r1.getHeight();
		_tempVert2.X = r1.getPositionX() + r1.getWidth();
		_tempVert2.Y = r1.getPositionY() + r1.getHeight();
		if (check(_tempVert1, _tempVert2, p1, p2, _tempVert3)) {
			col.CollisionCount++;
			if (col.CollisionCount == 1) {
				Area.sync(col.Collision1, _tempVert3);
				col.CollisionDirection1 = Direction.UP;
			}
			else {
				Area.sync(col.Collision2, _tempVert3);
				col.CollisionDirection2 = Direction.UP;
			}
		}
		
		// check right wall
		_tempVert1.X = r1.getPositionX() + r1.getWidth();
		_tempVert1.Y = r1.getPositionY();
		_tempVert2.X = r1.getPositionX() + r1.getWidth();
		_tempVert2.Y = r1.getPositionY() + r1.getHeight();
		if (col.CollisionCount < 2 && check(_tempVert1, _tempVert2, p1, p2, _tempVert3)) {
			col.CollisionCount++;
			if (col.CollisionCount == 1) {
				Area.sync(col.Collision1, _tempVert3);
				col.CollisionDirection1 = Direction.RIGHT;
			}
			else {
				Area.sync(col.Collision2, _tempVert3);
				col.CollisionDirection2 = Direction.RIGHT;
			}
		}
		
		// check bottom wall
		_tempVert1.X = r1.getPositionX();
		_tempVert1.Y = r1.getPositionY();
		_tempVert2.X = r1.getPositionX() + r1.getWidth();
		_tempVert2.Y = r1.getPositionY();
		if (col.CollisionCount < 2 && check(_tempVert1, _tempVert2, p1, p2, _tempVert3)) {
			col.CollisionCount++;
			if (col.CollisionCount == 1) {
				Area.sync(col.Collision1, _tempVert3);
				col.CollisionDirection1 = Direction.DOWN;
			}
			else {
				Area.sync(col.Collision2, _tempVert3);
				col.CollisionDirection2 = Direction.DOWN;
			}
		}
		
		return (col.CollisionCount > 0);
	}
	
	
}
