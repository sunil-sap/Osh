package com.hybris.osh.core.service;



/**
 * this class is used to provide all the subscribed and unsubscribed customers
 */
public interface CheetahMailSubscriberService
{
	/*
	 * this method provides all the subscribed customers
	 */
	public void getSubscribedUser();

	/*
	 * this method provides all the unsubscribed customers
	 */
	public void getUnSubscribedUser();

	public boolean setSubscriber(String emailId, boolean subscriber);

	public boolean setSubscriber(String emailId, boolean subscriber, final String postalCode);

	public boolean unSubscribeCheetahMail(final String emailId, final boolean subscriber);
}
