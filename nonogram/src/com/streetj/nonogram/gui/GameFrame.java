package com.streetj.nonogram.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.streetj.nonogram.Cell;
import com.streetj.nonogram.GameLoader;
import com.streetj.nonogram.GameState;
import com.streetj.nonogram.GameUpdate;
import com.streetj.nonogram.NonogramException;
import com.streetj.nonogram.util.GuiUtil;

public class GameFrame extends JFrame implements SwingConstants, ActionCommands {

	private GameState gameState;
	
	private GameBoard gameBoard;
	private GridUpdateListener gridUpdateListener;
	private GridDataListener gridDataListener;
	
	private CellValueSelectionListener cellValueSelectionListener;
	
	private Map<String, GameAction> gameActions = new HashMap<String, GameAction>();
	
	private JToolBar toolBar;
	private CellValueSelector cellValueSelector;
	private JMenuBar menuBar;
	
	public GameFrame() {
		installGameActions();
		installComponents();
		installDefaults();
		installListeners();
		
		setSize(600, 600);
		setLocationRelativeTo(null);
		setTitle("JNonogram");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	protected void installComponents() {
		menuBar = createMenuBar();
		setJMenuBar(menuBar);
		toolBar = createToolBar();
		add(toolBar, BorderLayout.NORTH);
		gameBoard = new GameBoard(0, 0);
		add(gameBoard);
		cellValueSelector = new CellValueSelector();
		add(cellValueSelector, BorderLayout.EAST);
	}
	
	protected void installListeners() {
		gridUpdateListener = createGridUpdateListener();
		gameBoard.getGridUpdateModel().addGridUpdateListener(gridUpdateListener);
		gridDataListener = createGridDataListener();
		gameBoard.getGridDataModel().addGridDataListener(gridDataListener);
		cellValueSelectionListener = createCellValueSelectionListener();
		cellValueSelector.getModel().addCellValueSelectionListener(cellValueSelectionListener);
		
	}
	
	protected void installDefaults() {
		gameBoard.setVisible(false);
		gameActions.get(ACTION_RESET_PUZZLE).setEnabled(false);
		gameActions.get(ACTION_UNDO).setEnabled(false);
		gameActions.get(ACTION_REDO).setEnabled(false);
	}
	
	protected void installGameActions() {
		gameActions.put(ACTION_OPEN_PUZZLE,
				new GameAction("Open Puzzle", 
						GuiUtil.createImageIcon("sheet-open.png", "Open icon"),
						ACTION_OPEN_PUZZLE,
						KeyEvent.VK_O));
		gameActions.put(ACTION_RESET_PUZZLE,
				new GameAction("Reset Puzzle",
						GuiUtil.createImageIcon("update.gif", "Reset icon"),
						ACTION_RESET_PUZZLE,
						KeyEvent.VK_R));
		gameActions.put(ACTION_UNDO,
				new GameAction("Undo",
						GuiUtil.createImageIcon("arrow-left-16.gif", "Arrow left icon"),
						ACTION_UNDO,
						KeyEvent.VK_Z));
		gameActions.put(ACTION_REDO,
				new GameAction("Redo",
						GuiUtil.createImageIcon("arrow-right-16.gif", "Arror right icon"),
						ACTION_REDO,
						KeyEvent.VK_Y));
	}
	
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		// File menu
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem mi = new JMenuItem(gameActions.get(ACTION_OPEN_PUZZLE));
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);
		
		menuBar.add(fileMenu);
		
		return menuBar;
	}
	
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar(HORIZONTAL);
		toolBar.setFloatable(false);
		
		toolBar.add(gameActions.get(ACTION_OPEN_PUZZLE));
		toolBar.add(gameActions.get(ACTION_RESET_PUZZLE));
		toolBar.add(gameActions.get(ACTION_UNDO));
		toolBar.add(gameActions.get(ACTION_REDO));
		
		return toolBar;
	}
	
	protected GridDataListener createGridDataListener() {
		return new GridDataAdapter() {
			
			@Override
			public void cellsChanged(GridDataEvent e) {
				syncGameActions();
			}
		};
	}
	
	protected GridUpdateListener createGridUpdateListener() {
		return new GridUpdateAdapter() {
			
			@Override
			public void updateStarted(GridUpdateEvent e) {
				doGameUpdate(e, true);
			}
			
			@Override
			public void updateChanged(GridUpdateEvent e) {
				doGameUpdate(e, true);
			}
			
			@Override
			public void updateFinished(GridUpdateEvent e) {
				doGameUpdate(e, false);
				if (gameState.isSolved()) {
					if (gameState.isSolution()) {
						JOptionPane.showMessageDialog(GameFrame.this,
								"Puzzle complete!", "Finished!",
								JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(GameFrame.this,
								"Alternate solution found!?", "Finished!?!?",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
			private void doGameUpdate(GridUpdateEvent e, boolean project) {
				int rs = Math.min(e.rowStart, e.rowEnd);
				int re = Math.max(e.rowStart, e.rowEnd); 
				int cs = Math.min(e.colStart, e.colEnd);
				int ce = Math.max(e.colStart, e.colEnd);
				Cell[] cells = new Cell[(re - rs + 1) * (ce - cs + 1)];
				int i = 0;
				for (int r = rs; r <= re; r++) {
					for (int c = cs; c <= ce; c++) {
						cells[i++] = new Cell(r, c, gameState.getWorkingCellValue(r, c));
					}
				}
				GameUpdate update = new GameUpdate(cells, e.newValue);
				if (project)
					gameState.projectUpdate(update);
				else
					gameState.doUpdate(update);
				gameBoard.getRowCluesModel().setClues(gameState.getRowClues());
				gameBoard.getColCluesModel().setClues(gameState.getColClues());
			}
		};
	}
	
	private CellValueSelectionListener createCellValueSelectionListener() {
		return new CellValueSelectionListener() {
			
			@Override
			public void selectionChanged(CellValueSelectionEvent e) {
				GridUpdateModel gum = gameBoard.getGridUpdateModel();
				gum.setPrimaryValue(e.primaryValue);
				gum.setSecondaryValue(e.secondaryValue);
			}
		};
	}
	
	private void syncGameActions() {
		if (gameState != null) {
			gameActions.get(ACTION_RESET_PUZZLE).setEnabled(!gameState.isEmpty());
			gameActions.get(ACTION_UNDO).setEnabled(gameState.getUndoCount() > 0);
			gameActions.get(ACTION_REDO).setEnabled(gameState.getRedoCount() > 0);
		}
		else {
			gameActions.get(ACTION_RESET_PUZZLE).setEnabled(false);
			gameActions.get(ACTION_UNDO).setEnabled(false);
			gameActions.get(ACTION_REDO).setEnabled(false);
		}
	}
	
	private void syncGameBoard() {
		if (gameState != null) {
			gameBoard.getRowCluesModel().setClues(gameState.getRowClues());
			gameBoard.getColCluesModel().setClues(gameState.getColClues());
			gameBoard.getGridDataModel().setCellValues(gameState.getWorkingCellValues());
		}
	}
	
	private class GameAction extends AbstractAction {
		
		public GameAction(String text, ImageIcon icon, String command) {
			this(text, icon, command, -1);
		}
		
		public GameAction(String text, ImageIcon icon, String command, int mnemonic) {
			super(text, icon);
			putValue(ACTION_COMMAND_KEY, command);
			if (mnemonic != -1)
				putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			String cmd = ae.getActionCommand();
			// Open Puzzle
			if (cmd.equals(ACTION_OPEN_PUZZLE)) {
				if (gameState == null ||
						gameState.isEmpty() || 
						gameState.isSolved() ||
						JOptionPane.showConfirmDialog(GameFrame.this,
								"Abandon current puzzle?", "Open Puzzle",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					JFileChooser puzzleChooser = new JFileChooser();
					puzzleChooser.setFileFilter(new PuzzleFileFilter());
					int rv = puzzleChooser.showOpenDialog(GameFrame.this);
					if ( rv == JFileChooser.APPROVE_OPTION) {
						try {
							gameState = new GameLoader().loadGame(puzzleChooser.getSelectedFile());
							syncGameBoard();
							if (!gameBoard.isVisible())
								gameBoard.setVisible(true);
						}
						catch (NonogramException e) {
							JOptionPane.showMessageDialog(GameFrame.this,
									e.getMessage(),
									"Open Puzzle",
									JOptionPane.ERROR_MESSAGE);
							gameState = null;
						}
					}
				}
				syncGameActions();
			}
			// Reset Puzzle
			if (cmd.equals(ACTION_RESET_PUZZLE)) {
				if (gameState.isEmpty() ||
						JOptionPane.showConfirmDialog(GameFrame.this,
								"Reset and lose changes?", "Reset Puzzle",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					gameState.reset();
					syncGameBoard();
					syncGameActions();
				}
			}
			
			// Undo
			if (cmd.equals(ACTION_UNDO)) {
				gameState.undo();
				syncGameBoard();
				syncGameActions();
			}
			
			// Redo
			if (cmd.equals(ACTION_REDO)) {
				gameState.redo();
				syncGameBoard();
				syncGameActions();
			}
		}
	}
}
