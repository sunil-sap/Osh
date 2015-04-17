package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.osh.facades.user.data.OshAddressData;
import com.hybris.osh.facades.user.data.StateData;


public class OshAddresssPopulator implements Populator<AddressModel, AddressData>
{


	@Override
	public void populate(final AddressModel source, final AddressData target) throws ConversionException
	{
		final RegionModel regionModel = source.getRegion();
		if (regionModel != null)
		{
			final StateData stateData = new StateData();
			stateData.setIsocode(regionModel.getIsocode());
			stateData.setName(regionModel.getName());
			((OshAddressData) target).setState(stateData);
		}
		target.setEmail(source.getEmail());

	}

}
