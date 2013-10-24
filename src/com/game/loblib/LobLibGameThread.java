package com.game.loblib;

import android.os.SystemClock;
import android.view.MotionEvent;

import com.game.loblib.utility.Global;
import com.game.loblib.utility.Manager;

public class LobLibGameThread implements Runnable {
	protected static StringBuffer _tag = new StringBuffer("LobLibGameThread");
	protected boolean _paused;
	protected boolean _finished;
	protected long _lastUpdate;
	protected Object _pauseLock = new Object();

	public LobLibGameThread() {
		_lastUpdate = SystemClock.uptimeMillis();
	}
	
	@Override
	public void run() {
		while (!_finished) {
			// Calculate time since last update
			long updateTime = Math.min(SystemClock.uptimeMillis() - _lastUpdate, 48);
			_lastUpdate = SystemClock.uptimeMillis();
			// update ratio: 60fps = 1f, 30fps = 2f, etc
			float updateRatio = (3f * (float)updateTime) / 50f;
			
			// Update all components
			Manager.Sprite.update(updateTime);
			Manager.Screen.update(updateRatio);
			Global.Camera.update();
			Manager.Entity.update(updateRatio);
			Manager.Trigger.update();
			
			// Finish drawing frame
			Global.Renderer.waitDrawingComplete();
			Manager.Sprite.frameComplete();
			
			//wait if thread is not running
			synchronized (_pauseLock) {
				if (_paused) {
					// TODO: pause things here?
					while (_paused) {
						try {
							_pauseLock.wait();
						}
						catch (InterruptedException e) {
							// ignore
						}
					}
				}
			}
			
			//wait for rest of frame
//			long waitTime = SystemClock.uptimeMillis() - _lastUpdate;
//			if (waitTime < 16)
//			try{
//				Thread.sleep(16 - waitTime);
//			} catch(InterruptedException e)
//			{
//				//ignore
//			}
		}
	}
	
	// ends the application
	public void stopGame() {
		synchronized (_pauseLock) {
			_paused = false;
			_finished = true;
			_pauseLock.notifyAll();
		}
	}
	
	// pauses the application
	public void pauseGame() {
		synchronized (_pauseLock) {
			_paused = true;
			Manager.Sprite.onPause();
			Manager.Sound.pause();
			//Manager.Screen.pause();
		}
	}
	
	// resumes paused game
	public void resumeGame() {
		synchronized (_pauseLock) {
			_paused = false;
			_pauseLock.notifyAll();
			Manager.Sprite.onResume();
			Manager.Sound.unpause();
			//Manager.Screen.unpause();
		}
	}

	public boolean isPaused() {
		return _paused;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return Manager.Input.onTouchEvent(event);
	}

	public boolean onBackDown() {
		return Manager.Screen.onBackDown();
	}
	
	public boolean onMenuDown() {
		return Manager.Screen.onMenuDown();
	}
}
