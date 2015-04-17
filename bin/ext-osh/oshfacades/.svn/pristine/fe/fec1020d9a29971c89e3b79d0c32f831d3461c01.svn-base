/**
 * 
 */
package com.hybris.osh.facades.customer;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;

import com.hybris.osh.facades.customer.data.OshCustomerData;


/**
 * 
 * This class is interface which extends customerfacade
 */
public interface OshCustomerFacade extends CustomerFacade<OshCustomerData>
{
	public void oshUpdateProfile(final OshCustomerData oshCustomerData) throws DuplicateUidException;

	/**
	 * This method will change the user group
	 * 
	 * @param userGroup
	 */
	public void changeUserGroup(final String userGroup);

	/**
	 * This method will provide customer's password token
	 * 
	 * @param uid
	 * @return token
	 */
	public String getPasswordToken(final String uid);


}
