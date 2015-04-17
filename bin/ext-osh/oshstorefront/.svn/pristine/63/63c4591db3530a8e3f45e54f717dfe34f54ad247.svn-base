/**
 * 
 */
package com.hybris.osh.storefront.util;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



public class OshStoreLocatorUtil
{

	private static final String GEOPLUGIN_URL = "geopluginurl";
	private static final String SEARCHPARAMS = "searchparams";
	private static final String LOCATION = "location";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(OshStoreLocatorUtil.class.getName());

	public String getUserLocation(final HttpServletRequest request)
	{
		//Instantiate an HttpClient
		final HttpClient client = new HttpClient();
		//Instantiate a GET HTTP method
		String location = "";
		try
		{
			final String url = configurationService.getConfiguration().getString(GEOPLUGIN_URL) + request.getRemoteAddr();
			final PostMethod method = new PostMethod(url);
			method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");
			final int statusCode = client.executeMethod(method);
			LOG.info("statusCode" + statusCode);
			final String geoResponse = method.getResponseBodyAsString();
			final String searchParams = configurationService.getConfiguration().getString(SEARCHPARAMS);
			final String[] paramsArray = searchParams.split(",");
			for (final String params : paramsArray)
			{
				location = location + StringUtils.substringBetween(geoResponse, params, "<") + " ";
			}
			request.getSession().setAttribute(LOCATION, location);
			method.releaseConnection();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return location;
	}
}
