package com.freeautoproxy.core;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Use in pool of threads to find out whether concrete proxy valid or not
 * 
 * @version 1.1
 */
class TestProxy extends WorkThread {
	
	@Override
    public void run() {
        
        // Do work while not call stop() or shutdown()
        while(true && false == this.Main.IsStopped.get()) {
            ProxyItem pi = null;
            
            try{
                pi = this.Main.ProxiesQueue.poll(100, TimeUnit.MILLISECONDS);
                if (null == pi) break;
            }
            catch(InterruptedException e){
                continue;
            }
            
            pi.LastChecked = null;
            pi.RespondMilliSeconds = 0;
            
            // If stop() is called
            if (true == this.Main.IsStopped.get()) break;
            
            try {
                
                java.util.Random randomGenerator = new java.util.Random();
                int index = randomGenerator.nextInt(Settings.getTestByUrls().size())-1;
                URL iu = Settings.getTestByUrls().get(index);
                
                long start_time = System.currentTimeMillis();

                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pi.Ip, pi.Port));
                HttpURLConnection connection =(HttpURLConnection)iu.openConnection(proxy);

                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("User-Agent", Settings.getUserAgent());
                connection.setReadTimeout((Settings.getUrlConnectionTimeOut()*1000));
                InputStream response = connection.getInputStream();

                long end_time = System.currentTimeMillis();
                long difference = end_time-start_time;
                
                pi.LastChecked = new Date();
                pi.RespondMilliSeconds = difference;
                this.Main.TestedProxies.addIfAbsent(pi);
            }
            catch(Exception e) {
                this.Main.WorkErrors.get().Errors.add(
                    this.getClass().getName() + "; " + e.getMessage() + "; " + pi.toString()
                );
            }
			
			this.Main.TestProxyCounter.getAndIncrement();
            
            Dev.out("TestProxy finished: " + pi.toString());
        }
        
        Dev.out("TestProxy thread stopped");
        
    }
    
    public TestProxy(jGetFreeProxyList parent) {
		super(parent);
    }
	
	
	
}
