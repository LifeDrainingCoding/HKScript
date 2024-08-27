package com.lifedrained.script;

import com.lifedrained.libs.Singletone;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;


import static com.lifedrained.libs.URLConsts.*;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import static com.lifedrained.libs.Singletone.*;
import static com.lifedrained.libs.ElementsConsts.*;
import static com.lifedrained.libs.ThreadManager.*;
public class ScriptLauncher extends Thread{
    private String energyText, currentEnergy,maxEnergy;
    private  ChromeDriver driver;
    private  WebElement container, energy,button, dialogBtn;
    private int tapForce;
    private Logger logger = log;
    private boolean isUserWarned = false;

    public  boolean isClickerRunning = false;


    public ScriptLauncher(int tapForce , ChromeDriver   driver)  {
        this.tapForce = tapForce;
        this.driver = driver;
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get(clientUrl);
        try{

            Thread.sleep(Duration.ofSeconds(1).toMillis());
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        WebElement confirmBtn = driver.findElement(By.cssSelector(CONFIRM_BUTTON_CSS));
        confirmBtn.click();
        start();
    }
    public ScriptLauncher(ChromeDriver driver)  {
        this.tapForce = Singletone.tapForce;
        this.driver = driver;
        start();
    }

    @Override
    public void run() {
        getEnergyValues();

        try {


            startWaitForEnergy();
        }catch (NoSuchWindowException ex){
            driver.quit();
            System.out.println("Driver was terminated due Window Exception in" + ScriptLauncher.class.getName());
            System.exit(0);
        }
    }
    private  void getEnergyValues(){
        if(driver.getCurrentUrl().startsWith(clickerStart) && driver.getCurrentUrl().endsWith(clickerEnd)) {
            button = driver.findElement(By.className("user-tap-button-circle"));
            container = driver.findElement(By.className("user-tap-energy"));

            energy = container.findElement(By.tagName("p"));
            energyText = energy.getText();
            energyText = energyText.replace(" ", "");
            String[] parts = energyText.split("/");
            maxEnergy = parts[1];
            currentEnergy = parts[0];

        }else{
            if(!isUserWarned){
                isUserWarned = true;
                System.out.println("You are not on the clicker page. Clicker won't work.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimerTask task = new TimerTask() {
                            public void run() {
                                isUserWarned = false;
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, Duration.ofMinutes(10).toMillis());
                    }
                });
            }
        }
    }




    public void startWaitForEnergy() throws NoSuchWindowException {
        isClickerRunning = false;
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofDays(365 * 100))
                .ignoring(NoSuchElementException.class).pollingEvery(Duration.ofSeconds(1))
                .ignoring(StaleElementReferenceException.class);
        wait.until(new ExpectedCondition<Object>() {

            @Override
            public Object apply(WebDriver input) {

                if(input.getCurrentUrl().startsWith(clickerStart) && input.getCurrentUrl().endsWith(clickerEnd)){
                    getEnergyValues();
                    if (currentEnergy.equals(maxEnergy)) {
                        isClickerRunning = true;
                        performClicks();

                        return true;
                    } else {
                        if(!isRefilerOnCooldown){
                            startEnergyRefiler();
                        }
                        isClickerRunning = false;
                        return false;
                    }
                }else{
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

    private void startEnergyRefiler (){
        if (!isRefilerOnCooldown){
            EnergyRefiler thread = null;
            try {

                 thread = new EnergyRefiler(driver,this);
                thread.setName("EnergyRefiler");
            }catch (NoSuchElementException ex){

            }
            threadManager.addThread(thread);
           isRefilerOnCooldown = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TimerTask cooldownTask =  new TimerTask() {
                        @Override
                        public void run() {
                            isRefilerOnCooldown = false;
                        }
                    };
                    Timer timer =  new Timer();
                    timer.schedule(cooldownTask, Duration.ofHours(1).toMillis());
                }

            }){{
                setName(COOLDOWN_THREAD_NAME);
                threadManager.addThread(this);
            }}.start();
        }

    }
    public boolean isClickerRunning(){
        return isClickerRunning;
    }

    public WebDriver getDriver() {
        return driver;
    }

}



