package com.zfabrik.samples.impl.vaadin_spring_hibernate.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.transaction.annotation.Transactional;


/**
 * Generic TX demarcation
 * @author hb
 *
 */
public class TransactionFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	@Override
	public void destroy() {}
	
	@Transactional
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,	FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}
	

}
