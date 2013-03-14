package com.streetj.nonogram;

import javax.swing.SwingUtilities;

import com.streetj.nonogram.gui.GameFrame;

public class Nonogram {
	
	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				GameFrame gameFrame = new GameFrame();
				gameFrame.setVisible(true);
			}
		});
	}
}
