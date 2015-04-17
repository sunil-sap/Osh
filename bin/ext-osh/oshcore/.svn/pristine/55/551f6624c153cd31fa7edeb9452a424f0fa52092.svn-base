/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.ContactUsEmailService;
import com.hybris.osh.core.storename.dao.OshStoreNameDao;


/**
 * This service is used to send mail.
 * 
 */
public class DefaultContactUsEmailService implements ContactUsEmailService
{

	protected static final Logger LOG = Logger.getLogger(DefaultContactUsEmailService.class);

	private OshStoreNameDao oshStoreNameDao;

	/**
	 * @return the oshStoreNameDao
	 */
	public OshStoreNameDao getOshStoreNameDao()
	{
		return oshStoreNameDao;
	}

	/**
	 * @param oshStoreNameDao
	 *           the oshStoreNameDao to set
	 */
	public void setOshStoreNameDao(final OshStoreNameDao oshStoreNameDao)
	{
		this.oshStoreNameDao = oshStoreNameDao;
	}

	/*
	 * This method is used to get store name.
	 */
	@Override
	public List<PointOfServiceModel> getStoreNames()
	{

		final List<PointOfServiceModel> storeNameList = oshStoreNameDao.storeName();

		return storeNameList;
	}

	/*
	 * This method will send email for contact us.
	 */
	@Override
	public void sendMail(final String firstName, final String lastName, final String emailAddress, final String emailContent,
			final String storeName, final String subject)
	{

		final String username = Config.getParameter("mail.smtp.user");
		final String password = Config.getParameter("mail.smtp.password");
		final Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Config.getParameter("mail.smtp.server"));
		props.put("mail.smtp.port", Config.getParameter("mail.smtp.port"));
		props.put("mail.smtp.auth", "true");
		try
		{
			final Session session = Session.getInstance(props, new javax.mail.Authenticator()
			{
				@Override
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			});

			final Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(username));
			msg.setSubject(subject);
			msg.setText(emailAddress + ":  " + emailContent);

			Transport.send(msg);

		}
		catch (final MessagingException e)
		{
			LOG.error(e.getMessage());
		}
	}

}
