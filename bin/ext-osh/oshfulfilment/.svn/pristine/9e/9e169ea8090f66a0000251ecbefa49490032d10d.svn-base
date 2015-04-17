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
package com.hybris.osh.actions;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.OrderSplittingService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;


/**
 * The Class SplitOrder.
 */
public class SplitOrder extends AbstractProceduralOrderAction implements ApplicationContextAware
{

	static private final Logger LOG = Logger.getLogger(SplitOrder.class);

	private OrderSplittingService orderSplittingService;
	private ApplicationContext applicationContext;

	@Override
	public void executeAction(final OrderProcessModel process) throws Exception //NOPMD
	{
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());


		//injections!!
		final List<ConsignmentModel> consignments = orderSplittingService.splitOrderForConsignment(process.getOrder(), process
				.getOrder().getEntries());

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Splitting order into " + consignments.size() + " consignments.");
		}

		final OrderModel orderModel = process.getOrder();

		for (final ConsignmentModel consignment : orderModel.getConsignments()) {
			for (final ConsignmentEntryModel consignmentEntry : consignment
					.getConsignmentEntries()) {
				consignmentEntry
						.setEntryLevelStatus(ConsignmentEntryStatus.PENDING);
				modelService.save(consignmentEntry);
			}
			consignment.setStatus(ConsignmentStatus.PENDING);

			modelService.save(consignment);
		}

		setOrderStatus(orderModel, OrderStatus.PENDING);
		modelService.save(orderModel);
		modelService.refresh(orderModel);
	}


	public BusinessProcessService getBusinessProcessService()
	{
		return applicationContext.getBean(BusinessProcessService.class);
	}


	public void setOrderSplittingService(final OrderSplittingService orderSplittingService)
	{
		this.orderSplittingService = orderSplittingService;
	}


	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;

	}
}
