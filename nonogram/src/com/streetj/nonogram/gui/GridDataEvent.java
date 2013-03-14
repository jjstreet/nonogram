package com.streetj.nonogram.gui;

import java.util.EventObject;

public class GridDataEvent extends EventObject {

	public final int rowStart;
	public final int rowEnd;
	public final int colStart;
	public final int colEnd;
	
	public GridDataEvent(GridDataModel source, int rowStart, int rowEnd, int colStart, int colEnd) {
		super(source);
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.colStart = colStart;
		this.colEnd = colEnd;
	}
}
