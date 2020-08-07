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

		UtilServices utilServices = UtilServices.getInstance();

// loaddata
		LinkedList<Items> items = loadData();

// initialize web driver
		System.setProperty("webdriver.chrome.driver", AppProps.getInstance().getChromeDriverPath());
		WebDriver driver = new ChromeDriver();
//    in order to handle authentication pops we need to follow the syntax -- http://username:password@ SiteURL
		driver.get(AppProps.getInstance().getMainUrl());
		driver.manage().window().maximize();
		driver.findElement(By.xpath(
				"//*[@class='swal-modal']//div[@class='swal-footer']/div/button[@class='swal-button swal-button--accept']"))
				.click();
		Thread.sleep(3000);

		System.out.println("no of records" + items.size());

		boolean isImage;

		for (Items item : items) {
			isImage = false;
			String scanCode = item.getScanCode();
			driver.findElement(By.name("searchfield")).sendKeys(scanCode);
			driver.findElement(By.name("searchbutton")).click();
			Thread.sleep(5000);
			List<WebElement> allImages = driver.findElements(By.tagName("img"));
			for (WebElement ele : allImages) {
				String imgUrl = ele.getAttribute("src");
				if (imgUrl.contains(AppProps.getInstance().getSearchKey())) {
					logger.trace("ScanCode : " + scanCode + " imgUrl : " + imgUrl);
					Thread.sleep(3000);
					isImage = true;

					// download image
					utilServices.downloadImage(scanCode, imgUrl);

				}

			}
			if (!isImage) {
				logger.info("image not found for ScanCode : " + scanCode);
			}

		}

		driver.close();
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