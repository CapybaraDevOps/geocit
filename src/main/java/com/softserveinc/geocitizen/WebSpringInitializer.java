/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen;

import com.softserveinc.geocitizen.configuration.AppConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.File;

public class WebSpringInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static final String LOCATION = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
	private static final long MAX_REQUEST_SIZE = MAX_FILE_SIZE * 2;
	private static final int FILE_SIZE_THRESHOLD = 0;

	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setMultipartConfig(getMultipartConfigElement());
	}

	private MultipartConfigElement getMultipartConfigElement() {
		return new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
	}
}
