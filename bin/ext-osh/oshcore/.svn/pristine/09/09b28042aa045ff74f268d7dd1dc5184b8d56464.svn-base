/**
 * 
 */
package com.hybris.osh.core.event;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.hybris.osh.events.PaymentFailedEvent;


/**
 * 
 */
public class PaymentFailedEventListner extends AbstractEventListener<PaymentFailedEvent>
{
	private static final Logger LOG = Logger.getLogger(PaymentFailedEventListner.class);


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
	protected void onEvent(final PaymentFailedEvent event)
	{
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"paymentFailedEmailProcess" + System.currentTimeMillis(), "paymentFailedEmailProcess");
		final OrderModel orderModel = event.getOrder();
		orderProcessModel.setOrder(orderModel);
		getModelServiceViaLookup().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
	}

}
