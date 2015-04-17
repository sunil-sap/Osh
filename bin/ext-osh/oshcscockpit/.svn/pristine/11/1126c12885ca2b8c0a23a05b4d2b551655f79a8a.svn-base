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
package com.hybris.osh.oshcockpit.widgets.renderers.details.impl;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.AbstractWidgetDetailRenderer;
import de.hybris.platform.enumeration.EnumerationService;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;


public class OshAbstractWidgetDetailRenderer extends AbstractWidgetDetailRenderer
{
	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;



	private LabelService labelService;


	public LabelService getLabelService()
	{
		return labelService;
	}


	public void setLabelService(final LabelService labelService)
	{
		this.labelService = labelService;
	}


	protected void populateRefundDataRow(final Listitem row, final List columns, final TypedObject item)
	{
		if (CollectionUtils.isNotEmpty(columns))
		{
			Listcell cell;
			for (final Iterator iterator = columns.iterator(); iterator.hasNext(); cell.setParent(row))
			{
				final Listbox returnStatusDropdown = new Listbox();
				returnStatusDropdown.setMold("select");
				returnStatusDropdown.setRows(1);
				//returnStatusDropdown.setParent(parent);
				final ColumnConfiguration col = (ColumnConfiguration) iterator.next();
				final String value = ObjectGetValueUtils.getValue(col.getValueHandler(), item);

				cell = new Listcell(value);


				final List<ReturnStatus> status = enumerationService.getEnumerationValues(ReturnStatus.class);
				if (status != null && !status.isEmpty())
				{
					for (int i = 0; i < status.size(); i++)
					{
						final Object t = status.get(i);
						if (t != null)
						{
							final String deliveryModeText = TypeTools.getValueAsString(getLabelService(), t);
							final Listitem deliveryModeItem = new Listitem(deliveryModeText, t);
							deliveryModeItem.setParent(returnStatusDropdown);
							deliveryModeItem.setSelected(true);
						}
					}
				}
			}

		}
	}


	@Override
	public HtmlBasedComponent createContent(final Object arg0, final Object arg1, final Widget arg2)
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public Object createContext()
	{
		// YTODO Auto-generated method stub
		return null;
	}

}
