/**
 * 
 */
package com.hybris.osh.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import org.apache.commons.io.FileUtils;

import com.hybris.osh.core.logger.OshIntegrationLogger;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.zehon.BatchTransferProgressDefault;
import com.zehon.FileTransferStatus;
import com.zehon.exception.FileTransferException;
import com.zehon.sftp.SFTPClient;


import org.apache.commons.io.FileUtils;

import com.hybris.osh.core.logger.OshIntegrationLogger;

public class OshSFTPService
{

	public void sendFile(final File srcFile, final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String sftpdestPath)
	{

		OshIntegrationLogger.info("**********SFTP Started*************");
		final int sftpPort = 22;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			if (sftpPassword != null)
			{
				session.setPassword(sftpPassword);
			}
			if (privateKey != null)
			{
				jsch.addIdentity(privateKey);
			}

			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			OshIntegrationLogger.info("**********Connected*************");
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(sftpdestPath);
			channelSftp.put(new FileInputStream(srcFile), srcFile.getName());
			channelSftp.disconnect();
			OshIntegrationLogger.info("Disconnected " + channelSftp.isConnected());
			channelSftp.exit();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public boolean checkProductFile(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String sftpdestPath)
	{

		OshIntegrationLogger.info("**********SFTP Started*************");
		final int sftpPort = 22;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		List<String> storeProductFilesList = null;
		List<String> controlFilesList = null;

		try
		{
			storeProductFilesList = new ArrayList();
			controlFilesList = new ArrayList();
			final JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			if (sftpPassword != null)
			{
				session.setPassword(sftpPassword);
			}
			if (privateKey != null)
			{
				jsch.addIdentity(privateKey);
			}

			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			OshIntegrationLogger.info("**********Connected*************");
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;

			channelSftp.cd(sftpdestPath);

			final Vector<ChannelSftp.LsEntry> list = channelSftp.ls(channelSftp.pwd());
			for (final ChannelSftp.LsEntry lists : list)
			{
				//fileslist.add(lists.getFilename());

				if ((lists.getFilename().endsWith(".dat")))
				{
					if (lists.getFilename().startsWith("catalog_"))
					{
						storeProductFilesList.add(lists.getFilename());
					}

				}

			}
			for (final ChannelSftp.LsEntry lists : list)
			{
				if (lists.getFilename().startsWith("control_"))
				{
				 final String filesCode = lists.getFilename().split("_")[1]; 
					if (storeProductFilesList.contains("catalog_" + filesCode))
					{
						controlFilesList.add(lists.getFilename());
					}

				}
			}
			channelSftp.disconnect();
			OshIntegrationLogger.info("Disconnected " + channelSftp.isConnected());
			channelSftp.exit();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		if (controlFilesList.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean checkPriceFile(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String sftpdestPath)
	{

		OshIntegrationLogger.info("**********SFTP Started*************");
		final int sftpPort = 22;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		List<String> priceFilesList = null;
		List<String> controlFilesList = null;

		try
		{
			priceFilesList = new ArrayList();
			controlFilesList = new ArrayList();
			final JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			if (sftpPassword != null)
			{
				session.setPassword(sftpPassword);
			}
			if (privateKey != null)
			{
				jsch.addIdentity(privateKey);
			}

			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			OshIntegrationLogger.info("**********Connected*************");
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;

			channelSftp.cd(sftpdestPath);

			final Vector<ChannelSftp.LsEntry> list = channelSftp.ls(channelSftp.pwd());
			for (final ChannelSftp.LsEntry lists : list)
			{
				//fileslist.add(lists.getFilename());

				if ((lists.getFilename().endsWith(".dat")))
				{
					if (lists.getFilename().startsWith("price_"))
					{
						priceFilesList.add(lists.getFilename());
					}
				}

			}
			for (final ChannelSftp.LsEntry lists : list)
			{
				if (lists.getFilename().startsWith("control_"))
				{
					final String filesCode = lists.getFilename().split("_")[1];
					if (priceFilesList.contains("price_" + filesCode))
					{
						controlFilesList.add(lists.getFilename());
					}

				}
			}
			channelSftp.disconnect();
			OshIntegrationLogger.info("Disconnected " + channelSftp.isConnected());
			channelSftp.exit();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		if (controlFilesList.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean retriveFile(final String sftpHost, final String sftpUser, final String sftpPassword, final String privateKey,
			final String sftpFolder, final String toLocalFolder, final String archiveFolderPath, final String copiedLocalFolderPath)
	{

		boolean isRetrived = false;
		final SFTPClient sftpClient = new SFTPClient(sftpHost, sftpUser, privateKey, true);

		try
		{
			if (sftpClient.folderExists(sftpFolder))
			{
				OshIntegrationLogger.info("Downloading the Files from " + sftpFolder);
				final int status = sftpClient.getFolderCreate(sftpFolder, toLocalFolder, new BatchTransferProgressDefault());

				if (FileTransferStatus.SUCCESS == status)
				{
					OshIntegrationLogger.info("Files are Downloaded Successfully in " + toLocalFolder);
					isRetrived = true;

				}
				else if (FileTransferStatus.FAILURE == status)
				{
					OshIntegrationLogger.error("Fail to download  to  folder " + toLocalFolder);
				}
			}
		}
		catch (final FileTransferException e)
		{
			OshIntegrationLogger.error("Unable to connect with transfer box" + e);
		}
		catch (final Exception e)
		{
			OshIntegrationLogger.error(e.getMessage());
		}

		return isRetrived;
	}

	public boolean retriveFileWithFileName(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String toLocalFolder, final String archiveFolderPath, final String file,
			final String filePath)
	{
		final SFTPClient sftpClient = new SFTPClient(sftpHost, sftpUser, privateKey, true);
		boolean isRetrived = false;
		try
		{
			if (sftpClient.fileExists(filePath, file))
			{
				final int status = sftpClient.getFile(file, filePath, toLocalFolder);

				if (FileTransferStatus.SUCCESS == status)
				{
					OshIntegrationLogger.info(" file copied successfully in " + filePath);
					if (sftpClient.fileExists(filePath, file))
					{
						final int fileMoveStatus = sftpClient.moveFile(file, filePath, "", archiveFolderPath);

						if (FileTransferStatus.SUCCESS == fileMoveStatus)
						{
							isRetrived = true;
							OshIntegrationLogger.info(file + " File Archived Succesfully");
						}
						else if (FileTransferStatus.FAILURE == fileMoveStatus)
						{
							OshIntegrationLogger.error(file + " File Archived Failed");
						}
					}
				}
				else if (FileTransferStatus.FAILURE == status)
				{
					OshIntegrationLogger.error("Fail to cppy file in " + filePath);
				}

			}
			else
			{
				OshIntegrationLogger.error("File:  " + file + " does not exist in the " + filePath + " location");
			}
		}
		catch (final FileTransferException e)
		{
			OshIntegrationLogger.error(e + "Facing FileTransferException while transfering " + file);
		}
		catch (final Exception e)
		{
			OshIntegrationLogger.error(e.getMessage());
		}
		return isRetrived;
	}


	public boolean retriveProductFile(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String toLocalFolder, final String archiveFolderPath,
			final Map<String, String> productFiles)
	{

		final SFTPClient sftpClient = new SFTPClient(sftpHost, sftpUser, privateKey, true);
		boolean isRetrived = false;
		for (final Entry<String, String> productFilesMap : productFiles.entrySet())
		{
			final String filePath = productFilesMap.getKey();
			final String file = productFilesMap.getValue();

			try
			{
				if (sftpClient.fileExists(filePath, file))
				{
					final int status = sftpClient.getFile(file, filePath, toLocalFolder);

					if (FileTransferStatus.SUCCESS == status)
					{
						OshIntegrationLogger.info(" File copied successfully in " + toLocalFolder);

					}
					else if (FileTransferStatus.FAILURE == status)
					{
						OshIntegrationLogger.error("Fail to cppy file in " + toLocalFolder);
					}
					final File sourceFile = new File(toLocalFolder + File.separator + file);
					boolean result = false;
					final File targetFile = new File(archiveFolderPath + File.separator + file);
					try
					{
						FileUtils.copyFile(sourceFile, targetFile);
						result = true;
					}
					catch (final IOException e)
					{
						OshIntegrationLogger.error("Error in copying the file to archive folder" + e.getMessage());
					}
					catch (final Exception e)
					{
						OshIntegrationLogger.error(e.getMessage());
					}
					try
					{
						if (result)
						{
							final int status1 = sftpClient.deleteFile(file, filePath);

							if (FileTransferStatus.SUCCESS == status1)
							{
								OshIntegrationLogger.info(" file removed successfully " + filePath);
							}
							else if (FileTransferStatus.FAILURE == status1)
							{
								OshIntegrationLogger.error("Fail to removed failed " + filePath);
							}
						}
					}
					catch (final FileTransferException e)
					{
						OshIntegrationLogger.error(e.getMessage());
					}
					catch (final Exception e)
					{
						OshIntegrationLogger.error(e.getMessage());
					}

					isRetrived = result;


				}
			}
			catch (final FileTransferException e)
			{
				OshIntegrationLogger.error(e + "Facing FileTransferException while transfering " + file);
			}
			catch (final Exception e)
			{
				OshIntegrationLogger.error(e.getMessage());
			}

		}
		return isRetrived;
	}


	public boolean sftpFiles(final String sftpHost, final String sftpUser, final String sftpPassword, final String sftpDestPath,
			final List<File> poFiles)
	{
		boolean fileShiftingStatus = false;
		final SFTPClient sftpClient = new SFTPClient(sftpHost, 22, sftpUser, sftpPassword);
		for (final File file : poFiles)
		{
			try
			{

				final int status = sftpClient.sendFile(file, sftpDestPath);
				if (FileTransferStatus.SUCCESS == status)
				{
					OshIntegrationLogger.info(" file moved successfully " + sftpDestPath);
					fileShiftingStatus = true;
				}
				else if (FileTransferStatus.FAILURE == status)
				{
					OshIntegrationLogger.error("Fail to move file " + sftpDestPath);
				}
			}
			catch (final Exception e)
			{
				OshIntegrationLogger.error("Failed to send Files" + e);
			}

		}
		return fileShiftingStatus;
	}

	/**
	 * This method will remove the images used for image magick by getting their name from sftp directories
	 * 
	 * @param sftpHost
	 * @param sftpUser
	 * @param sftpPassword
	 * @param privateKey
	 * @param sftpdestPath
	 * @return boolean
	 */
	public boolean removeOriginalImages(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String sftpdestPath)
	{

		OshIntegrationLogger.info("**********SFTP Started*************");
		final int sftpPort = 22;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			if (sftpPassword != null)
			{
				session.setPassword(sftpPassword);
			}
			if (privateKey != null)
			{
				jsch.addIdentity(privateKey);
			}

			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			OshIntegrationLogger.info("**********Connected*************");
			channel = session.openChannel("sftp");
			channelSftp = (ChannelSftp) channel;
			channelSftp.connect();
			channelSftp.cd(sftpdestPath);
			final Vector<ChannelSftp.LsEntry> list = channelSftp.ls(".");
			for (final ChannelSftp.LsEntry lsEntry : list)
			{
				if (lsEntry.getFilename() != null && !lsEntry.getFilename().isEmpty() && lsEntry.getFilename().endsWith(".jpg"))
				{
					channelSftp.rm(lsEntry.getFilename());
					OshIntegrationLogger.info("Image deletion done for " + lsEntry.getFilename());
				}
			}
			channelSftp.pwd();
			channelSftp.exit();
			channelSftp.disconnect();
			session.disconnect();
			return true;
		}
		catch (final Exception ex)
		{

			OshIntegrationLogger.error(ex.getMessage());
			return false;
		}
	}

	public boolean removeArchiveFoldersFromSftp(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String sftpdestPath)
	{
		OshIntegrationLogger.info("**********SFTP Started*************");
		final int sftpPort = 22;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			if (sftpPassword != null)
			{
				session.setPassword(sftpPassword);
			}
			if (privateKey != null)
			{
				jsch.addIdentity(privateKey);
			}

			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			OshIntegrationLogger.info("**********Connected*************");
			channel = session.openChannel("sftp");
			channelSftp = (ChannelSftp) channel;
			channelSftp.connect();
			channelSftp.cd(sftpdestPath);

			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -7);
			final Date compareDate = sdf.parse(sdf.format(cal.getTime()));

			final Vector<ChannelSftp.LsEntry> list = channelSftp.ls(".");
			for (final ChannelSftp.LsEntry lsEntry : list)
			{
				final String dirName = lsEntry.getFilename();
				if (!(dirName.equals(".") || dirName.equals("..") || dirName.equals("osh")))
				{
					if (lsEntry.getFilename() != null && !lsEntry.getFilename().isEmpty())
					{
						final Date dir_dt = sdf.parse(dirName);
						if (dir_dt.compareTo(compareDate) < 0)
						{
							channelSftp.cd(dirName);

							final Vector<ChannelSftp.LsEntry> list11 = channelSftp.ls(".");
							for (final ChannelSftp.LsEntry lsEntry1 : list11)
							{
								final String fileName = lsEntry1.getFilename();
								if (!(fileName.equals(".") || fileName.equals("..")))
								{
									if (lsEntry1.getFilename() != null && !lsEntry1.getFilename().isEmpty())
									{
										channelSftp.rm(lsEntry1.getFilename());
									}
								}
							}
							channelSftp.cd("..");
							channelSftp.rmdir(dirName);
						}
					}
				}
			}
			channelSftp.pwd();
			channelSftp.exit();
			channelSftp.disconnect();
			session.disconnect();
			return true;
		}
		catch (final Exception ex)
		{

			OshIntegrationLogger.error(ex.getMessage());
			return false;
		}
	}

	public boolean archiveFoldersFile(final String sftpHost, final String sftpUser, final String sftpPassword,
			final String privateKey, final String filePath, final List<File> poFiles, final String arhiveFolderPath,
			final String sftpFolder)
	{

		final SFTPClient sftpClient = new SFTPClient(sftpHost, sftpUser, privateKey, true);
		final Calendar cal = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String currentDate = sdf.format(cal.getTime());
		boolean result = false;
		for (final File file : poFiles)
		{
			final String fileName = file.getName();

			try
			{
				if (!sftpClient.folderExists(arhiveFolderPath + File.separator + currentDate))
				{
					sftpClient.createFolder(currentDate, arhiveFolderPath);
				}
				final File sourceFile = new File(filePath + File.separator + fileName);
				sftpClient.sendFile(sourceFile, arhiveFolderPath + File.separator + currentDate);
				//FileUtils.copyFile(sourceFile, targetFile);
				result = true;
			}
			catch (final FileTransferException e)
			{
				OshIntegrationLogger.error(e.getMessage());
			}
			catch (final Exception e)
			{
				OshIntegrationLogger.error(e.getMessage());
			}
			try
			{
				if (result)
				{
					final int status = sftpClient.deleteFile(fileName, sftpFolder);

					if (FileTransferStatus.SUCCESS == status)
					{
						OshIntegrationLogger.info(" file removed successfully " + sftpFolder);
					}
					else if (FileTransferStatus.FAILURE == status)
					{
						OshIntegrationLogger.error("Fail to removed failed " + sftpFolder);
					}
				}
			}
			catch (final FileTransferException e)
			{
				OshIntegrationLogger.error(e.getMessage());
			}
			catch (final Exception e)
			{
				OshIntegrationLogger.error(e.getMessage());
			}

		}
		return result;

	}
}
