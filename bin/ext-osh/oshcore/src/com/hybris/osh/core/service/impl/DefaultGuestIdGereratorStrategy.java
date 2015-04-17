package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.GuestIdGeneratorStrategy;




/**
 * This Strategy used to generate unique Ids for customer as well unique UID for Guest User
 */
public class DefaultGuestIdGereratorStrategy implements GuestIdGeneratorStrategy
{
	protected static final Logger LOG = Logger.getLogger(DefaultGuestIdGereratorStrategy.class);


	@Override
	public void generateCustomerId(final CustomerModel customerModel)
	{
		customerModel.setCustomerID(UUID.randomUUID().toString());
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Guest Customer is -" + customerModel.getCustomerID());
		}
	}

	/**
	 * This method will generate a user uid using Secure Random class in java
	 * 
	 * @return int
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public int generateUid() throws NoSuchAlgorithmException
	{
		SecureRandom secureRandom = null;
		try
		{
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			final byte[] bytes = new byte[128];
			secureRandom.nextBytes(bytes);
			final int seedByteCount = 10;
			final byte[] seed = secureRandom.generateSeed(seedByteCount);
			secureRandom.setSeed(seed);
		}
		catch (final NoSuchAlgorithmException excep)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("NoSuchAlgorithmException -" + excep);
			}
		}
		return secureRandom.nextInt();
	}
}
