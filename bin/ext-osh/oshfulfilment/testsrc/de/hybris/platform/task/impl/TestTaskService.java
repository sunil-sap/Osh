/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.task.impl;

import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


public class TestTaskService implements TaskService, BeanFactoryAware
{

	static private class ScheduledTask
	{

		private final TaskModel task;

		public ScheduledTask(final TaskModel task)
		{
			this.task = task;
		}

		public TaskModel getTask()
		{
			return task;
		}

		public boolean isReady()
		{
			for (final TaskConditionModel condition : task.getConditions())
			{
				if (condition.getFulfilled() == Boolean.FALSE)
				{
					return false;
				}
			}
			return true;
		}

		public void eventTriggered(final String event)
		{
			for (final TaskConditionModel condition : task.getConditions())
			{
				if (event.equals(condition.getUniqueID()))
				{
					condition.setFulfilled(Boolean.TRUE);
				}
			}
		}
	}

	private final List<ScheduledTask> tasks = new ArrayList<ScheduledTask>();
	private BeanFactory beanFactory;

	@Override
	public TaskEngine getEngine()
	{
		return null;
	}

	@Override
	public void scheduleTask(final TaskModel task)
	{
		tasks.add(new ScheduledTask(task));
	}

	@Override
	public void triggerEvent(final String event)
	{
		for (final ScheduledTask task : tasks)
		{
			task.eventTriggered(event);
		}
	}
	
	@Override
	public void triggerEvent(final String event, final Date expirationDate)
	{
		triggerEvent(event);
	}

	public List<TaskModel> executeReadyTasks() throws Exception //NOPMD
	{
		// Copy list of scheduled tasks because when a task is executed
		// it triggers new task that will be added to the list but should
		// not be considered during this run
		final Iterator<ScheduledTask> itr = new ArrayList(tasks).iterator();
		final List<TaskModel> executedTasks = new ArrayList<TaskModel>();
		while (itr.hasNext())
		{
			final ScheduledTask scheduledTask = itr.next();
			if (scheduledTask.isReady())
			{
				final TaskModel task = scheduledTask.getTask();
				final String runnerBeanId = task.getRunnerBean();
				final TaskRunner taskRunner = (TaskRunner) beanFactory.getBean(runnerBeanId);
				taskRunner.run(this, task);
				itr.remove();
				executedTasks.add(task);
			}
		}
		return executedTasks;
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		this.beanFactory = beanFactory;
	}


}
