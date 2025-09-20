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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class jGetFreeProxyListTest {
    
    public jGetFreeProxyListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calcPercentage method, of class jGetFreeProxyList.
     */
    @Test
    public void testCalcPercentage() {
        assertEquals(0, jGetFreeProxyList.calcPercentage(0, 0));
        assertEquals(0, jGetFreeProxyList.calcPercentage(57, 0));
        assertEquals(33, jGetFreeProxyList.calcPercentage(75, 25));
        assertEquals(0, jGetFreeProxyList.calcPercentage(0, 1));
        assertEquals(20, jGetFreeProxyList.calcPercentage(50, 10));
        assertEquals(50, jGetFreeProxyList.calcPercentage(38, 19));
        assertEquals(25, jGetFreeProxyList.calcPercentage(36, 9));
        assertEquals(1, jGetFreeProxyList.calcPercentage(-100, 1));
        assertEquals(1, jGetFreeProxyList.calcPercentage(100, -1));
    }
    
    @Test
    public void testGetTimezone(){
//        TimeZone tz = Calendar.getInstance().getTimeZone();
//        System.out.println(tz.getDisplayName()); // (i.e. Moscow Standard Time)
//        System.out.println(tz.getID()); // (i.e. Europe/Moscow)
//        
//        System.out.println(Settings.GetProxyUrls.get(0).Url.toString());
    }
    
}
