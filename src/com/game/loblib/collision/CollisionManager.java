package com.game.loblib.collision;

import com.game.loblib.messaging.IMessageHandler;
import com.game.loblib.messaging.Message;
import com.game.loblib.messaging.MessageType;
import com.game.loblib.utility.Manager;
import com.game.loblib.utility.android.FixedSizeArray;
import com.game.loblib.utility.area.Area;
import com.game.loblib.utility.area.IArea;
import com.game.loblib.utility.area.Rectangle;

public class CollisionManager implements IMessageHandler {
	protected final StringBuffer _tag = new StringBuffer("CollisionManager");
	
	// Partitions are used speed up collision detection by grouping nearby entities
	protected final static int PARTITION_ROW = 8;
	protected final static int TOTAL_PARTITIONS = PARTITION_ROW * PARTITION_ROW;
	
	@SuppressWarnings("unchecked")
	protected FixedSizeArray<ICollisionSender>[] _senderGrid = new FixedSizeArray[TOTAL_PARTITIONS];
	protected FixedSizeArray<Rectangle> _grid = new FixedSizeArray<Rectangle>(TOTAL_PARTITIONS);
	
	public CollisionManager() {
		for (int i = 0; i < TOTAL_PARTITIONS; i++)
			_senderGrid[i] = new FixedSizeArray<ICollisionSender>(512);
		
		for (int y = PARTITION_ROW - 1; y >= 0; y--) {
			for (int x = 0; x < PARTITION_ROW; x++) {
				_grid.add(new Rectangle(0,0,0,0,false));
			}
		}
	}
	
	public void init() {

	}
	
	// Checks for collision on the provided layer.
	// Returns the first ICollisionSender found with which a collision occurs.
	// If no collision is found returns null.
	public ICollisionSender getCollision(IArea area, long layers) {	
		ICollisionSender returnSender = null;
		for (int i = 0; i < TOTAL_PARTITIONS; i ++) {
			// partition check
			if (Area.collision(_grid.get(i), area)) {
				FixedSizeArray<ICollisionSender> senders = _senderGrid[i];
				int count = senders.getCount();
				for (int j = 0; j < count; j++) {
					ICollisionSender sender = senders.get(j);
					// layer check
					if ((sender.getLayers() & layers) > 0) {
						// collision check
						if (Area.collision(area, sender.getArea())) {
							Manager.Message.sendMessage(MessageType.COLLISION, sender);
							if (returnSender == null)
								returnSender = sender;
						}
					}
				}
			}
		}
		
		// no collision found
		return returnSender;
	}

	// Adds collision sender to the appropriate partition(s)
	public void addSender(ICollisionSender sender) {
		IArea senderArea = sender.getArea();
		for (int i = 0; i <  TOTAL_PARTITIONS; i++) {
			if (Area.collision(_grid.get(i), senderArea)) {
				_senderGrid[i].add(sender);
			}
		}
	}
	
	// removes collision sender from any partitions it is in
	public void removeSender(ICollisionSender sender) {
		IArea senderArea = sender.getArea();
		for (int i = 0; i <  TOTAL_PARTITIONS; i++) {
			if (Area.collision(_grid.get(i), senderArea)) {
				_senderGrid[i].remove(sender, false);
			}
		}
	}

	@Override
	public void handleMessage(Message message) {

	}

	// Removes all collision senders from all partitions
	public void flush() {
		for (int i = 0; i < _senderGrid.length; i++)
			_senderGrid[i].clear();
	}
}
