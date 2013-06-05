package uk.ltd.woodsideconsultancy.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CountryTest {
	private static final String NEW_ZEALAND = "New Zealand";
	private Country country;
	@Before
	public void setUp() throws Exception {
		country = new Country();
	}

	@Test
	public void testCountry() {
		assertNotNull(country);
		assertNull(country.getName());
	}

	@Test
	public void testGetName() {
		assertNull(country.getName());
		country.setName(NEW_ZEALAND);
		assertEquals(NEW_ZEALAND,country.getName());
	}

	@Test
	public void testSetName() {
		assertNull(country.getName());
		country.setName(NEW_ZEALAND);
		assertEquals(NEW_ZEALAND,country.getName());
	}

	@Test
	public void testToString() {
		assertNull(country.getName());
		country.setName(NEW_ZEALAND);
		assertTrue(country.toString().indexOf(NEW_ZEALAND)!=-1);
	}

}
