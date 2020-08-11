package com.ctylor;

import java.awt.Robot;
//import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

//import javax.imageio.ImageIO;
//import javax.imageio.stream.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctylor.config.AppProps;
import com.ctylor.domain.Items;
import com.ctylor.domain.ResultObj;
import com.ctylor.utils.ExcelParser;
import com.ctylor.utils.UtilServices;

public class Main {
	static Robot robot = null;
	final static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) throws InterruptedException {
		String[] urls = { "http://texas-wholesale.com/", "http://www.shopravis.com/", "https://www.barcodelookup.com/",
		"images.google.com" };
		
		String action = AppProps.getInstance().getExcelFileName();
		
//		int Option = 1;
//	
//

//
//		Scanner scanner = new Scanner(System.in); // Create a Scanner object
//		System.out.println("please choose url from below list ... by entering the s.no ");
//		System.out.println("S.NO       Url");
//		System.out.println("=====================================");
//		for (int i = 0; i < urls.length; i++) {
//			String string = urls[i];
//			System.out.println((i + 1) + "       " + string);
//
//		}
//		System.out.println("please enter number in range [1-4]  :");
//
//		String opt = scanner.nextLine(); // Read user input
//
//		try {
//
//			Option = Integer.parseInt(opt);
//			mainUrl = urls[Option - 1];
//			System.out.println(mainUrl);
//		} catch (Exception e) {
//			logger.error("please enter valid option ");
//			return;
//		}

		UtilServices utilServices = UtilServices.getInstance();
		ExcelParser excelServices = ExcelParser.getInstance();
		
		if(action == "download") {
			
			// loaddata
			LinkedList<Items> items = loadData();
			LinkedList<Items> missedList = items;
			
			for (int i = 0; i < urls.length; i++) {
				String mainUrl = urls[i];
				if ("http://texas-wholesale.com/".equalsIgnoreCase(mainUrl)) {
					//missedList = utilServices.texaswholesales(missedList, mainUrl);
				} else if ("http://www.shopravis.com/".equalsIgnoreCase(mainUrl)) {
					missedList = utilServices.shopravis(missedList, mainUrl);
				} else if ("https://www.barcodelookup.com/".equalsIgnoreCase(mainUrl)) {
					//missedList = utilServices.barcodelookup(missedList, mainUrl);
				} else if ("images.google.com".equalsIgnoreCase(mainUrl)) {
				//	missedList = utilServices.googleImages(items, mainUrl);
				} else {
					logger.info("plaese enter valid site Url");
					for (int j = 0; j < urls.length; j++) {
						logger.info((j+1) +" . " + urls[j]);
					}
				}
			}
			
			excelServices.downloadExcel(missedList);
			
		}
		else {
			
			utilServices.processImages();
		}

		

	}

	public static LinkedList<Items> loadData() {

		LinkedList<Items> items = new LinkedList<Items>();
		try {
			// get data
			ExcelParser parser = ExcelParser.getInstance();
			parser.setFilePath(AppProps.getInstance().getExcelFileName());
			parser.setSheetName(AppProps.getInstance().getExcelSheetName());
			ResultObj resObj = parser.parse();
			items = resObj.getItems();

			// total no of records from excel
			logger.info("Total no of  Records in excel  : "
					+ (resObj.getItems().size() + resObj.getSkippedElements().size()));

			// parsed records from excel
			logger.info("total parsed records: " + resObj.getItems().size());
			// NULL records from excel
			logger.info("Total skipped Records : " + resObj.getSkippedElements().size());

		} catch (IOException e) {
			logger.error("Exception occured while loading data");
		}
		return items;
	}

}