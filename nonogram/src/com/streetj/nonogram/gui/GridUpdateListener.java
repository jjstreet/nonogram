package com.streetj.nonogram.gui;

import java.util.EventListener;

public interface GridUpdateListener extends EventListener {

	public void updateStarted(GridUpdateEvent e);
	
	public void updateChanged(GridUpdateEvent e);
	
	public void updateFinished(GridUpdateEvent e);
}
