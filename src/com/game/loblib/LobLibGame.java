package com.game.loblib;

import android.view.MotionEvent;

import com.game.loblib.LobLibGameThread;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.Global;
import com.game.loblib.utility.Manager;

public class LobLibGame {
	protected static StringBuffer _tag = new StringBuffer("LobLibGame");
	protected boolean _running;
	protected Thread _thread;
	protected LobLibGameThread _gameThread;

	public LobLibGame() {
		_gameThread = new LobLibGameThread();
	}
	
	public void init() {
		Manager.init();
		Global.View.init();
		
		Manager.Message.sendMessage(MessageType.GAME_INIT);
	}
	
	public void start() {
		if (!_running) {
			Runtime r = Runtime.getRuntime();
			r.gc();
			_thread = new Thread(_gameThread);
			_thread.setName("LobLibGame");
			_thread.start();
			_running = true;
		}
	}

	public void onPause() {
		if (_running) {
			_gameThread.pauseGame();
		}
	}
	
	public void onResume() {
		if (_running) {
			Global.Renderer.requestCallback();
		}
		else
			start();
	}
	
	public void onStop() {
		if(_running) {
			_gameThread.stopGame();
		}
		try {
			_thread.join();
		} catch (InterruptedException e) {
			_thread.interrupt();
		}
		_thread = null;
		_running = false;
	}
	
	public void onSurfaceReady() {
		if (_gameThread.isPaused() && _running)
			_gameThread.resumeGame();	
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (_running)
			return _gameThread.onTouchEvent(event);
		else
			return true;
	}

    public boolean onBackDown()  {
    	if (_running)
    		return _gameThread.onBackDown();
    	else
    		return false;
    }
    
    public boolean onMenuDown() {
    	if (_running)
    		return _gameThread.onMenuDown();
    	else
    		return false;
    }
}
