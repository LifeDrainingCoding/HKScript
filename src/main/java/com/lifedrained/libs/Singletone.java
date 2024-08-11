package com.lifedrained;

public class Singletone {
    public static Singletone INSTANCE;
    private ThreadExt scriptThread;
    private Singletone() {

    }
    public static void createInstance() {
        INSTANCE = new Singletone();
    }
    
}
