/*
 * FreeAutoProxy - to get a list of tested free proxies to java program
 * 
 * Permission is granted to copy, distribute and/or
 * modify  this  document under  the  terms  of the
 * GNU General Public License
 * 
 * @author: ilya.gulevskiy
 * @email: mstorage.project@gmail.com
 * @date: 2025
 */
package com.freeautoproxy.core;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Technical class
 * 
 * @version 1.1
 */
class Dev {

    private Dev() {
    }
    
    /** Switch on debug regime */
    private final static AtomicBoolean EnableDebug = new AtomicBoolean(false);
    
    /** Common log for all messages */
    private static CopyOnWriteArrayList<String> Log = new CopyOnWriteArrayList<>();
    
    /** Get development regime */
    static boolean getEnableDebug() {
        return Dev.EnableDebug.get();
    }

    /** Set development regime */
    static void setEnableDebug(boolean b) {
        Dev.EnableDebug.set(b);
    }
    
    /**
     * Write message into log
     * @param s - debug message
     */
    static void out(String s) {
        Dev.Log.add(s);
        if (Dev.EnableDebug.get()) {
            System.out.println(s);
        }
    }
}
