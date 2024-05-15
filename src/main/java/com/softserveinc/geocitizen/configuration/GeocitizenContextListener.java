package com.softserveinc.geocitizen.configuration;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeocitizenContextListener implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(GeocitizenContextListener.class);
	

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.error("Context Initialized!!!!");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	
		try {
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while(drivers.hasMoreElements()) {
				Driver driver = drivers.nextElement();
				if (driver != null) {
					DriverManager.deregisterDriver(driver);	
				}
			}	
		} catch (Exception e) {
			LOG.error(e.getMessage());
 		}
	
	}
}
