package com.lifedrained.libs;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import static com.lifedrained.libs.Singletone.log;
public class KeepAwakeThread extends Thread{
    private ChromeDriver driver;
    private boolean isRunning = true;
    public KeepAwakeThread(ChromeDriver driver) {
        super();
        this.driver = driver;
        setName("KeepAwakeThread");
    }

    @Override
    public void run() {
        super.run();
        while(isRunning){
            if(driver == null){
                break;
            }
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("void(0);");
            try {

                Thread.sleep(Duration.ofSeconds(30).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopRunning(){
        isRunning = false;
    }
}
