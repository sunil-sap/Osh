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

import com.hybris.osh.core.service.SFTPPriceExportService;


/**
 *
 *
 */
public class PriceFeedProcessingCronjob extends AbstractJobPerformable<CronJobModel>
{

	private SFTPPriceExportService sftpPriceExportService;
	private static final Logger LOG = Logger.getLogger(PriceFeedProcessingCronjob.class);

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
		if (sftpPriceExportService.isPriceFileExported())
		{
			final boolean archiveStatus = sftpPriceExportService.getArchiveStatus();
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

	public SFTPPriceExportService getSftpPriceExportService()
	{
		return sftpPriceExportService;
	}

	public void setSftpPriceExportService(final SFTPPriceExportService sftpPriceExportService)
	{
		this.sftpPriceExportService = sftpPriceExportService;
	}



}
