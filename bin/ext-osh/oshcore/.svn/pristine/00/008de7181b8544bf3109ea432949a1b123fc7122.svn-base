/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Collections;

import com.hybris.osh.core.dao.OshPromotionDao;


/**
 * 
 */
public class DefaultOshPromotionDao implements OshPromotionDao
{

	private FlexibleSearchService flexibleSearchService;

	/**
	 * @param voucherCode
	 * @return VoucherModel
	 */
	@Override
	public VoucherModel getOshPromotionVouchers(final String voucherCode)
	{
		final SearchResult<VoucherModel> voucher = flexibleSearchService.search(
				"Select {pk} from {oshpromotionvoucher} where {voucherCode}=?voucherCode ",
				Collections.singletonMap("voucherCode", voucherCode));
		return voucher.getResult().isEmpty() ? null : voucher.getResult().get(0);
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
