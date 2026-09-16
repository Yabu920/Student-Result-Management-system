package com.example.studentresult.system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StudentResultPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public StudentResultPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/student-results");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("studentNumber")));
    }

    public void search(String studentNumber) {
        driver.findElement(By.id("studentNumber")).clear();
        driver.findElement(By.id("studentNumber")).sendKeys(studentNumber);
        driver.findElement(By.id("searchResults")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("studentResultsTable")));
    }

    public boolean hasResultsTable() {
        return !driver.findElements(By.id("studentResultsTable")).isEmpty();
    }

    public boolean containsResult(String courseCode, int mark, String grade) {
        return !driver.findElements(resultSelector(courseCode, mark, grade)).isEmpty();
    }

    public boolean doesNotContainResult(String courseCode, int mark, String grade) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("studentResultsTable")));
        return driver.findElements(resultSelector(courseCode, mark, grade)).isEmpty();
    }

    private By resultSelector(String courseCode, int mark, String grade) {
        return By.cssSelector(".student-result-row[data-course-code='" + courseCode + "'][data-mark='" + mark + "'][data-grade='" + grade + "']");
    }
}
