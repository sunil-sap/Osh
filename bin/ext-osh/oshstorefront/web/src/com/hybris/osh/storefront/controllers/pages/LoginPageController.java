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
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hybris.osh.facades.customer.OshCustomerFacade;
import com.hybris.osh.facades.customer.data.OshCustomerData;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.RegisterForm;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController
{

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "customerFacade")
	private OshCustomerFacade customerFacade;

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{

		/*
		 * if (httpSessionRequestCache.getRequest(request, response) != null) { return
		 * httpSessionRequestCache.getRequest(request, response).getRedirectUrl(); }
		 */

		return "/my-account";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("login");
	}

	private HttpSessionRequestCache httpSessionRequestCache;

	@Autowired
	@Qualifier("httpSessionRequestCache")
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer,
			@RequestParam(value = "error", defaultValue = "false") final boolean loginError, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException
	{
		if (session.getAttribute("passwordRequest") != null && session.getAttribute("passwordRequest").equals(Boolean.TRUE))
		{
			GlobalMessages.addInfoMessage(model, "account.confirmation.forgotten.password.link.sent");
			session.removeAttribute("passwordRequest");
		}
		if (!loginError)
		{
			storeReferer(referer, request, response);
		}
		return getDefaultLoginPage(loginError, session, model);
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@RequestHeader(value = "referer", required = false) final String referer,
			@Valid final RegisterForm form, final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		return processRegisterUserRequest(referer, form, bindingResult, model, request, response);
	}


	@RequestMapping(value = "/easyRegister.json", method = RequestMethod.POST, produces = "application/json")
	public String doEasyRegister(@RequestHeader(value = "referer", required = false) final String referer,
			@Valid final RegisterForm form, final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final RedirectAttributes redirectAttributes, final HttpServletResponse response) throws CMSItemNotFoundException,
			TokenInvalidatedException
	{
		return processEasyRegisterUserRequest(form, bindingResult, request, response, redirectAttributes);
	}

	protected String processEasyRegisterUserRequest(final RegisterForm form, final BindingResult bindingResult,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, TokenInvalidatedException
	{

		if (bindingResult.hasErrors())
		{
			return "form.global.error";
		}
		else
		{
			if (!form.getPwd().equals(form.getCheckPwd()))
			{
				redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
						Collections.singletonList("Password doesn't Match"));
				return "redirect:/checkout/orderConfirmation/" + form.getOrderCode();
			}

			final OshCustomerData customerData = customerFacade.getCurrentCustomer();
			final String token = customerFacade.getPasswordToken(customerData.getUid());

			customerData.setFirstName(form.getFirstName());
			customerData.setLastName(form.getLastName());
			try
			{

				customerFacade.updateProfile(customerData);
				if (customerFacade.getCurrentCustomer().getDefaultBillingAddress() != null)
				{
					customerFacade.getCurrentCustomer().getDefaultBillingAddress().setBillingAddress(true);
				}
				if (customerFacade.getCurrentCustomer().getDefaultShippingAddress() != null)
				{
					customerFacade.getCurrentCustomer().getDefaultShippingAddress().setShippingAddress(true);
				}
				customerFacade.changeUserGroup("customergroup");
				customerFacade.updatePassword(token, form.getPwd());
				customerFacade.changeUid(form.getEmail(), form.getPwd());
				if (form.getEmail() != null)
				{
					try
					{
						customerData.setUid(form.getEmail());
						final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
						final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
								customerFacade.getCurrentCustomer().getUid().toLowerCase(), null, oldAuthentication.getAuthorities());
						newAuthentication.setDetails(oldAuthentication.getDetails());
						SecurityContextHolder.getContext().setAuthentication(newAuthentication);

						getAutoLoginStrategy().login(form.getEmail(), form.getPwd(), request, response);
					}
					catch (final PasswordMismatchException passwordMismatchException)
					{
						redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
								Collections.singletonList("Password doesn't Match"));
						return "redirect:/checkout/orderConfirmation/" + form.getOrderCode();

					}
				}

				return "redirect:/my-account";
			}
			catch (final DuplicateUidException e)
			{
				redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
						Collections.singletonList("Email ID already exists"));
				return "redirect:/checkout/orderConfirmation/" + form.getOrderCode();
			}
			catch (final PasswordMismatchException localException)
			{
				redirectAttributes.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
						Collections.singletonList("Password doesn't Match"));
				return "redirect:/checkout/orderConfirmation/" + form.getOrderCode();
			}
		}
	}



}
