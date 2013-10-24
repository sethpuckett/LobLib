package com.game.loblib.utility;

import com.game.loblib.collision.CollisionManager;
import com.game.loblib.collision.TriggerManager;
import com.game.loblib.entity.GameEntityManager;
import com.game.loblib.graphics.SpriteManager;
import com.game.loblib.input.InputManager;
import com.game.loblib.messaging.MessageManager;
import com.game.loblib.screen.ScreenManager;
import com.game.loblib.sound.SoundManager;
import com.game.loblib.utility.area.CircleManager;
import com.game.loblib.utility.area.RectangleManager;
import com.game.loblib.utility.area.VertexManager;

public class Manager {
	protected static StringBuffer _tag = new StringBuffer("Manager");
	
	public static GameEntityManager Entity;
	public static SpriteManager Sprite;
	public static InputManager Input;
	public static MessageManager Message;
	public static CollisionManager Collision;
	public static VertexManager Vertex;
	public static RectangleManager Rectangle;
	public static CircleManager Circle;
	public static ScreenManager Screen;
	public static SoundManager Sound;
	public static TriggerManager Trigger;
	
	public static void init() {
		Entity = new GameEntityManager();
		Sprite = new SpriteManager();
		Input = new InputManager();
		Message = new MessageManager();
		Collision = new CollisionManager();
		Vertex = new VertexManager();
		Rectangle = new RectangleManager();
		Circle = new CircleManager();
		Screen = new ScreenManager();
		Sound = new SoundManager();
		Trigger = new TriggerManager();
		
		Collision.init();
		Screen.init();
		Trigger.init();
	}
	
	public static void flush() {
		Entity.flush();
		Sprite.flush();
		Input.flush();
		Message.flush();
		Collision.flush();
		Sound.flush();
		Trigger.flush();
	}
}
