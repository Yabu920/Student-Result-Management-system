package com.example.studentresult.system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ResultEntryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ResultEntryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/results");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mark")));
    }

    public void enterFirstStudentResult(int mark) {
        createResultByIndex(0, 0, mark);
    }

    public void createResult(String studentNumber, String courseCode, int mark) {
        selectOptionContaining(By.id("studentId"), studentNumber);
        selectOptionContaining(By.id("courseId"), courseCode);
        WebElement markInput = driver.findElement(By.id("mark"));
        markInput.clear();
        markInput.sendKeys(String.valueOf(mark));
        driver.findElement(By.id("saveResult")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.message, p.error, #mark")));
    }

    public void createResultByIndex(int studentIndex, int courseIndex, int mark) {
        new Select(driver.findElement(By.id("studentId"))).selectByIndex(studentIndex);
        new Select(driver.findElement(By.id("courseId"))).selectByIndex(courseIndex);
        WebElement markInput = driver.findElement(By.id("mark"));
        markInput.clear();
        markInput.sendKeys(String.valueOf(mark));
        driver.findElement(By.id("saveResult")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.message, p.error, #mark")));
    }

    public String firstGrade() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#resultsTable .grade"))).getText();
    }

    public String gradeFor(String studentNumber, String courseCode, int mark) {
        return resultRow(studentNumber, courseCode, mark)
                .findElement(By.cssSelector(".grade"))
                .getText();
    }

    public String statusFor(String studentNumber, String courseCode, int mark) {
        return resultRow(studentNumber, courseCode, mark)
                .findElement(By.cssSelector(".status"))
                .getText();
    }

    public void changeStatus(String studentNumber, String courseCode, int mark, String targetStatus) {
        WebElement row = resultRow(studentNumber, courseCode, mark);
        Select statusSelect = new Select(row.findElement(By.cssSelector(".target-status")));
        statusSelect.selectByValue(targetStatus);
        row.findElement(By.cssSelector(".changeStatus")).click();
        wait.until(ExpectedConditions.stalenessOf(row));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mark")));
    }

    public String errorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error"))).getText();
    }

    public boolean hasResult(String studentNumber, String courseCode, int mark) {
        return !driver.findElements(resultSelector(studentNumber, courseCode, mark)).isEmpty();
    }

    private WebElement resultRow(String studentNumber, String courseCode, int mark) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultSelector(studentNumber, courseCode, mark)));
    }

    private By resultSelector(String studentNumber, String courseCode, int mark) {
        return By.cssSelector(".result-row[data-student-number='" + studentNumber + "'][data-course-code='" + courseCode + "'][data-mark='" + mark + "']");
    }

    private void selectOptionContaining(By selectLocator, String text) {
        Select select = new Select(driver.findElement(selectLocator));
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            if (option.getText().contains(text)) {
                select.selectByVisibleText(option.getText());
                return;
            }
        }
        throw new IllegalArgumentException("No select option contains: " + text);
    }
}
