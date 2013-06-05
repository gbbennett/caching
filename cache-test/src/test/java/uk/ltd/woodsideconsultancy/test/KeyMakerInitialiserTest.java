package uk.ltd.woodsideconsultancy.test;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.KeyMaker;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.CacheImplementation;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.KeyMakerImplementation;
import static org.junit.Assert.*;

public class KeyMakerInitialiserTest {
	private static final String ANOTHER_CACHE = "fred";

	@KeyMakerImplementation
	private KeyMaker keyMaker;

	@KeyMakerImplementation(name=ANOTHER_CACHE)
	private KeyMaker anotherKeyMaker;
	
	public KeyMakerInitialiserTest()
	{
		super();
	}
	@Before
	public void setup(){
		keyMaker = new MyKeyMaker();
		anotherKeyMaker = new MyKeyMaker();
	}
	@Test
	public void hasCacheTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		CacheAdaptor adaptor = CacheAdaptor.getInstance();

		assertEquals(keyMaker, adaptor.getDefaultKeyMaker());
		
	}
	@Test
	public void hasAnotherCacheTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		CacheAdaptor adaptor = CacheAdaptor.getInstance();
		
		assertEquals(anotherKeyMaker, adaptor.getKeyMaker(ANOTHER_CACHE));
		
	}
	private class MyKeyMaker implements KeyMaker {

		public Object generate(Object[] params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
