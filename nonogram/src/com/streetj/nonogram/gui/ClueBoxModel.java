package com.streetj.nonogram.gui;

import javax.swing.event.EventListenerList;

import com.streetj.nonogram.Clue;

public class ClueBoxModel {

	EventListenerList listeners = new EventListenerList();
	
	private int length;
	
	private int maxClues;
	
	private int maxClueSize;
	
	private Clue[][] clues;
	
	public ClueBoxModel() {
		this(0);
	}
	
	public ClueBoxModel(int length) {
		this(new Clue[length][0]);
	}
	
	public ClueBoxModel(Clue[][] clues) {
		length = clues.length;
		this.clues = new Clue[length][];
		Clue[] c;
		int len;
		for (int i = 0; i < length; i++) {
			len = clues[i].length;
			c = new Clue[len];
			for (int j = 0; j < len; j++) {
				c[j] = new Clue(clues[i][j]);
				maxClueSize = Math.max(maxClueSize, clues[i][j].size);
			}
			this.clues[i] = c;
			maxClues = Math.max(maxClues, len);
		}
	}
	
	public int getLength() {
		return length;
	}
	
	public int getMaxClues() {
		return maxClues;
	}
	
	public int getMaxClueSize() {
		return maxClueSize;
	}
	
	public Clue[][] getClues() {
		Clue[][] cs = new Clue[length][];
		for (int i = 0; i < length; i++) {
			cs[i] = getClues(i);
		}
		return cs;
	}

	public Clue[] getClues(int loc) {
		Clue[] c = new Clue[clues[loc].length];
		for (int i = 0; i < c.length; i++) {
			c[i] = getClue(loc, i);
		}
		return c;
	}

	public Clue getClue(int loc, int idx) {
		return new Clue(clues[loc][idx]);
	}

	public void setClues(Clue[][] clues) {
		length = clues.length;
		this.clues = new Clue[length][];
		Clue[] c;
		int len;
		for (int i = 0; i < length; i++) {
			len = clues[i].length;
			c = new Clue[len];
			for (int j = 0; j < len; j++) {
				c[j] = new Clue(clues[i][j]);
				maxClueSize = Math.max(maxClueSize, clues[i][j].size);
			}
			this.clues[i] = c;
			maxClues = Math.max(maxClues, len);
		}
		fireUpdate();
	}

	public void addClueBoxUpdateListener(ClueBoxUpdateListener listener) {
		listeners.add(ClueBoxUpdateListener.class, listener);
	}
	
	public void removeClueBoxUpdateListener(ClueBoxUpdateListener listener) {
		listeners.remove(ClueBoxUpdateListener.class, listener);
	}
	
	protected void fireUpdate() {
		ClueBoxEvent event = new ClueBoxEvent(this);
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == ClueBoxUpdateListener.class)
				((ClueBoxUpdateListener) l[i + 1]).clueBoxUpdated(event);
		}
	}
}
