package com.hybris.osh.oshcscockpit.widget.controller.impl;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.CancelableOrderDenialReasonsException;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.CancellationController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCancellationController;
import de.hybris.platform.ordercancel.CancelDecision;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.ordercancel.OshOrderCancelEntry;


public class DefaultOshCancellationController extends AbstractCsWidgetController implements CancellationController
{
	private static final Logger LOG = Logger.getLogger(DefaultCancellationController.class);
	private OrderManagementActionsWidgetController orderManagementActionsWidgetController;
	private OrderCancelService orderCancelService;
	private UserService userService;

	protected OrderManagementActionsWidgetController getOrderManagementActionsWidgetController()
	{
		return this.orderManagementActionsWidgetController;
	}

	@Required
	public void setOrderManagementActionsWidgetController(
			final OrderManagementActionsWidgetController orderManagementActionsWidgetController)
	{
		this.orderManagementActionsWidgetController = orderManagementActionsWidgetController;
	}

	protected OrderCancelService getOrderCancelService()
	{
		return this.orderCancelService;
	}

	@Required
	public void setOrderCancelService(final OrderCancelService orderCancelService)
	{
		this.orderCancelService = orderCancelService;
	}

	protected UserService getUserService()
	{
		return this.userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
		getOrderManagementActionsWidgetController().dispatchEvent(null, source, data);
	}

	public TypedObject getOrder()
	{
		return getOrderManagementActionsWidgetController().getOrder();
	}

	public Map<TypedObject, Long> getCancelableOrderEntries() throws CancelableOrderDenialReasonsException
	{
		final Map cancelableObjects = new HashMap();

		final TypedObject order = getOrder();
		if ((order != null) && ((order.getObject() instanceof OrderModel)))
		{
			final OrderModel orderModel = (OrderModel) order.getObject();

			final CancelDecision cancelable = getOrderCancelService().isCancelPossible(orderModel,
					getUserService().getCurrentUser(), true, true);
			if ((cancelable != null) && (cancelable.isAllowed()))
			{
				return convert(getOrderCancelService().getAllCancelableEntries(orderModel, getUserService().getCurrentUser()));
			}

			throw new CancelableOrderDenialReasonsException("Unable to cancel order",
					cancelable != null ? cancelable.getDenialReasons() : null);
		}

		return cancelableObjects;
	}

	protected Map<TypedObject, Long> convert(final Map<AbstractOrderEntryModel, Long> cancelableOrderEntries)
	{
		final Map cancelableObjects = new HashMap();
		if (cancelableOrderEntries != null)
		{
			for (final AbstractOrderEntryModel modelObj : cancelableOrderEntries.keySet())
			{
				final Long value = cancelableOrderEntries.get(modelObj);
				cancelableObjects.put(getCockpitTypeService().wrapItem(modelObj), value);
			}
		}
		return cancelableObjects;
	}

	protected OrderCancelRequest buildCancelRequest(final OrderModel order,
			final Map<TypedObject, CancelEntryDetails> entriesToCancel, final ObjectValueContainer cancelRequest)
	{
		final String notes = (String) getPropertyValue(cancelRequest, "OrderCancelRequest.notes").getCurrentValue();
		final CancelReason reason = (CancelReason) getPropertyValue(cancelRequest, "OrderCancelRequest.cancelReason")
				.getCurrentValue();

		if ((order != null) && (entriesToCancel != null) && (!entriesToCancel.isEmpty()))
		{
			final List orderCancelEntries = new ArrayList();

			for (final TypedObject orderEntry : entriesToCancel.keySet())
			{
				if (!(orderEntry.getObject() instanceof OrderEntryModel))
				{
					continue;
				}
				final OrderEntryModel entryModel = (OrderEntryModel) orderEntry.getObject();
				final CancelEntryDetails cancelEntry = entriesToCancel.get(orderEntry);

				final OshOrderCancelEntry orderCancelEntry = new OshOrderCancelEntry(entryModel, cancelEntry.getQuantity(),
						cancelEntry.getNotes(), cancelEntry.getReason());
				orderCancelEntries.add(orderCancelEntry);
			}

			final OrderCancelRequest orderCancelRequest = new OrderCancelRequest(order, orderCancelEntries);
			orderCancelRequest.setCancelReason(reason);
			orderCancelRequest.setNotes(notes);
			return orderCancelRequest;
		}

		return null;
	}

	protected boolean validateCreateCancellationRequest(final OrderModel orderModel,
			final Map<TypedObject, Long> cancelableOrderEntries, final List<ObjectValueContainer> orderEntryCancelRecords)
			throws ValidationException
	{
		long okCount = 0L;
		final List errorMessages = new ArrayList();

		for (final ObjectValueContainer ovc : orderEntryCancelRecords)
		{
			final List entryErrorMessages = new ArrayList();
			boolean entryProcessed = false;

			final TypedObject orderEntry = (TypedObject) ovc.getObject();
			final int entryNumber = SafeUnbox.toInt(((AbstractOrderEntryModel) orderEntry.getObject()).getEntryNumber());
			if (cancelableOrderEntries.containsKey(orderEntry))
			{
				final ObjectValueContainer.ObjectValueHolder cancelRequestQuantity = getPropertyValue(ovc,
						"OrderCancelEntry.cancelQuantity");
				final ObjectValueContainer.ObjectValueHolder shipRequestQuantity = getPropertyValue(ovc,
						"OshOrderCancelEntry.shippingAmt");
				if ((shipRequestQuantity != null) && ((shipRequestQuantity.getCurrentValue() instanceof Double)))
				{
					final double shipRequestQuantityValue = SafeUnbox.toDouble((Double) shipRequestQuantity.getCurrentValue());

					if (shipRequestQuantityValue < 0)
					{
						entryErrorMessages.add(new ResourceMessage("cancelRecordEntry.validation.shipRequestQuantity.negative", Arrays
								.asList(new Integer[]
								{ Integer.valueOf(entryNumber) })));
					}

					if ((shipRequestQuantityValue > 0)
							&& (shipRequestQuantityValue * SafeUnbox.toLong((Long) cancelRequestQuantity.getCurrentValue()) > SafeUnbox
									.toDouble(orderModel.getNewDeliveryCost())))
					{
						entryErrorMessages.add(new ResourceMessage("cancelRecordEntry.validation.shipRequestQuantity.gtMaxQty", Arrays
								.asList(new Integer[]
								{ Integer.valueOf(entryNumber) })));
					}
					entryProcessed = true;
				}


				if ((cancelRequestQuantity != null) && ((cancelRequestQuantity.getCurrentValue() instanceof Long)))
				{
					final long cancelRequestQuantityValue = SafeUnbox.toLong((Long) cancelRequestQuantity.getCurrentValue());
					if (cancelRequestQuantityValue == 0L)
					{
						entryErrorMessages.add(new ResourceMessage("cancelRecordEntry.validation.cancelRequestQuantity.zeroQty", Arrays
								.asList(new Integer[]
								{ Integer.valueOf(entryNumber) })));
					}

					if (cancelRequestQuantityValue < 0L)
					{
						entryErrorMessages.add(new ResourceMessage("cancelRecordEntry.validation.cancelRequestQuantity.negative",
								Arrays.asList(new Integer[]
								{ Integer.valueOf(entryNumber) })));
					}

					if ((cancelRequestQuantityValue > 0L)
							&& (cancelRequestQuantityValue > SafeUnbox.toLong(cancelableOrderEntries.get(orderEntry))))
					{
						entryErrorMessages.add(new ResourceMessage("cancelRecordEntry.validation.cancelRequestQuantity.gtMaxQty",
								Arrays.asList(new Integer[]
								{ Integer.valueOf(entryNumber) })));
					}
					entryProcessed = true;
				}

			}
			else
			{
				entryErrorMessages.add(new ResourceMessage("cancelRecordEntry.validation.product.notCancelable", Arrays
						.asList(new Integer[]
						{ Integer.valueOf(entryNumber) })));
			}

			if (!entryErrorMessages.isEmpty())
			{
				errorMessages.addAll(entryErrorMessages);
			}

			if ((!entryProcessed) || (!entryErrorMessages.isEmpty()))
			{
				continue;
			}
			okCount += 1L;
		}

		if ((okCount == 0L) && (errorMessages.isEmpty()))
		{
			errorMessages.add(new ResourceMessage("cancellationRequest.validation.noneSelected"));
		}

		if (!errorMessages.isEmpty())
		{
			throw new ValidationException(errorMessages);
		}

		return okCount > 0L;
	}

	protected ObjectValueContainer.ObjectValueHolder getPropertyValue(final ObjectValueContainer ovc,
			final String propertyQualifier)
	{
		final PropertyDescriptor pdReason = getPropertyDescriptor(ovc.getPropertyDescriptors(), propertyQualifier);
		if (pdReason != null)
		{
			return ovc.getValue(pdReason, null);
		}

		return null;
	}

	protected PropertyDescriptor getPropertyDescriptor(final Set<PropertyDescriptor> properties, final String qualifier)
	{
		if (properties != null)
		{
			for (final PropertyDescriptor pd : properties)
			{
				if (pd.getQualifier().equals(qualifier))
				{
					return pd;
				}
			}
		}

		return null;
	}

	@Override
	public TypedObject createPartialOrderCancellationRequest(final List<ObjectValueContainer> orderEntryCancelRecords,
			final ObjectValueContainer cancelRequest) throws OrderCancelException, ValidationException
	{
		final TypedObject order = getOrder();
		if ((order != null) && ((order.getObject() instanceof OrderModel)))
		{
			final OrderModel orderModel = (OrderModel) order.getObject();
			try
			{
				final Map cancelableOrderEntries = getCancelableOrderEntries();
				if (validateCreateCancellationRequest(orderModel, cancelableOrderEntries, orderEntryCancelRecords))
				{
					final Map cancelEntries = new HashMap();

					for (final ObjectValueContainer ovc : orderEntryCancelRecords)
					{
						final TypedObject orderEntry = (TypedObject) ovc.getObject();
						if (!cancelableOrderEntries.containsKey(orderEntry))
						{
							continue;
						}
						final long cancelRequestQuantity = SafeUnbox.toLong((Long) getPropertyValue(ovc,
								"OrderCancelEntry.cancelQuantity").getCurrentValue());
						final CancelReason reason = (CancelReason) getPropertyValue(ovc, "OrderCancelEntry.cancelReason")
								.getCurrentValue();
						final String notes = (String) getPropertyValue(ovc, "OrderCancelEntry.notes").getCurrentValue();
						//final Double shippingAmt = (Double) getPropertyValue(ovc, "OshOrderCancelEntry.shippingAmt").getCurrentValue();
						if ((cancelRequestQuantity <= 0L)
								|| (cancelRequestQuantity > SafeUnbox.toLong((Long) cancelableOrderEntries.get(orderEntry))))
						{
							continue;
						}
						final CancelEntryDetails cancelEntry = new CancelEntryDetails(orderEntry, cancelRequestQuantity, reason, notes);
						cancelEntries.put(orderEntry, cancelEntry);
					}

					final OrderCancelRequest request = buildCancelRequest(orderModel, cancelEntries, cancelRequest);
					if (request != null)
					{
						final OrderCancelRecordEntryModel orderRequestRecord = getOrderCancelService().requestOrderCancel(request,
								getUserService().getCurrentUser());

						if (OrderCancelEntryStatus.DENIED.equals(orderRequestRecord.getCancelResult()))
						{
							final String orderCode = ((OrderModel) order.getObject()).getCode();

							String message = "";
							if (orderRequestRecord.getRefusedMessage() != null)
							{
								message = message + orderRequestRecord.getRefusedMessage();
							}
							if (orderRequestRecord.getFailedMessage() != null)
							{
								message = message + orderRequestRecord.getFailedMessage();
							}

							throw new OrderCancelException(orderCode, message);
						}

						return getCockpitTypeService().wrapItem(orderRequestRecord);
					}
				}
			}
			catch (final CancelableOrderDenialReasonsException e)
			{
				throw new OrderCancelException(orderModel.getCode(), "Failed to cancel", e);
			}
		}

		return null;
	}

	protected OrderCancelRequest buildCancelRequest(final OrderModel order, final ObjectValueContainer cancelRequest)
	{
		final String notes = (String) getPropertyValue(cancelRequest, "OrderCancelRequest.notes").getCurrentValue();
		final CancelReason reason = (CancelReason) getPropertyValue(cancelRequest, "OrderCancelRequest.cancelReason")
				.getCurrentValue();

		if (order != null)
		{
			final OrderCancelRequest orderCancelRequest = new OrderCancelRequest(order);
			orderCancelRequest.setCancelReason(reason);
			orderCancelRequest.setNotes(notes);
			return orderCancelRequest;
		}

		return null;
	}

	protected boolean validateCreateCancellationRequest(final OrderModel orderModel, final ObjectValueContainer cancelRequest)
			throws ValidationException
	{
		final List errorMessages = new ArrayList();

		final CancelReason reason = (CancelReason) getPropertyValue(cancelRequest, "OrderCancelRequest.cancelReason")
				.getCurrentValue();
		if (reason == null)
		{
			errorMessages.add(new ResourceMessage("cancelRecordEntry.validation.cancelRequestReason.missingReason"));
		}

		if (!errorMessages.isEmpty())
		{
			throw new ValidationException(errorMessages);
		}

		return true;
	}

	public TypedObject createOrderCancellationRequest(final ObjectValueContainer cancelRequest) throws OrderCancelException,
			ValidationException
	{
		final TypedObject order = getOrder();
		if ((order != null) && ((order.getObject() instanceof OrderModel)))
		{
			final OrderModel orderModel = (OrderModel) order.getObject();
			if (validateCreateCancellationRequest(orderModel, cancelRequest))
			{
				try
				{
					final OrderCancelRequest request = buildCancelRequest(orderModel, cancelRequest);
					if (request != null)
					{
						final OrderCancelRecordEntryModel orderRequestRecord = getOrderCancelService().requestOrderCancel(request,
								getUserService().getCurrentUser());

						if (OrderCancelEntryStatus.DENIED.equals(orderRequestRecord.getCancelResult()))
						{
							final String orderCode = ((OrderModel) order.getObject()).getCode();

							String message = "";
							if (orderRequestRecord.getRefusedMessage() != null)
							{
								message = message + orderRequestRecord.getRefusedMessage();
							}
							if (orderRequestRecord.getFailedMessage() != null)
							{
								message = message + orderRequestRecord.getFailedMessage();
							}

							throw new OrderCancelException(orderCode, message);
						}

						return getCockpitTypeService().wrapItem(orderRequestRecord);
					}

				}
				catch (final OrderCancelException ocEx)
				{
					throw ocEx;
				}
				catch (final Exception e)
				{
					LOG.warn("Failed to cancel [" + orderModel + "]", e);
					throw new OrderCancelException("Failed to cancel", e);
				}
			}
		}

		return null;
	}

	public static class CancelEntryDetails
	{
		private final TypedObject entry;
		private final CancelReason reason;
		private final String notes;
		private final long quantity;

		//private final double shippingAmt;

		public CancelEntryDetails(final TypedObject entry, final long quantity, final CancelReason reason, final String notes)
		{
			this.entry = entry;
			this.quantity = quantity;
			this.reason = reason;
			this.notes = notes;
			//this.shippingAmt = shippingAmt;
		}

		public TypedObject getEntry()
		{
			return this.entry;
		}

		public CancelReason getReason()
		{
			return this.reason;
		}

		public long getQuantity()
		{
			return this.quantity;
		}

		public String getNotes()
		{
			return this.notes;
		}

		/*
		 * public double getShippingAmt() { return this.shippingAmt; }
		 */
	}
}
