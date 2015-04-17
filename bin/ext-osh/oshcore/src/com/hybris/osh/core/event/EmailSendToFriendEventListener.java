/**
 * 
 */
package com.hybris.osh.core.event;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.hybris.osh.core.model.email.EmailToFriendModel;
import com.hybris.osh.core.orderprocessing.model.SendEmailToFriendProcessModel;
import com.hybris.osh.events.MailSendToFriendEvent;


/**
 * send Mail to friend
 */
public class EmailSendToFriendEventListener extends AbstractEventListener<MailSendToFriendEvent>
{
	private static final Logger LOG = Logger.getLogger(EmailSendToFriendEventListener.class);

	public BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}

	public ModelService getModelServiceViaLookup()
	{
		throw new UnsupportedOperationException(
				"Please define in the spring configuration a <lookup-method> for getModelServiceViaLookup().");
	}

	@Override
	protected void onEvent(final MailSendToFriendEvent event)
	{
		final SendEmailToFriendProcessModel sendEmailToFriendProcessModel = (SendEmailToFriendProcessModel) getBusinessProcessService()
				.createProcess("SendEmailToFriend" + System.currentTimeMillis(), "SendEmailToFriendProcess");
		final EmailToFriendModel emailToFriendModel = event.getProcess();
		sendEmailToFriendProcessModel.setSite(event.getSite());
		sendEmailToFriendProcessModel.setFromEmail(emailToFriendModel.getFromEmail());
		sendEmailToFriendProcessModel.setToEmail(emailToFriendModel.getToEmail());
		sendEmailToFriendProcessModel.setSubject(emailToFriendModel.getSubject());
		sendEmailToFriendProcessModel.setProduct(emailToFriendModel.getProduct());
		sendEmailToFriendProcessModel.setNotes(emailToFriendModel.getNotes());
		getModelServiceViaLookup().save(sendEmailToFriendProcessModel);
		getBusinessProcessService().startProcess(sendEmailToFriendProcessModel);
	}
}
