package com.streetj.nonogram.gui;

import java.util.Arrays;

import javax.swing.event.EventListenerList;

import com.streetj.nonogram.CellValue;

public class GridDataModel {

	EventListenerList listeners = new EventListenerList();
	
	private int rows;
	private int cols;
	private CellValue[][] values;
	
	public GridDataModel() {
		this(0, 0);
	}
	
	public GridDataModel(int rows, int cols) {
		this(new CellValue[rows][cols]);
	}
	
	public GridDataModel(CellValue[][] values) {
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
			System.arraycopy(values[r], 0, v[r], 0, values[r].length);
		}
		return v;
	}
	
	public void setRows(int rows) {
		if (this.rows == rows)
			return;
		int rmin = Math.min(this.rows, rows);
		boolean adding = rows > this.rows;
		CellValue[][] v = new CellValue[rows][cols];
		for (int r = 0; r < rmin; r++) {
			System.arraycopy(values[r], 0, v[r], 0, values[r].length);
		}
		if (adding) {
			for (int r = this.rows; r < rows; r++) {
				Arrays.fill(v[r], CellValue.UNKNOWN);
			}
			this.rows = rows;
			fireAdded(rmin, this.rows - 1, 0, cols);
		}
		else {
			rmin = this.rows;
			this.rows = rows;
			values = v;
			fireRemoved(this.rows, rmin - 1, 0, cols);
		}
	}
	
	public void setCols(int cols) {
		if (this.cols == cols)
			return;
		int cmin = Math.min(this.cols, cols);
		boolean adding = cols > this.cols;
		CellValue[][] v = new CellValue[rows][cols];
		for (int r = 0; r < rows; r++) {
			System.arraycopy(values[r], 0, v[r], 0, cmin);
			if (adding) {
				Arrays.fill(v[r], cmin, cols, CellValue.UNKNOWN);
			}
		}
		if (adding) {
			this.cols = cols;
			values = v;
			fireAdded(0, rows, cmin, this.cols - 1);
		}
		else {
			cmin = this.cols;
			this.cols = cols;
			values = v;
			fireRemoved(0, rows, this.cols, cmin - 1);
		}
	}
	
	public void setCellValue(int row, int col, CellValue value) {
		this.values[row][col] = value;
		fireChanged(row, row, col, col);
	}
	
	public void setRowCellValues(int row, CellValue value) {
		Arrays.fill(values[row], value);
		fireChanged(row, row, 0, cols - 1);
	}
	
	public void setRowCellValues(int row, int start, int end, CellValue value) {
		int cs = Math.min(start, end);
		int ce = Math.max(start, end);
		Arrays.fill(values[row], cs, ce + 1, value);
		fireChanged(row, row, start, end);
	}
	
	public void setColCellValues(int col, CellValue value) {
		for (int r = 0; r < rows; r++) {
			values[r][col] = value;
		}
		fireChanged(0, rows - 1, col, col);
	}
	
	public void setColCellValues(int col, int start, int end, CellValue value) {
		int rs = Math.min(start, end);
		int re = Math.max(start, end);
		for (int r = rs; r <= re; r++) {
			values[r][col] = value;
		}
		fireChanged(start, end, col, col);
	}
	
	public void setCellValues(CellValue[][] values) {
		rows = values.length;
		cols = values[0].length;
		CellValue[][] v = new CellValue[rows][cols];
		for (int r = 0; r < rows; r++) {
			System.arraycopy(values[r], 0, v[r], 0, cols);
		}
		this.values = v;
		fireReplaced(0, rows - 1, 0, cols - 1);
	}
	
	public void addGridDataListener(GridDataListener listener) {
		listeners.add(GridDataListener.class, listener);
	}
	
	public void removeGridDataListener(GridDataListener listener) {
		listeners.remove(GridDataListener.class, listener);
	}
	
	protected void fireAdded(int rowStart, int rowEnd, int colStart, int colEnd) {
		GridDataEvent event = new GridDataEvent(this, rowStart, rowEnd, colStart, colEnd);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridDataListener.class)
				((GridDataListener) l[i]).cellsAdded(event);
		}
	}
	
	protected void fireRemoved(int rowStart, int rowEnd, int colStart, int colEnd) {
		GridDataEvent event = new GridDataEvent(this, rowStart, rowEnd, colStart, colEnd);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridDataListener.class)
				((GridDataListener) l[i + 1]).cellsRemoved(event);
		}
	}
	
	protected void fireChanged(int rowStart, int rowEnd, int colStart, int colEnd) {
		GridDataEvent event = new GridDataEvent(this, rowStart, rowEnd, colStart, colEnd);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridDataListener.class)
				((GridDataListener) l[i + 1]).cellsChanged(event);
		}
	}
	
	protected void fireReplaced(int rowStart, int rowEnd, int colStart, int colEnd) {
		GridDataEvent event = new GridDataEvent(this, rowStart, rowEnd, colStart, colEnd);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridDataListener.class)
				((GridDataListener) l[i + 1]).cellsReplaced(event);
		}
	}
}
