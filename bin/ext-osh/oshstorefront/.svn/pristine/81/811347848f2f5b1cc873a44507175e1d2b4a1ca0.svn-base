package com.hybris.osh.storefront.controllers.cms;

import de.hybris.platform.cms2lib.model.components.BannerComponentModel;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.osh.core.model.OshFooterComponentModel;
import com.hybris.osh.core.model.OshImageTextComponentModel;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.forms.CheetahMailSubscriberForm;


@Controller("OshFooterComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.OshFooterComponent)
public class OshFooterComponentController extends AbstractCMSComponentController<OshFooterComponentModel>
{


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final OshFooterComponentModel component)
	{
		final Collection<OshImageTextComponentModel> oshFooterImageText = component.getOshFooterLogoText();
		if (!oshFooterImageText.isEmpty())
		{

			model.addAttribute("oshFooterImageText", oshFooterImageText);

		}
		final Collection<BannerComponentModel> banners = component.getBanners();
		model.addAttribute("banners", banners);
		model.addAttribute("cheetahMailForm", new CheetahMailSubscriberForm());
	}
}