package com.streetj.nonogram;

public class GameUpdate {

	private final Cell[] oldCells;
	private final Cell[] newCells;
	
	public GameUpdate(Cell oldCell, CellValue newValue) {
		this(new Cell[] {oldCell}, newValue);
	}
	
	public GameUpdate(Cell[] oldCells, CellValue newValue) {
		this.oldCells = new Cell[oldCells.length];
		System.arraycopy(oldCells, 0, this.oldCells, 0, oldCells.length);
		newCells = new Cell[oldCells.length];
		for (int i = 0; i < oldCells.length; i++) {
			newCells[i] = new Cell(oldCells[i], newValue);
		}
	}
	
	public GameUpdate(Cell[] oldCells, Cell[] newCells) {
		this.oldCells = new Cell[oldCells.length];
		System.arraycopy(oldCells, 0, this.oldCells, 0, oldCells.length);
		this.newCells = new Cell[newCells.length];
		System.arraycopy(newCells, 0, this.newCells, 0, newCells.length);
	}
	
	public Cell[] getOldCells() {
		return oldCells;
	}
	
	public Cell[] getNewCells() {
		return newCells;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < oldCells.length; i++) {
			sb.append("(" + oldCells[i].row + ", " + oldCells[i].col + ")")
					.append(":").append(oldCells[i].value).append("->").append(newCells[i].value);
			if (i < oldCells.length - 1) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}
}
