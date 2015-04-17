/**
 * 
 */
package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hybris.osh.facades.customer.GuestCustomerFacade;
import com.hybris.osh.facades.user.data.OshRegisterData;
import com.hybris.osh.storefront.controllers.pages.checkout.CheckoutLoginController;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.LoginForm;
import com.hybris.osh.storefront.forms.RegisterForm;


/**
 * 
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login/checkout/registerGuest")
public class GuestCheckoutLoginPageController extends CheckoutLoginController
{
	@Autowired
	private GuestCustomerFacade guestCustomerFacade;
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public String doCheckoutAsGuest(@Valid final RegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{

		if (userService.getCurrentUser().getName().equalsIgnoreCase("Guest User"))
		{
			return REDIRECT_PREFIX + "/checkout/single";
		}

		return processGuestRequest(form, model, request, response);
	}

	/**
	 * This method create a account for guest user. for this account the login is disabled after auto login. so using
	 * this account user can checkout only once.
	 * 
	 * @param form
	 *           RegisterForm
	 * @param model
	 *           Model
	 * @param request
	 *           HttpServletRequest
	 * @param response
	 *           HttpServletResponse
	 * @return true after successful creation of account and login for guest.
	 * @throws CMSItemNotFoundException
	 */
	protected String processGuestRequest(final RegisterForm form, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final OshRegisterData data = new OshRegisterData();
		if (getGuestCustomerFacade().isUserExist(form.getEmail()))
		{
			storeCmsPageInModel(model, getCmsPage());
			model.addAttribute(new LoginForm());
			GlobalMessages.addErrorMessage(model, "form.user.exist.cannot.guest");
			return getView();
		}
		else
		{
			data.setEmail(form.getEmail());
		}
		try
		{
			final String randomUid = getGuestCustomerFacade().generateUid();
			final String myStore = (String) request.getSession().getAttribute("storeName");

			data.setLogin(randomUid);
			data.setPassword(randomUid);
			getGuestCustomerFacade().registerGuest(data, myStore);
			getAutoLoginStrategy().login(randomUid, randomUid, request, response);

		}
		catch (final DuplicateUidException e)
		{
			LOG.warn("registration failed: " + e);
			model.addAttribute(new LoginForm());
			GlobalMessages.addErrorMessage(model, "registration.error.account.exists.title");
			return handleRegistrationError(model);
		}
		catch (final Exception e)
		{
			LOG.warn("registration failed: " + e);
			model.addAttribute(new LoginForm());
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return handleRegistrationError(model);
		}

		return REDIRECT_PREFIX + getSuccessRedirect(request, response);
	}

	private String handleRegistrationError(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		return getView();
	}

	/**
	 * @return the guestCustomerFacade
	 */
	public GuestCustomerFacade getGuestCustomerFacade()
	{
		return guestCustomerFacade;
	}

}
