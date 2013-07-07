/*
 * z2-Environment
 * 
 * Copyright(c) 2010, 2011, 2012 ZFabrik Software KG
 * 
 * contact@zfabrik.de
 * 
 * http://www.z2-environment.eu
 */
package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.zfabrik.vaadin.ExtensionComponentsUtil;

/**
 * A view showing thingies in a table and offering some modifying actions
 * 
 * @author hb
 */
@SuppressWarnings("serial")
public class ThingiesView extends VerticalLayout {

	// a label indicating pending modifications
	private Label modified = new Label();

	public ThingiesView(Application application) {
		setSizeFull();
		setMargin(false);
		setSpacing(true);
		
		final ThingiesTable t = new ThingiesTable(this);
		addComponent(t);
		setExpandRatio(t, 1);
		
		// add some buttons
		HorizontalLayout buttons = new HorizontalLayout(){{
			setSpacing(true);
			addComponent(new Button("add") {{
				addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.addNewThingy();
					}
				});
			}});
			addComponent(new Button("delete selected") {{
				addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.deleteSelectedThingies();
					}
				});
			}});
			addComponent(new Button("save changes") {{
				addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.storeThingyUpdates();
					}
				});
			}});
			addComponent(new Button("discard & reload") {{
				addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.reload();
					}
				});
			}});
		}};
		
		// find extensions and add them to the button row
		for (Component c : ExtensionComponentsUtil.getComponentyByExtensionForApplication(application,"com.zfabrik.samples.vaadin_spring_hibernate.actions")) {
		   // and add them to this.
		   buttons.addComponent(c);
		}

		// fill the main layout
		addComponent(buttons);
		// add the modification label
		addComponent(modified);
		setComponentAlignment(modified, Alignment.MIDDLE_RIGHT);
		setExpandRatio(buttons, 0);
	}
	
	// update modified state
	public void setModified(boolean modified) {
		this.modified.setValue(modified? "There are non-persistent modifications":"");
	}
	
}
