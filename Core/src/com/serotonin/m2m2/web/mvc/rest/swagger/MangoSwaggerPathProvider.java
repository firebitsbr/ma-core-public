/**
 * Copyright (C) 2017 Infinite Automation Software. All rights reserved.
 *
 */
package com.serotonin.m2m2.web.mvc.rest.swagger;

import springfox.documentation.spring.web.paths.AbstractPathProvider;

/**
 * 
 * @author Terry Packer
 */
public class MangoSwaggerPathProvider extends AbstractPathProvider{

	/* (non-Javadoc)
	 * @see springfox.documentation.spring.web.paths.AbstractPathProvider#applicationPath()
	 */
	@Override
	protected String applicationPath() {
		return "/";
	}

	/* (non-Javadoc)
	 * @see springfox.documentation.spring.web.paths.AbstractPathProvider#getDocumentationPath()
	 */
	@Override
	protected String getDocumentationPath() {
		return "api-docs";
	}

}
