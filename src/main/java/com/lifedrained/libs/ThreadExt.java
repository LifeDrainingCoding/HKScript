package com.lifedrained.libs;

import com.lifedrained.script.ScriptLauncher;
import org.openqa.selenium.WebDriver;

public class ThreadExt extends Thread{
    private ScriptLauncher task;
    public ThreadExt(ScriptLauncher task) {
        super(task);
        this.task = task;
    }
    public boolean isClickerRunning(){
        return task.isClickerRunning();
    }

    public WebDriver getDriver() {
        return task.getDriver();
    }
}
