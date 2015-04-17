package com.hybris.osh.facades.storelocator.converters;

import de.hybris.platform.acceleratorfacades.storefinder.impl.DefaultStoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.apache.log4j.Logger;


public class DefaultOshStoreFinderFacade extends DefaultStoreFinderFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultOshStoreFinderFacade.class);

	@Override
	public PointOfServiceData getPointOfServiceForName(final String name)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final PointOfServiceModel pointOfService = getStoreFinderService().getPointOfServiceForName(currentBaseStore, name);

		if (pointOfService != null)
		{
			return getPointOfServiceConverter().convert(pointOfService);
		}
		else
		{
			LOG.info("PointOfServiceModel is null");
			return new PointOfServiceData();
		}
	}
}
