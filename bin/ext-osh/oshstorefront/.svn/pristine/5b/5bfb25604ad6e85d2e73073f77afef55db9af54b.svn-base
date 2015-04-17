/**
 * 
 */
package com.hybris.osh.storefront.controllers.cms;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.hybris.osh.core.model.OshRotatingImagesComponentModel;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.pages.AbstractPageController;


/**
 * 
 * 
 */
@UnitTest
public class OshRotatingImagesComponentControllerTest
{
	private static final String COMPONENT_UID = "componentUid";
	private static final String TEST_COMPONENT_UID = "componentUID";
	private static final String TEST_TYPE_CODE = "myTypeCode";
	private static final String TEST_TYPE_VIEW = ControllerConstants.Views.Cms.ComponentPrefix
			+ StringUtils.lowerCase(TEST_TYPE_CODE);
	private static List<BannerComponentModel> banners;

	private final Integer timeout = 5000;
	private static final String BANNERS = "banners";
	private static final String COMPONENT = "component";
	private static final String TIMEOUT = "timeout";


	private OshRotatingImagesComponentController oshRotatingImagesComponentController;



	@Mock
	private OshRotatingImagesComponentModel oshRotatingImagesComponentModel;

	@Mock
	private Model model;
	@Mock
	private DefaultCMSComponentService cmsComponentService;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;


	/**
	 * Set up the test.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		oshRotatingImagesComponentController = new OshRotatingImagesComponentController();
		oshRotatingImagesComponentController.setCmsComponentService(cmsComponentService);
	}

	@Test
	public void testRenderComponent() throws Exception
	{
		given(oshRotatingImagesComponentModel.getBanners()).willReturn(banners);
		given(oshRotatingImagesComponentModel.getTimeout()).willReturn(timeout);
		given(oshRotatingImagesComponentModel.getItemtype()).willReturn(TEST_TYPE_CODE);

		final String viewName = oshRotatingImagesComponentController.handleComponent(request, response, model,
				oshRotatingImagesComponentModel);
		verify(model, Mockito.times(1)).addAttribute(BANNERS, oshRotatingImagesComponentModel.getBanners());
		verify(model, Mockito.times(1)).addAttribute(TIMEOUT, oshRotatingImagesComponentModel.getTimeout());
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test
	public void testRenderComponentUid() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(oshRotatingImagesComponentModel);
		given(oshRotatingImagesComponentModel.getBanners()).willReturn(banners);
		given(oshRotatingImagesComponentModel.getTimeout()).willReturn(timeout);
		given(oshRotatingImagesComponentModel.getItemtype()).willReturn(TEST_TYPE_CODE);

		final String viewName = oshRotatingImagesComponentController.handleGet(request, response, model);
		verify(model, Mockito.times(1)).addAttribute(COMPONENT, oshRotatingImagesComponentModel);
		verify(model, Mockito.times(1)).addAttribute(BANNERS, oshRotatingImagesComponentModel.getBanners());
		verify(model, Mockito.times(1)).addAttribute(TIMEOUT, oshRotatingImagesComponentModel.getTimeout());
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(null);
		oshRotatingImagesComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound2() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		oshRotatingImagesComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound3() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willThrow(new CMSItemNotFoundException(""));
		oshRotatingImagesComponentController.handleGet(request, response, model);
	}

}
