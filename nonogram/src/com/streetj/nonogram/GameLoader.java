package com.streetj.nonogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameLoader {
	
	private static final Pattern sizePattern = Pattern.compile("(\\d+)\\s(\\d+)");
	private static final Pattern cluePattern = Pattern.compile("^(\\d+)-(\\d+)$");
	
	private int rows;
	private int cols;
	
	private ClueArray rowClues;
	private ClueArray colClues;
	private DataGrid solution;
	
	private String title;
	private String author;

	public GameState loadGame(File file) throws NonogramException {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			if (!readHeader(br)) {
				br.close();
				throw new NonogramException("Specified file has malformed header");
			}
			
			if (!readClues(br)) {
				br.close();
				throw new NonogramException("Specified file has malformed clues");
			}
			
			if (!readSolution(br)) {
				br.close();
				throw new NonogramException("Specified file has malformed solution");
			}
			
			if (!doSolutionCheck()) {
				br.close();
				throw new NonogramException("Specified file has malformed data");
			}
			
			br.close();
			return new GameState(title, author, rows, cols, rowClues, colClues, solution);
		}
		catch (FileNotFoundException e) {
			throw new NonogramException("Specified file does not exist", e);
		} catch (UnsupportedEncodingException e) {
			throw new NonogramException("Specified file encoded in unknown format", e);
		} catch (IOException e) {
			throw new NonogramException("Error occurred while reading specified file", e);
		}
	}
	
	private boolean readHeader(BufferedReader br) throws IOException {
		String text = br.readLine();
		if (!"Header".equals(text)) {
			return false;
		}
		text = br.readLine();
		if (text == null)
			return false;
		title = text;
		text = br.readLine();
		if (text == null)
			return false;
		author = text;
		return true;
	}
	
	private boolean readClues(BufferedReader br) throws IOException {
		String text = br.readLine();
		if (!"Clues".equals(text))
			return false;
		text = br.readLine();
		Matcher matcher = sizePattern.matcher(text);
		rows = 0;
		cols = 0;
		if (matcher.matches()) {
			rows = Integer.valueOf(matcher.group(1));
			cols = Integer.valueOf(matcher.group(2));
		}
		else {
			return false;
		}
		text = br.readLine();
		if (!"R".equals(text))
			return false;
		Clue[][] clues = new Clue[rows][];
		for (int i = 0; i < rows; i++) {
			text = br.readLine();
			if (text == null || text.isEmpty())
				return false;
			clues[i] = getClues(text);
		}
		rowClues = new ClueArray(clues);
		text = br.readLine();
		if (!"C".equals(text))
			return false;
		clues = new Clue[cols][];
		for (int i = 0; i < cols; i++) {
			text = br.readLine();
			if (text == null || text.isEmpty())
				return false;
			clues[i] = getClues(text);
			if (clues[i] == null)
				return false;
		}
		colClues = new ClueArray(clues);
		return true;
	}
	
	private Clue[] getClues(String line) {
		Matcher matcher;
		String[] ct = line.split(" ");
		if (ct.length == 0)
			return null;
		Clue[] clues = new Clue[ct.length];
		for (int i = 0; i < ct.length; i++) {
			matcher = cluePattern.matcher(ct[i]);
			if (!matcher.matches())
				return null;
			int s = Integer.valueOf(matcher.group(1));
			CellValue v = CellValue.values()[Integer.valueOf(matcher.group(2))];
			boolean c = s != 0 ? false : true;
			clues[i] = new Clue(s, v, c);
		}
		return clues;
	}
	
	private boolean readSolution(BufferedReader br) throws IOException {
		String text = br.readLine();
		if (!"Solution".equals(text))
			return false;
		CellValue[][] values = new CellValue[rows][cols];
		for (int r = 0; r < rows; r++) {
			text = br.readLine();
			if (text == null || text.isEmpty())
				return false;
			String[] v = text.split(" ");
			if (v.length != cols)
				return false;
			for (int c = 0; c < cols; c++) {
				values[r][c] = CellValue.fromString(v[c]);
			}
		}
		solution = new DataGrid(values);
		return true;
	}
	
	private boolean doSolutionCheck() {
		for (int r = 0; r < rows; r++) {
			if (!rowClues.compareToClues(r, solution.getRowCellValues(r)))
				return false;
		}
		for (int c = 0; c < cols; c++) {
			if (!colClues.compareToClues(c, solution.getColCellValues(c)))
				return false;
		}
		return true;
	}
}
