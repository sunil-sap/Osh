/**
 * 
 */
package com.hybris.osh.core.interceptor.imageorder;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;


/**
 * 
 *
 */
public class ImageOrderPrepareInterceptor implements PrepareInterceptor
{

	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "modelService")
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.storefront.interceptors.imageorder.ImageOrder#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		// YTODO Auto-generated method stub
		if (model instanceof ProductModel)
		{

			final List<MediaContainerModel> unsortedMediaContainermodel = ((ProductModel) model).getGalleryImages();

			if (unsortedMediaContainermodel != null && !unsortedMediaContainermodel.isEmpty())
			{
				final Map<String, MediaContainerModel> unsortMap = new HashMap<String, MediaContainerModel>();
				for (final MediaContainerModel mediaContainermodel : unsortedMediaContainermodel)
				{
					unsortMap.put(mediaContainermodel.getQualifier(), mediaContainermodel);
				}

				final Map<String, MediaContainerModel> treeMap = new TreeMap<String, MediaContainerModel>(unsortMap);
				final List<MediaContainerModel> sortedModels = new ArrayList<MediaContainerModel>(treeMap.values());

				((ProductModel) model).setGalleryImages(sortedModels);
			}

		}

	}
}
