/**
 * 
 */
package com.hybris.osh.storefront.controllers.cms;

import de.hybris.platform.core.model.media.MediaModel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.osh.core.model.OshImageTextComponentModel;
import com.hybris.osh.storefront.controllers.ControllerConstants;


@Controller("OshTextImageComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.OshImageTextComponent)
public class OshImageTextComponentController extends AbstractCMSComponentController<OshImageTextComponentModel>
{


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final OshImageTextComponentModel component)
	{
		final MediaModel media = component.getMedia();
		final String text = component.getContent();
		model.addAttribute("media", media);
		model.addAttribute("text", text);
	}
}
