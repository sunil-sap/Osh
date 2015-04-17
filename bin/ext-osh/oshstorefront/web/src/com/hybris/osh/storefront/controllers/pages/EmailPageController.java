/**
 * 
 */
package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.osh.storefront.forms.SendEmailForm;


@Controller
@Scope("tenant")
public class EmailPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(EmailPageController.class);

	@ResponseBody
	@RequestMapping(value = "/sendEmail/sendEmailToFriend.json", method = RequestMethod.POST)
	public void sendEmailToFriend(@Valid final SendEmailForm form) throws CMSItemNotFoundException
	{
		final String username = "nagendrasikarwar034@gmail.com";
		final String password = "7620387597";
		// Recipient's email ID needs to be mentioned.
		final String to = form.getTo_emailadd();

		// Sender's email ID needs to be mentioned
		final String from = form.getFrom_email();

		// Assuming you are sending email from localhost
		//final String host = "localhost:9101";

		// Get system properties
		//final Properties properties = System.getProperties();

		final Properties properties = new Properties();
		/*
		 * properties.put("mail.smtp.auth", "true"); properties.put("mail.smtp.starttls.enable", "true");
		 * properties.put("mail.smtp.server", "smtpout.secureserver.net"); properties.put("mail.smtp.port", "25");
		 */

		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		// Setup mail server
		//properties.setProperty("smtpout.secureserver.net", host);

		//properties.setProperty("mail.user", "myuser");
		//properties.setProperty("mail.password", "mypwd");

		// Get the default Session object.
		final Session session = Session.getInstance(properties, new javax.mail.Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try
		{
			// Create a default MimeMessage object.
			final MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(form.getSubject());

			// Now set the actual message
			message.setText(form.getNotes());

			// Send message
			Transport.send(message);
			Logger.getLogger("Sent message successfully....");
		}
		catch (final MessagingException mex)
		{
			mex.printStackTrace();
		}


	}
}
