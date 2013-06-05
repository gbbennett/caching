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

import uk.ltd.woodsideconsultancy.aop.cache.annotations.Cache;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.PostInvalidate;


/**
 * 
 * @author Gary Bennett
 *
 * Cache invalidation aspect after call
 */
public aspect PostInvalidateCache {
	
    pointcut invalidatingMethod()
    	: execution(@PostInvalidate * *(..)) && !within(PostInvalidateCache) && !within(CacheAspect) && !within(uk.ltd.woodsideconsultancy.aop.cache.*);
    pointcut cachedMethod()
    	: execution(@Cache * *(..)) && !within(CacheAspect) && !within(uk.ltd.woodsideconsultancy.aop.cache.*);

    /**
     * If Invalidate annotation has a named cache then invalidate it
     * if keys (indexes to parameters) are present only invalidate this cache entry
     * otherwise invalidate named cache
     * @param invalidate
     */
    after (PostInvalidate invalidate) :  invalidatingMethod() && @annotation( invalidate ){
    	String cacheName = invalidate.name();
        String cacheImplentation = invalidate.implementation();
    	if(cacheName.trim().length()>0){
    		CacheAdaptor cacheInterface = CacheAdaptor.getInstance();
   		
    		String keyIndexes = invalidate.keys();
    		if(keyIndexes.trim().length()>0){
    			String indexes[] = keyIndexes.split(",");
    			int len = indexes.length;
    			Object keys[] = new Object[len];
    			Object args[] = thisJoinPoint.getArgs();
    			for(int i = 0; i < len; i++){
    				keys[i] = args[i];
    			}
    			cacheInterface.invalidate(cacheName,cacheImplentation,keys);
    		} else {
    			Object keys[] = CacheKeyFinder.getKeys(thisJoinPoint);

    			if(keys.length>0){
    				cacheInterface.invalidate(cacheName,cacheImplentation,keys);
    			} else {
    				cacheInterface.invalidateAll(cacheName,cacheImplentation);
    			}
    		}
    	}
    }
}