package com.zfabrik.samples.impl.vaadin_spring_hibernate.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.zfabrik.samples.vaadin_spring_hibernate.thingies.Thingy;
import com.zfabrik.samples.vaadin_spring_hibernate.thingies.ThingyRepository;

/**
 * A simple extension component (a button in this case) that
 * also interacts with thingies but is otherwise completely
 * independent of the main UI.
 * 
 * @author hb
 */
@Configurable
public class SomethingAboutThingies extends Button {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ThingyRepository thingies;
	
	@SuppressWarnings("serial")
	public SomethingAboutThingies(Application application) {
		setCaption("About these...");
		setIcon(new ClassResource(SomethingAboutThingies.class, "/mimes/information.png", application));
		addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// read all thingies and count
				int sum = 0;
				int cou = 0;
				for (Thingy t : thingies.findAll()) {
					sum += t.getName()!=null? t.getName().length() : 0;
					cou ++;
				}
				// say what we found
				getWindow().showNotification(String.format("Found %d thingies with a total of %d characters", cou,sum));
			}
		});
	}
	
}
