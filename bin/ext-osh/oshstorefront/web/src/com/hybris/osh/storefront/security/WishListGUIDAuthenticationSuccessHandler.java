package com.hybris.osh.storefront.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * 
 * handler for redirecting back to wishlist page.
 * 
 */
public class WishListGUIDAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements
		AuthenticationSuccessHandler
{

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		//super.setDefaultTargetUrl("/login/wishlist");
		handle(request, response, authentication);
	}



}
