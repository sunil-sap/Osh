/**
 * 
 */
package com.hybris.osh.facades.storelocator.data;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;


/**
 * 
 */
public class OshPointOfServiceData extends PointOfServiceData
{

	private String state;
	private String storeID;
	private boolean active;

	/**
	 * @return the active
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * @param active
	 *           the active to set
	 */
	public void setActive(final boolean active)
	{
		this.active = active;
	}

	/**
	 * @return the storeID
	 */
	public String getStoreID()
	{
		return storeID;
	}

	/**
	 * @param storeID
	 *           the storeID to set
	 */
	public void setStoreID(final String storeID)
	{
		this.storeID = storeID;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}
}
