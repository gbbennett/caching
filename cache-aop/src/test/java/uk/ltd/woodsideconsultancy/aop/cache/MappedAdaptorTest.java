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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MappedAdaptorTest {

	private static final String THE_DEFAULT = "theDefault";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMappedAdaptor() {
		MappedAdaptor<String> a = new MappedAdaptor<String>("fred");
		a.setDefaultAdaptor(THE_DEFAULT);
		assertEquals(THE_DEFAULT, a.getAdaptor("fred"));
	}

	@Test
	public void testGetDefaultAdaptor() {
		MappedAdaptor<String> a = new MappedAdaptor<String>("fred");
		assertNull(a.getDefaultAdaptor());
		a.setDefaultAdaptor(THE_DEFAULT);
		assertEquals(THE_DEFAULT,a.getDefaultAdaptor());
	}

	@Test
	public void testSetDefaultAdaptor() {
		MappedAdaptor<String> a = new MappedAdaptor<String>("fred");
		a.setDefaultAdaptor(THE_DEFAULT);
		assertEquals(THE_DEFAULT,a.getDefaultAdaptor());
	}

	@Test
	public void testSetAdaptors() {
		Map<String, String> adaptors = new HashMap<String,String>();
		adaptors.put("1", "100");
		adaptors.put("2", "200");
		MappedAdaptor<String> a = new MappedAdaptor<String>("fred");
		a.setAdaptors(adaptors);
		
		assertEquals("100",a.getAdaptor("1"));
		assertEquals("200",a.getAdaptor("2"));
		assertNull(a.getDefaultAdaptor());
	}

	@Test
	public void testAddAdaptor() {
		MappedAdaptor<String> a = new MappedAdaptor<String>("fred");
		a.addAdaptor("jack", "jill");
		assertEquals("jill",a.getAdaptor("jack"));
		assertNull(a.getDefaultAdaptor());
		a.addAdaptor("fred", "blogs");
		assertEquals("blogs",a.getAdaptor("fred"));
		assertEquals("blogs",a.getDefaultAdaptor());
	}

	@Test
	public void testGetAdaptor() {
		MappedAdaptor<String> a = new MappedAdaptor<String>("fred");
		assertNull(a.getAdaptor("jack"));
		a.addAdaptor("jack", "jill");
		assertEquals("jill",a.getAdaptor("jack"));
	}

}
