package com.game.loblib.input;

import com.game.loblib.utility.Global;
import com.game.loblib.utility.android.AllocationGuard;
import com.game.loblib.utility.area.Vertex;

// Represents a discrete instance of a player touching/swiping the screen
public class GameMotionEvent extends AllocationGuard {
	// Coordinates of touch event relative to the screen
	public Vertex ScreenCoords = new Vertex();
	// Coordinates of touch event relative to current camera position
	public Vertex CameraCoords = new Vertex();
	public int Type = MotionType.UNKNOWN;
	public boolean Primary = false;
	public int PointerId = 0;
	
	public GameMotionEvent() {
	}
	
	public void init(float x, float y, int type, boolean primary, int pointerId) {
		ScreenCoords.X = x;
		ScreenCoords.Y = Global.Renderer.Height - y;
		CameraCoords.X = ScreenCoords.X + Global.Camera.CameraArea.Position.X;
		CameraCoords.Y = ScreenCoords.Y + Global.Camera.CameraArea.Position.Y;
		Type = type;
		Primary = primary;
		PointerId = pointerId;
	}
}
