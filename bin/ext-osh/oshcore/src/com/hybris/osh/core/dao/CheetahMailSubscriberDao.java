/**
 * 
 */
package com.hybris.osh.core.dao;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.hybris.osh.core.model.EmailSubscriberModel;


public interface CheetahMailSubscriberDao
{
	/**
	 * this method will find all the cheetah mail subscribed user
	 * 
	 * @return list Of all the subscribed user
	 */
	public List<EmailSubscriberModel> findCheetahMailSubscribedCustomer();

	/**
	 * this method will find all the cheetah mail unsubscribed user
	 * 
	 * @return list Of all the unsubscribed user
	 */
	public List<EmailSubscriberModel> findCheetahMailUnSubscribedCustomer();

	public CustomerModel findEmailSubscribedCustomer(final String emailId);

	public EmailSubscriberModel findEmailSubscriber(final String emailId);

}
