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
package com.hybris.osh.jalo;

import com.hybris.osh.constants.OshfulfilmentConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


public class OshfulfilmentManager extends GeneratedOshfulfilmentManager
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OshfulfilmentManager.class.getName());

	public static final OshfulfilmentManager getInstance()
	{
		final ExtensionManager manager = JaloSession.getCurrentSession().getExtensionManager();
		return (OshfulfilmentManager) manager.getExtension(OshfulfilmentConstants.EXTENSIONNAME);
	}

}
