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

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author Gary Bennett
 * 
 * Implementation of cache allowing limits to size
 */
public class SizedMapBasedCache implements CacheInterface {
    /**
     * Maximum allowed entries in an individual cache
     */
    private int maxEntriesPerCache = 10;
    /** 
     * Max number of caches alive
     */
    private static int maxCaches = 10;
    /**
     * Max time a cache entry can stay alive
     */
    private long maxDuration = 600;
    
    /**
     * Initialise cache of caches
     */
    private static Map<String,TimeStampedEntry> cacheMap = Collections.synchronizedMap(new SizedMap<String,TimeStampedEntry>(){
	    
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size()>maxCaches;
        }
	});

    /**
     * Constructor
     */
    public SizedMapBasedCache(){
        super();
    }
    /**
     * Invalidate single entry in named cache based on keys
     * @param cacheName name of cache
     * @param keys parameters forming keys
     */
    public void invalidate(String cacheName, Object key) {
        Map map = getMap(cacheName);
        
        map.remove(key);
    }
    /**
     * Invalidate all entries in named cache
     * @param cacheName name of cache
     */
    public void invalidateAll(String cacheName) {
        cacheMap.remove(cacheName);
    }
    /**
     * lookup entry in cache, if not present call original method
     * then populate entry
     * @param cacheName name of cache
     * @param keys parameters forming keys
     * @param callback access to original cached method
     */
    public Object execute(String cacheName, Object key, CacheCallback callback) {
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
     * Max number of entries per individual cache
     * @return number of entries per individual cache
     */
    public int getMaxEntriesPerCache() {
        return maxEntriesPerCache;
    }
    /**
     * Max number of entries per individual cache
     * @param maxEntriesPerCache number of entries per individual cache
     */
    public void setMaxEntriesPerCache(int maxEntriesPerCache) {
        this.maxEntriesPerCache = maxEntriesPerCache;
    }
    /**
     * Max number of caches allowed
     * @return number of caches allowed
     */
    public int getMaxCaches() {
        return maxCaches;
    }
    /**
     * Max number of caches allowed
     * @param maxCaches number of caches allowed
     */
    public void setMaxCaches(int maxCaches) {
        this.maxCaches = maxCaches;
    }
    /**
     * Max time a cache entry can live in seconds
     * @return time a cache entry can live in seconds
     */
    public long getMaxDuration() {
        return maxDuration;
    }
    /**
     * Max time a cache entry can live in seconds
     * @param maxDuration time a cache entry can live in seconds
     */
    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }
    /**
     * get value from cache
     * @param cacheName name of cache
     * @param keys parameters forming keys
     * @return cached result
     */
    public Object get(String cacheName, Object key) {
        Map<Object,TimeStampedEntry> map = getMap(cacheName);
        Object obj = null;
 
        TimeStampedEntry tse = map.get(key);
        if(tse != null){
            obj = tse.getObject();
        }

        return obj;
    }

    /**
     * set value in cache
     * @param cacheName name of cache
     * @param keys parameters forming keys
     * @param obj what to cache
     */
    public void put(String cacheName, Object key, Object obj) {
        Map<Object,TimeStampedEntry> map = getMap(cacheName);
        
        map.put(key, new TimeStampedEntry(obj));

    }   
    /**
     * Find the map for a named cache
     * @param cacheName name of cache
     * @return Map of entries for named cache
     */
    private Map<Object,TimeStampedEntry> getMap(String cacheName){
        Map map = null;
        
        TimeStampedEntry<Map> tse = cacheMap.get(cacheName);
        if((tse==null)){
        	SizedMap<String,TimeStampedEntry> sm = new SizedMap<String,TimeStampedEntry>();
        	sm.setMaxEntries(maxEntriesPerCache);
        	sm.setMaxDuration(maxDuration);
            tse = new TimeStampedEntry<Map>(Collections.synchronizedMap(sm));
        }
        map = tse.getObject();

        cacheMap.put(cacheName, tse);
        return map;
    }


}
