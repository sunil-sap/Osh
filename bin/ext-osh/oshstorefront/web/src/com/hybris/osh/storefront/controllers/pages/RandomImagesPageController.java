/**
 * 
 */
package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.osh.storefront.controllers.ControllerConstants;


@Controller
@Scope("tenant")
@RequestMapping(value = "/blankpage")
public class RandomImagesPageController extends AbstractPageController
{
	private static final String BLANK_CMS_PAGE = "blankpage";
	private static final Logger LOG = Logger.getLogger(RandomImagesPageController.class);

	public String getBlankPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(BLANK_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BLANK_CMS_PAGE));
		return ControllerConstants.Views.Pages.Layout.BlankPage;
	}


}
