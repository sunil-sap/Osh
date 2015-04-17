/**
 *
 */
package com.hybris.osh.core.voucher.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.VoucherManager;
import de.hybris.platform.voucher.model.ProductRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.dao.OshPromotionDao;
import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.core.service.OshCommerceCartService;
import com.hybris.osh.core.voucher.OshVoucherService;


public class DefaultOshVoucherService extends DefaultVoucherService implements OshVoucherService
{
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "commerceCartService")
	private OshCommerceCartService commerceCartService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private OshPromotionDao oshPromotionDao;

	private static final String LOYALTY_VOUCHER = "loyalty.voucher";

	private static final Logger LOG = Logger.getLogger(DefaultOshVoucherService.class);


	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.core.voucher.OshVoucherService#removeVoucher(java.lang.String,
	 * de.hybris.platform.core.model.order.CartModel)
	 */
	@Override
	public void removeVoucher(final String voucherCode, final CartModel cartModel)
	{
		System.out.println("voucher removed............." + voucherCode);
		validateParameterNotNull(voucherCode, "Voucher Code cannot be null");
		validateParameterNotNull(cartModel, "Cart Model cannot be null");
		final VoucherModel voucherModel = getVoucher(voucherCode);
		if (voucherModel != null)
		{
			synchronized (cartModel)
			{
				try
				{
					if (voucherModel instanceof OshPromotionVoucherModel)
					{
						final Cart aCart = getModelService().getSource(cartModel);
						for (final DiscountModel discountModel : cartModel.getDiscounts())
						{
							final Discount discount = getModelService().getSource(discountModel);
							aCart.removeDiscount(discount);
							aCart.recalculate();
						}
						voucherModel.setRestrictions(null);
						modelService.save(voucherModel);
					}
					if (voucherModel instanceof PromotionVoucherModel)
					{
						final Cart aCart = getModelService().getSource(cartModel);
						for (final DiscountModel discountModel : cartModel.getDiscounts())
						{
							final Discount discount = getModelService().getSource(discountModel);
							aCart.removeDiscount(discount);

							aCart.setCalculated(false);
							aCart.recalculate();
						}
						//voucherModel.setRestrictions(null);
						modelService.save(voucherModel);
					}
					else
					{
						releaseVoucher(voucherCode, cartModel);
						if (voucherCode.equalsIgnoreCase(configurationService.getConfiguration().getString(LOYALTY_VOUCHER)))
						{
							final CartModel sessionCart = cartService.getSessionCart();
							sessionCart.setLoyaltyVoucher(null);
							getModelService().save(sessionCart);
						}
					}
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Removing voucher:" + voucherCode);
					}
				}
				catch (final Exception e)
				{
					LOG.error("Error removing voucher:" + voucherCode, e);
				}
			}
		}
		try
		{
			commerceCartService.recalculateCart(cartModel);
		}
		catch (final CalculationException e)
		{
			LOG.error("Error recalculating Cart:" + e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.core.voucher.OshVoucherService#reedemVoucher(java.lang.String,
	 * de.hybris.platform.core.model.order.CartModel)
	 */
	@Override
	public boolean redeemVoucher(final String voucherCode, final CartModel cart)
	{

		final String loyaltyVoucherCode = configurationService.getConfiguration().getString(LOYALTY_VOUCHER);
		validateParameterNotNull(voucherCode, "Voucher Code cannot be null");
		validateParameterNotNull(cart, "Cart Model cannot be null");
		VoucherModel voucher;
		boolean result = false;

		if (voucherCode.startsWith("441"))
		{
			voucher = getVoucher(loyaltyVoucherCode);
		}
		else
		{
			voucher = getVoucher(voucherCode);
		}
		final Map<Integer, Double> map = new LinkedHashMap<Integer, Double>();
		if (voucher != null)
		{
			synchronized (cart)
			{
				try
				{
					if ((voucher instanceof OshPromotionVoucherModel) && isReservable(voucher)
							&& voucherModelService.isApplicable(voucher, cart))
					{

						AbstractOrderEntryModel orderEntry = null;
						for (final AbstractOrderEntryModel order : cart.getEntries())
						{
							map.put(order.getEntryNumber(), order.getBasePrice());
						}
						final Map<Object, Double> sortedMap = sortByComparator(map);
						for (final Map.Entry entry : sortedMap.entrySet())
						{
							orderEntry = cartService.getEntryForNumber(cart, (Integer) entry.getKey());
							break;
						}
						if (voucher.getRestrictions().isEmpty())
						{
							final ProductRestrictionModel prm = new ProductRestrictionModel();
							prm.setPositive(Boolean.TRUE);
							prm.setVoucher(voucher);
							prm.setProducts(Collections.singletonList(orderEntry.getProduct()));
							getModelService().save(prm);
						}
						else
						{
							for (final RestrictionModel restrictions : voucher.getRestrictions())
							{
								((ProductRestrictionModel) restrictions).setProducts(Collections.singletonList(orderEntry.getProduct()));
								getModelService().save(restrictions);
							}
						}

						result = voucherModelService.redeem(voucher, voucherCode, cart);
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Reedeming voucher:" + voucherCode);
						}
						commerceCartService.calculateCart(cart);
					}


					else if (voucherModelService.isReservable(voucher, voucherCode, cart)
							&& voucherModelService.isApplicable(voucher, cart) && !(voucher instanceof OshPromotionVoucherModel))
					{
						if (voucherCode.startsWith("441") && voucherCode.length() == 13)
						{
							result = voucherModelService.redeem(voucher, loyaltyVoucherCode, cart);
							final CartModel sessionCart = cartService.getSessionCart();
							sessionCart.setLoyaltyVoucher(voucherCode);
							getModelService().save(sessionCart);
						}
						else
						{
							result = voucherModelService.redeem(voucher, voucherCode, cart);
						}

						if (LOG.isDebugEnabled())
						{
							LOG.debug("Reedeming voucher:" + voucherCode);
						}
						commerceCartService.calculateCart(cart);
					}
					else
					{
						result = false;
					}
				}
				catch (final Exception e)
				{
					LOG.error("Error reedeming voucher:" + voucherCode, e);
				}
			}
		}

		return result;

	}

	/**
	 * @param voucher
	 * @return boolean
	 */
	private boolean isReservable(final VoucherModel voucher)
	{
		return ((OshPromotionVoucherModel) voucher).getRedemptionQuantityLimit().intValue() > voucher.getOrders().size() ? true
				: false;
	}

	private static Map sortByComparator(final Map unsortMap)
	{
		final List list = new LinkedList(unsortMap.entrySet());
		// sort list based on comparator
		Collections.sort(list, new Comparator()
		{
			@Override
			public int compare(final Object obj1, final Object obj2)
			{
				final Double val1 = (Double) ((Map.Entry) obj1).getValue();
				final Double val2 = (Double) ((Map.Entry) obj2).getValue();
				if (val1.doubleValue() > val2.doubleValue())
				{
					return -1;
				}
				else
				{
					return 1;
				}

			}
		});
		// put sorted list into map again
		final Map sortedMap = new LinkedHashMap();
		for (final Iterator it = list.iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();

			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}


	@Override
	public void updateVoucher(final CartModel cartModel) throws JaloPriceFactoryException
	{
		final Object object = cartModel.getDiscounts().get(0);
		String voucherCode=null;
		if (object instanceof PromotionVoucherModel)
		{
			voucherCode = ((PromotionVoucherModel) cartModel.getDiscounts().get(0)).getVoucherCode();
		}
		if (object instanceof OshPromotionVoucherModel)
		{
			voucherCode = ((OshPromotionVoucherModel) cartModel.getDiscounts().get(0)).getVoucherCode();
		}

		final VoucherModel voucherModel = getVoucher(voucherCode);
		if (voucherModel instanceof OshPromotionVoucherModel)
		{
			for (final RestrictionModel restrictions : voucherModel.getRestrictions())
			{
				if (restrictions instanceof ProductRestrictionModel)
				{
					((ProductRestrictionModel) restrictions).setProducts(Collections.EMPTY_SET);
				}
			}
			voucherModel.setOrders(Collections.EMPTY_LIST);
			cartModel.setDiscounts(null);
			getModelService().save(cartModel);
			getModelService().save(voucherModel);
		}
		try
		{
			releaseVoucher(voucherCode, cartModel);
		}
		catch (final JaloPriceFactoryException e)
		{
			LOG.error(e);
		}

		if (voucherCode != null)
		{
			redeemVoucher(voucherCode, cartModel);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.core.voucher.OshVoucherService#isValidVouher(java.lang.String)
	 */
	@Override
	public boolean isValidVoucherCode(final String voucherCode)
	{
		final String loyaltyVoucherCode = configurationService.getConfiguration().getString(LOYALTY_VOUCHER);
		final VoucherModel voucher = voucherCode.startsWith("441") && voucherCode.length() == 13 ? getVoucher(loyaltyVoucherCode)
				: getVoucher(voucherCode);
		return voucher != null ? true : false;
	}

	@Override
	public VoucherModel getVoucher(final String voucherCode)
	{

		final VoucherModel oshvoucher = oshPromotionDao.getOshPromotionVouchers(voucherCode);
		if (oshvoucher != null)
		{
			return oshvoucher;
		}
		else
		{
			final Voucher voucher = VoucherManager.getInstance().getVoucher(voucherCode);
			if (voucher == null)
			{
				return null;
			}
			else
			{
				return (VoucherModel) getModelService().get(voucher);
			}
		}


	}

	/**
	 * @return the oshPromotionDao
	 */
	public OshPromotionDao getOshPromotionDao()
	{
		return oshPromotionDao;
	}

	/**
	 * @param oshPromotionDao
	 *           the oshPromotionDao to set
	 */
	public void setOshPromotionDao(final OshPromotionDao oshPromotionDao)
	{
		this.oshPromotionDao = oshPromotionDao;
	}





}
