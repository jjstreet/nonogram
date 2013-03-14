package com.streetj.nonogram.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.streetj.nonogram.gui.GameBoard;

public class GameBoardTest {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame();
				
				GameBoard gameBoard = new GameBoard(30, 30);
				frame.add(gameBoard);
				
				frame.setSize(600, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			
			}
		});
	}
}
