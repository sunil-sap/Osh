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
package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.hybris.osh.storefront.breadcrumb.Breadcrumb;
import com.hybris.osh.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.LoginForm;
import com.hybris.osh.storefront.forms.RegisterForm;


/**
 * Abstract base class for login page controllers
 */
public abstract class AbstractLoginPageController extends AbstractRegisterPageController
{
	protected static final String SPRING_SECURITY_LAST_USERNAME = "SPRING_SECURITY_LAST_USERNAME";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;
	@Resource(name = "userService")
	private UserService userService;

	protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model)
			throws CMSItemNotFoundException
	{
		final LoginForm loginForm = new LoginForm();
		model.addAttribute(loginForm);
		model.addAttribute(new RegisterForm());

		final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
		if (username != null)
		{
			session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
		}

		loginForm.setJ_username(username);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		model.addAttribute("metaRobots", "index,no-follow");

		if (session.getAttribute("hasComeFromWishList") != null)
		{

			final Breadcrumb breadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("text.account.wishlist", null,
					getI18nService().getCurrentLocale()), null);
			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			if (!userService.getCurrentUser().getUid().equals("anonymous"))
			{
				breadcrumbs.add(breadcrumbEntry);
				model.addAttribute("breadcrumbs", breadcrumbs);
			}
			else
			{
				model.addAttribute("breadcrumbs", Collections.singletonList(breadcrumbEntry));
			}

			session.removeAttribute("hasComeFromWishList");
		}
		else
		{
			final Breadcrumb breadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
					getI18nService().getCurrentLocale()), null);
			model.addAttribute("breadcrumbs", Collections.singletonList(breadcrumbEntry));
		}


		if (loginError)
		{
			GlobalMessages.addErrorMessage(model, "login.error.account.not.found.title");
		}

		return getView();
	}
}
