package com.flentas.listeners;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.flentas.driver.DriverManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

	private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/screenshots/";

	@Override
	public void onStart(ITestContext context) {
		cleanScreenshotDirectory();
	}

	@Override
	public void onTestFailure(ITestResult result) {
		WebDriver driver = DriverManager.getDriver();
		if (driver != null) {
			saveScreenshot(driver, result.getName(), "Failure Screenshot");
		}
		System.out.println("TestListener triggered for: " + result.getName());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		WebDriver driver = DriverManager.getDriver();
		if (driver != null) {
			saveScreenshot(driver, result.getName(), "Skipped Screenshot");
		}
	}

	private void saveScreenshot(WebDriver driver, String testName, String label) {
		try {
			File dir = new File(SCREENSHOT_DIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// save the screenshot with data,time
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File dest = new File(SCREENSHOT_DIR + testName + "_" + timestamp + ".png");
			FileUtils.copyFile(src, dest);
			System.out.println("Screenshot saved at: " + dest.getAbsolutePath());

			byte[] bytes = FileUtils.readFileToByteArray(dest);

			try {
				Allure.addAttachment(label, new ByteArrayInputStream(bytes));
			} catch (IllegalStateException e) {
				System.err.println("Allure attachment failed: No active test context");
			}

		} catch (IOException e) {
			System.err.println("Error saving screenshot: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void cleanScreenshotDirectory() {
		File dir = new File(SCREENSHOT_DIR);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
				System.out.println("Screenshot directory cleaned.");
			}
		}
	}

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {
	}

	@Override
	public void onFinish(ITestContext context) {
	}
}
