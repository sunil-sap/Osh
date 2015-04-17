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
package com.hybris.osh.oshcscockpit.widgets.renderers.details.impl;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.AbstractConfigurableAutoTypeListboxWidgetDetailRenderer;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.map.Map;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;


/*
 * For customize line entry model and add dropdownbox.
 */
public class OshAbstractConfigurableAutoTypeListboxWidgetDetailRenderer extends
		AbstractConfigurableAutoTypeListboxWidgetDetailRenderer
{

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	protected String getListConfigurationCode()
	{
		return listConfigurationCode;
	}

	@Override
	public void setListConfigurationCode(final String listConfigurationCode)
	{
		this.listConfigurationCode = listConfigurationCode;
	}


	@Override
	public HtmlBasedComponent createContent(final Object arg0, final Object arg1, final Widget arg2)
	{

		return null;
	}

	protected class DefaultReturnStatusChangedEventListener implements EventListener
	{

		@Override
		public void onEvent(final Event event) throws Exception
		{
			if (event instanceof SelectEvent)
			{
				handleDefaultStatusChangedEvent(widget, (SelectEvent) event, item);
			}
		}

		private final transient Widget widget;
		final TypedObject item;

		public DefaultReturnStatusChangedEventListener(final Widget widget, final TypedObject item)
		{
			this.item = item;
			this.widget = widget;
		}
	}

	protected void createContentList(final HtmlBasedComponent parent, final Object context, final Collection entries,
			final String listboxStyle, final Widget widget)
	{
		final List columns = getColumnConfigurations(entries);
		createContentList(parent, context, entries, columns, listboxStyle, widget);
	}

	protected void createContentList(final HtmlBasedComponent parent, final Object context, final Collection entries,
			final List columns, final String listboxStyle, final Widget widget)
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
		for (final Iterator iterator = entries.iterator(); iterator.hasNext(); renderRefundListEntry(context, entry, listBox,
				columns, widget))
		{
			entry = (ItemModel) iterator.next();
		}
	}

	protected void renderRefundListEntry(final Object context, final ItemModel entry, final Listbox listBox, final List columns,
			final Widget widget)
	{
		final TypedObject entryTypedObject = getCockpitTypeService().wrapItem(entry);
		final Listitem row = new Listitem();
		row.setParent(listBox);
		prependDataRow(row, entryTypedObject);
		populateRefundDataRow(row, columns, entryTypedObject, widget);

	}


	protected void populateRefundDataRow(final Listitem row, final List columns, final TypedObject item, final Widget widget)
	{
		if (CollectionUtils.isNotEmpty(columns))
		{
			Listcell cell = null;
			for (final Object obj : columns.toArray())
			{
				final ColumnConfiguration col = (ColumnConfiguration) obj;

				if (col.getColumnDescriptor().getName().equalsIgnoreCase("Status"))
				{
					cell = new Listcell();
					renderRefundStatus(col, cell, item, widget);
					cell.setParent(row);
				}
				else
				{
					final String value = ObjectGetValueUtils.getValue(col.getValueHandler(), item);
					cell = new Listcell(value);
					cell.setParent(row);

				}
			}

		}

	}

	protected void renderRefundStatus(final ColumnConfiguration col, final org.zkoss.zk.ui.HtmlBasedComponent cell,
			final TypedObject item, final Widget widget)
	{

		final Listbox returnStatusDropdown = new Listbox();
		returnStatusDropdown.setMold("select");
		returnStatusDropdown.setRows(1);
		returnStatusDropdown.setParent(cell);
		Listitem refundModeItem;

		final List<ReturnStatus> statusList = enumerationService.getEnumerationValues(ReturnStatus.class);
		final String value = ObjectGetValueUtils.getValue(col.getValueHandler(), item);
		if (!statusList.isEmpty())
		{
			for (final ReturnStatus status : statusList)
			{

				if (status != null)
				{
					final String returtnStatusText = TypeTools.getValueAsString(getLabelService(), status);
					refundModeItem = new Listitem(returtnStatusText, statusList);
					refundModeItem.setParent(returnStatusDropdown);
					if (value.equalsIgnoreCase(returtnStatusText))
					{
						refundModeItem.setSelected(true);

					}
				}
			}
			returnStatusDropdown.addEventListener("onSelect", createhandleDefaultStatusChangedEvent(widget, item));
		}
	}

	private EventListener createhandleDefaultStatusChangedEvent(final Widget widget, final TypedObject item)
	{
		return new DefaultReturnStatusChangedEventListener(widget, item);

	}

	@Override
	protected void populateHeaderRow(final Listhead row, final List columns)
	{
		if (CollectionUtils.isNotEmpty(columns))
		{
			Listheader header;
			for (final Iterator iterator = columns.iterator(); iterator.hasNext(); row.appendChild(header))
			{
				final ColumnConfiguration col = (ColumnConfiguration) iterator.next();
				header = new Listheader(getPropertyRendererHelper().getPropertyDescriptorName(col));
				header.setTooltiptext(col.getName());
				header.setSclass((new StringBuilder("csProp")).append(CssUtils.sanitizeForCss(col.getName())).toString());
			}
			header = new Listheader("");
			header.setTooltiptext("");
			header.setSclass((new StringBuilder("csProp")).append(CssUtils.sanitizeForCss("")).toString());
		}

	}

	protected void handleDefaultStatusChangedEvent(final Widget widget, final SelectEvent selectEvent, final TypedObject items)
	{
		final String selectedStatus = ((Listcell) selectEvent.getReference().getLastChild()).getLabel();
		final RefundEntryModel entry = (RefundEntryModel) items.getObject();
		if (!entry.getStatus().equals(selectedStatus))
		{
			entry.setStatus(enumerationService.getEnumerationValue(ReturnStatus.class, selectedStatus));
			modelService.save(entry);
			modelService.refresh(entry);
		}
	}


	protected static final String CSS_HEADER_SUFFIX = "Header";
	private String listConfigurationCode;
	private String listConfigurationType;
	protected transient List returnObjectValueContainers;
	private Map defaultPropertyValuesMap;


	public Map getDefaultPropertyValuesMap()
	{
		return defaultPropertyValuesMap;
	}


	public void setDefaultPropertyValuesMap(final Map defaultPropertyValuesMap)
	{
		this.defaultPropertyValuesMap = defaultPropertyValuesMap;
	}

	public String getListConfigurationType()
	{
		return listConfigurationType;
	}

	public void setListConfigurationType(final String listConfigurationType)
	{
		this.listConfigurationType = listConfigurationType;
	}

	private LabelService labelService;


	public LabelService getLabelService()
	{
		return labelService;
	}


	public void setLabelService(final LabelService labelService)
	{
		this.labelService = labelService;
	}

	@Override
	public SystemService getSystemService()
	{
		return systemService;
	}


	@Override
	public void setSystemService(final SystemService systemService)
	{
		this.systemService = systemService;
	}

	private SystemService systemService;


}
