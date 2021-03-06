package com.ctylor.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ctylor.config.AppProps;
import com.ctylor.domain.Items;
import com.ctylor.domain.ResultObj;

public class ExcelParser {

	LinkedList<String> missedElements = new LinkedList<String>();
	final static Logger logger = LogManager.getLogger(ExcelParser.class);

	private static ExcelParser single_instance = getInstance();
	static Map<Integer, String> column_mapper = new HashMap<Integer, String>();
	String filePath;
	String sheetName;

	public static ExcelParser getInstance() {
		if (single_instance == null)
			single_instance = new ExcelParser();
		return single_instance;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public ResultObj parse() throws IOException {

		ResultObj resObj = new ResultObj();
		FileInputStream fo = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fo);
		int sheets = workbook.getNumberOfSheets();
		LinkedList<Items> items = new LinkedList<Items>();
		for (int i = 0; i < sheets; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase(this.sheetName)) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rows = sheet.iterator();
				Row firstRow = rows.next();
				// set headers to column_mapper
				setColumnMapper(firstRow.cellIterator());
				// set data to object

				while (rows.hasNext()) {
					Items item = new Items();
					Row row = rows.next();
					int rowIndex = row.getRowNum();
					item.setId(rowIndex);
					item = setValue(row.cellIterator(), item);
					if (!"NULL".equalsIgnoreCase(item.getScanCode())) {
						items.add(item);
					} else {
						missedElements.add(item.getNum());
					}

				}

			}

		}

		resObj.setItems(items);
		resObj.setSkippedElements(missedElements);

		return resObj;

	}

	private Items setValue(Iterator<Cell> cellIterator, Items item) {

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			String field = column_mapper.get(cell.getColumnIndex());

			if ("Num".equalsIgnoreCase(field)) {
				try {
					String value = String.valueOf((long) cell.getNumericCellValue());
					item.setNum(value);
				} catch (java.lang.IllegalStateException e) {
					logger.trace(" Exception occured in Num for the value : ");
				}

			} else if ("Class".equalsIgnoreCase(field)) {

				try {
					String value = cell.getStringCellValue();
					item.set_class(value);
				} catch (java.lang.IllegalStateException e) {
					logger.trace(" Exception occured in Class for the Num : " + item.getNum());
				}

			} else if ("Cat".equalsIgnoreCase(field)) {
				try {
					String value = cell.getStringCellValue();
					item.setCat(value);
				} catch (java.lang.IllegalStateException e) {
					logger.trace(" Exception occured in Cat for the Num : " + item.getNum());
				}

			} else if ("ListPrice".equalsIgnoreCase(field)) {

				try {
					String value = String.valueOf((long) cell.getNumericCellValue());
					item.setLastPrice(value);
				} catch (java.lang.IllegalStateException e) {
					logger.trace(" Exception occured in ListPrice for the Num : " + item.getNum());
				}

			} else if ("StdSellUnitFactor".equalsIgnoreCase(field)) {
				try {
					String value = String.valueOf((long) cell.getNumericCellValue());
					item.setStdSellunitFactor(value);
				} catch (java.lang.IllegalStateException e) {
					logger.trace(" Exception occured in StdSellUnitFactor for the Num : " + item.getNum());
				}

			} else if ("ScanCode".equalsIgnoreCase(field)) {
				try {
					String value = String.valueOf((long) cell.getNumericCellValue());
					item.setScanCode(value);
				} catch (java.lang.IllegalStateException e) {
					item.setScanCode("NULL");
					logger.trace(" Exception occured in ScanCode due to the illegal value (NULL) for the Num : "
							+ item.getNum());
				}

			} else if ("Description".equalsIgnoreCase(field)) {
				try {
					String value = cell.getStringCellValue();
					item.setDesc(value);
				} catch (java.lang.IllegalStateException e) {
					logger.trace(" Exception occured in Description for the Num : " + item.getNum());
				}

			}

		}
		return item;
	}

	private void setColumnMapper(Iterator<Cell> cellIterator) {
		Iterator<Cell> ce = cellIterator;
		int index = 0;
		while (ce.hasNext()) {
			Cell value = ce.next();
			if (value.getStringCellValue().equalsIgnoreCase("Num")) {
				column_mapper.put(index, value.getStringCellValue());
			} else if (value.getStringCellValue().equalsIgnoreCase("Class")) {
				column_mapper.put(index, value.getStringCellValue());
			} else if (value.getStringCellValue().equalsIgnoreCase("Cat")) {
				column_mapper.put(index, value.getStringCellValue());
			} else if (value.getStringCellValue().equalsIgnoreCase("ListPrice")) {
				column_mapper.put(index, value.getStringCellValue());
			} else if (value.getStringCellValue().equalsIgnoreCase("StdSellUnitFactor")) {
				column_mapper.put(index, value.getStringCellValue());
			} else if (value.getStringCellValue().equalsIgnoreCase("ScanCode")) {
				column_mapper.put(index, value.getStringCellValue());
			} else if (value.getStringCellValue().equalsIgnoreCase("Description")) {
				column_mapper.put(index, value.getStringCellValue());
			}

			index++;
		}
	}

	public void downloadExcel(LinkedList<Items> missedList) {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(this.sheetName);
		String downloadPath = AppProps.getInstance().getFileDownloadPath().toString().trim();

		// create headers
		Items header = new Items();
		header.setDesc("Description");
		header.setScanCode("ScanCode");
		header.setStdSellunitFactor("StdSellUnitFactor");
		header.setLastPrice("ListPrice");
		header.setCat("Cat");
		header.set_class("Class");
		header.setNum("NUM");
		missedList.addFirst(header);

		int rowCount = 0;
		for (Items item : missedList) {
			Row row = sheet.createRow(rowCount);
			writeBook(item, row);
			rowCount++;
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(downloadPath);
			workbook.write(outputStream);
		} catch (Exception e) {
		}

	}

	private void writeBook(Items item, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(item.getDesc());

		cell = row.createCell(1);
		cell.setCellValue(item.getNum());

		cell = row.createCell(2);
		cell.setCellValue(item.get_class());

		cell = row.createCell(3);
		cell.setCellValue(item.getCat());

		cell = row.createCell(4);
		cell.setCellValue(item.getLastPrice());

		cell = row.createCell(5);
		cell.setCellValue(item.getStdSellunitFactor());

		cell = row.createCell(6);
		cell.setCellValue(item.getScanCode());
	}
}
