/**
 * 
 */
package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import com.hybris.osh.core.service.CheetahMailSubscriberService;


/**
 * This class is used to export the customer details for cheetah mail
 */
public class CheetahMailDataExportCronjob extends AbstractJobPerformable<CronJobModel>
{

	private CheetahMailSubscriberService cheetahMailSubscriberService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		getCheetahMailSubscriberService().getSubscribedUser();
		getCheetahMailSubscriberService().getUnSubscribedUser();

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @return the cheetahMailSubscriberService
	 */
	public CheetahMailSubscriberService getCheetahMailSubscriberService()
	{
		return cheetahMailSubscriberService;
	}

	/**
	 * @param cheetahMailSubscriberService
	 *           the cheetahMailSubscriberService to set
	 */
	public void setCheetahMailSubscriberService(final CheetahMailSubscriberService cheetahMailSubscriberService)
	{
		this.cheetahMailSubscriberService = cheetahMailSubscriberService;
	}

}
