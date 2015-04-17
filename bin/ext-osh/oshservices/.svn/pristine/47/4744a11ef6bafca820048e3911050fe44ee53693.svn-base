/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.converter.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.services.keyword.OshProductService;


/**
 * 
 *
 */
public class OshBrandCategoryConverter extends OshImpexConverter
{
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OshProductService oshProductService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ModelService modelService;


	@Override
	protected void buildColumnValue(final char nextChar, final int idx, final int endIdx, final Map<Integer, String> row,
			final StringBuilder builder)
	{
		final boolean mandatory = nextChar == getPlusChar();
		Integer mapIdx = null;
		try
		{
			mapIdx = Integer.valueOf(getImpexRow().substring(mandatory ? idx + 2 : idx + 1, endIdx));
		}
		catch (final NumberFormatException e)
		{
			throw new SystemException("Invalid row syntax [invalid column number]: " + getImpexRow(), e);
		}
		final String colValue = row.get(mapIdx);
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("oshProductCatalog", "Staged");
		final String categoryCode = row.get(Integer.valueOf(14));
		final ProductModel prodModel = oshProductService.getProduct(catalogVersion, "B-" + row.get(Integer.valueOf(0)));
		if (prodModel != null)
		{
			if (!categoryCode.isEmpty())
			{
				final CategoryModel superCategoryModel = categoryService.getCategoryForCode(catalogVersion, "brands");
				try
				{
					final CategoryModel categModel = categoryService.getCategoryForCode(catalogVersion, categoryCode);
				}
				catch (final UnknownIdentifierException ex)
				{
					final CategoryModel categoryModel = modelService.create(CategoryModel.class);
					categoryModel.setCode(categoryCode);
					categoryModel.setName(categoryCode);
					categoryModel.setCatalogVersion(catalogVersion);
					categoryModel.setSupercategories(Collections.singletonList(superCategoryModel));
					modelService.save(categoryModel);
					builder.append(categoryCode);
				}

			}
		}

	}

}
