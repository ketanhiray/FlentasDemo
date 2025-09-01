package com.flentas.tests;

import com.flentas.base.BaseComp;
import com.flentas.pages.BlogsPage;
import com.flentas.pages.HomePage;

import io.qameta.allure.Description;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FindBlogTest extends BaseComp {

	private static final Logger logger = LogManager.getLogger(FindBlogTest.class);
	HomePage homePage;
	BlogsPage blogsPage;

	@BeforeClass
	public void setup() {
		logger.info("Initializing WebDriver for FindBlogTest...");
		initializeDriver();
		homePage = new HomePage(driver);
		blogsPage = new BlogsPage(driver);
		logger.info("HomePage and BlogsPage initialized");
	}

	@Description("This test verifies Blog")
	@Test(priority = 3)
	public void findAndOpenBlog() {
		logger.info("Starting test: findAndOpenBlog");

		try {
			homePage.navigateToBlogs();
			logger.info("Navigated to Blogs section");

			blogsPage.openBlog();
			logger.info("Clicked on blog link");

			blogsPage.switchToChildWindow();
			logger.info("Switched to child window");

			String blogTitle = driver.getTitle();
			logger.info("Fetched blog title: {}", blogTitle);

			Assert.assertTrue(blogTitle.contains("Indian Cloud Gaming Industry | Mobile Multiplayer Game Dev"),
					"Blog title verification failed");
			logger.info("Blog title verification passed");

		} catch (AssertionError ae) {
			logger.error("Assertion failed: {}", ae.getMessage(), ae);
			throw ae;
		} catch (Exception e) {
			logger.error("Unexpected exception during blog test: {}", e.getMessage(), e);
			throw e;
		}
	}

	@AfterClass
	public void tearDown() {
		logger.info("Quitting WebDriver for FindBlogTest...");
		quitDriver();
	}
}
