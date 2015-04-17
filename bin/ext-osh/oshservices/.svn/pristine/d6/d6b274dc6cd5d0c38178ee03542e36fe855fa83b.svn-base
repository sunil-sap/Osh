/**
 * 
 */
package com.hybris.osh.services.dataimport.batch;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


public class OshFileOrderComparator implements Comparator<File>, InitializingBean
{
	private static final Integer DEFAULT_PRIORITY = NumberUtils.INTEGER_ZERO;
	private Map<String, Integer> fileNamePriority;
	private String filePriority;
	private String delimiter;

	@Override
	public void afterPropertiesSet()
	{
		Assert.notNull(filePriority);
		Assert.notNull(delimiter);
		fileNamePriority = new HashMap<String, Integer>();
		int i = 0;
		for (final String fileName : Arrays.asList(filePriority.split("\\|")))
		{
			fileNamePriority.put(fileName, Integer.valueOf(i++));
		}
	}

	@Override
	public int compare(final File file, final File otherFile)
	{
		// invert priority setting so files with higher priority go first
		int result = getPriority(otherFile).compareTo(getPriority(file));
		if (result == 0)
		{
			result = Long.valueOf(file.lastModified()).compareTo(Long.valueOf(otherFile.lastModified()));
		}
		return result;
	}

	/**
	 * Retrieves the priority for a file.
	 * 
	 * @param file
	 *           the file to get priority from
	 * @return the configured priority, if one exists, otherwise the default priority
	 */
	protected Integer getPriority(final File file)
	{
		final String fileName = file.getName();
		return fileNamePriority.containsKey(fileName) ? fileNamePriority.get(fileName) : DEFAULT_PRIORITY;
	}

	/**
	 * @return the filePriority
	 */
	public String getFilePriority()
	{
		return filePriority;
	}

	/**
	 * @param filePriority
	 *           the filePriority to set
	 */
	public void setFilePriority(final String filePriority)
	{
		this.filePriority = filePriority;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter()
	{
		return delimiter;
	}

	/**
	 * @param delimiter
	 *           the delimiter to set
	 */
	public void setDelimiter(final String delimiter)
	{
		this.delimiter = delimiter;
	}

}
