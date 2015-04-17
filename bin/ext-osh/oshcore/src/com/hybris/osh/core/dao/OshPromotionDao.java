/**
 * 
 */
package com.hybris.osh.core.dao;

import de.hybris.platform.voucher.model.VoucherModel;


/**
 * This interface is used to fetch OshPromotionModel
 */
public interface OshPromotionDao
{

	VoucherModel getOshPromotionVouchers(final String voucherCode);

}
