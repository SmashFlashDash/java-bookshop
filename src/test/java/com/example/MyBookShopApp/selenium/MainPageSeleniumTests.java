package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainPageSeleniumTests {
    private static ChromeDriver driver;
    private final String seleniumPath = "C://Users/SVAN/Downloads/chromedriver_win32/chromedriver.exe";

    @BeforeAll
    public void setup() {
        System.setProperty("webdriver.chrome.driver", seleniumPath);
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    public void afterAll() {
        driver.quit();
    }

    @Test
    public void testMainPageAcess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();
        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    public void testMainPageSearchQuery() throws InterruptedException {
        driver.manage().window().maximize();    // или элемент unreachable т.к. скрыт в верстке
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .setUpSearchToken("Snowriders")  // заполнгить строку поиска
                .pause()
                .submit()   // нажать кнопку
                .pause();
        assertTrue(driver.getPageSource().contains("Snowriders"));
    }
}