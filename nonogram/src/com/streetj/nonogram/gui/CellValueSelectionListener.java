package com.streetj.nonogram.gui;

import java.util.EventListener;

public interface CellValueSelectionListener extends EventListener {

	public void selectionChanged(CellValueSelectionEvent e);
}
