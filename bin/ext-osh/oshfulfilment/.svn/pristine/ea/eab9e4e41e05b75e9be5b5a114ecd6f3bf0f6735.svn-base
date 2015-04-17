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

import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.ClientExecuter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.fulfilmentservices.CalculateOrderStatusService;
import com.hybris.osh.order.cancel.strategy.CancelOrderStrategy;
import com.hybris.osh.payment.services.OshPaymentService;


/**
 * Cancels the authorized payment. The code expects previously authorized payment transaction, otherwise the order is
 * set to PROCESSING_ERROR status.
 */
public class CancelWholeOrderAuthorization extends AbstractSimpleDecisionAction
{

	private PaymentService paymentService;
	private CommonI18NService commonI18NService;
	private UserService userService;
	private OshPaymentService oshPaymentService;

	private CancelOrderStrategy cancelOrderStrategy;

	@Resource(name = "calculateOrderStatusService")
	CalculateOrderStatusService calculateOrderStatusService;
	
	@Override
	public Transition executeAction(final BusinessProcessModel process)
	{
		Logger.getLogger(this.getClass()).debug("The transaction is being cancelled.");

		boolean status = false;
		final BusinessProcessModel bpm = process;
		final OrderModel order = ((OrderProcessModel) bpm).getOrder();
		ConsignmentModel consignment= order.getConsignments().iterator().next();
		final List<PaymentTransactionModel> txns = order.getPaymentTransactions();

		// sanity check. There could be a problem with this order.
		if (txns.isEmpty())
		{
			Logger.getLogger(this.getClass()).error("Processing error - missing or ambiguous transaction.");
			setOrderStatus(order, OrderStatus.PROCESSING_ERROR);
			return Transition.NOK;
		}

		final PaymentTransactionModel txn = txns.iterator().next();
		final List<PaymentTransactionEntryModel> txnEntries = txn.getEntries();
		// another sanity check. Also here could be a problem with this order.
		if (txnEntries.isEmpty())
		{
			Logger.getLogger(this.getClass()).error("Processing error - missing or ambiguous transaction entries.");
			setOrderStatus(order, OrderStatus.PROCESSING_ERROR);
			return Transition.NOK;
		}

		List<PaymentTransactionEntryModel> paymentTxnEntries = txn.getEntries();
		final PaymentTransactionEntryModel txnEntry = paymentTxnEntries.get(paymentTxnEntries.size() - 1);
		/* final PaymentTransactionEntryModel txnEntry = txnEntries.iterator().next(); */
		final Currency currency = Currency.getInstance(order.getCurrency().getIsocode());
		final String merchantTransactionCode = txnEntry.getCode();
		final BigDecimal amount = txnEntry.getAmount();

		PaymentTransactionEntryModel txnResultEntry=null;
		try
		{
		txnResultEntry = oshPaymentService.fullReverseAuth(txn, merchantTransactionCode,
				txnEntry.getRequestId(), currency, txnEntry.getAmount(), txnEntry.getPaymentTransaction().getPaymentProvider());
		}
		catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e);
		}
		
		final OrderModificationRecordModel modifiedRecord = order.getModificationRecords().iterator().next();
		final Collection<OrderModificationRecordEntryModel> modifiedEntries = modifiedRecord.getModificationRecordEntries();

		if (txnResultEntry!=null && txnResultEntry.getTransactionStatus().equalsIgnoreCase(TransactionStatus.ACCEPTED.name()))
		{
			for (OrderModificationRecordEntryModel modifiedEntry : modifiedEntries)
			{
				final OrderCancelRecordEntryModel cancelOrderEntries = (OrderCancelRecordEntryModel) modifiedEntry;
				if (!cancelOrderEntries.getStatus().getCode().equals("SUCCESSFULL"))
				{

					final Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries = cancelOrderEntries
							.getOrderEntriesModificationEntries();

					if (cancelOrderEntries.getCancelResult().getCode().equals("PARTIAL"))
					{
						String cancelStatus = null;
						try
						{
							cancelStatus = getCancelOrderStrategy().AutherizeCancelableOrderEntries(orderModifiedEntries, order,
									txnResultEntry);
						}
						catch (Exception e)
						{
							Logger.getLogger(this.getClass()).error(e);
							PaymentTransactionEntryModel auth = null;
							for (final Iterator iterator = txn.getEntries().iterator(); iterator.hasNext();)
							{
								final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
								if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION)
										&& pte.getTransactionStatus().equals("ACCEPTED"))
								{
									auth = pte;
								}
							}
							
							setModificationRecordStatusFailed(modifiedEntries);
							modifiedRecord.setInProgress(false);
							modelService.save(modifiedRecord);
							modelService.refresh(modifiedRecord);
							final PaymentTransactionEntryModel	paymentTransactionEntry=oshPaymentService.authorize(txn, auth.getAmount(), currency, order.getPaymentAddress(),
									txnEntry.getSubscriptionID(), txnEntry.getPaymentTransaction().getPaymentProvider());
							cancelTransactionFailedMessage(order);
							 if(paymentTransactionEntry.getTransactionStatus().equals("REJECTED"))
							{
								cancelTransactionReAuthFailedMessage(order);
							}
							calculateOrderStatusService.setOrderStatus(consignment);
							order.setTransactionComplete(true);
							modelService.save(order);
							modelService.refresh(order);
							return Transition.NOK;
						}

						
						if (cancelStatus != null && cancelStatus.equals("ACCEPTED"))
						{
							setModificationRecordStatusSuccessfull(modifiedEntries);
							modifiedRecord.setInProgress(false);
							cancelOrderEntries.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
							modelService.save(modifiedRecord);
							modelService.save(cancelOrderEntries);
							status = true;
							getCancelOrderStrategy().setStatusForCancelAndShippedOrder(orderModifiedEntries);
							//	calculateOrderStatusService.setOrderStatus(consignment);

						}
						else if (cancelStatus != null && cancelStatus.equals("ZEROPRICE"))
						{
							if(getCancelOrderStrategy().setOrderStatusForCancelAndShippedOrder(order)){
								setOrderStatus(order, OrderStatus.PARTIAL_FULFILLED);
							}
							else{
								setOrderStatus(order, OrderStatus.CANCELLED);
							}
							setModificationRecordStatusSuccessfull(modifiedEntries);
							modifiedRecord.setInProgress(false);
							modelService.save(modifiedRecord);
							modelService.refresh(modifiedRecord);
							
							getCancelOrderStrategy().setStatusForCancelAndShippedOrder(orderModifiedEntries);
							//calculateOrderStatusService.setOrderStatus(consignment);
							status = true;
							//cancelOrderEntries.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
						}
						else
						{
							PaymentTransactionEntryModel auth = null;
							for (final Iterator iterator = txn.getEntries().iterator(); iterator.hasNext();)
							{
								final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
								if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION)
										&& pte.getTransactionStatus().equals("ACCEPTED"))
								{
									auth = pte;
								}
								
								//cancelOrderEntries.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
								
							}
							setModificationRecordStatusFailed(modifiedEntries);
							modifiedRecord.setInProgress(false);
							modelService.save(modifiedRecord);
							modelService.refresh(modifiedRecord);
							final PaymentTransactionEntryModel paymentTransactionEntry=oshPaymentService.authorize(txn, auth.getAmount(), currency, order.getPaymentAddress(),
									txnEntry.getSubscriptionID(), txnEntry.getPaymentTransaction().getPaymentProvider());
							cancelTransactionFailedMessage(order);
							 if(paymentTransactionEntry.getTransactionStatus().equals("REJECTED"))
							{
								cancelTransactionReAuthFailedMessage(order);
							}
							 calculateOrderStatusService.setOrderStatus(consignment);
							order.setTransactionComplete(true);
							modelService.save(order);
							modelService.refresh(order);
							return Transition.NOK;

						}
						calculateOrderStatusService.setOrderStatus(consignment);
						order.setTransactionComplete(true);
						modelService.save(order);
						modelService.refresh(order);
						return Transition.OK;
					}
					if (!status)
					{
						setModificationRecordStatusSuccessfull(modifiedEntries);
						modifiedRecord.setInProgress(false);
						modelService.save(modifiedRecord);
						modelService.refresh(modifiedRecord);
						getCancelOrderStrategy().setConsignmentStatusForCancelOrder(orderModifiedEntries);
						setOrderStatus(order, OrderStatus.CANCELLED);
						Logger.getLogger(this.getClass()).debug("Cancel successfull.");
						
						order.setTransactionComplete(true);
						modelService.save(order);
						modelService.refresh(order);
						return Transition.OK;
					}
				}
			}
		}
		else
		{
			setModificationRecordStatusFailed(modifiedEntries);
			cancelTransactionFailedMessage(order);
			modifiedRecord.setInProgress(false);
			modelService.save(modifiedRecord);
			modelService.refresh(modifiedRecord);
			calculateOrderStatusService.setOrderStatus(consignment);
			order.setTransactionComplete(true);
			modelService.save(order);
			modelService.refresh(order);
			return Transition.NOK;
		}

		return Transition.NOK;
	}

	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the oshPaymentService
	 */
	public OshPaymentService getOshPaymentService()
	{
		return oshPaymentService;
	}

	/**
	 * @param oshPaymentService
	 *           the oshPaymentService to set
	 */
	public void setOshPaymentService(final OshPaymentService oshPaymentService)
	{
		this.oshPaymentService = oshPaymentService;
	}

	/**
	 * @return the cancelOrderStrategy
	 */
	public CancelOrderStrategy getCancelOrderStrategy()
	{
		return cancelOrderStrategy;
	}

	/**
	 * @param cancelOrderStrategy
	 *           the cancelOrderStrategy to set
	 */
	public void setCancelOrderStrategy(final CancelOrderStrategy cancelOrderStrategy)
	{
		this.cancelOrderStrategy = cancelOrderStrategy;
	}


	public void cancelTransactionReAuthFailedMessage(final OrderModel order)
	{
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(new Date());
		historyEntry.setOrder(order);
		historyEntry.setDescription("Cancellation failed for Order "+order.getCode()+" (Please cancelled it through manual process)");
		modelService.save(historyEntry);
		modelService.refresh(historyEntry);
	}
	
	public void cancelTransactionFailedMessage(final OrderModel order)
	{
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(new Date());
		historyEntry.setOrder(order);
		historyEntry.setDescription("Cancellation failed for Order "+order.getCode());
		modelService.save(historyEntry);
		modelService.refresh(historyEntry);
	}
	
	public void setModificationRecordStatusFailed(final Collection<OrderModificationRecordEntryModel>modifiedEntries)
	{
		if (modifiedEntries != null && !modifiedEntries.isEmpty()) {
			final OrderModificationRecordEntryModel orderModificationRecordEntryModel = (OrderModificationRecordEntryModel) modifiedEntries
					.toArray()[modifiedEntries.size() - 1];
			orderModificationRecordEntryModel
					.setStatus(OrderModificationEntryStatus.FAILED);
			modelService.save(orderModificationRecordEntryModel);
			modelService.refresh(orderModificationRecordEntryModel);
		}

	}
	
	public void setModificationRecordStatusSuccessfull(final Collection<OrderModificationRecordEntryModel>modifiedEntries)
	{
		if (modifiedEntries != null && !modifiedEntries.isEmpty()) {
			final OrderModificationRecordEntryModel orderModificationRecordEntryModel = (OrderModificationRecordEntryModel) modifiedEntries
					.toArray()[modifiedEntries.size() - 1];
			orderModificationRecordEntryModel
					.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
			modelService.save(orderModificationRecordEntryModel);
			modelService.refresh(orderModificationRecordEntryModel);
		}

	}
	
	
	

}
