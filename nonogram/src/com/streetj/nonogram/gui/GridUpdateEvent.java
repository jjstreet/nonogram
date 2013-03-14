package com.streetj.nonogram.gui;

import java.util.EventObject;

import com.streetj.nonogram.CellValue;

public class GridUpdateEvent extends EventObject {

	public final int rowStart;
	public final int rowEnd;
	public final int colStart;
	public final int colEnd;
	public final CellValue newValue;
	
	public GridUpdateEvent(GridUpdateModel source, int rowStart, int rowEnd, int colStart, int colEnd, CellValue newValue) {
		super(source);
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.colStart = colStart;
		this.colEnd = colEnd;
		this.newValue = newValue;
	}
}
