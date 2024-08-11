package com.lifedrained.script;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.lifedrained.libs.URLConsts.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ScriptLauncher implements Runnable{
    private String energyText, currentEnergy,maxEnergy;
    private  WebDriver driver;
    private  WebElement container, energy,button;
    private int tapForce;
    public static boolean isClickerRunning = false;

    public ScriptLauncher(int tapForce) {
        this.tapForce = tapForce;
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get(clientUrl);

    }

    @Override
    public void run() {
        getEnergyValues();
        startWaitForEnergy();
    }
    private  void getEnergyValues(){
        if(driver.getCurrentUrl().equals(targetUrl)) {
            button = driver.findElement(By.className("user-tap-button-circle"));
            container = driver.findElement(By.className("user-tap-energy"));

            energy = container.findElement(By.tagName("p"));
            energyText = energy.getText();
            energyText = energyText.replace(" ", "");
            String[] parts = energyText.split("/");
            maxEnergy = parts[1];
            currentEnergy = parts[0];

        }else{
            System.out.println(driver.getCurrentUrl());
        }
    }




    private  void startWaitForEnergy() {
        isClickerRunning = false;
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofDays(365 * 100))
                .ignoring(NoSuchElementException.class).pollingEvery(Duration.ofSeconds(3))
                .ignoring(StaleElementReferenceException.class);
        wait.until(new ExpectedCondition<Object>() {

            @Override
            public Object apply(WebDriver input) {

                if(input.getCurrentUrl().equals(targetUrl)){
                    getEnergyValues();
                    if (currentEnergy.equals(maxEnergy)) {
                        isClickerRunning = true;
                        performClicks();

                        return true;
                    } else {
                        tryToRefilEnergy(input);
                        isClickerRunning = false;
                        return false;
                    }
                }else{
                    System.out.println(driver.getCurrentUrl());
                    return false;
                }

            }
        });


    }

    private  void performClicks(){
        getEnergyValues();
        double maxTaps = (double) Integer.valueOf(maxEnergy) / tapForce;
        maxTaps = Math.ceil(maxTaps);
        for (int i = 0; i < (int) maxTaps; i++) {
            try {
                button.click();
            } catch (ElementClickInterceptedException ex) {
            }
            if (i == maxTaps - 1) {
                isClickerRunning = false;
                startWaitForEnergy();

            }
        }

    }
    public boolean isClickerRunning(){
        return isClickerRunning;
    }
    private void tryToRefilEnergy(WebDriver driver){
        driver.navigate().to(boostUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement refilBtn = driver.findElement(By.className("boost-item"));
        refilBtn.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.navigate().to(targetUrl);


    }
    public WebDriver getDriver() {
        return driver;
    }

}
