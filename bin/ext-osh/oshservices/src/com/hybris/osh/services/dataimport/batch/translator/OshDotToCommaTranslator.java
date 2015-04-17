/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

/**
 * 
 *
 */
public class OshDotToCommaTranslator implements CustomBatchColumnTranslator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator#translate(java.lang.String)
	 */
	@Override
	public String translate(final String colValue)
	{
		String updatedValue = null;
		if (colValue != null)
		{
			updatedValue = colValue.replace(".", ",");
		}
		return updatedValue;
	}

}
