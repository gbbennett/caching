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



import java.util.Arrays;


/**
 * Default implementation of a key maker
 * @author Gary Bennett
 *
 */

public class StringKeyMaker implements KeyMaker {

	private static final String DEFAULT_KEY = "null";
	private static final int MAXLEN = 240;

	/* (non-Javadoc)
	 * @see uk.ltd.woodsideconsultancy.aop.cache.KeyMaker#generate(java.lang.Object)
	 */
	public Object generate(Object[] params) {
		String  sKey = DEFAULT_KEY;
		if(params !=null){
			StringBuilder generator = new StringBuilder();
			for(Object param : params){
				generator.append(param.getClass().getSimpleName());
				generator.append('[');
				Object obj[] = {param};
				generator.append(Arrays.deepToString(obj));
				generator.append(']');
			}
			sKey = generator.toString();
			if(sKey.length()>MAXLEN){
				Integer hash = new Integer(sKey.hashCode());
				generator.setLength(MAXLEN);
				generator.append(Integer.toHexString(hash));
				sKey = generator.toString();
			}
		}
		
		return sKey;
	}



}
