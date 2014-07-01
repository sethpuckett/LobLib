package com.game.loblib.utility;

import com.game.loblib.LobLibGame;
import com.game.loblib.LobLibView;
import com.game.loblib.collision.CollisionManager;
import com.game.loblib.collision.TriggerManager;
import com.game.loblib.entity.GameEntityManager;
import com.game.loblib.graphics.Camera;
import com.game.loblib.graphics.LobLibRenderer;
import com.game.loblib.graphics.SpriteHelper;
import com.game.loblib.graphics.SpriteManager;
import com.game.loblib.input.InputManager;
import com.game.loblib.messaging.MessageManager;
import com.game.loblib.screen.ScreenFactory;
import com.game.loblib.screen.ScreenManager;
import com.game.loblib.sound.MusicHelper;
import com.game.loblib.sound.SoundManager;
import com.game.loblib.text.TextManager;
import com.game.loblib.utility.area.CircleManager;
import com.game.loblib.utility.area.RectangleManager;
import com.game.loblib.utility.area.VertexManager;

public class ComponentFactory {
	
	public Global CreateGlobal() {
		return new Global();
	}
	
	public LobLibGame CreateGame() {
		return new LobLibGame();
	}
	
	public LobLibRenderer CreateRenderer() {
		return new LobLibRenderer();
	}
	
	public LobLibView CreateView() {
		return new LobLibView(Global.Context);
	}
	
	public Camera CreateCamera() {
		return new Camera();
	}
	
	public GameSettings CreateGameSettings() {
		return new GameSettings();
	}
	
	public CommonData CreateCommonData() {
		return new CommonData();
	}
	
	public SpriteHelper CreateSpriteHelper() {
		return new SpriteHelper();
	}
	
	public MusicHelper CreateMusicHelper() {
		return new MusicHelper();
	}
	
	public GameEntityManager CreateGameEntityManager() {
		return new GameEntityManager();
	}
	
	public SpriteManager CreateSpriteManager() {
		return new SpriteManager();
	}
	
	public InputManager CreateInputManager() {
		return new InputManager();
	}

	public MessageManager CreateMessageManager() {
		return new MessageManager();
	}
	
	public CollisionManager CreateCollisionManager() {
		return new CollisionManager();
	}
	
	public VertexManager CreateVertexManager() {
		return new VertexManager();
	}
	
	public RectangleManager CreateRectangleManager() {
		return new RectangleManager();
	}
	
	public CircleManager CreateCircleManager() {
		return new CircleManager();
	}
	
	public ScreenManager CreateScreenManager() {
		return new ScreenManager();
	}
	
	public SoundManager CreateSoundManager() {
		return new SoundManager();
	}
	
	public TriggerManager CreateTriggerManager() {
		return new TriggerManager();
	}
	
	public TextManager CreateTextManager() {
		return new TextManager();
	}
	
	public ScreenFactory CreateScreenFactory() {
		return new ScreenFactory();
	}
}
