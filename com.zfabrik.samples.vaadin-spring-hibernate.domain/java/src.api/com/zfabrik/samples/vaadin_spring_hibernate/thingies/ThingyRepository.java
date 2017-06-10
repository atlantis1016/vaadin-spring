/*
 * z2-Environment
 * 
 * Copyright(c) ZFabrik Software GmbH & Co. KG
 * 
 * contact@zfabrik.de
 * 
 * http://www.z2-environment.eu
 */
package com.zfabrik.samples.vaadin_spring_hibernate.thingies;

import java.util.Collection;

/**
 * A repository of thingies 
 * 
 * @author hb
 *
 */
public interface ThingyRepository {

	/**
	 * Store a new thingy
	 */
	void store(Thingy thingy);
	
	/**
	 * Delete a thingy by id
	 */
	void delete(int id);
	
	/**
	 * find a thingy by id
	 */
	Thingy findById(int id);
	
	/**
	 * Simply get all thingies at once
	 */
	Collection<Thingy> findAll();
	
	/**
	 * Find max count thingies matching the filter condition and starting at first. Sort result in ascending
	 * order of name, if asc set to <code>true</code>. Sort in descending order otherwise.
	 */
	Collection<Thingy> findInRange(int first, int count, boolean asc);
	
	/**
	 * How many are there?
	 * @return
	 */
	int getCount();
}
