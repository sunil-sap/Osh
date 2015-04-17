/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * 
 *
 */
public class OshDateColumnTranslator implements CustomBatchColumnTranslator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator#translate(java.lang.String)
	 */
	@Override
	public String translate(final String colValue)
	{
		final SimpleDateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));
		final SimpleDateFormat gmtFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
		gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String gmtdate = null;
		Date pstDate = null;
		try
		{
			pstDate = pstFormat.parse(colValue);
			gmtdate = gmtFormat.format(pstDate);
		}
		catch (final ParseException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return gmtdate;
	}

}
