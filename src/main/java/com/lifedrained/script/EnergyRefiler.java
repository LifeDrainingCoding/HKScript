package com.lifedrained.script;



import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import org.openqa.selenium.support.ui.FluentWait;


import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static com.lifedrained.libs.ElementsConsts.*;
import static com.lifedrained.libs.ThreadManager.*;
import static com.lifedrained.libs.Singletone.log;
public class EnergyRefiler extends  Thread {
    private ChromeDriver driver;
    private final ScriptLauncher instance;
    private  static  final Logger logger = log;
    public EnergyRefiler( ChromeDriver driver, ScriptLauncher instance) {

        this.driver = driver;
        this.instance = instance;
        start();

    }



    @Override
    public void run() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        FluentWait<ChromeDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofHours(1))
                .ignoring(StaleElementReferenceException.class)
                .pollingEvery(Duration.ofSeconds(5));
        wait.until(new ExpectedCondition<Object>() {
            @Override
            public Object apply(WebDriver input) {
                try {

                    WebElement boostBtn = driver.findElement(By.className(BOOST_BTN_CLASS));
                    try {
                        boostBtn.click();
                    }catch (ElementClickInterceptedException ex){
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollBy(0, 1000);");
                        boostBtn.click();
                    }
                     WebElement boost = driver.findElement(By.className("boost-item"));
                        boost.click();
                        try {
                            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
                            WebElement confirmBtn = driver.findElement(By.cssSelector(CONFIRM_BUTTON_CSS));
                            confirmBtn.click();
                        }catch (NoSuchElementException ex){
                            log.info("seems like boost on cooldown");
                            returnToMain();

                            return true;
                        }


                    returnToMain();
                    ScriptLauncher mainScriptThread  = new ScriptLauncher(driver){{
                        setName(MAIN_SCRIPT_NAME);
                    }};

                    threadManager.setScriptThread(mainScriptThread);
                    threadManager.addThread(mainScriptThread);
                    return true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        });
        try {
            sleep(Long.MAX_VALUE);
        } catch (InterruptedException ignore) {

        }
    }
    private void returnToMain() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        WebElement bar = driver.findElement(By.className("app-bar-nav"));
        List<WebElement> barItems = bar.findElements(By.tagName("a"));
        barItems.removeIf(new Predicate<WebElement>() {
            @Override
            public boolean test(WebElement webElement) {
                return !webElement.getAttribute("href").endsWith("clicker");
            }
        });
        try {
            barItems.get(0).click();
        }catch (ElementClickInterceptedException ignore){

        }
    }
}

