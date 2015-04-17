/**
 * 
 */
package com.hybris.osh.core.dao;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.List;


/**
 * Dao responsible for {@link RegionModel} access.
 * 
 * @spring.bean oshRegionDao
 * 
 */
public interface OshRegionDao
{


	/**
	 * Finds all {@link RegionModel} instances.
	 */
	List<RegionModel> findRegions();

	/**
	 * Finds all {@link RegionModel} instances for a country.
	 */
	List<RegionModel> findRegionsByCountry(CountryModel country);

	/**
	 * Finds all {@link RegionModel} instances for a country full filling region code criteria.
	 */
	List<RegionModel> findRegionsByCountryAndCode(CountryModel country, String regionCode);


}
