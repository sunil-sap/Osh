/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;


/**
 * This class extends the default behavior to handle an empty impex file, where none of the input rows are converted
 * into impex.
 * 
 */
public abstract class OshAbstractImpexRunnerTask extends AbstractImpexRunnerTask
{

	Logger log = Logger.getLogger(OshAbstractImpexRunnerTask.class);

	@Override
	public BatchHeader execute(final BatchHeader header) throws FileNotFoundException
	{
		if (header.getTransformedFiles() == null)
		{
			log.error("empty impex file generated for :" + header.getFile().getName()
					+ ". Please check error folder for the error file generated.");
			return header;
		}
		return super.execute(header);
	}
}
