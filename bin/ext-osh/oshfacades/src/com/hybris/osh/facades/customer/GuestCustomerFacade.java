/**
 * 
 */
package com.hybris.osh.facades.customer;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.security.NoSuchAlgorithmException;



/**
 * 
 *
 */
public interface GuestCustomerFacade<D>
{
	/**
	 * Allow user to checkout without login in the system.
	 * 
	 * @param registerData
	 *           the user data the user will be registered with
	 * @throws IllegalArgumentException
	 *            if required data is missing
	 * @throws UnknownIdentifierException
	 *            if the title code is invalid
	 * @throws DuplicateUidException
	 *            if the login is not unique
	 */
	void registerGuest(D registerData, String store) throws DuplicateUidException, UnknownIdentifierException,
			IllegalArgumentException, NoSuchAlgorithmException;

	/**
	 * Auto logout for guest after checkout process
	 */
	public void logout();

	/**
	 * Login is disabled for guest user
	 */
	public void loginDisabled();

	/**
	 * Generates a UID during registration
	 * 
	 * @return
	 */
	public String generateUid() throws NoSuchAlgorithmException;

	/**
	 * check for valid guest user, if user with email exist make him sign in
	 */
	public boolean isUserExist(String emailId);
}
