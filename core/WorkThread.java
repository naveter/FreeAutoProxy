/*
 * jGetFreeProxyList - to get a list of tested free proxies to java program
 * 
 * Permission is granted to copy, distribute and/or
 * modify  this  document under  the  terms  of the
 * GNU General Public License
 * 
 * @author: ilya.gulevskiy
 * @email: mstorage.project@gmail.com
 * @date: 2017
 */
package jGetFreeProxyList;

import jGetFreeProxyList.jGetFreeProxyList;

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
