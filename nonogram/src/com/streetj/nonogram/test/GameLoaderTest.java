package com.streetj.nonogram.test;

import java.io.File;

import com.streetj.nonogram.GameLoader;
import com.streetj.nonogram.GameState;

public class GameLoaderTest {

	public static void main(String[] args) throws Exception {
		GameState gameState = new GameLoader().loadGame(new File("puzzles/small fish.ngf"));
	}
}
