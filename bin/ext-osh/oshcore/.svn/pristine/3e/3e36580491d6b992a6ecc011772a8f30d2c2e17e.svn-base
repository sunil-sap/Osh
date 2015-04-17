package com.hybris.osh.core.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * service to process the drop-ship order based on the feed received from the POM
 * 
 */
public interface DropshipProcessingService
{
	/**
	 * reads the feed file and extract out the order no's to be processed
	 * 
	 * @return list of order no's
	 */
	public Map<Integer, List<String>> dropshipFeedReader(List<File> poFiles);

	/**
	 * triggers the consignment fulfillment process for drop-ship consignment
	 * 
	 * @param orderNumbers
	 */
	public void dropshipSettlementProcessTrigger(Map<String, String> orderNoAndTrackingIdMap);


	public void getDropshipModifiedOrder(Map<Integer, List<String>> PoStatusData);

	public void populateOrderWithStatusRecord(AbstractOrderModel order, List<String> statusData);


	public boolean retrivePoStatusFiles();

	public List<File> getPoStatusFiles();

}
