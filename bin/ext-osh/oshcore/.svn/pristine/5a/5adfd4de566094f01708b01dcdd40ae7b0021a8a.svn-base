/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hybris.osh.core.dao.CheetahMailSubscriberDao;
import com.hybris.osh.core.model.EmailSubscriberModel;


public class DefaultCheetahMailSubscriberDao implements CheetahMailSubscriberDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<EmailSubscriberModel> findCheetahMailSubscribedCustomer()
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		List<EmailSubscriberModel> subscribedCustomers = new ArrayList<EmailSubscriberModel>();
		final boolean status = true;
		final FlexibleSearchQuery query = new FlexibleSearchQuery("Select {pk} from {EmailSubscriber} where {subscriber} = ?status");
		params.put("status", status);
		query.addQueryParameters(params);
		final SearchResult<EmailSubscriberModel> result = flexibleSearchService.search(query);
		if (result != null)
		{
			subscribedCustomers = result.getResult();
		}
		return subscribedCustomers;
	}

	@Override
	public List<EmailSubscriberModel> findCheetahMailUnSubscribedCustomer()
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		List<EmailSubscriberModel> subscribedCustomers = new ArrayList<EmailSubscriberModel>();
		final boolean status = false;
		final FlexibleSearchQuery query = new FlexibleSearchQuery("Select {pk} from {EmailSubscriber} where {subscriber} = ?status");
		params.put("status", status);
		query.addQueryParameters(params);
		final SearchResult<EmailSubscriberModel> result = flexibleSearchService.search(query);
		if (result != null)
		{
			subscribedCustomers = result.getResult();
		}
		return subscribedCustomers;
	}

	@Override
	public CustomerModel findEmailSubscribedCustomer(final String emailId)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		CustomerModel customer = null;
		final FlexibleSearchQuery query = new FlexibleSearchQuery("Select {pk} from {Customer} where {uid} = ?emailId");
		params.put("emailId", emailId);
		query.addQueryParameters(params);
		final SearchResult<CustomerModel> result = flexibleSearchService.search(query);
		if (result != null && (!result.getResult().isEmpty()))
		{
			customer = result.getResult().get(0);
		}
		return customer;
	}


	@Override
	public EmailSubscriberModel findEmailSubscriber(final String emailId)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		EmailSubscriberModel emailSubscriber = null;
		final FlexibleSearchQuery query = new FlexibleSearchQuery("Select {pk} from {EmailSubscriber} where {emailId} = ?emailId");
		params.put("emailId", emailId);
		query.addQueryParameters(params);
		final SearchResult<EmailSubscriberModel> result = flexibleSearchService.search(query);
		if (result != null && (!result.getResult().isEmpty()))
		{
			emailSubscriber = result.getResult().get(0);
		}
		return emailSubscriber;
	}
}
