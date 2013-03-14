package com.streetj.nonogram.util;

public class MathUtil {

	public static final int clamp(int v, int min, int max) {
		return Math.min(Math.max(v, min), max);
	}
	
	public static final long clamp(long v, long min, long max) {
		return Math.min(Math.max(v, min), max);
	}
	
	public static final float clamp(float v, float min, float max) {
		return Math.min(Math.max(v, min), max);
	}
	
	public static final double clamp(double v, double min, double max) {
		return Math.min(Math.max(v, min), max);
	}
}
