package com.example.studentresult.system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        DemoPause.pause();
    }

    public void login(String username, String password) {
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        DemoPause.pause();
        driver.findElement(By.id("loginButton")).click();
        DemoPause.pause();
    }

    public boolean showsInvalidLogin() {
        wait.until(ExpectedConditions.urlContains("error"));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error")))
                .getText()
                .contains("Invalid username or password");
    }
}
