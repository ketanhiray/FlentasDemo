package com.flentas.tests;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.flentas.base.BaseComp;
import com.flentas.pages.ServicesPage;

import io.qameta.allure.Description;

@Listeners(com.flentas.listeners.TestListener.class)
public class CheckServicesTest extends BaseComp {

	private static final Logger logger = LogManager.getLogger(CheckServicesTest.class);

	@Description("This test verifies all services")
	@Test(priority = 4)
	public void validateAllServicesTitles() throws InterruptedException {
		logger.info("Starting test: validateAllServicesTitles");

		try {
			initializeDriver();
			logger.info("WebDriver initialized");

			ServicesPage servicesPage = new ServicesPage(driver);
			logger.debug("Navigated to ServicesPage");

			// expected titles
			List<String> expectedTitles = Arrays.asList("Managed cloud service provider and Infrastructure | Flentas",
					"Top AWS DevOps Services | Expert DevOps Consulting in Pune and UAE",
					"Tech Resource Augmentation - IT Staff augmentation | Flentas");

			logger.info("Expected service titles: {}", expectedTitles);

			// actual titles from browser
			List<String> actualTitles = servicesPage.openAllServiceTabsAndGetTitles();
			logger.info("Actual service titles: {}", actualTitles);

			// assertion in test
			Assert.assertEquals(actualTitles, expectedTitles, "Service titles mismatch!");

			logger.info("All service titles validated successfully");

		} catch (AssertionError ae) {
			logger.error("Assertion failed: {}", ae.getMessage(), ae);
			throw ae;
		} catch (Exception e) {
			logger.error("Unexpected exception during service title validation: {}", e.getMessage(), e);
			throw e;
		}
	}

	@AfterClass
	public void tearDown() {
		logger.info("Quitting WebDriver for CheckServicesTest...");
		quitDriver();
	}
}
