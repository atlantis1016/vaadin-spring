package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import java.util.Set;

import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryView;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;
import org.vaadin.addons.lazyquerycontainer.QueryItemStatus;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;

/**
 * A Vaadin table of thingies. It is backed by a lazy query container from the add-on of the same name
 * (see <a href="https://vaadin.com/directory#addon/lazy-query-container">https://vaadin.com/directory#addon/lazy-query-container</a>).
 *  
 * @author hb
 *
 */
@SuppressWarnings("serial")
public class ThingiesTable extends Table {
	private LazyQueryContainer data;

	public ThingiesTable(final ThingiesView thingiesView) {
		setSizeFull();
		
		setEditable(true);
		setSelectable(true);
		setMultiSelect(true);
		
		this.data = new LazyQueryContainer(
			// we use an inline query factory
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
		
		// need to declare the actual fields nevertheless
		this.data.addContainerProperty("id", Integer.class, null);
		this.data.addContainerProperty("name", String.class, null);
		
		this.data.addListener(new ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				// tell the view about our modification status
				thingiesView.setModified(data.isModified());
			}
		});
		setVisibleColumns(new String[]{"id","name"});
	}

	// overwrite some visualization to represented pending removed rows appropriately
	@Override
	protected void bindPropertyToField(Object rowId, Object colId,Property property, Field field) {
		Item i = getItem(rowId);
		if (i!=null) {
			// disabled fields for deleted items
			QueryItemStatus status = (QueryItemStatus) i.getItemProperty(LazyQueryView.PROPERTY_ID_ITEM_STATUS).getValue();
			field.setEnabled(QueryItemStatus.Removed!=status);
		}
		super.bindPropertyToField(rowId, colId, property, field);
	}
	
	// called from view
	public void storeThingyUpdates() {
		this.commit();
		this.data.commit();
	}

	// called from view
	public void discardThingyUpdates() {
		this.discard();
		this.data.discard();
	}

	// called from view
	@SuppressWarnings("unchecked")
	public void deleteSelectedThingies() {
		Set<Object> selected = (Set<Object>) this.getValue();
		if (selected!=null && !selected.isEmpty()) {
			for (Object id : selected) {
				this.removeItem(id);
			}
		}
	}

	// called from view
	public void addNewThingy() {
		this.data.addItem();
	}

	// called from view
	public void reload() {
		this.data.refresh();
	}
 
}
