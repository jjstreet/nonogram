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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.streetj.nonogram.CellValue;
import com.streetj.nonogram.util.GuiUtil;
import com.streetj.nonogram.util.MathUtil;

public class Grid extends JComponent implements Scrollable, SwingConstants, GuiConstants {
	
	private double cellSize;
	private GridDataModel dataModel;
	private GridUpdateModel updateModel;
	
	private GridDataListener gridDataListener;
	private GridUpdateListener gridUpdateListener;
	
	private Dimension ps = new Dimension();
	
	public Grid(GridDataModel dataModel, GridUpdateModel updateModel) {
		this.dataModel = dataModel;
		this.updateModel = updateModel;
		setLayout(null);
		setBorder(BorderFactory.createLineBorder(FGCOLOR_GRID, 2));
		setOpaque(true);
		setAutoscrolls(true);
		updateSize();
		installListeners();
	}
	
	protected void installListeners() {
		gridDataListener = createGridDataListener();
		dataModel.addGridDataListener(gridDataListener);
		gridUpdateListener = createGridUpdateListener();
		updateModel.addGridUpdateListener(gridUpdateListener);
	}
	
	private void updateSize() {
		ps.setSize(
				DEFAULT_CELLSIZE * dataModel.getCols(),
				DEFAULT_CELLSIZE * dataModel.getRows());
		setPreferredSize(ps);
		setSize(ps);
		setMaximumSize(ps);
		setMinimumSize(ps);
	}
	
	protected GridDataListener createGridDataListener() {
		return new GridDataListener() {
			
			@Override
			public void cellsReplaced(GridDataEvent e) {
				updateSize();
				repaint();
			}
			
			@Override
			public void cellsRemoved(GridDataEvent e) {
				updateSize();
				repaint();
			}
			
			@Override
			public void cellsChanged(GridDataEvent e) {
				repaint();
			}
			
			@Override
			public void cellsAdded(GridDataEvent e) {
				updateSize();
				repaint();
			}
		};
	}
	
	public GridUpdateListener createGridUpdateListener() {
		return new GridUpdateListener() {
			
			@Override
			public void updateStarted(GridUpdateEvent e) {
				repaint();
			}
			
			@Override
			public void updateFinished(GridUpdateEvent e) {
				/*
				 * No repainting here because the grid will be updated
				 * as part of a cell change on the data model.
				 */
				if (e.rowStart != e.rowEnd)
					dataModel.setColCellValues(e.colStart, e.rowStart, e.rowEnd, e.newValue);
				else if (e.colStart != e.colEnd)
					dataModel.setRowCellValues(e.rowStart, e.colStart, e.colEnd, e.newValue);
				else
					dataModel.setCellValue(e.rowStart, e.colStart, e.newValue);
			}
			
			@Override
			public void updateChanged(GridUpdateEvent e) {
				repaint();
			}
		};
	}
	
	public int convertXToCol(int x) {
		return MathUtil.clamp((int) (x / cellSize), 0, dataModel.getCols() - 1);
	}
	
	public int convertYToRow(int y) {
		return MathUtil.clamp((int) (y / cellSize), 0, dataModel.getRows() - 1);
	}
	
	public int convertColToX(int col) {
		return (int) (cellSize * col);
	}
	
	public int convertRowToY(int row) {
		return (int) (cellSize * row);
	}
	
	public int getCellSize() {
		return (int) cellSize;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		cellSize = (double) getWidth() / (double) dataModel.getCols();
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
				GuiUtil.getMaxFontSize(g2d, of, "?", (int) cellSize - 1, (int) cellSize - 1));
		g2d.setFont(nf);
		g2d.setColor(BGCOLOR_GRIDBG);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		paintCells(g2d);
		paintUpdate(g2d);
		paintMinorGrid(g2d);
		paintMajorGrid(g2d);
		g2d.dispose();
	}
	
	protected void paintCells(Graphics2D g2d) {
		final Stroke os = g2d.getStroke();
		final Stroke ns = new BasicStroke(2);
		final int rows = dataModel.getRows();
		final int cols = dataModel.getCols();
		final CellValue[][] cv = dataModel.getCellValues();
		final int s = (int) cellSize;
		final FontMetrics fm = g2d.getFontMetrics();
		final int wc = SwingUtilities.computeStringWidth(fm, "?");
		int x;
		int y;
		CellValue v;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				x = (int) (c * cellSize + 1);
				y = (int) (r * cellSize + 1);
				v = cv[r][c];
				g2d.setColor(chooseColor(v));
				g2d.fillRect(x, y, s, s);
				if (v == CellValue.BLANK) {
					g2d.setColor(FGCOLOR_BLANK);
					g2d.setStroke(ns);
					g2d.drawLine(x + s - 5, y + 3, x + 3, y + s - 5);
					g2d.drawLine(x + 3, y + 3, x + s - 5, y + s - 5);
					g2d.setStroke(os);
					
				}
				else if (v == CellValue.UNKNOWN) {
					g2d.setColor(FGCOLOR_UNKNOWN);
					g2d.drawString("?", x + ((s - wc) / 2), y + (fm.getHeight() - fm.getDescent()));
				}
			}
		}
	}
	
	protected void paintUpdate(Graphics2D g2d) {
		if (updateModel.isEmpty())
			return;
		final Stroke os = g2d.getStroke();
		final Stroke ns = new BasicStroke(2);
		final int s = (int) cellSize;
		final FontMetrics fm = g2d.getFontMetrics();
		final int wc = SwingUtilities.computeStringWidth(fm, "?");
		final CellValue v = updateModel.getNewValue();
		final int rs = Math.min(updateModel.getRowStart(), updateModel.getRowEnd());
		final int re = Math.max(updateModel.getRowStart(), updateModel.getRowEnd());
		final int cs = Math.min(updateModel.getColStart(), updateModel.getColEnd());
		final int ce = Math.max(updateModel.getColStart(), updateModel.getColEnd());
		int x;
		int y;
		for (int r = rs; r <= re; r++) {
			for (int c = cs; c <= ce; c++) {
				x = (int) (c * cellSize + 1);
				y = (int) (r * cellSize + 1);
				g2d.setColor(chooseColor(v));
				g2d.fillRect(x, y, s, s);
				if (v == CellValue.BLANK) {
					g2d.setColor(FGCOLOR_BLANK);
					g2d.setStroke(ns);
					g2d.drawLine(x + s - 5, y + 3, x + 3, y + s - 5);
					g2d.drawLine(x + 3, y + 3, x + s - 5, y + s - 5);
					g2d.setStroke(os);
					
				}
				else if (v == CellValue.UNKNOWN) {
					g2d.setColor(FGCOLOR_UNKNOWN);
					g2d.drawString("?", x + ((s - wc) / 2), y + (fm.getHeight() - fm.getDescent()));
				}
			}
		}
	}
	
	protected void paintMinorGrid(Graphics2D g2d) {
		g2d.setColor(FGCOLOR_GRID);
		final int w = getWidth();
		final int h = getHeight();
		int li;
		for (int i = 0; i < dataModel.getRows(); i++) {
			li = (int) (i * cellSize);
			g2d.drawLine(0, li, w, li);
		}
		for (int i = 0; i < dataModel.getCols(); i++) {
			li = (int) (i * cellSize);
			g2d.drawLine(li, 0, li, h);
		}
	}
	
	protected void paintMajorGrid(Graphics2D g2d) {
		g2d.setColor(FGCOLOR_GRID);
		final int w = getWidth();
		final int h = getHeight();
		int li;
		for (int i = 0; i < dataModel.getRows(); i += 5) {
			li = (int) (i * cellSize);
			g2d.drawLine(0, li, w, li);
			g2d.drawLine(0, li + 1, w, li + 1);
		}
		for (int i = 0; i < dataModel.getCols(); i += 5) {
			li = (int) (i * cellSize);
			g2d.drawLine(li, 0, li, h);
			g2d.drawLine(li + 1, 0, li + 1, h);
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
