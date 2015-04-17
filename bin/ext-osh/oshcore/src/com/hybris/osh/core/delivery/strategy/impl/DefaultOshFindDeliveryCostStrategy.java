/**
 *
 */
package com.hybris.osh.core.delivery.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.strategies.calculation.impl.DefaultFindDeliveryCostStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.impl.DefaultOshDeliveryModeDao;
import com.hybris.osh.core.model.OSHShippingModel;
import com.hybris.osh.core.model.OshVariantProductModel;
import com.hybris.osh.core.shipping.CalculateFinalShippingAmount;


public class DefaultOshFindDeliveryCostStrategy extends DefaultFindDeliveryCostStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultOshFindDeliveryCostStrategy.class);

	@Resource(name = "defaultOshDeliveryModeDao")
	private DefaultOshDeliveryModeDao defaultOshDeliveryModeDao;

	@Resource(name = "finalShippingAmount")
	private CalculateFinalShippingAmount calculateFinalShippingAmount;

	@Override
	public PriceValue getDeliveryCost(final AbstractOrderModel abstractOrder)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", abstractOrder);

		double totalWeight = 0.0;
		getModelService().save(abstractOrder);
		List<OSHShippingModel> oshShippingModels = new ArrayList<OSHShippingModel>();
		if (abstractOrder.getDeliveryAddress() != null)
		{
			final String state = abstractOrder.getDeliveryAddress().getRegion().getIsocode();
			final String zipcode = abstractOrder.getDeliveryAddress().getPostalcode();
			for (final AbstractOrderEntryModel orderEntryModel : abstractOrder.getEntries())
			{
				//for online only
				double weight = 0.0;
				if (orderEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE))
				{
					final OshVariantProductModel model = (OshVariantProductModel) orderEntryModel.getProduct();
					if (model.getWeight() != null)
					{
						weight = model.getWeight().doubleValue();

					}
					weight *= orderEntryModel.getQuantity().doubleValue();
				}
				totalWeight += weight;
			}
			if (abstractOrder.getDeliveryMode() != null
					&& abstractOrder.getDeliveryMode().getCode().equalsIgnoreCase(OshCoreConstants.HOME_DELIVERY_GROSS))
			{
				oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithHomeDirectMode(state, totalWeight);
			}
			else
			{
				oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithOtherMode(state, totalWeight, zipcode);
			}

			final double shippingValue = findOSHShippingModel(abstractOrder.getDeliveryMode(), oshShippingModels, abstractOrder);
			return new PriceValue(abstractOrder.getCurrency().getIsocode(), shippingValue, abstractOrder.getNet().booleanValue());
		}
		else
		{
			return new PriceValue(abstractOrder.getCurrency().getIsocode(), 0.0, abstractOrder.getNet().booleanValue());
		}
	}

	public double findOSHShippingModel(final DeliveryModeModel newdeliveryModes, final List<OSHShippingModel> oshShippingModels,
			final AbstractOrderModel abstractOrder)
	{
		boolean isSurcharge = false;
		double orderSurcharge = 0;
		double shippingVal = 0.0;
		for (final AbstractOrderEntryModel entry : abstractOrder.getEntries())
		{
			double unitSurcharge = 0;
			long qty = 0;
			qty = entry.getQuantity().longValue();

			if (entry.getProduct().getShippingSurcharge() != null)
			{
				unitSurcharge = entry.getProduct().getShippingSurcharge().doubleValue();
				unitSurcharge = unitSurcharge * qty;
			}

			orderSurcharge = orderSurcharge + unitSurcharge;

		}

		for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
		{

			if (newdeliveryModes instanceof ZoneDeliveryModeModel)
			{

				if (newdeliveryModes.getCode().contains(modifyOSHShippingModel.getShippingMethod().getCode()))
				{
					//add shipping final price to the final delivery mode
					//					final CalculateFinalShippingAmount calculateFinalShippingAmount = new CalculateFinalShippingAmount();
					shippingVal = calculateFinalShippingAmount.get(modifyOSHShippingModel).doubleValue();

					if (orderSurcharge != 0.0d && !modifyOSHShippingModel.getShippingMethod().getCode().equals("homeDirect"))
					{
						abstractOrder.setShippingSurchargeStatus(true);
						shippingVal = shippingVal + orderSurcharge;
						isSurcharge = true;
					}
					else
					{
						if (!isSurcharge)
						{
							abstractOrder.setShippingSurchargeStatus(false);
						}

					}

				}

			}

		}
		return shippingVal;
	}

	/**
	 * @return the defaultOshDeliveryModeDao
	 */
	public DefaultOshDeliveryModeDao getDefaultOshDeliveryModeDao()
	{
		return defaultOshDeliveryModeDao;
	}

	/**
	 * @param defaultOshDeliveryModeDao
	 *           the defaultOshDeliveryModeDao to set
	 */
	public void setDefaultOshDeliveryModeDao(final DefaultOshDeliveryModeDao defaultOshDeliveryModeDao)
	{
		this.defaultOshDeliveryModeDao = defaultOshDeliveryModeDao;
	}

}
