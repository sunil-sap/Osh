/**
 * 
 */
package com.hybris.osh.services.keyword.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import com.hybris.osh.services.keyword.OshProductService;


/**
 * 
 *
 */
public class DefaultOshProductService implements OshProductService
{
	private ProductDao productDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.services.keyword.OshProductService#getProduct(de.hybris.platform.catalog.model.CatalogVersionModel,
	 * java.lang.String)
	 */
	@Override
	public ProductModel getProduct(final CatalogVersionModel catalogVersion, final String code)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("catalogVersion", catalogVersion);
		ServicesUtil.validateParameterNotNullStandardMessage("ProductCode", code);

		final List<ProductModel> res = productDao.findProductsByCode(catalogVersion, code);

		return res.isEmpty() ? null : res.get(0);

	}

	public ProductDao getProductDao()
	{
		return productDao;
	}

	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}

}
