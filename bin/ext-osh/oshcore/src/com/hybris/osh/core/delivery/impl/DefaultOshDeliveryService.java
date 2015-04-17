/**
 *
 */
package com.hybris.osh.core.delivery.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.delivery.impl.DefaultDeliveryService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.impl.DefaultOshDeliveryModeDao;
import com.hybris.osh.core.model.OSHShippingModel;
import com.hybris.osh.core.model.OshVariantProductModel;
import com.hybris.osh.core.shipping.CalculateFinalShippingAmount;


/**
 * Class which Calculate Shipping Price on the Basis of weight and zipcode of the product.
 */
public class DefaultOshDeliveryService extends DefaultDeliveryService
{
	@Resource(name = "defaultOshDeliveryModeDao")
	private DefaultOshDeliveryModeDao defaultOshDeliveryModeDao;

	@Resource(name = "finalShippingAmount")
	private CalculateFinalShippingAmount calculateFinalShippingAmount;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public Collection<DeliveryModeModel> getSupportedDeliveryModesForOrder(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
		boolean isHomeDirect = false;
		boolean isGround = false;
		boolean isSecoundDay = false;
		boolean isNextDay = false;
		final AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();
		final CurrencyModel currency = abstractOrder.getCurrency();
		double totalWeight = 0.0;
		boolean upsOnly = false;
		List<OSHShippingModel> oshShippingModels = new ArrayList<OSHShippingModel>();

		if (currency != null && deliveryAddress != null && deliveryAddress.getCountry() != null)
		{
			final List<DeliveryModeModel> deliveryModes = new ArrayList<DeliveryModeModel>(getCountryZoneDeliveryModeDao()
					.findDeliveryModesByCountryAndCurrency(deliveryAddress.getCountry(), currency, abstractOrder.getNet()));
			final List<DeliveryModeModel> newDeliveryModes = new ArrayList<DeliveryModeModel>(getCountryZoneDeliveryModeDao()
					.findDeliveryModesByCountryAndCurrency(deliveryAddress.getCountry(), currency, abstractOrder.getNet()));
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
			final double maxWeight = maxWeight(state, zipcode);

			for (final AbstractOrderEntryModel orderEntryModel : abstractOrder.getEntries())
			{
				final OshVariantProductModel model = (OshVariantProductModel) orderEntryModel.getProduct();
				if (model.getShippingMethod() != null && model.getShippingMethod().getCode().equals("upsOnly")
						|| isValidWeight(maxWeight, totalWeight))
				{
					upsOnly = true;
				}
				if (model.getShippingMethod() != null && model.getShippingMethod().getCode().equals("homeDirect")
						|| isValidWeight(maxWeight, totalWeight))
				{
					isHomeDirect = true;
					break;
				}
				else if (model.getShippingMethod() != null && model.getShippingMethod().getCode().equals("ground")
						|| isValidWeight(maxWeight, totalWeight))
				{
					isGround = true;
				}
				else if (model.getShippingMethod() != null && model.getShippingMethod().getCode().equals("secondDay")
						|| isValidWeight(maxWeight, totalWeight))
				{
					isSecoundDay = true;
				}
				else if (model.getShippingMethod() != null && model.getShippingMethod().getCode().equals("nextDay")
						|| isValidWeight(maxWeight, totalWeight))
				{
					isNextDay = true;
				}


			}

			if (isHomeDirect || isGround || isSecoundDay || isNextDay || upsOnly)
			{
				if (isHomeDirect)
				{
					//fetch OshShippingModel in case of homeDirect Mode
					oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithHomeDirectMode(state, totalWeight);
					for (final DeliveryModeModel deliveryMode : deliveryModes)
					{
						//set Home Direct Method and remove other method
						for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
						{

							if (!(deliveryMode.getCode().contains(modifyOSHShippingModel.getShippingMethod().getCode())))
							{
								newDeliveryModes.remove(deliveryMode);
							}

						}

					}
				}
				else if (!isHomeDirect && upsOnly)
				{
					//fetch OshShippingModel in case of UPS Only
					oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithOtherMode(state, totalWeight, zipcode);
					for (final DeliveryModeModel deliveryMode : deliveryModes)
					{
						for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
						{
							//Remove Home Direct Method
							if (!modifyOSHShippingModel.getShippingMethod().getCode().equals("homeDirect")
									&& deliveryMode.getCode().contains("homeDirect"))
							{
								newDeliveryModes.remove(deliveryMode);
							}

						}
					}
				}
				else if (!isHomeDirect && !upsOnly && isGround)
				{
					//fetch OshShippingModel in case of Ground Mode
					oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithOtherMode(state, totalWeight, zipcode);
					for (final DeliveryModeModel deliveryMode : deliveryModes)
					{
						for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
						{
							//set Ground Method and remove other method
							if (!modifyOSHShippingModel.getShippingMethod().getCode().equals("ground")
									&& !deliveryMode.getCode().contains("ground"))
							{
								newDeliveryModes.remove(deliveryMode);
							}

						}
					}
				}
				else if (!isHomeDirect && !upsOnly && !isGround && isSecoundDay)
				{
					//fetch OshShippingModel in case of Second Mode
					oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithOtherMode(state, totalWeight, zipcode);
					for (final DeliveryModeModel deliveryMode : deliveryModes)
					{
						for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
						{
							//set Second Day Method and remove other method
							if (!modifyOSHShippingModel.getShippingMethod().getCode().equals("secondDay")
									&& !deliveryMode.getCode().contains("secondDay"))
							{
								newDeliveryModes.remove(deliveryMode);
							}

						}
					}
				}
				else if (!isHomeDirect && !upsOnly && !isGround && !isSecoundDay && isNextDay)
				{
					//fetch OshShippingModel in case of NextDay Mode
					oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithOtherMode(state, totalWeight, zipcode);
					for (final DeliveryModeModel deliveryMode : deliveryModes)
					{
						for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
						{
							//set Next Day Method and remove other method
							if (!modifyOSHShippingModel.getShippingMethod().getCode().equals("nextDay")
									&& !deliveryMode.getCode().contains("nextDay"))
							{
								newDeliveryModes.remove(deliveryMode);
							}

						}
					}
				}

				if (!oshShippingModels.isEmpty())
				{
					findOSHShippingModel(newDeliveryModes, oshShippingModels, abstractOrder);
				}
				if (!isHomeDirect && upsOnly)
				{
					sortDeliveryModes(newDeliveryModes, abstractOrder);
					return setDefaultGroundMethod(newDeliveryModes);
				}
				return newDeliveryModes;
			}
			else
			{
				//fetch OshShippingModel in case of not homeDirect Mode
				oshShippingModels = defaultOshDeliveryModeDao.findOSHShippingModelWithoutHomeDirectMode(state, totalWeight, zipcode);

				if (!oshShippingModels.isEmpty())
				{
					findOSHShippingModel(deliveryModes, oshShippingModels, abstractOrder);
				}
			}
			sortDeliveryModes(deliveryModes, abstractOrder);
			return setDefaultGroundMethod(deliveryModes);
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("boxing")
	public void findOSHShippingModel(final List<DeliveryModeModel> deliveryModes, final List<OSHShippingModel> oshShippingModels,
			final AbstractOrderModel abstractOrder)
	{
		boolean isSurcharge = false;
		double orderSurcharge = 0;
		for (final AbstractOrderEntryModel entry : abstractOrder.getEntries())
		{
			double unitSurcharge = 0;
			long qty = 0;
			qty = entry.getQuantity();

			if (entry.getProduct().getShippingSurcharge() != null)
			{
				unitSurcharge = entry.getProduct().getShippingSurcharge();
				unitSurcharge = unitSurcharge * qty;
			}

			orderSurcharge = orderSurcharge + unitSurcharge;

		}

		for (final OSHShippingModel modifyOSHShippingModel : oshShippingModels)
		{
			for (final DeliveryModeModel newdeliveryModes : deliveryModes)
			{
				if (newdeliveryModes instanceof ZoneDeliveryModeModel)
				{
					final ZoneDeliveryModeValueModel deliveryMode1Value = getZoneDeliveryModeValueForAbstractOrder(
							(ZoneDeliveryModeModel) newdeliveryModes, abstractOrder);

					if (newdeliveryModes.getCode().contains(modifyOSHShippingModel.getShippingMethod().getCode()))
					{
						//add shipping final price to the final delivery mode
						//	final CalculateFinalShippingAmount calculateFinalShippingAmount = new CalculateFinalShippingAmount();
						double shippingVal = calculateFinalShippingAmount.get(modifyOSHShippingModel).doubleValue();

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
						deliveryMode1Value.setValue(Double.valueOf(shippingVal));
						modelService.save(deliveryMode1Value);

					}

				}

			}
		}
		abstractOrder.setCalculated(false);
		modelService.save(abstractOrder);
		modelService.refresh(abstractOrder);
	}

	public List<OSHShippingModel> findMinMaxRangeOfZipcode(final String state)
	{
		final List<OSHShippingModel> minMaxRangeModel = defaultOshDeliveryModeDao.minMaxRangeOfZipcode(state);
		return minMaxRangeModel;
	}

	@SuppressWarnings("boxing")
	public double maxWeight(final String state, final String zipcode)
	{
		double maxWeight = 0.0;
		final List<OSHShippingModel> minMaxRangeModel = defaultOshDeliveryModeDao.minMaxRangeOfWeight(state, zipcode);
		final ArrayList<Double> maxWeightList = new ArrayList<Double>();
		for (final OSHShippingModel oshShippingModel : minMaxRangeModel)
		{
			if (oshShippingModel.getWeightFrom() != null && oshShippingModel.getWeightTo() != null)
			{
				if (!maxWeightList.contains(oshShippingModel.getWeightTo()))
				{
					maxWeightList.add(oshShippingModel.getWeightTo());
				}

			}

		}
		if (!maxWeightList.isEmpty())
		{
			//Find Max weight from Shipping Table
			maxWeight = Collections.max(maxWeightList);
		}

		return maxWeight;
	}

	public boolean isValidWeight(final double maxWeight, final double totalWeight)
	{
		if (maxWeight < totalWeight)
		{
			return true;
		}
		return false;
	}

	public List<DeliveryModeModel> setDefaultGroundMethod(final List<DeliveryModeModel> deliveryModes)
	{
		int index = 0;
		//set default ground delivery mode
		if (!(deliveryModes.get(0).getCode().contains("ground")))
		{
			for (final DeliveryModeModel deliveryModeModel : deliveryModes)
			{
				if (deliveryModeModel.getCode().contains("ground"))
				{
					final DeliveryModeModel deliveryModel = deliveryModes.get(0);
					index = deliveryModes.indexOf(deliveryModeModel);
					deliveryModes.set(0, deliveryModeModel);
					deliveryModes.set(index, deliveryModel);
				}
			}
		}
		return deliveryModes;
	}
}
