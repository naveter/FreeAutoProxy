package com.freeautoproxy.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Settings {

	private static String appVersion = "1.1";

    private static ArrayList<java.net.URL> testByUrls = new ArrayList<>();

    private static ArrayList<InfoUrl> proxyUrls = new ArrayList<>();

    private static int amountThreads = 50;

	private static int awaitGetProxy = 20;
	
	private static int awaitTestProxy = 600;

    private static int capacityProxiesQueue = 50;

    private static final TimeZone timeZone = Calendar.getInstance().getTimeZone();

    private static int urlConnectionTimeOut = 5;

    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    private static boolean enableDebug = false;
	
	static {
		Properties prop = new Properties();
		FileInputStream fileInputStream = null;

		try {

			fileInputStream = new FileInputStream("config.properties");
			prop.load(fileInputStream);

			appVersion = prop.getProperty("core.appVersion");

			try {
				List<String> t1 = Arrays.stream(prop.getProperty("core.TestByUrls").split(";")).toList();
				testByUrls.clear();
				for (String i : t1) {
					testByUrls.add(new URL(i));
				}

				List<String> t2 = Arrays.stream(prop.getProperty("core.GetProxyUrls").split(";")).toList();
				proxyUrls.clear();
                for (String i : t2) {
					proxyUrls.add(new InfoUrl(new URL(i)));
                }
			}
			catch(MalformedURLException e) {
				// With that urls it is impossible
			}

			appVersion = prop.getProperty("core.AppVersion");
			amountThreads = Integer.parseInt(prop.getProperty("core.AmountThreads"));
			awaitGetProxy = Integer.parseInt(prop.getProperty("core.AwaitGetProxy"));
			awaitTestProxy = Integer.parseInt(prop.getProperty("core.AwaitTestProxy"));
			capacityProxiesQueue = Integer.parseInt(prop.getProperty("core.CapacityProxiesQueue"));
			urlConnectionTimeOut = Integer.parseInt(prop.getProperty("core.URLConnectionTimeOut"));
			userAgent = prop.getProperty("core.UserAgent");
			enableDebug = Boolean.getBoolean(prop.getProperty("core.EnableDebug"));

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

	}
	
	public static String getAppVersion() {
		return appVersion;
	}

	public static ArrayList<URL> getTestByUrls() {
		return testByUrls;
	}

	public static ArrayList<InfoUrl> getProxyUrls() {
		return proxyUrls;
	}

	public static int getAmountThreads() {
		return amountThreads;
	}

	public static int getAwaitGetProxy() {
		return awaitGetProxy;
	}

	public static int getAwaitTestProxy() {
		return awaitTestProxy;
	}

	public static int getCapacityProxiesQueue() {
		return capacityProxiesQueue;
	}

	public static java.util.TimeZone getTimeZone() {
		return timeZone;
	}

	public static int getUrlConnectionTimeOut() {
		return urlConnectionTimeOut;
	}

	public static String getUserAgent() {
		return userAgent;
	}

	public static boolean isEnableDebug() {
		return enableDebug;
	}

}
