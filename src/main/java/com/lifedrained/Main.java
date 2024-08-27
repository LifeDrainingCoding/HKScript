package com.lifedrained;

import com.lifedrained.libs.*;
import com.lifedrained.script.ScriptLauncher;



import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import static com.lifedrained.libs.Singletone.*;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static com.lifedrained.libs.ThreadManager.*;

public class Main  {



    private static int tapForce=0;

    public static void main(String[] args) throws IOException {

        Singletone.createInstance();
        ThreadManager.createInstance();
        Scanner scanner = new Scanner(System.in);
        MACGather.getMAC();

        while (tapForce == 0){
            System.out.println("enter your tap force(amount of coins per tap)   " );
            tapForce = scanner.nextInt();
            System.out.println(" enter your auth link ");
            System.out.println("Guide , how to get your  auth game link (Save auth link  somewhere ):"+
                        "\n https://youtu.be/1eVZYCoAtYU");

            String url = scanner.next();
            Singletone.tapForce = tapForce;
            URLConsts.clientUrl = AuthLinkRefactor.changePlatform(url);
            if(tapForce != 0){
                scanner.close();
            }


        }
        scanner.close();
        launchScript();
//        Scanner in = new Scanner(System.in);
//        while (true){
//            System.out.println("Доступные команды: exit - выключает скрипт. "
//            +"\n r - перезапускает текущий скрипт"
//            +"\n t - настроить силу тапа ");
//            String command;
//            try {
//
//
//                 command = in.nextLine();
//            }catch (NoSuchElementException ex){
//                return;
//            }
//            switch (command){
//                    case "exit":
//                        ShutdownHook.createInstance(threadManager.getScriptThread().getDriver());
//                        Runtime.getRuntime().addShutdownHook(ShutdownHook.INSTANCE);
//                    System.exit(0); break;
//
//                    case "r":
//                        threadManager.getScriptThread().getDriver().quit();
//                        threadManager.terminateThreads();
//                        launchScript();
//                        break;
//                    case "t":
//                        int tapforce = in.nextInt();
//                        Singletone.tapForce = tapforce;
//                        System.out.println("Установлена сила тапа "+tapforce);
//                        break;
//
//
//            }
//
//        }
    }
    private static void launchScript(){
        ChromeDriver driver =  new ChromeDriver();
        ScriptLauncher thread = null;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                driver.quit();
            }
        }));
        try {

            thread= new ScriptLauncher(tapForce, driver);
            thread.setName("Main Script");

            threadManager.setScriptThread(thread);
            threadManager.addThread(thread);
            if (threadManager.getKeepAwakeThread()==null){

                threadManager.setKeepAwakeThread(new KeepAwakeThread(driver){{start();}});
            }




        }catch (NoSuchWindowException exception) {
            thread.getDriver().quit();
            System.out.println("Driver was terminated due Window Exception");
            System.exit(0);
        }catch (WebDriverException e) {
            thread.getDriver().quit();
            System.out.println("Driver was terminated due WebDriverException");
            launchScript();
        }
    }

}