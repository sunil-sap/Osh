/**
 * 
 */
package com.hybris.osh.cybersource.adapter.impl;

import de.hybris.platform.cybersource.adapter.impl.CybersourceFactoryImpl;
import de.hybris.platform.cybersource.api.v1_49.CCAuthReversalService;
import de.hybris.platform.cybersource.api.v1_49.ObjectFactory;

import com.hybris.osh.cybersource.adapter.OshCybersourceDTOFactory;


/**
 * 
 */
public class OshCybersourceFactoryImpl extends CybersourceFactoryImpl implements OshCybersourceDTOFactory
{


	public OshCybersourceFactoryImpl(final ObjectFactory objectFactory)
	{
		super(objectFactory);
	}

	@Override
	public CCAuthReversalService createCCAuthReversalService()
	{

		return getObjectFactory().createCCAuthReversalService();
	}

}
