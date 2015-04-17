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
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.AbstractConfigurableListboxWidgetDetailRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listitem;


public class OshOrderConsignmentsWidgetDetailRenderer extends AbstractConfigurableListboxWidgetDetailRenderer
{
	private static final Logger LOG = Logger.getLogger(OshOrderConsignmentsWidgetDetailRenderer.class);
	final String STATUS = "ConsignmentEntry.consignment.status";
	protected static final String CSS_ORDER_DETAILS_LABEL = "csOrderDetailsLabel";
	protected static final String CSS_ORDER_DETAILS_CONSIGNMENTS = "csOrderDetailsConsignments";

	public OshOrderConsignmentsWidgetDetailRenderer()
	{
	}

	public HtmlBasedComponent createContent(final Object context, final TypedObject item, final Widget widget)
	{
		if (!(item.getObject() instanceof ConsignmentModel))
		{
			LOG.error((new StringBuilder("Detail unexpected type. Expected ConsignmentModel for [")).append(item).append("]")
					.toString());
			return null;
		}
		final ConsignmentModel consignmentModel = (ConsignmentModel) item.getObject();
		final Set consignments = consignmentModel.getConsignmentEntries();
		if (consignments != null && !consignments.isEmpty())
		{
			final Div detailContainer = new Div();
			final Label orderDetailsLabel = new Label(LabelUtils.getLabel("cscockpit.widget.customer.orderconsignments",
					"orderDetails", new Object[0]));
			orderDetailsLabel.setSclass("csOrderDetailsLabel");
			orderDetailsLabel.setParent(detailContainer);
			final java.util.List consignmentsList = new ArrayList(consignments);
			createContentList(detailContainer, context, consignmentsList, "csOrderDetailsConsignments");
			return detailContainer;
		}
		else
		{
			return null;
		}
	}

	@Override
	protected void createContentList(final HtmlBasedComponent parent, final Object context, final Collection entries,
			final String listboxStyle)
	{
		final List columns = getColumnConfigurations(entries);
		createContentList(parent, context, entries, columns, listboxStyle);
	}


	@Override
	protected void createContentList(final HtmlBasedComponent parent, final Object context, final Collection entries,
			final List columns, final String listboxStyle)
	{
		final Listbox listBox = new Listbox();
		listBox.setParent(parent);
		final Listhead headRow = new Listhead();
		headRow.setParent(listBox);
		if (listboxStyle != null)
		{
			listBox.setSclass(listboxStyle);
			headRow.setSclass((new StringBuilder(String.valueOf(listboxStyle))).append("Header").toString());
		}
		prependHeader(headRow);
		populateHeaderRow(headRow, columns);
		ItemModel entry;
		for (final Iterator iterator = entries.iterator(); iterator.hasNext(); renderListEntry(context, entry, listBox, columns))
		{
			entry = (ItemModel) iterator.next();
		}

	}

	@Override
	protected void renderListEntry(final Object context, final ItemModel entry, final Listbox listBox, final List columns)
	{
		final TypedObject entryTypedObject = getCockpitTypeService().wrapItem(entry);
		final Listitem row = new Listitem();
		row.setParent(listBox);
		prependDataRow(row, entryTypedObject);
		populateDataRow(row, columns, entryTypedObject);
	}

	@Override
	protected void populateDataRow(final Listitem row, final List columns, final TypedObject item)
	{
		if (CollectionUtils.isNotEmpty(columns))
		{
			Listcell cell;
			for (final Iterator iterator = columns.iterator(); iterator.hasNext(); cell.setParent(row))
			{
				String value = null;
				final ColumnConfiguration col = (ColumnConfiguration) iterator.next();
				if (col.getName().equalsIgnoreCase(STATUS))
				{
					final ConsignmentEntryModel consignmentEntryModel = (ConsignmentEntryModel) item.getObject();
					value = consignmentEntryModel.getEntryLevelStatus().getCode();
				}
				else
				{
					value = ObjectGetValueUtils.getValue(col.getValueHandler(), item);
				}
				cell = new Listcell(value);
			}

		}
	}


	@Override
	public HtmlBasedComponent createContent(final Object obj, final Object obj1, final Widget widget)
	{
		return createContent(obj, (TypedObject) obj1, widget);
	}


}
