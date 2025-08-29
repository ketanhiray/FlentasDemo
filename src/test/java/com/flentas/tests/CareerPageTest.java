package com.flentas.tests;

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

	@BeforeClass
	public void setup() {
		initializeDriver();
	}

	@Description("Verify QA Lead job application form submission works")
	@Test(priority = 1)
	public void applyForQaLeadJob() {
		HomePage home = new HomePage(driver);
		CareerPage career = home.clickCareers();
		career.clickJoinUs();
		career.clickBrowseJobs();
		career.searchJob("QA Lead");

		JobDetailsPage jobPage = new JobDetailsPage(driver);
		jobPage.openQaLeadJob();
		jobPage.clickApply();

		ApplicationFormPage formPage = new ApplicationFormPage(driver);

		String[] applicant = CSVDataProvider.getRandomApplicant(ConfigReader.getProperty("csvPath"));
		formPage.fillForm(applicant[0], applicant[1], applicant[2], applicant[3],
				ConfigReader.getProperty("resumePath"));
		formPage.submitForm();

		Assert.assertEquals(formPage.getConfirmationMessage(), "Application Submitted!");
	}

	@AfterClass
	public void tearDown() {
		quitDriver();
	}
}
