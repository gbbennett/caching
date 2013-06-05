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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * @author Gary Bennett
 * 
 * Implementation of cache annotations using ehcache
 */
public class EhcacheBasedCache implements CacheInterface {
	private static final String EHCACHE_BASED_CACHE_FAILED_TO_WRITE_CACHE = "EhcacheBased cache failed to write to cache ";
	private static final String EHCACHE_BASED_CACHE_FAILED_TO_INVALIDATE_CACHE = "EhcacheBased cache failed to invalidate cache ";
	private static final String EHCACHE_BASED_CACHE_FAILED_TO_UPDATE_CACHE = "EhcacheBased cache failed to update cache ";
	/** Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(EhcacheBasedCache.class);
	/** ehcahce manager */
	private CacheManager manager;
	
	/**
	 * Constructor accesses manager
	 */
	public EhcacheBasedCache(){
		super();
		this.manager = CacheManager.getInstance();
	}
	/**
	 * Invalidate single entry in named cache based on keys
	 * @param cacheName name of cache
	 * @param keys parameters forming keys
	 */
	public void invalidate(String cacheName, Object key) {
		try {
			Cache cache = manager.getCache(cacheName);
			if(cache != null){				
				cache.remove(key);
			}
		} catch(Exception e){
			LOGGER.error(EHCACHE_BASED_CACHE_FAILED_TO_INVALIDATE_CACHE + cacheName + " " + e.getMessage(), e);
		}
	}
	/**
	 * Invalidate all entries in named cache
	 * @param cacheName name of cache
	 */
	public void invalidateAll(String cacheName) {
		try {
			Cache cache = manager.getCache(cacheName);
			if(cache != null){
				cache.removeAll();
			}
		} catch(Exception e){
			LOGGER.error(EHCACHE_BASED_CACHE_FAILED_TO_INVALIDATE_CACHE + cacheName + " " + e.getMessage(), e);
			
		}
	}
	/**
	 * lookup entry in cache, if not present call original method
	 * then populate entry
	 * @param cacheName name of cache
	 * @param keys parameters forming keys
	 * @param callback access to original cached method
	 */
	public Object execute(String cacheName, Object key,
			CacheCallback callback) {
		Cache cache = null;
		Element element = null;
		try {
			cache = manager.getCache(cacheName);
			element = cache.get(key);
			
		} catch(Exception e){
			LOGGER.error(EHCACHE_BASED_CACHE_FAILED_TO_WRITE_CACHE + cacheName + " " + e.getMessage(), e);
			
		}
		Object ret = null;
		
		if((element==null) || element.isExpired()){
			ret = callback.execute();
			try {
				cache.put(new Element(key, ret));
			} catch(Exception e){
				LOGGER.error(EHCACHE_BASED_CACHE_FAILED_TO_WRITE_CACHE + cacheName + " " + key + " "+ e.getMessage(), e);
				
			}
		} else {
			ret = element.getValue();
		}
		return ret;
	}
    /**
     * write value to cache
     * @param cacheName
     * @param keys
     * @param value 
     */
    public void update(String cacheName, Object key, Object value) {
		Cache cache = null;
		Element element = null;
	
		try {
			cache = manager.getCache(cacheName);
			
		} catch(Exception e){
			LOGGER.error(EHCACHE_BASED_CACHE_FAILED_TO_UPDATE_CACHE + cacheName + " " + e.getMessage(), e);
			
		}
        try {
                cache.put(new Element(key, value));
        } catch(Exception e){
                LOGGER.error(EHCACHE_BASED_CACHE_FAILED_TO_UPDATE_CACHE + cacheName + " " + key + " "+ e.getMessage(), e);

        }
    }        

}
