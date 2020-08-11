package com.ctylor.config;

public class AppProps {

	String excelFileName;
	String excelSheetName;
	String chromeDriverPath;
	String imageDownloadPath;
	String mainUrl;
	String searchKey;
	String fileDownloadPath;
	String action;

	private AppProps() {
		// Private constructor to restrict new instances
		this.excelFileName = PropertiesCache.getInstance().getProperty("excel_fileName");
		this.excelSheetName = PropertiesCache.getInstance().getProperty("excel_sheet_name");
		this.chromeDriverPath = PropertiesCache.getInstance().getProperty("chrome_driver_path");
		this.imageDownloadPath = PropertiesCache.getInstance().getProperty("image_download_path");
		this.mainUrl = PropertiesCache.getInstance().getProperty("main_url");
		this.searchKey = PropertiesCache.getInstance().getProperty("search_key");
		this.fileDownloadPath = PropertiesCache.getInstance().getProperty("file_download_path");
		this.action = PropertiesCache.getInstance().getProperty("action");
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public String getExcelSheetName() {
		return excelSheetName;
	}

	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}

	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public String getImageDownloadPath() {
		return imageDownloadPath;
	}

	public void setImageDownloadPath(String imageDownloadPath) {
		this.imageDownloadPath = imageDownloadPath;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	// Bill Pugh Solution for singleton pattern
	private static class LazyHolder {
		private static final AppProps INSTANCE = new AppProps();
	}

	public static AppProps getInstance() {
		return LazyHolder.INSTANCE;
	}

}
