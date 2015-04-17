/**
 * 
 */
package com.hybris.osh.storefront.controllers.cms;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Collection;

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

import com.hybris.osh.core.model.OshImageTextComponentModel;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.pages.AbstractPageController;



/**
 * 
 *
 */
public class OshImageTextComponentControllerTest
{
	private static final String COMPONENT_UID = "componentUid";
	private static final String TEST_COMPONENT_UID = "componentUID";
	private static final String TEST_TYPE_CODE = "myTypeCode";
	private static final String TEST_TYPE_VIEW = ControllerConstants.Views.Cms.ComponentPrefix
			+ StringUtils.lowerCase(TEST_TYPE_CODE);
	private Collection<MediaModel> media;
	private static final String MEDIA = "media";
	private static final String COMPONENT = "component";

	private OshImageTextComponentController oshImageTextComponentController;



	@Mock
	private OshImageTextComponentModel oshImageTextComponentModel;

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

		oshImageTextComponentController = new OshImageTextComponentController();
		oshImageTextComponentController.setCmsComponentService(cmsComponentService);
	}

	@Test
	public void testRenderComponent() throws Exception
	{
		given(oshImageTextComponentModel.getMedia()).willReturn((MediaModel) media);
		given(oshImageTextComponentModel.getItemtype()).willReturn(TEST_TYPE_CODE);

		final String viewName = oshImageTextComponentController.handleComponent(request, response, model,
				oshImageTextComponentModel);
		verify(model, Mockito.times(1)).addAttribute(MEDIA, oshImageTextComponentModel.getMedia());
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test
	public void testRenderComponentUid() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(oshImageTextComponentModel);
		given(oshImageTextComponentModel.getMedia()).willReturn((MediaModel) media);
		given(oshImageTextComponentModel.getItemtype()).willReturn(TEST_TYPE_CODE);

		final String viewName = oshImageTextComponentController.handleGet(request, response, model);
		verify(model, Mockito.times(1)).addAttribute(COMPONENT, oshImageTextComponentModel);
		verify(model, Mockito.times(1)).addAttribute(MEDIA, oshImageTextComponentModel.getMedia());
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(null);
		oshImageTextComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound2() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		oshImageTextComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound3() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willThrow(new CMSItemNotFoundException(""));
		oshImageTextComponentController.handleGet(request, response, model);
	}


}
