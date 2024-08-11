package com.lifedrained;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class ScriptLauncher implements Runnable{
    private String energyText, currentEnergy,maxEnergy;
    private  WebDriver driver;
    private  WebElement container, energy,button;
    private int tapForce;
    public static boolean isClickerRunning = false;
    private static final String targetUrl = "https://hamsterkombatgame.io/ru/clicker",
            clientUrl="https://hamsterkombatgame.io/clicker/#tgWebAppData=query_id%3DAAETJ0tXAgAAABMnS1emknCZ%26user%3D%257B%2522id%2522%253A5759510291%252C%2522first_name%2522%253A%2522Drained%2522%252C%2522last_name%2522%253A%2522%2522%252C%2522username%2522%253A%2522Life_Drained%2522%252C%2522language_code%2522%253A%2522ru%2522%252C%2522allows_write_to_pm%2522%253Atrue%257D%26auth_date%3D1723026271%26hash%3D4f0606a48cafd0f793af3ef453df3539496ec20e1d7a2f4c0f988d754e17d42c&tgWebAppVersion=7.6&tgWebAppPlatform=android&tgWebAppThemeParams=%7B%22bg_color%22%3A%22%23ffffff%22%2C%22text_color%22%3A%22%23000000%22%2C%22hint_color%22%3A%22%23707579%22%2C%22link_color%22%3A%22%233390ec%22%2C%22button_color%22%3A%22%233390ec%22%2C%22button_text_color%22%3A%22%23ffffff%22%2C%22secondary_bg_color%22%3A%22%23f4f4f5%22%2C%22header_bg_color%22%3A%22%23ffffff%22%2C%22accent_text_color%22%3A%22%233390ec%22%2C%22section_bg_color%22%3A%22%23ffffff%22%2C%22section_header_text_color%22%3A%22%23707579%22%2C%22subtitle_text_color%22%3A%22%23707579%22%2C%22destructive_text_color%22%3A%22%23e53935%22%7D";

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
        final boolean[] isSleepin = {true};
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofDays(365 * 100))
                .ignoring(NoSuchElementException.class).pollingEvery(Duration.ofSeconds(3))
                .ignoring(StaleElementReferenceException.class);
        wait.until(new ExpectedCondition<Object>() {

            @Override
            public Object apply(WebDriver input) {

                if(driver.getCurrentUrl().equals(targetUrl)){
                    getEnergyValues();
                    if (currentEnergy.equals(maxEnergy)) {
                        performClicks();

                        return true;
                    } else {

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
                startWaitForEnergy();
            }
        }

    }
    public boolean isClickerRunning(){
        return isClickerRunning;
    }
}
