package com.hybris.osh.core.service.impl;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.FileTransferService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


/**
 * Implementation class for FileTransferService
 * 
 */
public class DefaultFileTransferService implements FileTransferService
{

	Logger log = Logger.getLogger(DefaultFileTransferService.class);

	@Override
	public void sendFile(final File fileToBeSent)
	{

		log.info("**********Started*************");
		final String SFTPHOST = "";
		final int SFTPPORT = 22;
		final String SFTPUSER = "osh";
		final String SFTPPASS = "";
		final String SFTPWORKINGDIR = "";
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
			session.setPassword(SFTPPASS);
			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			log.info("**********Connected*************");
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			log.info("*** channelSftp.isConnected *** " + channelSftp.isConnected());
			channelSftp.cd(SFTPWORKINGDIR);
			//final File f = new File("D:\\Order_Export_Loyalty\\OSH_transactions_" + formattedDate + ".xml");
			//final String fileName = f.getName();
			log.info("***fileName*** " + fileToBeSent.getName());
			channelSftp.put(new FileInputStream(fileToBeSent), fileToBeSent.getName());
			log.info("**********Sent*************");
			channelSftp.disconnect();
			log.info("Disconnected" + channelSftp.isConnected());
			channelSftp.exit();

			log.info("*** channel.isClosed()*** " + channel.isClosed());
			log.info("*** channel.getExitStatus()*** " + channel.getExitStatus());
			log.info("*** channel.isConnected *** " + channel.isConnected());
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}


	}

}
