/**
 * 
 */
package com.hybris.osh.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.List;

import com.hybris.osh.core.dao.OrderEntryDao;
import com.hybris.osh.core.price.OshPriceFactory;
import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;
import com.hybris.osh.core.service.OshCommerceCartService;


/**
 * 
 */
public class DefaultOshCommerceCartService extends DefaultCommerceCartService implements OshCommerceCartService
{

	private static final int APPEND_AS_LAST = -1;
	private static final String CART_ENTRY = "CartEntry";
	private OrderDao orderDao;
	private OrderEntryDao orderEntryDao;

	@Override
	public void recalculateCart(final CartModel cartModel) throws CalculationException
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		getCalculationService().recalculate(cartModel);
		getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
				AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
		cartModel.setTaxAmount(cartModel.getTotalTax());
		getModelService().refresh(cartModel);
		getModelService().save(cartModel);

		if (cartModel.getTaxAmount() != null)
		{
			cartModel.setTotalTax(cartModel.getTaxAmount());
			getModelService().save(cartModel);
			if (cartModel.getAllPromotionResults() != null && !(cartModel.getAllPromotionResults().isEmpty()))
			{
				for (final PromotionResultModel promoResult : cartModel.getAllPromotionResults())
				{
					if (promoResult.getPromotion() instanceof OshTaxPromotionModel)
					{

						final double taxValue = cartModel.getTaxAmount() != null ? cartModel.getTaxAmount().doubleValue() : 0.0;
						int discountCount = cartModel.getGlobalDiscountValues().size();
						final List<DiscountValue> discountList = new ArrayList<DiscountValue>();
						final List<DiscountValue> discountValueList = cartModel.getGlobalDiscountValues();
						for (final DiscountValue dicountValue : discountValueList)
						{

							if (dicountValue.getAppliedValue() == 0.0)
							{
								final DiscountValue discount = new DiscountValue(cartModel.getPk().toString() + "_" + ++discountCount,
										taxValue, true, taxValue, cartModel.getCurrency().getIsocode());
								discountList.add(discount);
								break;
							}
							else
							{
								discountList.add(dicountValue);
							}
						}


						cartModel.setGlobalDiscountValues(discountList);
						getModelService().save(cartModel);
					}
				}
			}
		}
		getModelService().refresh(cartModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.OshCommerceCartService#addToCart(de.hybris.platform.core.model.order.CartModel,
	 * de.hybris.platform.core.model.product.ProductModel, long, de.hybris.platform.core.model.product.UnitModel,
	 * boolean, java.lang.String)
	 */
	@Override
	public CommerceCartModification addToCartOrder(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final UnitModel unit, boolean forceNewEntry, final String orderType)
			throws CommerceCartModificationException
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(productModel, "Product model cannot be null");

		if (productModel.getVariantType() != null)
		{
			throw new CommerceCartModificationException("Choose a variant instead of the base product");
		}

		if (quantityToAdd < 1)
		{
			throw new CommerceCartModificationException("Quantity must not be less than one");
		}

		UnitModel orderableUnit = unit;
		if (orderableUnit == null)
		{
			try
			{
				orderableUnit = getProductService().getOrderableUnit(productModel);
			}
			catch (final ModelNotFoundException e)
			{
				throw new CommerceCartModificationException(e.getMessage(), e);
			}
		}

		// So now work out what the maximum allowed to be added is (note that this may be negative!)
		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd);

		if (actualAllowedQuantityChange > 0)
		{
			AbstractOrderEntryModel entryModel = null;
			final CommerceCartModification modification = new CommerceCartModification();

			final List<AbstractOrderEntryModel> result = getOrderEntryDao().getOrderEntries(cartModel, productModel, orderType);

			if (!result.isEmpty())
			{
				entryModel = result.get(0);
			}
			if (entryModel != null)
			{
				entryModel.setQuantity(Long.valueOf(entryModel.getQuantity().longValue() + quantityToAdd));
				getModelService().save(entryModel);
				getModelService().refresh(cartModel);
				calculateCart(cartModel);
				getModelService().refresh(entryModel);


				modification.setQuantityAdded(actualAllowedQuantityChange);
				modification.setQuantity(entryModel.getQuantity().longValue());
				modification.setEntry(entryModel);
			}

			if (entryModel == null)
			{
				final List<AbstractOrderEntryModel> lst = getOrderDao().findEntriesByProduct(CART_ENTRY, cartModel, productModel);
				for (final AbstractOrderEntryModel e : lst)
				{
					forceNewEntry = true;
					if (e.getOrderType().equalsIgnoreCase(orderType))
					{
						forceNewEntry = false;
						break;
					}
				}
				final CartEntryModel cartEntryModel = getCartService().addNewEntry(cartModel, productModel,
						actualAllowedQuantityChange, orderableUnit, APPEND_AS_LAST, !forceNewEntry);
				cartEntryModel.setOrderType(orderType);

				cartModel.setOrderType(orderType);
				getModelService().save(cartModel);
				getModelService().save(cartEntryModel);
				calculateCart(cartModel);
				getModelService().save(cartEntryModel);

				// Create the card modification result

				modification.setQuantityAdded(actualAllowedQuantityChange);
				modification.setQuantity(cartEntryModel.getQuantity().longValue());
				modification.setEntry(cartEntryModel);

			}

			// Are we able to add the quantity we requested?
			if (actualAllowedQuantityChange == quantityToAdd)
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			}

			return modification;
		}
		else
		{
			// Not allowed to add any quantity, or maybe even asked to reduce the quantity
			// Do nothing!
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			modification.setQuantityAdded(0);
			return modification;
		}
	}

	/**
	 * @param entryModel
	 * @param mergeEntryModel
	 * @return AbstractOrderEntryModel
	 */
	private AbstractOrderEntryModel mergeCartEnrty(final AbstractOrderEntryModel entryModel,
			final AbstractOrderEntryModel mergeEntryModel)
	{
		final Long mergeQuantity = Long.valueOf(entryModel.getQuantity()) + Long.valueOf(mergeEntryModel.getQuantity());
		getModelService().remove(mergeEntryModel);
		entryModel.setQuantity(mergeQuantity);
		getModelService().save(entryModel);

		return entryModel;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.OshCommerceCartService#updateForMergeCartEntry(de.hybris.platform.core.model.order
	 * .CartModel, long, long, java.lang.String)
	 */
	@Override
	public CommerceCartModification updateForMergeCartEntry(final CartModel cartModel, final long entryNumber,
			final long quantityToAdd, final String orderType) throws CommerceCartModificationException
	{
		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
		if (!entryToUpdate.getOrderType().equalsIgnoreCase(orderType))
		{
			entryToUpdate.setOrderType(orderType);
		}

		getModelService().save(entryToUpdate);

		final CommerceCartModification modification = new CommerceCartModification();
		// We are allowed to add items to the cart

		final List<AbstractOrderEntryModel> result = getOrderEntryDao().getOrderEntries(cartModel, entryToUpdate.getProduct(),
				orderType);
		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate.getProduct(),
				quantityToAdd);
		if (result.size() == 2)
		{
			final AbstractOrderEntryModel newEntryModel = mergeCartEnrty(result.get(0), result.get(1));
			getModelService().refresh(cartModel);
			calculateCart(cartModel);
			getModelService().refresh(newEntryModel);
			modification.setQuantityAdded(actualAllowedQuantityChange);
			modification.setQuantity(newEntryModel.getQuantity().longValue());
			modification.setEntry(newEntryModel);
		}

		return modification;
	}


	@Override
	public boolean calculateCart(final CartModel cartModel)
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		try
		{
			JaloSession.getCurrentSession().setPriceFactory(new OshPriceFactory());
			getCalculationService().calculate(cartModel);
			getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
					AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
			getModelService().refresh(cartModel);

			if (cartModel.getTaxAmount() != null)
			{
				cartModel.setTotalTax(cartModel.getTaxAmount());
				getModelService().save(cartModel);

				if (cartModel.getTaxAmount() != null)
				{
					cartModel.setTotalTax(cartModel.getTaxAmount());
					getModelService().save(cartModel);
					if (cartModel.getAllPromotionResults() != null && !(cartModel.getAllPromotionResults().isEmpty()))
					{
						for (final PromotionResultModel promoResult : cartModel.getAllPromotionResults())
						{
							if (promoResult.getPromotion() instanceof OshTaxPromotionModel)
							{

								final double taxValue = cartModel.getTaxAmount() != null ? cartModel.getTaxAmount().doubleValue() : 0.0;
								int discountCount = cartModel.getGlobalDiscountValues().size();
								final List<DiscountValue> discountList = new ArrayList<DiscountValue>();
								final List<DiscountValue> discountValueList = cartModel.getGlobalDiscountValues();
								for (final DiscountValue dicountValue : discountValueList)
								{
									if (dicountValue.getAppliedValue() == 0.0)
									{
										final DiscountValue discount = new DiscountValue(cartModel.getPk().toString() + "_"
												+ ++discountCount, taxValue, true, taxValue, cartModel.getCurrency().getIsocode());
										discountList.add(discount);
									}
									else
									{
										discountList.add(dicountValue);
									}

								}


								cartModel.setGlobalDiscountValues(discountList);
								getModelService().save(cartModel);
							}
						}
					}
				}



			}
			getModelService().refresh(cartModel);
		}
		catch (final CalculationException calculationException)
		{
			throw new IllegalStateException("Cart model " + cartModel.getCode() + " was not calculated due to: "
					+ calculationException.getMessage());
		}
		return true;
	}


	/**
	 * @return the orderDao
	 */
	public OrderDao getOrderDao()
	{
		return orderDao;
	}

	/**
	 * @param orderDao
	 *           the orderDao to set
	 */
	public void setOrderDao(final OrderDao orderDao)
	{
		this.orderDao = orderDao;
	}

	/**
	 * @return the orderEntryDao
	 */
	public OrderEntryDao getOrderEntryDao()
	{
		return orderEntryDao;
	}

	/**
	 * @param orderEntryDao
	 *           the orderEntryDao to set
	 */
	public void setOrderEntryDao(final OrderEntryDao orderEntryDao)
	{
		this.orderEntryDao = orderEntryDao;
	}

}
