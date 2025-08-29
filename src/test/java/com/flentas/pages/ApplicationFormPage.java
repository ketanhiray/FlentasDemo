package com.flentas.pages;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.flentas.utils.TestUtil;

public class ApplicationFormPage {

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id = "firstName")
	WebElement firstNameField;

	@FindBy(id = "lastName")
	WebElement lastNameField;

	@FindBy(id = "gender")
	WebElement genderDropdown;

	@FindBy(name = "email")
	WebElement emailField;
	// input[id='mobilePhone.number']

	@FindBy(id = "mobilePhone.number")
	WebElement mobileField;

	@FindBy(xpath = "//input[@class='file-upload']")
	WebElement fileInput;

	@FindBy(id = "dateOfBirth")
	WebElement dobField;

	@FindBy(id = "workExperience")
	WebElement expField;

	@FindBy(id = "currentSalary")
	WebElement currSalary;

	@FindBy(id = "expectedSalary")
	WebElement expSalary;

	@FindBy(id = "availability")
	WebElement availability;

	@FindBy(id = "locationPreference")
	WebElement locationDropdown;

	@FindBy(id = "currentLocation")
	WebElement currentLocation;

	@FindBy(id = "skills")
	WebElement skillsField;

	// captcha
	@FindBy(id = "captcha")
	WebElement captcha;

	@FindBy(id = "candidateConsent")
	WebElement consentCheckbox;

	@FindBy(id = "jobApplicationSubmitButton")
	WebElement submitBtn;

	@FindBy(xpath = "//h1[normalize-space()='Application Submitted!']")
	WebElement confirmationMsg;

	public ApplicationFormPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}

	public void fillForm(String firstName, String lastName, String email, String mobile, String resumePath) {
		// First Name
		TestUtil.typeText(driver, firstNameField, firstName);

		// Last Name
		TestUtil.typeText(driver, lastNameField, lastName);

		// Gender
		new Select(genderDropdown).selectByVisibleText("Male");

		// Email
		TestUtil.typeText(driver, emailField, email);

		// Mobile Phone
		// selectCountryAndEnterMobile("India", mobile);
		// Click the dropdown to open it
		WebElement dropdown = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//span[@id='select2-mobilePhonecountryCode-container']")));
		dropdown.click();
		//// input[@role='searchbox']

		// search india
		WebElement searchIndiaOption = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@role='searchbox']")));

		searchIndiaOption.sendKeys("India");

		// Wait for the option list to be visible
		WebElement indiaOption = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='India (+91)']")));

		// Enter mobile no.
		indiaOption.click();
		// fillField(driver, wait, By.cssSelector("input[id='mobilePhone.number']"),
		// mobile);
		WebElement mobileInput = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id='mobilePhone.number']")));
		mobileInput.sendKeys(mobile);

		// Upload Resume
		String absoluteResumePath = new File(resumePath).getAbsolutePath();
		fileInput.sendKeys(absoluteResumePath);

		// Date of Birth
		((JavascriptExecutor) driver).executeScript("arguments[0].value='28-08-1995';", dobField);

		// Experience
		TestUtil.typeText(driver, expField, "5");

		// Current Salary
		TestUtil.typeText(driver, currSalary, "1200000");

		// Expected Salary
		TestUtil.typeText(driver, expSalary, "1800000");

		// Available to Join
		TestUtil.typeText(driver, availability, "0");

		// Preferred Location
		new Select(locationDropdown).selectByVisibleText("Pune");

		// Current Location
		TestUtil.typeText(driver, currentLocation, "Pune");

		// Skills
		String[] skills = { "Java", "Selenium", "TestNG", "Jenkins" };
		for (String skill : skills) {
			skillsField.sendKeys(skill);
			skillsField.sendKeys(Keys.ENTER);
		}
	}

	// Submit Form
	public void submitForm() {

		System.out.println("Pausing for manual CAPTCHA entry...");
		try {
			// driver.findElement(By.id("captcha")).click();
			captcha.click();
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TestUtil.safeClick(driver, consentCheckbox);
		TestUtil.safeClick(driver, submitBtn);
	}

	// Get confirmation
	public String getConfirmationMessage() {
		return confirmationMsg.getText().trim();
	}
}
