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

import de.hybris.platform.core.Registry;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;


/**
 * Listener for order submits.
 */
public class SubmitOrderEventListener extends AbstractEventListener<SubmitOrderEvent>
{
	private static final Logger LOG = Logger.getLogger(SubmitOrderEventListener.class);

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
	protected void onEvent(final SubmitOrderEvent event)
	{
		LOG.info("Order Submitted .........");
		final OrderProcessModel businessProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"placeorder" + event.getOrder().getCode() + +System.currentTimeMillis(), "placeorder");
		businessProcessModel.setOrder(event.getOrder());
		getModelServiceViaLookup().save(businessProcessModel);
		getBusinessProcessService().startProcess(businessProcessModel);
	}
}
