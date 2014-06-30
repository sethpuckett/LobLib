package com.game.loblib.graphics;

import com.game.loblib.entity.GameEntity;
import com.game.loblib.messaging.IMessageHandler;
import com.game.loblib.messaging.Message;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.MathHelper;
import com.game.loblib.utility.area.Area;
import com.game.loblib.utility.area.Rectangle;
import com.game.loblib.utility.area.Vertex;

// Used to define the current onscreen area
public class Camera implements IMessageHandler {
	protected final static StringBuffer _tag = new StringBuffer("Camera");
	
	// The entity that the camera will track (e.g. if the entity moves, the camera will move)
	public GameEntity Anchor = null;
	// The amount of X,Y distance the anchor can move from the camera's focal point before tracking will start
	public Vertex Threshold = new Vertex();
	// The total area the camera can cover; the camera will not scroll outside this area
	public Rectangle CoveredArea;
	// The area currently visible to the camera
	public Rectangle CameraArea = new Rectangle();
	// Stores the Y Offset of the camera. Useful for drawing status bar 
	public float YOffset;
	
	// These contain the min/max center points that the camera can reach while still being inside the CoveredArea
	protected float _coveredAreaMinCenterX;
	protected float _coveredAreaMaxCenterX;
	protected float _coveredAreaMinCenterY;
	protected float _coveredAreaMaxCenterY;
	
	public Camera() {
		CoveredArea = new Rectangle();
		CoveredArea.MaintainCenter = false;
	}
	
	public void init() {
		Manager.Message.subscribe(this, MessageType.SCREEN_SIZE_SET);
	}
	
	public void update() {
		// if anchor has moved beyond threshold update camera position
		if (Anchor != null) {
			Vertex center = Manager.Vertex.allocate();
			Vertex anchorCenter = Manager.Vertex.allocate();
			Anchor.Attributes.Area.getCenter(anchorCenter);
			CameraArea.getCenter(center);
			
			if(updatePosition(center, anchorCenter))
				CameraArea.setCenter(center);
			
			Manager.Vertex.release(center);
			Manager.Vertex.release(anchorCenter);
		}
	}
	
	// Sets the camera's focal point
	public void setCenter(Vertex center) {
		Vertex newCenter = Manager.Vertex.allocate();
		Vertex anchorCenter = Manager.Vertex.allocate();
		Anchor.Attributes.Area.getCenter(anchorCenter);
		Area.sync(newCenter, center);
		
		CameraArea.setCenter(center);
		if(updatePosition(newCenter, anchorCenter))
			CameraArea.setCenter(newCenter);
		
		Manager.Vertex.release(newCenter);
		Manager.Vertex.release(anchorCenter);
	}
	
	// Moves the camera's focal point by X & Y defined in vertex
	public void move(Vertex offset) {
		CameraArea.changePosition(offset);
	}
	
	// Moves the camera's focal point by X & Y
	public void move(float x, float y) {
		CameraArea.changePosition(x, y);
	}
	
	// Centers the camera on the anchor entity
	public void centerOnAnchor() {
		if (Anchor != null) {
			Vertex center = Manager.Vertex.allocate();
			Vertex anchorCenter = Manager.Vertex.allocate();
			Anchor.Attributes.Area.getCenter(anchorCenter);
			Area.sync(center, anchorCenter);
			
			CameraArea.setCenter(center);
			if(updatePosition(center, anchorCenter))
				CameraArea.setCenter(center);
			
			Manager.Vertex.release(center);
			Manager.Vertex.release(anchorCenter);
		}
		else
			Logger.e(_tag, "cannot center; no anchor set");
	}
	
	// Sets focal point to provided center, or as close as possible while maintaining the threshold distance from anchorCenter
	protected boolean updatePosition(Vertex center, Vertex anchorCenter) {
		boolean xchange = false;
		boolean ychange = false;
		
		if (Math.abs(anchorCenter.X - center.X) > Threshold.X) {
			xchange = true;
			if (anchorCenter.X < center.X)
				center.X = anchorCenter.X + Threshold.X;
			else
				center.X = anchorCenter.X - Threshold.X;
		}
		if (Math.abs(anchorCenter.Y - center.Y) > Threshold.Y) {
			ychange = true;
			if (anchorCenter.Y < center.Y)
				center.Y = anchorCenter.Y + Threshold.Y;
			else
				center.Y = anchorCenter.Y - Threshold.Y;
		}
		
		float oldX = center.X;
		float oldY = center.Y;
		
		float min = (CameraArea.Size.X / 2f) + CoveredArea.Position.X;
		float max = CoveredArea.Position.X + CoveredArea.Size.X - (CameraArea.Size.X / 2f);
		if (max < min)
			max = min;
		center.X = MathHelper.clamp(min, max, center.X);
		
		min = (CameraArea.Size.Y / 2f) + CoveredArea.Position.Y;
		max = CoveredArea.Position.Y + CoveredArea.Size.Y - (CameraArea.Size.Y / 2f);
		if (max < min)
			max = min;
		center.Y = MathHelper.clamp(min, max, center.Y);
		
		if (oldX != center.X)
			xchange = true;
		if (oldY != center.Y)
			ychange = true;
		
		return xchange || ychange;
	}

	@Override
	public void handleMessage(Message message) {
		if (message.Type == MessageType.SCREEN_SIZE_SET) {
			Vertex v = message.getData();
			CameraArea.setSize(v.X, v.Y /* - Manager.Level.getControlBarHeight() */);

			_coveredAreaMinCenterX = (CameraArea.Size.X / 2f) + CoveredArea.Position.X;
			_coveredAreaMaxCenterX = CoveredArea.Position.X + CoveredArea.Size.X - (CameraArea.Size.X / 2f);
			_coveredAreaMinCenterY = (CameraArea.Size.Y / 2f) + CoveredArea.Position.Y;
			_coveredAreaMaxCenterY = CoveredArea.Position.Y + CoveredArea.Size.Y - (CameraArea.Size.Y / 2f);
		}
	}
}
