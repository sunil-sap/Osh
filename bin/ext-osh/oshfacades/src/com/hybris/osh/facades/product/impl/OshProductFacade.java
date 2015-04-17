/**
 * 
 */
package com.hybris.osh.facades.product.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.stock.StockService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.service.OshStoreFlagService;
import com.hybris.osh.facades.product.data.OshProductReferenceData;


public class OshProductFacade extends DefaultProductFacade
{
	private OshStoreFlagService oshStoreFlagService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commercefacades.product.impl.DefaultProductFacade#getProductReferencesForCode(java.lang.String,
	 * de.hybris.platform.catalog.enums.ProductReferenceTypeEnum, java.util.List, java.lang.Integer)
	 */
	ArrayList<ProductReferenceModel> pRefModels = new ArrayList<ProductReferenceModel>();

	@Override
	public List getProductReferencesForCode(final String code, final ProductReferenceTypeEnum referenceType,
			final List<ProductOption> options, final Integer limit)
	{
		final ProductModel productModel = getProductService().getProductForCode(code);
		final Collection productReferenceModels = (Collection) getProductAttribute(productModel, "productReferences");
		final List<ProductReferenceModel> list = new ArrayList<ProductReferenceModel>(productReferenceModels);

		//final ProductReferenceComparator comparator = new ProductReferenceComparator();
		Collections.sort(list, new Comparator()
		{
			@Override
			public int compare(final Object o1, final Object o2)
			{
				final ProductReferenceModel prm1 = (ProductReferenceModel) o1;
				final ProductReferenceModel prm2 = (ProductReferenceModel) o2;
				if (prm1.getPriority() != null && prm2.getPriority() != null)
				{
					return prm1.getPriority().compareTo(prm2.getPriority());
				}
				else
				{
					return 0;
				}
			}

		});


		if (CollectionUtils.isNotEmpty(list))
		{
			final List productReferences = new ArrayList();
			final Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				final ProductReferenceModel productReferenceModel = (ProductReferenceModel) iterator.next();

				if (!BooleanUtils.isNotFalse(productReferenceModel.getActive()))
				{
					continue;
				}
				final Set allBaseProducts = getAllBaseProducts(productModel);
				if (!allBaseProducts.contains(productReferenceModel.getSource())
						|| !productReferenceModel.getReferenceType().equals(referenceType))
				{
					continue;
				}
				final WarehouseModel wm = getWarehouseService().getWarehouseForCode(OshCoreConstants.WAREHOUSE);
				final List<WarehouseModel> oshWarehouse = new ArrayList<WarehouseModel>();
				oshWarehouse.add(wm);
				final StockLevelStatus stockStatus = getStockService().getProductStatus(productReferenceModel.getTarget(), wm);
				if (stockStatus.getCode().equals(OshCoreConstants.INSTOCK))
				{
					if (productReferences.size() < 3)
					{
						final OshProductReferenceData referenceFromProduct = (OshProductReferenceData) getOshProductReferenceConverter()
								.convert(productReferenceModel);
						referenceFromProduct.setTarget(getProductForOptions(productReferenceModel.getTarget(), options));
						productReferences.add(referenceFromProduct);
					}
					else
					{
						break;
					}

				}
			}
			return productReferences;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/*
	 * get store flag for all stores
	 */
	public boolean getStoreFlag()
	{
		return oshStoreFlagService.getStoreFlag();
	}

	/**
	 * @return the warehouseService
	 */
	public WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	/**
	 * @param warehouseService
	 *           the warehouseService to set
	 */
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}

	private Converter oshProductReferenceConverter;
	private WarehouseService warehouseService;
	private StockService stockService;



	/**
	 * @return the stockService
	 */
	public StockService getStockService()
	{
		return stockService;
	}

	/**
	 * @param stockService
	 *           the stockService to set
	 */
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	/**
	 * @return the oshProductReferenceConverter
	 */
	public Converter getOshProductReferenceConverter()
	{
		return oshProductReferenceConverter;
	}

	/**
	 * @param oshProductReferenceConverter
	 *           the oshProductReferenceConverter to set
	 */
	public void setOshProductReferenceConverter(final Converter oshProductReferenceConverter)
	{
		this.oshProductReferenceConverter = oshProductReferenceConverter;
	}

	/**
	 * @param oshStoreFlagService
	 *           the oshStoreFlagService to set
	 */
	public void setOshStoreFlagService(final OshStoreFlagService oshStoreFlagService)
	{
		this.oshStoreFlagService = oshStoreFlagService;
	}


}
