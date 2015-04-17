package com.hybris.osh.core.service;

import de.hybris.platform.cronjob.model.TriggerModel;

import java.io.File;
import java.util.Date;
import java.util.List;



/**
 * Service for Creating the input file for t-log
 * 
 */
public interface TLogFileCreatorService
{
	/**
	 * 
	 * @return true if the file is created successfully
	 */
	public boolean tLogEODFileCreator();

	/**
	 * 
	 * @return true if the file is created successfully
	 */
	public boolean tLogHourlyFileCreator();

	public boolean lastHourOrders(Date triggerDate);

	public boolean sftpTlogFile(File f);

	public String[] getEmailIdForFailedTransaction();

	public Date findTlogTriggerDate(final List<TriggerModel> triggers);
}
