/**
 * 
 */
package com.hybris.osh.facades.inventoryvalidation.impl;

import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hybris.osh.core.model.OshVariantProductModel;
import com.hybris.osh.facades.inventoryvalidation.InventoryValidation;


/**
 * 
 */
public class DefaultInventoryValidation implements InventoryValidation
{
	private static final String GEOPLUGIN_URL = "geopluginurl";
	private static final String SEARCHPARAMS = "searchparams";
	private static final String LOCATION = "location";

	private static final Logger LOG = Logger.getLogger(DefaultInventoryValidation.class);


	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;

	@Resource(name = "stockService")
	private StockService stockService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	private StockLevelStatusStrategy stockLevelStatusStrategy;

	/**
	 * @return the stockLevelStatusStrategy
	 */
	public StockLevelStatusStrategy getStockLevelStatusStrategy()
	{
		return stockLevelStatusStrategy;
	}

	/**
	 * @param stockLevelStatusStrategy
	 *           the stockLevelStatusStrategy to set
	 */
	public void setStockLevelStatusStrategy(final StockLevelStatusStrategy stockLevelStatusStrategy)
	{
		this.stockLevelStatusStrategy = stockLevelStatusStrategy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.BOPIS.InventaryValidation#storeStock(java.lang.String,
	 * de.hybris.platform.core.model.product.ProductModel)
	 */
	@Override
	public StockLevelStatus storeStock(final String storeName, final ProductModel productModel)
	{
		StockLevelModel stockLevelModel = null;
		WarehouseModel wareHouseModel = null;
		if (storeName != null)
		{
			wareHouseModel = warehouseService.getWarehouseForCode(storeName);
		}
		if (wareHouseModel != null)
		{
			if (productModel instanceof OshVariantProductModel)
			{
				stockLevelModel = stockService.getStockLevel(productModel, wareHouseModel);
			}
			else
			{
				if (!productModel.getVariants().isEmpty())
				{
					stockLevelModel = stockService.getStockLevel((ProductModel) productModel.getVariants().toArray()[0],
							wareHouseModel);
				}

			}
		}

		return getStockLevelStatusStrategy().checkStatus(stockLevelModel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.inventoryvalidation.InventoryValidation#getMyStore()
	 */
	@Override
	public PointOfServiceData getMyStore()
	{
		final CustomerModel custModel = (CustomerModel) userService.getCurrentUser();
		if (custModel.getMyStore() != null)
		{
			final PointOfServiceData pos = storeFinderFacade.getPointOfServiceForName(custModel.getMyStore().getName());
			if (pos != null)
			{
				pos.setUrl("/store/" + pos.getName() + "?lat=" + pos.getLatitude() + "&long=" + pos.getLongitude() + "&q="
						+ pos.getName());
			}
			return pos;
		}
		return null;

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.inventoryvalidation.InventoryValidation#getMyStore()
	 */
	@Override
	public String getUserLocation(final HttpServletRequest request)
	{
		//Instantiate an HttpClient
		final HttpClient client = new HttpClient();
		//Instantiate a GET HTTP method
		String location = "";
		try
		{
			final String url = configurationService.getConfiguration().getString(GEOPLUGIN_URL) + request.getRemoteAddr();
			LOG.info(url);
			final PostMethod method = new PostMethod(url);
			method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");
			final int statusCode = client.executeMethod(method);
			LOG.info("statusCode" + statusCode);
			final String geoResponse = method.getResponseBodyAsString();
			final String searchParams = configurationService.getConfiguration().getString(SEARCHPARAMS);
			final String[] paramsArray = searchParams.split(",");
			for (final String params : paramsArray)
			{
				location = location + StringUtils.substringBetween(geoResponse, params, "<") + " ";
			}
			request.getSession().setAttribute(LOCATION, location);
			method.releaseConnection();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return location;
	}

}
