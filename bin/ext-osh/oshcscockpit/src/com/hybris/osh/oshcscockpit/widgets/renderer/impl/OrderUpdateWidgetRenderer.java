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

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.CockpitUiConfigLoader;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.WidgetHelper;
import de.hybris.platform.cscockpit.utils.comparators.TypedObjectOrderEntryNumberComparator;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.hybris.osh.constants.OshcscockpitConstants;
import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.oshcscockpit.editor.OshEditorHelper;
import com.hybris.osh.oshcscockpit.widget.controller.OrderUpdateController;



public class OrderUpdateWidgetRenderer extends AbstractOshCreateWithListWidgetRenderer
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

	protected class UpdateRequestCreateEventListener implements EventListener
	{

		private final InputWidget widget;
		private final List returnObjectValueContainers;
		private final List orderEntryUpdateRecordEntries;
		final OrderUpdateWidgetRenderer this$0;


		@Override
		public void onEvent(final Event event) throws Exception
		{
			try
			{
				handleReturnRequestCreateEvent(widget, event, returnObjectValueContainers, orderEntryUpdateRecordEntries);
				getPopupWidgetHelper().dismissCurrentPopup();
			}
			catch (final ValidationException e)
			{
				Messagebox
						.show((new StringBuilder(String.valueOf(e.getMessage()))).append(
								e.getCause() != null ? (new StringBuilder(" - ")).append(e.getCause().getMessage()).toString() : "")
								.toString(), LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
			}


		}

		public UpdateRequestCreateEventListener(final InputWidget widget, final List returnObjectValueContainers,
				final List orderEntryUpdateRecordEntries)
		{
			super();
			this$0 = OrderUpdateWidgetRenderer.this;
			this.widget = widget;
			this.returnObjectValueContainers = returnObjectValueContainers;
			this.orderEntryUpdateRecordEntries = orderEntryUpdateRecordEntries;
		}

	}

	public OrderUpdateWidgetRenderer()
	{

	}


	@Override
	protected org.zkoss.zk.ui.api.HtmlBasedComponent createContentInternal(final Widget widget,
			final org.zkoss.zk.ui.api.HtmlBasedComponent htmlbasedcomponent)
	{
		return createContentInternal((InputWidget) widget, htmlbasedcomponent);
	}

	/**
	 * Event that triggers the Consignment fulfillment process .
	 * 
	 * 
	 * @throws ValidationException
	 * 
	 */
	public void handleReturnRequestCreateEvent(final InputWidget widget, final Event event,
			final List returnObjectValueContainers, final List orderEntryUpdateRecordEntries) throws NullPointerException,
			ValidationException

	{

		boolean isTrackingIDUpdated = false;
		boolean isEmailSent = true;
		int indexNumber = 0;
		final OrderModel orderModel = (OrderModel) ((OrderUpdateController) widget.getWidgetController()).getCurrentOrder()
				.getObject();
		final List<ObjectValueContainer> objectValueContainerList = returnObjectValueContainers;
		final int listSize = objectValueContainerList.size();
		final List<ConsignmentModel> updatedConsignments = new ArrayList<ConsignmentModel>();
		for (final ObjectValueContainer objectValueContainer : objectValueContainerList)
		{
			String status = null;
			String trackingId = null;
			int shippedQty = 0;
			//String shippingAmt = null;
			//final double remainingAmt = 0;
			final TypedObject consignmentModelTypedObject = (TypedObject) objectValueContainer.getObject();
			final ConsignmentModel consignmentModel = (ConsignmentModel) consignmentModelTypedObject.getObject();
			/*
			 * if ((((OrderUpdateController) widget.getWidgetController()).getPropertyValue(objectValueContainer,
			 * "Consignment.status")) .getLiveValue() != null) {
			 */
			final ObjectValueHolder consignmentStatusObject = (((OrderUpdateController) widget.getWidgetController())
					.getPropertyValue(objectValueContainer, "Consignment.status"));
			if (consignmentModel.isPaymentCapture())
			{
				status = "SHIPPED";
			}
			else if (consignmentStatusObject != null)
			{
				if (consignmentStatusObject.getLiveValue() != null)
				{
					status = consignmentStatusObject.getLiveValue().toString();
				}
			}
			if ((consignmentModel.getCode().contains("online-") || consignmentModel.getCode().contains("dropship-"))
					&& status != null && status.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				/*
				 * if ((((OrderUpdateController) widget.getWidgetController()).getPropertyValue(objectValueContainer,
				 * "Consignment.trackingId")).getLiveValue() != null) {
				 */
				final ObjectValueHolder trackingIdObject = (((OrderUpdateController) widget.getWidgetController()).getPropertyValue(
						objectValueContainer, "Consignment.trackingId"));
				final ObjectValueHolder notesObject = (((OrderUpdateController) widget.getWidgetController()).getPropertyValue(
						objectValueContainer, "Consignment.notes"));
				if (trackingIdObject.getLiveValue() != null && !trackingIdObject.getLiveValue().toString().isEmpty())
				{
					trackingId = trackingIdObject.getLiveValue().toString();
					consignmentModel.setUpdateTrackingID(true);
					isTrackingIDUpdated = true;
					consignmentModel.setTrackingID(trackingId);
				}
				if (consignmentModel.isPaymentCapture())
				{
					consignmentModel.setShipedEmailSent(false);
				}
				if (notesObject.getLiveValue() != null && !notesObject.getLiveValue().toString().isEmpty())
				{
					final String notes = notesObject.getLiveValue().toString();
					consignmentModel.setNotes(notes);
				}
				for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
				{
					shippedQty = shippedQty + consignmentEntry.getOrderEntry().getQuantity().intValue();
					consignmentModel.setShippedQty(Integer.valueOf(shippedQty));
				}
				modelService.save(consignmentModel);
				modelService.refresh(consignmentModel);
				/*
				 * else { final List entryErrorMessages = new ArrayList(); entryErrorMessages.add(new
				 * ResourceMessage("Please enter Tracking Id", Arrays.asList(new Double[] { Double.valueOf(0.0) }))); throw
				 * new ValidationException(entryErrorMessages); }
				 */
				/*
				 * else { final List entryErrorMessages = new ArrayList(); entryErrorMessages.add(new
				 * ResourceMessage("Please enter Tracking Id", Arrays.asList(new Double[] { Double.valueOf(0.0) }))); throw
				 * new ValidationException(entryErrorMessages); }
				 */


			}

		}
		for (final ObjectValueContainer objectValueContainer : objectValueContainerList)
		{
			String status = null;
			String trackingId = null;
			boolean processOnline = false;
			boolean processDropShip = false;
			boolean processBopis = false;
			indexNumber++;


			/*
			 * if ((((OrderUpdateController) widget.getWidgetController()).getPropertyValue(objectValueContainer,
			 * "Consignment.shippingAmt")).getLiveValue() != null) { final ObjectValueHolder shippingIdObject =
			 * (((OrderUpdateController) widget.getWidgetController()).getPropertyValue( objectValueContainer,
			 * "Consignment.shippingAmt")); if (!shippingIdObject.getLiveValue().toString().isEmpty()) { shippingAmt =
			 * shippingIdObject.getLiveValue().toString(); }
			 * 
			 * }
			 */

			//final ConsignmentEntryModel consignmentEntry = consignmentModel.getConsignmentEntries().iterator().next();
			/**
			 * setting all the consignmentEntries as Shipped or PickedupAtStore or ReadyForPickup
			 * 
			 */
			final TypedObject consignmentModelTypedObject = (TypedObject) objectValueContainer.getObject();
			final ConsignmentModel consignmentModel = (ConsignmentModel) consignmentModelTypedObject.getObject();
			final ObjectValueHolder consignmentStatusObject = (((OrderUpdateController) widget.getWidgetController())
					.getPropertyValue(objectValueContainer, "Consignment.status"));
			if (consignmentModel.isPaymentCapture())
			{
				status = "SHIPPED";
			}
			else if (consignmentStatusObject != null)
			{
				if (consignmentStatusObject.getLiveValue() != null)
				{
					status = consignmentStatusObject.getLiveValue().toString();
				}
			}
			final ObjectValueHolder trackingIdObject = (((OrderUpdateController) widget.getWidgetController()).getPropertyValue(
					objectValueContainer, "Consignment.trackingId"));
			if (trackingIdObject.getLiveValue() != null && !trackingIdObject.getLiveValue().toString().isEmpty())
			{
				trackingId = trackingIdObject.getLiveValue().toString();
			}
			else
			{
				isTrackingIDUpdated = false;
			}
			for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
			{

				if (status != null && status.equals(ConsignmentStatus.SHIPPED.getCode())
						&& consignmentEntry.getEntryLevelStatus() != null
						&& !(consignmentEntry.getEntryLevelStatus().getCode().equals(ConsignmentEntryStatus.CANCELLED.getCode())))
				{
					consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.SHIPPED);

					if ((consignmentEntry.getConsignment().getCode().contains("online-") || consignmentEntry.getConsignment()
							.getCode().contains("dropship-")))
					{
						if ((consignmentEntry.getConsignment().getCode().contains("online-")))
						{
							processOnline = true;
						}
						else
						{
							processDropShip = true;
						}
						if (trackingId != null)
						{
							consignmentEntry.setTrackingId(trackingId);
						}

					}

					/**
					 * kept commented as shipping text box can come as a requirement in future.
					 */

					/*
					 * if (shippingAmt != null) { if (Double.valueOf(shippingAmt).doubleValue() < 0) { final List
					 * entryErrorMessages = new ArrayList(); entryErrorMessages.add(new
					 * ResourceMessage("Incorrect Shipping Amount", Arrays.asList(new Double[] { Double.valueOf(shippingAmt)
					 * }))); throw new ValidationException(entryErrorMessages); } else { remainingAmt =
					 * orderModel.getNewDeliveryCost().doubleValue() - Double.valueOf(shippingAmt).doubleValue(); if
					 * (remainingAmt < 0) { final List entryErrorMessages = new ArrayList(); entryErrorMessages.add(new
					 * ResourceMessage("Incorrect Shipping Amount", Arrays.asList(new Double[] { Double.valueOf(shippingAmt)
					 * }))); throw new ValidationException(entryErrorMessages);
					 * 
					 * } else { orderModel.setNewDeliveryCost(Double.valueOf(remainingAmt)); modelService.save(orderModel);
					 * modelService.refresh(orderModel); consignmentEntry.setShippingAmt(Double.valueOf(shippingAmt));
					 * modelService.save(consignmentEntry); modelService.refresh(consignmentEntry);
					 * 
					 * } } }
					 */
				}
				else if (status != null
						&& (status.equals(ConsignmentStatus.READY_FOR_PICKUP.getCode()) || status
								.equals(ConsignmentEntryStatus.PICKEDUP_AT_STORE.getCode()))
						&& consignmentEntry.getEntryLevelStatus() != null
						&& !(consignmentEntry.getEntryLevelStatus().getCode().equals(ConsignmentEntryStatus.CANCELLED.getCode())))
				{
					if (status.equals(ConsignmentStatus.PICKEDUP_AT_STORE.getCode()))
					{
						consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.PICKEDUP_AT_STORE);
					}
					else
					{
						consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.READY_FOR_PICKUP);
						final Calendar calendar = Calendar.getInstance();
						consignmentEntry.setReadyForPickupTimeStamp(calendar.getTime());
					}
					processBopis = true;
				}
				modelService.save(consignmentEntry);
				modelService.refresh(consignmentEntry);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Consignment fullfillment Process starts for : " + consignmentModel.getCode());
			}
			final int index = 0;
			if (!consignmentModel.isPaymentCapture())
			{
				if (processOnline || processDropShip || processBopis)
				{
					isEmailSent = false;
					triggerFulfillmentProcess(consignmentModel, orderModel, index);
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

				}
				else
				{
					if (isTrackingIDUpdated && status != null)
					{
						isEmailSent = false;
						triggerUpdateTrackingIdProcess(consignmentModel, orderModel, index);
					}

				}
			}
			else
			{
				if (isTrackingIDUpdated && isEmailSent)
				{
					updatedConsignments.add(consignmentModel);
				}
			}

		}
		if (!updatedConsignments.isEmpty() && isEmailSent)
		{
			triggerUpdateTrackingIdProcess(updatedConsignments.iterator().next(), orderModel, 0);
		}
		((OrderUpdateController) widget.getWidgetController()).dispatchEvent(widget.getControllerCtx(), widget, null);
		Executions.sendRedirect("");
	}

	public void triggerUpdateTrackingIdProcess(final ConsignmentModel consignmentModel, final OrderModel orderModel, int index)
	{
		final ConsignmentProcessModel subProcess = (ConsignmentProcessModel) businessProcessService.createProcess(
				orderModel.getCode() + Math.random() + "_" + (++index), "consignmentUpdateWithTrackingIdSubprocess");
		subProcess.setConsignment(consignmentModel);
		modelService.save(subProcess);
		modelService.save(orderModel);
		modelService.refresh(orderModel);
		businessProcessService.startProcess(subProcess);
	}

	public void triggerFulfillmentProcess(final ConsignmentModel consignmentModel, final OrderModel orderModel, int index)
	{
		orderModel.setTlogPerformed(false);
		final ConsignmentProcessModel subProcess = (ConsignmentProcessModel) businessProcessService.createProcess(
				orderModel.getCode() + Math.random() + "_" + (++index), "consignmentFulfilmentSubprocess");
		subProcess.setConsignment(consignmentModel);
		modelService.save(subProcess);
		modelService.save(orderModel);
		modelService.refresh(orderModel);
		businessProcessService.startProcess(subProcess);
	}

	protected HtmlBasedComponent createContentInternal(final InputWidget widget, final HtmlBasedComponent rootContainer)
	{
		final Div content = new Div();
		returnObjectValueContainers = new ArrayList();
		if (((OrderUpdateController) widget.getWidgetController()).canUpdate())
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
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "cantUpdate", new Object[0]));
			dummyLabel.setSclass("csCantReturn");
			dummyLabel.setParent(content);
		}

		return content;

	}

	private EventListener createUpdateRequestCreateEventListener(final InputWidget widget, final List returnObjectValueContainers)
	{
		return new UpdateRequestCreateEventListener(widget, returnObjectValueContainers, returnObjectValueContainers);
	}

	protected void renderListbox(final Listbox listBox, final InputWidget widget)
	{
		final ListWidgetModel widgetModel = (ListWidgetModel) widget.getWidgetModel();

		final OrderModel orderModel = (OrderModel) ((OrderUpdateController) widget.getWidgetController()).getCurrentOrder()
				.getObject();

		if (widgetModel != null)
		{

			final Map updatableOrderEntries = ((OrderUpdateController) widget.getWidgetController()).getUpdatableOrderEntries();

			if (updatableOrderEntries != null && !updatableOrderEntries.isEmpty())
			{
				try
				{

					final List columns = getColumnConfigurations(getListConfigurationCode(), getListConfigurationType());
					if (CollectionUtils.isNotEmpty(columns))
					{
						final Listhead headRow = new Listhead();
						headRow.setParent(listBox);
						/*
						 * final Listheader colEntryNoHeader = new Listheader(LabelUtils.getLabel(widget, "entryNumber", new
						 * Object[0])); colEntryNoHeader.setWidth("20px"); colEntryNoHeader.setParent(headRow);
						 */
						final Listheader colProductHeader = new Listheader(LabelUtils.getLabel(widget, "consignCode", new Object[0]));
						colProductHeader.setWidth("300px");
						colProductHeader.setParent(headRow);
						/*
						 * final Listheader colMaxQtyHeader = new Listheader(LabelUtils.getLabel(widget, "maxQty", new
						 * Object[0])); colMaxQtyHeader.setWidth("55px"); colMaxQtyHeader.setParent(headRow); final Listheader
						 * colBinLocation = new Listheader( LabelUtils.getLabel(widget, "warehouselocation", new Object[0]));
						 * colBinLocation.setWidth("145px"); colBinLocation.setParent(headRow);
						 */
						ColumnConfiguration col;
						Listheader colHeader;
						for (final Iterator iterator = columns.iterator(); iterator.hasNext(); colHeader.setTooltiptext(col.getName()))
						{
							col = (ColumnConfiguration) iterator.next();
							colHeader = new Listheader(getPropertyRendererHelper().getPropertyDescriptorName(col));
							if (colHeader.getLabel().equals(OshcscockpitConstants.TrackingId))
							{
								if (orderModel.getConsignments().size() == 1)
								{
									if (orderModel.getConsignments().iterator().next().getCode().contains("online-")
											|| orderModel.getConsignments().iterator().next().getCode().contains("dropship-"))
									{
										colHeader.setParent(headRow);
									}
								}
								else
								{
									colHeader.setParent(headRow);
								}
							}
							else
							{
								colHeader.setParent(headRow);
							}
						}

						final EditorConfiguration editorConf = CockpitUiConfigLoader.getEditorConfiguration(
								UISessionUtils.getCurrentSession(), getListEditorConfigurationCode(), getListConfigurationType());
						final List editorMapping = getPropertyEditorHelper().getAllEditorRowConfigurations(editorConf);
						final List orderEntries = new ArrayList(updatableOrderEntries.keySet());
						Collections.sort(orderEntries, TypedObjectOrderEntryNumberComparator.INSTANCE);
						for (final Iterator iterator1 = orderEntries.iterator(); iterator1.hasNext();)
						{
							final TypedObject item = (TypedObject) iterator1.next();
							final ConsignmentModel consignmentModel = (ConsignmentModel) item.getObject();
							/*
							 * final String warehouseNo = ((OshVariantProductModel) oem.getProduct()).getWarehouseBinLoc();
							 * final ConsignmentEntryModel consignmentEntryModel =
							 * oem.getConsignmentEntries().iterator().next(); final ConsignmentModel consignmentModel =
							 * consignmentEntryModel.getConsignment();
							 */
							final Listitem row = new Listitem();
							row.setSclass("listbox-row-item");
							row.setParent(listBox);
							final Listcell entryNoCell = new Listcell();
							entryNoCell.setParent(row);
							/*
							 * final Div entryNoDiv = new Div(); entryNoDiv.setParent(entryNoCell);
							 * entryNoDiv.setSclass("editorWidgetEditor"); final Label entryNoLabel = new
							 * Label(String.valueOf(((AbstractOrderEntryModel) item.getObject()) .getEntryNumber()));
							 * entryNoLabel.setParent(entryNoDiv);
							 */

							final Div consignCode = new Div();
							consignCode.setParent(entryNoCell);
							consignCode.setSclass("editorWidgetEditor");
							final Label consignCodeLabel = new Label(String.valueOf(((ConsignmentModel) item.getObject()).getCode()));
							consignCodeLabel.setParent(consignCode);
							/*
							 * final Listcell productCell = new Listcell(); productCell.setParent(row); final Div productDiv =
							 * new Div(); productDiv.setParent(productCell); productDiv.setSclass("editorWidgetEditor"); final
							 * List columnsOrderEntry = getColumnConfigurations(getProductInfoConfigurationCode(),
							 * item.getType() .getCode());
							 * getPropertyRendererHelper().buildPropertyValuesFromColumnConfigs(item, columnsOrderEntry,
							 * productDiv);
							 */
							/*
							 * final Listcell maxQtyCell = new Listcell(); maxQtyCell.setParent(row); final Div maxQtyDiv = new
							 * Div(); maxQtyDiv.setParent(maxQtyCell); maxQtyDiv.setSclass("editorWidgetEditor"); final Label
							 * maxQtyLabel = new Label(String.valueOf(updatableOrderEntries.get(item)));
							 * maxQtyLabel.setParent(maxQtyDiv);
							 *//*
								 * Below changes are for adding warehouse bin location into order update widget These are
								 * brought from OshVariant and have no entry in below XML files
								 * UpdateEntry_UpdateEntryEdit_CockpitGroup.xml,UpdateEntry_UpdateEntryList_CockpitGroup.xml
								 */
							/*
							 * final Listcell warehouseLoc = new Listcell(); warehouseLoc.setParent(row); final Div
							 * warehouseLocDiv = new Div(); warehouseLocDiv.setParent(warehouseLoc);
							 * warehouseLocDiv.setSclass("editorWidgetEditor"); final Label warehouseLocLabel = new
							 * Label(warehouseNo); warehouseLocLabel.setParent(warehouseLocDiv);
							 */
							/*
							 * warehouse bin location changes end
							 */
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

									if (consignmentModel.getCode().contains("online-") || consignmentModel.getCode().contains("dropship-"))
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
											if (consignmentModel.isPaymentCapture()
													&& propertyDescriptor.getName().equalsIgnoreCase("Status"))
											{
												final Label consignStatusLabel = new Label(String.valueOf(((ConsignmentModel) item
														.getObject()).getStatus()));
												consignStatusLabel.setParent(editorDiv);
											}
											else
											{
												renderEditor(editorDiv, editorRowConfiguration, returnEntryObject, returnEntryValueContainer,
														propertyDescriptor, widget);
												row.setSelected(true);
											}


										}
									}
									else
									{
										if (col1.getPropertyDescriptor().getQualifier().equals("Consignment.status"))
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
		if (rowConfig != null && rowConfig.isVisible())
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
					params);
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
