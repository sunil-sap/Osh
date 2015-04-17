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


public class CheckProductExistanceCronjob extends AbstractJobPerformable<CronJobModel>
{

	private CreateMediaImpexService createMediaImpexService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		if (createMediaImpexService.checkProductExistance())
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}

	/**
	 * @return the createMediaImpexService
	 */
	public CreateMediaImpexService getCreateMediaImpexService()
	{
		return createMediaImpexService;
	}

	/**
	 * @param createMediaImpexService
	 *           the createMediaImpexService to set
	 */
	public void setCreateMediaImpexService(final CreateMediaImpexService createMediaImpexService)
	{
		this.createMediaImpexService = createMediaImpexService;
	}
}
