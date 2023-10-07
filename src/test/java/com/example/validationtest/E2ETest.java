package com.example.validationtest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {


    @LocalServerPort
    private int port;

    @Test
    void 名前が長すぎ() {
        FirefoxDriver driver = new FirefoxDriver();
        try {
            driver.get("http://localhost:" + port + "/");
            WebElement name = driver.findElement(By.id("name"));
            name.clear();
            name.sendKeys("""
                    寿限無寿限無五劫のすりきれ海砂利水魚水行末雲来末風来末
                    食う寝るところに住むところ、
                    やぶらこうじのぶらこうじ、
                    パイポ・パイポ・パイポのシューリンガン、
                    シューリンガンのグーリンダイ、
                    グーリンダイのポンポコピーのポンポコナの、
                    長久命の長助""");
            WebElement age = driver.findElement(By.id("age"));
            age.clear();
            age.sendKeys("20");
            // フォームをサブミット
            driver.findElement(By.id("personForm")).submit();
            // nameErrorが登場するまで(サブミット後、ロードが完了するまで)待つ
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(e -> e.findElements(By.id("nameError")).size() == 1);
            WebElement nameError = driver.findElement(By.id("nameError"));
            assertEquals("名前は2文字から30文字以内にしてください", nameError.getText());
            assertEquals(0, driver.findElements(By.id("ageError")).size());

        } finally {
            driver.quit();
        }
    }

    @Test
    void 名前が短すぎ() {
        FirefoxDriver driver = new FirefoxDriver();
        try {
            driver.get("http://localhost:" + port + "/");
            WebElement name = driver.findElement(By.id("name"));
            name.clear();
            name.sendKeys("寿");
            WebElement age = driver.findElement(By.id("age"));
            age.clear();
            age.sendKeys("20");
            // フォームをサブミット
            driver.findElement(By.id("personForm")).submit();
            // nameErrorが登場するまで(サブミット後、ロードが完了するまで)待つ
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(e -> e.findElements(By.id("nameError")).size() == 1);
            WebElement nameError = driver.findElement(By.id("nameError"));
            assertEquals("名前は2文字から30文字以内にしてください", nameError.getText());
            assertEquals(0, driver.findElements(By.id("ageError")).size());

        } finally {
            driver.quit();
        }
    }

    @Test
    void 年齢低すぎ() {
        FirefoxDriver driver = new FirefoxDriver();
        try {
            driver.get("http://localhost:" + port + "/");
            WebElement name = driver.findElement(By.id("name"));
            name.clear();
            name.sendKeys("山本ユースケ");
            WebElement age = driver.findElement(By.id("age"));
            age.clear();
            age.sendKeys("18");
            // フォームをサブミット
            driver.findElement(By.id("personForm")).submit();
            // ageErrorが登場するまで(サブミット後、ロードが完了するまで)待つ
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(e -> e.findElements(By.id("ageError")).size() == 1);
            WebElement ageError = driver.findElement(By.id("ageError"));
            assertEquals(0, driver.findElements(By.id("nameError")).size());
            assertEquals("年齢は20以上である必要があります。", ageError.getText());
        } finally {
            driver.quit();
        }
    }

    @Test
    void 正常() {
        FirefoxDriver driver = new FirefoxDriver();
        try {
            driver.get("http://localhost:" + port + "/");
            WebElement name = driver.findElement(By.id("name"));
            name.clear();
            name.sendKeys("山本ユースケ");
            WebElement age = driver.findElement(By.id("age"));
            age.clear();
            age.sendKeys("32");
            // フォームをサブミット
            driver.findElement(By.id("personForm")).submit();
            // サブミット完了を待つ
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(e -> e.findElements(By.tagName("h1")).size() == 1);
            assertTrue(driver.getPageSource().contains("受け付けました"));
        } finally {
            driver.quit();
        }
    }
}

