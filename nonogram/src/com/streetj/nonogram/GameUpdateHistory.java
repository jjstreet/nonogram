package com.streetj.nonogram;

import java.util.Deque;
import java.util.LinkedList;

public class GameUpdateHistory {

	private Deque<GameUpdate> stack;
	
	public GameUpdateHistory() {
		stack = new LinkedList<GameUpdate>();
	}
	
	public void put(GameUpdate gameUpdate) {
		stack.offerFirst(gameUpdate);
	}
	
	public GameUpdate take() {
		return stack.pollFirst();
	}
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public int size() {
		return stack.size();
	}
	
	public void clear() {
		stack.clear();
	}
}
