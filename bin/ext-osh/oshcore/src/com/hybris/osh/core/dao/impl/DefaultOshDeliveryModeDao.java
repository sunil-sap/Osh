/**
 * 
 */
package com.hybris.osh.core.dao.impl;


import de.hybris.platform.order.daos.impl.DefaultDeliveryModeDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hybris.osh.core.dao.OshDeliveryModeDao;
import com.hybris.osh.core.model.OSHShippingModel;


/*
 * For fetch the value of shipping price on the basis of weight and zipcode of the product.
 * */
public class DefaultOshDeliveryModeDao extends DefaultDeliveryModeDao implements OshDeliveryModeDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@SuppressWarnings("boxing")
	@Override
	public List<OSHShippingModel> findOSHShippingModelWithoutHomeDirectMode(final String state, final double weight,
			final String zipcode)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {pk} from {OSHShipping} where {zipcodefrom}<=?zipcode AND {zipcodeto}>=?zipcode And {weightfrom}<=?weight AND {weightto}>=?weight AND {state}=?state");
		params.put("state", state);
		params.put("weight", weight);
		params.put("zipcode", zipcode);
		query.addQueryParameters(params);
		final SearchResult<OSHShippingModel> oshShippingModels = flexibleSearchService.search(query);
		final List<OSHShippingModel> homeDirectModeModel = findOSHShippingModelWithHomeDirectMode(state, weight);
		final List<OSHShippingModel> newDeliveryModes = new ArrayList<OSHShippingModel>();
		if (oshShippingModels != null)
		{
			final List<OSHShippingModel> getShippingResult = oshShippingModels.getResult();
			for (final OSHShippingModel modifyOSHShippingModel : getShippingResult)
			{
				newDeliveryModes.add(modifyOSHShippingModel);
			}
			for (final OSHShippingModel oSHShippingModel : homeDirectModeModel)
			{
				newDeliveryModes.add(oSHShippingModel);
			}
			return newDeliveryModes;
		}
		return null;

	}

	@SuppressWarnings("boxing")
	@Override
	public List<OSHShippingModel> findOSHShippingModelWithHomeDirectMode(final String state, final double weight)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {pk} from {OSHShipping} where {shipper}='Home_Direct' And {weightfrom}<=?weight AND {weightto}>=?weight AND {state}=?state");
		params.put("state", state);
		params.put("weight", weight);
		query.addQueryParameters(params);
		final SearchResult<OSHShippingModel> oshShippingModels = flexibleSearchService.search(query);
		if (oshShippingModels != null)
		{
			return oshShippingModels.getResult();
		}
		return null;

	}

	@SuppressWarnings("boxing")
	@Override
	public List<OSHShippingModel> findOSHShippingModelWithOtherMode(final String state, final double weight, final String zipcode)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {pk} from {OSHShipping} where {zipcodefrom}<=?zipcode AND {zipcodeto}>=?zipcode And {weightfrom}<=?weight AND {weightto}>=?weight AND {state}=?state");
		params.put("state", state);
		params.put("weight", weight);
		params.put("zipcode", zipcode);
		query.addQueryParameters(params);
		final SearchResult<OSHShippingModel> oshShippingModels = flexibleSearchService.search(query);
		if (oshShippingModels != null)
		{
			return oshShippingModels.getResult();
		}
		return null;

	}

	@Override
	public long findMaxShippingCode()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {oshshipping} ");
		final SearchResult<OSHShippingModel> result = flexibleSearchService.search(query);
		return result.getResult().size();
	}

	@Override
	public List<OSHShippingModel> minMaxRangeOfZipcode(final String state)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {oshshipping} where {state}=?state");
		params.put("state", state);
		query.addQueryParameters(params);
		final SearchResult<OSHShippingModel> result = flexibleSearchService.search(query);
		return result.getResult();

	}

	@Override
	public List<OSHShippingModel> minMaxRangeOfWeight(final String state, final String zipcode)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {pk} from {oshshipping} where {state}=?state AND {zipcodefrom}<=?zipcode AND {zipcodeto}>=?zipcode");
		params.put("state", state);
		params.put("zipcode", zipcode);
		query.addQueryParameters(params);
		final SearchResult<OSHShippingModel> result = flexibleSearchService.search(query);
		return result.getResult();

	}
}
