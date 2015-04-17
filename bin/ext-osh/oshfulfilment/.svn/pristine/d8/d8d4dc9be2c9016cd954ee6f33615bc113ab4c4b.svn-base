package com.hybris.osh.actions.csevents;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.actions.consignmentfulfilment.AbstractConsignmentSimpleDecisionAction;


import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.acceleratorservices.email.EmailService;



/**
 * A process action to send email after the consignment is shipped.
 */
public class GenerateSuccessfulShipmentEmail extends AbstractSimpleDecisionAction
{
	
	
	@Resource(name = "emailService")
	EmailService emailService;
	
	@Override
	public Transition executeAction(BusinessProcessModel process)
			throws RetryLaterException, Exception {
		for (final EmailMessageModel email : process.getEmails())
		{
			emailService.send(email);
		}
		process.setState(ProcessState.SUCCEEDED);
		save(process);
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " wrote DONE marker");
		return Transition.OK;
	}
	
}
