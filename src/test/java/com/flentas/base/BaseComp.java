package com.flentas.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;

import com.flentas.config.ConfigReader;
import com.flentas.driver.DriverManager;

import java.time.Duration;

@Listeners(com.flentas.listeners.TestListener.class)
public class BaseComp {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected Actions actions;
	protected JavascriptExecutor js;

	private static final Logger logger = LogManager.getLogger(BaseComp.class);

	// Initialize driver based on config
	public void initializeDriver() {
		logger.info("Loading configuration...");
		ConfigReader.loadConfig();
		String browser = ConfigReader.getProperty("browser");
		logger.info("Browser selected: {}", browser);

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
			DriverManager.setDriver(driver); // Thread safe assignment
			logger.info("ChromeDriver initialized");
		} else {
			logger.error("Unsupported browser: {}", browser);
			throw new RuntimeException("Browser not supported: " + browser);
		}

		long implicitWait = Long.parseLong(ConfigReader.getProperty("implicitWait"));
		long explicitWait = Long.parseLong(ConfigReader.getProperty("explicitWait"));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		driver.manage().window().maximize();
		logger.debug("Driver configured with implicit wait: {}s and maximized window", implicitWait);

		wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		actions = new Actions(driver);
		js = (JavascriptExecutor) driver;

		String url = ConfigReader.getProperty("url");
		driver.get(url);
		logger.info("Navigated to URL: {}", url);
	}

	// Quit driver
	public void quitDriver() {
		driver = DriverManager.getDriver(); // Get thread-local driver
		if (driver != null) {
			driver.quit();
			DriverManager.unload(); // Clean up thread-local
			logger.info("Driver quit successfully");
		} else {
			logger.warn("Driver was null during quit attempt");
		}
	}
}
