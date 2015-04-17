package com.hybris.osh.core.event;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.hybris.osh.events.ReturnOrderConfirmationEvent;


public class ReturnOrderConfirmationEventListener extends AbstractEventListener<ReturnOrderConfirmationEvent>
{

	private static final Logger LOG = Logger.getLogger(ReturnOrderConfirmationEventListener.class);

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
	protected void onEvent(final ReturnOrderConfirmationEvent event)
	{
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"returnOrderEmailProcess" + System.currentTimeMillis(), "returnOrderEmailProcess");
		final OrderModel orderModel = event.getProcess();
		orderProcessModel.setOrder(orderModel);
		getModelServiceViaLookup().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
	}


}
