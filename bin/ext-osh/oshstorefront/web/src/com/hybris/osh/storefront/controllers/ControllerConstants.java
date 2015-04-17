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
package com.hybris.osh.storefront.controllers;

import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.acceleratorcms.model.components.PurchasedProductReferencesComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;

import com.hybris.osh.core.model.OshFooterComponentModel;
import com.hybris.osh.core.model.OshImageTextComponentModel;
import com.hybris.osh.core.model.OshLinkComponentModel;
import com.hybris.osh.core.model.OshNavBarComponentModel;
import com.hybris.osh.core.model.OshRotatingImagesComponentModel;


/**
 */
public interface ControllerConstants
{
	/**
	 * Class with action name constants
	 */
	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";

			/**
			 * Default CMS component controller
			 */
			String DefaultCMSComponent = _Prefix + "DefaultCMSComponentController";

			/**
			 * CMS components that have specific handlers
			 */
			String PurchasedProductReferencesComponent = _Prefix + PurchasedProductReferencesComponentModel._TYPECODE + _Suffix;
			String ProductReferencesComponent = _Prefix + ProductReferencesComponentModel._TYPECODE + _Suffix;
			String ProductCarouselComponent = _Prefix + ProductCarouselComponentModel._TYPECODE + _Suffix;
			String MiniCartComponent = _Prefix + MiniCartComponentModel._TYPECODE + _Suffix;
			String ProductFeatureComponent = _Prefix + ProductFeatureComponentModel._TYPECODE + _Suffix;
			String CategoryFeatureComponent = _Prefix + CategoryFeatureComponentModel._TYPECODE + _Suffix;
			String NavigationBarComponent = _Prefix + NavigationBarComponentModel._TYPECODE + _Suffix;
			String CMSLinkComponent = _Prefix + CMSLinkComponentModel._TYPECODE + _Suffix;
			String OshLinkComponent = _Prefix + OshLinkComponentModel._TYPECODE + _Suffix;
			String OshFooterComponent = _Prefix + OshFooterComponentModel._TYPECODE + _Suffix;
			String OshImageTextComponent = _Prefix + OshImageTextComponentModel._TYPECODE + _Suffix;
			String OshRotatingImagesComponent = _Prefix + OshRotatingImagesComponentModel._TYPECODE + _Suffix;
			String OshNavBarComponent = _Prefix + OshNavBarComponentModel._TYPECODE + _Suffix;
		}
	}

	/**
	 * s Class with view name constants
	 */
	interface Views
	{
		interface Cms
		{
			String ComponentPrefix = "cms/";
		}

		interface Pages
		{

			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";
				String AccountHomePage = "pages/account/accountHomePage";
				String AccountOrderHistoryPage = "pages/account/accountOrderHistoryPage";
				String AccountOrderPage = "pages/account/accountOrderPage";
				String AccountProfilePage = "pages/account/accountProfilePage";
				String AccountProfileEditPage = "pages/account/accountProfileEditPage";
				String AccountProfileEmailEditPage = "pages/account/accountProfileEmailEditPage";
				String AccountChangePasswordPage = "pages/account/accountChangePasswordPage";
				String AccountAddressBookPage = "pages/account/accountAddressBookPage";
				String AccountEditAddressPage = "pages/account/accountEditAddressPage";
				String AccountPaymentInfoPage = "pages/account/accountPaymentInfoPage";
				String AccountAddPaymentDetailPage = "pages/account/accountAddPaymentDetailPage";
				String AccountAddPaymentDetailPopup = "pages/account/accountAddPaymentDetailPopup";
				String AccountRegisterPage = "pages/account/accountRegisterPage";
				String AccountEditPage = "pages/account/accountEditPage";
				String EmptyPage = "pages/account/emptyPage";
			}

			interface Checkout
			{
				String CheckoutLoginPage = "pages/checkout/checkoutLoginPage";
				String CheckoutRegisterPage = "pages/checkout/checkoutRegisterPage";
				String CheckoutConfirmationPage = "pages/checkout/checkoutConfirmationPage";
			}

			interface WishList
			{
				String WishListLoginPage = "pages/wishList/wishListLoginPage";

			}

			interface ContactUs
			{
				String contactUsPage = "pages/layout/contactUsPage";

			}

			interface SingleStepCheckout
			{
				String CheckoutSummaryPage = "pages/checkout/single/checkoutSummaryPage";
			}

			interface MultiStepCheckout
			{
				String ChooseDeliveryAddressPage = "pages/checkout/multi/chooseDeliveryAddressPage";
				String AddEditDeliveryAddressPage = "pages/checkout/multi/addEditDeliveryAddressPage";
				String ChooseDeliveryMethodPage = "pages/checkout/multi/chooseDeliveryMethodPage";
				String ChoosePaymentMethodPage = "pages/checkout/multi/choosePaymentMethodPage";
				String AddPaymentMethodPage = "pages/checkout/multi/addPaymentMethodPage";
				String CheckoutSummaryPage = "pages/checkout/multi/checkoutSummaryPage";
				String HostedOrderPageErrorPage = "pages/checkout/multi/hostedOrderPageErrorPage";
				String HostedOrderPostPage = "pages/checkout/multi/hostedOrderPostPage";
			}

			interface Password
			{
				String PasswordResetRequestPage = "pages/password/passwordResetRequestPage";
				String PasswordResetChangePage = "pages/password/passwordResetChangePage";
			}

			interface Error
			{
				String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
			}

			interface Cart
			{
				String CartPage = "pages/cart/cartPage";
			}

			interface Cheetah
			{
				String CheetahSubscriberPage = "pages/cheetah/subscribeCheetahMailPage";
			}

			interface StoreFinder
			{
				String StoreFinderSearchPage = "pages/storeFinder/storeFinderSearchPage";
				String StoreFinderDetailsPage = "pages/storeFinder/storeFinderDetailsPage";
				String StoreFinderViewMapPage = "pages/storeFinder/storeFinderViewMapPage";
			}

			interface Misc
			{
				String MiscRobotsPage = "pages/misc/miscRobotsPage";
			}

			interface SubCategory
			{
				final String SubCategoryPage = "pages/category/subCategoryPage";
			}

			interface Product
			{
				String writeReview = "pages/product/writeReview";
				String StoreDataPage = "pages/product/StoreDataPage";
			}

			interface Layout
			{
				String BlankPage = "pages/layout/blankLayoutPage";
			}
		}

		interface Fragments
		{
			interface Cart
			{
				String AddToCartPopup = "fragments/cart/addToCartPopup";
				String MiniCartPanel = "fragments/cart/miniCartPanel";
				String MiniCartErrorPanel = "fragments/cart/miniCartErrorPanel";
				String CartPopup = "fragments/cart/cartPopup";
			}

			interface SingleStepCheckout
			{
				String DeliveryAddressFormPopup = "fragments/checkout/single/deliveryAddressFormPopup";
				String PaymentDetailsFormPopup = "fragments/checkout/single/paymentDetailsFormPopup";
			}

			interface WishList
			{

				String CreateNewWishListPopup = "fragments/wishList/createNewWishListPagePopup";
				String ChangeWishlistNamePopup = "fragments/wishList/changeWishlistNamePopup";
				String SendWishListPopup = "fragments/wishList/SendWishListPopup";


			}

			interface AddToWishList
			{
				String CreateNewWishListPopup = "fragments/addToWishList/createNewWishListPopup";
				String CreateUpdateMultipleWishListPopup = "fragments/addToWishList/createUpdateMultipleWishlist";
			}

			interface Product
			{
				String QuickViewPopup = "fragments/product/quickViewPopup";
				String ZoomImagesPopup = "fragments/product/zoomImagesPopup";
				String ReviewsTab = "fragments/product/reviewsTab";
			}



		}
	}
}
