package com.freeautoproxy.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Settings {

	private String appVersion = "1.1";

    private ArrayList<java.net.URL> testByUrls = new ArrayList<>();

    private ArrayList<InfoUrl> proxyUrls = new ArrayList<>();

    private int amountThreads = 50;

	private int awaitGetProxy = 20;
	
	private int awaitTestProxy = 600;

    private int capacityProxiesQueue = 50;

    private TimeZone timeZone = Calendar.getInstance().getTimeZone();

    private int urlConnectionTimeOut = 5;

    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    private boolean enableDebug = false;
	
	public void init() {
		Properties prop = new Properties();
		FileInputStream fileInputStream = null;

		try {

			fileInputStream = new FileInputStream("config.properties");
			prop.load(fileInputStream);

			appVersion = prop.getProperty("core.appVersion");

			try {
				List<String> t1 = Arrays.stream(prop.getProperty("core.testByUrls").split(";")).toList();
				testByUrls.clear();
				for (String i : t1) {
					testByUrls.add(new URL(i));
				}

				List<String> t2 = Arrays.stream(prop.getProperty("core.proxyUrls").split(";")).toList();
				proxyUrls.clear();
                for (String i : t2) {
					proxyUrls.add(new InfoUrl(new URL(i)));
                }
			}
			catch(MalformedURLException e) {
				// With that urls it is impossible
			}

			amountThreads = Integer.getInteger(prop.getProperty("core.amountThreads"));
			awaitGetProxy = Integer.getInteger(prop.getProperty("core.awaitGetProxy"));
			awaitTestProxy = Integer.getInteger(prop.getProperty("core.awaitTestProxy"));
			capacityProxiesQueue = Integer.getInteger(prop.getProperty("core.capacityProxiesQueue"));
			urlConnectionTimeOut = Integer.getInteger(prop.getProperty("core.urlConnectionTimeOut"));
			userAgent = prop.getProperty("core.userAgent");
			enableDebug = Boolean.getBoolean(prop.getProperty("core.enableDebug"));

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

	}
	
	public String getAppVersion() {
		return appVersion;
	}

	public ArrayList<URL> getTestByUrls() {
		return testByUrls;
	}

	public ArrayList<InfoUrl> getProxyUrls() {
		return proxyUrls;
	}

	public int getAmountThreads() {
		return amountThreads;
	}

	public int getAwaitGetProxy() {
		return awaitGetProxy;
	}

	public int getAwaitTestProxy() {
		return awaitTestProxy;
	}

	public int getCapacityProxiesQueue() {
		return capacityProxiesQueue;
	}

	public java.util.TimeZone getTimeZone() {
		return timeZone;
	}

	public int getUrlConnectionTimeOut() {
		return urlConnectionTimeOut;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public boolean isEnableDebug() {
		return enableDebug;
	}
	
}
