package com.hybris.osh.core.voucher;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.voucher.VoucherService;


/**
 *
 *
 */
public interface OshVoucherService extends VoucherService
{
	/**
	 * ReedemVoucher for current Cart only if it isReservable true
	 * 
	 * @param voucherCode
	 * @param cart
	 * @return boolean value
	 */
	@Override
	public boolean redeemVoucher(final String voucherCode, final CartModel cart);

	/**
	 * removes the applied voucher from Cart if required
	 * 
	 * @param voucherCode
	 *           String
	 * @param cart
	 *           CartModel
	 */
	public void removeVoucher(final String voucherCode, final CartModel cart);

	/**
	 * Update the cart for the voucher
	 * 
	 */
	public void updateVoucher(final CartModel cart) throws JaloPriceFactoryException;

	/**
	 * To check whether the voucher code is valid or not
	 * 
	 * @param voucherCode
	 * @return boolean value
	 */
	public boolean isValidVoucherCode(final String voucherCode);


}
