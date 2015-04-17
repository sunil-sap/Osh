/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.storefront.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Base controller for all controllers. Provides common functionality for all controllers.
 */
public abstract class AbstractController
{
	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String FORWARD_PREFIX = "forward:";
	public static final String ROOT = "/";

	@ModelAttribute("request")
	public HttpServletRequest addRequestToModel(final HttpServletRequest request)
	{
		return request;
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public static class HttpNotFoundException extends RuntimeException
	{
		/**
		 * Default constructor
		 */
		public HttpNotFoundException()
		{
			super();
		}

		/**
		 * @param message the detail message (which is saved for later retrieval
		 *                by the {@link #getMessage()} method).
		 * @param cause   the cause (which is saved for later retrieval by the
		 *                {@link #getCause()} method).  (A <tt>null</tt> value is
		 *                permitted, and indicates that the cause is nonexistent or
		 *                unknown.)
		 */
		public HttpNotFoundException(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		/**
		 * @param message the detail message. The detail message is saved for
		 *                later retrieval by the {@link #getMessage()} method.
		 */
		public HttpNotFoundException(final String message)
		{
			super(message);
		}

		/**
		 * @param cause the cause (which is saved for later retrieval by the
		 *              {@link #getCause()} method).  (A <tt>null</tt> value is
		 *              permitted, and indicates that the cause is nonexistent or
		 *              unknown.)
		 */
		public HttpNotFoundException(final Throwable cause)
		{
			super(cause);
		}
	}
}
