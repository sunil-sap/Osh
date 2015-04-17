/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hybris.osh.core.dao.OshOrderDao;


/**
 * Implementation class for OshOrderDao
 * 
 */
public class DefaultOshOrderDao implements OshOrderDao
{
	static private final Logger LOG = Logger.getLogger(DefaultOshOrderDao.class);

	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<OrderModel> getIncompleteOrders()
	{
		final SearchResult<OrderModel> result = getFlexibleSearchService().search(
				"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE {os:code} != 'COMPLETED'");
		return result.getResult();
	}

	@Override
	public List<OrderModel> getAllOrders()
	{
		final SearchResult<OrderModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {Order}");
		return result.getResult();
	}

	@Override
	public List<OrderModel> getLastHourOrders(final Date lastHourDate)
	{
		List<OrderModel> orders = new ArrayList<OrderModel>();
		List<OrderModel> partialFulfilledOrders = new ArrayList<OrderModel>();
		final Map<String, Object> params = new HashMap<String, Object>();
		final Map<String, Object> params1 = new HashMap<String, Object>();
		final boolean poStatus = true;
		final boolean tlogStatus = false;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE LENGTH({o:code}) >=8 AND {os:code} IN ('SHIPPED','COMPLETED','PARTIAL_FULFILLED','PICKEDUP_AT_STORE') AND {o:modifiedTime} >= ?lastHourDate AND {o:tlogPerformed}=?tlogStatus");

		final FlexibleSearchQuery query1 = new FlexibleSearchQuery(
				"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE LENGTH({o:code}) >=8 AND {os:code} IN ('PARTIAL_FULFILLED','COMPLETED') AND {o:modifiedTime} >= ?lastHourDate AND {o:tlogPerformed}=?tlogStatus AND {o:poConfirmPerformed}=?poStatus AND {o:cancelDate}>=?lastHourDate");
		params1.put("lastHourDate", lastHourDate);
		params1.put("tlogStatus", tlogStatus);
		params1.put("poStatus", poStatus);
		params.put("lastHourDate", lastHourDate);
		params.put("tlogStatus", tlogStatus);
		query.addQueryParameters(params);
		query1.addQueryParameters(params1);
		final SearchResult<OrderModel> result = flexibleSearchService.search(query);
		final SearchResult<OrderModel> result1 = flexibleSearchService.search(query1);
		if (result != null && !result.getResult().isEmpty())
		{
			orders = new ArrayList<OrderModel>(result.getResult());
		}
		if (result1 != null && !result1.getResult().isEmpty())
		{
			partialFulfilledOrders = new ArrayList<OrderModel>(result1.getResult());
			for (final OrderModel orderModel : partialFulfilledOrders)
			{
				if (orderModel != null && !orders.contains(orderModel.getPk()))
				{
					orders.add(orderModel);
				}
			}
		}
		for (final OrderModel orderModel : partialFulfilledOrders)
		{
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
			for (final ConsignmentModel consignmentModel : consignments)
			{

				if (consignmentModel.getStatus() == ConsignmentStatus.PENDING
						|| consignmentModel.getStatus() == ConsignmentStatus.READY_FOR_PICKUP)
				{
					final AbstractOrderModel order = consignmentModel.getOrder();
					orders.remove(order);
				}
			}
		}
		LOG.info("TLOG FILE CREATION ELIGIBLE NO.OF ORDERS ARE " + orders.size());
		return orders;
	}


	@Override
	public List<OrderModel> getCreditOrders()
	{
		List<OrderModel> orders = new ArrayList<OrderModel>();
		//final List<OrderModel> partialFulfilledOrders = new ArrayList<OrderModel>();

		final Map<String, Object> params = new HashMap<String, Object>();
		final boolean poStatus = true;
		params.put("poStatus", poStatus);


		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {o:pk} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE {os:code} IN ('COMPLETED','PARTIAL_FULFILLED','PICKEDUP_AT_STORE') AND {credittlog}=?poStatus");
		query.addQueryParameters(params);
		final SearchResult<OrderModel> result = flexibleSearchService.search(query);

		if (result != null && !result.getResult().isEmpty())
		{
			orders = new ArrayList<OrderModel>(result.getResult());
		}

		return orders;
	}

	@Override
	public List<OrderModel> getCompleteOrders()
	{
		final SearchResult<OrderModel> result = getFlexibleSearchService()
				.search(
						"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE {os:code} = 'COMPLETED' OR {os:code} = 'PARTIAL_FULFILLED' AND {o:modifiedTime} >= ?lastDay");
		return result.getResult();
	}


	@Override
	public List<OrderModel> getPoRelatedOrders(final Date lastDay)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("lastDay", lastDay);
		params.put("poPerformed", false);
		//final FlexibleSearchQuery query = new FlexibleSearchQuery(
		//				"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE {os:code} IN ('PENDING','READY_FOR_PICKUP') AND {o:modifiedTime} >= ?lastDay");



		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {o:PK} FROM {Order AS o} WHERE {o:poPerformed} = ?poPerformed AND {o:modifiedTime} >= ?lastDay");
		query.addQueryParameters(params);
		final SearchResult<OrderModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}


	@Override
	public List<OrderModel> getPoConfirmationOrders(final Date lastDay)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("lastDay", lastDay);
		final boolean status = false;
		params.put("status", status);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE {os:code} IN ('PENDING','PARTIAL_FULFILLED','READY_FOR_PICKUP','CANCELLED') AND {o:modifiedTime} >= ?lastDay AND {o:cancelDate}>= ?lastDay AND {o:poConfirmPerformed}=?status");
		query.addQueryParameters(params);
		final SearchResult<OrderModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}


	@Override
	public List<OrderModel> getPendingOrders()
	{
		final SearchResult<OrderModel> result = getFlexibleSearchService().search(
				"SELECT {o:PK} FROM {Order AS o JOIN OrderStatus as os ON {o:status} = {os:PK} } WHERE {os:code} = 'PENDING'");
		return result.getResult();
	}

	@Override
	public OrderModel getOrderFromOrderCode(final String orderCode)
	{
		final SearchResult<OrderModel> result = getFlexibleSearchService().search(
				"SELECT {o:PK} FROM {Order AS o } WHERE {o:code} ='" + orderCode + "'");

		if (result.getResult() != null && !result.getResult().isEmpty())
		{
			return result.getResult().get(0);
		}
		return null;
	}

	@Override
	public List<ConsignmentProcessModel> getProcessForConsignment(final ConsignmentModel consignment)

	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final boolean done = false;
		params.put("done", done);
		params.put("consignment", consignment);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"Select {PK} From {ConsignmentProcess} Where {done}=?done And {Consignment}=?consignment");
		query.addQueryParameters(params);
		final SearchResult<ConsignmentProcessModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public List<ConsignmentModel> getConsignmentModelsOfNonDropshipAndPending()
	{
		final List<OrderModel> orderList = getIncompleteOrders();
		final List<ConsignmentModel> consignments = new ArrayList<ConsignmentModel>();
		for (final OrderModel orderModel : orderList)
		{
			for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
			{
				final Calendar cal = Calendar.getInstance();
				final long no = cal.getTimeInMillis() - orderModel.getCreationtime().getTime();
				if (!consignmentModel.getCode().contains("dropship-")
						&& !(consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED)) && no <= 86400000)
				{
					consignments.add(consignmentModel);
				}
			}

		}
		return consignments;
	}



	@Override
	public List<ConsignmentModel> getNonDropshipAndPendingConsignmentModelsBetweenAnHour()
	{
		final List<OrderModel> orderList = getIncompleteOrders();
		final List<ConsignmentModel> consignments = new ArrayList<ConsignmentModel>();
		for (final OrderModel orderModel : orderList)
		{
			for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
			{
				final Calendar cal = Calendar.getInstance();
				final long no = cal.getTimeInMillis() - orderModel.getCreationtime().getTime();
				if (!consignmentModel.getCode().contains("dropship-")
						&& !(consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED)) && no < 3600000)
				{
					consignments.add(consignmentModel);
				}
			}

		}
		return consignments;
	}


	@Override
	public List<ConsignmentModel> getConsignmentModelsForDropship()
	{
		final List<OrderModel> orderList = getIncompleteOrders();
		final List<ConsignmentModel> consignmentsForDropship = new ArrayList<ConsignmentModel>();
		for (final OrderModel orderModel : orderList)
		{
			for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
			{
				if (consignmentModel.getCode().contains("dropship-")
						&& !(consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED)) && !consignmentModel.isSentToDropship())
				{
					consignmentsForDropship.add(consignmentModel);
				}
			}

		}
		return consignmentsForDropship;
	}


	@Override
	public List<ConsignmentModel> getShippedConsignmentModelsOfDropship()
	{
		final List<OrderModel> orderList = getCompleteOrders();
		final List<ConsignmentModel> consignmentsForDropship = new ArrayList<ConsignmentModel>();
		for (final OrderModel orderModel : orderList)
		{
			for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
			{
				final Calendar cal = Calendar.getInstance();
				final Date shippingDate = consignmentModel.getShippingDate();
				if (shippingDate != null)
				{
					final long no = cal.getTimeInMillis() - shippingDate.getTime();
					if (consignmentModel.getCode().contains("dropship-")
							&& (consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED) || consignmentModel.getStatus().equals(
									ConsignmentStatus.PARTIAL_FULFILLED)) && no <= 86400000)
					{
						consignmentsForDropship.add(consignmentModel);
					}
				}
			}
		}
		return consignmentsForDropship;
	}

	@Override
	public PaymentTransactionModel getPaymentTransaction(final String code)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {PaymentTransaction} WHERE {code} = ?code");
		query.addQueryParameters(params);
		final SearchResult<PaymentTransactionModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<PaymentTransactionEntryModel> getCapturedTransactionEntries(final PK paymentTransaction, final Date triggerDate)
	{

		final Calendar cal = Calendar.getInstance();
		cal.setTime(triggerDate);
		cal.add(cal.DATE, -2);
		final Date lastDayDate = cal.getTime();
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("paymentTransaction", paymentTransaction);
		params.put("lastDayDate", lastDayDate);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {p:pk} FROM {PaymentTransactionEntry As p JOIN PaymentTransactionType As "
						+ "pt ON {p:type} = {pt:PK}} WHERE {pt:code} IN ('PARTIAL_CAPTURE','CAPTURE') AND {p:paymentTransaction}=?paymentTransaction AND {p:modifiedTime}>=?lastDayDate");
		query.addQueryParameters(params);
		final SearchResult<PaymentTransactionEntryModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public List<PaymentTransactionEntryModel> getReturnedTransactionEntries(final PK paymentTransaction, final Date triggerDate)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(triggerDate);
		cal.add(cal.DATE, -2);
		final Date lastDayDate = cal.getTime();
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("paymentTransaction", paymentTransaction);
		params.put("lastDayDate", lastDayDate);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {p:pk} FROM {PaymentTransactionEntry As p JOIN PaymentTransactionType As "
						+ "pt ON {p:type} = {pt:PK}} WHERE {pt:code} IN ('REFUND_FOLLOW_ON') AND {p:paymentTransaction}=?paymentTransaction AND {p:modifiedTime}>=?lastDayDate");
		query.addQueryParameters(params);
		final SearchResult<PaymentTransactionEntryModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}


	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
