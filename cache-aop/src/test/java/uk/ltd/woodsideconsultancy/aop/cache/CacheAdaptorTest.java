/*
 	Copyright 2013 Woodside Consultancy Limited
 	
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ltd.woodsideconsultancy.aop.cache;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

public class CacheAdaptorTest {
	private static final String VALUE = "hello";
        private static final String ALTCACHE = "blah";
	private CacheAdaptor instance;
	private CacheInterface cacheInterface;
        private CacheInterface altCacheInterface;
	private CacheCallback callback;
	private String[] keys = {"abc", "def"};
	private boolean invalidated;
	private boolean invalidatedAll;
	@Before
	public void setUp() throws Exception {
		instance = CacheAdaptor.getInstance();
		cacheInterface = mock(CacheInterface.class);
                altCacheInterface = mock(CacheInterface.class);
                Map<String,CacheInterface> alternatives = new HashMap<String,CacheInterface>();
                alternatives.put(ALTCACHE, altCacheInterface);
                instance.setCachingImplementations(alternatives);
                
		Object obj[] = new Object[0];
		when(cacheInterface.execute(
						Matchers.anyString(), 
						Matchers.any(Object[].class), 
						Matchers.any(CacheCallback.class))).thenReturn(VALUE);
		instance.setCachingImplementation(cacheInterface);
		invalidated = false;
		
		doAnswer(new Answer<CacheInterface>(){
			public CacheInterface answer(InvocationOnMock invocation) throws Throwable {
				invalidated = true;
				return null;
			}
			
		}).when(cacheInterface).invalidate(Matchers.anyString(), Matchers.any(Object[].class));
		invalidatedAll = false;
		doAnswer(new Answer<CacheInterface>(){
			public CacheInterface answer(InvocationOnMock invocation) throws Throwable {
				invalidatedAll = true;
				return null;
			}
			
		}).when(cacheInterface).invalidateAll(Matchers.anyString());

	}

	@Test
	public void testGetInstance() {
		CacheAdaptor theInstance = CacheAdaptor.getInstance();
		assertSame(theInstance, instance);
	}

	@Test
	public void testExecute() {
		Object obj = cacheInterface.execute("myCache", keys, callback);
		
		assertEquals(VALUE, obj);
	}

	@Test
	public void testSetCachingImplementation() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		
		Field f = instance.getClass().getDeclaredField("cacheInterfaces");
		f.setAccessible(true);
		MappedAdaptor<CacheInterface> cacheInterfaces = (MappedAdaptor<CacheInterface>)f.get(instance);
		CacheInterface ci = cacheInterfaces.getDefaultAdaptor();
		
		assertSame(cacheInterface,ci);
	}
	@Test
	public void testSetCachingImplementations() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		
		CacheInterface ci = CacheAdaptor.getInstance().getImplementation(ALTCACHE);
		

		assertSame(altCacheInterface,ci);
	}
	@Test
	public void testInvalidate() {
		cacheInterface.invalidate("myCache", keys);
		assertTrue(invalidated);
	}

	@Test
	public void testInvalidateAll() {
		cacheInterface.invalidateAll("myCache");
		assertTrue(invalidatedAll);
	}

}
