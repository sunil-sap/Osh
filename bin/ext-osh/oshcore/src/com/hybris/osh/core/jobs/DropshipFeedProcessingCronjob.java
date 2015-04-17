package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.util.List;

import com.hybris.osh.core.logger.OshIntegrationLogger;
import com.hybris.osh.core.service.DropshipProcessingService;


/**
 * Cron-job that reads the feed from dropship and do the settlement for all the orders present in the feed
 * 
 */
public class DropshipFeedProcessingCronjob extends AbstractJobPerformable<CronJobModel>
{
	private DropshipProcessingService dropshipProcessingService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		if (getDropshipProcessingService().retrivePoStatusFiles())
		{
			try
			{
				final List<File> pofiles = getDropshipProcessingService().getPoStatusFiles();
				if (!pofiles.isEmpty())
				{
					getDropshipProcessingService()
							.getDropshipModifiedOrder(getDropshipProcessingService().dropshipFeedReader(pofiles));
				}
			}
			catch (final Exception e)
			{
				OshIntegrationLogger.error(e.getMessage());
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public DropshipProcessingService getDropshipProcessingService()
	{
		return dropshipProcessingService;
	}

	public void setDropshipProcessingService(final DropshipProcessingService dropshipProcessingService)
	{
		this.dropshipProcessingService = dropshipProcessingService;
	}
}
