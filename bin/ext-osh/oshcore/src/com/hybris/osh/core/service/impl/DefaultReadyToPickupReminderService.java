package com.hybris.osh.core.service.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.hybris.osh.core.dao.ReadyToPickupReminderDao;
import com.hybris.osh.core.service.ReadyToPickupReminderService;
import com.hybris.osh.events.ReadyToPickUpEvent;


public class DefaultReadyToPickupReminderService implements ReadyToPickupReminderService
{
	private ReadyToPickupReminderDao readyToPickupReminderDao;
	private EventService eventService;
	private ConfigurationService configurationService;
	private ModelService modelService;
	protected static final Logger LOG = Logger.getLogger(DefaultReadyToPickupReminderService.class);


	@Override
	public boolean isReadyToPickupOrders()
	{
		final Configuration config = configurationService.getConfiguration();
		final Integer interval = Integer.valueOf(config.getString("ready.to.pickup.mail.interval"));
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -interval.intValue());
		final List<ConsignmentModel> orders = readyToPickupReminderDao.findReadyToPickupOrders(calendar.getTime());
		return getConsignmentEntryForReminder(orders);
	}

	@Override
	public boolean getConsignmentEntryForReminder(final List<ConsignmentModel> consignments)
	{
		final Configuration config = configurationService.getConfiguration();
		final Integer interval = Integer.valueOf(config.getString("ready.to.pickup.mail.interval"));
		final List<Integer> toList = getReminderIntervals();
		final DateTime todaysDate = new DateTime();
		final DateTime startDate = todaysDate.plus(Days.days(interval));
		boolean status = false;
		if (toList != null)
		{
			for (final ConsignmentModel ConsignmentModel : consignments)
			{

				final Set<ConsignmentEntryModel> consignmentEntries = ConsignmentModel.getConsignmentEntries();
				for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntries)
				{

					final Date date = consignmentEntryModel.getCreationtime();
					final Integer days = Days.daysBetween(new DateTime(date), startDate).getDays();
					if (toList.contains(days))
					{
						status = true;
						consignmentEntryModel.setNotificationSend(days / interval);
						getModelService().save(consignmentEntryModel);
						getModelService().refresh(consignmentEntryModel);
					}
				}
				if (status)
				{
					eventService.publishEvent(new ReadyToPickUpEvent(ConsignmentModel));
				}

			}
			return status;
		}
		else
		{
			LOG.info("Intervals Or LastDays for Ready For Pickup Reminder Order Is Not Definded");
		}
		return status;
	}

	public List<Integer> getReminderIntervals()
	{
		final Configuration config = configurationService.getConfiguration();
		final Integer interval = Integer.valueOf(config.getString("ready.to.pickup.mail.interval"));
		final Integer lastday = Integer.valueOf(config.getString("ready.to.pickup.reminder.lastday"));
		if (lastday != null && interval != null)
		{
			int intervalVal = 0;
			final List<Integer> intervalList = new ArrayList<Integer>();
			final int limit = Integer.valueOf(lastday) / Integer.valueOf(interval);
			for (int i = 1; i <= limit; i++)
			{
				intervalVal = intervalVal + interval;
				intervalList.add(intervalVal);
			}
			return intervalList;
		}
		return null;
	}


	/**
	 * @return the readyToPickupReminderDao
	 */
	public ReadyToPickupReminderDao getReadyToPickupReminderDao()
	{
		return readyToPickupReminderDao;
	}

	/**
	 * @param readyToPickupReminderDao
	 *           the readyToPickupReminderDao to set
	 */
	public void setReadyToPickupReminderDao(final ReadyToPickupReminderDao readyToPickupReminderDao)
	{
		this.readyToPickupReminderDao = readyToPickupReminderDao;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

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
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
