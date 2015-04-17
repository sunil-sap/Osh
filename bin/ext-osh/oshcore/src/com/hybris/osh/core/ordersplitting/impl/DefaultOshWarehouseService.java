/**
 * 
 */
package com.hybris.osh.core.ordersplitting.impl;

import de.hybris.platform.ordersplitting.daos.WarehouseDao;
import de.hybris.platform.ordersplitting.impl.DefaultWarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import javax.annotation.Resource;


/**
 * 
 *
 */
public class DefaultOshWarehouseService extends DefaultWarehouseService
{
	@Resource(name = "warehouseDao")
	private WarehouseDao warehouseDao;

	@Override
	public WarehouseModel getWarehouseForCode(final String code)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("code", code);

		final List res = warehouseDao.getWarehouseForCode(code);

		if (res.isEmpty())
		{
			return null;
		}
		ServicesUtil.validateIfSingleResult(res, WarehouseModel.class, "code", code);

		return (WarehouseModel) res.get(0);
	}

}
