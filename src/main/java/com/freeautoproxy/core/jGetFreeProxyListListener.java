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
package com.freeautoproxy.core;

import java.util.ArrayList;

/**
 * Listener for communication between client and work processes.
 * 
 * @version 1.1
 */
public interface jGetFreeProxyListListener {
	
	/** 
	 * Method will cal every second and give a consumer percentage of work, 100 - is 100%.
	 * To give opportunity to make a progress bar or other information things.
	 * <p>
	 * Be aware, testProxyPerc will be zero until getProxyPerc became 100.
	 * 
	 * @param getProxyPerc - percentage of ask urls with proxies
	 * @param testProxyPerc - percentage of test proxies
	 */
    public void process(int getProxyPerc, int testProxyPerc);
    
	/**
	 * Will call when all work is done. 
	 * Give <code>ArrayList&lt;ProxyItem&gt;</code> of tested proxies.
	 * 
	 * @param testedProxies - list of tested proxies
     * @param errors - structure of errors. <code>null</code> if it was no errors, 
     * but it will almost never happens, because it consists by all responses of 
     * servers when proxy was refused. 
	 */
    public void done(ArrayList<ProxyItem> testedProxies, WorkErrors errors);
	
}
