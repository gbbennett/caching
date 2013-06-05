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


public interface CacheInterface {
        /**
         * Default implementation name
         */
        String DEFAULT = "__default";
	/**
	 * Invalidate the cache based on cache keys
	 * @param cacheName
	 * @param keys
	 */
	void invalidate(String cacheName,Object key);
	/**
	 * Invalidate all entries in the named cache
	 * @param cacheName
	 */
	void invalidateAll(String cacheName);
	/**
	 * perform caching on the call encapsulated by callback
	 * @param cacheName
	 * @param keys
	 * @param callback
	 * @return
	 */
	Object execute(String cacheName, Object key, CacheCallback callback);
	/**
         * write result of annotated method to cache
         * @param cacheName
         * @param keys
         * @param value 
         */
	void update(String cacheName, Object key, Object value);
}
