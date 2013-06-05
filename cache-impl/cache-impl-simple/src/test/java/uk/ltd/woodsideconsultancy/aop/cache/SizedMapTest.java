/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ltd.woodsideconsultancy.aop.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.SizedMap;
import uk.ltd.woodsideconsultancy.aop.cache.TimeStampedEntry;
import static org.junit.Assert.*;

/**
 *
 * @author Gary Bennett
 */
public class SizedMapTest {
    
    public SizedMapTest() {
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
     * Test of setMaxEntries method, of class SizedMap.
     */
    @Test
    public void testSetMaxEntries() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("setMaxEntries");
        int maxEntries = 0;
        SizedMap instance = new SizedMap();
        instance.setMaxEntries(maxEntries);
        Field f = SizedMap.class.getDeclaredField("maxEntries");
        f.setAccessible(true);
        assertEquals(0, f.getInt(instance));
    }

    /**
     * Test of setMaxDuration method, of class SizedMap.
     */
    @Test
    public void testSetMaxDuration() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("setMaxDuration");
        long maxDuration = 0L;
        SizedMap instance = new SizedMap();
        instance.setMaxDuration(maxDuration);
        Field f = SizedMap.class.getDeclaredField("maxDuration");
        f.setAccessible(true);
        assertEquals(0L, f.getLong(instance));
    }

    /**
     * Test of removeEldestEntry method, of class SizedMap.
     */
    @Test
    public void testRemoveEldestEntry() {
        System.out.println("removeEldestEntry");
        Entry eldest = null;
        SizedMap instance = new SizedMap();
        boolean expResult = false;
        // test remove entries from empty map
        boolean result = instance.removeEldestEntry(eldest);
        assertEquals(expResult, result);

        instance.setMaxEntries(3);
        instance.setMaxDuration(3);
        // test remove entries beyond size
        for(int i = 0; i < 4; i++){
            TimeStampedEntry<String> tse = new TimeStampedEntry<String>(""+i);
            List<Integer> list = new ArrayList<Integer>();
            list.add(i);
            instance.put(list, tse);
        }
        assertEquals(3,instance.size());
        // test remove old entries
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SizedMapTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        TimeStampedEntry<String> tse = (TimeStampedEntry<String>)instance.get(list);
        assertNull(tse);
        
        
    }
}
