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

/**
 *
 * @author Gary Bennett
 * Inner class holding object with time stamp
 */
public class TimeStampedEntry<T> {
    /** time added milliseconds */
    private long timeStamp;
    /** the object */
    private T obj;
    /**
     * Constructor
     * @param obj enclosed object 
     */
    public TimeStampedEntry(T obj){
        timeStamp = new Date().getTime();
        this.obj = obj;
    }
    /**
     * retrieve enclosed object
     * @return 
     */
    public T getObject(){
        return obj;
    }
    /**
     * time object added
     * @return 
     */
    public long getTimeStamp(){
        return timeStamp;
    }
}
