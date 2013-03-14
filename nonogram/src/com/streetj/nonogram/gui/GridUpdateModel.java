package com.streetj.nonogram.gui;

import javax.swing.event.EventListenerList;

import com.streetj.nonogram.CellValue;

public class GridUpdateModel {

	private EventListenerList listeners = new EventListenerList();
	
	private int rowStart = -1;
	private int rowEnd = -1;
	private int colStart = -1;
	private int colEnd = -1;
	
	private boolean started = false;
	
	private CellValue primaryValue = CellValue.BLACK;
	private CellValue secondaryValue = CellValue.BLANK;

	private CellValue newValue = CellValue.UNKNOWN;
	
	public CellValue getPrimaryValue() {
		return primaryValue;
	}
	
	public CellValue getSecondaryValue() {
		return secondaryValue;
	}
	
	public CellValue getNewValue() {
		return newValue;
	}
	
	public int getRowStart() {
		return rowStart;
	}
	
	public int getRowEnd() {
		return rowEnd;
	}
	
	public int getColStart() {
		return colStart;
	}
	
	public int getColEnd() {
		return colEnd;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isEmpty() {
		return rowStart < 0 || colStart < 0;
	}
	
	public void setPrimaryValue(CellValue primaryValue) {
		this.primaryValue = primaryValue;
	}
	
	public void setSecondaryValue(CellValue secondaryValue) {
		this.secondaryValue = secondaryValue;
	}
	
	public void setNewValue(CellValue newValue) {
		this.newValue = newValue;
		if (started)
			fireChanged();
	}
	
	public void startUpdate(int rowStart, int colStart) {
		this.rowStart = rowStart;
		this.colStart = colStart;
		this.rowEnd = rowStart;
		this.colEnd = colStart;
		started = true;
		fireStarted();
	}
	
	public void changeUpdate(int rowEnd, int colEnd) {
		if (!started)
			throw new IllegalStateException("update not started");
		changeUpdate(rowStart, rowEnd, colStart, colEnd);
	}
	
	public void changeUpdate(int rowStart, int rowEnd, int colStart, int colEnd) {
		if (!started)
			throw new IllegalStateException("update not started");
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.colStart = colStart;
		this.colEnd = colEnd;
		fireChanged();
	}
	
	public void finishUpdate() {
		if (!started)
			throw new IllegalStateException("update not started");
		finishUpdate(rowEnd, colEnd);
	}
	
	public void finishUpdate(int rowEnd, int colEnd) {
		if (!started)
			throw new IllegalStateException("update not started");
		finishUpdate(rowStart, rowEnd, colStart, colEnd);
	}
	
	public void finishUpdate(int rowStart, int rowEnd, int colStart, int colEnd) {
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.colStart = colStart;
		this.colEnd = colEnd;
		fireFinished();
		started = false;
		this.rowStart = -1;
		this.colStart = -1;
		this.rowEnd = -1;
		this.colEnd = -1;
	}
	
	public void addGridUpdateListener(GridUpdateListener listener) {
		listeners.add(GridUpdateListener.class, listener);
	}
	
	public void removeGridUpdateListener(GridUpdateListener listener) {
		listeners.remove(GridUpdateListener.class, listener);
	}
	
	protected void fireStarted() {
		GridUpdateEvent event = new GridUpdateEvent(this, rowStart, rowEnd, colStart, colEnd, newValue);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridUpdateListener.class)
				((GridUpdateListener) l[i + 1]).updateStarted(event);
		}
	}
	
	protected void fireChanged() {
		GridUpdateEvent event = new GridUpdateEvent(this, rowStart, rowEnd, colStart, colEnd, newValue);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridUpdateListener.class)
				((GridUpdateListener) l[i + 1]).updateChanged(event);
		}
	}
	
	protected void fireFinished() {
		GridUpdateEvent event = new GridUpdateEvent(this, rowStart, rowEnd, colStart, colEnd, newValue);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == GridUpdateListener.class)
				((GridUpdateListener) l[i + 1]).updateFinished(event);
		}
	}
}
