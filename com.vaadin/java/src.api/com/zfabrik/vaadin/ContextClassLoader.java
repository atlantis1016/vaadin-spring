/*
 * z2-Environment
 * 
 * Copyright(c) 2010, 2011, 2012 ZFabrik Software KG
 * 
 * contact@zfabrik.de
 * 
 * http://www.z2-environment.eu
 */
package com.zfabrik.vaadin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * A delegating class loader. Vaadin doesn't use the context class loader (as of 6.x) 
 * but supports custom class loaders.
 * <p>
 * So, in order to load your application class by Vaadin but from a referencing module
 * (i.e. one Vaadin doesn't see), you may use this classloader by declaring it in the 
 * web.xml.
 * <p>
 * See also <a href="http://dev.vaadin.com/ticket/9809">Enhancement #9809</a>
 * 
 * @author hb
 *
 */
public class ContextClassLoader extends ClassLoader {
	private ClassLoader targetLoader;
	
	public ContextClassLoader(ClassLoader p) {
		this.targetLoader = Thread.currentThread().getContextClassLoader();
		if (this.targetLoader==null) {
			throw new IllegalStateException("Cannot set up custom class loader: No context class loader set");
		}
	}

	public URL getResource(String name) {
		return targetLoader.getResource(name);
	}

	public InputStream getResourceAsStream(String name) {
		return targetLoader.getResourceAsStream(name);
	}

	public Enumeration<URL> getResources(String name) throws IOException {
		return targetLoader.getResources(name);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return targetLoader.loadClass(name);
	}
	
	
}
