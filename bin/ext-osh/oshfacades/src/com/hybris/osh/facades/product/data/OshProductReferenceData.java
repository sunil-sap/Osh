/**
 * 
 */
package com.hybris.osh.facades.product.data;

import de.hybris.platform.commercefacades.product.data.ProductReferenceData;

public class OshProductReferenceData extends ProductReferenceData
{
	private String priority;

	/**
	 * @return the priority
	 */
	public String getPriority()
	{
		return priority;
	}

	/**
	 * @param priorityx
	 *           the priority to set
	 */
	public void setPriority(final String priority)
	{
		this.priority = priority;
	}

}
