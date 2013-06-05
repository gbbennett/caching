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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class BaseAspectAccess<T> {
	protected T constructObject(Class clazz) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Constructor c = clazz.getDeclaredConstructor();
		c.setAccessible(true);
		return (T)c.newInstance();
	}
	protected Method getAround(Class clazz, Class typeClass){
		Method ms[] = clazz.getDeclaredMethods();
		for(Method mt : ms){
			if(mt.getName().indexOf("around")!= -1){
				Type types[] = mt.getGenericParameterTypes();
				if(types.length==3){
					if(types[0].toString().equals(typeClass.toString())){
						return mt;
					}	
				}
			}
			
		}
		return null;
	}
	protected Method getBefore(Class clazz, Class typeClass){
		
		Method ms[] = clazz.getDeclaredMethods();
		for(Method mt : ms){
		
			if(mt.getName().indexOf("before")!= -1){
				Type types[] = mt.getGenericParameterTypes();
				if(types.length==2){
					
					if(types[0].toString().equals(typeClass.toString())){
						return mt;
					}	
				}
			}
			
		}
		return null;
	}
	protected Method getAfter(Class clazz, Class typeClass){
		Method ms[] = clazz.getDeclaredMethods();
		for(Method mt : ms){
			if(mt.getName().indexOf("after")!= -1){
				Type types[] = mt.getGenericParameterTypes();
				if(types.length==2){
					if(types[0].toString().equals(typeClass.toString())){
						return mt;
					}	
				}
			}
			
		}
		return null;
	}
}
