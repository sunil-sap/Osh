/**
 * 
 */
package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.hybris.osh.core.flexisearch.OshFlexiSearchUtil;
import com.hybris.osh.core.populators.loyalty.LoyaltyOrderExportPopulator;


public class LoyaltyOrderDataExportJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(LoyaltyOrderDataExportJobPerformable.class.getName());
	private OshFlexiSearchUtil oshFlexiSearchUtil;
	private LoyaltyOrderExportPopulator loyaltyOrderExportPopulator;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.info("perform");
		try
		{
			loyaltyOrderExportPopulator.populateOrderResults(oshFlexiSearchUtil.retrieveOrders());
		}
		catch (final JAXBException e)
		{
			e.printStackTrace();
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}



	/**
	 * @return the oshFlexiSearchUtil
	 */
	public OshFlexiSearchUtil getOshFlexiSearchUtil()
	{
		return oshFlexiSearchUtil;
	}



	/**
	 * @param oshFlexiSearchUtil
	 *           the oshFlexiSearchUtil to set
	 */
	public void setOshFlexiSearchUtil(final OshFlexiSearchUtil oshFlexiSearchUtil)
	{
		this.oshFlexiSearchUtil = oshFlexiSearchUtil;
	}



	/**
	 * @return the loyaltyOrderExportPopulator
	 */
	public LoyaltyOrderExportPopulator getLoyaltyOrderExportPopulator()
	{
		return loyaltyOrderExportPopulator;
	}

	/**
	 * @param loyaltyOrderExportPopulator
	 *           the loyaltyOrderExportPopulator to set
	 */
	public void setLoyaltyOrderExportPopulator(final LoyaltyOrderExportPopulator loyaltyOrderExportPopulator)
	{
		this.loyaltyOrderExportPopulator = loyaltyOrderExportPopulator;
	}





}
