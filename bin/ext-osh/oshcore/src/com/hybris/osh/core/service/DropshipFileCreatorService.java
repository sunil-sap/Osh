package com.hybris.osh.core.service;

import de.hybris.platform.cronjob.model.TriggerModel;

import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * Service for Creating the input file for Drop-ship
 * 
 */
public interface DropshipFileCreatorService
{
	/**
	 * creates the file containing the list of order entries to be sent to vendor for drop-ship
	 * 
	 * @return true if file is created successfully
	 */
	public boolean createDropshipInputFile(Date triggerDate);

	/**
	 * creates the file containing the list of order entries that are successfully shipped as drop-ship
	 * 
	 * @return true if file is created successfully
	 */
	public boolean createDropshipConfirmationFile(Date triggerDate);


	public boolean sftpPoFile(final File poFile);

	public Date findPOTriggerDate(final List<TriggerModel> triggers);

}
