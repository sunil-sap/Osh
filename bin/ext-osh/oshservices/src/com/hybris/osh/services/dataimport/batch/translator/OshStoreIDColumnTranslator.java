/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.translator;

import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 */
public class OshStoreIDColumnTranslator implements CustomBatchColumnTranslator
{
	@Autowired
	FlexibleSearchService flexibleSearchService;
	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator#translate(java.lang.String)
	 */
	@Override
	public String translate(final String storeID)
	{

		final SearchResult<PointOfServiceModel> lstresult = flexibleSearchService.search(
				"Select {pk} from {PointOfService} where {storeID}=?storeID ", Collections.singletonMap("storeID", storeID));
		final PointOfServiceModel val = lstresult.getResult().isEmpty() ? null : lstresult.getResult().get(0);
		final String StoreName = val.getName();
		final WarehouseModel warehouseModel = warehouseService.getWarehouseForCode(StoreName);
		return warehouseModel.getCode();


	}
}
