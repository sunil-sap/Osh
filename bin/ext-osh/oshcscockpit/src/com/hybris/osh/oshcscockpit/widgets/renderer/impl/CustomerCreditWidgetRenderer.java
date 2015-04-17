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

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.impl.PropertyColumnConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.models.ListWidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.CockpitUiConfigLoader;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.WidgetHelper;
import de.hybris.platform.cscockpit.utils.comparators.TypedObjectOrderEntryNumberComparator;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.hybris.osh.core.enums.OshShippingTypes;
import com.hybris.osh.core.model.CreditPaymentTransactionModel;
import com.hybris.osh.oshcscockpit.editor.OshEditorHelper;
import com.hybris.osh.oshcscockpit.widget.controller.impl.DefaultCustomerCreditController;



public class CustomerCreditWidgetRenderer extends AbstractOshCreateWithListWidgetRenderer
{
	@Resource(name = "businessProcessService")
	BusinessProcessService businessProcessService;

	@Resource(name = "modelService")
	ModelService modelService;

	@Resource(name = "eventService")
	EventService eventService;

	private static final Logger LOG = Logger.getLogger(OrderUpdateWidgetRenderer.class);
	private transient List returnObjectValueContainers;
	private String listEditorConfigurationCode;
	private String productInfoConfigurationCode;
	protected static final String CSS_LISTBOX_CONTAINER = "csListboxContainer";
	protected static final String CSS_WIDGET_LISTBOX = "csWidgetListbox";
	protected static final String CSS_RETURN_REQUEST_ACTIONS = "csUpdateRequestActions";
	protected static final String CSS_CANT_RETURN = "csCantReturn";
	protected static final String CSS_LISTBOX_ROW_ITEM = "listbox-row-item";
	protected static final String CSS_REFUND_CONFIRMATION_WIDGET = "csUpdateConfirmationWidget";
	private PopupWidgetHelper popupWidgetHelper;
	private WidgetHelper widgetHelper;

	protected class CustomerCreditCreateEventListener implements EventListener
	{

		private final InputWidget widget;
		private final List returnObjectValueContainers;
		private final List orderEntryUpdateRecordEntries;
		final CustomerCreditWidgetRenderer this$0;


		@Override
		public void onEvent(final Event event) throws Exception
		{
			try
			{
				handleReturnRequestCreateEvent(widget, event, returnObjectValueContainers, orderEntryUpdateRecordEntries);
				//	getPopupWidgetHelper().dismissCurrentPopup();
			}
			catch (final ValidationException e)
			{
				Messagebox
						.show((new StringBuilder(String.valueOf(e.getMessage()))).append(
								e.getCause() != null ? (new StringBuilder(" - ")).append(e.getCause().getMessage()).toString() : "")
								.toString(), LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
			}
		}

		public CustomerCreditCreateEventListener(final InputWidget widget, final List returnObjectValueContainers,
				final List orderEntryUpdateRecordEntries)
		{
			super();
			this$0 = CustomerCreditWidgetRenderer.this;
			this.widget = widget;
			this.returnObjectValueContainers = returnObjectValueContainers;
			this.orderEntryUpdateRecordEntries = orderEntryUpdateRecordEntries;
		}

	}

	@Override
	protected org.zkoss.zk.ui.api.HtmlBasedComponent createContentInternal(final Widget widget,
			final org.zkoss.zk.ui.api.HtmlBasedComponent htmlbasedcomponent)
	{
		return createContentInternal((InputWidget) widget, htmlbasedcomponent);
	}

	/**
	 * Event that triggers the Order fulfillment process .
	 *
	 *
	 * @throws ValidationException
	 *
	 */
	public void handleReturnRequestCreateEvent(final InputWidget widget, final Event event,
			final List returnObjectValueContainers, final List orderEntryUpdateRecordEntries) throws NullPointerException,
			ValidationException

	{
		final OrderModel orderModel = (OrderModel) ((DefaultCustomerCreditController) widget.getWidgetController())
				.getCurrentOrder().getObject();
		final List<ObjectValueContainer> objectValueContainerList = returnObjectValueContainers;

		boolean flag = true;

		final CreditPaymentTransactionModel creditPaymentTransactionModel = modelService
				.create(CreditPaymentTransactionModel.class);
		creditPaymentTransactionModel.setAbstractOrder(orderModel);
		AbstractOrderModel orderTypeModel = null;
		for (final ObjectValueContainer objectValueContainer : objectValueContainerList)
		{
			final TypedObject orderModelTypedObject = (TypedObject) objectValueContainer.getObject();
			orderTypeModel = (AbstractOrderModel) orderModelTypedObject.getObject();
			final ObjectValueHolder customerCreditAmount = (((DefaultCustomerCreditController) widget.getWidgetController())
					.getPropertyValue(objectValueContainer, "CreditPaymentTransaction.creditAmount"));
			final ObjectValueHolder customerCreditNotes = (((DefaultCustomerCreditController) widget.getWidgetController())
					.getPropertyValue(objectValueContainer, "CreditPaymentTransaction.creditNotes"));
			final ObjectValueHolder customerCreditShippingType = (((DefaultCustomerCreditController) widget.getWidgetController())
					.getPropertyValue(objectValueContainer, "CreditPaymentTransaction.OshShippingType"));
			if (customerCreditShippingType != null && customerCreditShippingType.getLiveValue() != null
					&& !customerCreditShippingType.getLiveValue().toString().isEmpty())
			{

				final String shippingType = customerCreditShippingType.getLiveValue().toString();
				if (shippingType.equals(OshShippingTypes.SHIPPING.getCode()))
				{
					creditPaymentTransactionModel.setOshShippingType(OshShippingTypes.SHIPPING);
				}
				else if (shippingType.equals(OshShippingTypes.OTHER.getCode()))
				{
					creditPaymentTransactionModel.setOshShippingType(OshShippingTypes.OTHER);
				}
				else
				{
					showMessages("Please Select Shipping Type...", "Customer Credit Error", Messagebox.OK);
					flag = false;
				}
			}
			else
			{
				showMessages("Please Select Shipping Type...", "Customer Credit Error", Messagebox.OK);
				flag = false;
			}
			if (customerCreditAmount != null && flag != false)
			{
				if (customerCreditAmount.getLiveValue() != null && !customerCreditAmount.getLiveValue().toString().isEmpty()
						&& !(customerCreditAmount.getLiveValue().toString().equals("0.0")))
				{

					creditPaymentTransactionModel.setCreditAmount((Double) customerCreditAmount.getLiveValue());
					final int i1 = showMessages("$" + customerCreditAmount.getLiveValue()
							+ " will be credited to customers account. Do you want to proceed with crediting the amount? ",
							"Customer Credit Conformation", Messagebox.OK | Messagebox.CANCEL);
					if (i1 != 1)
					{
						flag = false;
					}
				}
				else
				{
					showMessages("Please Enter Amount...", "Customer Credit Error", Messagebox.OK);
					flag = false;
				}

			}
			if (customerCreditNotes.getLiveValue() != null && !customerCreditNotes.getLiveValue().toString().isEmpty())
			{
				creditPaymentTransactionModel.setCreditNotes(customerCreditNotes.getLiveValue().toString());
			}

			orderModel.setCreditTlog(true);
		}

		if (flag == true)
		{
			modelService.save(creditPaymentTransactionModel);
			modelService.refresh(creditPaymentTransactionModel);

			modelService.save(orderTypeModel);
			modelService.refresh(orderTypeModel);
			modelService.save(orderModel);
			final int index = 0;
			triggerCustomerCreditProcess(orderModel, index);
			((DefaultCustomerCreditController) widget.getWidgetController()).dispatchEvent(widget.getControllerCtx(), widget, null);
			Executions.sendRedirect("");
		}
	}

	public int showMessages(final String messageHeader, final String message, final int buttons)
	{
		try
		{
			return Messagebox.show(messageHeader, message, buttons, null);
		}
		catch (final Exception e)
		{
			if (LOG.isDebugEnabled())
			{
				e.printStackTrace();
			}
		}
		return -1;
	}

	public void triggerCustomerCreditProcess(final OrderModel orderModel, int index)
	{
		//orderModel.setTlogPerformed(false);
		final OrderProcessModel subProcess = (OrderProcessModel) businessProcessService.createProcess(
				orderModel.getCode() + Math.random() + "_" + (++index), "customerCreditReturnProcess");
		subProcess.setOrder(orderModel);
		modelService.save(subProcess);
		modelService.refresh(subProcess);
		modelService.save(orderModel);
		modelService.refresh(orderModel);
		businessProcessService.startProcess(subProcess);
		checkprocess(subProcess.getCode());

		final List<PaymentTransactionEntryModel> listPaymentTransactionEntryModel = orderModel.getPaymentTransactions().get(0)
				.getEntries();
		final PaymentTransactionEntryModel paymentTransactionEntryModel = listPaymentTransactionEntryModel
				.get(listPaymentTransactionEntryModel.size() - 1);
		if (paymentTransactionEntryModel.getTransactionStatus().equals(TransactionStatus.ACCEPTED.name()))
		{
			showMessages("Transction has been completed successfully", "Customer Credit Notification", Messagebox.OK);
		}
		else
		{
			showMessages("Something Went Wrong", "Customer Credit Notification", Messagebox.OK);
		}
	}

	public boolean checkprocess(final String processCode)
	{
		if (businessProcessService.getProcess(processCode).getProcessState().getCode().equals(ProcessState.RUNNING.getCode()))
		{
			try
			{
				Thread.sleep(30000);
				//checkprocess(processCode);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}

	protected HtmlBasedComponent createContentInternal(final InputWidget widget, final HtmlBasedComponent rootContainer)
	{
		final Div content = new Div();
		returnObjectValueContainers = new ArrayList();
		if (((DefaultCustomerCreditController) widget.getWidgetController()).canUpdate())
		{

			final Div updateContainer = new Div();
			updateContainer.setSclass("csListboxContainer");
			updateContainer.setParent(content);
			final Listbox listBox = new Listbox();
			listBox.setParent(updateContainer);
			listBox.setVflex(false);
			listBox.setFixedLayout(true);
			listBox.setSclass("csWidgetListbox");
			renderListbox(listBox, widget);
			final Div requestCreationContent = new Div();
			requestCreationContent.setSclass("csUpdateRequestActions");
			requestCreationContent.setParent(content);
			final Button orderUpdateButton = new Button(LabelUtils.getLabel(widget, "createButton", new Object[0]));
			orderUpdateButton.setParent(requestCreationContent);
			orderUpdateButton.addEventListener("onClick",
					createUpdateRequestCreateEventListener(widget, returnObjectValueContainers));

		}

		return content;

	}

	private EventListener createUpdateRequestCreateEventListener(final InputWidget widget, final List returnObjectValueContainers)
	{
		return new CustomerCreditCreateEventListener(widget, returnObjectValueContainers, returnObjectValueContainers);
	}

	protected void renderListbox(final Listbox listBox, final InputWidget widget)
	{
		final ListWidgetModel widgetModel = (ListWidgetModel) widget.getWidgetModel();

		if (widgetModel != null)
		{

			final Map updatableOrderEntries = ((DefaultCustomerCreditController) widget.getWidgetController())
					.getUpdatableOrderEntries();

			if (updatableOrderEntries != null && !updatableOrderEntries.isEmpty())
			{
				try
				{

					final List columns = getColumnConfigurations(getListConfigurationCode(), getListConfigurationType());
					if (CollectionUtils.isNotEmpty(columns))
					{
						final Listhead headRow = new Listhead();
						headRow.setParent(listBox);
						ColumnConfiguration col;
						Listheader colHeader;
						for (final Iterator iterator = columns.iterator(); iterator.hasNext(); colHeader.setTooltiptext(col.getName()))
						{
							col = (ColumnConfiguration) iterator.next();
							colHeader = new Listheader(getPropertyRendererHelper().getPropertyDescriptorName(col));
							colHeader.setParent(headRow);
						}


						final EditorConfiguration editorConf = CockpitUiConfigLoader.getEditorConfiguration(
								UISessionUtils.getCurrentSession(), getListEditorConfigurationCode(), getListConfigurationType());
						final List editorMapping = getPropertyEditorHelper().getAllEditorRowConfigurations(editorConf);
						final List orderEntries = new ArrayList(updatableOrderEntries.keySet());
						Collections.sort(orderEntries, TypedObjectOrderEntryNumberComparator.INSTANCE);
						for (final Iterator iterator1 = orderEntries.iterator(); iterator1.hasNext();)
						{
							final TypedObject item = (TypedObject) iterator1.next();
							final Listitem row = new Listitem();
							row.setSclass("listbox-row-item");
							row.setParent(listBox);
							final TypedObject returnEntryObject = null;
							final ObjectType updateEntryType = getCockpitTypeService().getObjectType(getListConfigurationType());
							final ObjectValueContainer returnEntryValueContainer = buildReturnEntryValueContainer(item, updateEntryType,
									updateEntryType.getPropertyDescriptors(), getSystemService().getAvailableLanguageIsos());
							returnObjectValueContainers.add(returnEntryValueContainer);
							for (final Iterator iterator2 = columns.iterator(); iterator2.hasNext();)
							{
								final ColumnConfiguration column = (ColumnConfiguration) iterator2.next();

								if (column instanceof PropertyColumnConfiguration)
								{

									final PropertyColumnConfiguration col1 = (PropertyColumnConfiguration) column;

									if (true)
									{
										final PropertyDescriptor propertyDescriptor = col1.getPropertyDescriptor();

										final EditorRowConfiguration editorRowConfiguration = getPropertyEditorHelper()
												.findConfigurationByPropertyDescriptor(editorMapping, propertyDescriptor);

										if (editorRowConfiguration != null)
										{
											final Listcell cell = new Listcell();
											cell.setParent(row);

											final Div editorDiv = new Div();
											editorDiv.setParent(cell);
											editorDiv.setSclass("editorWidgetEditor");

											renderEditor(editorDiv, editorRowConfiguration, returnEntryObject, returnEntryValueContainer,
													propertyDescriptor, widget);
											row.setSelected(true);
										}
									}

								}
							}

						}

					}
				}
				catch (final Exception e)
				{
					LOG.error("failed to render return entries list", e);
				}
			}
		}

	}


	protected ObjectValueContainer buildReturnEntryValueContainer(final TypedObject orderEntry, final ObjectType returnEntryType,
			final Set returnEntryPropertyDescriptors, final Set langISOs)
	{
		final ObjectValueContainer currentObjectValues = new ObjectValueContainer(returnEntryType, orderEntry);
		for (final Iterator iterator = returnEntryPropertyDescriptors.iterator(); iterator.hasNext();)
		{
			final PropertyDescriptor pd = (PropertyDescriptor) iterator.next();
			if (pd.isLocalized())
			{
				String langIso;
				for (final Iterator iterator1 = langISOs.iterator(); iterator1.hasNext(); currentObjectValues.addValue(pd, langIso,
						getDefaultValue(pd)))
				{
					langIso = (String) iterator1.next();
				}
			}

			else
			{
				final Object currentValue = getDefaultValue(pd);
				currentObjectValues.addValue(pd, null, currentValue);
			}
		}

		return currentObjectValues;
	}

	protected List getColumnConfigurations(final String confCode, final String confTypeCode)
	{
		return CockpitUiConfigLoader.getAllVisibleColumnConfigurations(UISessionUtils.getCurrentSession(), confCode, confTypeCode);
	}

	protected void renderEditor(final org.zkoss.zk.ui.HtmlBasedComponent parent, final EditorRowConfiguration rowConfig,
			final TypedObject item, final ObjectValueContainer returnEntryValueContainer,
			final PropertyDescriptor propertyDescriptor, final Widget widget)
	{
		if (true)
		{
			final Hbox hbox = new Hbox();
			hbox.setParent(parent);
			hbox.setWidth("60%");
			hbox.setWidths("9em, none");


			final CreateContext ctx = new CreateContext(null, item, propertyDescriptor, null);
			final TypeService typeService = getCockpitTypeService();
			ctx.setExcludedTypes(EditorHelper.parseTemplateCodes(rowConfig.getParameter("excludeCreateTypes"), typeService));
			ctx.setAllowedTypes(EditorHelper.parseTemplateCodes(rowConfig.getParameter("restrictToCreateTypes"), typeService));
			final Map params = new HashMap();
			params.putAll(rowConfig.getParameters());
			params.put("createContext", ctx);
			params.put("eventSource", widget);

			OshEditorHelper.createEditor(item, propertyDescriptor, hbox, returnEntryValueContainer, true, rowConfig.getEditor(),
					params, "ABC");
		}
	}

	public String getListEditorConfigurationCode()
	{
		return listEditorConfigurationCode;
	}

	public void setListEditorConfigurationCode(final String listEditorConfigurationCode)
	{
		this.listEditorConfigurationCode = listEditorConfigurationCode;
	}


	public String getProductInfoConfigurationCode()
	{
		return productInfoConfigurationCode;
	}


	public void setProductInfoConfigurationCode(final String productInfoConfigurationCode)
	{
		this.productInfoConfigurationCode = productInfoConfigurationCode;
	}


	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}


	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}


	public WidgetHelper getWidgetHelper()
	{
		return widgetHelper;
	}


	public void setWidgetHelper(final WidgetHelper widgetHelper)
	{
		this.widgetHelper = widgetHelper;
	}


}
