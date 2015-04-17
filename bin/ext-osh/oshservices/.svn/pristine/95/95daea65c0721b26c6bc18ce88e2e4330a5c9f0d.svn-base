/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

/**
 * 
 *
 */
public class OshPriceValueColumnTranslator implements CustomBatchColumnTranslator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator#translate(java.lang.String)
	 */
	@Override
	public String translate(final String colValue)
	{
		String updatedPriceValue = null;
		if (colValue != null)
		{
			updatedPriceValue = colValue.replace(".", ",");
		}
		return updatedPriceValue;
	}

}
