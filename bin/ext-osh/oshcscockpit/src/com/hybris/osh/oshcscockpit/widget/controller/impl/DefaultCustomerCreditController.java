package com.hybris.osh.oshcscockpit.widget.controller.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.cscockpit.widgets.events.ReturnsEvent;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hybris.osh.oshcscockpit.update.UpdateService;
import com.hybris.osh.oshcscockpit.widget.controller.OshOrderManagementActionsWidgetController;


public class DefaultCustomerCreditController extends AbstractCsWidgetController
{
	private UpdateService updateService;

	private ReturnService returnService;

	protected static class UpdateOrderStatusDetails
	{
		private final long expectedQuantity;
		private final String notes;

		public long getExpectedQuantity()
		{
			return expectedQuantity;
		}

		public String getNotes()
		{
			return notes;
		}

		public UpdateOrderStatusDetails(final long expectedQuantity, final String notes)
		{
			this.expectedQuantity = expectedQuantity;
			this.notes = notes;
		}

	}




	public DefaultCustomerCreditController()
	{

	}


	private OshOrderManagementActionsWidgetController oshOrderManagementActionsWidgetController;

	private UserService userService;

	protected OshOrderManagementActionsWidgetController getOrderManagementActionsWidgetController()
	{
		return oshOrderManagementActionsWidgetController;
	}

	public void setOrderManagementActionsWidgetController(
			final OshOrderManagementActionsWidgetController oshOrderManagementActionsWidgetController)
	{
		this.oshOrderManagementActionsWidgetController = oshOrderManagementActionsWidgetController;
	}

	@Override
	public void dispatchEvent(final String context, final Object source, final Map data)
	{
		dispatchEvent("csCtx", (new ReturnsEvent(source, context)));
		getOrderManagementActionsWidgetController().dispatchEvent(null, source, data);
	}


	protected OrderModel getOrderModel()
	{
		final TypedObject orderObject = getCurrentOrder();
		if (orderObject != null)
		{
			return (OrderModel) orderObject.getObject();
		}
		else
		{
			return null;
		}
	}


	public TypedObject getCurrentOrder()
	{
		return oshOrderManagementActionsWidgetController.getOrder();
	}

	public TypedObject createUpdateRequest()
	{

		return null;
	}

	public Map getUpdatableOrderEntries()
	{
		final OrderModel orderModel = getOrderModel();
		final Map updatableEntries = new HashMap<OrderModel, String>();
		if (orderModel != null)
		{

			final AbstractOrderModel abstractOrderModel = orderModel;
			updatableEntries.put(abstractOrderModel, abstractOrderModel.getCode());


			final Map result = new HashMap();
			java.util.Map.Entry entry;
			for (final Iterator iterator = updatableEntries.entrySet().iterator(); iterator.hasNext(); result.put(
					getCockpitTypeService().wrapItem(entry.getKey()), entry.getValue()))
			{
				entry = (java.util.Map.Entry) iterator.next();
			}
			return result;
		}
		return null;
	}

	public UpdateService getUpdateService()
	{
		return updateService;
	}


	public void setUpdateService(final UpdateService updateService)
	{
		this.updateService = updateService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public boolean canUpdate()
	{
		final Map updatableOrderEntries = getUpdatableOrderEntries();
		return updatableOrderEntries != null && !updatableOrderEntries.isEmpty();
	}

	public ReturnService getReturnService()
	{
		return returnService;
	}

	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	public ObjectValueHolder getPropertyValue(final ObjectValueContainer ovc, final String propertyQualifier)
	{
		final PropertyDescriptor pdReason = getPropertyDescriptor(ovc.getPropertyDescriptors(), propertyQualifier);
		if (pdReason != null)
		{
			return ovc.getValue(pdReason, null);
		}
		else
		{
			return null;
		}
	}

	protected PropertyDescriptor getPropertyDescriptor(final Set properties, final String qualifier)
	{
		if (properties != null)
		{
			for (final Iterator iterator = properties.iterator(); iterator.hasNext();)
			{
				final PropertyDescriptor pd = (PropertyDescriptor) iterator.next();
				if (pd.getQualifier().equalsIgnoreCase(qualifier))
				{
					return pd;
				}
			}

		}
		return null;
	}


}
