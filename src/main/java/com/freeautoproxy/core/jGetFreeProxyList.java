package com.freeautoproxy.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * jGetFreeProxyList - library to get a list of tested free proxies to java code.
 * <p>
 * This Java library help when your app need a list of able to work proxies. 
 * Just add jGetFreeProxyList to your progect, set up settings if you want and run it. 
 * According to settings, after 5 or more seconds you get list of tested free proxies.
 * <p>
 * For example, your app have to send a lot of HTTP queries for some hosts, 
 * but you dont want do it with one IP address to refuse yours ban by IP. 
 * You import jGetFreeProxyList into your app, run it in other thread, 
 * set up settings and set handlers. Afterwards jGetFreeProxyList receives list 
 * of free proxies from sites you set up in settings, make test for every IP 
 * and give you list of tested proxies into yours java code. Amount of threads 
 * and timeouts can be set up too.
 * <p>
 * 
 * How to use:
 * <pre><code>
 *jGetFreeProxyList jGetFreeProxyList = new jGetFreeProxyList(
 *	new jGetFreeProxyListListener(){
 * 
 *		// Method will cal every second and give a consumer percentage of work
 *		public void process(int getProxyPerc, int testProxyPerc){
 *			
 *		}
 * 
 *		// Will call when all work is done. 
 *		public void done(ArrayList&lt;ProxyItem&gt; testedProxies, WorkErrors errors){
 *			
 *		}
 *	}
 *);
 *
 *try {
 * 
 *  // There you can adjust settings, see manual.
 *  // By default jGetFreeProxyList has common settings for correct work.
 *  // But is better to use your own.
 * 
 *  // Run process. But is better to run it in diffrent thread.
 *  jGetFreeProxyList.run();
 * 
 *}
 *catch(Exception e) {
 * 
 *  // Have to call to stop other processes if exception is occured
 *  jGetFreeProxyList.shutdown();
 * 
 * }
 * </code></pre>
 *
 * @see <a href="https://sourceforge.net/p/jgetfreeproxylist/wiki/Home/">See documentation</a>
 * @version 1.1
 */
public final class jGetFreeProxyList {
	
	/** Queue for threads to test proxies */
    ArrayBlockingQueue<ProxyItem> ProxiesQueue = new ArrayBlockingQueue(Settings.getCapacityProxiesQueue());
	
	/** Map of unique non tested proxies */
    ConcurrentHashMap<String, ProxyItem> RawProxies = new ConcurrentHashMap<>();
	
	/** Listener for cunsumer's communication */
    jGetFreeProxyListListener jGetFreeProxyListListener;
	
	/** Counter of ended GetProxy threads  */
    AtomicInteger GetProxyCounter = new AtomicInteger(0);
    
    /** Counter of ended tests of proxy in TestProxy */
    AtomicInteger TestProxyCounter = new AtomicInteger(0);
    
    /** Whether all process must be stopped or not */
    AtomicBoolean IsStopped = new AtomicBoolean(false);
	
	/** Proxies that tested already */
    CopyOnWriteArrayList<ProxyItem> TestedProxies = new CopyOnWriteArrayList<>();
    
    /** Collection of errors */
    AtomicReference<WorkErrors> WorkErrors = new AtomicReference<>(new WorkErrors());
	
	// Executors for threads
	ExecutorService ExStateControl;
	ExecutorService ExGetProxy;
	ExecutorService ExTestProxy;
	ExecutorService ExQueueProducer;
		
	public jGetFreeProxyList(jGetFreeProxyListListener listener){
		if (null == listener) {
			throw new NullPointerException("jGetFreeProxyListListener has to be defined");
		}
		
		this.jGetFreeProxyListListener = listener;
	}
	
	/**
	 * Run work processes
	 * 
	 * @throws RuntimeException - if no proxies were found
	 * @throws InterruptedException - from Executors
	 */
    public void run() throws InterruptedException {
        // Enable-disable development tool
        Dev.setEnableDebug(Settings.isEnableDebug());
        
		this.init();
		
		// Start StateControl
		this.ExStateControl = Executors.newSingleThreadExecutor();
		Future<?> futureExStateControl = this.ExStateControl.submit(new StateControl(this));
        		
		// Starting threads to get proxies 
		int cntGetProxyUrls = Settings.getProxyUrls().size();
		this.ExGetProxy = Executors.newFixedThreadPool(cntGetProxyUrls);
		
        for(int i = 0; i < cntGetProxyUrls; i++){
            this.ExGetProxy.submit(new GetProxy(this, Settings.getProxyUrls().get(i)));
        }

		// Await until all GetProxy threads will ended
		this.ExGetProxy.shutdown();
        this.ExGetProxy.awaitTermination(Settings.getAwaitGetProxy(), TimeUnit.SECONDS);
		
		// If there is alaliable proxies to test
		if (0 == this.RawProxies.size()) {
			this.ExStateControl.shutdown();
			this.jGetFreeProxyListListener.process(0, 0);
			this.jGetFreeProxyListListener.done(new ArrayList<ProxyItem>(),this.WorkErrors.get().get());
			
			throw new RuntimeException("There is no proxies were found for test");
		}
        
        Dev.out("Received proxies: " + this.RawProxies.size());
		
		// Start QueueProducer
		this.ExQueueProducer = Executors.newSingleThreadExecutor();
		Future<?> futureExQueueProducer = this.ExQueueProducer.submit(new QueueProducer(this));
				
		// Starting threads to test proxies 
		this.ExTestProxy = Executors.newFixedThreadPool(Settings.getAmountThreads());
        for(int i = 0; i < Settings.getAmountThreads(); i++){
            this.ExTestProxy.submit(new TestProxy(this));
        }
		
        this.ExTestProxy.shutdown();
		this.ExQueueProducer.shutdown();
		
        Dev.out("Before awaitTermination");
                
		// Await until all TestProxy threads and QueueProducer will ended
		this.ExTestProxy.awaitTermination(Settings.getAwaitTestProxy(), TimeUnit.SECONDS);
		this.ExQueueProducer.awaitTermination(Settings.getAwaitTestProxy(), TimeUnit.SECONDS);
				
		// Stop StateControl
		this.ExStateControl.shutdown();

		this.jGetFreeProxyListListener.process(100, 100);

		// Sort by RespondMilliSeconds
		ArrayList<ProxyItem> sortedProxies = new ArrayList<ProxyItem>(this.TestedProxies);
		Collections.sort(sortedProxies, new ProxyItemComparator());

		this.jGetFreeProxyListListener.done(sortedProxies, this.WorkErrors.get().get());
        
        Dev.out("-------- Run is finished -------------");
		
    }
    
    /**
     * Stop all launched process and run() will release as soon, as all threads will stopped.
     * Call this method in the same thread where is launched run() is senseless, obviously.
     * Please, call it in different thread than run().
     */
    public void stop(){
        this.IsStopped.set(true);
        
        // To stop QueueProducer thread
        this.ProxiesQueue.clear();
    }
    
    /**
     * Emergency stop to work when exception is occured.
     * Must use in <code>catch</code> block. 
     */
    public void shutdown(){
        this.stop();
        
		// Stop StateControl
		this.ExStateControl.shutdown();

		this.jGetFreeProxyListListener.done(
            new ArrayList<ProxyItem>(this.TestedProxies), this.WorkErrors.get().get()
        );
        
        Dev.out("-------- shutdown is finished -------------");
    }
    
	/**
	 * Checking of settings data and prepare to work.
	 * 
	 * @throws RuntimeException - if settings is not correct
	 */
	private void init() throws RuntimeException{
		if(0 == Settings.getProxyUrls().size()) {
			throw new RuntimeException("GetProxyList has to be defined");
		}
		if(0 == Settings.getTestByUrls().size()) {
			throw new RuntimeException("TestByUrls has to be defined");
		}
		if (Settings.getAmountThreads() <= 0) {
			throw new RuntimeException("AmountThreads has to be a positive number");
		}
        
        // Reset all storages
        this.ProxiesQueue.clear();
        this.RawProxies.clear();
        this.GetProxyCounter.set(0);
        this.TestProxyCounter.set(0);
        this.IsStopped.set(false);
        this.TestedProxies.clear();
        this.WorkErrors.set(new WorkErrors());
	}
	
// You can uncomment main() method and try to run this library as application
    
//	public static void main(String[] args) {
//      Settings.EnableDebug = true;    
//		final jGetFreeProxyList jGetFreeProxyList = new jGetFreeProxyList(
//			new jGetFreeProxyListListener(){
//				@Override
//				public void process(int getProxyPerc, int testProxyPerc){
//                    System.out.println(".process():" + getProxyPerc + ":" + testProxyPerc);
//				}
//				@Override
//				public void done(ArrayList<ProxyItem> testedProxies, WorkErrors errors){
//					String str = "";
//					for(ProxyItem s: testedProxies) str += s.toString();
//                    System.out.println(".done(): " + str);
//                    
//                    if (null != errors) {
//                        // If there were any pages without proxies
//                        if (!errors.WithoutProxies.isEmpty()){
//                            System.out.println(".errors.WithoutProxies: ");
//                            for(InfoUrl s: errors.WithoutProxies) System.out.println(s.toString());
//                        }
//                        
//                        // Print all errors of connections to tested proxies
//                        if (!errors.Errors.isEmpty()){
//                            System.out.println(".errors.Errors: ");
//                            for(String s: errors.Errors) System.out.println(s);
//                        }
//                    }
//                   
//				}
//			}
//		);
//        
//        Dev.out("Program is started");
//		
//		try {
//            // Execute work in other thread
//            ExecutorService es = Executors.newSingleThreadExecutor();
//            es.submit(new Runnable(){
//                @Override
//                public void run(){
//                    try {
//                        // Run work processes
//                        jGetFreeProxyList.run();
//                    }
//                    catch(Exception e) {
//                        // Have to call to stop other processes
//                        jGetFreeProxyList.shutdown();
//                        
//                        System.out.println(e.getMessage());
//                    }
//                }
//            });
//            
////            // Stop work if something happen
////            if (true /**something happen**/){
////                jGetFreeProxyList.stop();
////            }
//            
//            es.shutdown();
//            es.awaitTermination(Settings.AwaitTestProxy, TimeUnit.SECONDS);
//            
//		}
//		catch(InterruptedException e) {            
//			System.out.println(e.getMessage());
//		}
//        
//        Dev.out("Program is stopped");
//		
//	}
    
    /**
     * Calc percentage from given max and current value
     * 
     * @param max
     * @param now
     * @return 
     */
    static int calcPercentage(int max, int now){
        max = Math.abs(max);
        now = Math.abs(now);
        
		if (0 == max) return 0;
        if (now >= max) return 100;
        
        // mean max was multiply to 100
        max = max*100;
        now = now*100;
        int onePercent = max/100;
        int nowPercent = (now / onePercent);
        
        return nowPercent;
    }
  
}

