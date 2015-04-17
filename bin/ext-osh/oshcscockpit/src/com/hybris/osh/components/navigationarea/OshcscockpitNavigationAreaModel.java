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
package com.hybris.osh.components.navigationarea;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;

import com.hybris.osh.session.impl.OshcscockpitNavigationArea;


/**
 * Oshcscockpit navigation area model.
 */
public class OshcscockpitNavigationAreaModel extends DefaultNavigationAreaModel
{
	public OshcscockpitNavigationAreaModel()
	{
		super();
	}

	public OshcscockpitNavigationAreaModel(final AbstractUINavigationArea area)
	{
		super(area);
	}

	@Override
	public OshcscockpitNavigationArea getNavigationArea()
	{
		return (OshcscockpitNavigationArea) super.getNavigationArea();
	}
}
