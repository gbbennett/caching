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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * @author Gary Bennett
 * HashMap limited by size and age
 */
public class SizedMap<Key, TSE> extends LinkedHashMap<Key, TSE> {


    /** 
     * Max number of caches alive
     */
    private int maxEntries = 0;
    /**
     * Max time a cache entry can stay alive
     */
    private long maxDuration = 0; 
    /** One second */
    private static long MILLIS = 1000L;
    /**
     * Constructor
     */
    public SizedMap(){
        super();
    }
    /**
     * Max number of entries 
     * @return number of entries 
     */
    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Max duration in seconds
     * @param maxDuration duration in seconds
     */
    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }
    /**
     * Should oldest entry be deleted
     * @param eldest
     * @return 
     */
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return (((maxEntries>0) && (size() > maxEntries)) || expired(eldest));
    }
    public TSE get(Object key) {
        
        TSE tse = super.get(key);
        if(dateExpired((TimeStampedEntry)tse)){
            tse = null;
        }
        return tse;
    }
    /**
     * Has entry expired
     * @param eldest
     * @return 
     */
    private boolean expired(Map.Entry eldest){
        if(eldest==null){
            return false;
        }
        TimeStampedEntry entry = (TimeStampedEntry)eldest.getValue();

        return dateExpired(entry);
    }
    /**
     * Check if date expired
     * @param entry
     * @return 
     */
    private boolean dateExpired(TimeStampedEntry entry){
        if(entry==null){
            return false;
        }
        if(maxDuration==0){
        	return false;
        }
        return(((new Date().getTime())-entry.getTimeStamp())/MILLIS>maxDuration);
    }
}
