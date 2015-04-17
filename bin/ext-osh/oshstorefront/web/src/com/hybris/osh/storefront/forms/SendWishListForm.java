/**
 *
 */
package com.hybris.osh.storefront.forms;

import javax.validation.constraints.NotNull;


/**
 * @author ubuntu
 *
 */
public class SendWishListForm
{

	@NotNull(message = "{general.required}")
	private String to;
	@NotNull(message = "{general.required}")
	private String wishListName;

	/**
	 * @return the to
	 */
	public String getTo()
	{
		return to;
	}

	/**
	 * @param to
	 *           the to to set
	 */
	public void setTo(final String to)
	{
		this.to = to;
	}

	/**
	 * @return the wishListName
	 */
	public String getWishListName()
	{
		return wishListName;
	}

	/**
	 * @param wishListName
	 *           the wishListName to set
	 */
	public void setWishListName(final String wishListName)
	{
		this.wishListName = wishListName;
	}



}
