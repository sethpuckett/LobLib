package com.game.loblib.utility.area;

import java.security.InvalidParameterException;

import android.util.FloatMath;

import com.game.loblib.exception.UndefinedVertexException;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.MathHelper;
import com.game.loblib.utility.android.AllocationGuard;

public class Vertex extends AllocationGuard implements IArea {
	protected static StringBuffer _tag = new StringBuffer("Vertex");
	public float X = 0f;
	public float Y = 0f;
	public boolean Undefined = false;
	
	public Vertex() {
	}
	
	public Vertex(boolean undefined) {
		Undefined = undefined;
	}
	
	public Vertex(float x, float y) {
		X = x;
		Y = y;
		Undefined = false;
	}
	
	public Vertex(float x, float y, boolean undefined) {
		X = x;
		Y = y;
		Undefined = undefined;
	}
	
	public Vertex(Vertex v) {
		this(v.X, v.Y, v.Undefined);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		Vertex v1 = (Vertex) o;
		
		// if both are undefined return true
		if (this.Undefined && v1.Undefined)
			return true;
		
		if (MathHelper.floatEqZero(X - v1.X) &&
			MathHelper.floatEqZero(Y - v1.Y))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		if (!Undefined) {		
			hash = hash * 31 + Float.floatToIntBits(X);
			hash = hash * 31 + Float.floatToIntBits(Y);
		}
		return hash;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(Undefined ? "Undefined" : "Defined");
		sb.append(") ");
		sb.append(X);
		sb.append(", ");
		sb.append(Y);
		return sb.toString();
	}
	
	public AreaType getType() {
		return AreaType.Vertex;
	}
	
	public void getPosition(Vertex position) {
		position.X = X;
		position.Y = Y;
	}
	
	public float getPositionX() {
		return X;
	}
	
	public float getPositionY() {
		return Y;
	}
	
	public void setPosition(Vertex position) {
		Undefined = position.Undefined;
		X = position.X;
		Y = position.Y;
	}
	
	public void setPosition(float x, float y) {
		X = x;
		Y = y;
	}
	
	// Places the the Vertex on the edge of the circle defined by xCenter, yCenter, and radius
	public void setCirclePosition(float xCenter, float yCenter, float radius, float angleOffset) {
		X = xCenter + (radius * (float)Math.cos(angleOffset));
		Y = yCenter + (radius * (float)Math.sin(angleOffset));
	}
	
	public void getCenter(Vertex center) {
		center.X = X;
		center.Y = Y;
	}
	
	public float getCenterX() {
		return X;
	}
	
	public float getCenterY() {
		return Y;
	}
	
	public void setCenter(Vertex center) {
		Undefined = center.Undefined;
		X = center.X;
		Y = center.Y;
	}
	
	public void setCenter(float x, float y) {
		X = x;
		Y = y;
	}
	
	public void getSize(Vertex size) {
		size.X = 1;
		size.Y = 1;
	}
	
	public void setSize(float width, float height) {
		Logger.e(_tag, "Vertex cannot change size");
	}
	
	public void setSize(Vertex size) {
		Logger.e(_tag, "Vertex cannot change size");
	}
	
	public boolean getMaintainCenter() {
		Logger.e(_tag, "Vertex has no center");
		return false;
	}
	
	public void setMaintainCenter(boolean maintain) {
		Logger.e(_tag, "Vertex has no center");
	}
	
	public float getWidth() {
		return 1f;
	}
	
	public float getHeight() {
		return 1f;
	}
	
	// adds vertices. Result is stored in v1
	public static void add(Vertex v1, Vertex v2) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		v1.X += v2.X;
		v1.Y += v2.Y;
	}
	
	// adds vertices. Result is stored in result
	public static void add(Vertex v1, Vertex v2, Vertex result) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		result.X = v1.X + v2.X;
		result.Y = v1.Y + v2.Y;
		result.Undefined = false;
	}
	
	// subtracts v2 from v1. Result is stored in v1
	public static void sub(Vertex v1, Vertex v2) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		v1.X -= v2.X;
		v1.Y -= v2.Y;
	}
	
	// subtracts v2 from v1. Result is stored in result
	public static void sub(Vertex v1, Vertex v2, Vertex result) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		result.X = v1.X - v2.X;
		result.Y = v1.Y - v2.Y;
		result.Undefined = false;
	}
	
	// multiplies vertex with scalar. Result is stored in v1
	public static void mul(Vertex v1, float s) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		
		v1.X *= s;
		v1.Y *= s;
	}
	
	// multiplies vertex with scalar. Result is stored in result
	public static void mul(Vertex v1, float s, Vertex result) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		
		result.X = v1.X * s;
		result.Y = v1.Y * s;
		result.Undefined = false;
	}
	
	// returns magnitude of vertex. This requires a square root operation; use magnituedSquared() if possible
	public static float magnitude(Vertex v1) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		
		return (float)Math.sqrt(Math.pow(v1.X, 2) + Math.pow(v1.Y, 2));
	}
	
	// returns magnitude squared of a vertex
	public static float magnitudeSquared(Vertex v1) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		
		return (float)(Math.pow(v1.X, 2) + Math.pow(v1.Y, 2));
	}
	
	// normalize vertex
	public static void normalize(Vertex v1) throws UndefinedVertexException {
		if (v1.Undefined)
			return;
			//undefinedErr("v1");

		boolean inverse = false;
		if (v1.X < 0 && !v1.Undefined)
			inverse = true;
		
		if (MathHelper.floatEqZero(v1.X) && MathHelper.floatEqZero(v1.Y)) {
			v1.Undefined = true;
		}
		else if (MathHelper.floatEqZero(v1.X)) {
			if (v1.Y > 0) {
				v1.X = 0;
				v1.Y = 1;
			}
			else {
				v1.X = 0;
				v1.Y = -1;
			}
		}
		else if (MathHelper.floatEqZero(v1.Y)) {
			if (v1.X > 0) {
				v1.X = 1;
				v1.Y = 0;
			}
			else {
				v1.X = -1;
				v1.Y = 0;
			}
		}
		else {
			float slope = v1.Y / v1.X;
			float magnitude = FloatMath.sqrt(1f + (float)Math.pow(slope, 2));
			v1.X = (1f / magnitude);
			v1.Y = (slope / magnitude);
		}
		
		if (!v1.Undefined && inverse)
			Vertex.mul(v1, -1f);	
	}
	
	// normalize vertex. Result stored in result
	public static void normalize(Vertex v1, Vertex result) throws UndefinedVertexException {
		if (v1.Undefined)
			return;//undefinedErr("v1");
		Area.sync(result, v1);
		normalize(result);
	}
	
	// normalize vertex defined by v1 and v2. Result stored in result.
	public static void normalize(Vertex v1, Vertex v2, Vertex result) throws UndefinedVertexException {
		if (v1.Undefined || v2.Undefined) {
			result.Undefined = true;
			return;
		}
		
		float yDiff = v2.Y - v1.Y;
		float xDiff = v2.X - v1.X;
		
		if (MathHelper.floatEqZero(xDiff) && MathHelper.floatEqZero(yDiff)) {
			result.Undefined = true;
		}
		else if (MathHelper.floatEqZero(xDiff)) {
			result.Undefined = false;
			if (yDiff > 0) {
				result.X = 0;
				result.Y = 1;
			}
			else {
				result.X = 0;
				result.Y = -1;
			}
		}
		else if (MathHelper.floatEqZero(yDiff)) {
			result.Undefined = false;
			if (xDiff > 0) {
				result.X = 1;
				result.Y = 0;
			}
			else {
				result.X = -1;
				result.Y = 0;
			}
		}
		else {
			float slope = yDiff / xDiff;
			float magnitude = FloatMath.sqrt(1f + (float)Math.pow(slope, 2));
			result.X = (1f / magnitude);
			result.Y = (slope / magnitude);
			result.Undefined = false;
		}
		
		if (xDiff < 0f && yDiff != 0 && !result.Undefined)
			Vertex.mul(result, -1f);
	}

	// returns distance squared between two points
	public static float distanceSquared(Vertex v1, Vertex v2) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		return (float)(Math.abs(Math.pow((v1.X - v2.X), 2) + Math.pow(v1.Y - v2.Y, 2)));
	}
	
	// returns distance between two points
	public static float distance(Vertex v1, Vertex v2) throws UndefinedVertexException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		return (float)Math.sqrt((Math.abs(Math.pow((v1.X - v2.X), 2) + Math.pow(v1.Y - v2.Y, 2))));
	}
	
	// calculates the angle between the line defined by the vertices and (1, 0)
	public static float directionAngle(Vertex v1, Vertex v2) throws UndefinedVertexException, InvalidParameterException {
		if (v1.Undefined)
			undefinedErr("v1");
		if (v2.Undefined)
			undefinedErr("v2");
		
		double angle = 0;
		float xDelta = v2.X - v1.X;
		float yDelta = v2.Y - v1.Y;
		
		if (MathHelper.floatEqZero(xDelta) && MathHelper.floatEqZero(yDelta))
			throw new InvalidParameterException("Points are the same");
		
		// No X change?
		if (MathHelper.floatEqZero(xDelta)) {
			//line goes up Y axis
			if (v2.Y - v1.Y > 0)
				angle = Math.PI * .5d;
			//line goes down Y axis
			else
				angle = Math.PI * 1.5d;
		}
		// No Y change?
		else if (MathHelper.floatEqZero(yDelta)) {
			//line goes up X axis
			if (v2.X - v1.X > 0)
				angle = 0;
			//line goes down X axis
			else
				angle = Math.PI;
		}
		// X and Y change
		else {
			double ror = ((double)v2.Y - (double)v1.Y) / ((double)v2.X - (double)v1.X);
			angle = Math.atan(ror);
		}
		
		return (float)angle;		
	}
	
	// calculates the angle between the line defined by the vertex and (1, 0)
	public static float directionAngle(Vertex v1) throws UndefinedVertexException, InvalidParameterException {
		if (v1.Undefined)
			undefinedErr("v1");
		
		double angle = 0;
		
		if (MathHelper.floatEqZero(v1.X) && MathHelper.floatEqZero(v1.Y))
			throw new InvalidParameterException("zero vertex is invalid");
		
		// No X change?
		if (MathHelper.floatEqZero(v1.X)) {
			//line goes up Y axis
			if (v1.Y > 0)
				angle = Math.PI * .5d;
			//line goes down Y axis
			else
				angle = Math.PI * 1.5d;
		}
		// No Y change?
		else if (MathHelper.floatEqZero(v1.Y)) {
			//line goes up X axis
			if (v1.X > 0)
				angle = 0;
			//line goes down X axis
			else
				angle = Math.PI;
		}
		// X and Y change
		else {
			angle = Math.atan2(v1.Y, v1.X - 1);
			if (angle < 0)
				angle -= (Math.PI * 2d);
		}
		
		return (float)angle;		
	}
	
	protected static void undefinedErr(String arg) throws UndefinedVertexException {
		UndefinedVertexException e = new UndefinedVertexException(arg);
		Logger.e(_tag, "undefined vertex", e);
		throw e;
	}
}
