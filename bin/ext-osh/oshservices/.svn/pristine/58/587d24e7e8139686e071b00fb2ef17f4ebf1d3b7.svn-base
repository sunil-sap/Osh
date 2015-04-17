/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.ImpexTransformerTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.services.dataimport.batch.converter.impl.OshImpexConverter;
import com.hybris.osh.utils.OSHCSVReader;


public class OshImpexTransformerTask extends ImpexTransformerTask
{

	private static Logger LOG = Logger.getLogger(OshImpexTransformerTask.class);

	/**
	 * Converts the CSV file to an impex file using the given converter
	 * 
	 * @param header
	 * @param file
	 * @param impexFile
	 * @param converter
	 * @return true, if the file contains at least one converted row
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	@Override
	protected boolean convertFile(final BatchHeader header, final File file, final File impexFile, final ImpexConverter converter)
			throws UnsupportedEncodingException, FileNotFoundException
	{
		boolean result = false;
		OSHCSVReader csvReader = null;
		PrintWriter writer = null;
		PrintWriter errorWriter = null;
		try
		{
			csvReader = createCsvReader(file, converter);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(impexFile), "ISO-8859-1")));
			writer.println(getReplacedHeader(header, converter));
			while (csvReader.readNextLine())
			{
				final Map<Integer, String> row = csvReader.getLine();
				if (converter.filter(row))
				{
					try
					{
						writer.println(converter.convert(row, header.getSequenceId()));
						result = true;
					}
					catch (final IllegalArgumentException exc)
					{
						errorWriter = writeErrorLine(file, csvReader, errorWriter, exc);
					}
				}
			}
		}
		catch (final Exception exc)
		{
			LOG.error(exc.getMessage());
		}
		finally
		{
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(errorWriter);
			closeQuietly(csvReader);
		}
		return result;
	}

	@Override
	public BatchHeader execute(final BatchHeader header) throws UnsupportedEncodingException, FileNotFoundException
	{
		Assert.notNull(header);
		Assert.notNull(header.getFile());
		final File file = header.getFile();
		header.setEncoding("ISO-8859-1");
		final List<ImpexConverter> converters = getConverters(file);
		int position = 1;
		for (final ImpexConverter converter : converters)
		{
			final File impexFile = getImpexFile(file, position++);
			if (convertFile(header, file, impexFile, converter))
			{
				header.addTransformedFile(impexFile);
			}
			else
			{
				getCleanupHelper().cleanupFile(impexFile);
			}
		}
		return header;
	}

	/**
	 * @param file
	 * @param converter
	 * 
	 */
	private OSHCSVReader createCsvReader(final File file, final ImpexConverter converter) throws UnsupportedEncodingException,
			FileNotFoundException
	{
		int linesToSkip = getLinesToSkip();
		char[] fieldSeparator = new char[]
		{ getFieldSeparator() };

		if (converter instanceof OshImpexConverter)
		{
			linesToSkip = ((OshImpexConverter) converter).getLinesToSkip();
			final String fieldSeperator = ((OshImpexConverter) converter).getFieldSeperator();
			//default is \t
			fieldSeparator = StringUtils.isEmpty(fieldSeperator) ? "\t".toCharArray() : fieldSeperator.toCharArray();
		}

		final OSHCSVReader csvReader = new OSHCSVReader(file, getEncoding());
		csvReader.setLinesToSkip(linesToSkip);
		csvReader.setFieldSeparator(fieldSeparator);
		csvReader.setTextSeparator('^');
		return csvReader;
	}


	protected PrintWriter writeErrorLine(final File file, final OSHCSVReader csvReader, final PrintWriter errorWriter,
			final IllegalArgumentException exc) throws UnsupportedEncodingException, FileNotFoundException
	{
		PrintWriter result = errorWriter;
		if (result == null)
		{
			result = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getErrorFile(file)),
					getEncoding())));
		}
		result.print(exc.getMessage());
		result.print(": ");
		result.println(csvReader.getSourceLine());
		return result;
	}

	protected void closeQuietly(final OSHCSVReader csvReader)
	{
		if (csvReader != null)
		{
			try
			{
				csvReader.close();
			}
			catch (final IOException e)
			{
				LOG.warn("Could not close csvReader" + e);
			}
		}
	}

}
