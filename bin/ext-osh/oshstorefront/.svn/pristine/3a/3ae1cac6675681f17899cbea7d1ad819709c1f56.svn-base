/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.storefront.controllers.misc;

import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;
import de.hybris.platform.acceleratorservices.storefinder.StoreFinderService;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
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

import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.facades.cart.OshCartFacade;
import com.hybris.osh.storefront.controllers.AbstractController;
import com.hybris.osh.storefront.controllers.ControllerConstants;


/**
 * Controller for Add to Cart functionality which is not specific to a certain page.
 */
@Controller
@Scope("tenant")
public class AddToCartController extends AbstractController
{
	protected static final Logger LOG = Logger.getLogger(AddToCartController.class);

	@Resource(name = "cartFacade")
	private OshCartFacade cartFacade;

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "storeFinderService")
	private StoreFinderService storeFinderService;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;

	@Resource(name = "stockService")
	private StockService stockService;


	@ResponseBody
	@RequestMapping(value = "/cart/add", method = RequestMethod.POST, produces = "application/json")
	public HashMap addToCart(@RequestParam("productCodePost") final String code, final Model model,
			@RequestParam(value = "qty", required = false, defaultValue = "1") final long qty,
			@RequestParam(value = "orderType", required = false) final String orderType,
			@RequestParam(value = "storeName", required = false) String storeName, final HttpServletRequest request)
	{
		final ProductData productData = productFacade.getProductForCodeAndOptions(code, Arrays.asList(ProductOption.BASIC,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				ProductOption.VARIANT_FULL, ProductOption.STOCK));
		model.addAttribute("product", productData);
		model.addAttribute("cartData", cartFacade.getSessionCart());
		request.getSession().setAttribute("orderType", orderType);
		final HashMap<String, String> cartMap = new HashMap<String, String>();
		cartMap.put("orderType", orderType);
		cartMap.put("redirectURL", ControllerConstants.Views.Fragments.Cart.AddToCartPopup);

		if (qty <= 0)
		{
			model.addAttribute("errorMsg", "basket.error.quantity.invalid");
			return cartMap;
		}

		if (orderType.equalsIgnoreCase(OshCoreConstants.WAREHOUSE))
		{
			if (!(cartFacade.isQuantityAvailableOnAdd(qty, code, productData.getStockLevel() != null ? productData.getStockLevel()
					.longValue() : 0L)))
			{

				cartMap.put("errorMsg", "The available quantity for this item is " + productData.getStockLevel().toString()
						+ ". Please update your desired quantity in order to add this item to your cart.");
				return cartMap;
			}
		}
		else
		{
			final ProductModel productModel = productService.getProductForCode(code);
			WarehouseModel wareHouseModel = null;
			int storeStock = 0;
			storeName = storeName != null ? storeName : (String) request.getSession().getAttribute("storeName");
			if (storeName != null)
			{
				wareHouseModel = warehouseService.getWarehouseForCode(storeName);
			}
			try
			{
				if (wareHouseModel != null)
				{
					storeStock = stockService.getStockLevelAmount(productModel, wareHouseModel);

				}
			}
			catch (final StockLevelNotFoundException e)
			{
				storeStock = 0;
			}

			if (!(cartFacade.isQuantityAvailableOnAdd(qty, code, storeStock)))
			{
				cartMap.put("errorMsg", "The available quantity for this item is " + storeStock
						+ ". Please update your desired quantity in order to add this item to your cart.");
				return cartMap;
			}

		}


		try
		{
			if (storeName != null)
			{
				CustomerModel custModel = null;
				final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(storeName);
				if (!(userService.getCurrentUser().getUid().equalsIgnoreCase("anonymous")))
				{
					custModel = (CustomerModel) userService.getCurrentUser();
				}
				if (custModel != null && custModel.getMyStore() != null
						&& !custModel.getMyStore().getName().equalsIgnoreCase(storeName))
				{
					final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
					final PointOfServiceModel pointOfService = storeFinderService
							.getPointOfServiceForName(currentBaseStore, storeName);
					custModel.setMyStore(pointOfService);
					modelService.save(custModel);
					request.getSession().setAttribute("storeName", storeName);
					request.getSession().setAttribute("zipCode", pointOfServiceData.getAddress().getPostalCode());
					request.getSession().setAttribute("storeUrl", request.getContextPath() + pointOfServiceData.getUrl());
				}

				else
				{
					request.getSession().setAttribute("zipCode", pointOfServiceData.getAddress().getPostalCode());
					request.getSession().setAttribute("storeName", storeName);
				}

			}

			final CartModificationData cartModification = cartFacade.addToCartOrder(code, qty, orderType);
			model.addAttribute("quantity", Long.valueOf(cartModification.getQuantityAdded()));
			model.addAttribute("entry", cartModification.getEntry());
			cartMap.remove("errorMsg");
			if (cartModification.getQuantityAdded() == 0L)
			{
				model.addAttribute("errorMsg", "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
			}
			else if (cartModification.getQuantityAdded() < qty)
			{
				model.addAttribute("errorMsg",
						"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
			}

			// Put in the cart again after it has been modified
			model.addAttribute("cartData", cartFacade.getSessionCart());
		}
		catch (final CommerceCartModificationException ex)
		{
			model.addAttribute("errorMsg", "basket.error.occurred");
			model.addAttribute("quantity", Long.valueOf(0L));
			LOG.warn("Couldn't add product of code " + code + " to cart.", ex);
		}
		return cartMap;
	}
}
