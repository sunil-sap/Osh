/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.util.Assert;

import com.hybris.osh.core.service.OshSFTPService;
import com.hybris.osh.core.service.SFTPStoreExportService;


/**
 * 
 * 
 */
public class DefaultSFTPStoreExportService implements SFTPStoreExportService
{
	private ConfigurationService configurationService;
	private OshSFTPService oshSFTPService;
	private static final String STORE_FOLDER = "pom.store.file.sftp.folder";
	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_PORT = "tlog.file.sftp.port";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";
	private static final String TO_FOLDER = "pom.product.store.file.sftp.to.folder";
	private static final String ARCH_FOLDER = "pom.sftp.file.archive.folder";
	private static final String SFTP_FOLDER = "pom.store.file.sftp.folder";
	private static final String SFTP_COPY_FOLDER = "pom.product.copy.store.file.sftp.to.folder";

	public static String sftpHost = null;
	public static String sftpUser = null;
	public static String storeSourceFolder = null;
	public static String storeLocalStatusFolder = null;
	public static String privateKey = null;
	public static String archiveFolderPath = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.StoreExportService#isStoreFileExported()
	 */
	@Override
	public boolean isStoreFileExported()
	{
		final String storeFilePath = configurationService.getConfiguration().getString(STORE_FOLDER);
		Assert.notNull(storeFilePath, " Folder Path Can Not BE NULL");

		sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		storeSourceFolder = configurationService.getConfiguration().getString(SFTP_FOLDER);
		Assert.notNull(storeSourceFolder, "SFTP User Can Not BE NULL");

		privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		archiveFolderPath = configurationService.getConfiguration().getString(ARCH_FOLDER);
		Assert.notNull(archiveFolderPath, "SFTP archiveFolderPath  Can Not BE NULL");

		final String toLocalFolder = configurationService.getConfiguration().getString(TO_FOLDER);

		storeLocalStatusFolder = configurationService.getConfiguration().getString(SFTP_COPY_FOLDER);
		Assert.notNull(storeLocalStatusFolder, "SFTP storeLocalStatusFolder  Can Not BE NULL");

		final File storeFolder = new File(storeLocalStatusFolder);
		recursiveDelete(storeFolder);

		final File store = new File(toLocalFolder);
		if (!store.exists())
		{
			store.mkdir();
		}
		final StringBuilder archivePath = new StringBuilder(archiveFolderPath);
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String currentDate = sdf.format(calendar.getTime());
		archivePath.append("_" + currentDate);

		final File archive = new File(archivePath.toString());
		if (!archive.exists())
		{
			archive.mkdir();
		}

		return oshSFTPService.retriveFile(sftpHost, sftpUser, null, privateKey, storeSourceFolder, toLocalFolder,
				archivePath.toString(), storeLocalStatusFolder);

	}

	@Override
	public boolean getArchiveStatus()
	{
		final String storeFolder = configurationService.getConfiguration().getString(SFTP_COPY_FOLDER);
		Assert.notNull(storeFolder, "Store Folder Can Not BE NULL");

		final File[] files = new File(storeFolder).listFiles();
		final List<File> controlFiles = new ArrayList<File>();
		final List<File> results = new ArrayList<File>();
		final List<File> removeFile = new ArrayList<File>();

		final String fileName = "onhand_chgs";
		for (final File file : files)
		{
			if (file.isFile())
			{
				if (file.getName().startsWith("control_"))
				{
					controlFiles.add(file);
				}
				results.add(file);
			}
		}
		for (final File file : controlFiles)
		{
			final String[] filename = file.getName().split(".dat");
			final String[] controlfile = filename[0].split("control_");
			final String controlnum = controlfile[1];
			final String fileNamingFormat = fileName + controlnum;

			for (final File result : results)
			{
				if (result.getName().startsWith(fileNamingFormat))
				{
					removeFile.add(result);
				}
			}
			removeFile.add(file);
		}

		final StringBuilder archivePath = new StringBuilder(archiveFolderPath);

		return oshSFTPService.archiveFoldersFile(sftpHost, sftpUser, null, privateKey, storeLocalStatusFolder, removeFile,
				archivePath.toString(), storeSourceFolder);

	}

	public void recursiveDelete(final File file)
	{
		if (!file.exists())
		{
			return;
		}
		if (file.isDirectory())
		{
			for (final File f : file.listFiles())
			{
				recursiveDelete(f);
			}
			file.delete();
		}
		else
		{
			file.delete();
		}
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public OshSFTPService getOshSFTPService()
	{
		return oshSFTPService;
	}

	public void setOshSFTPService(final OshSFTPService oshSFTPService)
	{
		this.oshSFTPService = oshSFTPService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.SFTPStoreExportService#getArchiveStatus()
	 */

}
