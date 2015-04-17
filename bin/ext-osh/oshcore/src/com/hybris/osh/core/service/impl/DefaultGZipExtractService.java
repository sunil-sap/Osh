/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.GZipExtractService;


/**
 * DefaultGZipExtractService
 * 
 */
public class DefaultGZipExtractService implements GZipExtractService
{
	private static final String OUTPUT_FILE = "pom.product.store.file.sftp.outputFile";
	private static final String FILE_DIR = "pom.product.copy.store.file.sftp.to.folder";
	private static final String FILE_TEXT_EXT = ".gz";
	private ConfigurationService configurationService;
	private static final Logger LOG = Logger.getLogger(DefaultGZipExtractService.class);

	@Override
	public boolean isExtracted()
	{
		boolean result = false;
		final GenericExtFilter filter = new GenericExtFilter(FILE_TEXT_EXT);

		final File dir = new File(configurationService.getConfiguration().getString(FILE_DIR));

		if (!dir.isDirectory())
		{
			LOG.info("Directory does not exists : " + configurationService.getConfiguration().getString(FILE_DIR));
			return false;
		}

		// list out all the file name and filter by the extension
		final String[] list = dir.list(filter);

		if (list.length == 0)
		{
			LOG.info("No files end with : " + configurationService.getConfiguration().getString(FILE_DIR));
			return false;
		}
		else
		{
			for (final String file : list)
			{
				final String url = new StringBuffer(configurationService.getConfiguration().getString(FILE_DIR))
						.append(File.separator).append(file).toString();
				FileInputStream fileInputStream = null;
				try
				{
					fileInputStream = new FileInputStream(url);
				}
				catch (final FileNotFoundException e)
				{

					LOG.error(e.getMessage());
				}
				GZIPInputStream gZIPInputStream = null;
				try
				{
					gZIPInputStream = new GZIPInputStream(fileInputStream);
				}
				catch (final IOException e)
				{
					LOG.error(e.getMessage());
				}
				final FileWriter fstream;
				BufferedWriter fbw = null;
				try
				{
					fstream = new FileWriter(configurationService.getConfiguration().getString(OUTPUT_FILE), true);
					fbw = new BufferedWriter(fstream);

				}
				catch (final FileNotFoundException e)
				{
					LOG.error(e.getMessage());
				}
				catch (final IOException e)
				{
					LOG.error(e.getMessage());
				}
				result = doCopy(gZIPInputStream, fbw);
			}
			if (result)
			{
				return true;
			}
		}
		return false;

	}

	public class GenericExtFilter implements FilenameFilter
	{

		private final String ext;

		public GenericExtFilter(final String ext)
		{
			this.ext = ext;
		}

		@Override
		public boolean accept(final File dir, final String name)
		{
			return (name.endsWith(ext));
		}
	}

	public boolean doCopy(final InputStream inputStream, final BufferedWriter fbw)
	{
		int oneByte;
		if (inputStream != null && fbw != null)
		{
			try
			{
				while ((oneByte = inputStream.read()) != -1)
				{
					fbw.write(oneByte);
				}
				fbw.close();
				inputStream.close();
				return true;
			}
			catch (final Exception exc)
			{
				return false;
			}
		}
		return false;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
