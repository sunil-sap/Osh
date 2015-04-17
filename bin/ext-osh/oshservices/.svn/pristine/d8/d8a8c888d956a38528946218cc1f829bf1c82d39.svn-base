/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

/**
 *
 *
 */
public class AvailabilityIndTranslator implements CustomBatchColumnTranslator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator#translate(java.lang.String)
	 */
	@Override
	public String translate(final String colValue)
	{
		if (colValue != null)
		{
			if (colValue.equals("A"))
			{
				return "ALL";
			}
			else if (colValue.equals("S"))
			{
				return "STORE";
			}
			else if (colValue.equals("W"))
			{
				return "WEB";
			}
		}
		return colValue;
	}

}
