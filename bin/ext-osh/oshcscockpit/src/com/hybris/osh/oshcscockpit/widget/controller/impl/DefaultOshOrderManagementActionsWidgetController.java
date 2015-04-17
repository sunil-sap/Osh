/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.oshcscockpit.widget.controller.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.events.OrderEvent;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Map;

import org.apache.log4j.Logger;

import com.hybris.osh.oshcscockpit.editor.OshEditorHelper;
import com.hybris.osh.oshcscockpit.widget.controller.OshOrderManagementActionsWidgetController;



public class DefaultOshOrderManagementActionsWidgetController extends
		de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController implements
		OshOrderManagementActionsWidgetController
{
	private static final Logger LOG = Logger.getLogger(OshEditorHelper.class);
	private CallContextController callContextController;
	private OrderCancelService orderCancelService;
	private UserService userService;

	public DefaultOshOrderManagementActionsWidgetController()
	{
	}

	@Override
	protected CallContextController getCallContextController()
	{
		return callContextController;
	}

	@Override
	public void setCallContextController(final CallContextController callContextController)
	{
		this.callContextController = callContextController;
	}

	@Override
	protected OrderCancelService getOrderCancelService()
	{
		return orderCancelService;
	}

	@Override
	public void setOrderCancelService(final OrderCancelService orderCancelService)
	{
		this.orderCancelService = orderCancelService;
	}


	@Override
	protected UserService getUserService()
	{
		return userService;
	}

	@Override
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	@Override
	public void dispatchEvent(final String context, final Object source, final Map data)
	{
		dispatchEvent("csCtx", ((new OrderEvent(source, context))));
		getCallContextController().dispatchEvent(null, source, data);
	}

	@Override
	public boolean isOrderUpdatePossible()
	{

		return true;
	}

	@Override
	public boolean isCustomerCreditPossible()
	{

		/*
		 * final TypedObject order = getOrder(); if ((order != null) && (order.getObject() instanceof OrderModel) &&
		 * (StringUtils.isBlank(((OrderModel) order.getObject()).getVersionID()))) { try { final OrderModel orderModel =
		 * (OrderModel) order.getObject(); final Iterator<PaymentTransactionModel> itrpaymentTransactionModel =
		 * orderModel.getPaymentTransactions().iterator(); PaymentTransactionModel paymentTransactionModel = null; while
		 * (itrpaymentTransactionModel.hasNext()) { paymentTransactionModel = itrpaymentTransactionModel.next(); if
		 * (paymentTransactionModel != null) { break; } } final Iterator<PaymentTransactionEntryModel>
		 * itrPaymentTransactionEntryModel = paymentTransactionModel.getEntries() .iterator(); while
		 * (itrPaymentTransactionEntryModel.hasNext()) { final PaymentTransactionEntryModel paymentTransactionEntryModel =
		 * itrPaymentTransactionEntryModel.next(); final String paymentType =
		 * paymentTransactionEntryModel.getType().getCode(); if
		 * (paymentType.equals(PaymentTransactionType.CAPTURE.getCode()) ||
		 * paymentType.equals(PaymentTransactionType.PARTIAL_CAPTURE.getCode())) { if
		 * (paymentTransactionEntryModel.getTransactionStatus().equals("ACCEPTED")) { return true; } } } return false; }
		 * catch (final Exception e) { LOG.error("failed to work out if cancellation or order is possible", e); return
		 * false; } }
		 */
		return false;
	}

	@Override
	public TypedObject getOrder()
	{

		return getCallContextController().getCurrentOrder();

	}

}
