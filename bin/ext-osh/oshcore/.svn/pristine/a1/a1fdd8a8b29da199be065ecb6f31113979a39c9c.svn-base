/**
 * 
 */
package com.hybris.osh.core.shipping.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import com.hybris.osh.core.dao.impl.DefaultOshDeliveryModeDao;
import com.hybris.osh.core.model.OSHShippingModel;


/**
 * 
 *
 */
public class OshShippingInterceptor implements PrepareInterceptor
{
	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "defaultOshDeliveryModeDao")
	private DefaultOshDeliveryModeDao defaultOshDeliveryModeDao;

	@SuppressWarnings("boxing")
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof OSHShippingModel)
		{
			if (((OSHShippingModel) model).getCode() != null)
			{
				modelService.saveAll();
			}
			else
			{
				final long number = defaultOshDeliveryModeDao.findMaxShippingCode();
				//final Long code = Long.valueOf(number) + 1;
				((OSHShippingModel) model).setCode(number + 1);
				modelService.saveAll();
			}
		}

	}
}
