package com.streetj.nonogram.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ScrollPaneTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				final JFrame frame = new JFrame();
				
				final JPanel gameGrid = new JPanel();
				gameGrid.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
				gameGrid.setPreferredSize(new Dimension(1000, 1000));
				
				final JPanel colClues = new JPanel();
				colClues.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
				colClues.setPreferredSize(new Dimension(1000, 100));
				
				final JPanel rowClues = new JPanel();
				rowClues.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				rowClues.setPreferredSize(new Dimension(100, 1000));
				
				final JPanel corner = new JPanel();
				corner.setBorder(BorderFactory.createLineBorder(Color.MAGENTA.darker(), 3));
				corner.setPreferredSize(new Dimension(100, 100));
				
				final JScrollPane scrollPane = new JScrollPane();
				scrollPane.setPreferredSize(new Dimension(600, 600));
				scrollPane.setViewportView(gameGrid);
				scrollPane.setColumnHeaderView(colClues);
				scrollPane.setRowHeaderView(rowClues);
				scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				
				MouseAdapter mouseAdapter = new MouseAdapter() {
					
					
					int ox;
					int oy;
					int dx;
					int dy;
					
					@Override
					public void mousePressed(MouseEvent e) {
						ox = e.getXOnScreen();
						oy = e.getYOnScreen();
					}
					
					@Override
					public void mouseDragged(MouseEvent e) {
						dx = e.getXOnScreen() - ox;
						dy = e.getYOnScreen() - oy;
						ox = e.getXOnScreen();
						oy = e.getYOnScreen();
						int v = scrollPane.getVerticalScrollBar().getValue();
						int h = scrollPane.getHorizontalScrollBar().getValue();
						scrollPane.getVerticalScrollBar().setValue(v - dy);
						scrollPane.getHorizontalScrollBar().setValue(h - dx);
					}
				};
				
				gameGrid.addMouseListener(mouseAdapter);
				gameGrid.addMouseMotionListener(mouseAdapter);
				
				frame.add(scrollPane);
				
				frame.setSize(600, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				
				System.out.println(gameGrid.getSize());
			}
		});
	}
}
