package com.hybris.osh.oshcscockpit.widgets.renderer.impl;



import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.models.ListWidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.RefundConfirmationWidgetRenderer;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.hybris.osh.core.price.services.OshPriceService;
import com.hybris.osh.events.RMAGeneratedEmailEvent;


public class OshRefundConfirmationWidgetRenderer extends RefundConfirmationWidgetRenderer
{
	protected static final Logger LOG = Logger.getLogger(OshRefundConfirmationWidgetRenderer.class);

	@Resource
	private EventService eventService;

	@Autowired
	private OshPriceService oshPriceService;

	@Autowired
	private ModelService modelService;


	protected class RefundConfirmedEventListener implements EventListener
	{

		@Override
		public void onEvent(final Event event) throws Exception
		{
			handleRefundConfirmedEvent(widget, event);
		}

		private final transient ListboxWidget widget;
		final RefundConfirmationWidgetRenderer this$0;

		public RefundConfirmedEventListener(final ListboxWidget widget)
		{
			super();
			this$0 = OshRefundConfirmationWidgetRenderer.this;
			this.widget = widget;
		}
	}

	@Override
	protected void handleRefundConfirmedEvent(final ListboxWidget widget, final Event event) throws Exception
	{
		final TypedObject returnRequest = ((ReturnsController) widget.getWidgetController()).createRefundRequest();
		if (returnRequest != null)
		{
			if (getPopupWidgetHelper().getCurrentPopup() != null)
			{
				final List children = getPopupWidgetHelper().getCurrentPopup().getParent().getChildren();
				for (final Iterator iterator = children.iterator(); iterator.hasNext();)
				{
					final Component c = (Component) iterator.next();
					if (c instanceof Window)
					{
						Events.postEvent(new Event("onClose", c));
					}
				}

			}
			final ReturnRequestModel returnRequestModel = (ReturnRequestModel) returnRequest.getObject();
			Messagebox.show(LabelUtils.getLabel(widget, "rmaNumber", new Object[]
			{ returnRequestModel.getRMA() }), LabelUtils.getLabel(widget, "rmaNumberTitle", new Object[0]), 1,
					"z-msgbox z-msgbox-imformation");
			((ReturnsController) widget.getWidgetController()).dispatchEvent(null, widget, null);
			eventService.publishEvent(new RMAGeneratedEmailEvent(returnRequestModel.getOrder()));
		}
		else
		{
			Messagebox.show(LabelUtils.getLabel(widget, "error", new Object[0]),
					LabelUtils.getLabel(widget, "failed", new Object[0]), 1, "z-msgbox z-msgbox-error");
		}
	}


	@Override
	protected HtmlBasedComponent createContentInternal(final ListboxWidget widget, final HtmlBasedComponent rootContainer)
	{
		final Div container = new Div();
		container.setSclass("refundConfirmationWidget");
		final Div originalOrderContent = new Div();
		originalOrderContent.setParent(container);
		final OrderModel originalOrderModel = (OrderModel) ((ReturnsController) widget.getWidgetController()).getCurrentOrder()
				.getObject(); //final final
		//final CurrencyModel currencyModel = originalOrderModel.getCurrency();


		final Double originalTotal = Double.valueOf(originalOrderModel.getTotalPrice().doubleValue()
				+ originalOrderModel.getTotalTax().doubleValue());

		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final BigDecimal totalOrderValue = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), originalTotal.doubleValue());
		final String orderTotalText = LabelUtils.getLabel(widget, "originalOrderTotal", new Object[]
		{ Double.toString(totalOrderValue.doubleValue()) });
		final Label orderTotal = new Label(orderTotalText);
		orderTotal.setParent(originalOrderContent);



		final HtmlBasedComponent modifiedOrderDetails = renderList(widget, rootContainer);
		/*
		 * final String modifiedOrderLabelText = LabelUtils.getLabel(widget, "modifiedOrder", new Object[0]); final Label
		 * modifiedOrderLabel = new Label(modifiedOrderLabelText); container.appendChild(modifiedOrderLabel);
		 */
		container.appendChild(modifiedOrderDetails);

		final TypedObject refundOrder = ((ReturnsController) widget.getWidgetController()).getRefundOrderPreview();
		if (refundOrder != null)
		{


			/*
			 * final Div refundOrderContent = new Div(); refundOrderContent.setParent(container); final double newTotal =
			 * SafeUnbox.toDouble(((OrderModel) refundOrder.getObject()).getTotalPrice()); final String
			 * refundOrderTotalText = LabelUtils.getLabel(widget, "refundOrderTotal", new Object[] {
			 * Double.toString(newTotal) }); final Label refundOrderTotal = new Label(refundOrderTotalText);
			 * refundOrderTotal.setParent(refundOrderContent); final Div refundContent = new Div();
			 * refundContent.setParent(container);
			 */

			final Div refundContent = new Div();
			refundContent.setParent(container);
			final OrderModel ordModel = (OrderModel) refundOrder.getObject();
			final List requestRefund = ordModel.getReturnRequests();
			final ReturnRequestModel reqModel = ordModel.getReturnRequests().get(requestRefund.size() - 1);
			double refundTotal = 0.0;
			for (final ReturnEntryModel returnEntryModel : reqModel.getReturnEntries())
			{
				double refundTotalTax = returnEntryModel.getOrderEntry().getTaxPerUnit().doubleValue();
				refundTotalTax = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), refundTotalTax).doubleValue();
				double discountPrice = returnEntryModel.getOrderEntry().getDiscountPrice().doubleValue();
				discountPrice = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), discountPrice).doubleValue();
				refundTotal = refundTotal + (returnEntryModel.getExpectedQuantity().doubleValue() * (discountPrice + refundTotalTax));
			}
			final BigDecimal refundAmount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), refundTotal);
			final String refundAmountText = LabelUtils.getLabel(widget, "refundAmount", new Object[]
			{ refundAmount.toString() });
			final Label refund = new Label(refundAmountText);
			refund.setParent(refundContent);
		}
		final Div confirmContent = new Div();
		confirmContent.setParent(container);
		final Button confirmButton = new Button(LabelUtils.getLabel(widget, "confirmButton", new Object[0]));
		confirmButton.setParent(confirmContent);
		confirmButton.addEventListener("onClick", createRefundConfirmedEventListener(widget));
		return container;
	}

	/**
	 * 
	 */
	private HtmlBasedComponent renderList(final ListboxWidget widget, final HtmlBasedComponent rootContainer)
	{
		final Div containers = new Div();
		containers.setSclass("csListboxContainer");
		final Listbox listBox = new Listbox();
		listBox.setParent(containers);
		widget.setListBox(listBox);
		listBox.setVflex(false);
		listBox.setFixedLayout(true);
		listBox.setSclass("csWidgetListbox");
		renderListbox(listBox, widget, rootContainer);
		if (isLazyLoadingEnabled())
		{
			UITools.applyLazyload(listBox);
		}
		if (listBox.getItemCount() > 0 && listBox.getSelectedIndex() <= 0)
		{
			listBox.setSelectedIndex(0);
		}
		return containers;
	}


	public void updatePrice(final OrderModel refundOrder)
	{
		Double salePrice = null;
		Double mapPrice = null;
		Double regPrice = null;
		for (final AbstractOrderEntryModel entryModel : refundOrder.getEntries())
		{

			final List<PriceRow> priceRow = oshPriceService.getPriceInfo(entryModel.getProduct());
			for (final PriceRow priceinfo : priceRow)
			{
				if (priceinfo.getUg().getCode().equals("Osh_RegularPrice") && priceinfo.getPrice() != null
						&& calculatePrice(priceinfo))
				{
					regPrice = priceinfo.getPrice();

				}
				if (priceinfo.getUg().getCode().equals("Osh_MapPrice") && priceinfo.getPrice() != null && calculatePrice(priceinfo))
				{
					mapPrice = priceinfo.getPrice();
				}
				if (priceinfo.getUg().getCode().equals("Osh_SalesPrice") && priceinfo.getPrice() != null && calculatePrice(priceinfo))
				{
					salePrice = priceinfo.getPrice();
				}

			}
			if (mapPrice != null)
			{
				entryModel.setBasePrice(mapPrice);
				entryModel.setTotalPrice(Double.valueOf(mapPrice.doubleValue() * entryModel.getQuantity().doubleValue()));
				modelService.save(entryModel);
				modelService.refresh(entryModel);
			}
			else if (salePrice != null)
			{
				entryModel.setBasePrice(salePrice);
				entryModel.setTotalPrice(Double.valueOf(salePrice.doubleValue() * entryModel.getQuantity().doubleValue()));
				modelService.save(entryModel);
				modelService.refresh(entryModel);
			}
			else if (regPrice != null)
			{
				entryModel.setBasePrice(regPrice);
				entryModel.setTotalPrice(Double.valueOf(regPrice.doubleValue() * entryModel.getQuantity().doubleValue()));
				modelService.save(entryModel);
				modelService.refresh(entryModel);
			}


		}
		double subtotal = 0.0;
		for (final AbstractOrderEntryModel e : refundOrder.getEntries())
		{
			subtotal += e.getTotalPrice().doubleValue();
		}
		if (refundOrder.getDeliveryCost() == null)
		{
			refundOrder.setTotalPrice(Double.valueOf(subtotal));
		}
		else
		{
			refundOrder.setTotalPrice(Double.valueOf(subtotal + refundOrder.getDeliveryCost().doubleValue()));
		}
	}

	/**
	 * 
	 */
	private boolean calculatePrice(final PriceRow priceValue)
	{

		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
		Date startDate = null;
		Date endDate = null;
		Date currentDate = new Date();
		if (priceValue.getStartTime() != null)
		{
			try
			{
				startDate = sdf.parse(sdf.format(priceValue.getStartTime()));
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage());
			}

		}
		if (priceValue.getEndTime() != null)
		{
			try
			{
				endDate = sdf.parse(sdf.format(priceValue.getEndTime()));
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage());
			}

		}
		try
		{
			currentDate = sdf.parse(sdf.format(currentDate));
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage());
		}


		if (currentDate.after(startDate) && currentDate.before(endDate))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	protected void renderListbox(final Listbox listBox, final ListboxWidget widget, final HtmlBasedComponent rootContainer)
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final ListWidgetModel widgetModel = (ListWidgetModel) widget.getWidgetModel();

		if (widgetModel != null)
		{
			final OrderModel orderModel = (OrderModel) ((ReturnsController) widget.getWidgetController()).getCurrentOrder()
					.getObject();

			final OrderModel refundOrderModel = orderModel;

			if (refundOrderModel != null)
			{

				if (refundOrderModel.getEntries() != null && !refundOrderModel.getEntries().isEmpty())
				{
					try
					{
						final List columns = getColumnConfigurations();
						if (CollectionUtils.isNotEmpty(columns))
						{
							final Listhead headRow = new Listhead();
							headRow.setParent(listBox);
							final Listheader colProductHeader = new Listheader(LabelUtils.getLabel(widget, "product", new Object[0]));
							colProductHeader.setWidth("300px");
							colProductHeader.setParent(headRow);
							Listheader listheader = new Listheader(LabelUtils.getLabel(widget, "basePrice", new Object[0]));
							listheader.setWidth("80px");
							headRow.appendChild(listheader);
							listheader = new Listheader(LabelUtils.getLabel(widget, "totalPrice", new Object[0]));
							listheader.setWidth("80px");
							headRow.appendChild(listheader);
							listheader = new Listheader(LabelUtils.getLabel(widget, "qty", new Object[0]));
							listheader.setWidth("40px");
							headRow.appendChild(listheader);
						}
						final OrderModel ordModel = (OrderModel) ((ReturnsController) widget.getWidgetController())
								.getRefundOrderPreview().getObject();
						updatePrice(ordModel);
						final List requestRefund = ordModel.getReturnRequests();
						final ReturnRequestModel reqModel = ordModel.getReturnRequests().get(requestRefund.size() - 1);
						final List items = getCockpitTypeService().wrapItems(reqModel.getReturnEntries());
						Listitem row;
						String qtyString;
						for (final Iterator iterator = items.iterator(); iterator.hasNext(); row.appendChild(new Listcell(qtyString)))
						{
							final TypedObject item = (TypedObject) iterator.next();
							final ReturnEntryModel returnEntryModel = (ReturnEntryModel) item.getObject();
							final AbstractOrderEntryModel entryModel = returnEntryModel.getOrderEntry();
							row = new Listitem();
							row.setParent(listBox);
							row.setSclass("listbox-row-item");
							final Listcell productCell = new Listcell();
							productCell.setParent(row);
							final Div productDiv = new Div();
							productDiv.setParent(productCell);
							getPropertyRendererHelper().buildPropertyValuesFromColumnConfigs(item, columns, productDiv);
							final PropertyDescriptor basePricePD = getCockpitTypeService().getPropertyDescriptor(
									"AbstractOrderEntry.basePrice");
							final PropertyDescriptor totalPricePD = getCockpitTypeService().getPropertyDescriptor(
									"AbstractOrderEntry.totalPrice");
							final PropertyDescriptor qtyPD = getCockpitTypeService()
									.getPropertyDescriptor("AbstractOrderEntry.quantity");
							final ObjectValueContainer valueContainer = getValueContainer(item, Arrays.asList(new PropertyDescriptor[]
							{ basePricePD, totalPricePD, qtyPD }));
							//final CurrencyModel cartCurrencyModel = refundOrderModel.getCurrency();

							//final Double basePriceValue1 = ObjectGetValueUtils.getDoubleValue(valueContainer, basePricePD);
							//final String basePriceString = basePriceValue == null ? "" : Double.toString(basePriceValue.doubleValue());
							final Double basePriceValue = entryModel.getBasePrice();
							final Double totalPriceValue = entryModel.getDiscountPrice();
							final Double taxValue = entryModel.getTaxPerUnit();
							final String basePriceString = basePriceValue == null ? "" : Double.toString(basePriceValue.doubleValue());
							row.appendChild(new Listcell(basePriceString));
							//final Double totalPriceValue = ObjectGetValueUtils.getDoubleValue(valueContainer, totalPricePD);
							final String totalPriceString = basePriceValue == null ? "" : Double.toString(Helper.roundCurrencyValue(
									ctx,
									ctx.getCurrency(),
									totalPriceValue.doubleValue() + taxValue.doubleValue()
											* returnEntryModel.getExpectedQuantity().doubleValue()).doubleValue());
							row.appendChild(new Listcell(totalPriceString));

							//final Long qty = ObjectGetValueUtils.getLongValue(valueContainer, qtyPD);
							final Long qty = returnEntryModel.getExpectedQuantity();
							qtyString = qty == null ? "" : qty.toString();
						}

					}
					catch (final Exception e)
					{
						LOG.error("failed to render return entries list", e);
					}
				}
			}
		}

	}
}
