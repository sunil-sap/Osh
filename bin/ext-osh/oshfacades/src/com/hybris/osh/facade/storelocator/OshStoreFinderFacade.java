/**
 * 
 */
package com.hybris.osh.facade.storelocator;

import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;


public interface OshStoreFinderFacade extends StoreFinderFacade
{

	/**
	 * Checking the locationQuery .. if it is a state Name then convert it to proper string that hybris code required to
	 * reterive store information from hybris platform
	 * 
	 * 
	 */
	public String queryInputProcesssing(final String locationQuery);
}
