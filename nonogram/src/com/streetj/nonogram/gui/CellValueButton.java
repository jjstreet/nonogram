package com.streetj.nonogram.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.streetj.nonogram.CellValue;
import com.streetj.nonogram.util.GuiUtil;

public class CellValueButton extends JButton implements GuiConstants {

	private CellValue value;
	
	public CellValueButton(CellValue value) {
		super();
		this.value = value;
		Dimension d = new Dimension(24, 24);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
	}
	
	public CellValue getCellValue() {
		return value;
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		super.paintBorder(g);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int w = 16;
		int h = 16;
		Graphics2D g2d = (Graphics2D) g.create();
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
				GuiUtil.getMaxFontSize(g2d, of, "?", w, h));
		g2d.setFont(nf);
		final FontMetrics fm = g2d.getFontMetrics();
		
		int dx = 3;
		int dy = 3;
		int fw = fm.stringWidth("?");
		int fh = fm.getHeight() - fm.getDescent();
		if (isSelected()) {
			dx = 4;
			dy = 4;
		}
		
		g2d.setColor(chooseColor(value));
		g2d.fillRect(dx, dy, w, h);
		g2d.setColor(FGCOLOR_GRID);
		g2d.drawRect(dx, dy, w, h);
		
		if (value == CellValue.UNKNOWN) {
			g2d.setColor(FGCOLOR_UNKNOWN);
			g2d.drawString("?", w / 2 - fw / 2 + dx, fh + dy);
		}
		
		if (value == CellValue.BLANK) {
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(dx + 4, dy + 4, dx + w - 4, dy + h - 4);
			g2d.drawLine(dx + w - 4, dy + 4, dx + 4, dy + h - 4);
		}
		
		if (isSelected()) {
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRect(0, 0, getWidth(), getHeight());
		}
		g2d.dispose();
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
}
