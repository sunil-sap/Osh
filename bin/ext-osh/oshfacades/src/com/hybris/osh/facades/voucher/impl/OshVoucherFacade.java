/**
 *
 */
package com.hybris.osh.facades.voucher.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.voucher.OshVoucherService;


public class OshVoucherFacade
{
	protected static final Logger LOG = Logger.getLogger(OshVoucherFacade.class);

	@Resource(name = "oshVoucherService")
	private OshVoucherService oshVoucherService;

	@Resource(name = "cartService")
	private CartService cartService;

	public boolean redeemVoucher(final String voucherCode)
	{
		try
		{
			return oshVoucherService.redeemVoucher(voucherCode, getCart());
		}
		catch (final Exception e)
		{
			LOG.error("Error reedeming voucher:" + voucherCode, e);
		}
		return false;
	}

	/**
	 * This method will return current session Cart
	 *
	 * @return CartModel
	 */

	protected CartModel getCart()
	{
		return cartService.getSessionCart();
	}

	/**
	 * @param sessionCart
	 */

	public boolean updateVoucher()
	{
		final CartModel cartModel = getCart();
		final List<AbstractOrderEntryModel> ordermodel = cartModel.getEntries();
		if (cartModel.getDiscounts() != null && !cartModel.getDiscounts().isEmpty())
		{
			try
			{
				final VoucherModel voucherModel = ((VoucherModel) cartModel.getDiscounts().get(0));
				oshVoucherService.updateVoucher(cartModel);
				if (voucherModel.getFreeShipping().booleanValue() && voucherModel.getRestrictions() != null
						&& !voucherModel.getRestrictions().isEmpty())
				{
					//oshVoucherService.updateVoucher(cartModel);
					return true;
				}

			}
			catch (final Exception e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.warn("Could not found voucher for order");
					return false;
				}
			}
		}
		return false;

	}
}
