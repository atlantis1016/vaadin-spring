package com.zfabrik.samples.impl.vaadin_spring_hibernate;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.vaadin.Application;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Vaadin's main application class. Builds the main window. Takes care of global error handling.
 * 
 * @author hb
 *
 */
@SuppressWarnings("serial")
public class ApplicationImpl extends Application implements HttpServletRequestListener {
	// keep the current application instance on the thread
	private static ThreadLocal<ApplicationImpl> current = new ThreadLocal<ApplicationImpl>();
	
	@Override
	public void init() {
		current.set(this);
		setTheme("sample");
		setMainWindow(new Window() {{
			setCaption("Vaadin-Hibernate-Spring Sample");
			setSizeFull();
			// set up a main layout 
			setContent(
				new VerticalLayout() {{
					addStyleName("sample-main");
					setMargin(false);
					setSpacing(false);
					setSizeFull();
					
					Component header = new HorizontalLayout() {{
						addStyleName("sample-main-header");
						setHeight("40px");
						addComponent(new Label("Vaadin-Spring-Hibernate Thingies"));
					}};
					addComponent(header);
					setExpandRatio(header, 0);
					
					Component main = new Panel() {{
						setSizeFull();
						getContent().setSizeFull();
						addComponent(new ThingiesView());
					}};
					addComponent(main);
					setExpandRatio(main, 1);
					
					Component footer = new HorizontalLayout() {{
						addStyleName("sample-main-footer");
						addComponent(new Label("(c) 2012 ZFabrik Software KG"));
					}};
					addComponent(footer);
					setExpandRatio(footer, 0);
				}
			});
			
		}});
	}
	
	// unhandled errors end up here
	public void terminalError(Terminal.ErrorEvent event) {
		// roll back the current tx
		try {
			UserTransaction ut = getUserTransaction();
			ut.setRollbackOnly();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Failed to set roll back only",e);
			throw new RuntimeException("Failed to set roll back only",e);
		}
		
	    if (getMainWindow() != null) {
	        getMainWindow().showNotification(
                "Sorry! An application error occurred and was logged.",
                "("+event.getThrowable().getMessage()+")",
                Notification.TYPE_ERROR_MESSAGE);
	    }
	    logger.log(Level.WARNING,"Application Error",event.getThrowable());
	}

	private UserTransaction getUserTransaction() throws NamingException {
		UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		return ut;
	}

	// track current 
	@Override
	public void onRequestStart(HttpServletRequest request,HttpServletResponse response) {
		if (request.getLocale()!=null) {
			// set the app's locale
			this.setLocale(request.getLocale());
		}
	}	

	@Override
	public void onRequestEnd(HttpServletRequest request,HttpServletResponse response) {}
	
	private final static Logger logger = Logger.getLogger(ApplicationImpl.class.getName());

}
