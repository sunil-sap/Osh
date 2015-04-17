/**
 * 
 */
package com.hybris.osh.facades.cheetah.impl;

import javax.annotation.Resource;

import com.hybris.osh.core.service.CheetahMailSubscriberService;
import com.hybris.osh.facades.cheetah.CheetahMailSubscriberFacade;



/**
 * 
 */
public class DefaultCheetahMailSubscriberFacade implements CheetahMailSubscriberFacade
{
	@Resource
	private CheetahMailSubscriberService cheetahMailSubscriberService;


	@Override
	public boolean setSubscriber(final String emailId, final boolean subscriber, final String postalCode)
	{

		return getCheetahMailSubscriberService().setSubscriber(emailId, subscriber, postalCode);
	}

	/**
	 * @return the cheetahMailSubscriberService
	 */
	public CheetahMailSubscriberService getCheetahMailSubscriberService()
	{
		return cheetahMailSubscriberService;
	}

	/**
	 * @param cheetahMailSubscriberService
	 *           the cheetahMailSubscriberService to set
	 */
	public void setCheetahMailSubscriberService(final CheetahMailSubscriberService cheetahMailSubscriberService)
	{
		this.cheetahMailSubscriberService = cheetahMailSubscriberService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.cheetah.CheetahMailSubscriberFacade#setSubscriber(java.lang.String, boolean)
	 */
	@Override
	public boolean setSubscriber(final String emailId, final boolean subscriber)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.cheetah.CheetahMailSubscriberFacade#unSubscribeCheetahMail(java.lang.String, boolean)
	 */
	@Override
	public boolean unSubscribeCheetahMail(final String emailId, final boolean subscriber)
	{
		return getCheetahMailSubscriberService().unSubscribeCheetahMail(emailId, subscriber);

	}
}
