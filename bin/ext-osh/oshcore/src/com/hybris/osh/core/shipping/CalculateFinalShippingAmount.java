package com.hybris.osh.core.shipping;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import com.hybris.osh.core.model.OSHShippingModel;


/**
 * To Calculate Final Shipping amount based on weight and zipcode.
 *
 */
public class CalculateFinalShippingAmount implements DynamicAttributeHandler<Double, OSHShippingModel>
{
	private ConfigurationService configurationService;

	public static final String DISTANCE_ROUNDOFF_KEY = "osh.shipping.distance.defaultDistance";
	public static final String WEIGHT_ROUNDOFF_KEY = "osh.shipping.weight.defaultWeight";
	public static final String DISTANCE_ROUNDOFF_CHARGES_KEY = "osh.shipping.distance.roundOff.charges";
	public static final String WEIGHT_ROUNDOFF_CHARGES_KEY = "osh.shipping.weight.roundOff.charges";
	public static final String ORDER_SHIPPING_CHARGES = "osh.shipping.weight.orderCharges";

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@SuppressWarnings("boxing")
	@Override
	public Double get(final OSHShippingModel model)
	{
		if (model.getShippingMethod().getCode().equals("homeDirect"))
		{
			final double distanceRoundOff = Double.parseDouble(configurationService.getConfiguration().getString(
					DISTANCE_ROUNDOFF_KEY));
			final double weightRoundOff = Double.parseDouble(configurationService.getConfiguration().getString(WEIGHT_ROUNDOFF_KEY));
			final double distanceRoundOffCharges = Double.parseDouble(configurationService.getConfiguration().getString(
					DISTANCE_ROUNDOFF_CHARGES_KEY));
			final double weightRoundOffCharges = Double.parseDouble(configurationService.getConfiguration().getString(
					WEIGHT_ROUNDOFF_CHARGES_KEY));
			final double orderShippingCharges = Double.parseDouble(configurationService.getConfiguration().getString(
					ORDER_SHIPPING_CHARGES));


			final double weight = 1000;
			final double hubDistance = 1000;
			double shippingCharge = 0.0;


			if (weight != 0.0 && hubDistance != 0.0)
			{
				// distance roundoff charges
				if (weight <= weightRoundOff)
				{
					shippingCharge = orderShippingCharges;
				}
				else
				{
					shippingCharge = orderShippingCharges + (weight - weightRoundOff) * weightRoundOffCharges;
				}
				// fuel roundoff charge
				if (hubDistance >= distanceRoundOff)
				{
					shippingCharge = shippingCharge + (hubDistance - distanceRoundOff) * distanceRoundOffCharges;
				}
			}
			return shippingCharge;
		}

		final double shippingCharge = model.getShippingCost().doubleValue();
		if (shippingCharge != 0.0)
		{
			final double deliveryCost = shippingCharge + (shippingCharge * model.getOSHMarkUp() / 100);
			final double roundOff = Math.round(deliveryCost * 100) / 100D;
			return roundOff;
		}
		return shippingCharge;

	}

	@Override
	public void set(final OSHShippingModel model, final Double value)
	{
		throw new UnsupportedOperationException();

	}


}
