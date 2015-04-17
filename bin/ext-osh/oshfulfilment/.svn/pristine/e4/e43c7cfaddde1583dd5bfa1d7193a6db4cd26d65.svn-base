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
package com.hybris.osh.test.actions;

import de.hybris.platform.core.Registry;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 *
 */
public class TestActionTemp extends AbstractAction
{

	private String result = "OK";
	private boolean throwException = false;


	/**
	 * @return the result
	 */
	public String getResult()
	{
		return result;
	}






	public void setResult(final String result)
	{
		this.result = result;
	}

	@Override
	public String execute(final BusinessProcessModel process) throws Exception //NOPMD
	{
		return execute((OrderProcessModel) process);
	}


	public String execute(final OrderProcessModel process) throws Exception //NOPMD
	{
		// This call actually puts -this- into a queue.
		try
		{
			if (throwException)
			{
				throw new RuntimeException("Error");
			}
		}
		finally
		{
			//getQueueService().actionExecuted(getProcess(process), this);

		}


		Logger.getLogger(getClass()).info(result);
		return result;
	}



	public BusinessProcessModel getProcess(final BusinessProcessModel process)
	{
		return process;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.processengine.spring.Action#getTransitions()
	 */
	@Override
	public Set<String> getTransitions()
	{
		final Set<String> res = new HashSet<String>();
		res.add(result);
		return res;
	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}

	public void setThrowException(final boolean throwException)
	{
		this.throwException = throwException;
	}

}
