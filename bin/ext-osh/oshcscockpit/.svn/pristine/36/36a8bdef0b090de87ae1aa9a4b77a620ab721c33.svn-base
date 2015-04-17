/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.oshcscockpit.widget.controller;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;

import java.util.Map;


public interface OrderUpdateController extends WidgetController
{
	public abstract boolean canUpdate();

	public abstract Map getUpdatableOrderEntries();

	public abstract TypedObject getCurrentOrder();

	public abstract TypedObject createUpdateRequest();

	public abstract ObjectValueHolder getPropertyValue(ObjectValueContainer ovc, String propertyQualifier);

}
