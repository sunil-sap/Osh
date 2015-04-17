/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.CartRemovalService;


/**
 * Cronjob to delete the Cart after every 365 days
 */
public class CartRemovalCronjob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(CartRemovalCronjob.class);

	private CartRemovalService cartRemovalService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */

	@Override
	public PerformResult perform(final CronJobModel model)
	{
		boolean status;
		status = getCartRemovalService().getAbandonedCarts();
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
	 * @return the cartRemovalService
	 */
	public CartRemovalService getCartRemovalService()
	{
		return cartRemovalService;
	}

	/**
	 * @param cartRemovalService
	 *           the cartRemovalService to set
	 */
	public void setCartRemovalService(final CartRemovalService cartRemovalService)
	{
		this.cartRemovalService = cartRemovalService;
	}
}