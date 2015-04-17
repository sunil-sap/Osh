/**
 * 
 */
package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.osh.facades.customer.data.OshCustomerData;


/**
 * 
 *
 */
public class OshCustomerPopulator implements Populator<CustomerModel, CustomerData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final CustomerModel source, final CustomerData target) throws ConversionException
	{
		if (target instanceof OshCustomerData)
		{
			((OshCustomerData) target).setCluborchardinfo(source.isClubOrchardInfo());
			((OshCustomerData) target).setSpecialoffer(source.isSpecialOffers());
			((OshCustomerData) target).setStorenewletter(source.isStoreNewsLetter());
			((OshCustomerData) target).setPhone(source.getPhone());
			((OshCustomerData) target).setLoyaltyNumber(source.getLoyaltyNumber());
			if (source.getBirthDay() != null)
			{
				final String dateMonth[] = source.getBirthDay().split("/");
				if (dateMonth.length > 1)
				{
					((OshCustomerData) target).setMonth(dateMonth[0]);
					((OshCustomerData) target).setDay(dateMonth[1]);
				}
			}
		}

	}

}
