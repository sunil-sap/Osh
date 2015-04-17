/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.core.event;

import de.hybris.platform.commerceservices.event.ForgottenPwdEvent;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Listener for "forgotten password" functionality event.
 */
public class ForgottenPasswordEventListener extends AbstractEventListener<ForgottenPwdEvent>
{
	public BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}

	public ModelService getModelServiceViaLookup()
	{
		throw new UnsupportedOperationException(
				"Please define in the spring configuration a <lookup-method> for getModelServiceViaLookup().");
	}

	@Override
	protected void onEvent(final ForgottenPwdEvent forgottenPwdEvent)
	{
		final ForgottenPasswordProcessModel forgottenPasswordProcessModel = (ForgottenPasswordProcessModel) getBusinessProcessService()
				.createProcess("forgottenPassword" + System.currentTimeMillis(), "forgottenPasswordEmailProcess");
		forgottenPasswordProcessModel.setSite(forgottenPwdEvent.getSite());
		forgottenPasswordProcessModel.setCustomer(forgottenPwdEvent.getCustomer());
		forgottenPasswordProcessModel.setToken(forgottenPwdEvent.getToken());
		getModelServiceViaLookup().save(forgottenPasswordProcessModel);
		getBusinessProcessService().startProcess(forgottenPasswordProcessModel);
	}
}
