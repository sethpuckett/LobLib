package com.game.loblib.utility;

public class LayoutHelper {

	public static float Width() {
		return Global.Renderer.Width;
	}
	
	public static float WidthFrac(float numerator) {
		return Global.Renderer.Width / numerator;
	}
	
	public static float WidthMul(float multiplier) {
		return Global.Renderer.Width * multiplier;
	}
	
	public static float WidthAddFrac(float numerator1, float numerator2) {
		return Global.Renderer.Width / numerator1 + Global.Renderer.Width / numerator2;
	}

	public static float WidthSubFrac(float numerator1, float numerator2) {
		return Global.Renderer.Width / numerator1 - Global.Renderer.Width / numerator2;
	}
	
	public static float WidthAddMul(float multiplier1, float multiplier2) {
		return Global.Renderer.Width * multiplier1 + Global.Renderer.Width * multiplier2;
	}
	
	public static float Height() {
		return Global.Renderer.Height;
	}
	
	public static float HeightFrac(float numerator) {
		return Global.Renderer.Height / numerator;
	}
	
	public static float HeightMul(float multiplier) {
		return Global.Renderer.Height * multiplier;
	}
	
	public static float HeightAddFrac(float numerator1, float numerator2) {
		return Global.Renderer.Height / numerator1 + Global.Renderer.Height / numerator2;
	}

	public static float HeightSubFrac(float numerator1, float numerator2) {
		return Global.Renderer.Height / numerator1 - Global.Renderer.Height / numerator2;
	}
	
	public static float HeightAddMul(float multiplier1, float multiplier2) {
		return Global.Renderer.Height * multiplier1 + Global.Renderer.Height * multiplier2;
	}
}
