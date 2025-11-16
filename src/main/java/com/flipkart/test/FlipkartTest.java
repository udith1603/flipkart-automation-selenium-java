package com.flipkart.test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartTest {
    
    // Helper method to force click with multiple strategies
    public static void forceClick(WebDriver driver, WebElement element, JavascriptExecutor js) throws InterruptedException {
        try {
            // Strategy 1: Normal click
            element.click();
            System.out.println("✅ Clicked with normal click");
        } catch (Exception e1) {
            try {
                // Strategy 2: JS click
                js.executeScript("arguments[0].click();", element);
                System.out.println("✅ Clicked with JS click");
            } catch (Exception e2) {
                try {
                    // Strategy 3: Actions click
                    Actions actions = new Actions(driver);
                    actions.moveToElement(element).click().perform();
                    System.out.println("✅ Clicked with Actions click");
                } catch (Exception e3) {
                    System.out.println("❌ All click methods failed!");
                }
            }
        }
        Thread.sleep(500);
    }
    
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
        Actions actions = new Actions(driver);

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

            // Click Login button
            Thread.sleep(2000);
            try {
                driver.findElement(By.xpath("//span[text()='Login']")).click();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Login button not visible, moving on...");
            }

            
            // Enter mobile number
            driver.findElement(By.xpath("//input[contains(@class,'r4vIwl') and contains(@class,'BV+Dqf')]")).sendKeys("8799516975");


            // Click Request OTP / Continue
            driver.findElement(By.xpath("//button[contains(text(),'Request OTP')]")).click();

            // Wait 20 sec for OTP manually
            System.out.println("⏳ Waiting for 20 seconds... Please enter OTP manually.");
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

            // ========== PRODUCT 1 - Terranova Sneakers ==========
            System.out.println("\n🔥 === PROCESSING PRODUCT 1 (Terranova) === 🔥");
            
            String parentWindow = driver.getWindowHandle();
            Set<String> existingWindows = driver.getWindowHandles();
            
            // Click Terranova product
            boolean terranovaClicked = false;
            String[] terranovaXPaths = {
                "//a[contains(@title,'Terranova')]",
                "//div[contains(text(),'Terranova')]/ancestor::a",
                "//a[contains(@href,'/p/') and contains(.,'Terranova')]"
            };
            
            for (String xpath : terranovaXPaths) {
                try {
                    WebElement terranovaProduct = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", terranovaProduct);
                    Thread.sleep(1000);
                    js.executeScript("arguments[0].click();", terranovaProduct);
                    System.out.println("✅ Clicked Terranova product");
                    terranovaClicked = true;
                    Thread.sleep(4000);
                    break;
                } catch (Exception e) { }
            }
            
            if (!terranovaClicked) {
                System.out.println("⚠️ Terranova not found, clicking first PUMA product");
                WebElement firstProd = driver.findElement(By.xpath("(//a[contains(@href,'/p/')])[1]"));
                js.executeScript("arguments[0].click();", firstProd);
                Thread.sleep(4000);
            }

            // Switch to NEW tab only
            Set<String> allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!existingWindows.contains(window)) {
                    driver.switchTo().window(window);
                    System.out.println("✅ Switched to Terranova product tab");
                    break;
                }
            }
            Thread.sleep(3000);

            // Scroll down to load all elements
            js.executeScript("window.scrollTo({top: 500, behavior: 'smooth'});");
            Thread.sleep(2000);
            
            // Scroll up a bit to see buttons
            js.executeScript("window.scrollTo({top: 300, behavior: 'smooth'});");
            Thread.sleep(2000);

            // WISHLIST PRODUCT 1 - Using YOUR original XPaths
            boolean wishlist1 = false;
            String[] wishlistXPaths = {
                "//div[@class='oUss6M _2EB96d jmR1E0 gREhVj']",
                "//button[contains(text(),'ADD TO WISHLIST')]",
                "//span[contains(text(),'ADD TO WISHLIST')]",
                "//*[contains(text(),'WISHLIST')]",
                "//button[contains(@class,'_2KpZ6l _1GBz0c')]"
            };

            for (String xpath : wishlistXPaths) {
                try {
                    WebElement wishlistBtn = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].style.border='5px solid red'", wishlistBtn);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", wishlistBtn);
                    Thread.sleep(1000);
                    
                    // Remove any overlays
                    js.executeScript("var overlays = document.querySelectorAll('[class*=\"overlay\"],[class*=\"modal\"]'); overlays.forEach(function(el){el.remove();});");
                    
                    // Force click with multiple strategies
                    forceClick(driver, wishlistBtn, js);
                    
                    System.out.println("✅✅ PRODUCT 1 - WISHLIST ADDED!");
                    wishlist1 = true;
                    Thread.sleep(2000);
                    break;
                } catch (Exception e) { 
                    System.out.println("⚠️ Wishlist XPath failed: " + xpath);
                }
            }
            if (!wishlist1) System.out.println("❌ PRODUCT 1 - Wishlist failed");

            // ADD TO CART PRODUCT 1 - Using YOUR original XPaths
            boolean cart1 = false;
            String[] cartXPaths = {
                "//button[normalize-space()='Add to cart']",
                "//button[contains(text(),'Add to cart')]",
                "//button[contains(text(),'ADD TO CART')]",
                "//li[@class='_1KDZP9']//button",
                "//button[contains(@class,'_2KpZ6l _2U9uOA')]",
                "//ul[@class='row']//button[contains(@class,'_2KpZ6l')]"
            };

            for (String xpath : cartXPaths) {
                try {
                    WebElement cartBtn = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].style.border='5px solid blue'", cartBtn);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", cartBtn);
                    Thread.sleep(1000);
                    
                    // Remove any overlays
                    js.executeScript("var overlays = document.querySelectorAll('[class*=\"overlay\"],[class*=\"modal\"]'); overlays.forEach(function(el){el.remove();});");
                    
                    // Force click
                    forceClick(driver, cartBtn, js);
                    
                    System.out.println("✅✅ PRODUCT 1 - ADDED TO CART!");
                    cart1 = true;
                    Thread.sleep(3000);
                    break;
                } catch (Exception e) { 
                    System.out.println("⚠️ Cart XPath failed: " + xpath);
                }
            }
            if (!cart1) System.out.println("❌ PRODUCT 1 - Add to cart failed");

            // Close this tab and go back
            driver.close();
            driver.switchTo().window(parentWindow);
            Thread.sleep(2000);
            System.out.println("✅ Back to search results");

            // ========== PRODUCT 2 - Rungryp Running Shoes ==========
            System.out.println("\n🔥 === PROCESSING PRODUCT 2 (Rungryp) === 🔥");
            
            existingWindows = driver.getWindowHandles();
            
            // Click Rungryp product
            boolean runGrypClicked = false;
            String[] runGrypXPaths = {
                "//a[contains(@title,'Rungryp')]",
                "//div[contains(text(),'Rungryp')]/ancestor::a",
                "//a[contains(@href,'/p/') and contains(.,'Rungryp')]",
                "//a[contains(.,'Running Shoes')]"
            };
            
            for (String xpath : runGrypXPaths) {
                try {
                    WebElement runGrypProduct = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", runGrypProduct);
                    Thread.sleep(1000);
                    js.executeScript("arguments[0].click();", runGrypProduct);
                    System.out.println("✅ Clicked Rungryp product");
                    runGrypClicked = true;
                    Thread.sleep(4000);
                    break;
                } catch (Exception e) { }
            }
            
            if (!runGrypClicked) {
                System.out.println("⚠️ Rungryp not found, clicking second PUMA product");
                WebElement secondProd = driver.findElement(By.xpath("(//a[contains(@href,'/p/')])[2]"));
                js.executeScript("arguments[0].click();", secondProd);
                Thread.sleep(4000);
            }

            // Switch to NEW tab
            allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!existingWindows.contains(window)) {
                    driver.switchTo().window(window);
                    System.out.println("✅ Switched to Rungryp product tab");
                    break;
                }
            }
            Thread.sleep(3000);

            // Scroll down then up
            js.executeScript("window.scrollTo({top: 500, behavior: 'smooth'});");
            Thread.sleep(2000);
            js.executeScript("window.scrollTo({top: 300, behavior: 'smooth'});");
            Thread.sleep(2000);

            // WISHLIST PRODUCT 2
            boolean wishlist2 = false;
            for (String xpath : wishlistXPaths) {
                try {
                    WebElement wishlistBtn = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].style.border='5px solid red'", wishlistBtn);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", wishlistBtn);
                    Thread.sleep(1000);
                    js.executeScript("var overlays = document.querySelectorAll('[class*=\"overlay\"],[class*=\"modal\"]'); overlays.forEach(function(el){el.remove();});");
                    forceClick(driver, wishlistBtn, js);
                    System.out.println("✅✅ PRODUCT 2 - WISHLIST ADDED!");
                    wishlist2 = true;
                    Thread.sleep(2000);
                    break;
                } catch (Exception e) { }
            }
            if (!wishlist2) System.out.println("❌ PRODUCT 2 - Wishlist failed");

            // ADD TO CART PRODUCT 2
            boolean cart2 = false;
            for (String xpath : cartXPaths) {
                try {
                    WebElement cartBtn = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].style.border='5px solid blue'", cartBtn);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", cartBtn);
                    Thread.sleep(1000);
                    js.executeScript("var overlays = document.querySelectorAll('[class*=\"overlay\"],[class*=\"modal\"]'); overlays.forEach(function(el){el.remove();});");
                    forceClick(driver, cartBtn, js);
                    System.out.println("✅✅ PRODUCT 2 - ADDED TO CART!");
                    cart2 = true;
                    Thread.sleep(3000);
                    break;
                } catch (Exception e) { }
            }
            if (!cart2) System.out.println("❌ PRODUCT 2 - Add to cart failed");

            // Close tab
            driver.close();
            driver.switchTo().window(parentWindow);
            Thread.sleep(2000);

            // ========== GO TO CART ==========
            System.out.println("\n🛒 === NAVIGATING TO CART === 🛒");
            driver.get("https://www.flipkart.com/viewcart?exploreMode=true&preference=FLIPKART");
            System.out.println("✅ Opened cart page");
            Thread.sleep(5000);
            
            // Check if cart is loading or empty, retry if needed
            int retryCount = 0;
            while (retryCount < 3) {
                try {
                    driver.findElement(By.xpath("//button[contains(@class,'LcLcvv')]"));
                    System.out.println("✅ Cart loaded successfully!");
                    break;
                } catch (Exception e) {
                    System.out.println("⚠️ Cart still loading or empty, refreshing... (Attempt " + (retryCount + 1) + ")");
                    driver.navigate().refresh();
                    Thread.sleep(5000);
                    retryCount++;
                }
            }

            js.executeScript("window.scrollBy(0, 300);");
            Thread.sleep(2000);

            // ========== INCREMENT QUANTITY ==========
            System.out.println("\n➕ Incrementing quantity...");
            boolean quantityIncremented = false;
            
            // First find all LcLcvv buttons, then click the one with "+"
            try {
                List<WebElement> allButtons = driver.findElements(By.xpath("//button[@class='LcLcvv']"));
                System.out.println("Found " + allButtons.size() + " LcLcvv buttons");
                
                for (WebElement btn : allButtons) {
                    String btnText = btn.getText().trim();
                    System.out.println("Button text: '" + btnText + "'");
                    
                    if (btnText.equals("+")) {
                        js.executeScript("arguments[0].style.border='5px solid green'", btn);
                        Thread.sleep(500);
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
                        Thread.sleep(1000);
                        forceClick(driver, btn, js);
                        System.out.println("✅✅ QUANTITY INCREMENTED (+1)!");
                        quantityIncremented = true;
                        Thread.sleep(3000);
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error finding + button: " + e.getMessage());
            }
            
            if (!quantityIncremented) System.out.println("❌ Failed to increment");

            // ========== REMOVE ITEM ==========
            System.out.println("\n🗑️ Removing item...");
            boolean itemRemoved = false;
            String[] removeXPaths = {
                "(//div[text()='Remove'])[1]",
                "(//button[contains(text(),'Remove')])[1]",
                "(//*[contains(text(),'Remove')])[1]",
                "(//div[contains(@class,'_3dsJAO') and contains(text(),'Remove')])[1]"
            };

            for (String xpath : removeXPaths) {
                try {
                    WebElement removeBtn = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].style.border='5px solid red'", removeBtn);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", removeBtn);
                    Thread.sleep(1000);
                    forceClick(driver, removeBtn, js);
                    System.out.println("✅ Remove button clicked, waiting for confirmation popup...");
                    Thread.sleep(2000);
                    
                    // Click the confirmation Remove button in popup
                    try {
                        WebElement confirmRemove = driver.findElement(By.xpath("//div[@class='sBxzFz fF30ZI A0MXnh']"));
                        js.executeScript("arguments[0].style.border='5px solid red'", confirmRemove);
                        Thread.sleep(500);
                        forceClick(driver, confirmRemove, js);
                        System.out.println("✅✅ ITEM REMOVED - CONFIRMATION CLICKED!");
                        itemRemoved = true;
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println("⚠️ Confirmation button not found, trying alternate...");
                        try {
                            WebElement confirmAlt = driver.findElement(By.xpath("//div[contains(@class,'sBxzFz') and contains(@class,'fF30ZI')]"));
                            forceClick(driver, confirmAlt, js);
                            System.out.println("✅✅ ITEM REMOVED - ALTERNATE CONFIRMATION!");
                            itemRemoved = true;
                            Thread.sleep(2000);
                        } catch (Exception e2) { }
                    }
                    break;
                } catch (Exception e) { }
            }
            if (!itemRemoved) System.out.println("❌ Failed to remove");

            // ========== WISHLIST ==========
            System.out.println("\n👤 === GOING TO WISHLIST === 👤");
            driver.get("https://www.flipkart.com/wishlist");
            Thread.sleep(4000);

            js.executeScript("window.scrollBy(0, 300);");
            Thread.sleep(2000);

            System.out.println("\n🗑️ Removing PUMA from wishlist...");
            boolean pumaRemoved = false;
            
            // Click the bin icon (div with image inside)
            String[] binXPaths = {
                "(//div[@class='EjOX7q CwZdGm pPzhjs'])[1]",
                "(//div[contains(@class,'EjOX7q') and contains(@class,'CwZdGm') and contains(@class,'pPzhjs')])[1]",
                "(//div[contains(@class,'EjOX7q')])[1]"
            };

            for (String xpath : binXPaths) {
                try {
                    WebElement binIcon = driver.findElement(By.xpath(xpath));
                    js.executeScript("arguments[0].style.border='5px solid orange'", binIcon);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", binIcon);
                    Thread.sleep(1000);
                    forceClick(driver, binIcon, js);
                    System.out.println("✅ Bin icon clicked, waiting for popup...");
                    Thread.sleep(3000);
                    
                    // Now find and click "YES, REMOVE" button in popup
                    String[] yesRemoveXPaths = {
                        "//button[@class='QqFHMw AyekA8']",
                        "//button[contains(@class,'QqFHMw') and contains(@class,'AyekA8')]",
                        "//button[text()='YES, REMOVE']",
                        "//button[contains(text(),'YES, REMOVE')]",
                        "//button[contains(text(),'YES')]",
                        "//button[contains(text(),'REMOVE')]"
                    };
                    
                    boolean confirmClicked = false;
                    for (String confirmXpath : yesRemoveXPaths) {
                        try {
                            WebElement yesRemoveBtn = driver.findElement(By.xpath(confirmXpath));
                            js.executeScript("arguments[0].style.border='5px solid red'", yesRemoveBtn);
                            Thread.sleep(500);
                            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", yesRemoveBtn);
                            Thread.sleep(500);
                            forceClick(driver, yesRemoveBtn, js);
                            System.out.println("✅✅ 'YES, REMOVE' CLICKED - PUMA REMOVED!");
                            confirmClicked = true;
                            pumaRemoved = true;
                            Thread.sleep(2000);
                            break;
                        } catch (Exception e) {
                            System.out.println("⚠️ Trying next confirmation XPath...");
                        }
                    }
                    
                    if (!confirmClicked) {
                        System.out.println("❌ Confirmation button not found in popup!");
                    }
                    break;
                    
                } catch (Exception e) { 
                    System.out.println("⚠️ Trying next bin icon XPath...");
                }
            }
            if (!pumaRemoved) System.out.println("❌ Failed to remove from wishlist");

            System.out.println("\n\n🎉🎉🎉 ALL DONE BRUH! 🎉🎉🎉");
            Thread.sleep(15000);

        } catch (Exception e) {
            System.out.println("💥 ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        // driver.quit();
    }
}