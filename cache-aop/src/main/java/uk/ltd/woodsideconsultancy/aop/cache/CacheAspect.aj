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


import uk.ltd.woodsideconsultancy.aop.cache.CacheAdaptor;
import uk.ltd.woodsideconsultancy.aop.cache.CacheCallback;

import uk.ltd.woodsideconsultancy.aop.cache.annotations.Cache;


/**
 * Caching Aspect
 * @author Gary Bennett
 *
 */
public aspect CacheAspect {

	/**
	 * Implements cache implementation agnostic caching
	 * the specific implementation should be injected into CacheAdaptor
	 */
	
    pointcut cachedMethod()
    /**
     * Intercept any call to a method with @Cache annotation
     * if annotation has a name value then use it as the name of the cache
     */
        : execution(@Cache * *(..)) && !within(CacheAspect) && !within(uk.ltd.woodsideconsultancy.aop.cache.*);
    Object around (final Cache cache) : cachedMethod()  && @annotation( cache ) {

    	String cacheName = cache.name();
        String cacheImplentation = cache.implementation();
    	
    	Object keys[] = CacheKeyFinder.getKeys(thisJoinPoint);
    	
    	
   		CacheAdaptor cacheInterface = CacheAdaptor.getInstance();
   		
   		CacheCallback callback = new CacheCallback(){
   			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Object execute(){
   				return proceed(cache);
   			}
   		};
   		
   		return cacheInterface.execute(cacheName, cacheImplentation, keys, callback);

    }
 

}
