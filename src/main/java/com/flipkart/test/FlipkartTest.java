package com.flipkart.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartTest {
    public static void main(String[] args) throws Exception {

        // Setup ChromeDriver automatically
        WebDriverManager.chromedriver().setup();

        // Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        
        // Launch Chrome
        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Open Flipkart
            driver.get("https://www.flipkart.com");
            System.out.println("✅ Flipkart opened");
            Thread.sleep(3000);

            // Close login popup if appears
            try {
                WebElement closeBtn = driver.findElement(By.xpath("//button[@class='_2KpZ6l _2doB4z']"));
                closeBtn.click();
                System.out.println("✅ Login popup closed");
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("⚠️ No login popup found");
            }

            // // Click Login button
            Thread.sleep(2000);
            try {
                driver.findElement(By.xpath("//span[text()='Login']")).click();
            } catch (Exception e) {
                System.out.println("Login button not visible, moving on...");
            }

            
            // // Enter mobile number
            driver.findElement(By.xpath("//input[contains(@class,'r4vIwl') and contains(@class,'BV+Dqf')]")).sendKeys("8799516975");


            // //// Click Request OTP / Continue
            driver.findElement(By.xpath("//button[contains(text(),'Request OTP')]")).click();

            // // Wait 20 sec for OTP manually
            System.out.println("Waiting for 20 seconds... Please enter OTP manually.");
            Thread.sleep(20000);
             
            // Search for shoes
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("shoes" + Keys.ENTER);
            System.out.println("✅ Searched for shoes");
            Thread.sleep(4000);

            // Click PUMA filter
            WebElement pumaFilter = driver.findElement(By.xpath("//div[text()='PUMA']/preceding-sibling::div"));
            pumaFilter.click();
            System.out.println("✅ PUMA filter applied");
            Thread.sleep(4000);

            // Store parent window
            String parentWindow = driver.getWindowHandle();
            System.out.println("✅ Parent window stored: " + parentWindow);

            // Click first product
            WebElement firstProduct = driver.findElement(By.xpath("(//a[contains(@href,'/p/')])[1]"));
            firstProduct.click();
            System.out.println("✅ Clicked first product");
            Thread.sleep(5000);

            // THE FIX: Switch to CORRECT tab by checking all tabs
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            System.out.println("📊 Total windows open: " + tabs.size());
            
            boolean foundProductPage = false;
            for (String tab : tabs) {
                driver.switchTo().window(tab);
                String currentUrl = driver.getCurrentUrl();
                System.out.println("🔍 Checking tab with URL: " + currentUrl);
                
                // Find the tab that has /p/ in URL (product page)
                if (currentUrl.contains("/p/") && currentUrl.contains("flipkart.com")) {
                    System.out.println("✅✅✅ FOUND THE CORRECT PRODUCT TAB!");
                    foundProductPage = true;
                    break;
                }
            }

            if (!foundProductPage) {
                System.out.println("❌ Could not find product page! Trying last tab...");
                driver.switchTo().window(tabs.get(tabs.size() - 1));
            }

            Thread.sleep(3000);
            System.out.println("📍 Final URL: " + driver.getCurrentUrl());

            // Scroll down slowly to load everything
            js.executeScript("window.scrollTo({top: 500, behavior: 'smooth'});");
            Thread.sleep(2000);
            System.out.println("✅ Scrolled down 500px");

            // Try multiple XPaths for WISHLIST
            System.out.println("\n🔍 Trying to find WISHLIST button...");
            boolean wishlistClicked = false;
            
            String[] wishlistXPaths = {
                "//div[@class='oUss6M _2EB96d jmR1E0 gREhVj']",
                "//button[contains(text(),'ADD TO WISHLIST')]",
                "//span[contains(text(),'ADD TO WISHLIST')]",
                "//div[contains(@class,'_2EB96d')]",
                "//*[contains(text(),'WISHLIST')]",
                "//button[contains(@class,'_2KpZ6l _1GBz0c')]"
            };

            for (String xpath : wishlistXPaths) {
                try {
                    WebElement wishlistBtn = driver.findElement(By.xpath(xpath));
                    System.out.println("✅ Found wishlist with xpath: " + xpath);
                    
                    // Highlight element for debugging
                    js.executeScript("arguments[0].style.border='5px solid red'", wishlistBtn);
                    Thread.sleep(1000);
                    
                    // Scroll to it
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", wishlistBtn);
                    Thread.sleep(1000);
                    
                    // Try JS click
                    js.executeScript("arguments[0].click();", wishlistBtn);
                    System.out.println("✅✅✅ WISHLIST CLICKED!");
                    wishlistClicked = true;
                    Thread.sleep(2000);
                    break;
                } catch (Exception e) {
                    // Silent fail, try next xpath
                }
            }

            if (!wishlistClicked) {
                System.out.println("❌❌❌ WISHLIST NOT FOUND WITH ANY XPATH!");
            }

            // Try multiple XPaths for ADD TO CART
            System.out.println("\n🔍 Trying to find ADD TO CART button...");
            boolean cartClicked = false;
            
            String[] cartXPaths = {
                "//button[normalize-space()='Add to cart']",
                "//button[contains(text(),'Add to cart')]",
                "//button[contains(@class,'_2KpZ6l') and contains(text(),'cart')]",
                "//li[@class='_1KDZP9']//button",
                "//*[contains(text(),'ADD TO CART')]",
                "//button[contains(@class,'_2KpZ6l _2U9uOA')]"
            };

            for (String xpath : cartXPaths) {
                try {
                    WebElement cartBtn = driver.findElement(By.xpath(xpath));
                    System.out.println("✅ Found cart button with xpath: " + xpath);
                    
                    // Highlight element
                    js.executeScript("arguments[0].style.border='5px solid blue'", cartBtn);
                    Thread.sleep(1000);
                    
                    // Scroll to it
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", cartBtn);
                    Thread.sleep(1000);
                    
                    // JS click
                    js.executeScript("arguments[0].click();", cartBtn);
                    System.out.println("✅✅✅ ADD TO CART CLICKED!");
                    cartClicked = true;
                    Thread.sleep(3000);
                    break;
                } catch (Exception e) {
                    // Silent fail, try next
                }
            }

            if (!cartClicked) {
                System.out.println("❌❌❌ ADD TO CART NOT FOUND WITH ANY XPATH!");
            }

            // QUANTITY increase
            System.out.println("\n🔍 Trying to increase QUANTITY...");
            try {
                Thread.sleep(2000);
                js.executeScript("window.scrollBy(0, 300);");
                Thread.sleep(1500);
                
                String[] quantityXPaths = {
                    "//button[contains(@class,'_23FHuj')]",
                    "//button[contains(@class,'qa-add-quantity')]",
                    "//button[contains(text(),'+')]",
                    "//button[@class='_23FHuj']",
                    "//button[contains(@class,'qa-increaseQuantity')]"
                };

                WebElement plusBtn = null;
                for (String xpath : quantityXPaths) {
                    try {
                        plusBtn = driver.findElement(By.xpath(xpath));
                        System.out.println("✅ Found quantity button with xpath: " + xpath);
                        break;
                    } catch (Exception e) {
                        // Silent fail
                    }
                }

                if (plusBtn != null) {
                    // Highlight
                    js.executeScript("arguments[0].style.border='5px solid green'", plusBtn);
                    Thread.sleep(1000);
                    
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", plusBtn);
                    Thread.sleep(1000);
                    
                    js.executeScript("arguments[0].click();", plusBtn);
                    System.out.println("✅ Quantity increased to 2");
                    Thread.sleep(1500);

                    js.executeScript("arguments[0].click();", plusBtn);
                    System.out.println("✅ Quantity increased to 3");
                    Thread.sleep(1500);
                    
                    System.out.println("✅✅✅ QUANTITY SET TO 3!");
                } else {
                    System.out.println("❌❌❌ QUANTITY BUTTON NOT FOUND!");
                }
                
            } catch (Exception e) {
                System.out.println("❌ Quantity error: " + e.getMessage());
            }

            System.out.println("\n\n🔥🔥🔥 PROCESS COMPLETE! CHECK RESULTS ABOVE! 🔥🔥🔥");
            
            // Keep browser open
            Thread.sleep(15000);

        } catch (Exception e) {
            System.out.println("💥 MAJOR ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Uncomment to close
        // driver.quit();
    }
}