package com.flentas.tests;

import com.flentas.pages.HomePage;

import io.qameta.allure.Description;

import com.flentas.base.BaseComp;
import com.flentas.config.ConfigReader;

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

	HomePage homePage;

	@BeforeClass
	public void setup() {
		initializeDriver();
		homePage = new HomePage(driver);
		String url = ConfigReader.getProperty("homepageUrl");
		homePage.openHomePage(url);
	}

	@Description("Print all links with their HTTP response codes")
	@Test(priority = 2)
	public void printAllLinksWithResponseCode() {
		List<WebElement> links = homePage.getAllLinks();
		List<String> brokenLinks = new ArrayList<>();

		for (WebElement link : links) {
			String url = link.getAttribute("href");
			if (url == null || url.isEmpty())
				continue;

			// Skip non-http links
			if (!url.startsWith("http")) {
				System.out.println(url + " - Skipped (Non-HTTP link)");
				continue;
			}

			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("HEAD");
				conn.setConnectTimeout(5000);
				conn.connect();
				int code = conn.getResponseCode();

				System.out.println(url + " - Response Code: " + code);

				// only consider internal broken links for assertion
				if (code >= 400 && url.contains("flentas.com")) {
					brokenLinks.add(url + " (Code: " + code + ")");
				}

			} catch (Exception e) {
				System.out.println(url + " - Exception: " + e.getMessage());
				if (url.contains("flentas.com")) {
					brokenLinks.add(url + " (Exception: " + e.getMessage() + ")");
				}
			}
		}

		// Fail test if any internal links are broken
		Assert.assertTrue(brokenLinks.isEmpty(), "Broken internal links found on homepage!");
	}

	@AfterClass
	public void tearDown() {
		quitDriver();
	}
}
