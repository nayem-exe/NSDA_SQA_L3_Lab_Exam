package com.automation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class sauce_demo_login {

    private WebDriver driver;
    private WebDriverWait wait;

    // Helper method to slow things down so you can watch the process
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void testSuccessfulLogin() {
        driver.get("https://www.saucedemo.com/");
        delay(2000); 

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        delay(2000); 

        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        delay(2000); 

        driver.findElement(By.id("login-button")).click();
        delay(2000); 

        WebElement appLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("app_logo")));
        
        System.out.println("--------------------------------------------");
        System.out.println("Successfully Logged In");
        System.out.println("--------------------------------------------");
        
        Assertions.assertEquals("Swag Labs", appLogo.getText());
    }

    @Test
    public void testUnsuccessfulLogin() {
        driver.get("https://www.saucedemo.com/");
        delay(2000); 

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        delay(2000); 

        driver.findElement(By.id("password")).sendKeys("wrong_password");
        delay(2000); 
        
        driver.findElement(By.id("login-button")).click();
        delay(2000); 

        WebElement errorElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container h3"))
        );

        System.out.println("--------------------------------------------");
        System.out.println("Invalid Username or Password");
        System.out.println("Actual Error: " + errorElement.getText());
        System.out.println("--------------------------------------------");

        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        Assertions.assertEquals(expectedErrorMessage, errorElement.getText());
    }
    
    @Test
    public void testLockedOutUser() {
        driver.get("https://www.saucedemo.com/");
        delay(2000); 

        // Entering the designated locked_out_user profile
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("locked_out_user");
        delay(2000); 

        driver.findElement(By.id("password")).sendKeys("secret_sauce"); 
        delay(2000); 
        
        driver.findElement(By.id("login-button")).click();
        delay(2000); 

        WebElement errorElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container h3"))
        );

        System.out.println("--------------------------------------------");
        System.out.println("locked_out_user");
        System.out.println("Actual Error: " + errorElement.getText());
        System.out.println("--------------------------------------------");
        
        String expectedErrorMessage = "Epic sadface: Sorry, this user has been locked out.";
        Assertions.assertEquals(expectedErrorMessage, errorElement.getText());
    }

    @AfterEach
    public void tearDown() {
        delay(3000); 
        if (driver != null) {
            driver.quit();
        }
    }
}