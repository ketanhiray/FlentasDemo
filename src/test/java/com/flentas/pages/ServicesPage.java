package com.flentas.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class ServicesPage {
	WebDriver driver;
	JavascriptExecutor js;

	private String serviceLinksXpath = "//div[@class='serNav2']/ul/li/a";

	public ServicesPage(WebDriver driver) {
		this.driver = driver;
		this.js = (JavascriptExecutor) driver;
	}

	// Get all service URLs
	public List<String> getAllServiceUrls() {
		List<String> urls = new ArrayList<>();
		List<WebElement> links = driver.findElements(By.xpath(serviceLinksXpath));

		for (WebElement link : links) {
			String href = link.getAttribute("href");
			if (href != null && !href.isEmpty()) {
				urls.add(href);
			}
		}
		return urls;
	}

	// Open all urls in new tabs and return titles in the same order
	public List<String> openAllServiceTabsAndGetTitles() throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		List<String> urls = getAllServiceUrls();
		List<String> childTabs = new ArrayList<>();
		List<String> actualTitles = new ArrayList<>();

		// open each url in new tab
		for (String url : urls) {
			js.executeScript("window.open('" + url + "', '_blank');");
			Thread.sleep(1000);

			// find the new tab handle
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(parentWindow) && !childTabs.contains(handle)) {
					childTabs.add(handle);
					break;
				}
			}
		}

		// collect titles from child tabs
		for (String tab : childTabs) {
			driver.switchTo().window(tab);
			String actualTitle = driver.getTitle();
			System.out.println("Service Tab Title: " + actualTitle);
			actualTitles.add(actualTitle);
		}

		// switch back to parent window
		driver.switchTo().window(parentWindow);

		return actualTitles;
	}
}
