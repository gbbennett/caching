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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

import net.spy.memcached.ConnectionObserver;
import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author gary bennett
 * 
 * Implementation of cache based on memcached
 */
public class MemcachedBasedCache implements CacheInterface {
    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedBasedCache.class);
    public static final int DEFAULT_TIMEOUT = 3600;
    private int defaultTimeout = DEFAULT_TIMEOUT;
    private Map<String, Integer> cacheSeconds;

    private MemcachedClient client;
    private List<MemcachedServer> servers;
    
    public MemcachedBasedCache(){
        super();
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }
   
    public Map<String, Integer> getCacheSeconds() {
        return this.cacheSeconds;
    }

    public void setCacheSeconds(final Map<String, Integer> cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }
    public void invalidate(String cacheName, Object key) {

        
        try {
            MemcachedClient c = openClient();
            String mckey = getKey(c, cacheName, key);
            c.delete(mckey);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
    }

    public void invalidateAll(String cacheName) {
        try {
            MemcachedClient c = openClient();
            incrementCacheIndex(c, cacheName);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
    }

    public Object execute(String cacheName, Object key, CacheCallback callback) {
        Object ret = get(cacheName, key);
        if(ret == null){
                ret = callback.execute();
               
                put(cacheName, key, ret);
        }
    	return ret;
    }
    public void update(String cacheName, Object key, Object value) {
               
        put(cacheName, key, value);
    }    
    private String getKey(MemcachedClient c, String cacheName, Object key){
        StringBuffer buffer = new StringBuffer();
        
        Integer index = getCacheIndex(c,cacheName);
        buffer.append(cacheName);
        buffer.append(".");
        buffer.append(index.intValue());
        buffer.append(key);
       
        return buffer.toString();
    }
    private Integer getCacheIndex(MemcachedClient c,String cacheName){
        Integer index = (Integer)c.get(cacheName);
        if(index==null){
            index = new Integer(0);
            put(c, cacheName,cacheName,index);
        }
        return index;
    }
    private void incrementCacheIndex(MemcachedClient c,String cacheName){
        Integer index = getCacheIndex(c,cacheName);
        put(c, cacheName,cacheName,new Integer(index.intValue()+1));
    }
    /**
     * get value from cache
     * @param cacheName name of cache
     * @param keys parameters forming keys
     * @return cached result
     */
    public Object get(String cacheName, Object key) {
        Object value = null;
        
        try {
            MemcachedClient c = openClient();
            String mckey = getKey(c, cacheName, key);
           
            value = c.get(mckey);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
        
        
        return value;
    }
    /**
     * set value in cache
     * @param cacheName name of cache
     * @param keys parameters forming keys
     * @param obj what to cache
     */
    public void put(String cacheName, Object key, Object obj) {
        try {
            MemcachedClient c = openClient();
            String mckey = getKey(c, cacheName, key);
            put(c, cacheName,mckey,obj);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
    }    
    private void put(MemcachedClient c, String cacheName, String key, Object obj) {
        Integer timeout = null;
        
        try{
            timeout = getCacheSeconds().get(cacheName);
        } catch(Exception e){
            
        }
        c.set(key, (timeout==null)?defaultTimeout:timeout.intValue(), obj);
    }
    private MemcachedClient openClient() throws IOException{
        if(client==null){
            InetSocketAddress addresses[] = 
                    new InetSocketAddress[servers.size()];
            int i = 0;
            for(MemcachedServer server : servers){
                addresses[i++] = new InetSocketAddress(
                        server.getServer(), server.getPort());
            }
            client=new MemcachedClient(
                addresses);
            
            client.addObserver(new ConnectionObserver() {

                public void connectionLost(SocketAddress sa) {
                    client.removeObserver(this);
                    client = null;
                }

                public void connectionEstablished(SocketAddress sa,
                        int reconnectCount) {
                }
            });
        }
        return client;
    }
}
