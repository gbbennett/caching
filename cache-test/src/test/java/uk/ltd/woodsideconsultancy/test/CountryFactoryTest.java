package uk.ltd.woodsideconsultancy.test;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.HashMapBasedCache;
import uk.ltd.woodsideconsultancy.aop.cache.SizedMapBasedCache;
import uk.ltd.woodsideconsultancy.test.CountryFactory;
import java.util.HashMap;
import java.util.Map;

public class CountryFactoryTest {
	private CountryFactory co = new CountryFactory();
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLookupCountry() {
		for(int i = 0; i < 4; i++){
			Country country = co.lookupCountry("en");
			System.out.println(country.getName());
			assertEquals("England",country.getName() );
		}
		Country country = co.lookupAnotherCountry("fr");
		System.out.println(country.getName());
		assertEquals("France",country.getName() );
	}
	@Test
	public void testLookupCountry2() {
		Country country = co.lookupAnotherCountry("fr");
		System.out.println(country.getName());
		assertEquals("France",country.getName() );
		country = co.lookupCountry("en");
		System.out.println(country.getName());
		assertEquals("England",country.getName() );
	}
	@Test
	public void testSerializeable() throws IOException {
		Country country = new Country();
		country.setName("France");
		FileOutputStream fos = new FileOutputStream("serial");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(country);
		oos.flush();
		oos.close(); 
		
	}
        @Test
        public void testMultiple(){
            	Country country = co.lookupAnotherCountry("fr");
		System.out.println(country.getName());
		assertEquals("France",country.getName() );
		country = co.lookupCountry("en");
		System.out.println(country.getName());
		assertEquals("England",country.getName() );
            	country = co.lookupCountryinOtherCache("fr");
		System.out.println(country.getName());
		assertEquals("France",country.getName() );
        }
}
