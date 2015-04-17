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
package com.hybris.osh.ordercancel;

import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class DefaultOshOrderCancelRecordsHandler extends DefaultOrderCancelRecordsHandler
{
	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Override
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	private UserService userService;

	@Override
	protected OrderCancelRecordEntryModel createCancelRecordEntry(final OrderCancelRequest request, final OrderModel order,
			final OrderCancelRecordModel cancelRecord, final OrderHistoryEntryModel snapshot, final Map originalOrderEntriesMapping)
			throws OrderCancelRecordsHandlerException
	{
		final OrderCancelRecordEntryModel cancelRecordEntry = (OrderCancelRecordEntryModel) getModelService().create(
				OrderCancelRecordEntryModel.class);
		cancelRecordEntry.setTimestamp(new Date());
		cancelRecordEntry.setCode(generateEntryCode(snapshot));
		cancelRecordEntry.setOriginalVersion(snapshot);
		cancelRecordEntry.setModificationRecord(cancelRecord);
		cancelRecordEntry.setPrincipal(userService.getCurrentUser());
		cancelRecordEntry.setOwner(cancelRecord);
		cancelRecordEntry.setStatus(OrderModificationEntryStatus.INPROGRESS);
		cancelRecordEntry.setCancelResult(request.isPartialCancel() ? OrderCancelEntryStatus.PARTIAL : OrderCancelEntryStatus.FULL);
		cancelRecordEntry.setCancelReason(request.getCancelReason());
		cancelRecordEntry.setNotes(request.getNotes());
		getModelService().save(cancelRecordEntry);
		final List orderEntriesRecords = new ArrayList();
		OrderEntryCancelRecordEntryModel orderEntryRecordEntry;
		for (final Iterator iterator = request.getEntriesToCancel().iterator(); iterator.hasNext(); orderEntriesRecords
				.add(orderEntryRecordEntry))
		{
			final Object cancelRequestEntry = iterator.next();
			if (cancelRequestEntry instanceof OshOrderCancelEntry)
			{
				orderEntryRecordEntry = (OrderEntryCancelRecordEntryModel) getModelService().create(
						OrderEntryCancelRecordEntryModel.class);
				orderEntryRecordEntry.setCode(((OshOrderCancelEntry) cancelRequestEntry).getOrderEntry().getPk().toString());
				orderEntryRecordEntry.setCancelRequestQuantity(Integer.valueOf((int) ((OshOrderCancelEntry) cancelRequestEntry)
						.getCancelQuantity()));
				orderEntryRecordEntry.setModificationRecordEntry(cancelRecordEntry);
				orderEntryRecordEntry.setOrderEntry((OrderEntryModel) ((OshOrderCancelEntry) cancelRequestEntry).getOrderEntry());
				orderEntryRecordEntry.setOriginalOrderEntry(getOriginalOrderEntry(originalOrderEntriesMapping,
						(OrderCancelEntry) cancelRequestEntry));
				orderEntryRecordEntry.setNotes(((OrderCancelEntry) cancelRequestEntry).getNotes());
				orderEntryRecordEntry.setCancelReason(((OshOrderCancelEntry) cancelRequestEntry).getCancelReason());
				//orderEntryRecordEntry.setShippingAmt(Double.valueOf(((OshOrderCancelEntry) cancelRequestEntry).getShippingAmt()));
				getModelService().save(orderEntryRecordEntry);

			}
			else
			{
				orderEntryRecordEntry = (OrderEntryCancelRecordEntryModel) getModelService().create(
						OrderEntryCancelRecordEntryModel.class);
				orderEntryRecordEntry.setCode(((OrderCancelEntry) cancelRequestEntry).getOrderEntry().getPk().toString());
				orderEntryRecordEntry.setCancelRequestQuantity(Integer.valueOf((int) ((OrderCancelEntry) cancelRequestEntry)
						.getCancelQuantity()));
				orderEntryRecordEntry.setModificationRecordEntry(cancelRecordEntry);
				orderEntryRecordEntry.setOrderEntry((OrderEntryModel) ((OrderCancelEntry) cancelRequestEntry).getOrderEntry());
				orderEntryRecordEntry.setOriginalOrderEntry(getOriginalOrderEntry(originalOrderEntriesMapping,
						(OrderCancelEntry) cancelRequestEntry));
				orderEntryRecordEntry.setNotes(((OrderCancelEntry) cancelRequestEntry).getNotes());
				orderEntryRecordEntry.setCancelReason(((OrderCancelEntry) cancelRequestEntry).getCancelReason());
				getModelService().save(orderEntryRecordEntry);
			}

		}

		cancelRecordEntry.setOrderEntriesModificationEntries(orderEntriesRecords);
		getModelService().save(cancelRecordEntry);
		return cancelRecordEntry;
	}
}
