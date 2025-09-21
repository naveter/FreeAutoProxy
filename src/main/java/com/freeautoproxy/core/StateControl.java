package com.freeautoproxy.core;

/**
 * Control thread to check state of work and inform consumers
 * 
 * @version 1.1
 */
class StateControl extends WorkThread {
    /** How often ask state condition in milliseconds */
	private static int askPeriod = 1000;
	
	@Override
    public void run() {
		
		// Thread will finish when main thread shutdown it
        while(!this.Main.ExStateControl.isShutdown()) {
			
            // Ask for condition every N milliseconds
            try {
                Thread.sleep(StateControl.askPeriod);
            }
            catch(InterruptedException e) {}
        
			int percGetProxy = jGetFreeProxyList.calcPercentage(
				Settings.getProxyUrls().size(), this.Main.GetProxyCounter.get()
			);
			
			int percTestProxy = jGetFreeProxyList.calcPercentage(
				this.Main.RawProxies.size(), this.Main.TestProxyCounter.get()
			);
			
			if (!this.Main.ExStateControl.isShutdown()) {
				this.Main.jGetFreeProxyListListener.process(percGetProxy, percTestProxy);
			}
        }
        
        Dev.out("StateControl is stopped");
		
    }
    
    public StateControl(jGetFreeProxyList parent) {
		super(parent);
    }
}
