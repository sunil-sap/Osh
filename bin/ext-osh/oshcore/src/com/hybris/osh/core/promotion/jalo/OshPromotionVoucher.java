package com.hybris.osh.core.promotion.jalo;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.VoucherManager;

import java.text.MessageFormat;
import java.util.Collection;


public class OshPromotionVoucher extends GeneratedOshPromotionVoucher
{

	public OshPromotionVoucher()
	{
	}

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type,
			final de.hybris.platform.jalo.Item.ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		final String voucherCode = (String) allAttributes.get(VOUCHERCODE);
		if (voucherCode != null && voucherCode.trim().length() > 0)
		{
			final Collection result = getSession().search(
					new GenericQuery("OshPromotionVoucher".intern(), GenericCondition.equals(OshPromotionVoucher.VOUCHERCODE,
							voucherCode))).getResult();
			final Voucher voucher = result.isEmpty() ? null : (Voucher) result.iterator().next();
			if (voucher != null)
			{
				throw new JaloInvalidParameterException(MessageFormat.format(
						Localization.getLocalizedString("type.promotionvoucher.error.vouchercode.not.unique"), new Object[]
						{ voucherCode, voucher.getName() }), 0);
			}
		}
		return super.createItem(ctx, type, allAttributes);
	}

	@Override
	public boolean checkVoucherCode(final String aVoucherCode)
	{
		return aVoucherCode.equals(getVoucherCode());
	}

	@Override
	protected int getNextVoucherNumber(final SessionContext ctx)
	{
		return 1;
	}

	@Override
	public boolean isReservable(final String aVoucherCode, final User user)
	{
		return getInvalidations(aVoucherCode, user).size() < getRedemptionQuantityLimitPerUserAsPrimitive()
				&& getInvalidations(aVoucherCode).size() < getRedemptionQuantityLimitAsPrimitive();
	}

	@Override
	public void setVoucherCode(final SessionContext ctx, final String param)
	{
		if (param != null && param.trim().length() > 0)
		{
			final Voucher voucher = VoucherManager.getInstance(getSession()).getVoucher(param);
			if (voucher != null && voucher != this)
			{
				throw new JaloInvalidParameterException(MessageFormat.format(
						Localization.getLocalizedString("type.promotionvoucher.error.vouchercode.not.unique"), new Object[]
						{ param, voucher.getName() }), 0);
			}
		}
		super.setVoucherCode(ctx, param);
	}
}
