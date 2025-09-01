package com.flentas.listeners;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class TestListener implements ITestListener {

	private static final String SCREENSHOT_DIR = "screenshots/";

	@Override
	public void onTestFailure(ITestResult result) {
		WebDriver driver = getDriverFromTest(result.getInstance());
		if (driver != null) {
			takeScreenshot(driver, result.getName(), "Failure Screenshot");
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		WebDriver driver = getDriverFromTest(result.getInstance());
		if (driver != null) {
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			Allure.addAttachment("Skipped Screenshot", new ByteArrayInputStream(screenshot));
		}
	}

	private void takeScreenshot(WebDriver driver, String testName, String label) {
		try {
			new File(SCREENSHOT_DIR).mkdirs();
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String path = SCREENSHOT_DIR + testName + ".png";
			FileUtils.copyFile(src, new File(path));
			System.out.println("Screenshot saved: " + path);

			byte[] bytes = FileUtils.readFileToByteArray(new File(path));
			Allure.addAttachment(label, new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private WebDriver getDriverFromTest(Object testInstance) {
		try {
			Field field = testInstance.getClass().getDeclaredField("driver");
			field.setAccessible(true);
			return (WebDriver) field.get(testInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
	}
}
