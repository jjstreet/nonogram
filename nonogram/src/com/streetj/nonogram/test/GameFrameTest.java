package com.streetj.nonogram.test;

import javax.swing.SwingUtilities;

import com.streetj.nonogram.gui.GameFrame;

public class GameFrameTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				GameFrame gf = new GameFrame();
				gf.setVisible(true);
			}
		});
	}
}
