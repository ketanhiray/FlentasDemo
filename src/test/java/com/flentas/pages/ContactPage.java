package com.flentas.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.flentas.base.BaseComp;
import com.flentas.utils.TestUtil;

public class ContactPage {

	private WebDriver driver;

	// Constructor
	public ContactPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[@role='button'][normalize-space()='Contact']")
	private WebElement contactButton;

	@FindBy(id = "name_contact")
	private WebElement nameField;

	@FindBy(id = "email_contact")
	private WebElement emailField;

	@FindBy(id = "company_contact")
	private WebElement companyField;

	@FindBy(id = "mobile_contact")
	private WebElement phoneField;

	@FindBy(id = "message_contact")
	private WebElement messageField;

	@FindBy(id = "submitForm2")
	private WebElement submitButton;

	@FindBy(xpath = "//h1[contains(text(),'Thank You!')]")
	private WebElement successMsg;

	public void goToContactPage() {
		TestUtil.clickElement(driver, contactButton);
	}

	public void fillForm(String name, String email, String company, String phone, String message) {
		nameField.clear();
		nameField.sendKeys(name);

		emailField.clear();
		emailField.sendKeys(email);

		companyField.clear();
		companyField.sendKeys(company);

		phoneField.clear();
		phoneField.sendKeys(phone);

		messageField.clear();
		messageField.sendKeys(message);
	}

	public void submitForm() {
		TestUtil.clickElement(driver, submitButton);
	}

	public boolean isSuccessMessageDisplayed() {
		return successMsg.isDisplayed();
	}

	public boolean isSubmitEnabled() {
		return submitButton.isEnabled();
	}

	public WebElement getNameField() {
		return nameField;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}

	public WebElement getSuccessMessage() {
		return successMsg;
	}
}