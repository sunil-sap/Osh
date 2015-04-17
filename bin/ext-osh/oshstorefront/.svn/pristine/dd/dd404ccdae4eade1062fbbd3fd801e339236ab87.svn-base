package com.hybris.osh.storefront.controllers.misc;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.osh.facades.cheetah.CheetahMailSubscriberFacade;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.pages.AbstractPageController;
import com.hybris.osh.storefront.forms.CheetahMailSubscriberForm;


@Controller
@Scope("tenant")
@RequestMapping(value = "/subscribe")
public class EmailSubscriberController extends AbstractPageController
{
	@Resource(name = "cheetahMailSubscriberFacade")
	private CheetahMailSubscriberFacade cheetahMailSubscriberFacade;

	private static final String Cheetah_Form_PAGE = "/cheetah";

	protected static final Logger LOG = Logger.getLogger(EmailSubscriberController.class);



	@RequestMapping(method = RequestMethod.GET)
	public String subscribeCheetahMail(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("cheetahMailSubscriberForm", new CheetahMailSubscriberForm());
		model.addAttribute("cheetahMailForm", new CheetahMailSubscriberForm());
		storeCmsPageInModel(model, getContentPageForLabelOrId(Cheetah_Form_PAGE));
		return ControllerConstants.Views.Pages.Cheetah.CheetahSubscriberPage;
	}

	@RequestMapping(value = "/cheetah", method = RequestMethod.POST)
	public String setEmaiSubscriber(@Valid final CheetahMailSubscriberForm subscriberForm, final Model model)
			throws CMSItemNotFoundException
	{
		model.addAttribute("emailId", subscriberForm.getEmailId());
		storeCmsPageInModel(model, getContentPageForLabelOrId(Cheetah_Form_PAGE));
		return ControllerConstants.Views.Pages.Cheetah.CheetahSubscriberPage;

	}

	@ResponseBody
	@RequestMapping(value = "/cheetah/mail", method = RequestMethod.POST, produces = "application/json")
	public boolean populateEmailSubscriber(@RequestParam("emailId") final String emailId,
			@RequestParam("subscriber") final boolean subscriber, @RequestParam("postalCode") final String postalCode,
			final Model model)
	{
		if ((emailId != null) && (!emailId.isEmpty()))
		{
			return cheetahMailSubscriberFacade.setSubscriber(emailId, subscriber, postalCode);
		}
		return false;
	}

	@ResponseBody
	@RequestMapping(value = "/cheetah/unsubscribe", method = RequestMethod.POST, produces = "application/json")
	public boolean populateEmailUnSubscriber(@RequestParam("emailId") final String emailId,
			@RequestParam("subscriber") final boolean subscriber, final Model model)
	{
		if ((emailId != null) && (!emailId.isEmpty()))
		{
			return cheetahMailSubscriberFacade.unSubscribeCheetahMail(emailId, subscriber);
		}
		return false;
	}

}
