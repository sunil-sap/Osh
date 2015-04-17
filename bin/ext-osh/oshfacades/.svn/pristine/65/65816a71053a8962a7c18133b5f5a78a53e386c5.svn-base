package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import com.hybris.osh.facades.user.data.OshAddressData;
import com.hybris.osh.facades.user.data.StateData;


public class OshAddressReversePopulator extends AddressReversePopulator
{

	@Override
	public void populate(final AddressData addressData, final AddressModel addressModel) throws ConversionException
	{
		super.populate(addressData, addressModel);

		if (addressData instanceof OshAddressData)
		{
			final StateData stateData = ((OshAddressData) addressData).getState();
			final String isoCode = stateData.getIsocode();
			try
			{
				final RegionModel regionModel = getCommonI18NService().getRegion(addressModel.getCountry(), isoCode);
				addressModel.setRegion(regionModel);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new ConversionException("No country with the code " + addressModel.getCountry().getIsocode() + " found.", e);
			}
			addressModel.setEmail(addressData.getEmail());

		}

	}
}