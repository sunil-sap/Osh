/**
 * 
 */
package com.hybris.osh.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.Arrays;

import javax.annotation.Resource;

import com.hybris.osh.core.orderprocessing.model.SendEmailToFriendProcessModel;
import com.hybris.osh.facades.product.data.OshProductData;


/**
 * Email send to friend functionality.
 * 
 */
public class SendEmailToFriendContext extends AbstractEmailContext
{
	@Resource(name = "productFacade")
	private ProductFacade productFacade;
	private EnumerationService enumerationService;
	private OshProductData productData;
	private String toMail = null;
	private String fromMail = null;
	private String message = null;

	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{


		if (businessProcessModel instanceof SendEmailToFriendProcessModel)
		{
			emailPageModel.setFromEmail(((SendEmailToFriendProcessModel) businessProcessModel).getFromEmail());
			super.init(businessProcessModel, emailPageModel);
			final ProductModel productModel = ((SendEmailToFriendProcessModel) businessProcessModel).getProduct();
			productData = (OshProductData) productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
					ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
					ProductOption.VARIANT_FULL, ProductOption.STOCK));
			if (productData.getImages() != null && productData.getImages().size() != 0 && !productData.getImages().isEmpty())
			{
				productData.setShowImage(true);
			}
			message = ((SendEmailToFriendProcessModel) businessProcessModel).getNotes();
			toMail = ((SendEmailToFriendProcessModel) businessProcessModel).getToEmail();
			fromMail = ((SendEmailToFriendProcessModel) businessProcessModel).getFromEmail();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
	 * processengine.model.BusinessProcessModel)
	 */
	@Override
	protected BaseSiteModel getSite(final BusinessProcessModel businessProcessModel)
	{
		if (businessProcessModel instanceof SendEmailToFriendProcessModel)
		{
			return ((SendEmailToFriendProcessModel) businessProcessModel).getSite();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final BusinessProcessModel businessProcessModel)
	{
		return null;
	}

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public OshProductData getProductData()
	{
		return productData;
	}

	public void setProductData(final OshProductData productData)
	{
		this.productData = productData;
	}

	public String getToMail()
	{
		return toMail;
	}

	public void setToMail(final String toMail)
	{
		this.toMail = toMail;
	}

	public String getFromMail()
	{
		return fromMail;
	}

	public void setFromMail(final String fromMail)
	{
		this.fromMail = fromMail;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(final String message)
	{
		this.message = message;
	}



}
