package uk.ltd.woodsideconsultancy.aop.cache.test;

import java.net.URL;

import net.sf.ehcache.CacheManager;

import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.EhcacheBasedCache;
import uk.ltd.woodsideconsultancy.test.CountryFactory;

/**
 * 
 * @author Gary Bennett
 *
 * Test code
 */
public class CountryFinder {
	private static CacheAdaptor adaptor;
	/**
	 * Constructory
	 */
	public CountryFinder(){
		super();
		if(adaptor==null){
			adaptor = CacheAdaptor.getInstance();
			//adaptor.setCachingImplementation(new HashMapBasedCache());
			//adaptor.setCachingImplementation(new DynamapBasedCache());
			URL configurationFileURL = getClass().getResource("ehcache.xml");
			CacheManager.create(configurationFileURL);
			
			adaptor.setCachingImplementation(new EhcacheBasedCache());
		}
	}
	/**
	 * lookup
	 * @param isoCode
	 * @param invalid
	 * @return
	 */
	public String getCountry(String isoCode, String invalid){
		if(isoCode==null){
			return "null";
		}
		CountryFactory co = new CountryFactory();
		if(invalid!=null){
			if(invalid.equals("true")){
				// invalidate cache entry
				return co.lookupAnotherCountry(isoCode).getName();
			} else if(invalid.equals("all")){
				// invalidate all cache entries
				return co.clearCacheAndLookupCountry(isoCode).getName();
			} else if(invalid.equals("single")){
				// invalidate all cache entries
				return co.clearEntryAndlookupCountry("it", isoCode).getName();
			}
		}
		return co.lookupCountry(isoCode).getName();
	}

}
