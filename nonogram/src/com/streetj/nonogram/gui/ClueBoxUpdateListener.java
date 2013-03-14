package com.streetj.nonogram.gui;

import java.util.EventListener;

public interface ClueBoxUpdateListener extends EventListener {

	public void clueBoxUpdated(ClueBoxEvent e);
}
