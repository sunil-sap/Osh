package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.ReadyToPickupReminderService;


public class ReadyToPickupReminderCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(ReadyToPickupReminderCronJob.class);

	private ReadyToPickupReminderService readyToPickupReminderService;

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{

		final boolean status = getReadyToPickupReminderService().isReadyToPickupOrders();
		if (status)
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}

	/**
	 * @return the readyToPickupReminderService
	 */
	public ReadyToPickupReminderService getReadyToPickupReminderService()
	{
		return readyToPickupReminderService;
	}

	/**
	 * @param readyToPickupReminderService
	 *           the readyToPickupReminderService to set
	 */
	public void setReadyToPickupReminderService(final ReadyToPickupReminderService readyToPickupReminderService)
	{
		this.readyToPickupReminderService = readyToPickupReminderService;
	}

}
