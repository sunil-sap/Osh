/**
 * 
 */
package com.hybris.osh.core.service;

import java.util.HashMap;
import java.util.Properties;


public interface OshTaxService
{

	/**
	 * @param request
	 * @param cybersourceProperties
	 * @return
	 */
	public HashMap calculateTax(HashMap request, Properties cybersourceProperties);
}
