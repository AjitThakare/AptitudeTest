package psl.com.aptitudetest;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class DataProviderClass {
public DataProviderClass() {
	super();
	// TODO Auto-generated constructor stub
}

	public String[][] getTableArray(String xlFilePath, String sheetName,
			String pointer) {
		String[][] tabArray = null;
		try {
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			Cell tableStart = sheet.findCell(pointer);

			int startRow, startCol, endRow, endCol, ci, cj;

			startRow = tableStart.getRow();
			startCol = tableStart.getColumn();

			Cell tableEnd = sheet.findCell(pointer, startCol + 1,
					startRow + 1, 100, 64000, false);

			endRow = tableEnd.getRow();
			endCol = tableEnd.getColumn();
			tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
			ci = 0;

			for (int i = startRow + 1; i < endRow; i++, ci++) {
				cj = 0;
				for (int j = startCol + 1; j < endCol; j++, cj++) {
					tabArray[ci][cj] = sheet.getCell(j, i).getContents();
				}
			}
		} catch (Exception e) {

            e.printStackTrace();
			System.out.println("error in DataProviderClass.getTableArray()");

		}

		return (tabArray);
	}
	
public String [] getSimpleArray(String xlFilePath, String sheetName,
		String pointer)
{
	String[] tabArray = null;
	try {
		Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
		Sheet sheet = workbook.getSheet(sheetName);
		Cell tableStart = sheet.findCell(pointer);

		int startRow, endRow, colNo,ci;

		startRow = tableStart.getRow();
		colNo=tableStart.getColumn();
		Cell tableEnd = sheet.findCell(pointer, colNo ,startRow + 1, 100, 64000, false);

		endRow = tableEnd.getRow();
		tabArray = new String[endRow - startRow - 1];
		ci = 0;

		for (int i = startRow + 1; i < endRow; i++, ci++) {
				tabArray[ci] = sheet.getCell(colNo, i).getContents();
			}
	} catch (Exception e) {
		System.out.println("error in DataProviderClass.getSimpleArray()");

	}

	return tabArray;
	}


}