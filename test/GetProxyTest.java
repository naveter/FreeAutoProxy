/*
 * jGetFreeProxyList - to get a list of tested free proxies to java program
 * 
 * Permission is granted to copy, distribute and/or
 * modify  this  document under  the  terms  of the
 * GNU General Public License
 * 
 * @author: ilya.gulevskiy
 * @email: mstorage.project@gmail.com
 * @date: 2016
 */
package jGetFreeProxyList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        connection.setReadTimeout((Settings.URLConnectionTimeOut*1000));
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
