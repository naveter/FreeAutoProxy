package com.freeautoproxy.core;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use in pool of threads to find proxies from concrete url
 * 
 * @version 1.1
 */
class GetProxy extends WorkThread {
    
    protected InfoUrl InfoUrl;
	
	@Override
    public void run() {
        int cnt = 0;
                
        try {
            URLConnection connection = new URL( this.InfoUrl.getUrl().toString() ).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("User-Agent", Settings.getUserAgent());
            connection.setReadTimeout((Settings.getUrlConnectionTimeOut()*1000));
            InputStream response = connection.getInputStream();

            String result = "";
            try (Scanner scanner = new Scanner(response)) {
                result += scanner.useDelimiter("\\a").next();
            }

            int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;			
            Pattern pattern = Pattern.compile(this.InfoUrl.getPatternString(), flags);
            Matcher m = pattern.matcher(result);
            
            while(m.find()) {
                if (!m.group(1).isEmpty() && !m.group(2).isEmpty()) {
                    ProxyItem pi = new ProxyItem(
                        InetAddress.getByName(m.group(1)), Integer.decode(m.group(2))
                    );
                    this.Main.RawProxies.putIfAbsent(pi.toString(), pi );
                    cnt++;
                }
            }
        }
        catch(Exception e) {
            this.Main.WorkErrors.get().Errors.add(
                this.getClass().getName() + "; " + e.getMessage() + "; " + this.InfoUrl.toString()
            );
        }
        
        this.Main.GetProxyCounter.incrementAndGet();
        
        Dev.out("GetProxy stopped: " + this.Main.GetProxyCounter.get());
        
        // No proxies was found
        if(0 == cnt) {
            this.Main.WorkErrors.get().WithoutProxies.add(this.InfoUrl);
        }
        
    }
    
    public GetProxy(jGetFreeProxyList parent, InfoUrl url) {
		super(parent);
        
        if (null == url){
            throw new NullPointerException("InfoUrl can not be null");
        }
        
        this.InfoUrl = url;
    }
	
}
