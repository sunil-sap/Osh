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
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;


// Referenced classes of package de.hybris.platform.cscockpit.widgets.renderers.impl:
//            AbstractCsWidgetRenderer

public abstract class AbstractOshOrderTotalsWidgetRenderer extends AbstractCsWidgetRenderer
{

	protected static final String CSS_ORDER_TOTALS = "csOrderTotals";
	protected static final String CSS_ORDER_TOTAL_ROW = "csOrderTotalRow";
	protected static final String CSS_ORDER_TOTAL_TITLE = "csOrderTotalTitle";
	protected static final String CSS_ORDER_TOTAL_VALUE = "csOrderTotalValue";
	@Autowired
	private FormatFactory formatFactory;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private SessionService sessionService;

	public AbstractOshOrderTotalsWidgetRenderer()
	{
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected FormatFactory getFormatFactory()
	{
		return formatFactory;
	}

	public void setFormatFactory(final FormatFactory formatFactory)
	{
		this.formatFactory = formatFactory;
	}

	protected void renderOrderDetail(final Widget widget, final TypedObject order, final HtmlBasedComponent parent)
	{
		final Div container = new Div();
		container.setSclass("csOrderTotals");
		if (order != null && (order.getObject() instanceof AbstractOrderModel))
		{
			final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) order.getObject();
			final CurrencyModel cartCurrencyModel = abstractOrderModel.getCurrency();
			final NumberFormat currencyInstance = (NumberFormat) getSessionService().executeInLocalView(new SessionExecutionBody()
			{

				@Override
				public Object execute()
				{
					getCommonI18NService().setCurrentCurrency(cartCurrencyModel);
					return getFormatFactory().createCurrencyFormat();
				}

				final AbstractOshOrderTotalsWidgetRenderer this$0;
				private final CurrencyModel val$cartCurrencyModel;


				{
					this$0 = AbstractOshOrderTotalsWidgetRenderer.this;
					val$cartCurrencyModel = cartCurrencyModel;
					//super();
				}
			});
			final Double subtotal = abstractOrderModel.getSubtotal();
			renderRow(subtotal, LabelUtils.getLabel(widget, "subtotal", new Object[0]), currencyInstance, container);
			final Double taxes = abstractOrderModel.getTotalTax();
			renderRow(taxes, LabelUtils.getLabel(widget, "taxes", new Object[0]), currencyInstance, container);
			final Double paymentCosts = abstractOrderModel.getPaymentCost();
			renderRow(paymentCosts, LabelUtils.getLabel(widget, "paymentCosts", new Object[0]), currencyInstance, container);
			final Double deliveryCosts = abstractOrderModel.getDeliveryCost();
			renderRow(deliveryCosts, LabelUtils.getLabel(widget, "deliveryCosts", new Object[0]), currencyInstance, container);
			final Double discounts = abstractOrderModel.getTotalDiscounts();
			renderRow(discounts, LabelUtils.getLabel(widget, "discounts", new Object[0]), currencyInstance, container);
			final Double totalPrice = Double.valueOf(abstractOrderModel.getTotalPrice().doubleValue() + taxes.doubleValue());
			renderRow(totalPrice, LabelUtils.getLabel(widget, "totalPrice", new Object[0]), currencyInstance, container);
		}
		container.setParent(parent);
	}

	protected void renderRow(final Double value, final String label, final NumberFormat currencyInstance,
			final HtmlBasedComponent container)
	{
		if (value != null && label != null && label.length() > 0)
		{
			final Div content = new Div();
			content.setSclass("csOrderTotalRow");
			final Label title = new Label(label);
			title.setSclass("csOrderTotalTitle");
			content.appendChild(title);
			final String priceString = currencyInstance.format(value);
			final Label priceLabel = new Label(priceString);
			priceLabel.setSclass("csOrderTotalValue");
			content.appendChild(priceLabel);
			content.setParent(container);
		}
	}


}
