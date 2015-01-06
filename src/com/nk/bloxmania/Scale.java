package com.nk.bloxmania;

public class Scale {
	private static final int W = 960;
	private static final int H = 540;
	
	public static float getWidthScale(int w){
		return 1.0f * w / W;
	}
	
	public static float getHeightScale(int h){
		return 1.0f * h / H;
	}
	
	public static float getUniformScale(int size){
		return getHeightScale(size);
	}
}
