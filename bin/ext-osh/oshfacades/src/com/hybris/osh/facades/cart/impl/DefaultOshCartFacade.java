/**
 * 
 */
package com.hybris.osh.facades.cart.impl;

import de.hybris.platform.acceleratorservices.storefinder.StoreFinderService;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.price.OshPriceFactory;
import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.core.service.OshCommerceCartService;
import com.hybris.osh.core.voucher.OshVoucherService;
import com.hybris.osh.facades.cart.OshCartFacade;


public class DefaultOshCartFacade extends DefaultCartFacade implements OshCartFacade
{
	private UserService userService;
	private ProductService productService;
	private OshCommerceCartService oshCommerceCartService;
	private ProductFacade productFacade;
	@Resource(name = "oshVoucherService")
	private OshVoucherService oshVoucherService;

	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;

	@Resource(name = "stockService")
	private StockService stockService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "storeFinderService")
	private StoreFinderService storeFinderService;

	@Autowired
	private ModelService modelService;


	private static final Logger LOG = Logger.getLogger(DefaultOshCartFacade.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cyclegear.facades.cart.OshCartFacade#appendCartForCurrentUser()
	 */
	@Override
	public void appendCartForCurrentUser(final HttpServletRequest request)
	{
		if (!userService.getCurrentUser().getUid().equals("anonymous"))
		{
			appendCarts(request);
		}
	}

	/**
	 * appends current session Cart with the users previous cart
	 */
	private void appendCarts(final HttpServletRequest request)
	{
		final HttpSession httpSession = request.getSession(true);
		CartModel sessionCart = null;
		CartModel previousCart = null;
		if (hasSessionCart())
		{
			sessionCart = getCartService().getSessionCart();
		}

		final List<CartModel> carts = (List<CartModel>) userService.getCurrentUser().getCarts();

		if (carts.size() > 1)
		{
			previousCart = carts.get(carts.size() - 2);
		}


		if (sessionCart != null && sessionCart.getEntries() != null && sessionCart.getEntries().isEmpty() && previousCart != null)
		{
			getCartService().setSessionCart(previousCart);

		}

		else
		{
			ProductModel productModel = null;
			if (sessionCart != null && sessionCart.getEntries() != null && !sessionCart.getEntries().isEmpty()
					&& previousCart != null && previousCart.getEntries() != null && !previousCart.getEntries().isEmpty())
			{
				for (final AbstractOrderEntryModel entry : previousCart.getEntries())
				{
					try
					{
						if (entry.getProduct() != null)
						{
							productModel = productService.getProductForCode(entry.getProduct().getCode());
						}
					}
					catch (final Exception e)
					{
						productModel = null;
						LOG.debug("Exception: " + e.getMessage());
					}

					if (productModel != null)
					{
						final ProductData productData = productFacade.getProductForCodeAndOptions(productModel.getCode(), Arrays
								.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
										ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS,
										ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.STOCK));

						if (entry.getOrderType() != null && !(entry.getOrderType().equalsIgnoreCase(sessionCart.getOrderType())))
						{

							entry.setOrderType(sessionCart.getOrderType());
							modelService.save(entry);
						}
						if (!(productData.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK)) && entry.getOrderType() != null
								&& entry.getOrderType().equalsIgnoreCase("online") && entry.getProduct().getAvailabilityInd() != null
								&& !(entry.getProduct().getAvailabilityInd().equals("STORE")))
						{
							try
							{
								/*
								 * getCommerceCartService().addToCart(sessionCart, productModel, entry.getQuantity().intValue(),
								 * productModel.getUnit(), false);
								 */
								final CommerceCartModification modification = oshCommerceCartService.addToCartOrder(sessionCart,
										productModel, entry.getQuantity().intValue(), productModel.getUnit(), false, entry.getOrderType());
								httpSession.setAttribute("cartMerged", "YES");

							}
							catch (final Exception e)
							{
								LOG.debug("Exception: " + e.getMessage());
							}
						}
						else if (entry.getOrderType() != null && entry.getOrderType().equalsIgnoreCase("bopis")
								&& entry.getProduct().getAvailabilityInd() != null
								&& !(entry.getProduct().getAvailabilityInd().equals("WEB")))
						{
							String myStore = null;
							int storeStock = 0;
							WarehouseModel wareHouseModel = null;
							final CustomerModel custModel = (CustomerModel) userService.getCurrentUser();
							final String storeName = (String) request.getSession().getAttribute("storeName");

							if (custModel.getMyStore() != null && !custModel.getMyStore().getName().equalsIgnoreCase(storeName))
							{
								final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
								final PointOfServiceModel pointOfService = storeFinderService.getPointOfServiceForName(currentBaseStore,
										storeName);
								custModel.setMyStore(pointOfService);
								myStore = storeName;
							}
							else
							{
								myStore = custModel.getMyStore().getName();

							}
							try
							{
								wareHouseModel = warehouseService.getWarehouseForCode(myStore);
								if (wareHouseModel != null)
								{
									storeStock = stockService.getStockLevelAmount(productModel, wareHouseModel);
									if (storeStock > 0)
									{
										try
										{
											/*
											 * getCommerceCartService().addToCart(sessionCart, productModel,
											 * entry.getQuantity().intValue(), productModel.getUnit(), false);
											 */
											final CommerceCartModification modification = oshCommerceCartService.addToCartOrder(sessionCart,
													productModel, entry.getQuantity().intValue(), productModel.getUnit(), false,
													entry.getOrderType());

											httpSession.setAttribute("cartMerged", "YES");

										}
										catch (final Exception e)
										{
											LOG.debug("Exception: " + e.getMessage());
										}
									}
								}
							}
							catch (final StockLevelNotFoundException e)
							{
								storeStock = 0;
							}

						}
					}
				}
			}
			else if (previousCart != null)
			{
				modelService.remove(previousCart);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.cart.OshCartFacade#addToCart(java.lang.String, long, java.lang.String)
	 */
	@Override
	public CartModificationData addToCartOrder(final String code, final long quantity, final String orderType)
			throws CommerceCartModificationException
	{
		JaloSession.getCurrentSession().setPriceFactory(new OshPriceFactory());
		final ProductModel product = getProductService().getProductForCode(code);
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartModification modification = oshCommerceCartService.addToCartOrder(cartModel, product, quantity,
				product.getUnit(), false, orderType);
		if (cartModel.getDiscounts() != null
				&& !cartModel.getDiscounts().isEmpty()
				&& ((cartModel.getDiscounts().get(0) instanceof OshPromotionVoucherModel) || (cartModel.getDiscounts().get(0) instanceof PromotionVoucherModel)))
		{
			try
			{
				oshVoucherService.updateVoucher(cartModel);
			}
			catch (final JaloPriceFactoryException e)
			{
				LOG.error(e);
			}
		}
		return getCartModificationConverter().convert(modification);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.cart.OshCartFacade#mergeCartEntry(long, long, java.lang.String)
	 */
	@Override
	public CartModificationData mergeCartEntry(final long entryNumber, final long quantity, final String orderType)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartModification modification = oshCommerceCartService.updateForMergeCartEntry(cartModel, entryNumber,
				quantity, orderType);
		return getCartModificationConverter().convert(modification);
	}

	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final long quantity)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartModification modification = getCommerceCartService().updateQuantityForCartEntry(cartModel, entryNumber,
				quantity);

		if (cartModel.getDiscounts() != null
				&& !cartModel.getDiscounts().isEmpty()
				&& ((cartModel.getDiscounts().get(0) instanceof OshPromotionVoucherModel) || (cartModel.getDiscounts().get(0) instanceof PromotionVoucherModel)))
		{
			try
			{
				oshVoucherService.updateVoucher(cartModel);
			}
			catch (final JaloPriceFactoryException e)
			{
				LOG.error(e);
			}
		}
		return getCartModificationConverter().convert(modification);
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the productService
	 */
	@Override
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	@Override
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the oshCommerceCartService
	 */
	public OshCommerceCartService getOshCommerceCartService()
	{
		return oshCommerceCartService;
	}

	/**
	 * @param oshCommerceCartService
	 *           the oshCommerceCartService to set
	 */
	public void setOshCommerceCartService(final OshCommerceCartService oshCommerceCartService)
	{
		this.oshCommerceCartService = oshCommerceCartService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.cart.OshCartFacade#isQuantityAvailableOnAdd(long, java.lang.String, long)
	 */
	@Override
	public boolean isQuantityAvailableOnAdd(final long newQty, final String productCode, final long productStock)
	{
		final ProductModel product = getProductService().getProductForCode(productCode);
		final CartModel cartModel = getCartService().getSessionCart();

		long cartLevel = 0;
		for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, product))
		{
			cartLevel += entryModel.getQuantity().longValue();
		}

		final long newQuantitytoAdd = cartLevel + newQty;

		if (newQuantitytoAdd <= productStock)
		{
			return true;
		}
		return false;
	}

}
