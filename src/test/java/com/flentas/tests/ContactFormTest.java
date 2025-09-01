package com.flentas.tests;

import com.flentas.base.BaseComp;
import com.flentas.utils.ContactFormDataProvider;

import io.qameta.allure.Description;

import com.flentas.pages.ContactPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ContactFormTest extends BaseComp {

	private static final Logger logger = LogManager.getLogger(ContactFormTest.class);
	private ContactPage contactPage;

	@BeforeClass
	public void setup() {
		logger.info("Initializing WebDriver for ContactFormTest...");
		initializeDriver();
		contactPage = new ContactPage(driver);
		logger.info("Navigating to Contact page...");
		contactPage.goToContactPage();
	}

	@Description("This test verifies the Contact us form validation - Positive Test")
	@Test(priority = 5, dataProvider = "positiveContactData", dataProviderClass = ContactFormDataProvider.class)
	public void testPositiveFormSubmission(String name, String email, String company, String phone, String message) {
		logger.info("Running positive form submission test for: {}", name);

		try {
			wait.until(ExpectedConditions.visibilityOf(contactPage.getNameField()));
			logger.debug("Name field is visible");

			contactPage.fillForm(name, email, company, phone, message);
			logger.info("Filled form with valid data: {}, {}, {}, {}, {}", name, email, company, phone, message);

			wait.until(ExpectedConditions.elementToBeClickable(contactPage.getSubmitButton()));
			logger.debug("Submit button is clickable");

			contactPage.submitForm();
			logger.info("Form submitted");

			wait.until(ExpectedConditions.visibilityOf(contactPage.getSuccessMessage()));
			logger.debug("Success message is visible");

			assert contactPage.isSuccessMessageDisplayed()
					: "Success message was not displayed for positive test case.";
			logger.info("Success message displayed correctly for: {}", name);

		} catch (AssertionError ae) {
			logger.error("Assertion failed for positive test case: {}", ae.getMessage(), ae);
			throw ae;
		} catch (Exception e) {
			logger.error("Unexpected exception during positive form submission: {}", e.getMessage(), e);
			throw e;
		} finally {
			driver.navigate().refresh();
			logger.debug("Page refreshed for next iteration");
		}
	}

	@Description("This test verifies the Contact us form validation - Negative Test")
	@Test(priority = 6, dataProvider = "negativeContactData", dataProviderClass = ContactFormDataProvider.class)
	public void testNegativeFormValidation(String name, String email, String company, String phone, String message) {
		logger.info("Running negative form validation test for: {}, {}", name, email);

		contactPage.fillForm(name, email, company, phone, message);
		logger.info("Filled form with invalid data");

		try {
			wait.withTimeout(Duration.ofSeconds(5))
					.until(ExpectedConditions.elementToBeClickable(contactPage.getSubmitButton()));

			logger.error("BUG FOUND: Submit button became enabled for invalid data: {}, {}", name, email);
			assert false
					: "BUG FOUND: Submit button became enabled for invalid data. Test data: " + name + ", " + email;

		} catch (TimeoutException e) {
			if (!contactPage.isSubmitEnabled()) {
				logger.info("Validation triggered correctly. Submit button remained disabled for: {}, {}", name, email);
			} else {
				logger.error("Submit button was enabled despite invalid data: {}, {}", name, email);
				assert false
						: "Submit button should be disabled, but it was enabled. Test data: " + name + ", " + email;
			}
		} catch (Exception e) {
			logger.error("Unexpected exception during negative form validation: {}", e.getMessage(), e);
			throw e;
		} finally {
			driver.navigate().refresh();
			logger.debug("Page refreshed for next iteration");
		}
	}

	@AfterClass
	public void tearDown() {
		logger.info("Quitting WebDriver for ContactFormTest...");
		quitDriver();
	}
}
