package com.hybris.osh.facades.storelocator.converters;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.storelocator.data.OpeningScheduleData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.storelocator.model.OpeningScheduleModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.osh.facades.storelocator.data.OshPointOfServiceData;


/**
 * Converter implementation for {@link PointOfServiceModel} as source and {@link OshPointOfServiceData} as target type.
 */
public class OshPointOfServiceConverter extends AbstractPopulatingConverter<PointOfServiceModel, OshPointOfServiceData>
{
	private Converter<MediaModel, ImageData> imageConverter;
	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<OpeningScheduleModel, OpeningScheduleData> openingScheduleConverter;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private ImageFormatMapping imageFormatMapping;
	private List<String> imageFormats;


	protected Converter<MediaModel, ImageData> getImageConverter()
	{
		return imageConverter;
	}

	@Required
	public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter)
	{
		this.imageConverter = imageConverter;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	@Required
	public void setOpeningScheduleConverter(final Converter<OpeningScheduleModel, OpeningScheduleData> openingScheduleConverter)
	{
		this.openingScheduleConverter = openingScheduleConverter;
	}

	@Required
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	@Required
	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
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

	protected List<String> getImageFormats()
	{
		return imageFormats;
	}

	@Required
	public void setImageFormats(final List<String> imageFormats)
	{
		this.imageFormats = imageFormats;
	}


	@Override
	protected OshPointOfServiceData createTarget()
	{
		return new OshPointOfServiceData();
	}

	@Override
	public void populate(final PointOfServiceModel source, final OshPointOfServiceData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setName(source.getName());
		target.setStoreID(source.getStoreID());
		if (source.getAddress() != null)
		{
			target.setAddress(getAddressConverter().convert(source.getAddress()));
			target.setState(source.getAddress().getRegion().getIsocode());
		}
		target.setActive(source.isActive());
		target.setDescription(source.getDescription());
		target.setLatitude(source.getLatitude());
		target.setLongitude(source.getLongitude());
		if (source.getMapIcon() != null)
		{
			target.setMapIcon(getImageConverter().convert(source.getMapIcon()));
		}
		//	
		if (source.getOpeningSchedule() != null)
		{
			target.setOpeningHours(openingScheduleConverter.convert(source.getOpeningSchedule()));
		}
		target.setStoreContent(source.getStoreContent());

		// Build up the list of images in the requested imageFormats
		final List<ImageData> storeImages = new ArrayList<ImageData>();
		final MediaContainerModel storeImageContainer = source.getStoreImage();
		if (storeImageContainer != null)
		{
			for (final String imageFormat : getImageFormats())
			{
				final String mediaFormatCode = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
				if (mediaFormatCode != null)
				{
					final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatCode);
					if (mediaFormat != null)
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(storeImageContainer, mediaFormat);
						if (media != null)
						{
							final ImageData imageData = getImageConverter().convert(media);
							imageData.setFormat(imageFormat);
							storeImages.add(imageData);
						}
					}
				}
			}
		}
		target.setStoreImages(storeImages);

		// Add the store features
		final Map<String, String> features = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(source.getFeatures()))
		{
			for (final StoreLocatorFeatureModel feature : source.getFeatures())
			{
				features.put(feature.getCode(), feature.getName());
			}
		}
		target.setFeatures(features);

		super.populate(source, target);
	}

	protected Converter<OpeningScheduleModel, OpeningScheduleData> getOpeningScheduleConverter()
	{
		return openingScheduleConverter;
	}

}
