/**
 * 
 */
package com.hybris.osh.facades.product.converters;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commercefacades.product.converters.ProductReferenceConverter;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;

import org.springframework.util.Assert;

import com.hybris.osh.facades.product.data.OshProductReferenceData;


public class OshProductReferenceConverter extends ProductReferenceConverter
{
	//int i = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commercefacades.product.converters.ProductReferenceConverter#populate(de.hybris.platform.catalog
	 * .model.ProductReferenceModel, de.hybris.platform.commercefacades.product.data.ProductReferenceData)
	 */
	@Override
	public void populate(final ProductReferenceModel source, final ProductReferenceData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final OshProductReferenceData oshTarget = (OshProductReferenceData) target;

		if (!(target instanceof OshProductReferenceData))
		{
			throw new IllegalStateException("Found incompatible objects of " + target.getClass());
		}
		populateCustomAttributes(source, oshTarget);
		super.populate(source, target);
	}



	private void populateCustomAttributes(final ProductReferenceModel productReferenceModel,
			final OshProductReferenceData oshProductReferenceData)
	{


		//productReferenceModel.setPriority(new Integer(i).toString());
		//	i++;
		oshProductReferenceData.setPriority(productReferenceModel.getPriority() != null ? productReferenceModel.getPriority()
				: null);


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.converters.ProductReferenceConverter#createTarget()
	 */
	@Override
	protected ProductReferenceData createTarget()
	{
		// YTODO Auto-generated method stub
		return super.createTarget();
	}



}
