package com.flentas.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.flentas.utils.TestUtil;

public class HomePage {
	WebDriver driver;

	@FindBy(xpath = "//a[normalize-space()='Careers']")
	WebElement careersLink;

	@FindBy(id = "navbarDropdown")
	WebElement resourcesDropdown;

	@FindBy(xpath = "//a[@class='dropdown-item serv-item'][normalize-space()='Blogs']")
	WebElement blogsLink;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public CareerPage clickCareers() {
		TestUtil.forceClick(driver, By.xpath("//a[normalize-space()='Careers']"));
		return new CareerPage(driver);
	}

	public List<WebElement> getAllLinks() {
		return driver.findElements(By.tagName("a"));
	}

	public void openHomePage(String url) {
		driver.get(url);
	}

	public void navigateToBlogs() {
		TestUtil.forceClick(driver, By.id("navbarDropdown"));
		TestUtil.forceClick(driver, By.xpath("//a[@class='dropdown-item serv-item'][normalize-space()='Blogs']"));
	}
}
