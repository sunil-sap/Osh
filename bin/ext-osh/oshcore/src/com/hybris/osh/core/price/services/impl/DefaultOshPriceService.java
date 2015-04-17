package com.hybris.osh.core.price.services.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.constants.Europe1Tools;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.price.services.OshPriceService;


/**
 * implementaion class for oshPriceService
 * 
 */
public class DefaultOshPriceService implements OshPriceService
{

	@Autowired
	private ModelService modelService;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.price.services.OshPriceService#getPriceInfo(de.hybris.platform.core.model.product.ProductModel
	 * )
	 */
	@Override
	public List<PriceRow> getPriceInfo(final ProductModel productModel)
	{
		final Product product = (Product) modelService.getSource(productModel);
		final SessionContext sessionContext = OrderManager.getInstance().getSession().getSessionContext();
		final Europe1PriceFactory priceFactory = (Europe1PriceFactory) OrderManager.getInstance().getPriceFactory();
		final List<PriceRow> priceRows = (List<PriceRow>) priceFactory.getProductPriceRowsFast(sessionContext, product, null);
		return priceRows;


	}

	@Override
	public PriceInformation getPriceInfoForSolrFacets(final ProductModel productModel)
	{
		final Product product = (Product) modelService.getSource(productModel);
		final SessionContext sessionContext = OrderManager.getInstance().getSession().getSessionContext();
		final Europe1PriceFactory priceFactory = (Europe1PriceFactory) OrderManager.getInstance().getPriceFactory();
		final List<PriceRow> priceRows = (List<PriceRow>) priceFactory.getProductPriceRowsFast(sessionContext, product, null);
		PriceInformation priceInfo = null;

		for (final PriceRow myPrice : priceRows)
		{
			final String customerPriceGroup = myPrice.getUg().getCode();
			if (customerPriceGroup != null)
			{
				if (customerPriceGroup.equalsIgnoreCase("Osh_RegularPrice"))
				{
					priceInfo = Europe1Tools.createPriceInformation(myPrice, sessionContext.getCurrency());
				}
				else if (customerPriceGroup.equalsIgnoreCase("Osh_SalesPrice"))
				{
					priceInfo = Europe1Tools.createPriceInformation(myPrice, sessionContext.getCurrency());
					break;
				}
			}
		}

		return priceInfo;

	}

}
