/**
 * 
 */
package com.hybris.osh.core.checkout.ordersplitting.strategy.impl;


import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.ConsignmentService;
import de.hybris.platform.ordersplitting.impl.DefaultOrderSplittingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.SplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;


public class OshOrderSplittingService extends DefaultOrderSplittingService
{
	private static final Logger LOG = Logger.getLogger(OshOrderSplittingService.class);
	private List strategiesList;
	private ModelService modelService;
	private ConsignmentService consignmentService;

	/**
	 * 
	 */
	public OshOrderSplittingService()
	{
		strategiesList = new LinkedList();
	}


	@Override
	public List<SplittingStrategy> getStrategiesList()
	{
		return strategiesList;
	}

	@Override
	public void setStrategiesList(final List<SplittingStrategy> strategiesList)
	{
		this.strategiesList = strategiesList;
	}


	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public List<ConsignmentModel> splitOrderForConsignment(final AbstractOrderModel order,
			final List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException
	{
		final List listConsignmentModel = splitOrderForConsignmentNotPersist(order, orderEntryList);
		ConsignmentModel consignment;
		for (final Iterator iterator = listConsignmentModel.iterator(); iterator.hasNext(); modelService.save(consignment))
		{
			consignment = (ConsignmentModel) iterator.next();
		}

		modelService.save(order);
		return listConsignmentModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.ordersplitting.impl.DefaultOrderSplittingService#splitOrderForConsignmentNotPersist(de.hybris
	 * .platform.core.model.order.AbstractOrderModel, java.util.List)
	 */
	@Override
	public List<ConsignmentModel> splitOrderForConsignmentNotPersist(final AbstractOrderModel order,
			final List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException
	{
		List splitedList = new ArrayList();
		final OrderEntryGroup tmpOrderEntryList = new OrderEntryGroup();
		tmpOrderEntryList.addAll(orderEntryList);
		splitedList.add(tmpOrderEntryList);
		if (strategiesList == null || strategiesList.isEmpty())
		{
			LOG.warn("No splitting strategies were configured!");
		}
		for (final Iterator iterator = strategiesList.iterator(); iterator.hasNext();)
		{
			final SplittingStrategy strategy = (SplittingStrategy) iterator.next();
			if (LOG.isDebugEnabled())
			{
				LOG.debug((new StringBuilder("Applying order splitting strategy : [")).append(strategy.getClass().getName())
						.append("]").toString());
			}
			splitedList = strategy.perform(splitedList);
		}

		String orderCode;
		if (order == null)
		{
			orderCode = getUniqueNumber("ORDER", 10, "GEN0001");
		}
		else
		{
			orderCode = order.getCode();
		}
		final List consignmentList = new ArrayList();
		//char prefixCode = 'a';
		final String online_prefixCode = "online-";
		final String store_prefixCode = "store-";
		final String dropship_prefixCode = "dropship-";
		ConsignmentModel cons;
		for (final Iterator iterator1 = splitedList.iterator(); iterator1.hasNext(); consignmentList.add(cons))
		{
			final OrderEntryGroup orderEntryResultList = (OrderEntryGroup) iterator1.next();
			//orderEntryResultList.
			final String orderType = orderEntryResultList.get(0).getOrderType();
			LOG.info("***orderType***  " + orderType);
			if (orderType.equalsIgnoreCase("online"))
			{
				cons = consignmentService.createConsignment(order,
						(new StringBuilder(String.valueOf(online_prefixCode))).append(orderCode).toString(), orderEntryResultList);
			}
			else if (orderType.equalsIgnoreCase("dropship"))
			{
				cons = consignmentService.createConsignment(order,
						(new StringBuilder(String.valueOf(dropship_prefixCode))).append(orderCode).toString(), orderEntryResultList);
			}
			else
			{
				cons = consignmentService.createConsignment(order,
						(new StringBuilder(String.valueOf(store_prefixCode))).append(orderCode).toString(), orderEntryResultList);
			}

			//prefixCode++;
			SplittingStrategy strategy;
			for (final Iterator iterator2 = strategiesList.iterator(); iterator2.hasNext(); strategy.afterSplitting(
					orderEntryResultList, cons))
			{
				strategy = (SplittingStrategy) iterator2.next();
			}

		}

		return consignmentList;
	}

	private String getUniqueNumber(final String code, final int digits, final String startValue)
	{
		try
		{
			NumberSeriesManager.getInstance().getNumberSeries(code);
		}
		catch (final JaloInvalidParameterException _ex)
		{
			NumberSeriesManager.getInstance().createNumberSeries(code, startValue, 1, digits);
		}
		return NumberSeriesManager.getInstance().getUniqueNumber(code, digits);
	}

	@Override
	public void setConsignmentService(final ConsignmentService consignmentService)
	{
		this.consignmentService = consignmentService;
	}

}
