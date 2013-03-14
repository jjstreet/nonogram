package com.streetj.nonogram.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.streetj.nonogram.CellValue;

public class CellValueSelector extends JToolBar implements GuiConstants, SwingConstants {
	
	private CellValueButton[] primaryButtons;
	private CellValueButton[] secondaryButtons;
	
	private CellValueSelectionModel model;
	
	private CellValueSelectionListener listener;
	
	public CellValueSelector() {
		this(null);
	}
	
	public CellValueSelector(CellValueSelectionModel model) {
		super(VERTICAL);
		setFloatable(false);
		if (model == null)
			model = new CellValueSelectionModel();
		this.model = model;
		installComponents();
		installListeners();
		updateButtons(primaryButtons, model.getPrimaryValue());
		updateButtons(secondaryButtons, model.getSecondaryValue());
	}
	
	public CellValueSelectionModel getModel() {
		return model;
	}
	
	protected void installComponents() {
		CellValue[] values = CellValue.values();
		primaryButtons = new CellValueButton[values.length];
		secondaryButtons = new CellValueButton[values.length];
		CellValue v;
		CellValueButton b;
		for (int i = 0; i < values.length; i++) {
			v = values[i];
			b = new CellValueButton(v);
			b.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					CellValueButton cvb = (CellValueButton) e.getSource();
					if (!cvb.isSelected()) {
						if (model != null)
							model.setPrimaryValue(cvb.getCellValue());
					}
				}
			});
			primaryButtons[i] = b;
			add(b);
		}
		addSeparator();
		for (int i = 0; i < values.length; i++) {
			v = values[i];
			b = new CellValueButton(v);
			b.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					CellValueButton cvb = (CellValueButton) e.getSource();
					if (!cvb.isSelected()) {
						if (model != null)
							model.setSecondaryValue(cvb.getCellValue());
					}
				}
			});
			secondaryButtons[i] = b;
			add(b);
		}
	}
	
	protected void updateButtons(CellValueButton[] buttons, CellValue value) {
		CellValueButton cvb;
		for (int i = 0; i < buttons.length; i++) {
			cvb = buttons[i];
			cvb.setSelected(cvb.getCellValue() == value);
		}
		repaint();
	}
	
	protected void installListeners() {
		listener = createCellValueSelectionListener();
		if (model != null)
			model.addCellValueSelectionListener(listener);
	}
	
	private CellValueSelectionListener createCellValueSelectionListener() {
		return new CellValueSelectionListener() {
			
			@Override
			public void selectionChanged(CellValueSelectionEvent e) {
				updateButtons(primaryButtons, e.primaryValue);
				updateButtons(secondaryButtons, e.secondaryValue);
			}
		};
	}
	
	
}
