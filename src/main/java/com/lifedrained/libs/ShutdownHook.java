package com.lifedrained.libs;

import org.openqa.selenium.WebDriver;

public class ShutdownHook extends  Thread{
    public static ShutdownHook INSTANCE;
    public static void   createInstance(WebDriver driver) {
        if (INSTANCE == null) {
            INSTANCE = new ShutdownHook(driver);
        }
    }
    private ShutdownHook(WebDriver driver) {
        super(new Runnable() {
            @Override
            public void run() {
                driver.quit();

            }
        });
    }
}
