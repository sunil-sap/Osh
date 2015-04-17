package com.hybris.osh.core.service;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.security.NoSuchAlgorithmException;


public interface GuestCustomerAccountService
{
	/**
	 * This method is called during guest checkout for registering the user into Hybris User needs to be inserted into a
	 * newly created group called "guestcustomergroup"
	 * 
	 * @param newCustomer
	 * @throws DuplicateUidException
	 */
	public abstract void registerGuest(final CustomerModel newCustomer) throws DuplicateUidException, NoSuchAlgorithmException;

	/**
	 * Auto Logout for guest user
	 */
	public void logout();

	/**
	 * Generates a UID during registration
	 * 
	 * @return generated UID
	 */
	public String generateUid() throws NoSuchAlgorithmException;

	/**
	 * check for valid guest user, if user with email exist make him sign in
	 */
	public boolean isUserExist(String emailId);

	public boolean setCustomerAccNo(CustomerModel customer);

}
