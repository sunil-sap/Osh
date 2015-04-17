package com.hybris.osh.core.service.impl;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hybris.osh.core.service.GuestCustomerAccountService;
import com.hybris.osh.core.service.GuestIdGeneratorStrategy;


/**
 * This class Provide all methods to create/logout a guestUser
 */
public class DefaultGuestCustomerAccountService implements GuestCustomerAccountService
{
	private static final Logger LOG = Logger.getLogger(DefaultGuestCustomerAccountService.class);

	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private UserService userService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private PasswordEncoderService passwordEncoderService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private EventService eventService;
	@Autowired
	private GuestIdGeneratorStrategy guestIdGeneratorStrategy;

	/**
	 * @return the guestIdGeneratorStrategy
	 */
	public GuestIdGeneratorStrategy getGuestIdGeneratorStrategy()
	{
		return guestIdGeneratorStrategy;
	}



	/**
	 * This method is called during guest checkout for registering the user into Hybris User needs to be inserted into a
	 * newly created group called "guestcustomergroup"
	 * 
	 * @param newCustomer
	 * @throws DuplicateUidException
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public void registerGuest(final CustomerModel newCustomer) throws DuplicateUidException, NoSuchAlgorithmException
	{
		if (newCustomer != null)
		{
			getGuestIdGeneratorStrategy().generateCustomerId(newCustomer);
			final UserGroupModel usergroup = getUserService().getUserGroupForUID("guestcustomergroup");
			final Set<PrincipalGroupModel> group = new HashSet<PrincipalGroupModel>();
			group.add(usergroup);
			newCustomer.setGroups(Collections.unmodifiableSet(group));
			//getEventService().publishEvent(initializeEvent(new RegisterEvent(), newCustomer));
			internalSaveCustomer(newCustomer);

			getModelService().save(newCustomer);
			getModelService().refresh(newCustomer);
		}
	}

	/**
	 * Auto Logout for guest user
	 */
	@Override
	public void logout()
	{
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAnonymousCustomer());
	}

	@Override
	public String generateUid() throws NoSuchAlgorithmException
	{
		return Integer.valueOf(getGuestIdGeneratorStrategy().generateUid()).toString();
	}

	@Override
	public boolean isUserExist(final String emailId)
	{
		Customer customer = null;
		final UserGroup usergroup = UserManager.getInstance().getUserGroupByGroupID("guestcustomergroup");

		try
		{
			customer = UserManager.getInstance().getCustomerByLogin(emailId);
		}
		catch (final Exception e)
		{
			customer = null;
		}

		if (customer == null || customer.isMemberOf(usergroup))
		{
			return false;
		}
		return true;
	}

	/**
	 * initializes an {@link AbstractCommerceUserEvent}
	 * 
	 * @param event
	 * @param customerModel
	 * @return the event
	 */
	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
	{
		event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
		event.setSite(getBaseSiteService().getCurrentBaseSite());
		event.setCustomer(customerModel);
		return event;
	}


	/**
	 * Saves the customer translating model layer exceptions regarding duplicate identifiers
	 * 
	 * @param customerModel
	 * @throws DuplicateUidException
	 *            if the uid is not unique
	 */
	protected void internalSaveCustomer(final CustomerModel customerModel) throws DuplicateUidException
	{
		try
		{
			getModelService().save(customerModel);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				throw new DuplicateUidException(customerModel.getUid(), e);
			}
			else
			{
				throw e;
			}
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new DuplicateUidException(customerModel.getUid(), e);
		}
	}


	@Override
	public boolean setCustomerAccNo(final CustomerModel customer)
	{
		if (customer != null && customer.getCustomerAccNO() == null)
		{
			try
			{
				final String accNo = generateUid();
				if (accNo.contains("-"))
				{
					final String[] accNoId = accNo.split("-");
					customer.setCustomerAccNO(accNoId[1]);
				}
				else
				{
					customer.setCustomerAccNO(accNo);
				}
				return true;
			}
			catch (final NoSuchAlgorithmException e)
			{
				LOG.info("Unable set Customer Acc No", e);
				return false;
			}

		}
		return false;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}


	public PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public EventService getEventService()
	{
		return eventService;
	}


}
