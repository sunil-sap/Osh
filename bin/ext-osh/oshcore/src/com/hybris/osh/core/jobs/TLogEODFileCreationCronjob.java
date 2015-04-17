package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import com.hybris.osh.core.service.TLogFileCreatorService;


/**
 * Job to send the t-log request file.
 * 
 */
public class TLogEODFileCreationCronjob extends AbstractJobPerformable<CronJobModel>
{
	private TLogFileCreatorService tLogFileCreatorService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		gettLogFileCreatorService().tLogEODFileCreator();
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public TLogFileCreatorService gettLogFileCreatorService()
	{
		return tLogFileCreatorService;
	}

	public void settLogFileCreatorService(final TLogFileCreatorService tLogFileCreatorService)
	{
		this.tLogFileCreatorService = tLogFileCreatorService;
	}


}
