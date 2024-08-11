package com.lifedrained.libs;

public class Singletone {
    public static Singletone INSTANCE;
    private ThreadExt scriptThread;
    private Singletone() {

    }
    public static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singletone();
        }else{
         throw new RuntimeException("Instance already created");
        }
    }

    public void setScriptThread(ThreadExt scriptThread) {
        this.scriptThread = scriptThread;
    }
    public boolean isClickerRunning() {
        return scriptThread.isClickerRunning();
    }
}
