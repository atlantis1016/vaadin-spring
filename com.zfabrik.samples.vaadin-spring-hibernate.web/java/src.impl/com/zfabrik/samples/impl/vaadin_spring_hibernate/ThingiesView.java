package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ThingiesView extends VerticalLayout {
	private Label modified = new Label();

	public ThingiesView() {
		setSizeFull();
		setMargin(false);
		setSpacing(true);
		
		final ThingiesTable t = new ThingiesTable(this);
		addComponent(t);
		setExpandRatio(t, 1);
		
		// add some buttons
		Component buttons = new HorizontalLayout(){{
			setSpacing(true);
			addComponent(new Button("new") {{
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
			addComponent(new Button("discard changes") {{
				addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.discardThingyUpdates();
					}
				});
			}});
			addComponent(new Button("reload") {{
				addListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						t.reload();
					}
				});
			}});
			addComponent(modified);
			setComponentAlignment(modified, Alignment.MIDDLE_RIGHT);
		}};
		
		addComponent(buttons);
		setExpandRatio(buttons, 0);
	}
	
	// update modified state
	public void setModified(boolean modified) {
		this.modified.setValue(modified? "Press \"save changes\" to persist or \"discard changes\" to discard pending modifications":"");
	}
	
}
