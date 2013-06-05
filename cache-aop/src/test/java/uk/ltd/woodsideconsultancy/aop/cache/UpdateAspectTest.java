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
import java.lang.reflect.Type;

import org.aspectj.runtime.internal.AroundClosure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;


import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.UpdateAspect;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Update;

public class UpdateAspectTest extends BaseAspectAccess<UpdateAspect> {
	private UpdateAspect updateAspect;
	private CacheAdaptor cacheAdaptor;
	private Update update;
	private static String REPLY_OBJECT = "reply";
	private static String MYCACHE = "mycache";
	private AroundClosure aroundClosure;
	private org.aspectj.lang.JoinPoint joinPoint;
	private CacheInterface cachingImplementation;
        private String value;
	@Before
	public void setUp() throws Exception {
		updateAspect = constructObject(UpdateAspect.class);
		cacheAdaptor = CacheAdaptor.getInstance();
		cachingImplementation = mock(CacheInterface.class);
		
                doAnswer(new Answer<String>() {
		    public String answer(InvocationOnMock invocation) throws Throwable {
		        value = REPLY_OBJECT;
		        return value;
		    }
		}).when(cachingImplementation).update(Matchers.anyString(), Matchers.any(Object[].class), Matchers.any(CacheCallback.class));
                
		cacheAdaptor.setCachingImplementation(cachingImplementation);
		
		update = mock(Update.class);
		when(update.name()).thenReturn(MYCACHE);
		when(update.implementation()).thenReturn(CacheInterface.DEFAULT);
		
		aroundClosure = mock(AroundClosure.class);
		
		joinPoint = mock(org.aspectj.lang.JoinPoint.class);
	}


	@Test
	public void testAround() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method m = getAround(UpdateAspect.class,Update.class);
		m.setAccessible(true);
		m.invoke(updateAspect, update, aroundClosure, joinPoint);
		
		assertEquals(REPLY_OBJECT, value);
	}

}
