package com.hybris.osh.core.storelocator.googlemaps;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.data.RouteData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.impl.GoogleMapsServiceWrapper;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import de.hybris.platform.storelocator.route.Route;
import de.hybris.platform.storelocator.route.impl.DefaultDistanceAndRoute;
import de.hybris.platform.storelocator.route.impl.DefaultRoute;
import de.hybris.platform.util.Config;


public class MyGoogleMapsServiceWrapper extends GoogleMapsServiceWrapper
{

	private MyGoogleMapTools getMapTools(final String url)
	{
		final MyGoogleMapTools geocodingModule = new MyGoogleMapTools();
		geocodingModule.setGoogleKey(Config.getString(GOOGLE_API_KEY, null));
		geocodingModule.setBaseUrl(url);
		return geocodingModule;
	}

	@Override
	public GPS geocodeAddress(final Location address) throws GeoServiceWrapperException
	{
		final MyGoogleMapTools mapTools = getMapTools(Config.getString(GOOGLE_GEOCODING_URL, null));
		return mapTools.geocodeAddress(address);
	}

	@Override
	public GPS geocodeAddress(final AddressData address) throws GeoServiceWrapperException
	{
		final MyGoogleMapTools mapTools = getMapTools(Config.getString(GOOGLE_GEOCODING_URL, null));
		return mapTools.geocodeAddress(address);
	}

	@Override
	public DistanceAndRoute getDistanceAndRoute(final Location start, final Location destination)
			throws GeoServiceWrapperException
	{
		final MyGoogleMapTools geocodingModule = getMapTools(Config.getString(GOOGLE_MAPS_URL, null));
		final RouteData routeData = geocodingModule.getDistanceAndRoute(start, destination);
		final Route route = new DefaultRoute(start.getGPS(), destination, routeData.getCoordinates());
		return new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), route);
	}

	@Override
	public String formatAddress(final Location address) throws GeoServiceWrapperException
	{
		//using google query format for addresses 
		return new MyGoogleMapTools().getGoogleQuery(address.getAddressData());
	}

	@Override
	public DistanceAndRoute getDistanceAndRoute(final GPS start, final Location destination) throws GeoServiceWrapperException
	{
		final MyGoogleMapTools geocodingModule = getMapTools(Config.getString(GOOGLE_MAPS_URL, null));
		final RouteData routeData = geocodingModule.getDistanceAndRoute(start, destination);
		final Route route = new DefaultRoute(start, destination, routeData.getCoordinates());
		return new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), route);
	}

}
