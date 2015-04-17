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
package com.hybris.osh.actions.consignmentfulfilment;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.util.Helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.payment.services.OshPaymentService;



/**
 * The TakePayment step captures the payment transaction.
 */
public class TakePayment extends AbstractConsignmentSimpleDecisionAction
{
	private PaymentService paymentService;

	@Resource(name = "oshPaymentService")
	private OshPaymentService oshPaymentService;

	@Override
	public Transition executeAction(final ConsignmentProcessModel process)
	{
		final String[] stringArrayContainingOrderCode = process.getCode().split("_");
		final String orderCode = stringArrayContainingOrderCode[0];
		Logger.getLogger(this.getClass()).debug((" the element is :" + orderCode));
		final OrderModel order = (OrderModel) process.getConsignment().getOrder();
		final ConsignmentModel consignmentModel = process.getConsignment();
		final Set<ConsignmentEntryModel> entries = process.getConsignment().getConsignmentEntries();
		final List<PaymentTransactionModel> txns = order.getPaymentTransactions();
		final Set<ConsignmentModel> consignments = order.getConsignments();
		final int noOfConsignments = consignments.size();
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		Double totalAuthorizedAmount = 0.0;
		boolean isFullCapture = false;
		// sanity check. There could be a problem with this order.
		if (txns.size() != 1)
		{
			Logger.getLogger(this.getClass()).error("Ambigous payment transaction, capture failed.");
			setOrderStatus(order, OrderStatus.PROCESSING_ERROR);
			return Transition.NOK;
		}

		PaymentTransactionModel txn = txns.iterator().next();
		PaymentTransactionEntryModel auth = null;
		for (final Iterator iterator = txn.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION)&& pte.getTransactionStatus().equals("ACCEPTED"))
			{
				auth = pte;
				totalAuthorizedAmount = auth.getAmount().doubleValue();
			}

		}

		if (auth == null)
		{
			throw new AdapterException("Could not capture partially without authorization");
		}
		int counter = 0;
		Double settlementPrice = 0.0;
		boolean doNotPerformeTransaction = false;
		//final Double amountToSettle = 0.0;
		Double priceToBeAuthorized = 0.0;
		final int totalLineEntries = entries.size();
		PaymentTransactionEntryModel txnEntry = null;
		boolean isCancelledEntryPresent = false;
		if(order.getDeliveryCost()!=null && (consignmentModel.getCode().contains("online-")|| noOfConsignments == 1))
		{
			if(order.getDeliveryCost()!=null)
			{
				settlementPrice = order.getDeliveryCost();
			}
		}

		final List<ConsignmentEntryModel> consignemententriesForTransaction = new ArrayList<ConsignmentEntryModel>();
		try
		{
		for (final ConsignmentEntryModel entry : entries)
		{
			long cancelQty=0L;
			if(entry.getCancelQty()==null)
			{
				cancelQty=0L;
			}
			else
			{
				cancelQty=entry.getCancelQty();
			}
			long totalUpdateableQuantity = entry.getQuantity() - cancelQty;
			//AbstractOrderEntryModel orderEntryModel = entry.getOrderEntry();
			Double calculatedTotalPrize = entry.getOrderEntry().getDiscountPrice() * totalUpdateableQuantity;
			calculatedTotalPrize=Helper.roundCurrencyValue(ctx, ctx.getCurrency(),calculatedTotalPrize).doubleValue();
			/*if(entry.getCancelQty()!=null)
			{
				
				calculatedTotalPrize = entry.getOrderEntry().getDiscountPrice() * totalUpdateableQuantity;	
			}*/
			if ((entry.getEntryLevelStatus().getCode().equals(ConsignmentEntryStatus.SHIPPED.getCode()) || entry
					.getEntryLevelStatus().getCode().equals(ConsignmentEntryStatus.PICKEDUP_AT_STORE.getCode()))
					&& !entry.isTransactionDone()
					&& !(entry.getEntryLevelStatus().getCode().equals(ConsignmentEntryStatus.CANCELLED.getCode())))
			{
				Double entryRemainingTax= 0.0d;
				double cancelTax=entry.getOrderEntry().getTaxPerUnit();
				//cancelTax=Helper.roundCurrencyValue(ctx, ctx.getCurrency(),cancelTax).doubleValue();
				double cancelTotalTax=cancelQty *cancelTax;
				cancelTotalTax=Helper.roundCurrencyValue(ctx, ctx.getCurrency(),cancelTotalTax).doubleValue();
				entryRemainingTax = entry.getOrderEntry().getTaxAmount() - cancelTotalTax;
				consignemententriesForTransaction.add(entry);
				settlementPrice = settlementPrice + calculatedTotalPrize + entryRemainingTax;
				settlementPrice=Double.valueOf(Helper.roundCurrencyValue(ctx, ctx.getCurrency(),settlementPrice.doubleValue()).doubleValue());
				if(entry.isCancellationPerformed()){
					isCancelledEntryPresent = true;
				}
				counter++;
			}
			else if (entry.getEntryLevelStatus().getCode().equals(ConsignmentEntryStatus.READY_FOR_PICKUP.getCode()))
			{
				doNotPerformeTransaction= true;
			}
		}
		if (!isCancelledEntryPresent && counter == totalLineEntries && noOfConsignments == 1 && !doNotPerformeTransaction)
		{
			txnEntry = oshPaymentService.capture(txn);
				if (txnEntry != null && txnEntry.getTransactionStatus() == TransactionStatus.ACCEPTED.name())
				{
					isFullCapture = true;
				}
		}

		else if (settlementPrice != 0.0)
		{
			if(settlementPrice> totalAuthorizedAmount)
			{
				settlementPrice = totalAuthorizedAmount;
			}
			if((totalAuthorizedAmount-settlementPrice)<0.03){
				settlementPrice = totalAuthorizedAmount;
			}
			txnEntry = oshPaymentService.partialCapture(txn, BigDecimal.valueOf(settlementPrice));
		}

		if (txnEntry != null)
		{
			if (txnEntry.getTransactionStatus() == TransactionStatus.ACCEPTED.name())
			{
				Logger.getLogger(this.getClass()).debug("The payment transaction has been captured.");
				if (counter == totalLineEntries)
				{
					setOrderStatus(order, OrderStatus.PAYMENT_CAPTURED);
				}

				for (final ConsignmentEntryModel entry : consignemententriesForTransaction)
				{
					entry.setTransactionModel(txnEntry);
					entry.setTransactionDone(true);
					final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
					historyEntry.setTimestamp(new Date());
					historyEntry.setOrder((OrderModel) entry.getOrderEntry().getOrder());
					if (entry.getEntryLevelStatus().equals(ConsignmentEntryStatus.SHIPPED))
					{
						historyEntry.setDescription("Product " + entry.getOrderEntry().getProduct().getCode() + " with quantity "
								+ entry.getOrderEntry().getQuantity() + " shipped for order: "
								+ entry.getOrderEntry().getOrder().getCode());
					}
					if (entry.getEntryLevelStatus().equals(ConsignmentEntryStatus.PICKEDUP_AT_STORE))
					{
						/**
						 * this shipping quantity is set for the partial cancel order entry rendering
						 */
						entry.setShippedQuantity(1L);
						historyEntry.setDescription("Product " + entry.getOrderEntry().getProduct().getCode() + " with quantity "
								+ entry.getOrderEntry().getQuantity() + " picked up for order: "
								+ entry.getOrderEntry().getOrder().getCode());
					}
					modelService.save(historyEntry);
					modelService.refresh(historyEntry);
					modelService.save(entry);
					modelService.refresh(entry);
				}
			}
			else
			{
				Logger.getLogger(this.getClass()).debug("The payment transaction capture has failed.");
				setOrderStatus(order, OrderStatus.PAYMENT_NOT_CAPTURED);

				for (final ConsignmentEntryModel entry : consignemententriesForTransaction)
				{
					if (!entry.isTransactionDone())
					{
						final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
						historyEntry.setTimestamp(new Date());
						historyEntry.setOrder((OrderModel) entry.getOrderEntry().getOrder());
						historyEntry.setDescription("Product " + entry.getOrderEntry().getProduct().getCode() + " with quantity "
								+ entry.getOrderEntry().getQuantity() + " not updated due to transaction: "
								+ txnEntry.getTransactionStatusDetails() + " for order: " + entry.getOrderEntry().getOrder().getCode());
						entry.setTransactionDone(false);
						entry.setEntryLevelStatus(ConsignmentEntryStatus.PENDING);
						modelService.save(entry);
						modelService.save(historyEntry);
						modelService.refresh(historyEntry);
					}
				}
				return Transition.NOK;
			}
			// finding out the total price to be authorized
			priceToBeAuthorized = totalAuthorizedAmount - settlementPrice ;
			for (final ConsignmentModel consignment : consignments)
			{
				final Set<ConsignmentEntryModel> consignmentEntries = consignment.getConsignmentEntries();
				boolean isShipped = false;
					
				for (final ConsignmentEntryModel consignmentEntry : consignmentEntries)
				{
					if (!consignmentEntry.isTransactionDone()
							&& !(consignmentEntry.getEntryLevelStatus().equals(ConsignmentEntryStatus.CANCELLED)))
					{
						consignmentEntry.setShippedQuantity(0L);
						modelService.save(consignmentEntry);
						modelService.refresh(consignmentEntry);
					}
					if(consignmentEntry.getEntryLevelStatus().equals(ConsignmentEntryStatus.SHIPPED) || consignmentEntry.getEntryLevelStatus().equals(ConsignmentEntryStatus.PICKEDUP_AT_STORE))
					{
						consignment.setPaymentCapture(true);
						isShipped = true;
					}
				}
				if((consignment.getStatus().equals(ConsignmentStatus.PENDING)|| consignment.getStatus().equals(ConsignmentStatus.READY_FOR_PICKUP)) && !doNotPerformeTransaction && isShipped)
				{
					consignment.setShippingDate(new Date());
					modelService.save(consignment);
					modelService.refresh(consignment);
				}
			}

				
				double roundPriceToBeAuth = Double.valueOf(Helper.roundCurrencyValue(ctx, ctx.getCurrency(),
						priceToBeAuthorized.doubleValue()).doubleValue());

				if (roundPriceToBeAuth != 0.0)
			{
				final String subscriptionId = auth.getSubscriptionID();
				final BigDecimal amount = BigDecimal.valueOf(roundPriceToBeAuth);
				final AddressModel address = order.getPaymentAddress();
					if (txnEntry != null && !isFullCapture)
				{
					final PaymentTransactionEntryModel pta = oshPaymentService.authorize(txn, amount,
							Currency.getInstance(auth.getCurrency().getIsocode()), address, subscriptionId);
					txn = pta.getPaymentTransaction();//txn=txn
				}

			}
		}
		}
		catch (Exception e)
		{
			Logger.getLogger(this.getClass()).debug("The payment transaction capture has failed.");
			
			if(!(order.getStatus().equals(OrderStatus.PARTIAL_FULFILLED) && !(order.getStatus().equals(OrderStatus.COMPLETED))))
			{
				setOrderStatus(order, OrderStatus.PENDING);
				
			}
			

			for (final ConsignmentEntryModel entry : consignemententriesForTransaction)
			{
				if (!entry.isTransactionDone())
				{
					final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
					historyEntry.setTimestamp(new Date());
					historyEntry.setOrder((OrderModel) entry.getOrderEntry().getOrder());
					historyEntry.setDescription(entry.getOrderEntry().getOrder().getCode() + " Order Transaction Failed ");
					entry.setTransactionDone(false);
					entry.setEntryLevelStatus(ConsignmentEntryStatus.PENDING);
					modelService.save(entry);
					modelService.save(historyEntry);
					modelService.refresh(historyEntry);
				}
			}
			return Transition.NOK;

		}
		return Transition.OK;
	}

	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}
}
