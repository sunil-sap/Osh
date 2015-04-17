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


public class RemoveArchiveFoldersFromSftpCronjob extends AbstractJobPerformable<CronJobModel>
{
	private CreateMediaImpexService createMediaImpexService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		if (createMediaImpexService.removeArchiveFoldersFromSftp())
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
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
