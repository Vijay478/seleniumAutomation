package com.ctylor;

import java.awt.Robot;
//import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//import javax.imageio.ImageIO;
//import javax.imageio.stream.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ctylor.config.AppProps;
import com.ctylor.domain.Items;
import com.ctylor.domain.ResultObj;
import com.ctylor.utils.ExcelParser;
import com.ctylor.utils.UtilServices;

public class Main {
	static Robot robot = null;
	final static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) throws InterruptedException {

		String[] urls = { "http://texas-wholesale.com/", };

		UtilServices utilServices = UtilServices.getInstance();

// loaddata
		LinkedList<Items> items = loadData();

//		http://texas-wholesale.com/
		String mainUrl = AppProps.getInstance().getMainUrl().toString().trim();

		if ("http://texas-wholesale.com/".equalsIgnoreCase(mainUrl)) {
			utilServices.texaswholesales(items);
		} else if ("http://www.shopravis.com/".equalsIgnoreCase(mainUrl)) {
			utilServices.shopravis(items);
		} else if ("https://www.barcodelookup.com/".equalsIgnoreCase(mainUrl)) {
			utilServices.barcodelookup(items);
		} else if ("images.google.com".equalsIgnoreCase(mainUrl)) {
			utilServices.googleImages(items);
		} else {
			logger.info("plaese enter valid site Url");
			for (int i = 0; i < urls.length; i++) {
				logger.info(1 + " . " + urls[i]);
			}
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