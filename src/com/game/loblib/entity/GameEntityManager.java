package com.game.loblib.entity;

import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.FixedSizeArray;

// Handles the updating of Game Entities
public class GameEntityManager {
	protected boolean _updating = false;
	protected FixedSizeArray<GameEntity> _entities;
	protected FixedSizeArray<GameEntity> _addList;
	protected FixedSizeArray<GameEntity> _removeList;
	
	public GameEntityManager() {
		_entities = new FixedSizeArray<GameEntity>(4096);
		_addList = new FixedSizeArray<GameEntity>(4096);
		_removeList = new FixedSizeArray<GameEntity>(4096);
	}
	
	// Add entity to managed pool
	public void addEntity(GameEntity entity) {
		if (!_updating)
			_entities.add(entity);
		else
			_addList.add(entity);
	}
	
	// Remove entity from managed pool
	public void removeEntity(GameEntity entity) {
		if(!_updating)
			_entities.remove(entity, false);
		else
			_removeList.add(entity);
	}
	
	// Helper function to free sprite, remove from management, and disable behaviors
	public void freeEntity(GameEntity entity) {
		if (entity != null) {
			if (entity.Attributes.Sprite != null) {
				Manager.Sprite.release(entity.Attributes.Sprite);
				entity.Attributes.Sprite = null;
			}
			removeEntity(entity);
			entity.destroyBehaviors();
		}
	}
	
	// Updates all entities; updateRatio: 60fps = 1, 30fps = 2, etc.
	public void update(float updateRatio) {
		_updating = true;
		int count = _entities.getCount();
		for (int i = 0; i < count; i++) {
			_entities.get(i).update(updateRatio);
		}
		_updating = false;
		
		_entities.removeAll(_removeList, false);
		_removeList.clear();
		_entities.addAll(_addList);
		_addList.clear();
	}

	// Removes all entities
	public void flush() {
		_entities.clear();
		_addList.clear();
		_removeList.clear();
	}
}
