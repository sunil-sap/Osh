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
package com.hybris.osh.storefront.security;

import de.hybris.platform.spring.security.CoreAuthenticationProvider;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Derived authentication provider supporting two additional authentication checks:
 * <ul>
 * <li>prevent login without password for users created via CSCockpit</li>
 * <li>prevent admin login since all SearchRestrictions are disabled and therefore no page can be viewed correctly</li>
 * </ul>
 */
public class AcceleratorAuthenticationProvider extends CoreAuthenticationProvider
{
	private static final String ROLE_ADMIN_GROUP = "ROLE_ADMINGROUP";

	private GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ROLE_ADMIN_GROUP);

	/**
	 * @see de.hybris.platform.spring.security.CoreAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails,
	 *      org.springframework.security.authentication.AbstractAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(final UserDetails details, final AbstractAuthenticationToken authentication)
			throws AuthenticationException
	{
		if (StringUtils.isEmpty((String) authentication.getCredentials()))
		{
			throw new BadCredentialsException("Login without password");
		}
		if (adminAuthority != null && details.getAuthorities().contains(adminAuthority))
		{
			throw new LockedException("Admin account not allowed to login");
		}
	}

	/**
	 * @param adminGroup
	 *           the adminGroup to set
	 */
	public void setAdminGroup(final String adminGroup)
	{
		if (StringUtils.isBlank(adminGroup))
		{
			adminAuthority = null;
		}
		else
		{
			adminAuthority = new SimpleGrantedAuthority(adminGroup);
		}
	}
}
