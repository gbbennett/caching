/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ltd.woodsideconsultancy.aop.cache;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.TimeStampedEntry;
import static org.junit.Assert.*;

/**
 *
 * @author Gary Bennett
 */
public class TimeStampedEntryTest {
    
    public TimeStampedEntryTest() {
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
     * Test of getObject method, of class TimeStampedEntry.
     */
    @Test
    public void testGetObject() {
        System.out.println("getObject");
        TimeStampedEntry instance = new TimeStampedEntry(null);
        Object expResult = null;
        Object result = instance.getObject();
        assertEquals(expResult, result);

        expResult = "Hello";
        instance = new TimeStampedEntry(expResult);
        result = instance.getObject();
        assertEquals(expResult, result);

    }

    /**
     * Test of getTimeStamp method, of class TimeStampedEntry.
     */
    @Test
    public void testGetTimeStamp() {
        System.out.println("getTimeStamp");
        TimeStampedEntry instance = new TimeStampedEntry(null);
        long result = instance.getTimeStamp();
        assertTrue(result>0L);
    }
}
