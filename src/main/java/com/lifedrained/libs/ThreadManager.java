package com.lifedrained.libs;

import com.lifedrained.script.ScriptLauncher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ThreadManager {
    public static final String MAIN_SCRIPT_NAME = "Main Script", COOLDOWN_THREAD_NAME = "Cooldown Timer";
    public static ThreadManager threadManager;
    private ScriptLauncher scriptThread;
    private KeepAwakeThread keepAwakeThread;
    private static ArrayList<Thread> threads = new ArrayList<>();

    private ThreadManager(){

    }
    public static void createInstance(){
        if(threadManager == null){
            threadManager = new ThreadManager();
        }

    }
    public void setScriptThread(ScriptLauncher scriptThread) {
        this.scriptThread = scriptThread;
    }
    public void addThread(Thread thread) {
        threads.forEach(new Consumer<Thread>() {
            @Override
            public void accept(Thread element) {
                if (thread.getName().equals(element.getName()) && thread.getName().equals(MAIN_SCRIPT_NAME) &&
                element.isAlive()) {
                    throw new RuntimeException("Main script thread already exists");
                }
                if (thread.getName().equals(element.getName())&& thread.getName().equals(COOLDOWN_THREAD_NAME)
                && element.isAlive()) {
                    throw new RuntimeException("Cooldown timer thread already exists");
                }
                if(element.isAlive() && thread.getName().equals("KeepAwakeThread") && thread.getName().equals(element.getName())){
                    throw new RuntimeException("KeepAwake thread already exists");
                }
            }
        });

        threads.add(thread);
    }
    public void removeThread(Thread thread) {
        terminateThread(thread);
        threads.remove(thread);

    }
    public void terminateThreads(){
        threads.forEach(new Consumer<Thread>() {
            @Override
            public void accept(Thread thread) {
                terminateThread(thread);
                threads.remove(thread);

            }
        });
    }
    public void removeThread(String name){
        threads.forEach(new Consumer<Thread>() {
            @Override
            public void accept(Thread thread) {
                if (thread.getName().equals(name)) {
                    terminateThread(thread);
                    threads.remove(thread);

                }
            }

        });
    }
    public ScriptLauncher getScriptThread() {
        return scriptThread;
    }
    private void terminateThread(Thread thread){
        if(!thread.isInterrupted()){
            if (thread.isAlive()){
                thread.interrupt();
            }
        }
    }
    public KeepAwakeThread getKeepAwakeThread() {
        return keepAwakeThread;
    }
    public void setKeepAwakeThread(KeepAwakeThread keepAwakeThread) {
        this.keepAwakeThread = keepAwakeThread;
    }
}
