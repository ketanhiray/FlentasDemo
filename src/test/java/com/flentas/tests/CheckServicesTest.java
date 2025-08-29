package com.flentas.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.flentas.base.BaseComp;
import com.flentas.pages.ServicesPage;

import io.qameta.allure.Description;

public class CheckServicesTest extends BaseComp {

	@Description("This test verifies all services")
	@Test(priority = 4)
	public void validateAllServicesTitles() throws InterruptedException {
		initializeDriver();

		ServicesPage servicesPage = new ServicesPage(driver);

		// Correct expected titles
		List<String> expectedTitles = Arrays.asList("Managed cloud service provider and Infrastructure | Flentas",
				"Top AWS DevOps Services | Expert DevOps Consulting in Pune and UAE",
				"Tech Resource Augmentation - IT Staff augmentation | Flentas");

		servicesPage.openAllServiceTabsAndAssertTitles(expectedTitles);
	}

	@AfterClass
	public void tearDown() {
		quitDriver();
	}

}
