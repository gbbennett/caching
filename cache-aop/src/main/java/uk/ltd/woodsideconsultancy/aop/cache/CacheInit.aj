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


import java.lang.reflect.Field;

import org.aspectj.lang.reflect.FieldSignature;

import uk.ltd.woodsideconsultancy.aop.cache.annotations.CacheImplementation;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.KeyMakerImplementation;

public aspect CacheInit {
	pointcut setCachingImplementation() : set(@CacheImplementation CacheInterface *.*) && !within(uk.ltd.woodsideconsultancy.aop.cache.*);
	Object around(final CacheImplementation implementation) : setCachingImplementation() && @annotation(implementation){
		Object cacheInterface = proceed(implementation);
		
		Object targetClass = thisJoinPoint.getTarget();
		
		FieldSignature fieldSignature = (FieldSignature)thisJoinPoint.getSignature();
		Field field = fieldSignature.getField();
		
		
		
		try{
			field.setAccessible(true);
			CacheInterface ci = (CacheInterface)field.get(targetClass);
			if(ci != null){
				CacheAdaptor.getInstance().addCachingImplementation(implementation.name(), 
					ci);
			}
		} catch(IllegalAccessException e){
			
		}


		return cacheInterface;
	}
	pointcut setKeyMakerImplementation() : set(@KeyMakerImplementation KeyMaker *.*) && !within(uk.ltd.woodsideconsultancy.aop.cache.*);
	Object around(final KeyMakerImplementation implementation) : setKeyMakerImplementation() && @annotation(implementation){
		Object keyMaker = proceed(implementation);
		
		Object targetClass = thisJoinPoint.getTarget();
		
		FieldSignature fieldSignature = (FieldSignature)thisJoinPoint.getSignature();
		Field field = fieldSignature.getField();
				
		try{
			field.setAccessible(true);
			KeyMaker km = (KeyMaker)field.get(targetClass);
			if(km != null){
				CacheAdaptor.getInstance().addKeyMaker(implementation.name(), 
					km);
			}
		} catch(IllegalAccessException e){
			
		}


		return keyMaker;
	}
}
