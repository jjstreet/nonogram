package com.streetj.nonogram.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PuzzleFileFilter extends FileFilter {
	
	@Override
	public boolean accept(File f) {
		return f.getPath().endsWith(".ngf") || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Nonogram files";
	}

}
