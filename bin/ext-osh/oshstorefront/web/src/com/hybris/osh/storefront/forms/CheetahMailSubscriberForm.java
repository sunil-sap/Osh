/**
 * 
 */
package com.hybris.osh.storefront.forms;

public class CheetahMailSubscriberForm
{

	private String emailId;
	private String postalCode;
	private boolean subscriber;

	/**
	 * @return the emailId
	 */
	public String getEmailId()
	{
		return emailId;
	}

	/**
	 * @param emailId
	 *           the emailId to set
	 */
	public void setEmailId(final String emailId)
	{
		this.emailId = emailId;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param postalCode
	 *           the postalCode to set
	 */
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * @return the subscriber
	 */
	public boolean isSubscriber()
	{
		return subscriber;
	}

	/**
	 * @param subscriber
	 *           the subscriber to set
	 */
	public void setSubscriber(final boolean subscriber)
	{
		this.subscriber = subscriber;
	}

}
