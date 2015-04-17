package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;
import java.util.List;

import com.hybris.osh.core.logger.OshIntegrationLogger;
import com.hybris.osh.core.service.TLogFileCreatorService;


/**
 * Job to send the t-log file on hourly basis.
 * 
 */
public class TLogHourlyBasedFileCreatorCronjob extends AbstractJobPerformable<CronJobModel>
{
	private TLogFileCreatorService tLogFileCreatorService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		Date triggerDate = null;
		final List<TriggerModel> triggers = cronJob.getTriggers();

		triggerDate = gettLogFileCreatorService().findTlogTriggerDate(triggers);
		OshIntegrationLogger.error("Tlog Transaction amount will based on the date: " + triggerDate);
		gettLogFileCreatorService().lastHourOrders(triggerDate);
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
