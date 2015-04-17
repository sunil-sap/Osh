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
@RequestMapping(value = "/errorPage")
public class ErrorPageController extends AbstractPageController
{

	private static final String ERROR_CMS_PAGE = "notFound";
	private static final Logger LOG = Logger.getLogger(ErrorPageController.class);

	public String getErrorPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
		return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
	}


}
