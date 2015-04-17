/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hybris.osh.core.dao.OshRegionDao;


/**
 * 
 *
 */
public class DefaultOshRegionDao extends DefaultGenericDao<RegionModel> implements OshRegionDao
{
	/**
	 * Exposed to create dao instance
	 */
	public DefaultOshRegionDao()
	{
		this(RegionModel._TYPECODE);
	}

	/**
	 * Hidden constructor to prevent the type code change.
	 */
	public DefaultOshRegionDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public List<RegionModel> findRegions()
	{
		return find();
	}

	@Override
	public List<RegionModel> findRegionsByCountry(final CountryModel country)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(RegionModel.COUNTRY, country);
		params.put(RegionModel.ACTIVE, Integer.valueOf(1));
		return find(params);
	}

	@Override
	public List<RegionModel> findRegionsByCountryAndCode(final CountryModel country, final String regionCode)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(RegionModel.COUNTRY, country);
		params.put(RegionModel.ISOCODE, regionCode);
		params.put(RegionModel.ACTIVE, Integer.valueOf(1));

		return find(params);
	}





}
