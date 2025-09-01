package com.flentas.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.flentas.config.ConfigReader;
import com.flentas.base.BaseComp;
import com.flentas.pages.ApplicationFormPage;
import com.flentas.pages.CareerPage;
import com.flentas.pages.HomePage;
import com.flentas.pages.JobDetailsPage;

import com.flentas.utils.*;

import io.qameta.allure.Description;

public class CareerPageTest extends BaseComp {

	private static final Logger logger = LogManager.getLogger(CareerPageTest.class);

	@BeforeClass
	public void setup() {
		logger.info("Initializing WebDriver for CareerPageTest...");
		initializeDriver();
	}

	@Description("Verify QA Lead job application form submission works")
	@Test(priority = 1)
	public void applyForQaLeadJob() {
		logger.info("Starting test: applyForQaLeadJob");

		try {
			HomePage home = new HomePage(driver);
			logger.debug("Navigated to HomePage");

			CareerPage career = home.clickCareers();
			logger.debug("Clicked on Careers");

			career.clickJoinUs();
			logger.debug("Clicked on Join Us");

			career.clickBrowseJobs();
			logger.debug("Clicked on Browse Jobs");

			career.searchJob("QA Lead");
			logger.info("Searched for job: QA Lead");

			JobDetailsPage jobPage = new JobDetailsPage(driver);
			jobPage.openQaLeadJob();
			logger.info("Opened QA Lead job details");

			jobPage.clickApply();
			logger.info("Clicked Apply on QA Lead job");

			ApplicationFormPage formPage = new ApplicationFormPage(driver);

			String[] applicant = CSVDataProvider.getRandomApplicant(ConfigReader.getProperty("csvPath"));
			logger.debug("Fetched applicant data from CSV");

			formPage.fillForm(applicant[0], applicant[1], applicant[2], applicant[3],
					ConfigReader.getProperty("resumePath"));
			logger.info("Filled application form");

			formPage.submitForm();
			logger.info("Submitted application form");

			String confirmation = formPage.getConfirmationMessage();
			logger.info("Received confirmation message: {}", confirmation);

			Assert.assertEquals(confirmation, "Application Submitted!");
			logger.info("Assertion passed: Application Submitted!");

		} catch (AssertionError ae) {
			logger.error("Assertion failed: {}", ae.getMessage(), ae);
			throw ae;
		} catch (Exception e) {
			logger.error("Test failed due to unexpected exception: {}", e.getMessage(), e);
			throw e;
		}
	}

	@AfterClass
	public void tearDown() {
		logger.info("Quitting WebDriver for CareerPageTest...");
		quitDriver();
	}
}
