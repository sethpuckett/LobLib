package com.game.loblib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.game.loblib.graphics.Texture;
import com.game.loblib.messaging.IMessageHandler;
import com.game.loblib.messaging.Message;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.Global;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.FixedSizeArray;

public class LobLibView extends GLSurfaceView implements IMessageHandler {
	protected FixedSizeArray<Texture> _unloadedTextures = new FixedSizeArray<Texture>(256);

	public LobLibView(Context context) {
		super(context);
		//setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
	}
	
	public void init() {
		Manager.Message.subscribe(this, MessageType.SURFACE_CREATED);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return Global.Game.onTouchEvent(event);
	}

	public void bindTexture(final Texture tex) {
		if (Global.Renderer.isSurfaceCreated()) {
			queueEvent(new Runnable() {
				public void run() {
					Global.Renderer.bindTexture(tex);
				}});
		}
		else
			_unloadedTextures.add(tex);
	}
	
	@Override
	public void handleMessage(Message message) {
		if (message.Type == MessageType.SURFACE_CREATED)
			loadTextures();
	}
	
	// loads textures for all sprites in _unloadedSprites
	protected void loadTextures() {		
		int count = _unloadedTextures.getCount();
		for (int i = 0; i < count; i++) {
			bindTexture(_unloadedTextures.get(i));
		}
		_unloadedTextures.clear();
	}
}
