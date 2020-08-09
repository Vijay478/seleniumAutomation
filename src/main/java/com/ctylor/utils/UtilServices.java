package com.ctylor.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ctylor.config.AppProps;
import com.ctylor.domain.Items;

public class UtilServices {

	private static UtilServices single_instance = getInstance();
	final static Logger logger = LogManager.getLogger(UtilServices.class);
	private static final String Dir_Path = AppProps.getInstance().getImageDownloadPath();

	public static UtilServices getInstance() {
		if (single_instance == null)
			single_instance = new UtilServices();
		return single_instance;
	}

	public void downloadImage(String scanCode, String imgUrl) {

		String imagePath = setImageName(scanCode, imgUrl);

		try {
			BufferedInputStream inputStream = new BufferedInputStream(new URL(imgUrl).openStream());
			FileOutputStream fileOS = new FileOutputStream(imagePath);
			byte data[] = new byte[1024];
			int byteContent;
			while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
				fileOS.write(data, 0, byteContent);

			}
			logger.trace("Image download path " + imagePath);
		} catch (IOException e) {
			logger.error("error occured while downloading the image url : " + imgUrl);
		}

	}

	private String setImageName(String scanCode, String imgUrl) {
		String fileName = Dir_Path + scanCode + ".jpg";
		String[] arr = imgUrl.split("/");
		if (arr.length > 0) {
			String[] arr2 = imgUrl.split(".");
			if (arr2.length > 1) {
				fileName = Dir_Path + scanCode + "." + arr2[1];
			}

		}

		return fileName;
	}

	public void texaswholesales(LinkedList<Items> items, String mainUrl) throws InterruptedException {


		// initialize web driver
				System.setProperty("webdriver.chrome.driver", AppProps.getInstance().getChromeDriverPath());
				WebDriver driver = new ChromeDriver();
//		    in order to handle authentication pops we need to follow the syntax -- http://username:password@ SiteURL
				driver.get(mainUrl);
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
							downloadImage(scanCode, imgUrl);

						}

					}
					if (!isImage) {
						logger.info("image not found for ScanCode : " + scanCode);
					}

				}

				driver.close();
	}

	public void shopravis(LinkedList<Items> items, String mainUrl) {

		// initialize web driver
				System.setProperty("webdriver.chrome.driver", AppProps.getInstance().getChromeDriverPath());
				WebDriver driver = new ChromeDriver();
//		    in order to handle authentication pops we need to follow the syntax -- http://username:password@ SiteURL
				driver.get(mainUrl);
				driver.manage().window().maximize();		
	}

	public void barcodelookup(LinkedList<Items> items, String mainUrl) {

		// initialize web driver
				System.setProperty("webdriver.chrome.driver", AppProps.getInstance().getChromeDriverPath());
				WebDriver driver = new ChromeDriver();
//		    in order to handle authentication pops we need to follow the syntax -- http://username:password@ SiteURL
				driver.get(mainUrl);
				driver.manage().window().maximize();		
	}

	public void googleImages(LinkedList<Items> items, String mainUrl) {

		// initialize web driver
				System.setProperty("webdriver.chrome.driver", AppProps.getInstance().getChromeDriverPath());
				WebDriver driver = new ChromeDriver();
//		    in order to handle authentication pops we need to follow the syntax -- http://username:password@ SiteURL
				driver.get(mainUrl);
				driver.manage().window().maximize();		
	}

}
