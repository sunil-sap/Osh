package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
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
import com.hybris.osh.facades.product.data.OshProductData;


/*
 * populator for oshproduct
 * set data from OshProductModel to OshProductData
 */

public class OshProductPopulator implements Populator<ProductModel, OshProductData>
{


	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "oshPriceFacade")
	private OshPriceFacade oshPriceFacade;


	private static final Logger LOG = Logger.getLogger(OshProductPopulator.class);


	@SuppressWarnings("deprecation")
	@Override
	public void populate(final ProductModel source, final OshProductData target) throws ConversionException
	{

		if (source.getEdiStock() != null)
		{
			target.setEdiStock(source.getEdiStock());
		}
		if (source.getAvailabilityInd() != null)
		{
			target.setAvailabilityInd(source.getAvailabilityInd().getCode());
		}

		if (source.getBrandName() != null)
		{
			target.setBrandName(source.getBrandName());
		}

		if (source.getRestriction() != null)
		{
			target.setRestriction(source.getRestriction());
		}
		if (source.getMsds() != null)
		{
			target.setMsds(source.getMsds());
		}
		if (source.getOwnersManual() != null)
		{
			target.setOwnersManual(source.getOwnersManual());
		}
		if (source.getRebateInformation() != null)
		{
			target.setRebateInformation(source.getRebateInformation());
		}
		if (source.getHelpfulAdvice() != null)
		{
			target.setHelpfulAdvice(source.getHelpfulAdvice());
		}

		if (source.getShippingRestriction() != null)
		{
			target.setShippingRestriction(source.getShippingRestriction());
		}
		if (source.getVideo() != null)
		{
			target.setVideo(source.getVideo());
		}
		if (source.getWarrantyInformation() != null)
		{
			target.setWarrantyInformation(source.getWarrantyInformation());
		}
		if (source.getProductAttachment() != null)
		{
			target.setProductAttachment(source.getProductAttachment());
		}
		target.setTaxable(source.getTaxable() != null ? source.getTaxable().booleanValue() : false);

		final List<PriceRow> priceRow = oshPriceFacade.getPriceInfo(source);

		/*
		 * this will set Osh_RegularPrice row and Osh_MapPrice in product data
		 */
		for (final PriceRow priceinfo : priceRow)
		{
			if (priceinfo.getUg().getCode().equals("Osh_RegularPrice") && priceinfo.getPrice() != null)
			{
				target.setRegPriceData(calculatePrice(priceinfo));
			}
			if (priceinfo.getUg().getCode().equals("Osh_MapPrice") && priceinfo.getPrice() != null)
			{
				target.setMapPriceData(calculatePrice(priceinfo));
			}
			if (priceinfo.getUg().getCode().equals("Osh_SalesPrice") && priceinfo.getPrice() != null)
			{
				target.setSalePriceData(calculatePrice(priceinfo));
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
