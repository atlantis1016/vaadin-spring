/*
 * z2-Environment
 * 
 * Copyright(c) ZFabrik Software GmbH & Co. KG
 * 
 * contact@zfabrik.de
 * 
 * http://www.z2-environment.eu
 */
package com.zfabrik.samples.vaadin_spring_hibernate.impl.thingies;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.zfabrik.samples.vaadin_spring_hibernate.thingies.Thingy;
import com.zfabrik.samples.vaadin_spring_hibernate.thingies.ThingyRepository;


/**
 * A repository of "thingies".
 * 
 * @author hb
 */
@Repository("thingyRepository")
public class ThingyRepositoryImpl implements ThingyRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void store(Thingy thingy) {
		this.em.persist(thingy);
	}
	
	@Override
	public Thingy findById(int id) {
		return this.em.find(Thingy.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Thingy> findAll() {
		return this.em.createQuery("select t from Thingy t").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Thingy> findInRange(int first, int count, boolean asc) {
		Query q = this.em.createQuery("select t from Thingy t order by t.name "+(asc? "asc":"desc"));
		q.setFirstResult(first);
		q.setMaxResults(count);
		return q.getResultList();
	}
	
	@Override
	public int getCount() {
		return ((Number) this.em.createQuery("select count(t.id) from Thingy t").getSingleResult()).intValue();
	}
	
	@Override
	public void delete(int id) {
		Thingy t = this.em.find(Thingy.class, id);
		if (t != null) {
			this.em.remove(t);
		}
	}
}
