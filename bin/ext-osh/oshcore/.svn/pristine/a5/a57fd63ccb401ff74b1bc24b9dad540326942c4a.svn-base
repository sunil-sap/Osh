package com.hybris.osh.core.price;

/**
 * 
 */

import de.hybris.platform.core.Registry;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.PriceValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;


public class OshPriceFactory extends Europe1PriceFactory
{

	private static final Logger logger = Logger.getLogger(OshPriceFactory.class);

	@Override
	public PriceValue getBasePrice(final AbstractOrderEntry entry) throws JaloPriceFactoryException

	{
		final SessionService sessionService = (SessionService) Registry.getApplicationContext().getBean("sessionService");
		sessionService.setAttribute(Europe1Constants.PARAMS.UPG, null);
		PriceRow salePrice = null;
		PriceRow mapPrice = null;
		for (final PriceRow pr : super.getEurope1Prices(entry.getProduct()))
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("price row ->" + pr.getPrice() + " " + pr.getUg().getCode() + " for product ->"
						+ entry.getProduct().getCode());
			}
			if (pr.getUg().getCode().equals(OshCoreConstants.MAP_PRICE))
			{
				mapPrice = pr;
			}
			if (pr.getUg().getCode().contains(OshCoreConstants.SALE_PRICE))
			{
				salePrice = pr;
			}
		}
		if (mapPrice != null && calculatePrice(mapPrice))
		{
			sessionService.setAttribute(Europe1Constants.PARAMS.UPG, mapPrice.getUg());
			if (logger.isDebugEnabled())
			{
				logger.debug("Fetching Map Price ");
			}

			final PriceValue priceValue = super.getBasePrice(entry);
			sessionService.setAttribute(Europe1Constants.PARAMS.UPG, null);
			return priceValue;
		}
		else if (salePrice != null && calculatePrice(salePrice))
		{
			sessionService.setAttribute(Europe1Constants.PARAMS.UPG, salePrice.getUg());
			if (logger.isDebugEnabled())
			{
				logger.debug("Fetching Sale Price ");
			}
			final PriceValue priceValue = super.getBasePrice(entry);
			sessionService.setAttribute(Europe1Constants.PARAMS.UPG, null);
			return priceValue;
		}

		else
		{
			return super.getBasePrice(entry);
		}

	}

	private boolean calculatePrice(final PriceRow priceValue)
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
				logger.error(e.getMessage());
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
				logger.error(e.getMessage());
			}

		}
		try
		{
			currentDate = sdf.parse(sdf.format(currentDate));
		}
		catch (final ParseException e)
		{
			logger.error(e.getMessage());
		}


		if (currentDate.after(startDate) && currentDate.before(endDate))
		{
			return true;
		}
		else
		{
			return false;
		}
	}




}
