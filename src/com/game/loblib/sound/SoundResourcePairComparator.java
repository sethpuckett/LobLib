package com.game.loblib.sound;

import java.util.Comparator;

public class SoundResourcePairComparator implements Comparator<SoundResourcePair> {
	@Override
	public int compare(SoundResourcePair p1, SoundResourcePair p2) {
		return p2.ClientId - p1.ClientId;
	}
}