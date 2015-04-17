/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.core.service.OshSFTPService;
import com.hybris.osh.core.service.SFTPPriceCheckService;


/**
 * 
 * 
 */
public class DefaultSFTPPriceCheckService implements SFTPPriceCheckService
{
	private static final Logger LOG = Logger.getLogger(DefaultSFTPProductExportService.class);


	private ConfigurationService configurationService;
	private OshSFTPService oshSFTPService;
	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";
	private static final String TO_FOLDER = "pom.product.store.file.sftp.to.folder";
	private static final String FILE_EXT = "pom.sftp.store.file.name.ext";
	private static final String ARCH_FOLDER = "pom.sftp.file.archive.folder";
	private static final String SFTP_FOLDER = "pom.product.file.sftp.from.folder";
	private static final String SFTP_COPY_FOLDER = "pom.price.file.sftp.folder";
	private static final String LOCAL_FOLDER = "pom.product.file.sftp.to.folder";


	public static String sftpHost = null;
	public static String sftpUser = null;
	public static String productSourceFolder = null;
	public static String productLocalFolder = null;
	public static String privateKey = null;
	public static String archiveFolderPath = null;
	public static String localFolderPath = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.StoreExportService#isStoreFileExported()
	 */

	@Override
	public boolean isPriceFilesPresent()
	{
		sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		productSourceFolder = configurationService.getConfiguration().getString(SFTP_FOLDER);
		Assert.notNull(productSourceFolder, "SFTP User Can Not BE NULL");

		privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		archiveFolderPath = configurationService.getConfiguration().getString(ARCH_FOLDER);
		Assert.notNull(archiveFolderPath, "SFTP archiveFolderPath  Can Not BE NULL");

		final String toLocalFolder = configurationService.getConfiguration().getString(TO_FOLDER);

		productLocalFolder = configurationService.getConfiguration().getString(SFTP_COPY_FOLDER);
		Assert.notNull(productLocalFolder, "SFTP storeLocalStatusFolder  Can Not BE NULL");

		localFolderPath = configurationService.getConfiguration().getString(LOCAL_FOLDER);
		Assert.notNull(localFolderPath, "SFTP storeLocalStatusFolder  Can Not BE NULL");


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
		return oshSFTPService.checkPriceFile(sftpHost, sftpUser, null, privateKey, productLocalFolder);
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
}
