/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.commerceservices.i18n.impl.DefaultCommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.List;

import javax.annotation.Resource;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.OshRegionDao;
import com.hybris.osh.core.service.OshCommerceCommonI18NService;


/**
 * 
 */
public class DefaultOshCommerceCommonI18NService extends DefaultCommerceCommonI18NService implements OshCommerceCommonI18NService
{

	@Resource(name = "oshRegionDao")
	private OshRegionDao oshRegionDao;



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cyclegear.core.services.i18n.CGCommonI18NService#getRegionsByCountry(de.hybris.platform.core.model.c2l.
	 * CountryModel)
	 */
	@Override
	public List<RegionModel> getRegionsByCountry(final CountryModel country)
	{
		return getOshRegionDao().findRegionsByCountry(country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cyclegear.core.services.i18n.CGCommonI18NService#getDefaultCountry()
	 */
	@Override
	public CountryModel getDefaultCountry()
	{
		return getCommonI18NService().getCountry(OshCoreConstants.DEFAULT_COUNTRY_CODE);
	}

	public OshRegionDao getOshRegionDao()
	{
		return oshRegionDao;
	}

}
