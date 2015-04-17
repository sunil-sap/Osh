/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.core.service.OshSFTPService;
import com.hybris.osh.core.service.SFTPPriceExportService;


/**
 * 
 * 
 */
public class DefaultSFTPPriceExportService implements SFTPPriceExportService
{
	private static final Logger LOG = Logger.getLogger(DefaultSFTPPriceExportService.class);


	private ConfigurationService configurationService;
	private OshSFTPService oshSFTPService;
	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";
	private static final String TO_FOLDER = "pom.product.store.file.sftp.to.folder";
	private static final String ARCH_FOLDER = "pom.sftp.file.archive.folder";
	private static final String SFTP_FOLDER = "pom.price.file.sftp.folder";
	private static final String FILE_EXT = "pom.sftp.store.file.name.ext";
	private static final String SFTP_COPY_FOLDER = "pom.product.copy.price.file.sftp.to.folder";
	private static final String LOCAL_FOLDER = "pom.product.file.sftp.to.folder";

	public static String sftpHost = null;
	public static String sftpUser = null;
	public static String priceSourceFolder = null;
	public static String priceLocalFolder = null;
	public static String privateKey = null;
	public static String archiveFolderPath = null;
	public static String localFolderPath = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.StoreExportService#isStoreFileExported()
	 */
	@Override
	public boolean isPriceFileExported()
	{
		sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		priceSourceFolder = configurationService.getConfiguration().getString(SFTP_FOLDER);
		Assert.notNull(priceSourceFolder, "SFTP User Can Not BE NULL");

		archiveFolderPath = configurationService.getConfiguration().getString(ARCH_FOLDER);
		Assert.notNull(archiveFolderPath, "SFTP archiveFolderPath  Can Not BE NULL");

		final String toLocalFolder = configurationService.getConfiguration().getString(TO_FOLDER);

		priceLocalFolder = configurationService.getConfiguration().getString(SFTP_COPY_FOLDER);
		Assert.notNull(priceLocalFolder, "SFTP storeLocalStatusFolder  Can Not BE NULL");

		localFolderPath = configurationService.getConfiguration().getString(LOCAL_FOLDER);
		Assert.notNull(localFolderPath, "SFTP storeLocalStatusFolder  Can Not BE NULL");

		final File storeFolder = new File(priceLocalFolder);
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

		return oshSFTPService.retriveFile(sftpHost, sftpUser, null, privateKey, priceSourceFolder, toLocalFolder,
				archivePath.toString(), priceLocalFolder);

	}

	@Override
	public boolean getArchiveStatus()
	{
		final String priceFolder = configurationService.getConfiguration().getString(SFTP_COPY_FOLDER);
		Assert.notNull(priceFolder, "Store Folder Can Not BE NULL");

		final String fileExt = configurationService.getConfiguration().getString(FILE_EXT);
		Assert.notNull(fileExt, "Store Folder Can Not BE NULL");


		final File[] files = new File(priceFolder).listFiles();
		final List<File> controlFiles = new ArrayList<File>();
		final List<File> results = new ArrayList<File>();
		final List<File> removeFile = new ArrayList<File>();

		final String fileName = "price_";
		for (final File file : files)
		{
			if (file.isFile() && (file.getName().endsWith(fileExt)))
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
			final String[] filename = file.getName().split(fileExt);
			final String[] controlfile = filename[0].split("control_");
			final String controlnum = controlfile[1];
			final String fileNamingFormat = fileName + controlnum;

			for (final File result : results)
			{
				if (result.getName().toLowerCase().startsWith(fileNamingFormat))
				{
					removeFile.add(result);
				}
			}
			removeFile.add(file);
		}

		final StringBuilder archivePath = new StringBuilder(archiveFolderPath);

		final boolean result = oshSFTPService.archiveFoldersFile(sftpHost, sftpUser, null, privateKey, priceLocalFolder,
				removeFile, archivePath.toString(), priceSourceFolder);

		if (result)
		{
			final File localPricefolder = new File(priceLocalFolder);
			final File[] listOfFilesInLocalFolder = localPricefolder.listFiles();

			for (int i = 0; i < listOfFilesInLocalFolder.length; i++)
			{
				if (listOfFilesInLocalFolder[i].isFile() && !listOfFilesInLocalFolder[i].getName().contains("control_"))
				{
					final File sourceFile = new File(priceFolder + File.separator + listOfFilesInLocalFolder[i].getName());
					final File targetFile = new File(localFolderPath + File.separator + "price" + fileExt);

					try
					{
						FileUtils.copyFile(sourceFile, targetFile);
					}
					catch (final IOException e)
					{
						LOG.info("Fail to copy the file in POM location" + e.getMessage());
						return false;
					}
				}
			}
			/*
			 * final File folder = new File(localFolderPath); final File[] listOfFiles = folder.listFiles();
			 * 
			 * for (int i = 0; i < listOfFiles.length; i++) { if (listOfFiles[i].isFile()) { final String changeFileName =
			 * listOfFiles[i].getName(); final String fileNameArr[] = changeFileName.split("_"); if (fileNameArr.length >
			 * 0) { final File renamedFile = listOfFiles[i]; final String destPath = localFolderPath + File.separator +
			 * fileNameArr[0] + fileExt; final File newFile = new File(destPath); renamedFile.renameTo(newFile); } } }
			 */

			LOG.info("conversion is done");
			return true;
		}
		return false;
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
}
