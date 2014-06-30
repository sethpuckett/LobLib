package com.game.loblib.utility;

import java.security.InvalidParameterException;
import java.util.Random;

import com.game.loblib.utility.area.Rectangle;
import com.game.loblib.utility.area.Vertex;

public class MathHelper {
	protected static StringBuffer _tag = new StringBuffer("MathHelper");
	
	public static final float PI = 3.14159265f;
	public static final float PI_OVER2 = 1.57079633f;
	public static final float PI_OVER4 = 0.785398163f;
	public static final float PI_TIMES2 = 6.28318531f;
	
	
	public static boolean pointInRectangle(Vertex v1, Rectangle r1) {
		boolean retVal = true;
		
		if (v1.X < r1.Position.X)
			retVal = false;
		else if (v1.X > r1.Position.X + r1.Size.X)
			retVal = false;
		else if (v1.Y < r1.Position.Y)
			retVal = false;
		else if (v1.Y > r1.Position.Y + r1.Size.Y)
			retVal = false;
		
		return retVal;
	}
	
	public static boolean floatEqZero(float f1) {
		if (f1 < .000001 && f1 > -.000001)
			return true;
		else
			return false;
	}
	
	public static boolean floatEq(float f1, float f2) {
		if (f1 - f2 < .000001 && f1 - f2 > -.000001 )
			return true;
		else
			return false;
	}
	
	public static <T extends Comparable<T>> T clamp(T min, T max, T value) {
		if (min.compareTo(max) > 0) {
			InvalidParameterException e = new InvalidParameterException("min cannot be greater than max");
			Logger.e(_tag, "min cannot be greater than max", e);
			throw e;
		}
		
		if (value.compareTo(min) < 0)
			return min;
		else if (value.compareTo(max) > 0)
			return max;
		else
			return value;
		
	}
	
	public static float angle(Vertex v) {
		if (v.X == 0 || v.Y == 0) {
			Logger.e(_tag, "Angle undefined");
			return 0f;
		}
		
		double angle = 0f;
		
		double atan = Math.atan(v.Y / v.X);
		if (v.X > 0 && v.Y > 0)
			angle = atan;
		else if (v.X < 0 && v.Y > 0)
			angle = PI - Math.abs(atan);
		else if (v.X < 0 && v.Y < 0)
			angle = PI + atan;
		else
			angle = PI_TIMES2 - Math.abs(atan);
		
		return (float)angle;
	}
 
	public static <T> T GetRandomValueFromSet(Random rand, T... set) {
		if (set == null || set.length == 0)
			Logger.e(_tag, "Cannot select random value; set is empty");
		
		int index = rand.nextInt(set.length);
		return set[index];
	}
	
	public static void ShuffleArray(int[] array, Random rand) {
	    int index;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        if (index != i)
	        {
	            array[index] ^= array[i];
	            array[i] ^= array[index];
	            array[index] ^= array[i];
	        }
	    }
	}
}