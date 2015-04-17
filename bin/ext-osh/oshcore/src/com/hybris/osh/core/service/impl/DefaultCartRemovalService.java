/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.hybris.osh.core.dao.CartRemovalDao;
import com.hybris.osh.core.service.CartRemovalService;


/**
 * Service which is responsible for removal of the cart after every 365 days
 */
public class DefaultCartRemovalService implements CartRemovalService
{
	private CartRemovalDao cartRemovalDao;
	private ModelService modelService;
	protected static final Logger LOG = Logger.getLogger(DefaultCartRemovalService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.CartRemovalService#getCartModel()
	 */
	@Override
	public boolean getAbandonedCarts()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		final List<CartModel> carts = cartRemovalDao.findAbandonedCarts(calendar.getTime());
		if (!carts.isEmpty())
		{
			for (final CartModel cartModel : carts)
			{
				modelService.remove(cartModel);
			}
			return true;
		}
		else
		{
			LOG.info("cart model is empty");
			return false;
		}

	}

	/**
	 * @return the cartRemovalDao
	 */
	public CartRemovalDao getCartRemovalDao()
	{
		return cartRemovalDao;
	}

	/**
	 * @param cartRemovalDao
	 *           the cartRemovalDao to set
	 */
	public void setCartRemovalDao(final CartRemovalDao cartRemovalDao)
	{
		this.cartRemovalDao = cartRemovalDao;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
