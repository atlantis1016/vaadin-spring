package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.addons.lazyquerycontainer.Query;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.zfabrik.samples.vaadin_spring_hibernate.thingies.Thingy;
import com.zfabrik.samples.vaadin_spring_hibernate.thingies.ThingyRepository;

@Configurable
public class ThingiesQuery implements Query {
	@Autowired
	private ThingyRepository repository;
	private Integer size;
	private boolean asc;
	
	public ThingiesQuery(Object[] sortProperties, boolean[] sortOrder) {
		this.asc = (sortOrder.length>0? sortOrder[0]:true);
	}

	@Override
	public Item constructItem() {
		Thingy thingy = new Thingy();
		thingy.setName("<please enter some name>");
		thingy.setId(-1);
		Item i = new BeanItem<Thingy>(thingy);
		i.getItemProperty("id").setReadOnly(true);
		return i;
	}

	@Override
	public boolean deleteAllItems() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Item> loadItems(int first, int count) {
		Collection<Thingy> ts = this.repository.findInRange(first, count, this.asc);
		LinkedList<Item> r = new LinkedList<Item>();
		for (Thingy t : ts) {
			BeanItem<Thingy> i = new BeanItem<Thingy>(t);
			i.getItemProperty("id").setReadOnly(true);
			r.add(i);
		}
		return r;
	}

	@Override
	public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
		for (Item i : addedItems) {
			Thingy t = new Thingy();
			t.setName(i.getItemProperty("name").getValue().toString());
			this.repository.store(t);
		}
		for (Item i : modifiedItems) {
			Thingy t = this.repository.findById((Integer) i.getItemProperty("id").getValue());
			if (t!=null) {
				t.setName(i.getItemProperty("name").getValue().toString());
			}
		}
		for (Item i : removedItems) {
			this.repository.delete((Integer) i.getItemProperty("id").getValue());
		}
	}

	@Override
	public int size() {
		if (size==null) {
			size = this.repository.getCount();
		}
		return size;
	}

}
