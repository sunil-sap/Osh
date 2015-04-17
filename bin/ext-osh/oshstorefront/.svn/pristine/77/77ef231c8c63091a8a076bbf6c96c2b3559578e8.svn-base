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
package com.hybris.osh.storefront.controllers.pages;


import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;
import de.hybris.platform.acceleratorservices.storefinder.StoreFinderService;
import de.hybris.platform.acceleratorservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageData.ImageType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hybris.osh.core.model.OshVariantProductModel;
import com.hybris.osh.core.model.email.EmailToFriendModel;
import com.hybris.osh.events.MailSendToFriendEvent;
import com.hybris.osh.facades.inventoryvalidation.InventoryValidation;
import com.hybris.osh.facades.product.data.OshProductData;
import com.hybris.osh.facades.product.impl.OshProductFacade;
import com.hybris.osh.facades.storelocator.data.OshPointOfServiceData;
import com.hybris.osh.storefront.breadcrumb.Breadcrumb;
import com.hybris.osh.storefront.breadcrumb.impl.ProductBreadcrumbBuilder;
import com.hybris.osh.storefront.constants.WebConstants;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.controllers.util.ProductDataHelper;
import com.hybris.osh.storefront.forms.ReviewForm;
import com.hybris.osh.storefront.forms.SendEmailForm;
import com.hybris.osh.storefront.util.MetaSanitizerUtil;
import com.hybris.osh.storefront.variants.VariantSortStrategy;


/**
 * Controller for product details page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/p/{productCode}")
public class ProductPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(ProductPageController.class);

	@Resource(name = "productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;

	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "categoryConverter")
	private Converter<CategoryModel, CategoryData> categoryConverter;

	@Resource(name = "categoryDataUrlResolver")
	private UrlResolver<CategoryData> categoryDataUrlResolver;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "variantSortStrategy")
	private VariantSortStrategy variantSortStrategy;

	@Resource(name = "oshStoreFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "inventoryValidation")
	private InventoryValidation inventoryValidation;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "storeFinderService")
	private StoreFinderService storeFinderService;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "oshProductFacade")
	private OshProductFacade oshProductFacade;

	private static final String GEOPLUGIN_URL = "geopluginurl";
	private static final String LOCATION = "location";
	private static final int INITIAL_LOCATIONS_TO_DISPLAY = 5;
	private static final int MAX_LOCATIONS_TO_DISPLAY = 100;
	private static final String INITIAL_LOCATIONS_TO_DISPLAY_TEXT = "" + INITIAL_LOCATIONS_TO_DISPLAY; //NOPMD
	private static final String STORENAME = "storeName";
	private static final String ZIPCODE = "zipCode";
	private static final String NOSTORE = "noStore";
	private static final String POSRESULT = "posResults";

	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET)
	public String productDetail(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{
		final OshVariantProductModel baseModel = (OshVariantProductModel) productService.getProductForCode(productCode);
		final ProductModel productModel = baseModel.getBaseProduct();
		final String redirection = checkRequestUrl(request, response, productModelUrlResolver.resolve(baseModel));
		final boolean storeFlag = oshProductFacade.getStoreFlag();
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}
		updatePageTitle(productModel, model);
		populateProductDetailForDisplay(productModel, model, request);
		model.addAttribute(new ReviewForm());
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.REVIEW, ProductOption.GALLERY));
		final int totalReviews = productData.getReviews().size();
		model.addAttribute("totalReviews", Integer.valueOf(totalReviews));
		model.addAttribute("showingReviews", Integer.valueOf(productData.getReviews().size()));
		model.addAttribute("pageType", PageType.Product);
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productModel.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productModel.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);

		if (storeFlag)
		{
			getStoreDetails(baseModel, model, request);
		}

		return getViewForPage(model);
	}

	protected PageableData preparePage(final int pageSize)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0); // Always on the first page
		pageableData.setPageSize(pageSize); // Adjust pageSize to see more
		return pageableData;
	}

	@RequestMapping(value = "/zoomImages", method = RequestMethod.GET)
	public String showZoomImages(@PathVariable("productCode") final String productCode,
			@RequestParam(value = "galleryPosition", required = false) final String galleryPosition, final Model model)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Collections.singleton(ProductOption.GALLERY));
		final List<Map<String, ImageData>> images = getGalleryImages(productData);
		model.addAttribute("galleryImages", images);
		model.addAttribute("product", productData);
		if (galleryPosition != null)
		{
			try
			{
				model.addAttribute("zoomImageUrl", images.get(Integer.parseInt(galleryPosition)).get("zoom").getUrl());
			}
			catch (final IndexOutOfBoundsException ioebe)
			{
				model.addAttribute("zoomImageUrl", "");
			}
			catch (final NumberFormatException e)
			{
				model.addAttribute("zoomImageUrl", "");
			}
		}
		return ControllerConstants.Views.Fragments.Product.ZoomImagesPopup;
	}

	@RequestMapping(value = "/myStore/{storeName}", method = RequestMethod.GET, produces = "application/json")
	public String makeMyStore(@PathVariable("productCode") final String productCode,
			@PathVariable("storeName") final String storeName, final Model model, final HttpServletRequest request,
			final HttpServletResponse response)
	{

		boolean notGoogleBot = true;

		final String userAgent = request.getHeader("user-agent");

		if (userAgent != null && !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("bot"))
		{
			notGoogleBot = false;
		}

		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		final PointOfServiceModel pointOfService = storeFinderService.getPointOfServiceForName(currentBaseStore, storeName);
		final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(storeName);
		if (!customerModel.getUid().equalsIgnoreCase("anonymous"))
		{
			customerModel.setMyStore(pointOfService);
			modelService.save(customerModel);
		}
		if (pointOfService.getAddress() != null && pointOfService.getAddress().getPostalcode() != null)
		{
			if (notGoogleBot)
			{
				final int pageSize = 5;
				final PageableData pageableData = preparePage(pageSize);
				final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.locationSearch(pointOfService
						.getAddress().getPostalcode(), pageableData);
				request.getSession().setAttribute(POSRESULT, searchResult);
				LOG.info("Google Map Call in during make this my store");
			}
		}
		//request.getSession().setAttribute("storeName", storeName);
		setCookie(request, response, storeName, pointOfServiceData.getAddress().getPostalCode());
		return "redirect:/**/p/" + productCode;
	}

	protected void setCookie(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse, final String storeName,
			final String zipCode)
	{

		final Cookie[] cookie = httpRequest.getCookies();
		boolean storeZipCodeCookieExist = false;//flag for zipCode
		if (cookie != null)
		{
			for (Cookie storeCookie : cookie)
			{

				if (storeCookie.getName().equals("store"))
				{
					storeCookie.setValue(storeName);
					storeCookie.setMaxAge(Integer.MAX_VALUE);
					storeCookie.setPath("/");
					httpResponse.addCookie(storeCookie);
					httpRequest.getSession().setAttribute(STORENAME, storeName);
				}
				else
				{
					storeCookie = new Cookie("store", storeName);
					storeCookie.setMaxAge(Integer.MAX_VALUE);
					storeCookie.setPath("/");
					httpResponse.addCookie(storeCookie);
					httpRequest.getSession().setAttribute(STORENAME, storeName);
					httpRequest.getSession().setAttribute(NOSTORE, false);
				}
				//set Zip Code Cookie
				if (storeCookie.getName().equals("storeZipCode"))
				{
					storeCookie.setValue(zipCode);
					storeCookie.setMaxAge(Integer.MAX_VALUE);
					storeCookie.setPath("/");
					httpResponse.addCookie(storeCookie);
					httpRequest.getSession().setAttribute(ZIPCODE, zipCode);
					storeZipCodeCookieExist = true;
				}
			}
		}
		//set new Cookie for Zip Code
		if (!storeZipCodeCookieExist)
		{
			Cookie storeZipCodeCookie = null;
			storeZipCodeCookie = new Cookie("storeZipCode", zipCode);
			storeZipCodeCookie.setMaxAge(Integer.MAX_VALUE);
			storeZipCodeCookie.setPath("/");
			httpResponse.addCookie(storeZipCodeCookie);
			httpRequest.getSession().setAttribute(ZIPCODE, zipCode);
		}
	}

	@RequestMapping(value = "/quickView", method = RequestMethod.GET)
	public String showQuickView(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request)
	{
		final OshVariantProductModel baseModel = (OshVariantProductModel) productService.getProductForCode(productCode);
		final ProductModel productModel = baseModel.getBaseProduct();
		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				ProductOption.VARIANT_FULL, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW));
		final boolean storeFlag = oshProductFacade.getStoreFlag();
		populateProductData(productData, model, request);

		model.addAttribute("oshVariantProduct", productData.getVariantOptions().get(0));

		if (storeFlag)
		{
			getStoreDetails(baseModel, model, request);
		}
		return ControllerConstants.Views.Fragments.Product.QuickViewPopup;
	}

	@RequestMapping(value = "/review", method = RequestMethod.POST)
	public String postReview(@PathVariable final String productCode, @Valid final ReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		if (result.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "review.general.error");
			model.addAttribute("showReviewForm", Boolean.TRUE);
			populateProductDetailForDisplay(productModel, model, request);
			storeCmsPageInModel(model, getPageForProduct(productModel));
			return getViewForPage(model);
		}

		final ReviewData review = new ReviewData();
		review.setHeadline(form.getHeadline());
		review.setComment(form.getComment());
		review.setRating(form.getRating());
		review.setAlias(form.getAlias());
		productFacade.postReview(productCode, review);
		redirectAttrs.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("review.confirmation.thank.you.title"));
		return REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
	}

	@RequestMapping(value = "/reviewhtml/{numberOfReviews}", method = RequestMethod.GET)
	public String reviewHtml(@PathVariable("productCode") final String productCode,
			@PathVariable("numberOfReviews") final String numberOfReviews, final Model model, final HttpServletRequest request)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION));
		populateProductData(productData, model, request);
		model.addAttribute(new ReviewForm());
		final List reviews = (List) productData.getReviews();

		final int totalReviews = productData.getReviews().size();
		int numReviews = 0;
		if (!"all".equals(numberOfReviews))
		{
			numReviews = Integer.parseInt(numberOfReviews);
			if (numReviews > totalReviews)
			{
				numReviews = totalReviews;
			}
			productData.setReviews(reviews.subList(0, numReviews));
		}

		model.addAttribute("totalReviews", Integer.valueOf(totalReviews));
		model.addAttribute("showingReviews", Integer.valueOf(productData.getReviews().size()));
		model.addAttribute("product", productData);

		return ControllerConstants.Views.Fragments.Product.ReviewsTab;
	}

	protected void updatePageTitle(final ProductModel productModel, final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveProductPageTitle(productModel));
	}

	@ExceptionHandler(UnknownIdentifierException.class)
	public String handleUnknownIdentifierException(final UnknownIdentifierException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	protected void populateProductDetailForDisplay(final ProductModel productModel, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final OshProductData productData = (OshProductData) productFacade.getProductForOptions(productModel, Arrays.asList(
				ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				ProductOption.VARIANT_FULL, ProductOption.STOCK));

		final SendEmailForm sendEmailForm = new SendEmailForm();
		sendEmailForm.setProductModel(productModel);
		model.addAttribute("sendEmailForm", sendEmailForm);

		if (productData.getClassifications() != null)
		{
			final Collection<ClassificationData> specifications = productData.getClassifications();

			model.addAttribute("specifications", specifications);
		}

		if (productData.getDescription() != null && !productData.getDescription().isEmpty())
		{
			int index = 0;
			if (productData.getDescription().contains(". "))
			{
				index = productData.getDescription().indexOf(". ") + 1;
			}
			else if (productData.getDescription().contains("<ul>"))
			{
				index = productData.getDescription().indexOf("<ul>");
			}
			else
			{
				index = productData.getDescription().length();
			}

			final String summary = productData.getDescription().substring(0, index);

			/*
			 * productData.getDescription() .substring(0, index); final boolean validString = index > 100 ?
			 *
			 * Character.isWhitespace(summary.charAt(100)) : Character.isWhitespace(summary .charAt(index - 1));
			 */

			//			if (validString)
			//			{
			model.addAttribute("summary", summary);
			//			}
			//			else
			//			{
			//				model.addAttribute("summary", summary.substring(0, summary.length()));
			//
			//			}
		}

		sortVariantOptionData(productData);
		storeCmsPageInModel(model, getPageForProduct(productModel));
		populateProductData(productData, model, request);

		final List<Breadcrumb> breadcrumbs = productBreadcrumbBuilder.getBreadcrumbs(productModel);

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

		// Note: This is the index of the category above the product's
		//       supercategory
		int productSuperSuperCategoryIndex = breadcrumbs.size() - 3;
		final List<CategoryData> superCategories = new ArrayList<CategoryData>();









		if (productSuperSuperCategoryIndex == 0)
		{
			// The category at index 0 is never displayed as a supercategory; for
			// display purposes, the category at index 1 is the root category
			productSuperSuperCategoryIndex = 1;
		}

		if (productSuperSuperCategoryIndex > 0)
		{
			// When product has any supercategory
			final Breadcrumb productSuperSuperCategory = breadcrumbs.get(productSuperSuperCategoryIndex);
			final CategoryModel superSuperCategory = commerceCategoryService.getCategoryForCode(productSuperSuperCategory
					.getCategoryCode());


			for (final CategoryModel superCategory : superSuperCategory.getCategories())
			{
				final CategoryData categoryData = new CategoryData();

				categoryData.setName(superCategory.getName());
				categoryData.setUrl(categoryDataUrlResolver.resolve(categoryConverter.convert(superCategory)));

				superCategories.add(categoryData);
			}
		}

		model.addAttribute(WebConstants.SUPERCATEGORIES_KEY, superCategories);
	}

	protected void populateProductData(final ProductData productData, final Model model, final HttpServletRequest request)
	{
		ProductDataHelper.setCurrentProduct(request, productData.getCode());
		model.addAttribute("galleryImages", getGalleryImages(productData));
		model.addAttribute("oshProduct", productData);

		if (productData.getVariantOptions().get(0) != null)
		{
			model.addAttribute("oshVariantProduct", productData.getVariantOptions().get(0));
		}

	}

	protected void sortVariantOptionData(final ProductData productData)
	{
		if (CollectionUtils.isNotEmpty(productData.getBaseOptions()))
		{
			for (final BaseOptionData baseOptionData : productData.getBaseOptions())
			{
				if (CollectionUtils.isNotEmpty(baseOptionData.getOptions()))
				{
					Collections.sort(baseOptionData.getOptions(), variantSortStrategy);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(productData.getVariantOptions()))
		{
			Collections.sort(productData.getVariantOptions(), variantSortStrategy);
		}
	}

	protected AbstractPageModel getPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		return cmsPageService.getPageForProduct(product);
	}

	protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData)
	{

		final ProductModel productModel = productService.getProductForCode(productData.getCode());

		final ProductData OshProductImageData = productFacade.getProductForOptions(productModel,
				Collections.singleton(ProductOption.GALLERY));

		final List<Map<String, ImageData>> galleryImages = new ArrayList<Map<String, ImageData>>();
		if (CollectionUtils.isNotEmpty(OshProductImageData.getImages()))
		{
			final List<ImageData> images = new ArrayList<ImageData>();
			for (final ImageData image : OshProductImageData.getImages())
			{
				if (ImageType.GALLERY.equals(image.getImageType()))
				{
					images.add(image);
				}
			}
			Collections.sort(images, new Comparator<ImageData>()
			{
				@Override
				public int compare(final ImageData image1, final ImageData image2)
				{
					return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
				}
			});

			if (CollectionUtils.isNotEmpty(images))
			{
				int currentIndex = images.get(0).getGalleryIndex().intValue();
				Map<String, ImageData> formats = new HashMap<String, ImageData>();
				for (final ImageData image : images)
				{
					if (currentIndex != image.getGalleryIndex().intValue())
					{
						galleryImages.add(formats);
						formats = new HashMap<String, ImageData>();
						currentIndex = image.getGalleryIndex().intValue();
					}
					formats.put(image.getFormat(), image);
				}
				if (!formats.isEmpty())
				{
					galleryImages.add(formats);
				}
			}
		}
		return galleryImages;
	}


	@RequestMapping(value = "/writeReview", method = RequestMethod.GET)
	public String writeReview(@PathVariable final String productCode, final Model model) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		updatePageTitle(productModel, model);

		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW));
		model.addAttribute("productData", productData);
		final int totalReviews = productData.getReviews().size();
		model.addAttribute("totalReviews", Integer.valueOf(totalReviews));
		model.addAttribute("showingReviews", Integer.valueOf(productData.getReviews().size()));
		model.addAttribute(new ReviewForm());
		storeCmsPageInModel(model, getPageForProduct(productModel));
		return ControllerConstants.Views.Pages.Product.writeReview;
	}

	@RequestMapping(value = "/writeReview", method = RequestMethod.POST)
	public String writeReview(@PathVariable final String productCode, @Valid final ReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW));
		model.addAttribute("productData", productData);
		if (result.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "review.general.error");
			populateProductDetailForDisplay(productModel, model, request);
			storeCmsPageInModel(model, getPageForProduct(productModel));
			return ControllerConstants.Views.Pages.Product.writeReview;
		}

		final ReviewData review = new ReviewData();
		review.setHeadline(form.getHeadline());
		review.setComment(form.getComment());
		review.setRating(form.getRating());
		review.setAlias(form.getAlias());
		productFacade.postReview(productCode, review);
		redirectAttrs.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("review.confirmation.thank.you.title"));
		return REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
	}

	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event)
	{
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		return event;
	}

	@ResponseBody
	@RequestMapping(value = "/sendEmailToFriend.json", method = RequestMethod.POST)
	public String sendEmailToFriend(@PathVariable("productCode") final String productCode, @Valid final SendEmailForm form)
			throws CMSItemNotFoundException
	{
		final OshVariantProductModel baseModel = (OshVariantProductModel) productService.getProductForCode(productCode);
		final ProductModel productModel = baseModel.getBaseProduct();
		form.setProductModel(productModel);
		final EmailToFriendModel emailToFriendModel = new EmailToFriendModel();
		emailToFriendModel.setFromEmail(form.getFrom_email());
		emailToFriendModel.setToEmail(form.getTo_emailadd());
		emailToFriendModel.setNotes(form.getNotes());
		emailToFriendModel.setSubject(form.getSubject());
		emailToFriendModel.setProduct(productModel);
		eventService.publishEvent(initializeEvent(new MailSendToFriendEvent(emailToFriendModel)));
		return "success";

	}


	@RequestMapping(value = "/findStore", method = RequestMethod.GET, produces = "application/json")
	public String findStores(
			@RequestParam(value = "q") final String locationQuery,
			@PathVariable("productCode") final String productCode,
			@RequestParam(value = "more", defaultValue = INITIAL_LOCATIONS_TO_DISPLAY_TEXT, required = false) final int requestedPageSize,
			final Model model, final HttpServletRequest request) throws GeoLocatorException, MapServiceException,
			CMSItemNotFoundException

	{

		final String userAgent = request.getHeader("user-agent");

		if (!(userAgent != null && !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("bot")))
		{
			final ProductModel productModel = productService.getProductForCode(productCode);
			final PageableData pageableData = preparePage(50);
			final Map<PointOfServiceData, StockLevelStatus> posMap = new LinkedHashMap<PointOfServiceData, StockLevelStatus>();

			final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.locationSearch(locationQuery,
					pageableData);
			LOG.info("Google Map Call ...........to find a store");


			final List<PointOfServiceData> posList = new ArrayList<PointOfServiceData>();

			for (final PointOfServiceData pos : searchResult.getResults())
			{
				String str = pos.getFormattedDistance();
				str = str.substring(0, str.indexOf("Miles")).replaceAll(",", "");
				if (Float.parseFloat(str) < 50 && ((OshPointOfServiceData) pos).isActive())
				{
					posList.add(pos);
					posMap.put(pos, inventoryValidation.storeStock(pos.getName(), productModel));
				}
			}
			searchResult.setResults(posList);
			model.addAttribute("stores", posMap);
			model.addAttribute("productCode", productCode);
			if (searchResult.getResults().isEmpty())
			{
				GlobalMessages.addErrorMessage(model, "storelocator.error.no.results.subtitle");
			}
		}
		return ControllerConstants.Views.Pages.Product.StoreDataPage;
	}



	@SuppressWarnings("boxing")
	protected void getStoreDetails(final ProductModel productModel, final Model model, final HttpServletRequest request)
	{

		boolean notGoogleBot = true;

		final String userAgent = request.getHeader("user-agent");

		if (userAgent != null && !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("bot"))
		{
			notGoogleBot = false;
		}
		final PageableData pageableData = preparePage(50);
		final String url = configurationService.getConfiguration().getString(GEOPLUGIN_URL) + request.getRemoteAddr();
		LOG.info(url);
		model.addAttribute("bopisEnabled", configurationService.getConfiguration().getBoolean("bopisEnabled"));
		final Map<PointOfServiceData, StockLevelStatus> posMap = new LinkedHashMap<PointOfServiceData, StockLevelStatus>();
		CustomerModel customerModel = null;
		if (!userService.getCurrentUser().getUid().equals("anonymous"))
		{
			customerModel = (CustomerModel) userService.getCurrentUser();
		}
		String myStore = null;
		String zipCode = null;
		StoreFinderSearchPageData<PointOfServiceData> posResult = null;
		final String userLocation = (String) request.getSession().getAttribute("storeName");

		if (customerModel != null && customerModel.getMyStore() != null
				&& customerModel.getMyStore().getAddress().getPostalcode() != null)
		{
			myStore = customerModel.getMyStore().getName();
			zipCode = customerModel.getMyStore().getAddress().getPostalcode();
			final PointOfServiceData pos = storeFinderFacade.getPointOfServiceForName(myStore);
			if (pos.getName() == null || pos.getName().isEmpty())
			{
				return;
			}
			if (pos.getUrl() == null)
			{
				pos.setUrl("/store/" + pos.getName() + "?lat=" + pos.getLatitude() + "&long=" + pos.getLongitude() + "&q="
						+ pos.getName());
			}

			posMap.put(pos, inventoryValidation.storeStock(myStore, productModel));
			model.addAttribute("myStore", customerModel.getMyStore().getName());
		}
		else if (userLocation != null && !(userLocation.equals("noStore")))
		{
			myStore = userLocation;
			zipCode = (String) request.getSession().getAttribute("zipCode");
			final PointOfServiceData pos = storeFinderFacade.getPointOfServiceForName(userLocation);
			if (pos.getName() == null || pos.getName().isEmpty())
			{
				return;
			}
			if (pos.getUrl() == null)
			{
				pos.setUrl("/store/" + pos.getName() + "?lat=" + pos.getLatitude() + "&long=" + pos.getLongitude() + "&q="
						+ pos.getName());
			}
			posMap.put(pos, inventoryValidation.storeStock(userLocation, productModel));
			model.addAttribute("myStore", userLocation);
		}

		if (request.getSession().getAttribute(POSRESULT) != null)
		{
			LOG.info("No Call in product Page Controller");
			posResult = (StoreFinderSearchPageData<PointOfServiceData>) request.getSession().getAttribute(POSRESULT);
		}
		else
		{
			if (myStore != null && !(myStore.equals("noStore")))
			{
				final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(myStore);
				if (pointOfServiceData.getName() == null || pointOfServiceData.getName().isEmpty())
				{
					return;
				}

				if (pointOfServiceData.getAddress() != null && pointOfServiceData.getAddress().getPostalCode() != null)
				{
					if (notGoogleBot)
					{
						final String postalCode = pointOfServiceData.getAddress().getPostalCode();
						posResult = storeFinderFacade.locationSearch(postalCode, pageableData);
						LOG.info("Google Map Call.............for user's default store");
						request.getSession().setAttribute(POSRESULT, posResult);
						request.setAttribute(POSRESULT, posResult);
					}
				}
			}
		}
		/*
		 * else { LOG.info("Product Page .........Google Map Call"); posResult = storeFinderFacade.locationSearch(zipCode,
		 * pageableData); request.getSession().setAttribute("posResult", posResult); }
		 */
		if (posResult != null)
		{
			for (final PointOfServiceData pos : posResult.getResults())
			{
				String str = pos.getFormattedDistance();
				str = str.substring(0, str.indexOf("Miles")).replaceAll(",", "");
				if (Float.parseFloat(str) < 50 && ((OshPointOfServiceData) pos).isActive()
						&& !pos.getName().equalsIgnoreCase(myStore))
				{
					posMap.put(pos, inventoryValidation.storeStock(pos.getName(), productModel));
				}
			}
		}

		model.addAttribute("stores", posMap);

	}
}
