/*
 * z2-Environment
 * 
 * Copyright(c) ZFabrik Software GmbH & Co. KG
 * 
 * contact@zfabrik.de
 * 
 * http://www.z2-environment.eu
 */
package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
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
	private Label modified = new Label("&nbsp;",ContentMode.HTML);

	public ThingiesView(UI application) {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		final ThingiesTable t = new ThingiesTable(this);
		addComponent(t);
		setExpandRatio(t, 1);
		
		// add some buttons
		HorizontalLayout buttons = new HorizontalLayout(){{
			setSpacing(true);
			addComponent(new Button("add") {{
				addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.addNewThingy();
					}
				});
			}});
			addComponent(new Button("delete selected") {{
				addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.deleteSelectedThingies();
					}
				});
			}});
			addComponent(new Button("save changes") {{
				addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.storeThingyUpdates();
					}
				});
			}});
			addComponent(new Button("discard & reload") {{
				addClickListener(new ClickListener() {
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
		this.modified.setValue(modified? "There are non-persistent modifications":"&nbsp;");
	}
	
}
