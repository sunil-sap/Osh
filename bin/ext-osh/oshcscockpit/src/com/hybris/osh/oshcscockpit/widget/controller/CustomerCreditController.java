package com.hybris.osh.oshcscockpit.widget.controller;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;

import java.util.Map;


public interface CustomerCreditController extends WidgetController
{
	public abstract boolean canUpdate();

	public abstract Map getUpdatableOrderEntries();

	public abstract TypedObject getCurrentOrder();

	public abstract TypedObject createUpdateRequest();

	public abstract ObjectValueHolder getPropertyValue(ObjectValueContainer ovc, String propertyQualifier);

}
