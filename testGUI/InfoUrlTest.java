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

import java.net.URL;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bass
 */
public class InfoUrlTest {
    
    public InfoUrlTest() {
    }


    @Test
    public void testTest() {        
        try {
            InfoUrl iu1 = new InfoUrl(new URL("https://www.us-proxy.org/"));
            String p1 = "<td>([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})</td>\\s*<td>([0-9]+)</td>";
            iu1.setPatternString(p1);
            ArrayList<ProxyItem> l1 = iu1.test();
            
            System.out.println(l1.toString());
                        
        }
        catch (Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
        
        
    }

    
}
