/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.converter.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 *
 */
public class OshWarehouseBinLocationConverter extends OshImpexConverter
{
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ModelService modelService;

	private static Logger LOG = Logger.getLogger(OshWarehouseBinLocationConverter.class);

	@Override
	protected void buildColumnValue(final char nextChar, final int idx, final int endIdx, final Map<Integer, String> row,
			final StringBuilder builder)
	{
		final boolean mandatory = nextChar == getPlusChar();
		Integer mapIdx = null;
		try
		{
			mapIdx = Integer.valueOf(getImpexRow().substring(mandatory ? idx + 2 : idx + 1, endIdx));
		}
		catch (final NumberFormatException e)
		{
			throw new SystemException("Invalid row syntax [invalid column number]: " + getImpexRow(), e);
		}
		String colValue = row.get(mapIdx);

		if (mapIdx.intValue() == 7)
		{
			if (colValue.equalsIgnoreCase("1570") || colValue.equalsIgnoreCase("NOSEL"))
			{
				colValue = "online";
			}
			else
			{
				colValue = "dropship";
			}
		}

		builder.append(colValue);
	}


}
