/**
 * 
 */
package com.hybris.osh.core.storename.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import org.apache.log4j.Logger;

import com.hybris.osh.core.storename.dao.OshStoreNameDao;


/**
 * This dao class will fetch store name from database.
 * 
 */
public class DefaultOshStoreNameDao implements OshStoreNameDao
{

	/*
	 * This method will return list of store name.
	 */

	protected static final Logger LOG = Logger.getLogger(DefaultOshStoreNameDao.class);

	private FlexibleSearchService flexibleSearchService;


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	@Override
	public List<PointOfServiceModel> storeName()
	{


		final SearchResult<PointOfServiceModel> storeName = getFlexibleSearchService().search("select {pk} from {PointOfService}");
		return storeName.getCount() == 0 ? null : storeName.getResult();
	}
}
