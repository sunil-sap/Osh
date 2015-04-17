/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

/**
 * 
 *
 */
public class ApprovedStatusTranslator implements CustomBatchColumnTranslator
{
	@Override
	public String translate(final String colValue)
	{
		if (colValue != null)
		{
			if (colValue.startsWith("P"))
			{
				return "approved";
			}
			else
			{
				return "unapproved";
			}
		}
		return colValue;
	}

}
