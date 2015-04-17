/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.services.dataimport.batch.task;



import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.CleanupHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Cleanup for the impex import process. Extends the default cleanup helper. To use custom error and archive folders
 * directory.
 */
public class OshCleanupHelper extends CleanupHelper
{
	private static final String DATE_SEPARATOR = "_";

	private static final Logger LOG = Logger.getLogger(OshCleanupHelper.class);
	private String errorFolder;
	private String archiveFolder;

	/**
	 * Removes the transformed file
	 * 
	 * @param header
	 * @param error
	 */
	@Override
	protected void cleanupSourceFile(final BatchHeader header, final boolean error)
	{
		if (header.getFile() != null)
		{
			FileUtils.deleteQuietly(header.getFile());
		}
	}

	/**
	 * Returns the destination location of the file
	 * 
	 * @param file
	 * @param error
	 *           flag indicating if there was an error
	 * @return the destination file
	 */
	@Override
	protected File getDestFile(final File file, final boolean error)
	{
		final StringBuilder builder = new StringBuilder(file.getName());
		if (!StringUtils.isBlank(getTimeStampFormat()))
		{
			final SimpleDateFormat sdf = new SimpleDateFormat(getTimeStampFormat(), Locale.getDefault());
			builder.append(DATE_SEPARATOR);
			builder.append(sdf.format(new Date()));
		}
		return new File(error ? getErrorFolder() : getArchiveFolder(), builder.toString());
	}


	/**
	 * @return the errorFolder
	 */
	public String getErrorFolder()
	{
		return errorFolder;
	}


	/**
	 * @param errorFolder
	 *           the errorFolder to set
	 */
	public void setErrorFolder(final String errorFolder)
	{
		this.errorFolder = errorFolder;
	}


	/**
	 * @return the archiveFolder
	 */
	public String getArchiveFolder()
	{
		return archiveFolder;
	}


	/**
	 * @param archiveFolder
	 *           the archiveFolder to set
	 */
	public void setArchiveFolder(final String archiveFolder)
	{
		this.archiveFolder = archiveFolder;
	}

}
