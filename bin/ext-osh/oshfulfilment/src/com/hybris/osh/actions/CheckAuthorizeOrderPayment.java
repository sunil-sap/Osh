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

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * This action implements payment authorization using {@link CreditCardPaymentInfoModel}. Any other payment model could
 * be implemented here, or in a separate action, if the process flow differs.
 */
public class CheckAuthorizeOrderPayment extends AbstractSimpleDecisionAction
{


	@Override
	public Transition executeAction(final BusinessProcessModel process)
	{
		final OrderModel order = ((OrderProcessModel) process).getOrder();

		for (final PaymentTransactionModel transaction : order.getPaymentTransactions())
		{
			for (final PaymentTransactionEntryModel entry : transaction.getEntries())
			{
				if (entry.getType().equals(PaymentTransactionType.AUTHORIZATION))
				{
					order.setStatus(OrderStatus.PAYMENT_AUTHORIZED);
					modelService.save(order);
					return Transition.OK;
				}
			}
		}

		return Transition.NOK;
	}
}
