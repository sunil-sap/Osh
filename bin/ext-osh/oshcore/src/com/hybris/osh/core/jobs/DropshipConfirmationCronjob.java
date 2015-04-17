package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;

import com.hybris.osh.core.service.DropshipFileCreatorService;


/**
 * Job to send the drop ship confirmation file.
 * 
 */
public class DropshipConfirmationCronjob extends AbstractJobPerformable<CronJobModel>
{
	private DropshipFileCreatorService dropshipFileCreatorService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		final Date triggerDate = getDropshipFileCreatorService().findPOTriggerDate(arg0.getTriggers());
		getDropshipFileCreatorService().createDropshipConfirmationFile(triggerDate);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public DropshipFileCreatorService getDropshipFileCreatorService()
	{
		return dropshipFileCreatorService;
	}

	public void setDropshipFileCreatorService(final DropshipFileCreatorService dropshipFileCreatorService)
	{
		this.dropshipFileCreatorService = dropshipFileCreatorService;
	}

}
