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

import java.net.URL;

import net.sf.ehcache.CacheManager;

import org.junit.Before;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.EhcacheBasedCache;



public class EhcacheBasedCacheTest {
	private CacheManager manager;
	private static final String HELLO_MSG = "Hello";
	private static final String MY_CACHE = "test.Country.Cache";
	private Object[] keys = new Object[1];
	private EhcacheBasedCache cache;
	private boolean updated = false;
	@Before
	public void setUp() throws Exception {
		if(manager == null){
			URL configurationFileURL = getClass().getResource("ehcache.xml");
			manager = CacheManager.create(configurationFileURL);
		}
		cache = new EhcacheBasedCache();
		keys[0] = "mykey";
	}

	@Test
	public void testInvalidate() {
		final String value = HELLO_MSG;
		updated = false;
		CacheCallback callback = new CacheCallback(){

			public Object execute() {
				updated = true;
				return value;
			}};
		Object obj = cache.execute(MY_CACHE, keys, callback);
		
		assertEquals(HELLO_MSG, obj.toString());
		
		cache.invalidate(MY_CACHE, keys);
		updated = false;
		
		obj = cache.execute(MY_CACHE, keys, callback);
		assertEquals(HELLO_MSG, obj.toString());
		assertTrue(updated);
	}

	@Test
	public void testInvalidateAll() {
		final String value = HELLO_MSG;
		updated = false;
		CacheCallback callback = new CacheCallback(){

			public Object execute() {
				updated = true;
				return value;
			}};
		Object obj = cache.execute(MY_CACHE, keys, callback);
		
		assertEquals(HELLO_MSG, obj.toString());
		
		cache.invalidateAll(MY_CACHE);
		updated = false;
		
		obj = cache.execute(MY_CACHE, keys, callback);
		assertEquals(HELLO_MSG, obj.toString());
		assertTrue(updated);
	}

	@Test
	public void testExecute() {
		final String value = HELLO_MSG;
		CacheCallback callback = new CacheCallback(){

			public Object execute() {
				// TODO Auto-generated method stub
				return value;
			}};
		Object obj = cache.execute(MY_CACHE, keys, callback);
		
		assertEquals(HELLO_MSG, obj.toString());
	}

	@Test
	public void testUpdate() {
		final String value = HELLO_MSG;
		cache.update(MY_CACHE, keys, value);
		
		assertFalse(updated);
	}        
	@Test
	public void testInvalidateException() {
		final String value = HELLO_MSG;
		updated = false;
		CacheCallback callback = new CacheCallback(){

			public Object execute() {
				updated = true;
				return value;
			}};
		Object obj = cache.execute(MY_CACHE, keys, callback);
		
		assertEquals(HELLO_MSG, obj.toString());
		
		// null causes exception
		// exceptions should be caught to avoid aspects breaking code
		boolean hadException = false;
		try {
			cache.invalidate(MY_CACHE, null);
		} catch( Exception e){
			hadException = true;
		}
		assertFalse(hadException);
	}
	@Test
	public void testInvalidateAllException() {
		final String value = HELLO_MSG;
		updated = false;
		CacheCallback callback = new CacheCallback(){

			public Object execute() {
				updated = true;
				return value;
			}};
		Object obj = cache.execute(MY_CACHE, keys, callback);
		
		assertEquals(HELLO_MSG, obj.toString());
		
		// null causes exception
		// exceptions should be caught to avoid aspects breaking code
		boolean hadException = false;
		try {
			cache.invalidateAll(null);
		} catch( Exception e){
			hadException = true;
		}
		assertFalse(hadException);
	}	
	@Test
	public void testExecuteException() {
		final String value = HELLO_MSG;
		CacheCallback callback = new CacheCallback(){

			public Object execute() {
				// TODO Auto-generated method stub
				return value;
			}
		};
		// null causes exception
		// exceptions should be caught to avoid aspects breaking code
		boolean hadException = false;
		try {
			Object obj = cache.execute(null, keys, callback);
		} catch( Exception e){
			hadException = true;
		}
		assertFalse(hadException);
	}
}
