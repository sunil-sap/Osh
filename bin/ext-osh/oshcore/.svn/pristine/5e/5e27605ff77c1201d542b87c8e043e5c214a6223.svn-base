/**
 * 
 */
package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.SFTPStoreExportService;


/**
 * 
 *
 */
public class StoreInventoryCronjob extends AbstractJobPerformable<CronJobModel>
{
	private SFTPStoreExportService sftpStoreExportService;
	private final Logger LOG = Logger.getLogger(StoreInventoryCronjob.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		if (sftpStoreExportService.isStoreFileExported())
		{
			final boolean archiveStatus = sftpStoreExportService.getArchiveStatus();
			if (archiveStatus)
			{
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
			else
			{
				LOG.info("Fail to archive the files");
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			}

		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}

	/**
	 * @return the sftpStoreExportService
	 */
	public SFTPStoreExportService getSftpStoreExportService()
	{
		return sftpStoreExportService;
	}


	/**
	 * @param sftpStoreExportService
	 *           the sftpStoreExportService to set
	 */
	public void setSftpStoreExportService(final SFTPStoreExportService sftpStoreExportService)
	{
		this.sftpStoreExportService = sftpStoreExportService;
	}


}
