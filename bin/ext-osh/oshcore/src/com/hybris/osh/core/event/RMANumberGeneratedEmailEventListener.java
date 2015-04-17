package com.hybris.osh.core.event;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hybris.osh.events.RMAGeneratedEmailEvent;


public class RMANumberGeneratedEmailEventListener extends AbstractEventListener<RMAGeneratedEmailEvent>
{

	private static final Logger LOG = Logger.getLogger(RMANumberGeneratedEmailEventListener.class);

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
	protected void onEvent(final RMAGeneratedEmailEvent event)
	{

		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"RMANumberEmailProcess" + System.currentTimeMillis(), "RMANumberEmailProcess");
		final OrderModel orderModel = (OrderModel) event.getOrder();
		final Set<ConsignmentModel> consignments = new HashSet<ConsignmentModel>();
		consignments.add(event.getConsignment());
		orderModel.setConsignments(consignments);
		orderProcessModel.setOrder(orderModel);
		getModelServiceViaLookup().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
	}


}
