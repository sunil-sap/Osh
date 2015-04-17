package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hybris.osh.facades.wishlist.OshWishListFacade;
import com.hybris.osh.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.forms.CreateWishListForm;
import com.hybris.osh.storefront.forms.CurrentWishListForm;
import com.hybris.osh.storefront.forms.RenameWishListForm;
import com.hybris.osh.storefront.forms.SendWishListForm;


/**
 * wish List controller handles the login for the wish list.
 *
 */

@Controller
@Scope("tenant")
@RequestMapping(value = "/login/wishlist")
public class WishListPageController extends AbstractLoginPageController
{
	private static final Logger LOG = Logger.getLogger(WishListPageController.class);

	@Resource(name = "userService")
	private UserService userService;

	private HttpSessionRequestCache httpSessionRequestCache;


	@Resource(name = "oshWishlistFacade")
	private OshWishListFacade oshWishlistFacade;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;



	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.WishList.WishListLoginPage;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutLogin(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("sendWishListFrom", new SendWishListForm());
		model.addAttribute("createWishListForm", new CreateWishListForm());
		model.addAttribute("renameWishListForm", new RenameWishListForm());
		final CurrentWishListForm currentWishListForm = new CurrentWishListForm();
		model.addAttribute("currentWishListForm", currentWishListForm);
		session.setAttribute("hasComeFromWishList", Boolean.TRUE);
		final List<ProductData> productList = new ArrayList<ProductData>();
		final List<Wishlist2Model> listOfWishList = userService.getCurrentUser().getWishlist();
		if (!listOfWishList.isEmpty())
		{
			model.addAttribute("listOfWishList", listOfWishList);
			final Wishlist2Model currentDefaultWishList = oshWishlistFacade.getWishList();
			currentWishListForm.setWishlist2Model(currentDefaultWishList);
			model.addAttribute("currentWishListName", currentDefaultWishList.getName());

		}

		if (!listOfWishList.isEmpty() && oshWishlistFacade.getWishList().getEntries() != null)
		{
			for (final Wishlist2EntryModel entry : oshWishlistFacade.getWishList().getEntries())
			{
				final String code = entry.getProduct().getCode();
				final ProductData productData = productFacade.getProductForCodeAndOptions(code, Arrays.asList(ProductOption.BASIC,
						ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
						ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
						ProductOption.VARIANT_FULL, ProductOption.STOCK));
				productList.add(productData);
			}

		}
		model.addAttribute("currentWishList", productList);
		return getDefaultLoginPage(loginError, session, model);
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("wishlist");
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return "/my-account";
	}

	@RequestMapping(value = "/getCreateWishListForm.json", method =
	{ RequestMethod.GET })
	public String getCreateWishListForm(final Model model)
	{
		model.addAttribute("createWishListForm", new CreateWishListForm());
		return ControllerConstants.Views.Fragments.WishList.CreateNewWishListPopup;
	}

	@RequestMapping(value = "/sendWishList", method =
	{ RequestMethod.GET })
	public String sendWishList(final Model model)
	{
		model.addAttribute("sendWishListForm", new SendWishListForm());
		return ControllerConstants.Views.Fragments.WishList.SendWishListPopup;
	}

	@RequestMapping(value = "/getCreateWishListForm.json", method =
	{ RequestMethod.POST })
	public String createWishList(final Model model, @Valid final CreateWishListForm form)
	{
		final List<Wishlist2Model> userWishLists = userService.getCurrentUser().getWishlist();
		final String newWishListName = form.getWishListName();
		boolean isWishListPresent = false;
		for (final Wishlist2Model wishlist : userWishLists)
		{
			if (wishlist.getName().equals(newWishListName))
			{
				isWishListPresent = true;
				break;
			}

		}
		if (!isWishListPresent)
		{
			oshWishlistFacade.createNewWishList(form.getWishListName());
		}
		return "redirect:/login/wishlist";
	}

	@RequestMapping(value = "/removeWishList")
	public String removeWishList(final Model model)
	{
		final List<Wishlist2Model> userWishLists = userService.getCurrentUser().getWishlist();
		String name = null;
		if (!userService.getCurrentUser().getWishlist().isEmpty())
		{

			final Wishlist2Model currentWishList = oshWishlistFacade.getWishList();
			name = oshWishlistFacade.getWishList().getName();
			modelService.detach(currentWishList);
			modelService.remove(currentWishList);
			modelService.save(userService.getCurrentUser());
		}
		if (!userService.getCurrentUser().getWishlist().isEmpty() && name != null)
		{

			for (final Wishlist2Model wishList : userWishLists)
			{
				if (!name.equals(wishList.getName()))
				{
					wishList.setDefault(Boolean.TRUE);
					modelService.save(wishList);
					break;
				}
			}
		}
		modelService.save(userService.getCurrentUser());
		return "redirect:/login/wishlist";
	}

	@RequestMapping(value = "/getRenameWishListForm.json", method =
	{ RequestMethod.GET })
	public String getRenameWishListForm(final Model model)
	{
		model.addAttribute("renameWishListForm", new RenameWishListForm());
		return ControllerConstants.Views.Fragments.WishList.ChangeWishlistNamePopup;
	}

	@RequestMapping(value = "/getRenameWishListForm.json", method =
	{ RequestMethod.POST })
	public String renameWishList(final Model model, @Valid final RenameWishListForm form)
	{

		final Wishlist2Model wishlist2Model = oshWishlistFacade.getWishList();
		if (oshWishlistFacade.getWishList() != null)
		{
			oshWishlistFacade.getWishList().setName(form.getNewName());
			modelService.setAttributeValue(wishlist2Model, "name", wishlist2Model.getName());
			modelService.save(wishlist2Model);
		}
		return "redirect:/login/wishlist";
	}


	@RequestMapping(value = "/changeDefaultWishList", method =
	{ RequestMethod.POST })
	public String changeDefaultWishList(final Model model, @Valid final CurrentWishListForm form)
	{
		for (final Wishlist2Model wishList : userService.getCurrentUser().getWishlist())
		{
			if (wishList.getName().equals(form.getWishlist2Model().getName()))
			{
				final Wishlist2Model currentWishList = oshWishlistFacade.getWishList();
				currentWishList.setDefault(Boolean.FALSE);
				modelService.save(currentWishList);
				wishList.setDefault(Boolean.TRUE);
				modelService.save(wishList);
				break;
			}

		}
		return "redirect:/login/wishlist";
	}


	@RequestMapping(value = "/removeProduct", method =
	{ RequestMethod.GET })
	public String removeFromWishList(@RequestParam(value = "productCode") final String productCode)
	{
		LOG.info("the product code is" + productCode);
		oshWishlistFacade.removeFromWishList(productCode);
		return "redirect:/login/wishlist";
	}


}
