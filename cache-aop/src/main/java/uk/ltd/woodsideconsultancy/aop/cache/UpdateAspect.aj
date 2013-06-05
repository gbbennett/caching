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


import uk.ltd.woodsideconsultancy.aop.cache.annotations.Update;


/**
 * Caching Aspect
 * @author Gary Bennett
 *
 */
public aspect UpdateAspect {

	/**
	 * Implements cache implementation agnostic caching
	 * the specific implementation should be injected into CacheAdaptor
	 */
	
    pointcut cachedUpdateMethod()
    /**
     * Intercept any call to a method with @Cache annotation
     * if annotation has a name value then use it as the name of the cache
     */
        : execution(@Update * *(..)) && !within(UpdateAspect) && !within(uk.ltd.woodsideconsultancy.aop.cache.*);
    Object around (final Update updateCache) : cachedUpdateMethod()  && @annotation( updateCache ) {

    	String cacheName = updateCache.name();
        String cacheImplentation = updateCache.implementation();
    	Object keys[] = thisJoinPoint.getArgs();
   	CacheAdaptor cacheInterface = CacheAdaptor.getInstance();
   		
        Object value = proceed(updateCache);
   		
        cacheInterface.update(cacheName, cacheImplentation, keys, 
                    value);
        return value;

    }
 

}
