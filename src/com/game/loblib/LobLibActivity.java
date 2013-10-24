package com.game.loblib;

import com.game.loblib.LobLibGame;
import com.game.loblib.LobLibView;
import com.game.loblib.graphics.Camera;
import com.game.loblib.graphics.LobLibRenderer;
import com.game.loblib.graphics.SpriteHelper;
import com.game.loblib.sound.MusicHelper;
import com.game.loblib.utility.CommonData;
import com.game.loblib.utility.GameSettings;
import com.game.loblib.utility.Global;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.android.AllocationGuard;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.WindowManager;

public class LobLibActivity extends Activity {
	
	// These can be overridden in child classes
	protected GameSettings _gameSettings;
	protected CommonData _commonData;
	protected SpriteHelper _spriteHelper;
	protected MusicHelper _musicHelper;

	protected static StringBuffer _tag = new StringBuffer("LobLibActivity");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(
        		  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        		  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Logger.init();
        
        AllocationGuard.sGuardActive = false;
        
        //TODO: testing
		//Editor editor = getSharedPreferences("loblib", Context.MODE_PRIVATE).edit();
		//editor.putInt("maxLevel", 21);
		//editor.commit();
        
        LobLibView view = new LobLibView(this);
        setContentView(view);
        LobLibGame game = new LobLibGame();
        LobLibRenderer renderer = new LobLibRenderer();
        view.setRenderer(renderer);
        
        Global.Context = this;
        Global.View = view;
        Global.Game = game;
        Global.Renderer = renderer;
        Global.Camera = new Camera();
        
        if (_gameSettings == null)
        	_gameSettings = new GameSettings();
        Global.Settings = _gameSettings;
        
        if (_commonData == null)
        	_commonData = new CommonData();
        Global.Data = _commonData;
        
        if (_spriteHelper == null)
        	_spriteHelper = new SpriteHelper();
        Global.SpriteHelper = _spriteHelper;
        
        if (_musicHelper == null)
        	_musicHelper = new MusicHelper();
        Global.MusicHelper = _musicHelper;
        
        game.init();
        Global.Camera.init();
        Runtime.getRuntime().gc();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
    	boolean returnSuper = true;
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (Global.Game.onBackDown())
            	returnSuper = false;
        }
        else if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
            if (Global.Game.onMenuDown())
            	returnSuper = false;
        }
        
        if (returnSuper)
        	return super.onKeyDown(keyCode, event);
        else
        	return true;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Global.Game.onPause();
    	Global.Renderer.onPause();
    	Global.View.onPause();
		Runtime r = Runtime.getRuntime();
		r.gc();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Global.View.onResume();
    	Global.Renderer.onResume();
    	Global.Game.onResume();	
    }
    
    @Override
    protected void onDestroy() {
    	Global.Game.onStop();
    	super.onDestroy();
    } 

}
