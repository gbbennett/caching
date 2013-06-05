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
import java.lang.reflect.Type;

import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.runtime.internal.AroundClosure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheAspect;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Cache;



import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CacheAspectTest extends BaseAspectAccess<CacheAspect> {
	private CacheAspect cacheAspect;
	private CacheAdaptor cacheAdaptor;
	private Cache cache;
	private static String REPLY_OBJECT = "reply";
	private static String MYCACHE = "mycache";
	private AroundClosure aroundClosure;
	private org.aspectj.lang.JoinPoint joinPoint;
	private CacheInterface cachingImplementation;
	private MethodSignature signature;
	@Before
	public void setUp() throws Exception {
		cacheAspect = constructObject(CacheAspect.class);
		cacheAdaptor = CacheAdaptor.getInstance();
		cachingImplementation = mock(CacheInterface.class);
		
		when(cachingImplementation.execute(Matchers.anyString(), Matchers.any(Object[].class), Matchers.any(CacheCallback.class))).thenReturn(REPLY_OBJECT);
		cacheAdaptor.setCachingImplementation(cachingImplementation);
		
		cache = mock(Cache.class);
		when(cache.name()).thenReturn(MYCACHE);
		when(cache.implementation()).thenReturn(CacheInterface.DEFAULT);
		
		aroundClosure = mock(AroundClosure.class);
		
		joinPoint = mock(org.aspectj.lang.JoinPoint.class);
		signature = mock(MethodSignature.class);
		when(joinPoint.getSignature()).thenReturn(signature);
	}


	@Test
	public void testAround() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getAround(cacheAspect.getClass(),Cache.class);
		m.setAccessible(true);
		when(signature.getMethod()).thenReturn(m);
		when(joinPoint.getTarget()).thenReturn(cacheAspect);
		
		String reply = (String)m.invoke(cacheAspect, cache, aroundClosure, joinPoint);
		
		assertEquals(REPLY_OBJECT, reply);
	}

}
