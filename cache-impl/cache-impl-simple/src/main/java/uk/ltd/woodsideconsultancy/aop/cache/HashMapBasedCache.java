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


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;
import uk.ltd.woodsideconsultancy.aop.cache.CacheInterface;




/**
 * 
 * @author Gary Bennett
 * 
 * Implementation of cache annotations using map
 *
 */
public class HashMapBasedCache implements CacheInterface {
	private static final String VALUE = "value";
	private static Map<String,Map<Object,Object>> cacheMap = new ConcurrentHashMap<String, Map<Object,Object>>();
	
	/**
	 * Constructor
	 */
	public HashMapBasedCache(){
		super();
	}
	/**
	 * lookup entry in cache, if not present call original method
	 * then populate entry
	 * @param cacheName name of cache
	 * @param keys parameters forming keys
	 * @param callback access to original cached method
	 */	
	public Object execute(String cacheName, Object key, CacheCallback callback){
		Object ret = get(cacheName, key);
   		if(ret == null){
   			ret = callback.execute();
			//ret = jp.proceed(keys);
			put(cacheName, key, ret);
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
        put(cacheName,key,value);
    }        
	/**
	 * get value from cache
	 * @param cacheName name of cache
	 * @param keys parameters forming keys
	 * @return cached result
	 */
	public Object get(String cacheName, Object key) {
		Map m = getEntryMap(cacheName, key);
		return m.get(VALUE);
	}

	/**
	 * set value in cache
	 * @param cacheName name of cache
	 * @param keys parameters forming keys
	 * @param obj what to cache
	 */
	public void put(String cacheName, Object key, Object obj) {
		Map m = getEntryMap(cacheName, key);
		m.put(VALUE,obj);
	}
	/**
	 * Invalidate single entry in named cache based on keys
	 * @param cacheName name of cache
	 * @param keys parameters forming keys
	 */
	public void invalidate(String cacheName, Object key) {
		Map m = getEntryMap(cacheName, key);
		m.remove(VALUE);
	}
	/**
	 * Invalidate all entries in named cache
	 * @param cacheName name of cache
	 */
	public void invalidateAll(String cacheName) {
		cacheMap.remove(cacheName);
	}
	private Map getEntryMap(String cacheName, Object key) {
		Map<Object, Object> map = getCacheMap(cacheName);
		
		Map<Object, Object> m = (Map<Object,Object>)map.get(key);
		
		if(m==null){
			m = new ConcurrentHashMap<Object,Object>();
			map.put(key, m);
		}
		return m;
	}
	/**
	 * get map for a cache
	 * @param cacheName
	 * @return
	 */
	private Map<Object, Object> getCacheMap(String cacheName) {
		Map<Object,Object> map = cacheMap.get(cacheName);
		if(map == null){
			map = new ConcurrentHashMap<Object,Object>();
			cacheMap.put(cacheName, map);
		}
		return map;
	}


}
