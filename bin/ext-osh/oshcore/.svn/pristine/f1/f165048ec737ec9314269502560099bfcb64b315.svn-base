package com.hybris.osh.core.service;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;


public interface OshCustomerAccountService<CUSTOMER extends CustomerModel> extends CustomerAccountService
{
	public void oshUpdateProfile(CustomerModel customerModel, String title, String name, String uid, Long phone, String birthday,
			boolean specialoffer, boolean storenewsletter, boolean cluborchardinfo, String loyaltyNumber)
			throws DuplicateUidException;

	/**
	 * this method is used to update customer profile
	 * 
	 * @param customerModel
	 * @param name
	 * @param login
	 * @throws DuplicateUidException
	 */
	public void updateProfile(final CustomerModel customerModel, final String name, final String login)
			throws DuplicateUidException;

	/**
	 * this method will provide password token of customer
	 * 
	 * @param customerModel
	 * @return token
	 */
	public String getGuestUserPasswordToken(final CustomerModel customerModel);
}
