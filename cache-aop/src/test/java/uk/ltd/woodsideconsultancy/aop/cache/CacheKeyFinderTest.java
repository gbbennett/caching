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

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.annotations.CacheKey;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

public class CacheKeyFinderTest {

	private JoinPoint basicJoinPoint;
	private JoinPoint simpleJoinPoint;
	private JoinPoint annotatedJoinPoint;
	private JoinPoint annotatedDateJoinPoint;
	private Date date = new Date();
	@Before
	public void setUp() throws Exception {
		basicJoinPoint = initJoinPoint("keyMethodBasic", new Object[0]);
		Object[] simpleParams = {"a",0};
		simpleJoinPoint = initJoinPoint("keyMethodSimple",
				simpleParams,
				String.class, int.class);
		Object[] annotatedParams = {"a",99};
		annotatedJoinPoint = initJoinPoint("keyMethodAnnotated",
				annotatedParams,
				String.class, int.class);
		Object[] annotatedDateParams = {"a",date};
		annotatedDateJoinPoint = initJoinPoint("keyMethodAnnotatedFormat",
				annotatedDateParams,
				String.class, Date.class);
	}
	private JoinPoint initJoinPoint(String methodName, Object[] params, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException{
		JoinPoint jp = mock(JoinPoint.class);
		MethodSignature signature = mock(MethodSignature.class);
		Method method = this.getClass().getMethod(methodName, paramTypes);
		when(jp.getArgs()).thenReturn(params);
		when(jp.getSignature()).thenReturn(signature);
		when(jp.getTarget()).thenReturn(this);
		when(signature.getMethod()).thenReturn(method);
		return jp;
		
	}

	@Test
	public void testGetKeysBasic() {
		
		Object keys[] = CacheKeyFinder.getKeys(basicJoinPoint);
		
		assertTrue(keys.length==0);
	}
	@Test
	public void testGetKeysSimple() {
		
		Object keys[] = CacheKeyFinder.getKeys(simpleJoinPoint);
		
		assertTrue(keys.length==2);
		assertEquals("a",keys[0]);
		assertEquals(0,keys[1]);
	}
	@Test
	public void testGetKeysAnnotated() {
		
		Object keys[] = CacheKeyFinder.getKeys(annotatedJoinPoint);
		
		assertTrue(keys.length==1);
		assertEquals(99,keys[0]);

	}
	@Test
	public void testGetKeysAnnotatedDate() {
		
		Object keys[] = CacheKeyFinder.getKeys(annotatedDateJoinPoint);
		
		assertTrue(keys.length==1);
		SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy");
		
		assertEquals(sdf.format(date),keys[0]);

	}
	public Object keyMethodBasic(){
		return null;
	}
	public Object keyMethodSimple(String s, int i){
		return null;
	}
	public Object keyMethodAnnotated(String s, @CacheKey int i){
		return null;
	}
	public Object keyMethodAnnotatedFormat(String s, @CacheKey(style="date",format="dd:MM:yy") Date d){
		return null;
	}
}
