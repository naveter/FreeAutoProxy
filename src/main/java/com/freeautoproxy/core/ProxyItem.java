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

import java.net.InetAddress;
import java.util.Date;

/**
 * Substance wich take care about proxy.
 * 
 * @version 1.1
 */
public class ProxyItem {
    /** IP-address of proxy */
    public InetAddress Ip;
    
    /** Port of proxy */
    public int Port;
    
	/** How much second wait answer in last test time */
    public long RespondMilliSeconds;
	
	/** When was last check */
    public Date LastChecked = null;
    
	/**
     * Constructor
	 * 
     * @param ip - IP address
     * @param port - port of host
     */
	public ProxyItem(InetAddress ip, int port){
		if (null == ip || port <= 0) {
			throw new RuntimeException("IP or host is not valid");
		}
		
		this.Ip = ip;
		this.Port = port;
	}
	
	/**
	 * Return true if LastChecked is filled
	 * @return - if checked
	 */
    public boolean isChecked() {
		return (null != this.LastChecked);
    }
    
    public String toString() {
		return this.Ip.getHostAddress() + ":" + Integer.toString(this.Port);
    }
}
