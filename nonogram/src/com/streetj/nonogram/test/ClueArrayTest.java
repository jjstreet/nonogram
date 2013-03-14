package com.streetj.nonogram.test;

import com.streetj.nonogram.CellValue;
import com.streetj.nonogram.Clue;
import com.streetj.nonogram.ClueArray;

public class ClueArrayTest {

	public static void main(String[] args) {
		ClueArray ca = new ClueArray();
		ca.setClues(new Clue[][] {
				new Clue[] { new Clue(7), new Clue(2)}});
		
		CellValue[] values = new CellValue[] {
				CellValue.UNKNOWN,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN};
		System.out.println(ca);
		ca.updateClues(0, values);
		System.out.println(ca);
		values = new CellValue[] {
				CellValue.UNKNOWN,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.BLACK,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN,
				CellValue.UNKNOWN};
		ca.updateClues(0, values);
		System.out.println(ca);
	}
}
