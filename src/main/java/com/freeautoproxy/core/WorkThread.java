package com.freeautoproxy.core;

/**
 * Abstract extension for all work threads in this lib
 * 
 * @version 1.1
 */
abstract class WorkThread implements Runnable {
	/** Instance of main thread for interthreads communication */
    protected jGetFreeProxyList Main;
	    
    public WorkThread(jGetFreeProxyList main) {
		this.Main = main;
    }
	
}
