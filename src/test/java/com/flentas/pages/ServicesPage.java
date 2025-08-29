package com.flentas.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
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
		driver.findElements(org.openqa.selenium.By.xpath(serviceLinksXpath)).forEach(link -> {
			String href = link.getAttribute("href");
			if (href != null && !href.isEmpty())
				urls.add(href);
		});
		System.out.println("Total Services Found: " + urls.size());
		return urls;
	}

	// open all URLs in new tabs and get titles in the same order
	public void openAllServiceTabsAndAssertTitles(List<String> expectedTitles) throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		List<String> urls = getAllServiceUrls();
		List<String> childTabs = new ArrayList<>();

		if (urls.size() != expectedTitles.size()) {
			throw new RuntimeException("Number of URLs and expected titles mismatch!");
		}

		// open each URL in new tab
		for (String url : urls) {
			js.executeScript("window.open('" + url + "', '_blank');");
			Thread.sleep(1000);

			// Find the new tab handle
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(parentWindow) && !childTabs.contains(handle)) {
					childTabs.add(handle);
					break;
				}
			}
		}

		// Iterate child tabs in order and assert titles
		for (int i = 0; i < childTabs.size(); i++) {
			driver.switchTo().window(childTabs.get(i));
			String actualTitle = driver.getTitle();
			System.out.println("Service Tab Title: " + actualTitle);
			Assert.assertEquals(actualTitle, expectedTitles.get(i), "Title mismatch for service URL: " + urls.get(i));
		}

		// Switch back to parent
		driver.switchTo().window(parentWindow);
	}
}
