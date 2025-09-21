package com.freeautoproxy.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Structure to represent info about URL-page when possible to find list of proxies
 * and how to parse it.
 * <p>
 * By default, <a href="https://en.wikipedia.org/wiki/Regular_expression">RegExp</a> 
 * pattern to parse page represented as 
 * <code>([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\s*:\\s*([0-9]+)</code>,
 * that mean all proxies in HTML code of page must be in format IP:PORT 
 * as 52.119.20.14:80 or 138.68.242.184:3128. It is a simpliest form of parsing, and
 * only not many sites represent his lists of proxies in such format.
 * <p>
 * You can expand your search pages if will use custom parsing rules. 
 * <p>
 * For example, any page save HTML-code with proxy in items like  
 * <code>&lt;td&gt;52.119.20.14&lt;/td&gt;&lt;td&gt;80&lt;/td&gt;</code>. You can parse it
 * with next RegExp: 
 * <p>
 * <code>&lt;td&gt;([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}
 * \\.[0-9]{1,3})&lt;/td&gt;\\s*&lt;td&gt;([0-9]+)&lt;/td&gt;</code>
 * <p>
 * <b>NOTE</b>, when you add new URL-page and RegExp pattern for parse it, 
 * is highly recommend on development stage to check once with InfoUrl.test() 
 * method to know whether this pattern work right or not. In the future this check is not need.
 * 
 * @see <a href="https://sourceforge.net/p/jgetfreeproxylist/wiki/Home/">See manual</a>
 * @version 1.1
 */
public class InfoUrl {
    private java.net.URL Url;
	
	/**
	 * PregMach how to find proxy.
	 */
    private String PatternString = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\s*:\\s*([0-9]+)";
	
    public InfoUrl(java.net.URL url) {
		if (null == url) {
			throw new NullPointerException("InfoUrl has to be defined");
		}
		
		this.Url = url;
    }
    
    /**
     * Get Url of HTTP-page when possible to find free proxies.
     * 
     * @return 
     */
    public URL getUrl() {
        return this.Url;
    }

    /**
     * Get pattern string.
	 * By default represented as 
     * <code>([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\s*:\\s*([0-9]+)</code>
     * 
     * @return 
     */
    public String getPatternString() {
        return this.PatternString;
    }

    /**
     * Set RegExp pattern to parse page and get ProxyItem
     * 
     * @param PatternString - RegExp for parser
     * @exception java.util.regex.PatternSyntaxException - when set up wrong pattern string
     */
    public void setPatternString(String PatternString) {
        // Check whether PatternString is correct
        Pattern pattern = Pattern.compile(PatternString);
        
        this.PatternString = PatternString;
    }
    
    /**
     * To test how pattern will work and how it will be parse a page.
     * <p>
     * When you add custom PatternString, on the stage of test
     * you have to try yours RegExp on current Url.
     * This method will connect to Url and try to parse page 
     * for create ProxyItem and return ArrayList of them.
     * <p>
     * Be aware, if returned ArrayList is empty, that means 
     * yours RegExp setted by <code>setPatternString(String PatternString)</code>
     * <b>is wrong</b> and you must try other RegExp.
     * <p>
     * This method exists for refuse senseless work when will launch run() method.
     * <p>
     * Must not use in release stage, usually used to once when invented RegExp.
     * 
     * @return - list of found proxies
     */
    public ArrayList<ProxyItem> test() throws MalformedURLException, IOException {
        ArrayList<ProxyItem> list = new ArrayList<>();
        
        URLConnection connection = new URL( this.getUrl().toString() ).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("User-Agent", Settings.getUserAgent());
        connection.setReadTimeout((Settings.getUrlConnectionTimeOut()*1000));
        InputStream response = connection.getInputStream();

        String result = "";
        try (Scanner scanner = new Scanner(response)) {
            result += scanner.useDelimiter("\\a").next();
        }
        
        int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;			
        Pattern pattern = Pattern.compile(this.getPatternString(), flags);
        Matcher m = pattern.matcher(result);

        while(m.find()) {
            if (!m.group(1).isEmpty() && !m.group(2).isEmpty()) {
                ProxyItem pi = new ProxyItem(
                    InetAddress.getByName(m.group(1)), Integer.decode(m.group(2))
                );
                if (!list.contains(pi)) list.add(pi);
            }
        }
        
        return list;
    }
    
    @Override
    public String toString(){
        return this.Url.getHost();
    }
}
