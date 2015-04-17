package com.hybris.osh.events;

import com.hybris.osh.core.model.email.EmailToFriendModel;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
public class MailSendToFriendEvent extends AbstractCommerceUserEvent<BaseSiteModel>{

	private EmailToFriendModel process=null;

	public MailSendToFriendEvent()
	{
		super();
	}
	public MailSendToFriendEvent(EmailToFriendModel emailToFriendModel)
	{
		super();
		this.process = emailToFriendModel;
	}

	public EmailToFriendModel getProcess()
	{
		return process;
	}

}
