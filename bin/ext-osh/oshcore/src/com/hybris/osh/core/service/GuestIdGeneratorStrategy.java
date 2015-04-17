/**
 * 
 */
package com.hybris.osh.core.service;

import de.hybris.platform.core.model.user.CustomerModel;

import java.security.NoSuchAlgorithmException;


/**
 *
 */
public interface GuestIdGeneratorStrategy
{
	/**
	 * Generates a customer ID during registration
	 * 
	 * @param customerModel
	 */
	void generateCustomerId(final CustomerModel customerModel);

	/**
	 * This method will generate a user id using Secure Random class in java
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public int generateUid() throws NoSuchAlgorithmException;
}
