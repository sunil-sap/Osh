/**
 * 
 */
package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.price.facade.OshPriceFacade;
import com.hybris.osh.facades.order.data.OshOrderEntryData;


/**
 *
 */
public class OshOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "oshPriceFacade")
	private OshPriceFacade oshPriceFacade;

	private static final Logger LOG = Logger.getLogger(OshProductPopulator.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target) throws ConversionException
	{
		if (target instanceof OshOrderEntryData)
		{
			((OshOrderEntryData) target).setOrderType(source.getOrderType());
			if (source.getDiscountPrice() != null)
			{
				((OshOrderEntryData) target).setDiscountPrice(priceDataFactory.create("BUY",
						BigDecimal.valueOf(source.getDiscountPrice().doubleValue()), "USD"));
			}
			if (source.getProductDiscountPrice() != null)
			{
				((OshOrderEntryData) target).setProductDiscountPrice(priceDataFactory.create("BUY",
						BigDecimal.valueOf(source.getProductDiscountPrice().doubleValue()), "USD"));
			}

			final List<PriceRow> priceRow = oshPriceFacade.getPriceInfo(source.getProduct());

			/*
			 * this will set Osh_RegularPrice row and Osh_MapPrice in product data
			 */
			for (final PriceRow priceinfo : priceRow)
			{
				if (priceinfo.getUg().getCode().equals("Osh_RegularPrice") && priceinfo.getPrice() != null)
				{
					((OshOrderEntryData) target).setRegPriceData(calculatePrice(priceinfo));
				}
				if (priceinfo.getUg().getCode().equals("Osh_MapPrice") && priceinfo.getPrice() != null)
				{
					((OshOrderEntryData) target).setMapPriceData(calculatePrice(priceinfo));
				}
				if (priceinfo.getUg().getCode().equals("Osh_SalesPrice") && priceinfo.getPrice() != null)
				{
					((OshOrderEntryData) target).setSalePriceData(calculatePrice(priceinfo));
				}
			}


		}

	}

	private PriceData calculatePrice(final PriceRow priceValue)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
		Date startDate = null;
		Date endDate = null;
		Date currentDate = new Date();
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


		if (currentDate.after(startDate) && currentDate.before(endDate))
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
