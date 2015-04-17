/**
 * 
 */
package com.hybris.osh.core.dao;

import java.util.List;

import com.hybris.osh.core.model.OSHShippingModel;


public interface OshDeliveryModeDao
{
	public List<OSHShippingModel> findOSHShippingModelWithoutHomeDirectMode(final String state, final double weight,
			final String zipcode);

	public List<OSHShippingModel> findOSHShippingModelWithHomeDirectMode(final String state, final double weight);

	public List<OSHShippingModel> findOSHShippingModelWithOtherMode(final String state, final double weight, final String zipcode);

	public long findMaxShippingCode();

	public List<OSHShippingModel> minMaxRangeOfZipcode(final String state);

	public List<OSHShippingModel> minMaxRangeOfWeight(final String state, final String zipcode);
}
