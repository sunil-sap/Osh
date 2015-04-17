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
package com.hybris.osh.oshcscockpit.widgets.renderer.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CancellationController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.PartialOrderCancellationWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.edit.BasicPropertyDescriptor;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;


public class OshPartialOrderCancellationWidgetRenderer extends PartialOrderCancellationWidgetRenderer
{
	@Resource(name = "modelService")
	ModelService modelService;

	private List orderCancelEntryProperties;

	OshPartialOrderCancellationWidgetRenderer()
	{

	}

	@Override
	protected List getOrderCancelEntryProperties()
	{
		if (orderCancelEntryProperties == null)
		{
			orderCancelEntryProperties = new ArrayList();
			orderCancelEntryProperties.add(new BasicPropertyDescriptor("OrderCancelEntry.cancelQuantity", "LONG"));
			final BasicPropertyDescriptor spdReason = new BasicPropertyDescriptor("OrderCancelEntry.cancelReason", "ENUM");
			spdReason.setAvailableValues(Arrays.asList(getAllCancelReasons().toArray()));
			orderCancelEntryProperties.add(spdReason);
			//orderCancelEntryProperties.add(new BasicPropertyDescriptor("OshOrderCancelEntry.shippingAmt", "DECIMAL"));
			orderCancelEntryProperties.add(new BasicPropertyDescriptor("OrderCancelEntry.notes", "TEXT"));
		}
		return orderCancelEntryProperties;
	}

	@Override
	protected void handleAttemptCancellationEvent(final InputWidget widget, final Event event,
			final ObjectValueContainer orderCancelRequestOVC, final List orderEntryCancelRecordEntries) throws Exception
	{
		final OrderModel orderModel = (OrderModel) ((CancellationController) widget.getWidgetController()).getOrder().getObject();
		if ("onOK".equals(event.getName()))
		{
			try
			{
				final TypedObject cancellationRequest = ((CancellationController) widget.getWidgetController())
						.createPartialOrderCancellationRequest(orderEntryCancelRecordEntries, orderCancelRequestOVC);
				if (cancellationRequest != null)
				{
					getPopupWidgetHelper().dismissCurrentPopup();
					final OrderCancelRecordEntryModel orderCancelRecordEntryModel = (OrderCancelRecordEntryModel) cancellationRequest
							.getObject();
					Messagebox.show(LabelUtils.getLabel(widget, "cancellationNumber", new Object[]
					{ orderCancelRecordEntryModel.getCode() }), LabelUtils.getLabel(widget, "cancellationNumberTitle", new Object[0]),
							1, "z-msgbox z-msgbox-imformation");
					while (!orderModel.isTransactionComplete())
					{
						modelService.refresh(orderModel);
						try
						{
							Thread.sleep(15000);
						}
						catch (final InterruptedException e)
						{
							e.printStackTrace();
						}

					}
					orderModel.setTransactionComplete(false);
					modelService.save(orderModel);
					modelService.refresh(orderModel);
					((CancellationController) widget.getWidgetController()).dispatchEvent(widget.getControllerCtx(), widget, null);
					Executions.sendRedirect("");

				}
				else
				{
					Messagebox.show(LabelUtils.getLabel(widget, "errorCreatingRequest", new Object[0]),
							LabelUtils.getLabel(widget, "failed", new Object[0]), 1, "z-msgbox z-msgbox-error");
				}
			}
			catch (final OrderCancelException e)
			{
				Messagebox
						.show((new StringBuilder(String.valueOf(e.getMessage()))).append(
								e.getCause() != null ? (new StringBuilder(" - ")).append(e.getCause().getMessage()).toString() : "")
								.toString(), LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
			}
			catch (final ValidationException e)
			{
				Messagebox
						.show((new StringBuilder(String.valueOf(e.getMessage()))).append(
								e.getCause() != null ? (new StringBuilder(" - ")).append(e.getCause().getMessage()).toString() : "")
								.toString(), LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
			}
		}
	}



}
