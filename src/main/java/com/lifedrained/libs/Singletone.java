package com.lifedrained.libs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Singletone {
    public static Singletone INSTANCE;
    public static final APP_VERSION version = APP_VERSION.CLI;
    public static final Logger log = Logger.getLogger("main");

    public static int tapForce = 0;
    public static boolean isRefilerOnCooldown = false;

    private Singletone() {

    }
    public static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singletone();
            try {
                FileHandler handler = new FileHandler("log.txt" ,false);
                handler.setFormatter(new SimpleFormatter());
                log.addHandler(handler);
                log.setLevel(Level.ALL);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
         throw new RuntimeException("Instance already created");
        }
    }

}
