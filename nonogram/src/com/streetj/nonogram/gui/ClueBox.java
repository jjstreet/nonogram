package com.streetj.nonogram.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.streetj.nonogram.CellValue;
import com.streetj.nonogram.Clue;
import com.streetj.nonogram.util.GuiUtil;

public class ClueBox extends JComponent implements Scrollable, GuiConstants {
	
	private double cellSize;
	private boolean forColumns;
	private int minClues = 5;
	
	private ClueBoxModel model;
	
	private ClueBoxUpdateListener listener;
	
	private Dimension ps = new Dimension();
	
	public ClueBox(boolean forColumns, ClueBoxModel model) {
		this.forColumns = forColumns;
		this.model = model;
		setLayout(null);
		setBorder(BorderFactory.createLineBorder(FGCOLOR_GRID, 2));
		setOpaque(true);
		
		listener = createClueBoxUpdateListener();
		this.model.addClueBoxUpdateListener(listener);
		updateSize();
	}
	
	private ClueBoxUpdateListener createClueBoxUpdateListener() {
		return new ClueBoxUpdateListener() {
			
			@Override
			public void clueBoxUpdated(ClueBoxEvent e) {
				updateSize();
				repaint();
			}
		};
	}
	
	private void updateSize() {
		ps.setSize(
				DEFAULT_CELLSIZE * (forColumns ? model.getLength() : Math.max(model.getMaxClues(), minClues)),
				DEFAULT_CELLSIZE * (forColumns ? Math.max(model.getMaxClues(), minClues) : model.getLength()));
		setPreferredSize(ps);
		setSize(ps);
		setMaximumSize(ps);
		setMinimumSize(ps);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		final int w = getWidth();
		final int h = getHeight();
		final int l = model.getLength();
		if (forColumns)
			cellSize = (double) w / (double) l;
		else
			cellSize = (double) h / (double) l;
		final Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		final Font of = g2d.getFont();
		final Font nf = new Font(
				of.getName(),
				of.getStyle(),
				GuiUtil.getMaxFontSize(g2d, of, String.valueOf(model.getMaxClueSize()), ((int) cellSize) - 4, ((int) cellSize - 4)));
		g2d.setFont(nf);
		g2d.setColor(BGCOLOR_CLUEBOXBG);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		paintClues(g2d);
		paintMinorGrid(g2d);
		paintMajorGrid(g2d);
		g2d.dispose();
	}
	
	protected void paintClues(Graphics2D g2d) {
		final Clue[][] cs = model.getClues();
		final Stroke os = g2d.getStroke();
		final Stroke ns = new BasicStroke(2);
		final int length = model.getLength();
		final int m = Math.max(model.getMaxClues(), minClues);
		final int s = (int) cellSize;
		final FontMetrics fm = g2d.getFontMetrics();
		int len;
		int shift;
		Clue c;
		String cstr;
		int fw;
		int fh = fm.getHeight() - fm.getDescent();
		for (int i = 0; i < length; i++) {
			len = cs[i].length;
			shift = m - len;
			for (int j = 0; j < len; j++) {
				int x = (int) ((j + shift) * cellSize + 1);
				int y = (int) (i * cellSize + 1);
				if (forColumns) {
					x = (int) (i * cellSize + 1);
					y = (int) ((j + shift) * cellSize + 1);
				}
				c = cs[i][j];
				cstr = String.valueOf(c.size);
				fw = fm.stringWidth(cstr);
				g2d.setColor(BGCOLOR_CLUE);
				g2d.fillRect(x, y, s, s);
				g2d.setColor(chooseColor(c.value));
				g2d.drawString(cstr, x + s / 2 - fw / 2, y + fh);
				if (c.complete) {
					g2d.setStroke(ns);
					g2d.drawLine(x +7, y + 7, x + s - 7, y + s - 7);
					g2d.setStroke(os);
				}
			}
		}
	}
	
	protected void paintMinorGrid(Graphics2D g2d) {
		final int w = getWidth();
		final int h = getHeight();
		final int m = Math.max(model.getMaxClues(), minClues);
		final int l = model.getLength();
		g2d.setColor(FGCOLOR_GRID);
		for (int i = 0; i < l; i++) {
			int s = (int) (i * cellSize);
			if (forColumns)
				g2d.drawLine(s, 0, s, h);
			else
				g2d.drawLine(0, s, w, s);
		}
		for (int i = 0; i < m; i++) {
			int s = (int) (i * cellSize);
			if (forColumns)
				g2d.drawLine(0, s, w, s);
			else
				g2d.drawLine(s, 0, s, h);
		}
	}
	
	protected void paintMajorGrid(Graphics2D g2d) {
		final int w = getWidth();
		final int h = getHeight();
		final int m = Math.max(model.getMaxClues(), minClues);
		final int l = model.getLength();
		g2d.setColor(FGCOLOR_GRID);
		for (int i = 0; i < l; i += 5) {
			int s = (int) (i * cellSize);
			if (forColumns) {
				g2d.drawLine(s, 0, s, h);
				g2d.drawLine(s + 1, 0, s + 1, h);
			}
			else {
				g2d.drawLine(0, s, w, s);
				g2d.drawLine(0, s + 1, w, s + 1);
			}
		}
		for (int i = 0; i < m; i += 5) {
			int s = (int) (i * cellSize);
			if (forColumns) {
				g2d.drawLine(0, s, w, s);
				g2d.drawLine(0, s + 1, w, s + 1);
			}
			else {
				g2d.drawLine(s, 0, s, h);
				g2d.drawLine(s + 1, 0, s + 1, h);
			}
		}
	}
	
	private Color chooseColor(CellValue v) {
		switch (v) {
		case UNKNOWN: return BGCOLOR_UNKNOWN;
		case BLANK: return BGCOLOR_BLANK;
		case PURPLE: return BGCOLOR_PURPLE;
		case BLUE: return BGCOLOR_BLUE;
		case GREEN: return BGCOLOR_GREEN;
		case YELLOW: return BGCOLOR_YELLOW;
		case ORANGE: return BGCOLOR_ORANGE;
		case RED: return BGCOLOR_RED;
		case PINK: return BGCOLOR_PINK;
		default: return BGCOLOR_BLACK;
		}
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	private int getIncrement(Rectangle r, int o, int d, int m) {
		int s = (int) cellSize;
		s *= m;
		if (d < 0) {
			int u = r.x % s;
			if (o == SwingConstants.VERTICAL)
				u = r.y % s;
			if (u == 0)
				return s;
			return u;
		}
		else {
			int u = s - (r.x + r.width) % s;
			if (o == SwingConstants.VERTICAL)
				u = s - (r.y + r.height) % s;
			if (u == 0)
				return s;
			return u;
		}
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return getIncrement(visibleRect, orientation, direction, 5);
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return getIncrement(visibleRect, orientation, direction, 1);
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
}
