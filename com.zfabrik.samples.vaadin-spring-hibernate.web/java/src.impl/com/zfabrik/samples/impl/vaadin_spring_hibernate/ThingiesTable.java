package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import java.util.Set;

import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class ThingiesTable extends Table {
	private LazyQueryContainer data;

	public ThingiesTable(final ThingiesView thingiesView) {
		setSizeFull();
		setImmediate(true);
		setEditable(true);
		setSelectable(true);
		setMultiSelect(true);
		this.data = new LazyQueryContainer(
			new QueryFactory() {
				@Override
				public void setQueryDefinition(QueryDefinition arg0) {}
				@Override
				public Query constructQuery(Object[] sortProperties, boolean[] sortOrder) {
					return new ThingiesQuery(sortProperties,sortOrder);
				}
			},
			false,
			100
		);
		setContainerDataSource(this.data);
		this.data.addContainerProperty("id", Integer.class, null);
		this.data.addContainerProperty("name", String.class, null);
		this.data.addListener(new ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				thingiesView.setModified(data.isModified());
			}
		});
		setVisibleColumns(new String[]{"id","name"});
	}
	
	public void storeThingyUpdates() {
		this.commit();
		this.data.commit();
	}

	public void discardThingyUpdates() {
		this.discard();
		this.data.discard();
	}

	@SuppressWarnings("unchecked")
	public void deleteSelectedThingies() {
		Set<Object> selected = (Set<Object>) this.getValue();
		if (selected!=null && !selected.isEmpty()) {
			for (Object id : selected) {
				this.removeItem(id);
			}
		}
	}

	public void addNewThingy() {
		this.data.addItem();
	}

	public void reload() {
		this.data.refresh();
	}
 
}
