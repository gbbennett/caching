/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ltd.woodsideconsultancy.aop.cache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.spy.memcached.MemcachedClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.ltd.woodsideconsultancy.aop.cache.MemcachedBasedCache;
import static org.mockito.Mockito.doAnswer;
/**
 *
 * @author gary bennett
 */
public class MemcachedBasedCacheTest {
    private MemcachedClient client;
    private boolean deleted = false;
    private Map<String,Object> cacheMap = new HashMap<String,Object>();
    
    public MemcachedBasedCacheTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        client = mock(MemcachedClient.class);
        doAnswer(new Answer<String>(){
			public String answer(InvocationOnMock invocation) throws Throwable {
				deleted = true;
				return null;
			}
			
		}).when(client).delete(Matchers.anyString());
        
        doAnswer(new Answer<String>(){
			public String answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
                                cacheMap.put((String)args[0], args[2]);
				return null;
			}
			
		}).when(client).set(Matchers.anyString(),Matchers.anyInt(),Matchers.anyObject());
        
        doAnswer(new Answer<Object>(){
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
                                return cacheMap.get((String)args[0]);
			}
			
		}).when(client).get(Matchers.anyString());
        

    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDefaultTimeout method, of class MemcachedBasedCache.
     */
    @Test
    public void testGetDefaultTimeout() {
        System.out.println("getDefaultTimeout");
        MemcachedBasedCache instance = new MemcachedBasedCache();
        int expResult = MemcachedBasedCache.DEFAULT_TIMEOUT;
        int result = instance.getDefaultTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDefaultTimeout method, of class MemcachedBasedCache.
     */
    @Test
    public void testSetDefaultTimeout() {
        System.out.println("setDefaultTimeout");
        int expResult = 50;
        MemcachedBasedCache instance = new MemcachedBasedCache();
        instance.setDefaultTimeout(expResult);
        int result = instance.getDefaultTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCacheSeconds method, of class MemcachedBasedCache.
     */
    @Test
    public void testGetCacheSeconds() {
        System.out.println("getCacheSeconds");
        MemcachedBasedCache instance = new MemcachedBasedCache();

        Map result = instance.getCacheSeconds();
        assertNull(result);

        Map<String, Integer> map = new HashMap<String,Integer>();
        map.put("hello", 123);
        instance.setCacheSeconds(map);
        
        result = instance.getCacheSeconds();
        assertNotNull(result);
        assertEquals(123, result.get("hello"));
    }

    /**
     * Test of setCacheSeconds method, of class MemcachedBasedCache.
     */
    @Test
    public void testSetCacheSeconds() {
        System.out.println("setCacheSeconds");
        MemcachedBasedCache instance = new MemcachedBasedCache();

        Map result = instance.getCacheSeconds();
        assertNull(result);

        Map<String, Integer> map = new HashMap<String,Integer>();
        map.put("hello", 123);
        instance.setCacheSeconds(map);
        
        result = instance.getCacheSeconds();
        assertNotNull(result);
        assertEquals(123, result.get("hello"));
    }

    /**
     * Test of invalidate method, of class MemcachedBasedCache.
     */
    @Test
    public void testInvalidate() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("invalidate");
        String cacheName = "fred";
        Object[] keys = {"abc"};
        MemcachedBasedCache instance = new MemcachedBasedCache();
        Field f = instance.getClass().getDeclaredField("client");
	f.setAccessible(true);
        f.set(instance, client);
        Map<String, Integer> map = new HashMap<String,Integer>();
        map.put("fred", 123);
        instance.setCacheSeconds(map);
        instance.put(cacheName, keys, "data");
        
        instance.invalidate(cacheName, keys);

        Object result = instance.get(cacheName, keys);
        
        assertTrue(deleted);
        
    }
    /**
	@Test
	public void testUpdate() {
		final String value = HELLO_MSG;
		instance.update(MY_CACHE, keys, value);
		
		assertTrue(updated);
	}        
        * */

    /**
     * Test of execute method, of class MemcachedBasedCache.
     *
    @Test
    public void testExecute() {
        MemcachedBasedCache instance = new MemcachedBasedCache();
        CacheCallback callback = new CacheCallback(){

                public Object execute() {
                        return "hello";
                }

        };
        String cacheName = "mycache4";
        Object keys[] = {"1","2","3"};
        Object ret = instance.execute(cacheName, keys, callback);

        assertEquals("hello", ret);
    }
    */
    /**
     * Test of put method, of class MemcachedBasedCache.
     */
    @Test
    public void testPut() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        System.out.println("put");
        String cacheName = "fred";
        Object[] keys = {"abc"};
        Object obj = "123";
        MemcachedBasedCache instance = new MemcachedBasedCache();
        Field f = instance.getClass().getDeclaredField("client");
	f.setAccessible(true);
        f.set(instance, client);
        instance.put(cacheName, keys, obj);

        Object value = instance.get(cacheName, keys);
        
        assertEquals(obj, value);
    }
}
