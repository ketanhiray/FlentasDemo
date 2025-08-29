package com.flentas.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.flentas.config.ConfigReader;

import java.time.Duration;

public class BaseComp {
	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected static Actions actions;
	protected static JavascriptExecutor js;

	// Initialize driver based on config
	public void initializeDriver() {
		ConfigReader.loadConfig();
		String browser = ConfigReader.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else {
			throw new RuntimeException("Browser not supported: " + browser);
		}

		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicitWait"))));
		driver.manage().window().maximize();

		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("explicitWait"))));

		actions = new Actions(driver);
		js = (JavascriptExecutor) driver;

		driver.get(ConfigReader.getProperty("url"));
	}

	//utility methods
	protected void clickElement(WebElement element) {
		wait.until(driver -> element.isDisplayed() && element.isEnabled());
		element.click();
	}

	protected void hoverElement(WebElement element) {
		actions.moveToElement(element).perform();
	}

	protected void scrollIntoView(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	protected void clickJS(WebElement element) {
		js.executeScript("arguments[0].click();", element);
	}

	protected void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}

	// Quit driver
	public void quitDriver() {
		if (driver != null) {
			driver.quit();
		}
	}
}
