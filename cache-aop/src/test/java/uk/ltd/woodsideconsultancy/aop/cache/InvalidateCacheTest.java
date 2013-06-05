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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.doAnswer;




import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.stubbing.Answer;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.InvalidateCache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Cache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Invalidate;


public class InvalidateCacheTest extends BaseAspectAccess<InvalidateCache> {
	private InvalidateCache invalidateAspect;
	private CacheAdaptor cacheAdaptor;
	private Cache cache;
	private Invalidate invalidate;
	private Invalidate invalidateAll;
	private static String REPLY_OBJECT = "reply";
	private static String MYCACHE = "mycache";
	private org.aspectj.lang.JoinPoint joinPoint;
	private CacheInterface cachingImplementation;
	private boolean invalidateCalled;
	private boolean invalidateAllCalled;
	private Object jpargs[] = {"one"};
	private Object jpargsNone[] = new Object[0];
	private MethodSignature signature;
	
	@Before
	public void setUp() throws Exception {
		invalidateAspect = constructObject(InvalidateCache.class);
		//invalidateAspect = new InvalidateCache();
		cacheAdaptor = CacheAdaptor.getInstance();
		cachingImplementation = mock(CacheInterface.class);
		
		when(cachingImplementation.execute(Matchers.anyString(), Matchers.any(Object[].class), Matchers.any(CacheCallback.class))).thenReturn(REPLY_OBJECT);
		cacheAdaptor.setCachingImplementation(cachingImplementation);
		
		cache = mock(Cache.class);
		when(cache.name()).thenReturn(MYCACHE);
		when(cache.implementation()).thenReturn(CacheInterface.DEFAULT);
		
		invalidate = mock(Invalidate.class);
		when(invalidate.name()).thenReturn(MYCACHE);
		when(invalidate.keys()).thenReturn("0");
		when(invalidate.implementation()).thenReturn(CacheInterface.DEFAULT);
		
		invalidateAll = mock(Invalidate.class);
		when(invalidateAll.name()).thenReturn(MYCACHE);
		when(invalidateAll.keys()).thenReturn("");
		when(invalidateAll.implementation()).thenReturn(CacheInterface.DEFAULT);
		
		
		joinPoint = mock(org.aspectj.lang.JoinPoint.class);
		
		when(joinPoint.getArgs()).thenReturn(jpargs);
		
		signature = mock(MethodSignature.class);
		when(joinPoint.getSignature()).thenReturn(signature);
		
		
		invalidateCalled = false;
		doAnswer(new Answer<CacheInterface>(){
			public CacheInterface answer(InvocationOnMock invocation) throws Throwable {
				invalidateCalled = true;
				return null;
			}
		}).when(cachingImplementation).invalidate(Matchers.anyString(), Matchers.any(Object[].class));
		
		invalidateAllCalled = false;
		doAnswer(new Answer<CacheInterface>(){
			public CacheInterface answer(InvocationOnMock invocation) throws Throwable {
				invalidateAllCalled = true;
				return null;
			}
		}).when(cachingImplementation).invalidateAll(Matchers.anyString());
	}


	@Test
	public void testBeforeCache() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getBefore(invalidateAspect.getClass(), Cache.class);
		m.setAccessible(true);
		
		when(signature.getMethod()).thenReturn(m);
		when(joinPoint.getTarget()).thenReturn(invalidateAspect);
		
		m.invoke(invalidateAspect, cache, joinPoint);
		
		assertTrue(invalidateCalled);
	}
	@Test
	public void testInvalidate() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getBefore(InvalidateCache.class, Invalidate.class);
		m.setAccessible(true);
		m.invoke(invalidateAspect, invalidate, joinPoint);
		
		assertTrue(invalidateCalled);
	}
	@Test
	public void testInvalidate2() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getBefore(invalidateAspect.getClass(), Invalidate.class);
		m.setAccessible(true);
		when(signature.getMethod()).thenReturn(m);
		when(joinPoint.getTarget()).thenReturn(invalidateAspect);
		when(joinPoint.getArgs()).thenReturn(jpargs);
		
		m.invoke(invalidateAspect, invalidateAll, joinPoint);
		
		assertTrue(invalidateCalled);
	}
	@Test
	public void testInvalidateAll() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getBefore(invalidateAspect.getClass(), Invalidate.class);
		m.setAccessible(true);
		when(signature.getMethod()).thenReturn(m);
		when(joinPoint.getTarget()).thenReturn(invalidateAspect);
		when(joinPoint.getArgs()).thenReturn(jpargsNone);
		
		m.invoke(invalidateAspect, invalidateAll, joinPoint);
		
		assertTrue(invalidateAllCalled);
	}
}
