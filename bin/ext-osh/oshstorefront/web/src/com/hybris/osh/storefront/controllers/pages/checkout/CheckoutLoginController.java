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
package com.hybris.osh.storefront.controllers.pages.checkout;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import com.hybris.osh.core.enums.CheckoutFlowEnum;
import com.hybris.osh.facades.flow.CheckoutFlowFacade;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.pages.AbstractLoginPageController;
import com.hybris.osh.storefront.forms.RegisterForm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Checkout Login Controller. Handles login and register for the checkout flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login/checkout")
public class CheckoutLoginController extends AbstractLoginPageController
{
	@Resource(name = "checkoutFlowFacade")
	private CheckoutFlowFacade checkoutFlowFacade;

	protected CheckoutFlowFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	protected String getCheckoutUrl()
	{
		final CheckoutFlowEnum checkoutFlow = getCheckoutFlowFacade().getCheckoutFlow();
		if (CheckoutFlowEnum.SINGLE.equals(checkoutFlow))
		{
			return "/checkout/single";
		}

		// Default to the multi-step checkout
		return "/checkout/multi";
	}

	/**
	 * Checks if there are any items in the cart.
	 * 
	 * @return returns true if items found in cart.
	 */
	protected boolean hasItemsInCart()
	{
		final CartData cartData = getCheckoutFlowFacade().getCheckoutCart();

		return (cartData.getEntries() != null && !cartData.getEntries().isEmpty());
	}

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Checkout.CheckoutLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (hasItemsInCart())
		{
			return getCheckoutUrl();
		}
		//Redirect to the main checkout controller to handle checkout.
		return "/checkout";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("checkout-login");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutLogin(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model) throws CMSItemNotFoundException
	{
		return getDefaultLoginPage(loginError, session, model);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doCheckoutRegister(@Valid final RegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		return processRegisterUserRequest(null, form, bindingResult, model, request, response);
	}
}
