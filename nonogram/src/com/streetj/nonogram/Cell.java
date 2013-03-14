package com.streetj.nonogram;

public class Cell {

	public final int row;
	public final int col;
	public final CellValue value;
	
	public Cell() {
		this(0, 0, CellValue.UNKNOWN);
	}
	
	public Cell(Cell cell) {
		this(cell.row, cell.col, cell.value);
	}
	
	public Cell(Cell cell, CellValue value) {
		this(cell.row, cell.col, value);
	}
	
	public Cell(int row, int col) {
		this(row, col, CellValue.UNKNOWN);
	}
	
	public Cell(int row, int col, CellValue value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}
}
