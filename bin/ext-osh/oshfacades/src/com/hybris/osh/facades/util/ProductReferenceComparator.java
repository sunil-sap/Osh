/**
 * 
 */
package com.hybris.osh.facades.util;

import de.hybris.platform.catalog.model.ProductReferenceModel;

import java.util.Comparator;


public class ProductReferenceComparator implements Comparator<ProductReferenceModel>
{


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final ProductReferenceModel prm1, final ProductReferenceModel prm2)
	{
		if (prm1.getPriority() != null && prm2.getPriority() != null)
		{
			return prm1.getPriority().compareTo(prm2.getPriority());
		}
		else
		{
			return 0;
		}

	}

}
