/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;



/**
 * 
 */
public class OshOnlineInventoryColumnTranslator implements CustomBatchColumnTranslator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator#translate(java.lang.String)
	 */
	@Override
	public String translate(final String colValue)
	{

		if (colValue.equalsIgnoreCase("1570") || colValue.equalsIgnoreCase("NOSEL"))
		{
			return "online";
		}
		else
		{
			return "dropship";
		}




	}
}
