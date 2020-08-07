package com.ctylor.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctylor.config.AppProps;

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

}
