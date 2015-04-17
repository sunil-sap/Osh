/**
 *
 */
package com.hybris.osh.core.storelocator.googlemaps;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.data.RouteData;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;


public class MyGoogleMapTools
{

	private final static Logger LOG = Logger.getLogger(MyGoogleMapTools.class);

	private String googleKey;
	private String baseUrl;

	private static final String CLIENT_ID = "clientId";
	private static String KEYSTRING = "your_private_key";

	private class LocationResponseData
	{
		private String latitude;
		private String longitude;

		/**
		 * @return the latitude
		 */
		public String getLatitude()
		{
			return latitude;
		}

		/**
		 * @param latitude
		 *           the latitude to set
		 */
		public void setLatitude(final String latitude)
		{
			this.latitude = latitude;
		}

		/**
		 * @return the longitude
		 */
		public String getLongitude()
		{
			return longitude;
		}

		/**
		 * @param longitude
		 *           the longitude to set
		 */
		public void setLongitude(final String longitude)
		{
			this.longitude = longitude;
		}

	}

	public String getGoogleQuery(final AddressData addressData)
	{
		final StringBuffer sBuffer = new StringBuffer();
		if (addressData.getStreet() != null)
		{
			sBuffer.append(addressData.getStreet());
		}
		if (addressData.getBuilding() != null)
		{
			sBuffer.append(" " + addressData.getBuilding() + ", ");
		}
		else
		{
			sBuffer.append(", ");
		}
		if (addressData.getZip() != null)
		{
			sBuffer.append(addressData.getZip() + " ");
		}
		if (addressData.getCity() != null)
		{
			sBuffer.append(addressData.getCity() + ", ");
		}
		if (addressData.getCountryCode() != null)
		{
			sBuffer.append(addressData.getCountryCode());
		}
		return sBuffer.toString();
	}

	public GPS geocodeAddress(final Location address) throws GeoServiceWrapperException
	{
		if (address == null || address.getAddressData() == null)
		{
			throw new GeoServiceWrapperException("Geocoding failed! Address cannot be null");
		}
		else
		{
			return geocodeAddress(address.getAddressData());
		}
	}

	//@Override
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	//@Override
	public void setGoogleKey(final String googleKey)
	{
		this.googleKey = googleKey;
	}

	public RouteData getDistanceAndRoute(final Location start, final Location destination) throws GeoServiceWrapperException
	{
		try
		{
			final String encodeType = "xml";
			final StringBuilder urlAddress = new StringBuilder(baseUrl);
			urlAddress.append("/api/directions/").append(encodeType);
			urlAddress.append("?origin=").append(URLEncoder.encode(getGoogleQuery(start.getAddressData()), "UTF-8"));
			urlAddress.append("&destination=").append(URLEncoder.encode(getGoogleQuery(destination.getAddressData()), "UTF-8"));
			urlAddress.append("&sensor=true");
			urlAddress.append("&mode=driving");
			RouteData routeData = null;
			Document response = null;
			response = getGoogleResponse(urlAddress.toString());
			routeData = parseResponseDocument(response);
			final double distance = GeometryUtils.getElipticalDistanceKM(start.getGPS(), destination.getGPS());
			routeData.setEagleFliesDistance(distance);
			return routeData;
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new GeoServiceWrapperException(e);
		}
	}

	public RouteData getDistanceAndRoute(final GPS start, final Location destination) throws GeoServiceWrapperException
	{
		try
		{
			final String encodeType = "xml";
			final StringBuilder urlAddress = new StringBuilder(baseUrl);
			urlAddress.append("/api/directions/").append(encodeType);
			urlAddress.append("?origin=").append(URLEncoder.encode(start.toGeocodeServiceFormat(), "UTF-8"));
			urlAddress.append("&destination=").append(URLEncoder.encode(destination.getGPS().toGeocodeServiceFormat(), "UTF-8"));
			urlAddress.append("&sensor=true");
			urlAddress.append("&mode=driving");

			RouteData routeData = null;
			Document response = null;
			response = getGoogleResponse(urlAddress.toString());
			routeData = parseResponseDocument(response);
			//YTODO : calculate if addresses are from europe to set the right Elipsoid model for the calculation:
			final double distance = GeometryUtils.getElipticalDistanceKM(start, destination.getGPS());
			routeData.setEagleFliesDistance(distance);
			return routeData;
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new GeoServiceWrapperException(e);
		}
	}

	private Document getGoogleResponse(final String urlAddress) throws GeoServiceWrapperException
	{
		try
		{
			// Using Apache HTTPClient
			final HttpClient client = new HttpClient();
			final String host = System.getProperty("http.proxyHost");
			final String port = System.getProperty("http.proxyPort");
			if (!StringUtils.isEmpty(host) && StringUtils.isNumeric(port))
			{
				LOG.debug("proxy host : " + host);
				LOG.debug("proxy port : " + port);
				client.getHostConfiguration().setProxy(host, Integer.parseInt(port));
			}
			final String user = System.getProperty("http.proxyUser");
			final String password = System.getProperty("http.proxyPassword");
			if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password))
			{
				final HttpState state = new HttpState();
				state.setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
				client.setState(state);
			}
			client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
			int reqStatusCode;
			LOG.debug("google request:\n" + urlAddress);
			final GetMethod method = new GetMethod(urlAddress);
			method.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
			reqStatusCode = client.executeMethod(method);
			if (reqStatusCode != HttpStatus.SC_OK)
			{
				throw new GeoServiceWrapperException("Google maps unavailable. Status: " + method.getStatusLine());
			}
			else
			{
				InputStream in = null;
				final StringWriter sw = new StringWriter();
				try
				{
					in = method.getResponseBodyAsStream();
					IOUtils.copy(in, sw, "ISO-8859-1");
				}
				finally
				{
					IOUtils.closeQuietly(in);
				}
				final String str = sw.toString();
				if (str == null || "".equals(str))
				{
					throw new GeoServiceWrapperException("Empty google response");
				}
				LOG.debug("### GOOGLE RESPONSE ###");
				LOG.debug(str);
				LOG.debug("#######################");
				return DocumentHelper.parseText(str);
			}
		}
		catch (final IOException e)
		{
			throw new GeoServiceWrapperException("Cannot get Google response due to :", e);
		}
		catch (final DocumentException e)
		{
			throw new GeoServiceWrapperException("Cannot get Google response due to :", e);
		}

	}

	public RouteData parseResponseDocument(final Document response) throws GeoServiceWrapperException
	{
		final RouteData routeData = new RouteData();
		treeWalk(response, routeData);
		return routeData;
	}


	public GPS geocodeAddress(final AddressData addressData) throws GeoServiceWrapperException
	{
		try
		{
			Document response = null;
			final String encodeType = "xml";
			final StringBuilder urlAddress = new StringBuilder(baseUrl);
			urlAddress.append("/");
			urlAddress.append(encodeType);
			urlAddress.append("?address=");
			urlAddress.append(URLEncoder.encode(getGoogleQuery(addressData), "UTF-8"));
			urlAddress.append("&sensor=false");
			urlAddress.append("&client=");

			//Get Client Id from config
			urlAddress.append(Config.getString(CLIENT_ID, null));

			//Get Crypto key from config
			final String key = Config.getString(KEYSTRING, null);
			try
			{
				final UrlSigner signer = new UrlSigner(key);
				final String finalUrl = signer.getSignature(key, urlAddress.toString());
				urlAddress.append("&" + finalUrl.substring(finalUrl.lastIndexOf("&signature=") + 1) + "");
			}
			catch (final IOException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final InvalidKeyException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final NoSuchAlgorithmException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final URISyntaxException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			response = getGoogleResponse(urlAddress.toString());

			final LocationResponseData locationData = new LocationResponseData();
			treeWalk(response, locationData);

			final String latitude = locationData.getLatitude();
			final String longitude = locationData.getLongitude();

			return new DefaultGPS().create(Double.parseDouble(latitude), Double.parseDouble(longitude));

		}
		catch (final UnsupportedEncodingException e)
		{
			throw new GeoServiceWrapperException(e);
		}
		catch (final GeoLocatorException e)
		{
			throw new GeoServiceWrapperException(e);
		}

	}

	private void treeWalk(final Element element, final RouteData routeData) throws GeoServiceWrapperException
	{
		final int size = element.nodeCount();

		for (int i = 0; i < size; i++)
		{
			final Node node = element.node(i);
			if (node instanceof Element)
			{
				if (node.getName().equals("status"))
				{
					if (!node.getStringValue().trim().equals("OK"))
					{
						throw new GeoServiceWrapperException("Could not get directions : ", node.getStringValue().trim());
					}
				}
				else if (node.getName().equals("route"))
				{
					final Element route = (Element) node;
					populateRoute(route, routeData);
				}
			}
		}
	}

	private void treeWalk(final Document document, final LocationResponseData locationData) throws GeoServiceWrapperException
	{
		treeWalk(document.getRootElement(), locationData);
	}

	private void treeWalk(final Element element, final LocationResponseData locationData) throws GeoServiceWrapperException
	{
		final int size = element.nodeCount();
		for (int i = 0; i < size; i++)
		{
			final Node node = element.node(i);

			if (locationData.getLatitude() != null && locationData.getLongitude() != null)
			{
				return;
			}

			if (node instanceof Element)
			{
				if (node.getName().equals("status"))
				{
					if (!node.getStringValue().trim().equals("OK"))
					{
						throw new GeoServiceWrapperException("Could not get geocode : ", node.getStringValue().trim());
					}
				}
				else if (node.getName().equals("location"))
				{
					final Element location = (Element) node;
					populateLocation(location, locationData);

				}
				treeWalk((Element) node, locationData);
			}
		}
	}

	private void populateLocation(final Element location, final LocationResponseData locationData)
	{
		final int size = location.nodeCount();
		for (int i = 0; i < size; i++)
		{
			final Node node = location.node(i);
			if (node instanceof Element)
			{
				if (node.getName().equals("lat"))
				{
					final Element lat = (Element) node;
					locationData.setLatitude(lat.getStringValue());

				}
				else if (node.getName().equals("lng"))
				{
					final Element lng = (Element) node;
					locationData.setLongitude(lng.getStringValue());
				}
			}
		}
	}

	private void populateRoute(final Element route, final RouteData routeData)
	{
		final int size = route.nodeCount();
		for (int i = 0; i < size; i++)
		{
			final Node node = route.node(i);
			if (node instanceof Element)
			{
				if (node.getName().equals("leg"))
				{
					final Element leg = (Element) node;
					populateLeg(routeData, leg);

				}
				else if (node.getName().equals("overview_polyline"))
				{
					final Element polyline = (Element) node;
					populateCoordinates(polyline, routeData);
				}
			}
		}
	}

	private void populateLeg(final RouteData routeData, final Element leg)
	{
		for (int j = 0; j < leg.nodeCount(); j++)
		{
			final Node legNode = leg.node(j);
			if (legNode instanceof Element)
			{
				if (legNode.getName().equals("distance"))
				{
					routeData.setDistance(Double.parseDouble(((Element) legNode).node(1).getStringValue().trim()));
				}
			}
		}
	}

	private void populateCoordinates(final Element polyline, final RouteData routeData)
	{
		for (int i = 0; i < polyline.nodeCount(); i++)
		{
			if (polyline.node(i) instanceof Element && polyline.node(i).getName().equals("points"))
			{
				routeData.setCoordinates(((Element) polyline.node(i)).node(0).getStringValue().trim());
			}
		}
	}

	private void treeWalk(final Document document, final RouteData routeData) throws GeoServiceWrapperException
	{
		treeWalk(document.getRootElement(), routeData);
	}



}
