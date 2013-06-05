package uk.ltd.woodsideconsultancy.test;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.CacheImplementation;
import static org.junit.Assert.*;

public class CacheInitialiserTest {
	@CacheImplementation
	private CacheInterface hashMapBasedCache;
	@CacheImplementation(name="anotherCache")
	private CacheInterface anotherCacheImpl;
	
	public CacheInitialiserTest()
	{
		super();
	}
	@Before
	public void setup(){
		hashMapBasedCache = new MyCacheImplementation();
		anotherCacheImpl = new MyCacheImplementation();
		
	}
	@Test
	public void hasCacheTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		CacheAdaptor adaptor = CacheAdaptor.getInstance();
		assertEquals(hashMapBasedCache, adaptor.getImplementation(CacheInterface.DEFAULT));
		
		assertEquals(anotherCacheImpl, adaptor.getImplementation("anotherCache"));
		
	}
	private class MyCacheImplementation implements CacheInterface {

		public void invalidate(String cacheName, Object key) {
			// TODO Auto-generated method stub
			
		}

		public void invalidateAll(String cacheName) {
			// TODO Auto-generated method stub
			
		}

		public Object execute(String cacheName, Object key,
				CacheCallback callback) {
			// TODO Auto-generated method stub
			return null;
		}

		public void update(String cacheName, Object key, Object value) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
