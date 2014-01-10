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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Vaadin's main application class. Builds the main window. Takes care of global error handling.
 * 
 * @author hb
 *
 */
@SuppressWarnings("serial")
@Theme("sample")
@Title("Vaadin-Hibernate-Spring Sample")
public class ApplicationImpl extends UI {
	
	@Override
	protected void init(VaadinRequest request) {
		
		if (request.getLocale()!=null) {
			// set the app's locale
			this.setLocale(request.getLocale());
		}
		
		UI.getCurrent().setErrorHandler(new DefaultErrorHandler(){
			public void error(com.vaadin.server.ErrorEvent event) {
				try {
					UserTransaction ut = getUserTransaction();
					ut.setRollbackOnly();
					
				} catch (Exception e) {
					logger.log(Level.SEVERE,"Failed to set roll back only",e);
					throw new RuntimeException("Failed to set roll back only",e);
				}
				
				Notification.show(
		                "Sorry! An application error occurred and was logged.",
		                "("+event.getThrowable().getMessage()+")",
		                Notification.Type.ERROR_MESSAGE
		        );
			    
			    logger.log(Level.WARNING,"Application Error",event.getThrowable());
			}
		});
		
		// set up a main layout 
		setContent(getMainLayout());
			
	}
	
	private Component getMainLayout() {
		return 
			new VerticalLayout() {{
				addStyleName("sample-main");
				setMargin(false);
				setSpacing(false);
				setSizeFull();
				
				Component header = new HorizontalLayout() {{
					addStyleName("sample-main-header");
					setHeight("60px");
					addComponent(new Label("Vaadin-Spring-Hibernate Thingies"));
				}};
				addComponent(header);
				setExpandRatio(header, 0);
				
				Component main = new Panel() {{
					setSizeFull();
					setContent(new ThingiesView(ApplicationImpl.this));
					getContent().setSizeFull();
					
				}};
				addComponent(main);
				setExpandRatio(main, 1);
				
				Component footer = new HorizontalLayout() {{
					addStyleName("sample-main-footer");
					addComponent(new Label("(c) 2013 ZFabrik Software KG"));
				}};
				addComponent(footer);
				setExpandRatio(footer, 0);
			}};
	}
	

	private UserTransaction getUserTransaction() throws NamingException {
		UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		return ut;
	}

	private final static Logger logger = Logger.getLogger(ApplicationImpl.class.getName());

}
