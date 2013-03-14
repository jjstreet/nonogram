package com.streetj.nonogram.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.streetj.nonogram.Resource;

public class GuiUtil {

	public static final int getMaxFontSize(Graphics g, Font f, String t, Dimension s) {
		return getMaxFontSize(g, f, t, s.width, s.height);
	}
	
	public static final int getMaxFontSize(Graphics g, Font f, String t, int w, int h) {
		int min = 0;
		int max = 288;
		int c = f.getSize();
		
		FontMetrics fm;
		int fw;
		int fh;
		while (max - min > 2) {
			fm = g.getFontMetrics(new Font(f.getName(), f.getStyle(), c));
			fw = SwingUtilities.computeStringWidth(fm, t);
			fh = fm.getHeight();
			if (fw > w || fh > h) {
				max = c;
				c = (max + min) / 2;
			}
			else {
				min = c;
				c = (min + max) / 2;
			}
		}
		return c;
	}
	
	public static final ImageIcon createImageIcon(String name, String description) {
		URL url = GuiUtil.class.getResource(Resource.RES_IMAGE_PATH + name);
		return new ImageIcon(url, description);
	}
}
