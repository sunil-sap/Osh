/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

/**
 *
 *
 */
public class MapPriceTranslator implements CustomBatchColumnTranslator
{
	@Override
	public String translate(final String colValue)
	{
		if (colValue != null)
		{
			if (colValue.equals("0"))
			{
				return "false";
			}
			else
			{
				return "true";
			}
		}
		return colValue;
	}

}
