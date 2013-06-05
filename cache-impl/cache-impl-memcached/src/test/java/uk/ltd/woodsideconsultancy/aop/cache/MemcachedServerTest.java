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

import uk.ltd.woodsideconsultancy.aop.cache.MemcachedServer;
import static org.junit.Assert.*;

/**
 *
 * @author gary bennett
 */
public class MemcachedServerTest {
    
    public MemcachedServerTest() {
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
     * Test of getServer method, of class MemcachedServer.
     */
    @Test
    public void testGetServer() {
        System.out.println("getServer");
        MemcachedServer instance = new MemcachedServer();
        String expResult = null;
        String result = instance.getServer();
        assertEquals(expResult, result);

        expResult = "abc";
        instance.setServer(expResult);
        assertEquals(expResult, instance.getServer());
    }

    /**
     * Test of setServer method, of class MemcachedServer.
     */
    @Test
    public void testSetServer() {
        System.out.println("setServer");
        MemcachedServer instance = new MemcachedServer();
        String expResult = null;
        String result = instance.getServer();
        assertEquals(expResult, result);

        expResult = "abc";
        instance.setServer(expResult);
        assertEquals(expResult, instance.getServer());
    }

    /**
     * Test of getPort method, of class MemcachedServer.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        MemcachedServer instance = new MemcachedServer();
        int expResult = 0;
        int result = instance.getPort();
        assertEquals(expResult, result);
        instance.setPort(123);
        assertEquals(123, instance.getPort());
    }

    /**
     * Test of setPort method, of class MemcachedServer.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        MemcachedServer instance = new MemcachedServer();
        int expResult = 0;
        int result = instance.getPort();
        assertEquals(expResult, result);
        instance.setPort(123);
        assertEquals(123, instance.getPort());
    }
}
