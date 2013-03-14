package com.streetj.nonogram;

public class Clue {

	public final int size;
	public final CellValue value;
	public final boolean complete;
	
	public Clue(Clue clue) {
		this(clue.size, clue.value, clue.complete);
	}
	
	public Clue(Clue clue, boolean complete) {
		this(clue.size, clue.value, complete);
	}
	
	public Clue(int size) {
		this(size, CellValue.BLACK);
	}
	
	public Clue(int size, CellValue value) {
		this(size, value, false);
	}
	
	public Clue(int size, CellValue value, boolean complete) {
		this.size = size;
		this.value = value;
		this.complete = complete;
	}
}
