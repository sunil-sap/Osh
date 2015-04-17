/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.dao.OshStoreFlagDao;
import com.hybris.osh.core.model.OshStoreFlagModel;


/**
 * Store Flag for all stores
 * 
 */
public class DefaultOshStoreFlagDao implements OshStoreFlagDao
{
	static private final Logger LOG = Logger.getLogger(DefaultOshStoreFlagDao.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * check store flag to show stores or not
	 */
	@Override
	public boolean getStoreFlag()
	{
		// YTODO Auto-generated method stub
		final String queryString = "Select {PK} FROM {OshStoreFlag}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		final SearchResult<OshStoreFlagModel> result = flexibleSearchService.search(query);

		//default flag = false
		boolean flag = false;
		if (result.getResult() != null && !result.getResult().isEmpty())
		{

			if (result.getResult().get(0).getStoreFlag() != null)
			{
				flag = result.getResult().get(0).getStoreFlag();
			}
		}

		return flag;
	}


}
