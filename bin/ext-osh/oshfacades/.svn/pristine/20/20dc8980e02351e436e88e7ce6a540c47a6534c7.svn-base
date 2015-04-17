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
package com.hybris.osh.facades.converters;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.osh.facades.product.data.OshProductData;


/**
 * Converter implementation for {@link SearchResultValueData} as source and {@link OshProductData} as target type.
 */
public class OshSearchResultProductConverter extends AbstractPopulatingConverter<SearchResultValueData, OshProductData>
{
	private static final Logger LOG = Logger.getLogger(OshSearchResultProductConverter.class);
	private ImageFormatMapping imageFormatMapping;
	private PriceDataFactory priceDataFactory;
	private UrlResolver<ProductData> productDataUrlResolver;
	private Populator<FeatureList, ProductData> productFeatureListPopulator;
	private ProductService productService;
	private CommonI18NService commonI18NService;
	private int defaultInStockLevel = 20;
	private Populator<ProductModel, ProductData> productStockPopulator;

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	protected ImageFormatMapping getImageFormatMapping()
	{
		return imageFormatMapping;
	}

	@Required
	public void setImageFormatMapping(final ImageFormatMapping imageFormatMapping)
	{
		this.imageFormatMapping = imageFormatMapping;
	}

	protected UrlResolver<ProductData> getProductDataUrlResolver()
	{
		return productDataUrlResolver;
	}

	@Required
	public void setProductDataUrlResolver(final UrlResolver<ProductData> productDataUrlResolver)
	{
		this.productDataUrlResolver = productDataUrlResolver;
	}

	protected Populator<FeatureList, ProductData> getProductFeatureListPopulator()
	{
		return productFeatureListPopulator;
	}

	@Required
	public void setProductFeatureListPopulator(final Populator<FeatureList, ProductData> productFeatureListPopulator)
	{
		this.productFeatureListPopulator = productFeatureListPopulator;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected int getDefaultInStockLevel()
	{
		return defaultInStockLevel;
	}

	// Optional
	public void setDefaultInStockLevel(final int defaultInStockLevel)
	{
		this.defaultInStockLevel = defaultInStockLevel;
	}

	protected Populator<ProductModel, ProductData> getProductStockPopulator()
	{
		return productStockPopulator;
	}

	@Required
	public void setProductStockPopulator(final Populator<ProductModel, ProductData> productStockPopulator)
	{
		this.productStockPopulator = productStockPopulator;
	}

	@Override
	protected OshProductData createTarget()
	{
		return new OshProductData();
	}

	@Override
	public void populate(final SearchResultValueData source, final OshProductData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		// Pull the values directly from the SearchResult object
		target.setCode(this.<String> getValue(source, "code"));
		target.setName(this.<String> getValue(source, "name"));
		target.setManufacturer(this.<String> getValue(source, "manufacturerName"));
		target.setDescription(this.<String> getValue(source, "description"));
		target.setSummary(this.<String> getValue(source, "summary"));
		target.setAverageRating(this.<Double> getValue(source, "reviewAvgRating"));
		target.setAvailabilityInd(this.<String> getValue(source, "availabilityInd"));

		final Boolean freeShippingFlag = this.<Boolean> getValue(source, "FreeShipping");
		target.setFreeShipping(freeShippingFlag == null ? false : freeShippingFlag.booleanValue());

		//target.setFreeShipping(this.<Boolean> getValue(source, "FreeShipping").booleanValue());

		final Boolean mapPriceFlag = this.<Boolean> getValue(source, "mapPriceFlag");
		target.setMapPriceType(mapPriceFlag == null ? false : mapPriceFlag.booleanValue());
		populatePrices(source, target);

		// Populate product's classification features
		getProductFeatureListPopulator().populate(getFeaturesList(source), target);

		final List<ImageData> images = createImageData(source);
		if (CollectionUtils.isNotEmpty(images))
		{
			target.setImages(images);
		}

		populateUrl(source, target);
		populatePromotions(source, target);
		populateStock(source, target);

		super.populate(source, target);
	}

	protected void populatePrices(final SearchResultValueData source, final OshProductData target)
	{
		// Pull the volume prices flag
		final Boolean volumePrices = this.<Boolean> getValue(source, "volumePrices");
		target.setVolumePricesFlag(volumePrices == null ? Boolean.FALSE : volumePrices);

		// Pull the price value for the current currency
		final Double salePriceValue = this.<Double> getValue(source, "salePriceValue");
		if (salePriceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceData.PriceType.BUY,
					BigDecimal.valueOf(salePriceValue.doubleValue()), "USD");
			target.setSalePriceData(priceData);
		}

		final Double regularPriceValue = this.<Double> getValue(source, "regularPriceValue");
		if (regularPriceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceData.PriceType.BUY,
					BigDecimal.valueOf(regularPriceValue.doubleValue()), "USD");
			target.setRegPriceData(priceData);
		}

		final Double mapPriceValue = this.<Double> getValue(source, "mapPriceValue");
		if (mapPriceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceData.PriceType.BUY,
					BigDecimal.valueOf(mapPriceValue.doubleValue()), "USD");
			target.setMapPriceData(priceData);
		}

		final Double priceValue = this.<Double> getValue(source, "priceValue");
		if (priceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceData.PriceType.BUY,
					BigDecimal.valueOf(priceValue.doubleValue()), getCommonI18NService().getCurrentCurrency().getIsocode());
			target.setPrice(priceData);
		}
	}

	protected void populateUrl(final SearchResultValueData source, final ProductData target)
	{
		final String url = this.<String> getValue(source, "url");
		if (StringUtils.isEmpty(url))
		{
			// Resolve the URL and set it on the product data
			target.setUrl(getProductDataUrlResolver().resolve(target));
		}
		else
		{
			target.setUrl(url);
		}
	}

	protected void populatePromotions(final SearchResultValueData source, final OshProductData target)
	{
		final String promotionCode = this.<String> getValue(source, "primaryPromotionCode");
		if (StringUtils.isNotEmpty(promotionCode))
		{
			final String primaryPromotionBannerUrl = this.<String> getValue(source, "primaryPromotionBanner");
			target.setPotentialPromotions(Collections.singletonList(createPromotionData(promotionCode, primaryPromotionBannerUrl)));
		}
	}

	protected void populateStock(final SearchResultValueData source, final OshProductData target)
	{
		final String stockLevelStatus = this.<String> getValue(source, "stockLevelStatus");
		if (StringUtils.isNotEmpty(stockLevelStatus))
		{
			final StockLevelStatus stockLevelStatusEnum = StockLevelStatus.valueOf(stockLevelStatus);

			if (StockLevelStatus.LOWSTOCK.equals(stockLevelStatusEnum))
			{
				// In case of low stock then make a call to the stock service to determine if in or out of stock.
				// In this case (low stock) it is ok to load the product from the DB and do the real stock check
				final ProductModel productModel = getProductService().getProductForCode(target.getCode());
				if (productModel != null)
				{
					getProductStockPopulator().populate(productModel, target);
				}
			}
			else if (StockLevelStatus.OUTOFSTOCK.equals(stockLevelStatusEnum))
			{
				target.setStockLevelStatus(StockLevelStatus.OUTOFSTOCK);
				target.setStockLevel(Integer.valueOf(0));
			}
			else if (StockLevelStatus.INSTOCK.equals(stockLevelStatusEnum))
			{
				target.setStockLevelStatus(StockLevelStatus.INSTOCK);
				target.setStockLevel(Integer.valueOf(getDefaultInStockLevel()));
			}
		}
	}

	protected List<ImageData> createImageData(final SearchResultValueData source)
	{
		final List<ImageData> result = new ArrayList<ImageData>();

		addImageData(source, "category", result);

		return result;
	}

	protected void addImageData(final SearchResultValueData source, final String imageFormat, final List<ImageData> images)
	{
		final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
		if (mediaFormatQualifier != null && !mediaFormatQualifier.isEmpty())
		{
			addImageData(source, imageFormat, mediaFormatQualifier, ImageData.ImageType.PRIMARY, images);
		}
	}

	protected void addImageData(final SearchResultValueData source, final String imageFormat, final String mediaFormatQualifier,
			final String type, final List<ImageData> images)
	{
		final String imgValue = getValue(source, "img-" + mediaFormatQualifier);
		if (imgValue != null && !imgValue.isEmpty())
		{
			final ImageData imageData = createImageData();
			imageData.setImageType(type);
			imageData.setFormat(imageFormat);
			imageData.setUrl(imgValue);

			images.add(imageData);
		}
	}

	protected PromotionData createPromotionData(final String code, final String imageUrl)
	{
		final PromotionData promotionData = createPromotionData();
		promotionData.setCode(code);

		if (imageUrl != null && !imageUrl.isEmpty())
		{
			final ImageData productBanner = createImageData();
			productBanner.setUrl(imageUrl);
			promotionData.setProductBanner(productBanner);
		}

		return promotionData;
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}

	protected FeatureList getFeaturesList(final SearchResultValueData source)
	{
		final List<Feature> featuresList = new ArrayList<Feature>();
		final Locale currentLocale = getCommonI18NService().getLocaleForLanguage(getCommonI18NService().getCurrentLanguage());

		if (source != null && source.getFeatureValues() != null && !source.getFeatureValues().isEmpty())
		{
			// Pull the classification features
			for (final Map.Entry<ClassAttributeAssignmentModel, Object> featureEntry : source.getFeatureValues().entrySet())
			{
				final ClassAttributeAssignmentModel classAttributeAssignment = featureEntry.getKey();
				final Object value = featureEntry.getValue();

				final FeatureValue featureValue = new FeatureValue(value, null, classAttributeAssignment.getUnit());
				final Feature feature;
				if (Boolean.TRUE.equals(classAttributeAssignment.getLocalized()))
				{
					final Map<Locale, List<FeatureValue>> featureMap = new HashMap<Locale, List<FeatureValue>>();
					featureMap.put(currentLocale, Collections.singletonList(featureValue));
					feature = new LocalizedFeature(classAttributeAssignment, featureMap, currentLocale);
				}
				else
				{
					feature = new UnlocalizedFeature(classAttributeAssignment, Collections.singletonList(featureValue));
				}
				featuresList.add(feature);
			}
		}
		return new FeatureList(featuresList);
	}

	protected PromotionData createPromotionData()
	{
		return new PromotionData();
	}

	protected ImageData createImageData()
	{
		return new ImageData();
	}
}
