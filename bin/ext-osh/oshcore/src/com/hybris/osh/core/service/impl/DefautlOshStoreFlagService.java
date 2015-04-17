/**
 * 
 */
package com.hybris.osh.core.service.impl;

import javax.annotation.Resource;

import com.hybris.osh.core.dao.OshStoreFlagDao;
import com.hybris.osh.core.service.OshStoreFlagService;


/**
 * check this store flag to show stores or not
 */
public class DefautlOshStoreFlagService implements OshStoreFlagService
{
	@Resource(name = "oshStoreFlagDao")
	private OshStoreFlagDao oshStoreFlagDao;

	/*
	 * check this store flag to show stores or not
	 */
	@Override
	public boolean getStoreFlag()
	{
		// YTODO Auto-generated method stub
		return oshStoreFlagDao.getStoreFlag();
	}

}
