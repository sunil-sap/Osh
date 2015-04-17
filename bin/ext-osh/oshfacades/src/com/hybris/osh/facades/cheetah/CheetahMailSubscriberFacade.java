/**
 * 
 */
package com.hybris.osh.facades.cheetah;

/**
 * 
 */
public interface CheetahMailSubscriberFacade
{
	/**
	 * 
	 * @param emailId
	 * @param isSubscriber
	 * @return
	 */
	public boolean setSubscriber(String emailId, boolean subscriber);


	/**
	 * 
	 * @param emailId
	 * @param isSubscriber
	 * @param postalCode
	 * @return
	 */
	public boolean setSubscriber(String emailId, boolean subscriber, String postalCode);

	/**
	 * 
	 * @param emailId
	 * @param isSubscriber
	 */
	public boolean unSubscribeCheetahMail(final String emailId, final boolean subscriber);
}
