package com.lifedrained;

import com.lifedrained.libs.Singletone;
import com.lifedrained.libs.ThreadExt;
import com.lifedrained.libs.URLConsts;
import com.lifedrained.script.ScriptLauncher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.Scanner;
import java.util.Set;

public class Main  {


    private static final Logger log = LogManager.getLogger(Main.class);
    private static int tapForce=0;

    public static void main(String[] args)  {
        Singletone.createInstance();
        Scanner scanner = new Scanner(System.in);
        while (tapForce == 0){
            System.out.println("enter your tap per click");
            tapForce = scanner.nextInt();
            System.out.println("Enter your authentication link");
            URLConsts.clientUrl = scanner.nextLine();
            if(tapForce != 0){
                scanner.close();
            }
            

        }


        ThreadExt[] threads =  new ThreadExt[1];

        try {

            for (int i = 0; i < 1; i++) {
                ThreadExt thread = new ThreadExt(new ScriptLauncher(tapForce));
                threads[i] = thread;
                threads[i].start();
                threads[i].setName("ScriptThread " + i);
                Singletone.INSTANCE.setScriptThread(threads[0]);
            }

            while (true){
               String window = threads[0].getDriver().getWindowHandle();
                Set<String>  windows = threads[0].getDriver().getWindowHandles();
               if(!windows.contains(window)){
                   System.out.println("Window was closed \n Window: "+window);
                   threads[0].getDriver().quit();
                   System.exit(0);
               }
            }
        }catch (WebDriverException e) {
            log.debug(e.getMessage());
            threads[0].getDriver().quit();
            System.out.println("Driver was terminated");
            System.exit(0);

        } finally{
            threads[0].getDriver().quit();
            System.exit(0);
        }

    }



}