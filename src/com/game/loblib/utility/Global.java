package com.game.loblib.utility;

import android.content.Context;

import com.game.loblib.LobLibGame;
import com.game.loblib.LobLibView;
import com.game.loblib.graphics.Camera;
import com.game.loblib.graphics.LobLibRenderer;
import com.game.loblib.graphics.SpriteHelper;
import com.game.loblib.sound.MusicHelper;
import com.game.loblib.text.GLText;

// making these globally accessible so I don't have to pass them around everywhere
public class Global {
	public static Context Context;
	public static LobLibView View;
	public static LobLibRenderer Renderer;
	public static LobLibGame Game;
	public static Camera Camera;
	public static GameSettings Settings;
	public static CommonData Data;
	public static SpriteHelper SpriteHelper;
	public static MusicHelper MusicHelper;
}
