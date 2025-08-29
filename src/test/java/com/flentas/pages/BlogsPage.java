package com.flentas.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

public class BlogsPage {
	WebDriver driver;

	// Locators
	private By nextPageBtn = By.id("nextPageBtn");
	private String blogCardXpath = "//h4[contains(normalize-space(), 'Indian Gaming Industry')]/ancestor::a[1]";

	public BlogsPage(WebDriver driver) {
		this.driver = driver;
	}

	// Open the target blog
	public void openBlog() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		for (int page = 1; page <= 17; page++) {

			// Find blog cards on current page
			List<WebElement> blogCards = driver.findElements(By.xpath(blogCardXpath));
			if (!blogCards.isEmpty()) {
				WebElement blogCard = blogCards.get(0);
				js.executeScript("arguments[0].scrollIntoView(true);", blogCard);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ignored) {
				}
				js.executeScript("arguments[0].click();", blogCard);
				break;
			}

			// Locate Next button dynamically on each page
			try {
				WebElement nextBtn = driver.findElement(nextPageBtn);
				if (nextBtn.isDisplayed() && nextBtn.isEnabled()) {
					js.executeScript("arguments[0].scrollIntoView(true);", nextBtn);
					js.executeScript("arguments[0].click();", nextBtn);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException ignored) {
					}
				} else {

					break;
				}
			} catch (NoSuchElementException e) {

				break;
			}
		}
	}

	// Switch to child window
	public void switchToChildWindow() {
		String parentWindow = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(parentWindow)) {
				driver.switchTo().window(handle);
				break;
			}
		}
	}

}
