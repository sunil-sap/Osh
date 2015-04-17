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
package com.hybris.osh.oshcscockpit.widgets.renderers.details.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import com.hybris.osh.events.ReturnOrderConfirmationEvent;


public class OshOrderReturnRequestListWidgetDetailRenderer extends OshAbstractConfigurableAutoTypeListboxWidgetDetailRenderer
{
	@Resource(name = "modelService")
	private ModelService modelService;

	public OshOrderReturnRequestListWidgetDetailRenderer()
	{
	}

	protected class OrderRefundConfirmedEventListener implements EventListener
	{
		private final transient Widget widget;
		private final TypedObject item;

		//final RefundConfirmationWidgetRenderer this$0;

		@Override
		public void onEvent(final Event event) throws Exception
		{
			handleRefundConfirmedEvent(widget, item);
		}

		public OrderRefundConfirmedEventListener(final Widget widget, final TypedObject item)
		{

			super();
			this.widget = widget;
			this.item = item;
		}
	}

	public HtmlBasedComponent createContent(final Object context, final TypedObject item, final Widget widget)
	{
		int count = 0;
		final Div detailContainer = new Div();
		returnObjectValueContainers = new ArrayList();
		final List returnEntries = ((ReturnRequestModel) item.getObject()).getReturnEntries();
		if (returnEntries != null && !returnEntries.isEmpty())
		{
			for (final Object object : returnEntries)
			{
				if (((RefundEntryModel) object).isRefundDone())
				{
					count++;
				}
			}
			createContentList(detailContainer, context, returnEntries, "csOrderReturnRequests", widget);
		}
		final Div refundContainer = new Div();
		refundContainer.setParent(detailContainer);
		final Button refundButton = new Button(LabelUtils.getLabel(widget, "refundOrder", new Object[0]));
		refundButton.setParent(refundContainer);
		refundButton.addEventListener("onClick", orderRefundConfirmation(widget, item));
		if (returnEntries != null && count == returnEntries.size())
		{
			refundButton.setDisabled(true);
		}
		return detailContainer;
	}

	public EventListener orderRefundConfirmation(final Widget widget, final TypedObject item)
	{
		return new OrderRefundConfirmedEventListener(widget, item);
	}

	protected void handleRefundConfirmedEvent(final Widget widget, final TypedObject item)
	{
		final ReturnRequestModel returnRequestModel = (ReturnRequestModel) item.getObject();
		eventService.publishEvent(new ReturnOrderConfirmationEvent(returnRequestModel.getOrder()));
		try
		{
			Thread.sleep(20000);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			((DefaultOrderController) widget.getWidgetController()).dispatchEvent(null, widget, null);
			((DefaultMasterDetailListWidgetModel) widget.getWidgetModel()).notifyListeners();
		}


	}

	@Override
	public HtmlBasedComponent createContent(final Object obj, final Object obj1, final Widget widget)
	{
		return createContent(obj, (TypedObject) obj1, widget);
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	//private ModelService modelService;
	protected static final String CSS_ORDER_RETURN_REQUESTS = "csOrderReturnRequests";
	private EventService eventService;
}
