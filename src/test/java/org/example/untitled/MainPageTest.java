package org.example.untitled;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class MainPageTest {
    public static WebDriver driver;
    public static void openNewTab() {
        ((JavascriptExecutor)driver).executeScript("window.open('about:blank','_blank');");
    }

    public static void analyzeLog() {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
            System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
            //do something useful with the data
        }
    }

    @Test
    public static void openPage() {

        System.setProperty("webdriver.chrome.driver", "c://chromedriver//chromedriver81.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("allow-file-access-from-files");
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.media_stream_mic", 1);
        prefs.put("profile.default_content_setting_values.media_stream_camera", 1);
        prefs.put("profile.default_content_setting_values.geolocation", 1);
        prefs.put("profile.default_content_setting_values.notifications", 1);
        options.setExperimentalOption("prefs", prefs);
        driver=new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        String baseUrl = "http://www.google.co.uk/";
        driver.get(baseUrl);
        openNewTab();

        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0)); //switches to new tab
        driver.get("https://localhost:8443");
        MainPageTest.login("john@live.ie","");
        dismissAlert();


        driver.switchTo().window(tabs.get(1)); // switch back to main screen
        driver.get("https://localhost:8443");
        login("davidmather@live.ie","");
        dismissAlert();

        driver.findElement(By.id("onlineUsersContainer")).findElement(By.tagName("div")).click();

        driver.switchTo().window(tabs.get(0)); //switches to new tab
        driver.get("https://localhost:8443");
        login("john@live.ie","");
        dismissAlert();

        driver.findElement(By.id("onlineUsersContainer")).findElement(By.tagName("div")).click();

        driver.findElement(By.id("videocallbutton")).click();
        driver.switchTo().window(tabs.get(1));
        dismissAlert();
        analyzeLog();
        driver.switchTo().window(tabs.get(0));
        analyzeLog();


    }

    public static void login(String username, String password){
        driver.findElement(By.id("chatbutton")).click();
        driver.findElement(By.id("loginemail")).sendKeys(username);
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
    }

    public static void dismissAlert(){
        int i=0;
        while(i++<50000)
        {
            try
            {
                Alert alert = driver.switchTo().alert();
                alert.accept();
                break;
            }
            catch(NoAlertPresentException e)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                continue;
            }
        }
    }
}
