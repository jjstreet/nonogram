package com.streetj.nonogram.gui;

import java.util.EventListener;

public interface GridDataListener extends EventListener {

	public void cellsAdded(GridDataEvent e);
	
	public void cellsRemoved(GridDataEvent e);
	
	public void cellsReplaced(GridDataEvent e);
	
	public void cellsChanged(GridDataEvent e);
}
