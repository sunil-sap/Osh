/**
 *
 */
package com.hybris.osh.core.service.impl;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cybersource.ws.client.Client;
import com.cybersource.ws.client.ClientException;
import com.cybersource.ws.client.FaultException;
import com.hybris.osh.core.service.OshTaxService;


public class OshTaxServiceImpl implements OshTaxService
{

	protected static final Logger LOG = Logger.getLogger(OshTaxServiceImpl.class);


	@Override
	public HashMap calculateTax(final HashMap request, final Properties cybersourceProperties)
	{
		HashMap reply = new HashMap();
		try
		{
			reply = Client.runTransaction(request, cybersourceProperties);
			LOG.info(reply);
		}
		catch (final FaultException e)
		{
			LOG.info(e.getLogString());
		}
		catch (final ClientException e)
		{
			LOG.info(e.getLogString());
		}

		return reply;
	}


}
