package com.flentas.tests;

import com.flentas.base.BaseComp;
import com.flentas.utils.ContactFormDataProvider;

import io.qameta.allure.Description;

import com.flentas.pages.ContactPage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ContactFormTest extends BaseComp {

	private ContactPage contactPage;

	@BeforeClass
	public void setup() {
		initializeDriver();
		contactPage = new ContactPage(driver);
		contactPage.goToContactPage();
	}

	@Description("This test verifies the Contact us form validation")
	@Test(priority = 5, dataProvider = "positiveContactData", dataProviderClass = ContactFormDataProvider.class)
	public void testPositiveFormSubmission(String name, String email, String company, String phone, String message) {
		// wait for form field to be visible
		wait.until(ExpectedConditions.visibilityOf(contactPage.getNameField()));

		contactPage.fillForm(name, email, company, phone, message);

		// wait for submit button clickable
		wait.until(ExpectedConditions.elementToBeClickable(contactPage.getSubmitButton()));

		contactPage.submitForm();

		// wait for success message
		wait.until(ExpectedConditions.visibilityOf(contactPage.getSuccessMessage()));

		assert contactPage.isSuccessMessageDisplayed() : "Success message was not displayed for positive test case.";
		System.out.println("Success for: " + name);

		driver.navigate().refresh();
	}

	@Test(dataProvider = "negativeContactData", dataProviderClass = ContactFormDataProvider.class)
	public void testNegativeFormValidation(String name, String email, String company, String phone, String message) {
		contactPage.fillForm(name, email, company, phone, message);

		try {
			wait.withTimeout(Duration.ofSeconds(5))
					.until(ExpectedConditions.elementToBeClickable(contactPage.getSubmitButton()));
			assert false
					: "BUG FOUND: Submit button became enabled for invalid data. Test data: " + name + ", " + email;
		} catch (TimeoutException e) {
			assert !contactPage.isSubmitEnabled()
					: "Submit button should be disabled, but it was enabled. Test data: " + name + ", " + email;
			System.out.println("Validation triggered correctly. Button remained disabled for: " + name + ", " + email);
		}

		driver.navigate().refresh();
	}

	@AfterClass
	public void tearDown() {
		quitDriver();
		// driver.quit();;
	}
}
