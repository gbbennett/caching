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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.JoinPoint;
import uk.ltd.woodsideconsultancy.aop.cache.annotations.CacheKey;

/**
 * Utility class to retrieve keys from method signature
 * These are either the array of parameters
 * or only those with a CacheKey annotation
 * @author garybennett
 *
 */
public class CacheKeyFinder {
	/**
	 * formaters format the parameters to define keys
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, Formater> formaters = new HashMap<String, Formater>();
	static {
		formaters.put("date", new Formater<Date,String>(){

			public String format(Date date, String format) {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				return sdf.format(date);
			}});
	}
	public static void addFormater(String name, @SuppressWarnings("rawtypes") Formater formater){
		formaters.put(name, formater);
	}
	/**
	 * determine keys to use for cache storage based on joinpoint
	 * these are either the parameters for the method or only those
	 * with a CacheKey annotation
	 * @param joinPoint
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object[] getKeys(JoinPoint joinPoint){
		Object keys[] = joinPoint.getArgs();
		MethodSignature signature =(MethodSignature) joinPoint.getSignature();
		Object target = joinPoint.getTarget();
		
    	String methodName = signature.getMethod().getName();
    	Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
    	try {
    		List<Object> keyList = new ArrayList<Object>();
    		int paramIdx = 0;
    		Annotation[][] annotations = target.getClass().getMethod(methodName,parameterTypes).getParameterAnnotations();
    		for(Annotation[] anos : annotations){
    			for(Annotation ano : anos){
    				if(ano.annotationType().equals(CacheKey.class)){
    					CacheKey sk = (CacheKey)ano;
    					@SuppressWarnings("rawtypes")
						Formater formater = formaters.get(sk.style());
    					if(formater != null){
    						keyList.add(formater.format(keys[paramIdx],sk.format()));
    					} else {
    						keyList.add(keys[paramIdx]);
    					}
    				}
    			}
    			paramIdx++;
    		}
    		if(keyList.size()>0){
    			keys = keyList.toArray();
    		}
    	} catch (NoSuchMethodException nsme){
    		
    	}

		return keys;
	}

}
