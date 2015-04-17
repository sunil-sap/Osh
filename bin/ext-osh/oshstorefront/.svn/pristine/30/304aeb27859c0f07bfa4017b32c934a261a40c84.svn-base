/**
 * 
 */
package com.hybris.osh.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.osh.core.model.OshRotatingImagesComponentModel;
import com.hybris.osh.storefront.controllers.ControllerConstants;


@Controller("OshRotatingImagesComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.OshRotatingImagesComponent)
public class OshRotatingImagesComponentController extends AbstractCMSComponentController<OshRotatingImagesComponentModel>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.
	 * HttpServletRequest, org.springframework.ui.Model,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final OshRotatingImagesComponentModel component)
	{
		model.addAttribute("banners", component.getBanners());
		model.addAttribute("timeout", component.getTimeout());
	}



}
