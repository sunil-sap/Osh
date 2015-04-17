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


import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


public class ReceiveConsignmentStatus extends AbstractAction
{
	static private final Logger LOG = Logger.getLogger(ReceiveConsignmentStatus.class);

	public enum Transition
	{
		OK, PARTIAL, CANCEL, ERROR;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition t : Transition.values())
			{
				res.add(t.toString());
			}
			return res;
		}
	}

	public Transition executeAction(final ConsignmentProcessModel process)
	{
		Transition result = null;
		if (process.getWarehouseConsignmentState() == null)
		{
			LOG.error("Process has no warehouse consignment state");
			result = Transition.ERROR;
		}
		else
		{
			switch (process.getWarehouseConsignmentState())
			{
				case CANCEL:
					result = Transition.CANCEL;
					break;
				case COMPLETE:
					result = Transition.OK;
					break;
				case PARTIAL:
					result = Transition.PARTIAL;
					break;
				default:
					LOG.error("Unexpected warehouse consignment state: " + process.getWarehouseConsignmentState());
					result = Transition.ERROR;
			}
		}
		process.setWaitingForConsignment(false);
		modelService.save(process);
		return result;
	}

	@Override
	public String execute(final BusinessProcessModel process)
	{

		return executeAction((ConsignmentProcessModel) process).toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}
