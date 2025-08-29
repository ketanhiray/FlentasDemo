package com.flentas.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.flentas.utils.TestUtil;

public class JobDetailsPage {
    WebDriver driver;

    @FindBy(css = "h3[title='QA Lead']")
    WebElement qaLeadJob;

    @FindBy(css = "#apply-job")
    WebElement applyBtn;

    public JobDetailsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openQaLeadJob() {
        TestUtil.safeClick(driver, qaLeadJob);
    }

    public void clickApply() {
        TestUtil.safeClick(driver, applyBtn);
    }
}
