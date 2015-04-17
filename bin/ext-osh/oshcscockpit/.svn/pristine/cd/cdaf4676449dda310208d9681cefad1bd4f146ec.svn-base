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

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderManagementActionsWidgetRenderer;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;

import com.hybris.osh.oshcscockpit.widget.controller.OshOrderManagementActionsWidgetController;


public class OshOrderManagementActionsWidgetRenderer extends OrderManagementActionsWidgetRenderer
{

	private OshOrderManagementActionsWidgetController oshOrderManagementActionsWidgetController;

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
	protected HtmlBasedComponent createContentInternal(final Widget widget, final HtmlBasedComponent rootContainer)
	{
		final Div component = new Div();
		component.setSclass("orderManagementActionsWidget");
		createButton(widget, component, "cancelWholeOrder", "csFullOrderCancellationWidgetConfig", "csFullOrderCancel-Popup",
				"csFullCancelPopup", "popup.fullCancellationRequestCreate",
				!((OrderManagementActionsWidgetController) widget.getWidgetController()).isFullCancelPossible());
		createButton(widget, component, "cancelPartialOrder", "csPartialOrderCancellationWidgetConfig",
				"csPartialOrderCancellationWidgetConfig-Popup", "csPartialCancelPopup", "popup.partialCancellationRequestCreate",
				!((OrderManagementActionsWidgetController) widget.getWidgetController()).isPartialCancelPossible());
		createButton(widget, component, "refundOrder", "csRefundRequestCreateWidgetConfig", "csRefundRequestCreateWidget-Popup",
				"csReturnRequestCreateWidget", "popup.refundRequestCreate",
				!((OrderManagementActionsWidgetController) widget.getWidgetController()).isRefundPossible());
		createButton(widget, component, "replaceOrder", "csReplacementRequestCreateWidgetConfig",
				"csReplacementRequestCreateWidget-Popup", "csReturnRequestCreateWidget", "popup.replacementRequestCreate",
				!((OrderManagementActionsWidgetController) widget.getWidgetController()).isReplacePossible());
		createButton(widget, component, "updateOrder", "csOrderUpdateRequestWidgetConfig", "csUpdateRequestCreateWidget-Popup",
				"csUpdateRequestCreateWidget", "popup.UpdateRequestCreate",
				!((OshOrderManagementActionsWidgetController) widget.getWidgetController()).isOrderUpdatePossible());
		createButton(widget, component, "customerCredit", "csCustomerCreditRequestWidgetConfig",
				"csCustomerCreditCreateWidget-Popup", "csCustomerCreditCreateWidget", "popup.CustomerCreditCreate",
				!((OshOrderManagementActionsWidgetController) widget.getWidgetController()).isCustomerCreditPossible());
		return component;
	}


	protected static final String CSS_UPDATE_REQUEST_CREATE_WIDGET = "csUpdateRequestCreateWidget";
}
