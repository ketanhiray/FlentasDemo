package com.flentas.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.flentas.utils.TestUtil;

public class CareerPage {
	WebDriver driver;

	@FindBy(xpath = "//a[text()='Join Us']")
	WebElement joinUsBtn;

	@FindBy(css = ".d-flex.align-self-center")
	WebElement browseJobs;

	@FindBy(css = "#searchText")
	WebElement searchBox;

	public CareerPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void clickJoinUs() {
		TestUtil.safeClick(driver, joinUsBtn);
	}

	public void clickBrowseJobs() {
		TestUtil.safeClick(driver, browseJobs);
	}

	public void searchJob(String jobTitle) {
		TestUtil.typeText(driver, searchBox, jobTitle);
	}
}
