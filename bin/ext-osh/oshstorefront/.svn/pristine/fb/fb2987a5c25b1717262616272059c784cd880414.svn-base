/**
 * 
 */
package com.hybris.osh.storefront.controllers.cms;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.enums.NavigationBarMenuLayout;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

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

import com.hybris.osh.core.model.OshNavBarComponentModel;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.pages.AbstractPageController;


/**
 * 
 *
 */
@UnitTest
public class OshNavBarComponentControllerTest
{
	private static final String COMPONENT_UID = "componentUid";
	private static final String TEST_COMPONENT_UID = "componentUID";
	private static final String TEST_TYPE_CODE = "myTypeCode";
	private static final String TEST_TYPE_VIEW = ControllerConstants.Views.Cms.ComponentPrefix
			+ StringUtils.lowerCase(TEST_TYPE_CODE);
	private static final String DROP_DOWN_LAYOUT = "dropDownLayout";
	private static final String COMPONENT = "component";

	private OshNavBarComponentController oshNavBarComponentController;
	private final NavigationBarMenuLayout navigationBarMenuLayout = NavigationBarMenuLayout.LEFT_EDGE;

	@Mock
	private ComposedTypeModel composedTypeModel;

	@Mock
	private OshNavBarComponentModel oshNavBarComponentModel;

	@Mock
	private Model model;
	@Mock
	private DefaultCMSComponentService cmsComponentService;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;

	@Mock
	private TypeService typeService;

	/**
	 * Set up the test.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		oshNavBarComponentController = new OshNavBarComponentController();
		oshNavBarComponentController.setCmsComponentService(cmsComponentService);
	}

	@Test
	public void testRenderComponent() throws Exception
	{
		given(oshNavBarComponentModel.getDropDownLayout()).willReturn(navigationBarMenuLayout);
		given(typeService.getComposedTypeForClass(Mockito.<Class> any())).willReturn(composedTypeModel);
		given(oshNavBarComponentModel.getItemtype()).willReturn(TEST_TYPE_CODE);

		final String viewName = oshNavBarComponentController.handleComponent(request, response, model, oshNavBarComponentModel);
		verify(model, Mockito.times(1)).addAttribute(DROP_DOWN_LAYOUT, navigationBarMenuLayout.getCode().toLowerCase());
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test
	public void testRenderComponentUid() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(oshNavBarComponentModel);
		given(oshNavBarComponentModel.getDropDownLayout()).willReturn(navigationBarMenuLayout);
		given(typeService.getComposedTypeForClass(Mockito.<Class> any())).willReturn(composedTypeModel);
		given(oshNavBarComponentModel.getItemtype()).willReturn(TEST_TYPE_CODE);

		final String viewName = oshNavBarComponentController.handleGet(request, response, model);
		verify(model, Mockito.times(1)).addAttribute(DROP_DOWN_LAYOUT,
				oshNavBarComponentModel.getDropDownLayout().getCode().toLowerCase());
		verify(model, Mockito.times(1)).addAttribute(COMPONENT, oshNavBarComponentModel);
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(null);
		oshNavBarComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound2() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		oshNavBarComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound3() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willThrow(new CMSItemNotFoundException(""));
		oshNavBarComponentController.handleGet(request, response, model);
	}


}
