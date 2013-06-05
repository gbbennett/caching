package uk.ltd.woodsideconsultancy.aop.cache.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CountryFinderTest {
	private CountryFinder finder;
	@Before
	public void setUp() throws Exception {
		finder = new CountryFinder();
	}

	@Test
	public void testCountryFinder() {
		assertNotNull(finder);
	}

	@Test
	public void testGetCountry() {
		String country = finder.getCountry("it",null);
		assertEquals("Italy", country);
	}
	@Test
	public void testGetCountryNull(){
		String country = finder.getCountry(null,null);
		assertEquals("null",country);
	}
	@Test
	public void testGetCountryTrue() {
		String country = finder.getCountry("it","true");
		assertEquals("Italy", country);
	}
	@Test
	public void testGetCountrySingle() {
		String country = finder.getCountry("it","single");
		assertEquals("Italy", country);
	}
	@Test
	public void testGetCountryAll() {
		String country = finder.getCountry("it","all");
		assertEquals("Italy", country);
	}

}
