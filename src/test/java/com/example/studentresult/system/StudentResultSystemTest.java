package com.example.studentresult.system;

import com.example.studentresult.system.pages.DashboardPage;
import com.example.studentresult.system.pages.LoginPage;
import com.example.studentresult.system.pages.ResultEntryPage;
import com.example.studentresult.system.pages.StudentResultPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("system")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentResultSystemTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void validLoginShowsDashboard() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(baseUrl());
        loginPage.login("admin", "admin123");

        assertTrue(new DashboardPage(driver).isLoaded());
    }

    @Test
    void invalidLoginShowsError() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(baseUrl());
        loginPage.login("admin", "wrong");

        assertTrue(loginPage.showsInvalidLogin());
    }

    @Test
    void enterValidMarkShowsCalculatedGrade() {
        loginAsAdmin();
        ResultEntryPage resultEntryPage = new ResultEntryPage(driver);
        resultEntryPage.open(baseUrl());
        resultEntryPage.createResult("STU-001", "SE501", 90);

        assertEquals("A+", resultEntryPage.gradeFor("STU-001", "SE501", 90));
    }

    @Test
    void invalidMarkIsRejectedWithMessageAndNoResultCreated() {
        loginAsAdmin();
        ResultEntryPage resultEntryPage = new ResultEntryPage(driver);
        resultEntryPage.open(baseUrl());
        resultEntryPage.createResult("STU-001", "SE501", 101);

        assertTrue(resultEntryPage.errorMessage().contains("Mark must be between 0 and 100"));
        assertFalse(resultEntryPage.hasResult("STU-001", "SE501", 101));
    }

    @Test
    void completeResultLifecyclePublishesResult() {
        loginAsAdmin();
        ResultEntryPage resultEntryPage = new ResultEntryPage(driver);
        resultEntryPage.open(baseUrl());
        resultEntryPage.createResult("STU-002", "SE501", 85);

        assertEquals("DRAFT", resultEntryPage.statusFor("STU-002", "SE501", 85));

        resultEntryPage.changeStatus("STU-002", "SE501", 85, "SUBMITTED");
        assertEquals("SUBMITTED", resultEntryPage.statusFor("STU-002", "SE501", 85));

        resultEntryPage.changeStatus("STU-002", "SE501", 85, "REVIEWED");
        assertEquals("REVIEWED", resultEntryPage.statusFor("STU-002", "SE501", 85));

        resultEntryPage.changeStatus("STU-002", "SE501", 85, "PUBLISHED");
        assertEquals("PUBLISHED", resultEntryPage.statusFor("STU-002", "SE501", 85));
    }

    @Test
    void directDraftToPublishedTransitionIsRejected() {
        loginAsAdmin();
        ResultEntryPage resultEntryPage = new ResultEntryPage(driver);
        resultEntryPage.open(baseUrl());
        resultEntryPage.createResult("STU-002", "SE502", 80);

        resultEntryPage.changeStatus("STU-002", "SE502", 80, "PUBLISHED");

        assertTrue(resultEntryPage.errorMessage().contains("Invalid result workflow transition"));
        assertEquals("DRAFT", resultEntryPage.statusFor("STU-002", "SE502", 80));
    }

    @Test
    void studentCanViewPublishedResult() {
        loginAsAdmin();
        ResultEntryPage resultEntryPage = new ResultEntryPage(driver);
        resultEntryPage.open(baseUrl());
        resultEntryPage.createResult("STU-001", "SE502", 75);
        resultEntryPage.changeStatus("STU-001", "SE502", 75, "SUBMITTED");
        resultEntryPage.changeStatus("STU-001", "SE502", 75, "REVIEWED");
        resultEntryPage.changeStatus("STU-001", "SE502", 75, "PUBLISHED");

        StudentResultPage studentResultPage = new StudentResultPage(driver);
        studentResultPage.open(baseUrl());
        studentResultPage.search("STU-001");

        assertTrue(studentResultPage.containsResult("SE502", 75, "B+"));
    }

    @Test
    void studentCannotViewUnpublishedResult() {
        loginAsAdmin();
        ResultEntryPage resultEntryPage = new ResultEntryPage(driver);
        resultEntryPage.open(baseUrl());
        resultEntryPage.createResult("STU-003", "SE501", 70);

        StudentResultPage studentResultPage = new StudentResultPage(driver);
        studentResultPage.open(baseUrl());
        studentResultPage.search("STU-003");

        assertTrue(studentResultPage.doesNotContainResult("SE501", 70, "B"));
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private void loginAsAdmin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(baseUrl());
        loginPage.login("admin", "admin123");
        assertTrue(new DashboardPage(driver).isLoaded());
    }
}
