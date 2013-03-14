package com.streetj.nonogram.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.streetj.nonogram.CellValue;

public class GameBoard extends JComponent implements GuiConstants {

	private GridDataModel gridDataModel;
	private GridUpdateModel gridUpdateModel;
	private ClueBoxModel rowCluesModel;
	private ClueBoxModel colCluesModel;
	
	private JScrollPane scrollPane;
	private Grid grid;
	private ClueBox rowClues;
	private ClueBox colClues;
	private JPanel corner;
	
	private JScrollBar gridHsb;
	private JScrollBar gridVsb;
	
	private MouseAdapter gridMouseAdapter;
	
	
	public GameBoard(int rows, int cols) {
		gridDataModel = new GridDataModel(rows, cols);
		gridUpdateModel = new GridUpdateModel();
		rowCluesModel = new ClueBoxModel(rows);
		colCluesModel = new ClueBoxModel(cols);
		
		setLayout(new BorderLayout());
		buildComponent();
		installListeners();
	}
	
	
	protected void buildComponent() {
		scrollPane = new JScrollPane();
		gridHsb = scrollPane.getHorizontalScrollBar();
		gridVsb = scrollPane.getVerticalScrollBar();
		grid = new Grid(gridDataModel, gridUpdateModel);
		rowClues = new ClueBox(false, rowCluesModel);
		colClues = new ClueBox(true, colCluesModel);
		corner = new JPanel();
		corner.setOpaque(true);
		corner.setBorder(BorderFactory.createLineBorder(FGCOLOR_GRID, 2));
		scrollPane.setViewportView(grid);
		scrollPane.setColumnHeaderView(colClues);
		scrollPane.setRowHeaderView(rowClues);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	protected void installListeners() {
		gridMouseAdapter = createGridMouseAdapter();
		grid.addMouseListener(gridMouseAdapter);
		grid.addMouseMotionListener(gridMouseAdapter);
	}
	
	private MouseAdapter createGridMouseAdapter() {
		return new MouseAdapter() {
			
			private static final int B1 = MouseEvent.BUTTON1;
			private static final int B2 = MouseEvent.BUTTON2;
			private static final int B3 = MouseEvent.BUTTON3;
			private static final int B1M = MouseEvent.BUTTON1_DOWN_MASK;
			private static final int B2M = MouseEvent.BUTTON2_DOWN_MASK;
			private static final int B3M = MouseEvent.BUTTON3_DOWN_MASK;
			/**
			 * B3MD is for capturing autoscrolling behavior on mouse 3 clicks
			 * For whatever reason, AWT changes the modifiers when button 3
			 * dragging goes outside the visible area of the grid.
			 */
			private static final int B3MD = MouseEvent.BUTTON3_DOWN_MASK | MouseEvent.META_DOWN_MASK;
			private static final int SHIFT = MouseEvent.SHIFT_DOWN_MASK;
			
			boolean pressed;
			int buttonPressed;
			boolean moving;
			
			Point pointPressed = new Point();
			Point pointDelta = new Point();
			
			int mod;
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (!pressed) {
					mod = e.getModifiersEx();
					if (mod == B1M || mod == B3M) {
						// Instantiating a move
						buttonPressed = e.getButton();
						int row = grid.convertYToRow(e.getY());
						int col = grid.convertXToCol(e.getX());
						CellValue newValue = CellValue.UNKNOWN;
						if (buttonPressed == B1 && gridDataModel.getCellValue(row, col) != gridUpdateModel.getPrimaryValue())
							newValue = gridUpdateModel.getPrimaryValue();
						else if (buttonPressed == B3 && gridDataModel.getCellValue(row, col) != gridUpdateModel.getSecondaryValue())
							newValue = gridUpdateModel.getSecondaryValue();
						gridUpdateModel.setNewValue(newValue);
						gridUpdateModel.startUpdate(row, col);
						pressed = true;
						moving = false;
					}
					else if (mod == B2M || mod == (B1M | SHIFT)) {
						// Instantiating a drag
						buttonPressed = e.getButton();
						pointPressed.setLocation(e.getXOnScreen(), e.getYOnScreen());
						setCursor(new Cursor(Cursor.HAND_CURSOR));
						pressed = true;
						moving = true;
					}
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (pressed) {
					mod = e.getModifiersEx();
					if (!moving && (buttonPressed == B1 && mod == B1M ||
							buttonPressed == B3 && mod == B3M ||
							buttonPressed == B3 && mod == B3MD)) {
						// Dragging udpate
						int row = grid.convertYToRow(e.getY());
						int col = grid.convertXToCol(e.getX());
						int dr = Math.abs(row - gridUpdateModel.getRowStart());
						int dc = Math.abs(col - gridUpdateModel.getColStart());
						if (dr > dc) {
							col = gridUpdateModel.getColStart();
						}
						else if (dc > dr) {
							row = gridUpdateModel.getRowStart();
						}
						else {
							row = gridUpdateModel.getRowStart();
							col = gridUpdateModel.getColStart();
						}
						if (row != gridUpdateModel.getRowEnd() || col != gridUpdateModel.getColEnd()) {
							gridUpdateModel.changeUpdate(row, col);
						}
						Rectangle vr = new Rectangle(
								grid.convertColToX(col),
								grid.convertRowToY(row),
								grid.getCellSize(),
								grid.getCellSize());
						grid.scrollRectToVisible(vr);
//						grid.scrollRectToVisible(new Rectangle(e.getX(), e.getY(), 1, 1));
					}
					else if (moving && (buttonPressed == B1 && mod == (B1M | SHIFT) ||
							buttonPressed == B2 && mod == B2M)) {
						// Dragging scroll
						pointDelta.setLocation(e.getXOnScreen() - pointPressed.x, e.getYOnScreen() - pointPressed.y);
						gridHsb.setValue(gridHsb.getValue() - pointDelta.x);
						gridVsb.setValue(gridVsb.getValue() - pointDelta.y);
						pointPressed.setLocation(e.getXOnScreen(), e.getYOnScreen());
					}
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (pressed && buttonPressed == e.getButton()) {
					if (!moving)
						gridUpdateModel.finishUpdate();
					else
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					buttonPressed = 0;
					pressed = false;
					moving = false;
				}
			}
		};
	}
	
	public GridDataModel getGridDataModel() {
		return gridDataModel;
	}
	
	public GridUpdateModel getGridUpdateModel() {
		return gridUpdateModel;
	}
	
	public ClueBoxModel getRowCluesModel() {
		return rowCluesModel;
	}
	
	public ClueBoxModel getColCluesModel() {
		return colCluesModel;
	}
}
