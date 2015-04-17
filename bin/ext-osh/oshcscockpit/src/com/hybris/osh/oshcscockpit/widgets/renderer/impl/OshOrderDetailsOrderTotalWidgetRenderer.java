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
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;


// Referenced classes of package de.hybris.platform.cscockpit.widgets.renderers.impl:
//            AbstractOrderTotalsWidgetRenderer

public class OshOrderDetailsOrderTotalWidgetRenderer extends AbstractOshOrderTotalsWidgetRenderer
{

	public OshOrderDetailsOrderTotalWidgetRenderer()
	{
	}

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget widget, final HtmlBasedComponent rootContainer)
	{
		final Div content = new Div();
		renderOrderDetail(widget, ((OrderItemWidgetModel) widget.getWidgetModel()).getOrder(), content);
		return content;
	}
}
