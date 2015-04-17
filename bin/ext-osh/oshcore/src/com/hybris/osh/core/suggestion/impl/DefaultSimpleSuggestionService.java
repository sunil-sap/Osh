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
package com.hybris.osh.core.suggestion.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import com.hybris.osh.core.suggestion.SimpleSuggestionService;
import com.hybris.osh.core.suggestion.dao.SimpleSuggestionDao;

import java.util.List;


/**
 * Default implementation of {@link SimpleSuggestionService}.
 */
public class DefaultSimpleSuggestionService implements SimpleSuggestionService
{
	private SimpleSuggestionDao simpleSuggestionDao;

	@Override
	public List<ProductModel> getReferencesForPurchasedInCategory(final CategoryModel category, final UserModel user,
			final ProductReferenceTypeEnum referenceType, final boolean excludePurchased, final Integer limit)
	{
		return getSimpleSuggestionDao().findProductsRelatedToPurchasedProductsByCategory(category, user, referenceType,
				excludePurchased, limit);
	}

	protected SimpleSuggestionDao getSimpleSuggestionDao()
	{
		return simpleSuggestionDao;
	}

	public void setSimpleSuggestionDao(final SimpleSuggestionDao simpleSuggestionDao)
	{
		this.simpleSuggestionDao = simpleSuggestionDao;
	}
}
