package com.freeautoproxy.test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bass
 */
public class GetProxyTest {
    ConcurrentHashMap<String, ProxyItem> RawProxies = new ConcurrentHashMap<>();
    InfoUrl InfoUrl;
    
    public GetProxyTest() {
    }

    @Test
    public void testRun() throws Exception {
        
        long start_time = System.currentTimeMillis();
        
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("120.52.73.97", 88));
        HttpURLConnection connection =(HttpURLConnection)new URL("http://myip.ru/").openConnection(proxy);

        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setReadTimeout((Settings.getUrlConnectionTimeOut()*1000));
        InputStream response = connection.getInputStream();
        
        String result = "";
        try (Scanner scanner = new Scanner(response)) {
            result += scanner.useDelimiter("\\a").next();
        }
        
        long end_time = System.currentTimeMillis();
        int difference = (int)(end_time-start_time);
        
        System.out.println(difference);
    }
    
}
