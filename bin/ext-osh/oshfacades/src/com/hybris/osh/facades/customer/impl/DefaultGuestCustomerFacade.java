package com.hybris.osh.facades.customer.impl;

import de.hybris.platform.acceleratorservices.storefinder.StoreFinderService;
import de.hybris.platform.commercefacades.customer.CustomerNameStrategy;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.service.GuestCustomerAccountService;
import com.hybris.osh.facades.customer.GuestCustomerFacade;
import com.hybris.osh.facades.user.data.OshRegisterData;


public class DefaultGuestCustomerFacade implements GuestCustomerFacade<OshRegisterData>
{

	private static final String DEFAULT_PASSWORD_ENCODING = "md5";

	@Autowired
	private GuestCustomerAccountService guestCustomerAccountService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "storeFinderService")
	private StoreFinderService storeFinderService;

	@Autowired
	private CustomerNameStrategy customerNameStrategy;


	private final String passwordEncoding = DEFAULT_PASSWORD_ENCODING;

	/**
	 * This method is called during guest checkout for registering the user into hybris
	 * 
	 * @param registerData
	 * @throws DuplicateUidException
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public void registerGuest(final OshRegisterData registerData, final String store) throws DuplicateUidException,
			UnknownIdentifierException, IllegalArgumentException, NoSuchAlgorithmException
	{
		final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
		newCustomer.setGuest(Boolean.TRUE);
		newCustomer.setEmailId(registerData.getEmail());
		final String myStore = store;
		if (myStore != null)
		{
			final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
			final PointOfServiceModel pointOfService = storeFinderService.getPointOfServiceForName(currentBaseStore, myStore);
			newCustomer.setMyStore(pointOfService);
		}
		newCustomer.setName(getCustomerNameStrategy().getName("Guest", "User"));
		newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		newCustomer.setUid(registerData.getLogin());
		getUserService().setPassword(newCustomer, registerData.getPassword(), getPasswordEncoding());
		getGuestCustomerAccountService().registerGuest(newCustomer);

	}

	@Override
	public void loginDisabled()
	{
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		customer.setLoginDisabled(true);
		getModelService().save(customer);
	}

	@Override
	public void logout()
	{
		getGuestCustomerAccountService().logout();
	}

	@Override
	public String generateUid() throws NoSuchAlgorithmException
	{
		return getGuestCustomerAccountService().generateUid();
	}

	@Override
	public boolean isUserExist(final String emailId)
	{
		return getGuestCustomerAccountService().isUserExist(emailId);
	}

	public UserService getUserService()
	{
		return userService;
	}

	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	public GuestCustomerAccountService getGuestCustomerAccountService()
	{
		return guestCustomerAccountService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the passwordEncoding
	 */
	public String getPasswordEncoding()
	{
		return passwordEncoding;
	}


}
