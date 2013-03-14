package com.streetj.nonogram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClueArray {

	private int length;
	private int maxClues;
	private Clue[][] clues;
	private Pattern[] forwardPatterns;
	private Pattern[] backwardPatterns;
	
	private boolean[] forwardMatchResults;
	private boolean[] backwardMatchResults;
	
	private Matcher forwardMatcher;
	private Matcher backwardMatcher;
	
	public ClueArray() {
		this.length = 0;
		this.maxClues = 0;
		this.clues = new Clue[0][0];
		this.forwardPatterns = new Pattern[0];
		this.backwardPatterns = new Pattern[0];
		this.forwardMatchResults = new boolean[0];
		this.backwardMatchResults = new boolean[0];
	}
	
	public ClueArray(Clue[][] clues) {
		setClues(clues);
	}
	
	public int getLength() {
		return length;
	}
	
	public int getMaxClues() {
		return maxClues;
	}
	
	public Clue[][] getClues() {
		Clue[][] cs = new Clue[length][];
		for (int i = 0; i < length; i++) {
			Clue[] c = new Clue[clues[i].length];
			System.arraycopy(clues[i], 0, c, 0, clues[i].length);
			cs[i] = c;
		}
		return cs;
	}
	
	public boolean isComplete() {
		int len;
		for (int i = 0; i < length; i++) {
			len = clues[i].length;
			for (int j = 0; j < len; j++) {
				if (!clues[i][j].complete) return false;
			}
		}
		return true;
	}
	
	public boolean isComplete(int loc) {
		int len = clues[loc].length;
		for (int i = 0; i < len; i++) {
			if (!clues[loc][i].complete) return false;
		}
		return true;
	}
	
	public boolean isComplete(int loc, int idx) {
		return clues[loc][idx].complete;
	}
	
	public void setClues(Clue[][] clues) {
		length = clues.length;
		this.clues = new Clue[length][];
		forwardPatterns = new Pattern[length];
		backwardPatterns = new Pattern[length];
		Clue[] c;
		int len;
		for (int i = 0; i < length; i++) {
			len = clues[i].length;
			c = new Clue[len];
			for (int j = 0; j < len; j++) {
				c[j] = new Clue(clues[i][j]);
			}
			this.clues[i] = c;
			forwardPatterns[i] = buildForwardCluePattern(c);
			backwardPatterns[i] = buildBackwardCluePattern(c);
			maxClues = Math.max(maxClues, len);
		}
		forwardMatchResults = new boolean[maxClues];
		backwardMatchResults = new boolean[maxClues];
	}
	
	private Pattern buildForwardCluePattern(Clue[] clues) {
		final int len = clues.length;
		StringBuilder sb = new StringBuilder();
		sb.append("^[")
				.append(CellValue.UNKNOWN.idStr)
				.append(CellValue.BLANK.idStr)
				.append("]*");
		for (int i = 0; i < len; i++) {
			sb.append("(?:(?<!")
					.append(clues[i].value.idStr)
					.append(")[01]*|[01]+)([")
					.append(CellValue.UNKNOWN.idStr)
					.append(clues[i].value.idStr)
					.append("]{")
					.append(clues[i].size)
					.append("})");
		}
		sb.append("[")
				.append(CellValue.UNKNOWN.idStr)
				.append(CellValue.BLANK.idStr)
				.append("]*$");
		return Pattern.compile(sb.toString());
	}
	
	private Pattern buildBackwardCluePattern(Clue[] clues) {
		final int len = clues.length;
		StringBuilder sb = new StringBuilder();
		sb.append("^[")
				.append(CellValue.UNKNOWN.idStr)
				.append(CellValue.BLANK.idStr)
				.append("]*");
		for (int i = len - 1; i >= 0; i--) {
			sb.append("(?:(?<!")
					.append(clues[i].value.idStr)
					.append(")[01]*|[01]+)([")
					.append(CellValue.UNKNOWN.idStr)
					.append(clues[i].value.idStr)
					.append("]{")
					.append(clues[i].size)
					.append("})");
		}
		sb.append("[")
				.append(CellValue.UNKNOWN.idStr)
				.append(CellValue.BLANK.idStr)
				.append("]*$");
		return Pattern.compile(sb.toString());
	}
	
	public boolean compareToClues(int loc, CellValue[] values) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i].idStr);
		}
		forwardMatcher = forwardPatterns[loc].matcher(sb.toString());
		return forwardMatcher.matches();
	}
	
	public void updateClues(int loc, CellValue[] values) {
		StringBuilder fsb = new StringBuilder();
		StringBuilder bsb = new StringBuilder();
		final int vlen = values.length;
		for (int f = 0, b = vlen - 1; f < vlen; f++, b--) {
			fsb.append(values[f].idStr);
			bsb.append(values[b].idStr);
		}
		forwardMatcher = forwardPatterns[loc].matcher(fsb.toString());
		backwardMatcher = backwardPatterns[loc].matcher(bsb.toString());
		final Clue[] cs = clues[loc];
		final int clen = cs.length;
		if (forwardMatcher.matches() && backwardMatcher.matches()) {
			for (int i = 0, f = 1, b = clen; i < clen; i++, f++, b--) {
				forwardMatchResults[i] = !forwardMatcher.group(f).contains(CellValue.UNKNOWN.idStr);
				backwardMatchResults[i] = !backwardMatcher.group(b).contains(CellValue.UNKNOWN.idStr);
			}
			for (int i = 0; i < clen; i++) {
				cs[i] = new Clue(cs[i], forwardMatchResults[i] && backwardMatchResults[i]);
			}
		}
		else {
			for (int i = 0; i < clen; i++) {
				cs[i] = new Clue(cs[i], false);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ClueArray[")
				.append(length)
				.append(" ")
				.append(maxClues)
				.append("] {");
		int len;
		Clue c;
		for (int i = 0; i < length; i++) {
			sb.append("(");
			len = clues[i].length;
			for (int j = 0; j < len; j++) {
				c = clues[i][j];
				if (j > 0)
					sb.append(", ");
				sb.append(c.size)
					.append("-")
					.append(c.value.id)
					.append("-")
					.append(c.complete ? "T" : "F");
			}
			sb.append(")");
		}
		sb.append("}");
		return sb.toString();
	}
}
