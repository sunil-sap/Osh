package com.hybris.osh.order.cancel.strategy.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.order.cancel.strategy.CancelOrderStrategy;
import com.hybris.osh.payment.services.OshPaymentService;


/**
 *
 */
public class DefaultCancelOrderStrategy implements CancelOrderStrategy
{
	@Resource
	private ModelService modelService;

	@Resource
	private OshPaymentService oshPaymentService;

	@Override
	public String AutherizeCancelableOrderEntries(final Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries,
			final OrderModel order, final PaymentTransactionEntryModel paymentTransactionEntry) throws Exception
	{
		Integer totalQty = 0;
		double cancelPrice = 0.0d;
		OrderEntryModel orderEntry = null;
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final HashMap<Integer, Integer> cancledEntries = new HashMap<Integer, Integer>();

		for (final OrderEntryModificationRecordEntryModel orderEntryModificationRecordEntryModel : orderModifiedEntries)
		{
			final OrderEntryCancelRecordEntryModel cancelEntries = (OrderEntryCancelRecordEntryModel) orderEntryModificationRecordEntryModel;
			orderEntry = orderEntryModificationRecordEntryModel.getOrderEntry();
			final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
			if (!cancelEntries.getCancelRequestQuantity().equals(null) && !cancelEntries.getCancelRequestQuantity().equals(0))
			{
				totalQty = orderEntry.getQuantity().intValue();
				final Integer reqQty = cancelEntries.getCancelRequestQuantity();
				//final Double shippingAmt = cancelEntries.getShippingAmt() * reqQty;

				Double calculatedPrice = orderEntry.getDiscountPrice();
				//calculatedPrice=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), calculatedPrice).doubleValue();
				if ((totalQty) != 0 && (totalQty) >= reqQty.intValue())
				{
					double reqQtyPrice = (calculatedPrice.doubleValue()) * reqQty;
					reqQtyPrice=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), reqQtyPrice).doubleValue();
					double cancelTax=consignmentEntry.getOrderEntry().getTaxPerUnit()* reqQty;
					cancelTax=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), cancelTax).doubleValue();
					cancelPrice = cancelPrice + reqQtyPrice + cancelTax;
					if ((totalQty - reqQty) == 0 || totalQty.equals(1))
					{
						consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.CANCELLED);
						consignmentEntry.setShippedQuantity(totalQty.longValue());
					}
					else
					{
						totalQty = totalQty - reqQty;
						consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.PENDING);
					}
					cancledEntries.put(orderEntry.getEntryNumber(), totalQty);
					modelService.save(cancelEntries);
					modelService.refresh(cancelEntries);
					modelService.save(consignmentEntry);
					modelService.refresh(consignmentEntry);
				}

			}

		}
		final List<PaymentTransactionModel> txns = order.getPaymentTransactions();
		final PaymentTransactionModel txn = txns.iterator().next();
		final List<PaymentTransactionEntryModel> txnEntries = txn.getEntries();

		PaymentTransactionEntryModel txnEntry = null;
		final PaymentTransactionModel transaction = paymentTransactionEntry.getPaymentTransaction();

		for (final Iterator iterator = transaction.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION) && pte.getTransactionStatus().equals("ACCEPTED"))
			{
				txnEntry = pte;
			}
		}

		if (txnEntry == null)
		{
			throw new AdapterException("Could not capture without authorization");
		}
		cancelPrice=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), cancelPrice).doubleValue();
		final BigDecimal amount = txnEntry.getAmount();
		paymentTransactionEntry.setAmount(BigDecimal.valueOf(cancelPrice));
		modelService.save(paymentTransactionEntry);
		modelService.refresh(paymentTransactionEntry);
		final Currency currency = Currency.getInstance(txnEntry.getCurrency().getIsocode());

		 BigDecimal authPrice = amount.subtract(BigDecimal.valueOf(cancelPrice));
		if (!(authPrice.doubleValue() == 0.0d))
		{
			//final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
			authPrice=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), authPrice);
			final PaymentTransactionEntryModel Autherization = oshPaymentService.authorize(txn, authPrice, currency,
					order.getPaymentAddress(), txnEntry.getSubscriptionID(), txnEntry.getPaymentTransaction().getPaymentProvider());

			if (Autherization.getTransactionStatus().equalsIgnoreCase(TransactionStatus.ACCEPTED.name()))
			{

				final List<AbstractOrderEntryModel> orderEntries = order.getEntries();
				for (final AbstractOrderEntryModel cancelOrderEntry : orderEntries)
				{
					
					if (cancledEntries != null && (!cancledEntries.isEmpty())
							&& cancledEntries.containsKey(cancelOrderEntry.getEntryNumber()))
					{
						final Long cancledQty = cancledEntries.get(cancelOrderEntry.getEntryNumber()).longValue();
						final ConsignmentEntryModel coEntry = cancelOrderEntry.getConsignmentEntries().iterator().next();
						final Double calculatedPrice =  orderEntry.getTotalPrice()/totalQty;
						//final Long cancelQty = coEntry.getQuantity() - cancledQty;

						//changed for po files
						final Integer cancelQty = coEntry.getOrderEntry().getQuantity().intValue() - cancledQty.intValue();

						if (cancelQty.equals(0))
						{
							coEntry.setCancelQty(cancledQty.intValue());
						}
						else
						{
							if (coEntry.getCancelQty() != null && !coEntry.getConsignment().getCode().contains("dropship"))
							{
								int existingCancelledqty = coEntry.getCancelQty();
								coEntry.setCancelQty(existingCancelledqty + cancelQty);
							}
							else
							{
								coEntry.setCancelQty(cancelQty);
							}
							
						}
						coEntry.setPoConfirmPerformed(false);
						final Calendar calendar = Calendar.getInstance();
						order.setCancelDate(calendar.getTime());
						order.setPoConfirmPerformed(false);
						cancelOrderEntry.setQuantity(cancledQty);
						//cancelOrderEntry.setTotalPrice((calculatedPrice) * cancledQty);
						modelService.save(cancelOrderEntry);
						modelService.refresh(cancelOrderEntry);
						modelService.save(coEntry);
						modelService.refresh(coEntry);
						modelService.save(order);
						modelService.refresh(order);
						
					}
				}

				final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
				consignmentEntry.setCancellationPerformed(true);

				modelService.save(consignmentEntry);
				modelService.refresh(consignmentEntry);
			}
			else
			{
				for (final OrderEntryModificationRecordEntryModel orderEntryModificationRecordEntryModel : orderModifiedEntries)
				{
					final OrderEntryCancelRecordEntryModel cancelEntries = (OrderEntryCancelRecordEntryModel) orderEntryModificationRecordEntryModel;
					orderEntry = orderEntryModificationRecordEntryModel.getOrderEntry();
					final ConsignmentEntryModel consignmentEntryModel = orderEntry.getConsignmentEntries().iterator().next();
					if (!cancelEntries.getCancelRequestQuantity().equals(null) && !cancelEntries.getCancelRequestQuantity().equals(0))
					{
						final Integer reqQty = cancelEntries.getCancelRequestQuantity();
						consignmentEntryModel.setEntryLevelStatus(ConsignmentEntryStatus.PENDING);
						if (consignmentEntryModel.getShippedQuantity() != null)
						{
							consignmentEntryModel.setShippedQuantity(consignmentEntryModel.getShippedQuantity() - reqQty);
							modelService.save(consignmentEntryModel);
							modelService.refresh(consignmentEntryModel);
						}
					}
				}
			}
			return Autherization.getTransactionStatus();
		}
		else
		{
			return "ZEROPRICE";
		}

	}

	@Override
	public void setConsignmentStatusForCancelOrder(final Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries)
	{
		for (final OrderEntryModificationRecordEntryModel orderEntryModificationRecordEntryModel : orderModifiedEntries)
		{
			final OrderEntryModel orderEntry = orderEntryModificationRecordEntryModel.getOrderEntry();
			final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
			consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.CANCELLED);
			final ConsignmentModel consignment = consignmentEntry.getConsignment();
			Integer cancelEntryCount = 0;
			final Set<ConsignmentEntryModel> consignmentEntries = consignment.getConsignmentEntries();
			for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntries)
			{
				if (consignmentEntryModel.getEntryLevelStatus().getCode().equals("CANCELLED"))
				{
					Integer cancelQty = consignmentEntryModel.getCancelQty();
					if (cancelQty == null)
					{
						cancelQty = 0;
					}
					final Calendar cal = Calendar.getInstance();
					AbstractOrderModel order = consignmentEntryModel.getOrderEntry().getOrder();
					order.setCancelDate(cal.getTime());
					order.setPoConfirmPerformed(false);
					modelService.save(order);
					modelService.refresh(order);
					//consignmentEntryModel.setCancelQty(cancelQty + consignmentEntryModel.getOrderEntry().getQuantity());
					consignmentEntryModel.setCancelQty(consignmentEntryModel.getOrderEntry().getQuantity().intValue());
					consignmentEntryModel.setPoPerformed(false);
					consignmentEntryModel.setPoConfirmPerformed(false);
					cancelEntryCount++;
					if (consignmentEntries.size() == cancelEntryCount.intValue())
					{
						consignment.setStatus(ConsignmentStatus.CANCELLED);
						modelService.save(consignment);
						modelService.refresh(consignment);
					}
				}
			}
			modelService.save(consignmentEntry);
			modelService.refresh(consignmentEntry);
		}
	}

	@Override
	public void setStatusForCancelAndShippedOrder(final Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries)
	{
		for (final OrderEntryModificationRecordEntryModel orderEntryModificationRecordEntryModel : orderModifiedEntries)
		{
			final OrderEntryModel orderEntry = orderEntryModificationRecordEntryModel.getOrderEntry();
			final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
			// consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.CANCELLED);
			final ConsignmentModel consignment = consignmentEntry.getConsignment();
			Integer cancelEntryCount = 0;
			Integer shippedEntryCount = 0;
			Integer completeEntryCount = 0;
			final Set<ConsignmentEntryModel> consignmentEntries = consignment.getConsignmentEntries();
			final int totalNoOfEntries = consignmentEntries.size();
			for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntries)
			{
				if (consignmentEntryModel.getEntryLevelStatus().getCode().equals("CANCELLED"))
				{
					cancelEntryCount++;
				}
				if (consignmentEntryModel.getEntryLevelStatus().getCode().equals("SHIPPED"))
				{
					shippedEntryCount++;
				}
				if (consignmentEntryModel.getEntryLevelStatus().getCode().equals("COMPLETE"))
				{
					completeEntryCount++;
				}
			}
			if (totalNoOfEntries == cancelEntryCount)
			{
				consignment.setStatus(ConsignmentStatus.CANCELLED);
			}
			if (totalNoOfEntries == completeEntryCount)
			{
				consignment.setStatus(ConsignmentStatus.COMPLETE);
			}
			if (totalNoOfEntries == shippedEntryCount)
			{
				consignment.setStatus(ConsignmentStatus.SHIPPED);
			}
			if (cancelEntryCount != 0 && totalNoOfEntries > cancelEntryCount && shippedEntryCount==0 && completeEntryCount==0)
			{
				consignment.setStatus(ConsignmentStatus.PENDING);
			}
			else if (cancelEntryCount != 0 && totalNoOfEntries > cancelEntryCount && (shippedEntryCount!=0 || completeEntryCount!=0))
			{
				consignment.setStatus(ConsignmentStatus.PARTIAL_FULFILLED);
			}
			
			modelService.save(consignment);
			modelService.refresh(consignment);
		}
	}

	@Override
	public boolean setOrderStatusForCancelAndShippedOrder(final OrderModel orderModel)
	{
		boolean isStatus = false;
		final Set<ConsignmentModel> consignmentEntries = orderModel.getConsignments();
		for (final ConsignmentModel consignmentModel : consignmentEntries)
		{
			if (consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED)
					|| consignmentModel.getStatus().equals(ConsignmentStatus.PARTIAL_FULFILLED))
			{
				isStatus = true;
			}

		}
		if (isStatus)
		{
			return true;
		}
		return false;

	}
}
