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
package jGetFreeProxyList;

import jGetFreeProxyList.jGetFreeProxyList;
import java.util.Map;
import java.util.Set;



/**
 * Filler into queue, it will add tasks to queue as it will possible.
 * 
 * @version 1.1
 */
class QueueProducer extends WorkThread {
    
	@Override
    public void run() {
        
        Dev.out("QueueProducer started");
        
        for(ProxyItem pi : this.Main.RawProxies.values()){
            
            // If stop() is called
            if (true == this.Main.IsStopped.get()) break;
            
            try {
                this.Main.ProxiesQueue.put(pi);
            }
            catch(InterruptedException e) {
                this.Main.WorkErrors.get().Errors.add(
                    this.getClass().getName() + "; " + e.getMessage()
                );                            
            }
        }
        
        Dev.out("QueueProducer stopped");
        
    }
    
    public QueueProducer(jGetFreeProxyList parent) {
		super(parent);
    }
}
