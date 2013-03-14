package com.streetj.nonogram.gui;

import javax.swing.event.EventListenerList;

import com.streetj.nonogram.CellValue;

public class CellValueSelectionModel {

	private EventListenerList listeners = new EventListenerList();
	
	private CellValue primaryValue;
	private CellValue secondaryValue;
	
	public CellValueSelectionModel() {
		primaryValue = CellValue.BLACK;
		secondaryValue = CellValue.BLANK;
	}
	
	public CellValue getPrimaryValue() {
		return primaryValue;
	}
	
	public CellValue getSecondaryValue() {
		return secondaryValue;
	}
	
	public void setPrimaryValue(CellValue primaryValue) {
		if (this.primaryValue == primaryValue)
			return;
		this.primaryValue = primaryValue;
		fireChange();
	}
	
	public void setSecondaryValue(CellValue secondaryValue) {
		if (this.secondaryValue == secondaryValue)
			return;
		this.secondaryValue = secondaryValue;
		fireChange();
	}
	
	protected void fireChange() {
		CellValueSelectionEvent event = new CellValueSelectionEvent(this, primaryValue, secondaryValue);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == CellValueSelectionListener.class)
				((CellValueSelectionListener) l[i + 1]).selectionChanged(event);
		}
	}
	
	public void addCellValueSelectionListener(CellValueSelectionListener listener) {
		listeners.add(CellValueSelectionListener.class, listener);
	}
	
	public void removeCellValueSelectionListener(CellValueSelectionListener listener) {
		listeners.add(CellValueSelectionListener.class, listener);
	}
}
