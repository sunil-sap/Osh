package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.dao.CheetahMailSubscriberDao;
import com.hybris.osh.core.model.EmailSubscriberModel;
import com.hybris.osh.core.service.CheetahMailSubscriberService;
import com.hybris.osh.core.service.OshSFTPService;


public class DefaultCheetahMailSubscriberService implements CheetahMailSubscriberService
{
	private final Logger log = Logger.getLogger(DefaultCheetahMailSubscriberService.class);
	private CheetahMailSubscriberDao cheetahMailSubscriberDao;

	@Resource
	ModelService modelService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "oshSFTPService")
	private OshSFTPService oshSFTPService;

	private static final String SFTPSOURCE = "cheetah.mail.sftp.sourcefolder";
	private static final String SFTPDEST = "cheetah.mail.sftp.dir";
	private static final String SFTPHOST = "cheetah.mail.sftp.host";
	private static final String SFTPPASS = "cheetah.mail.sftp.password";
	private static final String SFTPUSER = "cheetah.mail.sftp.user";

	@Override
	public void getSubscribedUser()
	{
		final List<EmailSubscriberModel> emailSubcriber = getCheetahMailSubscriberDao().findCheetahMailSubscribedCustomer();


		final Date date = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String formattedDate = sdf.format(date);
		try
		{
			final String sftpSource = configurationService.getConfiguration().getString(SFTPSOURCE);
			final File parentDir = new File(sftpSource + "\\" + formattedDate);
			parentDir.mkdirs();
			final String hash = "osh_customers_pragiti";
			final String fileName = formattedDate + "_" + hash + ".txt";

			final File file = new File(parentDir, fileName);

			final BufferedWriter output = new BufferedWriter(new FileWriter(file));
			file.createNewFile();
			for (final EmailSubscriberModel emailSubscriberModel : emailSubcriber)
			{
				final CustomerModel customerModel = emailSubscriberModel.getSubscribedCustomer();
				output.write(emailSubscriberModel.getEmailId());
				if (customerModel != null && customerModel.getDefaultPaymentAddress() != null)
				{
					final String name = customerModel.getName().trim();
					final String[] arr = name.split(" ");
					output.append(",");
					output.append(customerModel.getDefaultPaymentAddress().getLine1());
					output.append(",");
					if (customerModel.getDefaultPaymentAddress().getLine2() != null)
					{
						output.append(customerModel.getDefaultPaymentAddress().getLine2());
					}
					output.append(",");
					output.append(customerModel.getDefaultPaymentAddress().getTown());
					output.append(",");
					output.append(arr[0]);
					output.append(",");
					output.append("Y");
					output.append(",");
					output.append("Y");
					output.append(",");
					output.append(arr[1]);
					output.append(",");
					output.append("hybris");
					output.append(",");
					output.append(customerModel.getDefaultPaymentAddress().getRegion().getIsocode());
					output.append(",");
					output.append(customerModel.getDefaultPaymentAddress().getPostalcode());
				}
				else
				{
					output.append(",,,,,,,,,," + emailSubscriberModel.getPostalCode());
				}
				output.newLine();
			}

			output.flush();

			if (file.exists())
			{
				output.close();
				sendFile(file);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

	}


	@Override
	public void getUnSubscribedUser()
	{

		final List<EmailSubscriberModel> emailUnsubscriber = getCheetahMailSubscriberDao().findCheetahMailUnSubscribedCustomer();

		final Date date = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String formattedDate = sdf.format(date);
		try
		{
			final String sftpSource = configurationService.getConfiguration().getString(SFTPSOURCE);
			final File parentDir = new File(sftpSource + "\\" + formattedDate);
			parentDir.mkdir();
			final String hash = "osh_customers_unsubscribe_pragiti";
			final String fileName = formattedDate + "_" + hash + ".txt";

			final File file = new File(parentDir, fileName);

			final BufferedWriter output = new BufferedWriter(new FileWriter(file));
			file.createNewFile();
			for (final EmailSubscriberModel emailSubscriberModel : emailUnsubscriber)
			{

				output.write(emailSubscriberModel.getEmailId());
				output.newLine();

			}

			output.flush();
			if (file.exists())
			{
				output.close();
				sendFile(file);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendFile(final File f)
	{
		log.info("**********Started*************");

		final String sftpWorkingDir = configurationService.getConfiguration().getString(SFTPDEST);
		final String sftpHost = configurationService.getConfiguration().getString(SFTPHOST);
		final int sftpPort = 22;
		final String sftpUser = configurationService.getConfiguration().getString(SFTPUSER);

		final String sftpPass = configurationService.getConfiguration().getString(SFTPPASS);

		final List<File> files = new ArrayList<File>();
		files.add(f);
		oshSFTPService.sftpFiles(sftpHost, sftpUser, sftpPass, sftpWorkingDir, files);
		/*
		 * Session session = null; Channel channel = null; ChannelSftp channelSftp = null; //final Date date = new Date();
		 * //final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); //final String formattedDate =
		 * sdf.format(date);
		 * 
		 * try { final JSch jsch = new JSch(); session = jsch.getSession(sftpUser, sftpHost, sftpPort);
		 * session.setPassword(sftpPass); final java.util.Properties config = new java.util.Properties();
		 * config.put("StrictHostKeyChecking", "no"); session.setConfig(config); session.connect();
		 * log.info("**********Connected*************"); channel = session.openChannel("sftp"); channel.connect();
		 * channelSftp = (ChannelSftp) channel; log.info("*** channelSftp.isConnected *** " + channelSftp.isConnected());
		 * channelSftp.cd(sftpWorkingDir); log.info("***fileName*** " + f.getName()); channelSftp.put(new
		 * FileInputStream(f), f.getName()); log.info("**********Sent*************"); channelSftp.disconnect();
		 * log.info("Disconnected" + channelSftp.isConnected());
		 * 
		 * channelSftp.exit();
		 * 
		 * log.info("*** channel.isClosed()*** " + channel.isClosed()); log.info("*** channel.getExitStatus()*** " +
		 * channel.getExitStatus()); log.info("*** channel.isConnected *** " + channel.isConnected()); } catch (final
		 * Exception ex) { ex.printStackTrace(); }
		 */

	}

	/*
	 * @Override public boolean setSubscriber(final String emailId, final boolean subscriber) {
	 * 
	 * if (subscriber && (emailId != null) && (!emailId.isEmpty())) { try { final EmailSubscriberModel subscriberModel =
	 * modelService.create(EmailSubscriberModel.class); subscriberModel.setSubscriber(subscriber);
	 * subscriberModel.setEmailId(emailId); modelService.save(subscriberModel); modelService.refresh(subscriberModel);
	 * return true; } catch (final ModelSavingException e) { return false; } } return false;
	 * 
	 * }
	 */


	@Override
	public boolean unSubscribeCheetahMail(final String emailId, final boolean subscriber)
	{
		if ((emailId != null) && (!emailId.isEmpty()))
		{
			try
			{
				final EmailSubscriberModel emailSubscriber = getCheetahMailSubscriberDao().findEmailSubscriber(emailId);
				if (emailSubscriber != null && emailSubscriber.isSubscriber())
				{
					emailSubscriber.setSubscriber(subscriber);
					modelService.save(emailSubscriber);
					modelService.refresh(emailSubscriber);
					return true;
				}
			}
			catch (final Exception e)
			{
				log.info("Unable TO Unsubscribe User", e);
				return false;
			}

		}
		return false;

	}

	@Override
	public boolean setSubscriber(final String emailId, final boolean subscriber)
	{

		if ((emailId != null) && (!emailId.isEmpty()))
		{

			try
			{
				final EmailSubscriberModel emailSubscriber = getCheetahMailSubscriberDao().findEmailSubscriber(emailId);
				final CustomerModel customer = getCheetahMailSubscriberDao().findEmailSubscribedCustomer(emailId);
				if (emailSubscriber == null)
				{
					final EmailSubscriberModel subscriberModel = modelService.create(EmailSubscriberModel.class);
					subscriberModel.setSubscriber(subscriber);
					subscriberModel.setEmailId(emailId);
					subscriberModel.setSubscribedCustomer(customer);
					modelService.save(subscriberModel);
					modelService.refresh(subscriberModel);

				}
				else
				{
					emailSubscriber.setSubscriber(subscriber);
					emailSubscriber.setSubscribedCustomer(customer);
					emailSubscriber.setEmailId(emailId);
					modelService.save(emailSubscriber);
					modelService.refresh(emailSubscriber);
				}
				return true;

			}
			catch (final ModelSavingException e)
			{
				return false;
			}
		}
		return false;

	}

	@Override
	public boolean setSubscriber(final String emailId, final boolean subscriber, final String postalCode)
	{

		if ((emailId != null) && (!emailId.isEmpty()) && (postalCode != null) && (!postalCode.isEmpty()))
		{

			try
			{
				final EmailSubscriberModel emailSubscriber = getCheetahMailSubscriberDao().findEmailSubscriber(emailId);
				final CustomerModel customer = getCheetahMailSubscriberDao().findEmailSubscribedCustomer(emailId);
				if (emailSubscriber == null)
				{
					final EmailSubscriberModel subscriberModel = modelService.create(EmailSubscriberModel.class);
					subscriberModel.setSubscriber(subscriber);
					subscriberModel.setEmailId(emailId);
					subscriberModel.setPostalCode(postalCode);
					subscriberModel.setSubscribedCustomer(customer);
					modelService.save(subscriberModel);
					modelService.refresh(subscriberModel);

				}
				else
				{
					emailSubscriber.setSubscriber(subscriber);
					emailSubscriber.setSubscribedCustomer(customer);
					emailSubscriber.setPostalCode(postalCode);
					emailSubscriber.setEmailId(emailId);
					modelService.save(emailSubscriber);
					modelService.refresh(emailSubscriber);
				}
				return true;

			}
			catch (final ModelSavingException e)
			{
				return false;
			}
		}
		return false;

	}


	/**
	 * @return the cheetahMailSubscriberDao
	 */
	public CheetahMailSubscriberDao getCheetahMailSubscriberDao()
	{
		return cheetahMailSubscriberDao;
	}



	/**
	 * @param cheetahMailSubscriberDao
	 *           the cheetahMailSubscriberDao to set
	 */
	public void setCheetahMailSubscriberDao(final CheetahMailSubscriberDao cheetahMailSubscriberDao)
	{
		this.cheetahMailSubscriberDao = cheetahMailSubscriberDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.CheetahMailSubscriberService#setSubscriber(java.lang.String, boolean)
	 */
}
