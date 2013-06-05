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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
import uk.ltd.woodsideconsultancy.aop.cache.PostInvalidateCache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Cache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.PostInvalidate;


public class PostInvalidateCacheTest extends BaseAspectAccess<PostInvalidateCache> {
	private PostInvalidateCache invalidateAspect;
	private CacheAdaptor cacheAdaptor;
	private Cache cache;
	private PostInvalidate postInvalidate;
	private static String REPLY_OBJECT = "reply";
	private static String MYCACHE = "mycache";
	private org.aspectj.lang.JoinPoint joinPoint;
	private CacheInterface cachingImplementation;
	private boolean invalidateCalled;
	private Object jpargs[] = {"one"};
	@Before
	public void setUp() throws Exception {
		invalidateAspect = constructObject(PostInvalidateCache.class);
		cacheAdaptor = CacheAdaptor.getInstance();
		cachingImplementation = mock(CacheInterface.class);
		
		when(cachingImplementation.execute(Matchers.anyString(), Matchers.any(Object[].class), Matchers.any(CacheCallback.class))).thenReturn(REPLY_OBJECT);
		cacheAdaptor.setCachingImplementation(cachingImplementation);
		
		cache = mock(Cache.class);
		when(cache.name()).thenReturn(MYCACHE);
		when(cache.implementation()).thenReturn(CacheInterface.DEFAULT);

		postInvalidate = mock(PostInvalidate.class);
		when(postInvalidate.name()).thenReturn(MYCACHE);
		when(postInvalidate.keys()).thenReturn("0");
		when(postInvalidate.implementation()).thenReturn(CacheInterface.DEFAULT);

		
		joinPoint = mock(org.aspectj.lang.JoinPoint.class);
		
		when(joinPoint.getArgs()).thenReturn(jpargs);
		invalidateCalled = false;
		doAnswer(new Answer<CacheInterface>(){
			public CacheInterface answer(InvocationOnMock invocation) throws Throwable {
				invalidateCalled = true;
				return null;
			}
		}).when(cachingImplementation).invalidate(Matchers.anyString(), Matchers.any(Object[].class));
		
	}



	@Test
	public void testInvalidateAll() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getAfter(PostInvalidateCache.class,PostInvalidate.class);
		m.setAccessible(true);
		m.invoke(invalidateAspect,postInvalidate, joinPoint);
		
		assertTrue(invalidateCalled);
	}

}
