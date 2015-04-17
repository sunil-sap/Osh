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
package com.hybris.osh.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.commercefacades.product.ProductOption;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.osh.facades.product.data.OshProductReferenceData;
import com.hybris.osh.facades.product.impl.OshProductFacade;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.ProductDataHelper;


/**
 * Controller for CMS ProductReferencesComponent
 */
@Controller("ProductReferencesComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ProductReferencesComponent)
public class ProductReferencesComponentController extends AbstractCMSComponentController<ProductReferencesComponentModel>
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Resource(name = "oshProductFacade")
	private OshProductFacade oshProductFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ProductReferencesComponentModel component)
	{
		final List<OshProductReferenceData> oshProductReferences = oshProductFacade.getProductReferencesForCode(
				ProductDataHelper.getCurrentProduct(request), component.getProductReferenceType(), PRODUCT_OPTIONS,
				component.getMaximumNumberProducts());

		model.addAttribute("title", component.getTitle());
		model.addAttribute("productReferences", oshProductReferences);
	}
}
