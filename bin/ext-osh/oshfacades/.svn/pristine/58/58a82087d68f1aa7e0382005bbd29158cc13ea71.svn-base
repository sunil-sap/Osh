/**
 * 
 */
package com.hybris.osh.facades.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.HashSet;
import java.util.List;

import org.springframework.util.Assert;

import com.hybris.osh.core.service.OshCustomerAccountService;
import com.hybris.osh.facades.customer.OshCustomerFacade;
import com.hybris.osh.facades.customer.data.OshCustomerData;


public class DefaultOshCustomerFacade extends DefaultCustomerFacade<OshCustomerData> implements OshCustomerFacade
{

	private OshCustomerAccountService oshCustomerAccountService;

	@Override
	public void oshUpdateProfile(final OshCustomerData customerData) throws DuplicateUidException
	{
		final String name = getCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
		final CustomerModel customer = getCurrentSessionCustomer();
		String birthday = null;
		if (customerData.getMonth() != null && customerData.getDay() != null)
		{
			birthday = customerData.getMonth() + "/" + customerData.getDay();
		}
		getOshCustomerAccountService().oshUpdateProfile(customer, customerData.getTitleCode(), name, customerData.getUid(),
				customerData.getPhone(), birthday, customerData.isSpecialoffer(), customerData.isStorenewletter(),
				customerData.isCluborchardinfo(), customerData.getLoyaltyNumber());
	}

	/**
	 * @return the oshCustomerAccountService
	 */
	public OshCustomerAccountService getOshCustomerAccountService()
	{
		return oshCustomerAccountService;
	}

	/**
	 * @param oshCustomerAccountService
	 *           the oshCustomerAccountService to set
	 */
	public void setOshCustomerAccountService(final OshCustomerAccountService oshCustomerAccountService)
	{
		this.oshCustomerAccountService = oshCustomerAccountService;
	}

	@Override
	public void register(final RegisterData registerData) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("registerData", registerData);
		Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");
		Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");

		final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
		newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
		setUidForRegister(registerData, newCustomer);
		newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		getCustomerAccountService().register(newCustomer, registerData.getPassword());
	}

	@Override
	public void changeUserGroup(final String userGroup)
	{
		final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();

		final UserGroupModel userGroupModel = getUserService().getUserGroupForUID(userGroup);
		if (userGroupModel != null)
		{
			final HashSet<PrincipalGroupModel> newGroup = new HashSet<PrincipalGroupModel>();
			newGroup.add(userGroupModel);
			customerModel.setGroups(newGroup);
			getModelService().save(customerModel);
		}
	}

	@Override
	public void updateProfile(final OshCustomerData customerData) throws DuplicateUidException
	{

		final String name = getCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
		final CustomerModel customer = getCurrentSessionCustomer();
		final List<PaymentInfoModel> paymentInfo = (List<PaymentInfoModel>) customer.getPaymentInfos();

		if (customer.getDefaultPaymentAddress() != null)
		{
			customer.getDefaultShipmentAddress().setShippingAddress(Boolean.TRUE);
			getModelService().save(customer.getDefaultPaymentAddress());
		}

		if (customer.getDefaultShipmentAddress() != null)
		{
			customer.getDefaultPaymentAddress().setBillingAddress(Boolean.TRUE);
			getModelService().save(customer.getDefaultShipmentAddress());
		}
		for (final PaymentInfoModel paymentInfoModel : paymentInfo)
		{
			paymentInfoModel.setSaved(true);
			getModelService().save(paymentInfoModel);
			break;
		}

		getModelService().save(customer);

		getOshCustomerAccountService().updateProfile(customer, name, customerData.getUid());

	}

	@Override
	public String getPasswordToken(final String uid)
	{
		Assert.hasText(uid, "The field [uid] cannot be empty");
		final CustomerModel customerModel = getUserService().getUserForUID(uid, CustomerModel.class);
		return getOshCustomerAccountService().getGuestUserPasswordToken(customerModel);
	}

}
