package uk.ltd.woodsideconsultancy.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;
import uk.ltd.woodsideconsultancy.aop.cache.HashMapBasedCache;
import uk.ltd.woodsideconsultancy.aop.cache.SizedMapBasedCache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Cache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.CacheImplementation;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.Invalidate;


/**
 * 
 * @author Gary Bennett
 *
 * Test implementation
 */
public class CountryFactory {
	private static final int DELAY_MS = 2000;

	/** Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryFactory.class);
	
	@CacheImplementation
	private CacheInterface hashMapBasedCache;
	@CacheImplementation(name="othercaching")
	private CacheInterface anotherCacheImpl;
	
	private static Map<String,String> countryMap = new HashMap<String,String>();
	static {
			countryMap.put("en", "England");
			countryMap.put("fr", "France");
			countryMap.put("it", "Italy");
			countryMap.put("es", "Spain");
			countryMap.put("dk", "Denmark");
			countryMap.put("de", "Germany");
			countryMap.put("pt", "Portugal");
			countryMap.put("no", "Norway");
			countryMap.put("ie", "Ireland");
	};
	
	public CountryFactory(){
		super();
		hashMapBasedCache = new HashMapBasedCache();
		anotherCacheImpl = new SizedMapBasedCache();
	}
	@Cache(name="test.Country.Cache")
	public Country lookupCountry(String isoCode){
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Expensive slow SQL made");
		}
		try {
			Thread.sleep(DELAY_MS);
		} catch (Exception e){
			
		}
		Country country = new Country();
		String name = countryMap.get(isoCode);
		if(name==null){
			name = "unknown";
		}
		country.setName(name);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(country.getName());
		}
		return country;
	}
        @Cache(name="test.Country.Cache",implementation="othercaching")
	public Country lookupCountryinOtherCache(String isoCode){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Expensive slow SQL made");
                        
		}
		try {
			Thread.sleep(DELAY_MS);
		} catch (Exception e){
			
		}
		Country country = new Country();
		String name = countryMap.get(isoCode);
		if(name==null){
			name = "unknown";
		}
		country.setName(name);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(country.getName());
		}
		return country;
	}
	@Invalidate 
	public Country lookupAnotherCountry(String isoCode){
		return lookupCountry(isoCode);
	}
	/**
	 * Invalidate all cache
	 * @param isoCode
	 * @return
	 */
	@Invalidate(name="test.Country.Cache")
	public Country clearCacheAndLookupCountry(String isoCode){
		return lookupCountry(isoCode);
	}
	/**
	 * Invalidate an entry in cache
	 * @param isoCode
	 * @return
	 */
	@Invalidate(name="test.Country.Cache",keys="1")
	public Country clearEntryAndlookupCountry(String param1, String isoCode){
		return lookupCountry(isoCode);
	}	
}
