/**
 * 
 */
package com.hybris.osh.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.mail.MailUtils;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;


/**
 *
 *
 */
public class DefaultOshCustomerEmailResolutionService extends DefaultCustomerEmailResolutionService
{
	private static final Logger LOG = Logger.getLogger(DefaultOshCustomerEmailResolutionService.class);


	@Override
	public String getEmailForCustomer(final CustomerModel customerModel)
	{
		if (isEmailValidForCustomer(customerModel))
		{
			if (customerModel.getName().equalsIgnoreCase(OshCoreConstants.GUEST_USER))
			{
				if (customerModel.getDefaultShipmentAddress() != null)
				{
					return customerModel.getDefaultShipmentAddress().getEmail();
				}
				else if (customerModel.getDefaultPaymentAddress() != null)
				{
					return customerModel.getDefaultPaymentAddress().getEmail();
				}
				else if (customerModel.getPaymentInfos() != null && !customerModel.getPaymentInfos().isEmpty())
				{
					final PaymentInfoModel paymentInfo = (PaymentInfoModel) customerModel.getPaymentInfos().toArray()[customerModel
							.getPaymentInfos().size() - 1];
					return paymentInfo.getBillingAddress().getEmail();
				}
			}
			else
			{
				return customerModel.getUid();
			}
		}
		else
		{
			if (customerModel.getPaymentInfos() != null && !customerModel.getPaymentInfos().isEmpty())
			{
				final PaymentInfoModel paymentInfo = (PaymentInfoModel) customerModel.getPaymentInfos().toArray()[customerModel
						.getPaymentInfos().size() - 1];
				if (paymentInfo.getBillingAddress() != null && paymentInfo.getBillingAddress().getEmail() != null)
				{
					return paymentInfo.getBillingAddress().getEmail();
				}
			}
		}

		return getConfigurationService().getConfiguration().getString(DEFAULT_CUSTOMER_KEY, DEFAULT_CUSTOMER_EMAIL);
	}

	private boolean isEmailValidForCustomer(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		String email = null;
		if (customerModel.getName().equalsIgnoreCase(OshCoreConstants.GUEST_USER))
		{
			if (customerModel.getDefaultShipmentAddress() != null)
			{
				email = customerModel.getDefaultShipmentAddress().getEmail();
			}
			else if (customerModel.getDefaultPaymentAddress() != null)
			{
				email = customerModel.getDefaultPaymentAddress().getEmail();
			}
			else if (customerModel.getPaymentInfos() != null && !customerModel.getPaymentInfos().isEmpty())
			{
				final PaymentInfoModel paymentInfo = (PaymentInfoModel) customerModel.getPaymentInfos().toArray()[customerModel
						.getPaymentInfos().size() - 1];
				email = paymentInfo.getBillingAddress().getEmail();
			}
		}
		else
		{
			email = customerModel.getUid();
		}
		try
		{
			MailUtils.validateEmailAddress(email, "customer email");
			return true;
		}
		catch (final EmailException e)
		{
			LOG.info("Given uid is not appropriate email " + email);
		}
		return false;
	}
}
