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


/**
 * Adaptor class to wrap real implementation of caching
 * @author Gary Bennett
 *
 */
public final class CacheAdaptor  {
	/** singleton instance */
	private static volatile CacheAdaptor instance = null;
	/** the caching implementations */
	private MappedAdaptor<CacheInterface> cacheInterfaces = new MappedAdaptor<CacheInterface>(CacheInterface.DEFAULT);
    /** allow a key maker per cache */
    private MappedAdaptor<KeyMaker> keyMakers = new MappedAdaptor<KeyMaker>(CacheInterface.DEFAULT);
	/**
	 * private constructor
	 */
	private CacheAdaptor(){
		super();
		keyMakers.setDefaultAdaptor(new StringKeyMaker());
	}
	/**
	 * get the singleton instance
	 * @return instance of this
	 */
	public static CacheAdaptor getInstance(){
		if(instance == null){
			synchronized(CacheAdaptor.class){
				if(instance == null){
					instance = new CacheAdaptor();
				}
			}
		}
		return instance;
	}
	/**
	 * inject actual implementation
	 * @param cachingImplementation
	 */
	public void setCachingImplementation(CacheInterface cachingImplementation) {
		cacheInterfaces.setDefaultAdaptor(cachingImplementation);
	}
	/**
	 * supply access to default keymaker
	 * @return
	 */
    public KeyMaker getDefaultKeyMaker() {
		return keyMakers.getDefaultAdaptor();
	}
    /**
     * Allow replacement keymaker
     * @param defaultKeyMaker
     */
	public void setDefaultKeyMaker(KeyMaker defaultKeyMaker) {
		keyMakers.setDefaultAdaptor(defaultKeyMaker);
	}
	/**
	 * Allow injection of cache specific keymakers
	 * @param defaultAdaptor
	 */
	public void setKeyMakers(Map<String, KeyMaker> defaultAdaptors) {
		keyMakers.setAdaptors(defaultAdaptors);
	}
    public void addKeyMaker(String name, KeyMaker keyMaker) {
    	keyMakers.addAdaptor(name, keyMaker);
    }
    /**
     * get the key make for the named cache
     * @param name
     * @return
     */
    public KeyMaker getKeyMaker(String name){
    	KeyMaker maker = keyMakers.getAdaptor(name);
    	if(maker==null){
    		maker = keyMakers.getDefaultAdaptor();
    	}
    	return maker;
    }
	/**
     * allow additional implementations
     * 
     * @param cachingImplementations 
     */
    public void setCachingImplementations(Map<String, CacheInterface> cachingImplementations) {
    	cacheInterfaces.setAdaptors(cachingImplementations);
    }
    /**
     * add additional implementations
     * 
     * @param cachingImplementations 
     */
    public void addCachingImplementation(String name, CacheInterface cachingImplementation) {
    	cacheInterfaces.addAdaptor(name, cachingImplementation);
    }
   
	/**
	 * query and or cache the object 
	 * @param cacheName which cache to use
	 * @param keys list of parameters for cached method
	 * @param callback where to call original method
	 */
	public Object execute(String cacheName, String cacheImplentation, Object keys[], CacheCallback callback) {
            CacheInterface cacheInterface = getImplementation(cacheImplentation);
            
            if(cacheInterface!=null){
            	KeyMaker keyMaker = getKeyMaker(cacheName);
                return cacheInterface.execute(cacheName, keyMaker.generate(keys), callback);
            }
            // if no implementation do not cache
            return callback.execute();
	}

	/**
	 * Invalidate individual entry
	 * @param cacheName which cache to find entry
	 * @param keys parameters used as cache key
	 */
	public void invalidate(String cacheName, String cacheImplentation, Object keys[]) {
            CacheInterface cacheInterface = getImplementation(cacheImplentation);
            if(cacheInterface!=null){
            	KeyMaker keyMaker = getKeyMaker(cacheName);
            	cacheInterface.invalidate(cacheName, keyMaker.generate(keys));
            }
	}
	/**
	 * remove all entries from cache
	 * @param cacheName which cache to find entry
	 */
	public void invalidateAll(String cacheName, String cacheImplentation) {
            CacheInterface cacheInterface = getImplementation(cacheImplentation);
            if(cacheInterface!=null){
            	cacheInterface.invalidateAll(cacheName);
            }
	}
        /**
         * update cache entry
         * @param cacheName
         * @param keys
         * @param value 
         */
        public void update(String cacheName, String cacheImplentation, Object[] keys, Object value) {
            CacheInterface cacheInterface = getImplementation(cacheImplentation);
            if(cacheInterface!=null){
            	KeyMaker keyMaker = getKeyMaker(cacheName);
                cacheInterface.update(cacheName, keyMaker.generate(keys), value);
            }
        }
        /**
         * lookup caching implementation
         * @param cacheImplentation
         * @return 
         */
        public CacheInterface getImplementation(String cacheImplentation){
        	return cacheInterfaces.getAdaptor(cacheImplentation);
        }
}
