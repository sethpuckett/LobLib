package com.game.loblib.messaging;

public class MessageType {
	public final static long UNKNOWN = 0;
	public final static long SCREEN_SIZE_SET = 		1 << 0;		//data: Vertex
	public final static long GAME_INIT =			1 << 1;		//no data	
	public final static long SURFACE_CREATED =		1 << 2;		//no data
	public final static long BUTTON_CLICKED =		1 << 3;		//data: GameEntity
	public final static long ANIMATION_FINISHED = 	1 << 4;		//data: Sprite
	public final static long TWEEN_FINISHED =		1 << 5;		//data: GameEntity
	public final static long SOUND_ENABLED =		1 << 7;		//no data
	public final static long SOUND_DISABLED =		1 << 8;		//no data
	public final static long COLLISION =			1 << 9;		//data: ICollisionSender
	public final static long TRIGGER_HIT =			1 << 10;	//data: GameEntity
	public final static long TRIGGER_RELEASED =		1 << 11;	//data: GameEntity
	public final static long TIMER_ALARM		 =	1 << 13;	//data: GameEntity
	public final static long FADE_IN_COMPLETE =		1 << 20;	//data: GameEntity
	public final static long FADE_OUT_COMPLETE =	1 << 21;	//data: GameEntity
	public final static long BUTTON_DOWN =			1 << 25;	//data: GameEntity
	public final static long BUTTON_UP =			1 << 26;	//data: GameEntity
	public final static long DRAG_START =			1 << 27;	//data: GameEntity
	public final static long DRAG_STOP =			1 << 28;	//data: GameEntity
	public final static long FADE_STATE_CHANGE =	1 << 29;	//data: GameEntity
	
	public final static long ALL = 					Integer.MAX_VALUE;
}
