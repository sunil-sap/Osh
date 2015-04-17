/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.util;

import de.hybris.platform.acceleratorservices.dataimport.batch.util.SequenceIdParser;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;


public class OshSequenceIdParser extends SequenceIdParser
{
	Logger log = Logger.getLogger(OshSequenceIdParser.class);

	@Override
	public Long getSequenceId(final File file)
	{
		Long result = null;
		Assert.notNull(file);
		final String fileName = file.getName();
		final String part = getParser().parse(fileName, 1);
		if (part != null)
		{
			result = Long.valueOf(part);
		}
		else
		{
			//use the modified time of the file uploaded as the sequence if no sequece id is provided
			result = Long.valueOf(file.lastModified());
			//throw new IllegalArgumentException("missing sequenceId in " + fileName);
		}
		log.info("file name :" + fileName + "    sequenceid :" + result);
		return result;
	}

}
