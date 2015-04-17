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
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Span;
import org.zkoss.zul.Textbox;

import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.core.voucher.OshVoucherService;
import com.hybris.osh.oshcscockpit.widget.controller.impl.DefaultCsVoucherController;


/**
 *
 */
public class DefaultCsVoucherRenderer extends AbstractCsWidgetRenderer
{
	private OshVoucherService voucherService;
	private VoucherModelService voucherModelService;
	private UserService userService;
	private BasketController basketController;
	private static final Logger LOG = Logger.getLogger(DefaultCsVoucherRenderer.class);

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget widget, final HtmlBasedComponent arg1)
	{
		final Div content = new Div();
		final Div component = new Div();

		component.setSclass("orderManagementActionsWidget");
		createButton(widget, component, "ApplyVoucher", "csVoucherWidgetConfig",
				((DefaultCsVoucherController) widget.getWidgetController()).isVoucherApplied());

		component.setParent(content);
		content.setClass("applyVoucher");
		return content;
	}

	protected void createButton(final Widget widget, final Div container, final String buttonLabelName,
			final String springWidgetName, final boolean voucherApplied)
	{
		final EventListener eventListener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleButtonClickEvent(widget, event, container, springWidgetName);
			}
		};

		createButton(widget, container, buttonLabelName, eventListener, voucherApplied);
	}

	protected class RemoveEventListener implements EventListener
	{
		@Override
		public void onEvent(final Event event) throws Exception
		{
			handleRemoveClickEvent(widget, event, container, springWidgetName, id);
		}

		String id;
		final Widget widget;
		final Div container;
		final String buttonLabelName;
		final String springWidgetName;

		public RemoveEventListener(final String id, final Widget widget, final Div container, final String buttonLabelName,
				final String springWidgetName)
		{
			this.id = id;
			this.widget = widget;
			this.container = container;
			this.buttonLabelName = buttonLabelName;
			this.springWidgetName = springWidgetName;

		}
	}

	protected CartModel getCartModel()
	{
		final TypedObject cart = getBasketController().getCart();
		if (cart != null && (cart.getObject() instanceof CartModel))
		{
			return (CartModel) cart.getObject();
		}
		else
		{
			return null;
		}
	}

	protected void handleButtonClickEvent(final Widget widget, final Event event, final Div container,
			final String springWidgetName) throws Exception
	{
		final List<AbstractComponent> componentList = container.getChildren();
		for (final AbstractComponent component : componentList)
		{
			if (component instanceof Span)
			{
				for (final Object innerComponent : component.getChildren())
				{
					if (innerComponent instanceof Textbox)
					{
						final Textbox textbox = (Textbox) innerComponent;
						final String voucherCode = textbox.getValue();
						if (voucherCode != null && !voucherCode.isEmpty())
						{
							final VoucherModel voucherModel = voucherService.getVoucher(voucherCode);
							if (voucherModel != null)
							{
								if (voucherModelService.isReservable(voucherModel, voucherCode, getCartModel()))
								{
									voucherService.redeemVoucher(voucherCode, getCartModel());
								}
								else
								{
									Messagebox
											.show("Failed to Apply Voucher",
													LabelUtils.getLabel(widget, "FailedtoApplyVoucher", new Object[0]), 1,
													"z-msgbox z-msgbox-error");

									LOG.error("Failed to Apply Voucher");
								}
							}
							else
							{
								Messagebox.show("Failed to Apply Voucher",
										LabelUtils.getLabel(widget, "FailedtoApplyVoucher", new Object[0]), 1, "z-msgbox z-msgbox-error");

								LOG.error("Failed to Apply Voucher");
							}
						}
						else
						{
							Messagebox.show("Please enter Voucher Code", LabelUtils.getLabel(widget, "blankVoucherCode", new Object[0]),
									1, "z-msgbox z-msgbox-error");

							LOG.error("Failed to Apply Voucher");
						}
					}
				}
			}
		}
		try
		{
			getBasketController().triggerCheckout();
			((DefaultCsVoucherController) widget.getWidgetController()).dispatchEvent(null, widget, null);
		}
		catch (final ValidationException ex)
		{
			((CheckoutController) widget.getWidgetController()).dispatchEvent(null, widget, null);
		}
	}

	protected void handleRemoveClickEvent(final Widget widget, final Event event, final Div container,
			final String springWidgetName, final String id) throws InterruptedException
	{
		if (id != null)
		{
			voucherService.removeVoucher(id, getCartModel());
		}
		try
		{
			getBasketController().triggerCheckout();
			((DefaultCsVoucherController) widget.getWidgetController()).dispatchEvent(null, widget, null);
		}
		catch (final ValidationException ex)
		{
			Messagebox.show("Failed to Apply Voucher", LabelUtils.getLabel(widget, "FailedtoApplyVoucher", new Object[0]), 1,
					"z-msgbox z-msgbox-error");
		}
	}

	protected void createButton(final Widget widget, final Div container, final String buttonLabelName,
			final EventListener eventListener, final boolean voucherApplied)
	{
		final Span div = new Span();
		final Textbox textbox = new Textbox();
		textbox.setParent(div);

		final Button button = new Button();
		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		{
			if ("ApplyVoucher".equals(buttonLabelName))
			{
				UITools.applyTestID(button, "ApplyVoucher");
			}
		}

		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(div);
		button.addEventListener("onClick", eventListener);
		div.setParent(container);
		if (voucherApplied)
		{
			final List<String> appliedVouchers = new ArrayList<String>(voucherService.getAppliedVoucherCodes(getCartModel()));
			for (final String voucherCode : appliedVouchers)
			{
				for (final DiscountModel discount : getCartModel().getDiscounts())
				{
					if (discount instanceof OshPromotionVoucherModel
							&& ((OshPromotionVoucherModel) discount).getVoucherCode().equalsIgnoreCase(voucherCode))
					{
						final Div entry = new Div();
						final Label label = new Label(voucherCode + " Voucher is Applied to Cart");
						label.setParent(entry);
						final Button removeButton = new Button();
						if (UISessionUtils.getCurrentSession().isUsingTestIDs())
						{
							if ("RemoveVoucher".equals(buttonLabelName))
							{
								UITools.applyTestID(button, "RemoveVoucher");
							}
						}

						removeButton.setLabel(LabelUtils.getLabel(widget, "RemoveVoucher", new Object[0]));
						removeButton.setId(voucherCode);
						removeButton.setParent(entry);
						removeButton.setDisabled(false);
						removeButton.addEventListener("onClick", new RemoveEventListener(voucherCode, widget, container,
								buttonLabelName, "remove"));
						entry.setParent(container);

					}
					else
					{
						final Div entry = new Div();
						final Label label = new Label(voucherCode + " Voucher is Applied to Cart");
						label.setParent(entry);
						final Button removeButton = new Button();
						if (UISessionUtils.getCurrentSession().isUsingTestIDs())
						{
							if ("RemoveVoucher".equals(buttonLabelName))
							{
								UITools.applyTestID(button, "RemoveVoucher");
							}
						}

						removeButton.setLabel(LabelUtils.getLabel(widget, "RemoveVoucher", new Object[0]));
						removeButton.setId(voucherCode);
						removeButton.setParent(entry);
						removeButton.setDisabled(false);
						removeButton.addEventListener("onClick", new RemoveEventListener(voucherCode, widget, container,
								buttonLabelName, "remove"));
						entry.setParent(container);
					}

				}
			}
		}
	}



	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public BasketController getBasketController()
	{
		return basketController;
	}

	public void setBasketController(final BasketController basketController)
	{
		this.basketController = basketController;
	}

	/**
	 * @return the voucherService
	 */
	public OshVoucherService getVoucherService()
	{
		return voucherService;
	}

	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final OshVoucherService voucherService)
	{
		this.voucherService = voucherService;
	}

	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}

	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}
}
