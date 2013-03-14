package com.streetj.nonogram;

public enum CellValue {

	UNKNOWN(false),
	BLANK(false),
	BLACK(true),
	PURPLE(true),
	BLUE(true),
	GREEN(true),
	YELLOW(true),
	ORANGE(true),
	RED(true),
	PINK(true);
	
	public final boolean filled;
	public final int id;
	public final String idStr;
	
	private CellValue(boolean filled) {
		this.filled = filled;
		this.id = ordinal();
		this.idStr = String.valueOf(ordinal());
	}

	public static CellValue fromInt(int value) {
		return CellValue.values()[value];
	}
	
	public static CellValue fromString(String str) {
		return CellValue.values()[Integer.valueOf(str)];
	}
}
