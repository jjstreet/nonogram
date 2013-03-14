package com.streetj.nonogram;

public class GameState {
	
	private String title;
	private String author;

	private int rows;
	private int cols;
	
	private ClueArray rowClues;
	private ClueArray colClues;
	
	private DataGrid working;
	private DataGrid solution;
	
	private GameUpdateHistory undoHistory;
	private GameUpdateHistory redoHistory;
	
	public GameState(String title, String author, int rows, int cols, ClueArray rowClues, ClueArray colClues, DataGrid solution) {
		this.title = title;
		this.author = author;
		this.rows = rows;
		this.cols = cols;
		this.rowClues = rowClues;
		this.colClues = colClues;
		this.working = new DataGrid(rows, cols);
		this.solution = solution;
		this.undoHistory = new GameUpdateHistory();
		this.redoHistory = new GameUpdateHistory();
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public boolean isSolved() {
		return rowClues.isComplete() && colClues.isComplete();
	}
	
	public boolean isSolution() {
		return working.isSolutionTo(solution);
	}
	
	public boolean isEmpty() {
		return working.isEmpty();
	}
	
	public CellValue getWorkingCellValue(int row, int col) {
		return working.getCellValue(row, col);
	}
	
	public CellValue[][] getWorkingCellValues() {
		return working.getCellValues();
	}
	
	public CellValue[][] getSolutionCellValues() {
		return solution.getCellValues();
	}
	
	public Clue[][] getRowClues() {
		return rowClues.getClues();
	}
	
	public Clue[][] getColClues() {
		return colClues.getClues();
	}
	
	public int getUndoCount() {
		return undoHistory.size();
	}
	
	public int getRedoCount() {
		return redoHistory.size();
	}
	
	public void doUpdate(GameUpdate gameUpdate) {
		doUpdate(gameUpdate, true, true);
	}
	
	private void doUpdate(GameUpdate gameUpdate, boolean addUndo, boolean clearRedo) {
		if (addUndo)
			undoHistory.put(gameUpdate);
		if (clearRedo)
			redoHistory.clear();
		final Cell[] newCells = gameUpdate.getNewCells();
		Cell c;
		for (int i = 0; i < newCells.length; i++) {
			c = newCells[i];
			working.setCellValue(c.row, c.col, c.value);
		}
		CellValue[][] values = working.getCellValues();
		updateClues(values);
	}
	
	public void projectUpdate(GameUpdate gameUpdate) {
		final Cell[] newCells = gameUpdate.getNewCells();
		Cell c;
		CellValue[][] values = working.getCellValues();
		for (int i = 0; i < newCells.length; i++) {
			c = newCells[i];
			values[c.row][c.col] = c.value;
		}
		updateClues(values);
	}
	
	private void updateClues(CellValue[][] values) {
		CellValue[] v;
		for (int r = 0; r < rows; r++) {
			v = values[r];
			rowClues.updateClues(r, v);
		}
		v = new CellValue[rows];
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				v[r] = values[r][c];
			}
			colClues.updateClues(c, v);
		}
	}
	
	public void undo() {
		GameUpdate gu = undoHistory.take();
		if (gu == null)
			return;
		redoHistory.put(gu);
		gu = new GameUpdate(gu.getNewCells(), gu.getOldCells());
		doUpdate(gu, false, false);
	}
	
	public void redo() {
		GameUpdate gu = redoHistory.take();
		if (gu == null)
			return;
		undoHistory.put(gu);
		doUpdate(gu, false, false);
	}
	
	public void reset() {
		working = new DataGrid(rows, cols);
		updateClues(working.getCellValues());
		undoHistory.clear();
		redoHistory.clear();
	}
}
