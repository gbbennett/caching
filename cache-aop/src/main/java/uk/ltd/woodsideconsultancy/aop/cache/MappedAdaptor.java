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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappedAdaptor<T> {
	private Map<String,T> mappedAdaptors = new ConcurrentHashMap<String,T>();
	private T defaultAdaptor;
	private String defaultName;
	
	public MappedAdaptor(String name){
		super();
		defaultName = name;
	}

	public T getDefaultAdaptor() {
		return defaultAdaptor;
	}

	public void setDefaultAdaptor(T defaultAdaptor) {
		this.defaultAdaptor = defaultAdaptor;
	}
	public void setAdaptors(Map<String,T> adaptors){
		mappedAdaptors.clear();
		mappedAdaptors.putAll(adaptors);
	}
	public void addAdaptor(String name, T adaptor){
		if(defaultName.equals(name)){
			setDefaultAdaptor(adaptor);
		} else {
			mappedAdaptors.put(name,adaptor);
		}
	}
	public T getAdaptor(String name){
		T adaptor = null;
	
		if(defaultName.equals(name)){
			adaptor = getDefaultAdaptor();
		} else {
			adaptor = mappedAdaptors.get(name);
		}
		return adaptor;
	}
}
