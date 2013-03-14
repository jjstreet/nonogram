package com.streetj.nonogram;

public class DataGrid {

	private int rows;
	private int cols;
	private CellValue[][] values;
	
	public DataGrid() {
		this(0, 0);
	}
	
	public DataGrid(int rows, int cols) {
		this(new CellValue[rows][cols]);
	}
	
	public DataGrid(CellValue[][] values) {
		rows = values.length;
		cols = rows > 0 ? values[0].length : 0;
		this.values = new CellValue[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				this.values[r][c] = values[r][c] != null ? values[r][c] : CellValue.UNKNOWN;
			}
		}
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public CellValue getCellValue(int row, int col) {
		return values[row][col];
	}
	
	public CellValue[][] getCellValues() {
		CellValue[][] v = new CellValue[rows][cols];
		for (int r = 0; r < rows; r++) {
			System.arraycopy(values[r], 0, v[r], 0, cols);
		}
		return v;
	}
	
	public CellValue[] getRowCellValues(int row) {
		CellValue[] v = new CellValue[cols];
		System.arraycopy(values[row], 0, v, 0, cols);
		return v;
	}
	
	public CellValue[] getRowCellValues(int row, int start, int end) {
		int cs = Math.min(start, end);
		int ce = Math.max(start, end);
		CellValue[] v = new CellValue[ce - cs + 1];
		for (int c = cs; c <= ce; c++) {
			v[c] = values[row][c];
		}
		return v;
	}
	
	public CellValue[] getColCellValues(int col) {
		CellValue[] v = new CellValue[rows];
		for (int r = 0; r < rows; r++) {
			v[r] = values[r][col];
		}
		return v;
	}
	
	public CellValue[] getColCellValues(int col, int start, int end) {
		int rs = Math.min(start, end);
		int re = Math.max(start, end);
		CellValue[] v = new CellValue[re - rs + 1];
		for (int r = rs; r <= re; r++) {
			v[r] = values[r][col];
		}
		return v;
	}
	
	public boolean isEmpty() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				if (values[r][c] != CellValue.UNKNOWN)
					return false;
		return true;
	}
	
	public boolean isSolutionTo(DataGrid dataGrid) {
		if (rows != dataGrid.rows || cols != dataGrid.cols)
			return false;
		CellValue[][] compared = dataGrid.values;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (values[r][c].filled != compared[r][c].filled)
					return false;
			}
		}
		return true;
	}

	public void setCellValue(int row, int col, CellValue value) {
		values[row][col] = value;
	}
}
