package uk.ltd.woodsideconsultancy.aop.cache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.SizedMapBasedCache;



public class SizedMapBasedCacheTest {
	private SizedMapBasedCache instance;
	@Before
	public void setUp() throws Exception {
		instance = new SizedMapBasedCache();
	}

	@Test
	public void testGet() {
		String cacheName = "mycache1";
		Object keys[] = {"one","two","threee"};
		assertNull(instance.get(cacheName, keys));
		
		instance.put(cacheName, keys, "hello");
		assertEquals("hello",instance.get(cacheName, keys));
		
	}

	@Test
	public void testPut() {
		String cacheName = "mycache2";
		Object keys[] = {"a","b","c"};
		instance.put(cacheName, keys, "hello");
		assertEquals("hello",instance.get(cacheName, keys));
	}

	@Test
	public void testInvalidate() {
		String cacheName = "mycache3";
		Object keys1[] = {"a","b","c"};
		instance.put(cacheName, keys1, "hello");
		Object keys2[] = {"a","b","c", "d"};
		instance.put(cacheName, keys2, "goodbye");
		
		instance.invalidate(cacheName, keys1);
		assertNull(instance.get(cacheName, keys1));
		assertEquals("goodbye",instance.get(cacheName, keys2));
		
	}

	@Test
	public void testInvalidateAll() {
		String cacheName = "mycache4";
		Object keys1[] = {"1","2","3"};
		instance.put(cacheName, keys1, "hello");
		Object keys2[] = {"1","2","3", "4"};
		instance.put(cacheName, keys2, "goodbye");
		
		instance.invalidateAll(cacheName);
		assertNull(instance.get(cacheName, keys1));
		assertNull(instance.get(cacheName, keys2));
		
	}
	@Test
	public void testExecute(){
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
	@Test
	public void testUpdate(){
		String cacheName = "mycache4";
		Object keys[] = {"1","2","3"};
		instance.update(cacheName, keys, "hello");
				
                CacheCallback callback = new CacheCallback(){

			public Object execute() {
				return "goodbye";
			}
			
		};
                Object ret = instance.execute(cacheName, keys, callback);
                // should be hello - this is deliberate
                // as hello should be cached
		assertEquals("hello", ret);
	}         
}
