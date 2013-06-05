package uk.ltd.woodsideconsultancy.test;

import java.io.Serializable;

/**
 * 
 * @author Gary Bennett
 *
 * Simple caching example
 */
public class Country implements Serializable {
	/** name of country */
	private String name;
	/** Constructor */
	public Country(){
		super();
	}
	/**
	 * get name
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * set name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * toString
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		return sb.toString();
	}
}
