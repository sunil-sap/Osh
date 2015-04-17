package com.hybris.osh.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.hybris.osh.core.service.OshCustomerAccountService;


public class DefaultOshCustomerAccountService extends DefaultCustomerAccountService implements OshCustomerAccountService
{
	@Resource(name = "userService")
	private UserService userService;

	@Override
	public CreditCardPaymentInfoModel createPaymentSubscription(final CustomerModel customerModel, final CardInfo cardInfo,
			final BillingInfo billingInfo, final String titleCode, final String paymentProvider, final boolean saveInAccount)
			throws IllegalArgumentException
	{
		validateParameterNotNull(customerModel, "Customer cannot be null");
		validateParameterNotNull(cardInfo, "CardInfo cannot be null");
		validateParameterNotNull(billingInfo, "billingInfo cannot be null");
		validateParameterNotNull(paymentProvider, "PaymentProvider cannot be null");
		final CurrencyModel currencyModel = getCurrency(customerModel);
		validateParameterNotNull(currencyModel, "Customer session currency cannot be null");

		final Currency currency = getI18nService().getBestMatchingJavaCurrency(currencyModel.getIsocode());

		final AddressModel billingAddress = getModelService().create(AddressModel.class);
		if (StringUtils.isNotBlank(titleCode))
		{
			final TitleModel title = new TitleModel();
			title.setCode(titleCode);
			billingAddress.setTitle(getFlexibleSearchService().getModelByExample(title));
		}
		billingAddress.setFirstname(billingInfo.getFirstName());
		billingAddress.setLastname(billingInfo.getLastName());
		billingAddress.setLine1(billingInfo.getStreet1());
		billingAddress.setLine2(billingInfo.getStreet2());
		billingAddress.setTown(billingInfo.getCity());
		billingAddress.setPostalcode(billingInfo.getPostalCode());
		billingAddress.setPhone1(billingInfo.getPhoneNumber());

		final CountryModel country = getCommonI18NService().getCountry(billingInfo.getCountry());
		billingAddress.setCountry(country);

		final RegionModel regionModel = getCommonI18NService().getRegion(country, billingInfo.getState());
		billingAddress.setRegion(regionModel);


		if (userService.getCurrentUser().getName().equalsIgnoreCase("Guest User"))
		{
			billingAddress.setEmail(billingInfo.getEmail());
		}
		else
		{
			final String email = getCustomerEmailResolutionService().getEmailForCustomer(customerModel);
			billingAddress.setEmail(email);
		}

		final String merchantTransactionCode = customerModel.getUid() + "-" + UUID.randomUUID();
		final NewSubscription subscription = getPaymentService().createSubscription(merchantTransactionCode, paymentProvider,
				currency, billingAddress, cardInfo);

		if (StringUtils.isNotBlank(subscription.getSubscriptionID()))
		{
			final StringBuilder owner = new StringBuilder();
			owner.append(billingInfo.getFirstName());
			owner.append(" ");
			owner.append(billingInfo.getLastName());
			final CreditCardPaymentInfoModel cardPaymentInfoModel = getModelService().create(CreditCardPaymentInfoModel.class);
			cardPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
			cardPaymentInfoModel.setUser(customerModel);
			cardPaymentInfoModel.setSubscriptionId(subscription.getSubscriptionID());
			cardPaymentInfoModel.setNumber(getMaskedCardNumber(cardInfo.getCardNumber()));
			cardPaymentInfoModel.setType(cardInfo.getCardType());
			cardPaymentInfoModel.setCcOwner(owner.toString());
			cardPaymentInfoModel.setValidToMonth(String.valueOf(cardInfo.getExpirationMonth()));
			cardPaymentInfoModel.setValidToYear(String.valueOf(cardInfo.getExpirationYear()));
			if (cardInfo.getIssueMonth() != null)
			{
				cardPaymentInfoModel.setValidFromMonth(String.valueOf(cardInfo.getIssueMonth()));
			}
			if (cardInfo.getIssueYear() != null)
			{
				cardPaymentInfoModel.setValidFromYear(String.valueOf(cardInfo.getIssueYear()));
			}

			cardPaymentInfoModel.setSubscriptionId(subscription.getSubscriptionID());
			cardPaymentInfoModel.setSaved(saveInAccount);

			billingAddress.setOwner(cardPaymentInfoModel);
			cardPaymentInfoModel.setBillingAddress(billingAddress);

			getModelService().saveAll(billingAddress, cardPaymentInfoModel);
			getModelService().refresh(customerModel);

			addPaymentInfo(customerModel, cardPaymentInfoModel);

			return cardPaymentInfoModel;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.OshCustomerAccountService#oshUpdateProfile(de.hybris.platform.core.model.user.
	 * CustomerModel, java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	public void oshUpdateProfile(final CustomerModel customerModel, final String title, final String name, final String uid,
			final Long phone, final String birthday, final boolean specialoffer, final boolean storenewsletter,
			final boolean cluborchardinfo, final String loyaltyNumber) throws DuplicateUidException
	{
		//customerModel.setTitle(getUserService().getTitleForCode(title));
		customerModel.setBirthDay(birthday);
		customerModel.setName(name);
		customerModel.setPhone(phone);
		customerModel.setUid(uid);
		customerModel.setClubOrchardInfo(Boolean.valueOf(cluborchardinfo));
		customerModel.setStoreNewsLetter(Boolean.valueOf(storenewsletter));
		customerModel.setSpecialOffers(Boolean.valueOf(specialoffer));
		customerModel.setLoyaltyNumber(loyaltyNumber);

		internalSaveCustomer(customerModel);
	}

	@Override
	public void updateProfile(final CustomerModel customerModel, final String name, final String login)
			throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		customerModel.setUid(login);
		customerModel.setName(name);
		internalSaveCustomer(customerModel);
	}


	@Override
	public String getGuestUserPasswordToken(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
		final SecureToken data = new SecureToken(customerModel.getUid(), timeStamp);
		final String token = getSecureTokenService().encryptData(data);
		customerModel.setToken(token);
		getModelService().save(customerModel);
		getModelService().refresh(customerModel);
		//getEventService().publishEvent(initializeEvent(new ForgottenPwdEvent(token), customerModel));
		return token;
	}

	@Override
	public void register(final CustomerModel customerModel, final String password) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		generateCustomerId(customerModel);
		if (password != null)
		{
			getUserService().setPassword(customerModel, password, getPasswordEncoding());
		}
		internalSaveCustomer(customerModel);
		//getEventService().publishEvent(initializeEvent(new RegisterEvent(), customerModel));
	}


}
