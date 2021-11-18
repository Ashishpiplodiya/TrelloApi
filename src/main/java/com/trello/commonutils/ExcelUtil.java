package com.trello.commonutils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.trello.baseutil.BaseTest;

/**
 * Description: Implements generic methods to perform operations on Excel
 * Workbook.
 * 
 * @author: Sajal, Vikas
 */
public class ExcelUtil {
	/* Private constructor */
	private ExcelUtil() {

	}


/**
 * Description: Implements writing contents to Excel file
 * 
 * @author: Sajal, Vikas
 * @param fileWithPath,data
 */
	public static synchronized void writeToTextFile(String fileWithPath, String data) {
		try {
			Files.write(Paths.get(fileWithPath), data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Implements conversion of Array into String
	 * 
	 * @author: Sajal, Vikas
	 * @param twoDArray
	 */
	public static synchronized String convertArrayToFormattedString(String[][] twoDArray) {
		String data = "";

		for (int i = 0; i < twoDArray.length; i++) {
			for (int j = 0; j < twoDArray[i].length; j++) {
				if (twoDArray[i].length - j != 1) {
					data = data + twoDArray[i][j].trim() + "\t";
				} else {
					data = data + twoDArray[i][j];
				}
			}
			data = data + "\n";
		}

		return data;
	}

	/**
	 * Description: Implements getting Sheet Name
	 * 
	 * @author: Sajal, Vikas
	 * @param xlPath
	 */
	public synchronized static String getSheetName(String xlPath) {
		String sheetName = null;
		try {
			FileInputStream fis = new FileInputStream(xlPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			sheetName = wb.getSheetName(0);
			wb.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetName;
	}
	
	/**
	 * Description: Implements fetching Row Data from Excel using Fillo
	 * 
	 * @author: Sajal, Vikas
	 * @param xlPath
	 */
	public static synchronized String[][] getRowDataFromExcelUsingFillo(String xlPath, String sql) {

		Fillo fillo = new Fillo();
		String[][] tableValues = null;
		Connection connection;
		try {
			connection = fillo.getConnection(xlPath);
			Recordset recordset = connection.executeQuery(sql);
		
			ArrayList<String> lstFieldNames = recordset.getFieldNames();


			tableValues = new String[recordset.getCount()][lstFieldNames.size()];

			int rowno = 0;

			while (recordset.next()) {

				int colno = 0;

				for (String fieldName : lstFieldNames) {
					tableValues[rowno][colno++] = recordset.getField(fieldName);
					

				}
				rowno++;

			}

			recordset.close();
			connection.close();
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			System.out.println("Fillo exception");
			e.printStackTrace();
		}

		return tableValues;
	}

	/**
	 * Description:Fetches the row count in the specified sheet
	 * 
	 * @author:Sajal, Vikas
	 * @param sPath
	 * @param sSheet
	 */
	public static synchronized int getRowCount(String sPath, String sSheet) {
		int iRowNum = 0;
		try {
			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			iRowNum = sht.getLastRowNum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iRowNum;
	}

	/**
	 * Description:Fetches the column count in the specified sheet
	 * 
	 * @author:Sajal, Vikas
	 * @param sSheet
	 * @parm sPath
	 */
	public static synchronized int getColumnCount(String sPath, String sSheet) {
		int colnum = 0;
		try {
			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			colnum = sht.getRow(0).getPhysicalNumberOfCells();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return colnum;
	}

	/**
	 * Description:Fetches the data from the specified cell
	 * 
	 * @author:Sajal, Vikas
	 * @param xlPAth
	 * @param sheetName
	 * @param rowNo
	 * @param colNo
	 */
	public static synchronized String getCellData(String xlPAth, String sheetName, int rowNo, int colNo) {
		DataFormatter dataFormatter = new DataFormatter();

		int iRowNum = 0;
		String data = null;
		try {
			FileInputStream fis = new FileInputStream(xlPAth);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sheetName);
			iRowNum = sht.getLastRowNum();
			if (rowNo <= iRowNum) {
				Cell cell = sht.getRow(rowNo).getCell(colNo);
				data = dataFormatter.formatCellValue(cell);
			} else {
				BaseTest.logger.info("Please provide the valid row count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Description : Fetches the specified row data from the Excel sheet
	 * 
	 * @author:Sajal, Vikas
	 * @param sFilePath
	 * @param sSheet
	 * @param rowNo
	 */
	public static synchronized String[] getRowData(String sFilePath, String sSheet, int rowNo) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilePath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iCellNum = sht.getRow(rowNo).getPhysicalNumberOfCells();
			sData = new String[iCellNum];
			for (int j = 0; j < iCellNum; j++) {
				Cell cell = sht.getRow(rowNo).getCell(j);
				sData[j] = dataFormatter.formatCellValue(cell);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return sData;
	}

	/**
	 * Description :Fetches the specified column data from the Excel sheet
	 * 
	 * @author:Sajal, Vikas
	 * @param sFilePath
	 * @param sSheet
	 * @param colno
	 */
	public static synchronized String[] getColoumData(String sFilePath, String sSheet, int colno) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilePath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			sData = new String[iRowNum];

			for (int i = 1; i <= iRowNum; i++) {
				Cell cell = sht.getRow(i).getCell(colno);
				sData[i - 1] = dataFormatter.formatCellValue(cell);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/**
	 * Description Fetches the column index for a given value of a cell value from
	 * the first/header row.
	 * 
	 * @author:Sajal, Vikas
	 * @param sFilePath
	 * @param sSheet
	 * @param colName
	 */
	public static synchronized int getColoumIndex(String filepath, String sSheet, String colName) {
		String[] firstrow = getRowData(filepath, sSheet, 0);
		int index = 0;
		for (int i = 0; i < firstrow.length; i++) {
			if (firstrow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;

	}

	/**
	 * Description: Checks if an array contains blank/default value.
	 * 
	 * @author:Sajal, Vikas
	 * @param data
	 * @return blank
	 */
	public static synchronized boolean doesArrayContainsBlank(String[] data) {
		boolean blank = false;

		for (int i = 0; i < data.length; i++) {
			if (data[i].isEmpty() || data[i] == null) {
				blank = true;
				break;
			}
		}
		return blank;
	}

	/**
	 * Description :Reads the Excel data from a specified Excel sheet based on
	 * TestCaseId.
	 * 
	 * @author:Sajal, Vikas
	 * @param sFilePath
	 * @param sSheet
	 * @param sTestCaseId
	 * @return SData
	 */
	public static synchronized String[] toReadExcelData(String sFilePath, String sSheet, String sTestCaseId) {
		DataFormatter dataFormatter = new DataFormatter();
		String SData[] = null;
		try {
			// File Read
			FileInputStream fis = new FileInputStream(sFilePath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			for (int i = 0; i <= iRowNum; i++) {
				if (sht.getRow(i).getCell(0).toString().equals(sTestCaseId)) {
					int iCellNum = sht.getRow(i).getPhysicalNumberOfCells();
					SData = new String[iCellNum];
					for (int j = 0; j < iCellNum; j++) {
						Cell cell = sht.getRow(i).getCell(j);
						SData[j] = dataFormatter.formatCellValue(cell);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return SData;

	}

	/**
	 * Description :Fetches the column index
	 * 
	 * @author:Sajal, Vikas
	 * @param sFilePath
	 * @param sSheet
	 * @param colName
	 * @param firstRowName
	 * @return index
	 */
	public static synchronized int getColoumIndex(String filepath, String sSheet, String colName, String firstRowName) {
		String[] firstRow = ExcelUtil.toReadExcelData(filepath, sSheet, firstRowName);
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Description: Fetches the cell count in the specified row.
	 * 
	 * @author:Sajal, Vikas
	 * @param sPath
	 * @param sSheet
	 * @param sSheeet
	 * @param row
	 * @return column
	 */
	public static synchronized int getCellCount(String sPath, String sSheeet, int row) {
		int colnum = 0;
		try {
			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheeet);
			colnum = sht.getRow(row).getPhysicalNumberOfCells();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return colnum;
	}

	/**
	 * Description: This method is used to delete the file if at all it exists to
	 * avoid interaction when New ExcelFile is generated
	 * 
	 * @author: Sajal, Vikas
	 * @param filePath
	 * @param fileName
	 * 
	 */
	private static void deleteFileIfExists(String filePath, String fileName) {
		String[] fileNameSplit = fileName.split("[.]");
		String newFileName = fileNameSplit[0] + "_1" + ".xlsx";
		Path path = Paths.get(filePath + "\\" + newFileName);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}