package com.game.loblib;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.game.loblib.graphics.LobLibRenderer;
import com.game.loblib.utility.ComponentFactory;
import com.game.loblib.utility.Global;
import com.game.loblib.utility.Logger;
import com.game.loblib.utility.android.AllocationGuard;

public class LobLibActivity extends Activity {
	protected static StringBuffer _tag = new StringBuffer("LobLibActivity");
	
	protected ComponentFactory _componentFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set display to full screen & always on
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(
        		  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        		  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Initialize to default component factory if child class does not specify
        if (_componentFactory == null)
        	_componentFactory = new ComponentFactory();
        
        // Create and initialize components
        Logger.init();
        AllocationGuard.sGuardActive = false;
        
        LobLibView view = _componentFactory.CreateView();
        setContentView(view);
        LobLibGame game = _componentFactory.CreateGame();
        LobLibRenderer renderer = _componentFactory.CreateRenderer();
        view.setRenderer(renderer);
        
        Global.Context = this;
        Global.View = view;
        Global.Game = game;
        Global.Renderer = renderer;
        Global.Camera = _componentFactory.CreateCamera();
        Global.Settings = _componentFactory.CreateGameSettings();
        Global.Data =_componentFactory.CreateCommonData();
        Global.SpriteHelper =_componentFactory.CreateSpriteHelper();
        Global.MusicHelper = _componentFactory.CreateMusicHelper();
        
        game.init(_componentFactory);
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
