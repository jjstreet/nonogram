package com.streetj.nonogram.gui;

import java.util.EventObject;

import com.streetj.nonogram.CellValue;

public class CellValueSelectionEvent extends EventObject {

	public final CellValue primaryValue;
	public final CellValue secondaryValue;
	
	public CellValueSelectionEvent(CellValueSelectionModel source, CellValue primaryValue, CellValue secondaryValue) {
		super(source);
		this.primaryValue = primaryValue;
		this.secondaryValue = secondaryValue;
	}
}
