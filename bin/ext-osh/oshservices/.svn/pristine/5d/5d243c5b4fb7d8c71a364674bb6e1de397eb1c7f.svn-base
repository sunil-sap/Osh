/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

/**
 * convert price row
 * 
 */
public class OshPriceColumnTranslator implements CustomBatchColumnTranslator
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
			if (colValue.equals("PERM"))
			{
				return "Osh_RegularPrice";
			}
			else if (colValue.equals("TEMP"))
			{
				return "Osh_SalesPrice";
			}
		}
		return colValue;
	}

}
