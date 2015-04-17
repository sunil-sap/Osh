package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.impl.DefaultSFTPPriceCheckService;
import com.hybris.osh.core.service.impl.DefaultSFTPProductCheckService;




/**
 * @author ubuntu
 * 
 */
public class CheckFeedFileExistance extends AbstractJobPerformable<CronJobModel>
{
	private DefaultSFTPPriceCheckService sftpPriceCheckService;
	private DefaultSFTPProductCheckService sftpProductCheckService;

	@Resource
	private CronJobService cronJobService;
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(CheckFeedFileExistance.class);



	private static final String REQUIRED_FEED_JOB_KEY = "cronjob.required.feed.job";


	public static String REQUIRED_FEED_JOB = null;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}



	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}



	/**
	 * @return the sftpPriceCheckService
	 */
	public DefaultSFTPPriceCheckService getSftpPriceCheckService()
	{
		return sftpPriceCheckService;
	}



	/**
	 * @param sftpPriceCheckService
	 *           the sftpPriceCheckService to set
	 */
	public void setSftpPriceCheckService(final DefaultSFTPPriceCheckService sftpPriceCheckService)
	{
		this.sftpPriceCheckService = sftpPriceCheckService;
	}



	/**
	 * @return the sftpProductCheckService
	 */
	public DefaultSFTPProductCheckService getSftpProductCheckService()
	{
		return sftpProductCheckService;
	}



	/**
	 * @param sftpProductCheckService
	 *           the sftpProductCheckService to set
	 */
	public void setSftpProductCheckService(final DefaultSFTPProductCheckService sftpProductCheckService)
	{
		this.sftpProductCheckService = sftpProductCheckService;
	}




	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		final boolean isProductFiles = sftpProductCheckService.isProductFilesPresent();
		final boolean isPriceFiles = sftpPriceCheckService.isPriceFilesPresent();

		try
		{
			setConfiguration();

			if (isProductFiles && isPriceFiles)
			{
				cronJobService.performCronJob(cronJobService.getCronJob(REQUIRED_FEED_JOB));
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}


	}

	public void setConfiguration()
	{
		REQUIRED_FEED_JOB = configurationService.getConfiguration().getString(REQUIRED_FEED_JOB_KEY);
	}
}
