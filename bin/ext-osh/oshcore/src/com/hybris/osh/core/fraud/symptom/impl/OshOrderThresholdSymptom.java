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
package com.hybris.osh.core.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;


/**
 * To increase the thresholdLimit for shopping.
 */
public class OshOrderThresholdSymptom extends AbstractOrderFraudSymptomDetection
{

	private double thresholdLimit = 10000;
	private double thresholdDelta = 100;

	/**
	 * @return the thresholdLimit
	 */
	public double getThresholdLimit()
	{
		return thresholdLimit;
	}

	/**
	 * @param thresholdLimit
	 *           the thresholdLimit to set
	 */
	public void setThresholdLimit(final double thresholdLimit)
	{
		this.thresholdLimit = thresholdLimit;
	}

	/**
	 * @return the thresholdDelta
	 */
	public double getThresholdDelta()
	{
		return thresholdDelta;
	}

	/**
	 * @param thresholdDelta
	 *           the thresholdDelta to set
	 */
	public void setThresholdDelta(final double thresholdDelta)
	{
		this.thresholdDelta = thresholdDelta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection#recognizeSymptom(de.hybris.platform.fraud
	 * .impl.FraudServiceResponse, de.hybris.platform.core.model.order.AbstractOrderModel)
	 */
	@Override
	public FraudServiceResponse recognizeSymptom(final FraudServiceResponse fraudResponse, final AbstractOrderModel order)
	{
		if (order.getTotalPrice().compareTo(Double.valueOf(getThresholdLimit())) > 0)
		{
			final double difference = order.getTotalPrice().doubleValue() - getThresholdLimit();
			fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), getIncrement(difference)));
		}
		else
		{
			fraudResponse.addSymptom(createSymptom(false));
		}
		return fraudResponse;
	}

	public double getIncrement(final double orderDelta)
	{
		final double stepIncrement = super.getIncrement();
		return (Math.ceil(orderDelta / getThresholdDelta()) * stepIncrement);
	}


}
