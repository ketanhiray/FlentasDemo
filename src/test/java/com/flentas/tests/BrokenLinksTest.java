package com.flentas.tests;

import com.flentas.pages.HomePage;

import io.qameta.allure.Description;

import com.flentas.base.BaseComp;
import com.flentas.config.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrokenLinksTest extends BaseComp {

	private static final Logger logger = LogManager.getLogger(BrokenLinksTest.class);
	HomePage homePage;

	@BeforeClass
	public void setup() {
		logger.info("Initializing WebDriver for BrokenLinksTest...");
		initializeDriver();
		homePage = new HomePage(driver);
		String url = ConfigReader.getProperty("homepageUrl");
		logger.info("Navigating to homepage: {}", url);
		homePage.openHomePage(url);
	}

	@Description("Print all links with their HTTP response codes")
	@Test(priority = 2)
	public void printAllLinksWithResponseCode() {
		logger.info("Starting test: printAllLinksWithResponseCode");

		List<WebElement> links = homePage.getAllLinks();
		logger.info("Total links found: {}", links.size());

		List<String> brokenLinks = new ArrayList<>();

		for (WebElement link : links) {
			String url = link.getAttribute("href");

			if (url == null || url.isEmpty()) {
				logger.debug("Skipped empty or null href");
				continue;
			}

			if (!url.startsWith("http")) {
				logger.debug("{} - Skipped (Non-HTTP link)", url);
				continue;
			}

			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("HEAD");
				conn.setConnectTimeout(5000);
				conn.connect();
				int code = conn.getResponseCode();

				logger.info("{} - Response Code: {}", url, code);

				if (code >= 400 && url.contains("flentas.com")) {
					brokenLinks.add(url + " (Code: " + code + ")");
					logger.warn("Broken internal link detected: {} (Code: {})", url, code);
				}

			} catch (Exception e) {
				logger.error("{} - Exception occurred: {}", url, e.getMessage(), e);
				if (url.contains("flentas.com")) {
					brokenLinks.add(url + " (Exception: " + e.getMessage() + ")");
				}
			}
		}

		if (!brokenLinks.isEmpty()) {
			logger.error("Broken internal links found: {}", brokenLinks);
		}

		Assert.assertTrue(brokenLinks.isEmpty(), "Broken internal links found on homepage!");
		logger.info("Assertion passed: No broken internal links found");
	}

	@AfterClass
	public void tearDown() {
		logger.info("Quitting WebDriver for BrokenLinksTest...");
		quitDriver();
	}
}
