package com.flentas.tests;

import com.flentas.base.BaseComp;
import com.flentas.pages.BlogsPage;
import com.flentas.pages.HomePage;

import io.qameta.allure.Description;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FindBlogTest extends BaseComp {
	HomePage homePage;
	BlogsPage blogsPage;

	@BeforeClass
	public void setup() {
		initializeDriver();
		homePage = new HomePage(driver);
		blogsPage = new BlogsPage(driver);
	}

	@Description("This test verifies Blog")
	@Test(priority = 3)
	public void findAndOpenBlog() {
		homePage.navigateToBlogs();
		blogsPage.openBlog();
		blogsPage.switchToChildWindow();
		String blogTitle = driver.getTitle();
		System.out.println("Blog Title: " + blogTitle);
		Assert.assertTrue(blogTitle.contains("Indian Cloud Gaming Industry | Mobile Multiplayer Game Dev"),
				"Blog title verification");
	}

	@AfterClass
	public void tearDown() {
		 quitDriver();
	}

}
