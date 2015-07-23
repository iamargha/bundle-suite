package com.mhe.bundle.web.servlet;

import javax.servlet.ServletContextEvent;


import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



public class BundleContextLoaderListener extends org.jboss.resteasy.plugins.spring.SpringContextLoaderListener {
	

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	    
  	}
}
