/**
 * 
 */
package com.hybris.osh.facades.storename.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.List;

import com.hybris.osh.core.service.ContactUsEmailService;
import com.hybris.osh.facades.storename.OshStoreNameFacade;
import com.hybris.osh.facades.user.data.ContactUsFormData;



/**
 * This class will converting store name model in data
 * 
 */
public class DefaultOshStoreNameFacade implements OshStoreNameFacade
{


	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	/**
	 * @return the pointOfServiceConverter
	 */

	private ContactUsEmailService contactUsEmailService;

	/**
	 * @return the pointOfServiceConverter
	 */
	public Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	/**
	 * @param pointOfServiceConverter
	 *           the pointOfServiceConverter to set
	 */
	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}

	/**
	 * @return the contactUsEmailService
	 */
	public ContactUsEmailService getContactUsEmailService()
	{
		return contactUsEmailService;
	}

	/**
	 * @param contactUsEmailService
	 *           the contactUsEmailService to set
	 */
	public void setContactUsEmailService(final ContactUsEmailService contactUsEmailService)
	{
		this.contactUsEmailService = contactUsEmailService;
	}

	/*
	 * This method is sending contact us form data to Contact us email service as String
	 */
	@Override
	public void contactUsData(final ContactUsFormData contactUsData)
	{

		final String firstName = contactUsData.getFirstname();
		final String lastName = contactUsData.getLastname();
		final String emailAddress = contactUsData.getEmailAddress();
		final String emailContent = contactUsData.getEmailContent();
		final String storeName = contactUsData.getStoreName();
		final String subject = contactUsData.getSubject();
		contactUsEmailService.sendMail(firstName, lastName, emailAddress, emailContent, storeName, subject);
	}

	/*
	 * Converting model into data
	 */


	@Override
	public List<PointOfServiceData> storeNames()
	{
		final List<PointOfServiceData> posData = new ArrayList<PointOfServiceData>();
		final List<PointOfServiceModel> storeNames = contactUsEmailService.getStoreNames();
		for (final PointOfServiceModel pointOfServiceModel : storeNames)
		{
			final PointOfServiceData pos = getPointOfServiceConverter().convert(pointOfServiceModel);
			posData.add(pos);

		}
		return posData;

	}
}
