/**
 * 
 */
package com.hybris.osh.facades.converters;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.VariantOptionDataConverter;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.model.OshVariantProductModel;
import com.hybris.osh.core.price.facade.OshPriceFacade;
import com.hybris.osh.facades.product.data.OshVariantOptionData;




/**
 * 
 */
public class OshVariantOptionDataConverter extends VariantOptionDataConverter
{
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "oshPriceFacade")
	private OshPriceFacade oshPriceFacade;

	@Autowired
	private WarehouseService warehouseService;

	private static final Logger LOG = Logger.getLogger(OshVariantOptionDataConverter.class);

	@Override
	protected OshVariantOptionData createTarget()
	{
		return new OshVariantOptionData();
	}

	@Override
	public void populate(final VariantProductModel source, final VariantOptionData target)
	{
		super.populate(source, target);
		if (target instanceof OshVariantOptionData)
		{
			final OshVariantOptionData oshTarget = (OshVariantOptionData) target;
			if (source instanceof OshVariantProductModel)
			{
				if (source.getCode() != null && !source.getCode().isEmpty())
				{
					oshTarget.setCode(source.getCode());
				}
				if (source.getAvailabilityInd() != null)
				{
					oshTarget.setAvailabilityInd(source.getAvailabilityInd().getCode());
				}


				if (!isStockSystemEnabled())
				{
					target.setStockLevelStatus(StockLevelStatus.INSTOCK);
					target.setStockLevel(Integer.valueOf(0));
				}
				else if (source.getCode().startsWith("8"))
				{
					final Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(source,
							Collections.singletonList(warehouseService.getWarehouseForCode("dropship")));
					target.setStockLevel(Integer.valueOf(calculateTotalActualAmount(stockLevels)));
					target.setStockLevelStatus(getStockLevelStatusStrategy().checkStatus(stockLevels));
				}
				else
				{
					final Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(source,
							warehouseService.getDefWarehouse());
					target.setStockLevel(Integer.valueOf(calculateTotalActualAmount(stockLevels)));
					target.setStockLevelStatus(getStockLevelStatusStrategy().checkStatus(stockLevels));
				}

				if (source.getName() != null && !source.getName().isEmpty())
				{
					oshTarget.setName(source.getName());
				}
				if (((OshVariantProductModel) source).getColour() != null && !((OshVariantProductModel) source).getColour().isEmpty())
				{
					oshTarget.setColour(((OshVariantProductModel) source).getColour());
				}
				if (((OshVariantProductModel) source).getSize() != null && !((OshVariantProductModel) source).getSize().isEmpty())
				{
					oshTarget.setSize(((OshVariantProductModel) source).getSize());
				}


				oshTarget.setNewSKU(source.isNewSKU());
				oshTarget.setFreeShipping(source.isFreeShipping());
				oshTarget.setRecall(source.isRecall());
				oshTarget.setWhiteGlove(source.isWhiteGlove());
				oshTarget.setSizeChart(source.isSizeChart());
				oshTarget.setMapPriceType(source.isMapPriceFlag());



				final List<PriceRow> priceRow = oshPriceFacade.getPriceInfo(source);

				/*
				 * this will set Osh_RegularPrice row and Osh_MapPrice in product data
				 */
				for (final PriceRow priceinfo : priceRow)
				{
					if (priceinfo.getUg().getCode().equals("Osh_RegularPrice") && priceinfo.getPrice() != null)
					{
						oshTarget.setRegPriceData(calculatePrice(priceinfo));
					}
					if (priceinfo.getUg().getCode().equals("Osh_MapPrice") && priceinfo.getPrice() != null)
					{
						oshTarget.setMapPriceData(calculatePrice(priceinfo));
					}
					if (priceinfo.getUg().getCode().equals("Osh_SalesPrice") && priceinfo.getPrice() != null)
					{
						oshTarget.setSalePriceData(calculatePrice(priceinfo));
					}

				}

			}
		}
	}

	private PriceData calculatePrice(final PriceRow priceValue)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
		Date currentDate = new Date();
		//	final Date startTime = priceValue.getStartTime();
		//	final Date endTime = priceValue.getEndTime();
		if (priceValue.getStartTime() != null)
		{
			try
			{
				startDate = sdf.parse(sdf.format(priceValue.getStartTime()));
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage());
			}

		}
		if (priceValue.getEndTime() != null)
		{
			try
			{
				endDate = sdf.parse(sdf.format(priceValue.getEndTime()));
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage());
			}

		}
		try
		{
			currentDate = sdf.parse(sdf.format(currentDate));
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage());
		}


		if (startDate != null && endDate != null && currentDate.after(startDate) && currentDate.before(endDate))
		{
			final BigDecimal price = BigDecimal.valueOf(priceValue.getPrice().doubleValue());
			final PriceData priceData = priceDataFactory.create("BUY", price, "USD");
			return priceData;
		}
		else
		{
			return null;
		}
	}


}
