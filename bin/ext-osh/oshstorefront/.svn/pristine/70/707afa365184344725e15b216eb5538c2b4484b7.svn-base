/**
 * 
 */
package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hybris.osh.facades.storename.OshStoreNameFacade;
import com.hybris.osh.facades.user.data.ContactUsFormData;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.forms.ContactUsEmailForm;


/**
 * controller for contact us page
 * 
 */


@Controller
@Scope("tenant")
@RequestMapping(value = "/contactUs")
public class ContactUsPageController extends AbstractPageController
{

	private static final String CONTACT_US = "/contactUs";

	@Autowired
	private OshStoreNameFacade oshStoreNameFacade;

	/**
	 * This method will send the point of service data in contact us page
	 */

	protected void getStoreInfo(final Model model)
	{
		final List<PointOfServiceData> posData = oshStoreNameFacade.storeNames();
		if (posData != null)
		{
			model.addAttribute("posData", posData);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	protected String getContactUsForm(final Model model) throws CMSItemNotFoundException
	{

		getStoreInfo(model);

		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTACT_US));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTACT_US));
		return ControllerConstants.Views.Pages.ContactUs.contactUsPage;
	}

	/**
	 * 
	 * Fetching the value of contact us form and forward it on facade using data object
	 */
	@RequestMapping(method = RequestMethod.POST)
	protected String getContactUsForm(final ContactUsEmailForm form, final Model model) throws CMSItemNotFoundException
	{

		final ContactUsFormData contactUsFormData = new ContactUsFormData();

		contactUsFormData.setFirstname(form.getFirstname());
		contactUsFormData.setLastname(form.getLastname());
		contactUsFormData.setSubject(form.getContact_us_subject());
		contactUsFormData.setEmailAddress(form.getEmail());
		contactUsFormData.setStoreName(form.getStore_name());
		contactUsFormData.setEmailContent(form.getEmail_content());

		oshStoreNameFacade.contactUsData(contactUsFormData);

		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTACT_US));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTACT_US));

		getStoreInfo(model);
		return ControllerConstants.Views.Pages.ContactUs.contactUsPage;

	}

}
