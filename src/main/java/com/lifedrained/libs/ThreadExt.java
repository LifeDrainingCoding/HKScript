package com.lifedrained;

public class ThreadExt extends Thread{
    private ScriptLauncher task;
    public ThreadExt(ScriptLauncher task) {
        super(task);
        this.task = task;
    }
    public boolean isClickerRunning(){
        return task.isClickerRunning();
    }
}
