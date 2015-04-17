/**
 * 
 */
package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import com.hybris.osh.core.service.CreateMediaImpexService;


/**
 * To remove original image from media container after completion of image magick
 */
public class RemoveOriginalImageCronJob extends AbstractJobPerformable<CronJobModel>
{

	private CreateMediaImpexService createMediaImpexService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		if (getCreateMediaImpexService().removeOriginalImages())
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

	}


	public CreateMediaImpexService getCreateMediaImpexService()
	{
		return createMediaImpexService;
	}


	public void setCreateMediaImpexService(final CreateMediaImpexService createMediaImpexService)
	{
		this.createMediaImpexService = createMediaImpexService;
	}

}
