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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Admin
 * 
 */
abstract public class AbstractFraudCheckAction extends AbstractSimpleOrderDecisionAction
{
	/**
	 * @param response
	 * @param order
	 */

	protected FraudReportModel createFraudReport(final String providerName, final FraudServiceResponse response,
			final OrderModel order, final FraudStatus status)
	{
		final FraudReportModel fraudReport = modelService.create(FraudReportModel.class);
		fraudReport.setOrder(order);
		fraudReport.setStatus(status);
		fraudReport.setProvider(providerName);
		fraudReport.setTimestamp(new Date());
		int reportNumber = 0;
		if (order.getFraudReports() != null && !order.getFraudReports().isEmpty())
		{
			reportNumber = order.getFraudReports().size();
		}
		fraudReport.setCode(order.getCode() + "_FR" + reportNumber);
		List<FraudSymptomScoringModel> symptoms = null;
		for (final FraudSymptom symptom : response.getSymptoms())
		{
			if (symptoms == null)
			{
				symptoms = new ArrayList<FraudSymptomScoringModel>();
			}
			final FraudSymptomScoringModel symptomScoring = modelService.create(FraudSymptomScoringModel.class);
			symptomScoring.setFraudReport(fraudReport);
			symptomScoring.setName(symptom.getSymptom());
			symptomScoring.setExplanation(symptom.getExplanation());
			symptomScoring.setScore(symptom.getScore());
			symptoms.add(symptomScoring);
		}
		fraudReport.setFraudSymptomScorings(symptoms);
		return fraudReport;
	}

	protected OrderHistoryEntryModel createHistoryLog(final String providerName, final OrderModel order, final FraudStatus status,
			final String code)
	{
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(new Date());
		historyEntry.setOrder(order);
		if (status.equals(FraudStatus.OK))
		{
			historyEntry.setDescription("Fraud check [" + providerName + "]: OK");
		}
		else
		{
			historyEntry.setDescription("Fraud check [" + providerName + "]: " + status.toString() + ". Check the fraud report :"
					+ code);
		}
		return historyEntry;
	}



}
