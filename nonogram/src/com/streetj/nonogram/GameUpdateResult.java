package com.streetj.nonogram;

public class GameUpdateResult {

	public final boolean rowCluesUpdated;
	public final boolean colCluesUpdated;
	public final boolean gameCompleted;
	
	public GameUpdateResult(boolean rowCluesUpdated, boolean colCluesUpdated) {
		this(rowCluesUpdated, colCluesUpdated, false);
	}
	
	public GameUpdateResult(boolean rowCluesUpdate, boolean colCluesUpdated, boolean gameCompleted) {
		this.rowCluesUpdated = rowCluesUpdate;
		this.colCluesUpdated = colCluesUpdated;
		this.gameCompleted = gameCompleted;
	}
}
