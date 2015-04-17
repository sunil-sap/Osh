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

import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.FraudService;
import de.hybris.platform.fraud.constants.FrauddetectionConstants;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.model.FraudReportModel;
import com.hybris.osh.constants.OshfulfilmentConstants;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.util.Config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


public class FraudCheckOrderInternal extends AbstractFraudCheckAction
{

	private FraudService fraudService;

	private String providerName;

	public FraudService getFraudService()
	{
		return fraudService;
	}

	@Required
	public void setFraudService(final FraudService fraudService)
	{
		this.fraudService = fraudService;
	}


	public String getProviderName()
	{
		return providerName;
	}

	public void setProviderName(final String providerName)
	{
		this.providerName = providerName;
	}

	@Override
	public Transition executeAction(final OrderProcessModel process)//NOPMD
	{
		//get the fraud-detection configuration
		double scoreLimit = 0;
		double scoreTolerance = 0;
		scoreLimit = Double.parseDouble(Config.getParameter(OshfulfilmentConstants.EXTENSIONNAME + ".fraud.scoreLimit"));
		scoreTolerance = Double
				.parseDouble(Config.getParameter(OshfulfilmentConstants.EXTENSIONNAME + ".fraud.scoreTolerance"));



		final OrderModel order = process.getOrder();
		FraudServiceResponse response;
		if (order == null)
		{
			final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
			historyEntry.setTimestamp(new Date());
			historyEntry.setOrder(order);
			historyEntry.setDescription(FrauddetectionConstants.NULL_ORDER_MSG);
			modelService.save(historyEntry);
			return Transition.NOK;
		}
		else
		{
			response = getFraudService().recognizeOrderSymptoms(getProviderName(), order);

			final double score = response.getScore();
			if (score < scoreLimit)
			{
				final FraudReportModel fraudReport = createFraudReport(providerName, response, order, FraudStatus.OK);
				final OrderHistoryEntryModel historyEntry = createHistoryLog(providerName, order, FraudStatus.OK, null);
				order.setFraudulent(Boolean.FALSE);
				order.setPotentiallyFraudulent(Boolean.FALSE);
				order.setStatus(OrderStatus.FRAUD_CHECKED);
				modelService.save(fraudReport);
				modelService.save(historyEntry);
				modelService.save(order);
				return Transition.OK;
			}
			else if (score < scoreLimit + scoreTolerance)
			{
				final FraudReportModel fraudReport = createFraudReport(providerName, response, order, FraudStatus.CHECK);
				final OrderHistoryEntryModel historyEntry = createHistoryLog(providerName, order, FraudStatus.CHECK, fraudReport
						.getCode());
				order.setFraudulent(Boolean.FALSE);
				order.setPotentiallyFraudulent(Boolean.TRUE);
				order.setStatus(OrderStatus.FRAUD_CHECKED);
				modelService.save(fraudReport);
				modelService.save(historyEntry);
				modelService.save(order);
				return Transition.NOK;
			}
			else
			{
				final FraudReportModel fraudReport = createFraudReport(providerName, response, order, FraudStatus.FRAUD);
				final OrderHistoryEntryModel historyEntry = createHistoryLog(providerName, order, FraudStatus.FRAUD, fraudReport
						.getCode());
				order.setFraudulent(Boolean.TRUE);
				order.setPotentiallyFraudulent(Boolean.FALSE);
				order.setStatus(OrderStatus.FRAUD_CHECKED);
				modelService.save(fraudReport);
				modelService.save(historyEntry);
				modelService.save(order);
				return Transition.NOK;
			}
		}

	}


}
