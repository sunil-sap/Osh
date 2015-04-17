/**
 * 
 */
package com.hybris.osh.storefront.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


public class WishListLoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{
		// Store the j_username in the session
		request.getSession().setAttribute("SPRING_SECURITY_LAST_USERNAME", request.getParameter("j_username"));
		super.setDefaultFailureUrl("/login/wishlist?error=true");
		super.onAuthenticationFailure(request, response, exception);
	}
}
