package com.hybris.osh.storefront.controllers.pages;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.facades.wishlist.OshWishListFacade;
import com.hybris.osh.storefront.controllers.ControllerConstants;


/**
 * wish List controller handles the login for the wish list.
 * 
 */

@Controller
@Scope("tenant")
@RequestMapping(value = "/addWishList")
public class WishListController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(WishListController.class);

	@Resource(name = "userService")
	private UserService userService;
	private HttpSessionRequestCache httpSessionRequestCache;

	@Resource(name = "oshWishlistFacade")
	private OshWishListFacade oshWishlistFacade;

	@Resource(name = "modelService")
	private ModelService modelService;

	public OshWishListFacade getOshWishlistFacade()
	{
		return oshWishlistFacade;
	}



	@ResponseBody
	@RequestMapping(value = "/createAndUpdateWishlistname.json", method = RequestMethod.POST)
	public String createUpdateWishlistWithName(@RequestParam(value = "name") final String name,
			@RequestParam(value = "productCode") final String productCode, final Model model)
	{

		final List<Wishlist2Model> wishListModel = userService.getCurrentUser().getWishlist();


		for (final Wishlist2Model wishlist : wishListModel)
		{
			if (wishlist.getName().equals(name))
			{
				final Wishlist2Model currentWishList = oshWishlistFacade.getWishList();
				currentWishList.setDefault(Boolean.FALSE);
				modelService.save(currentWishList);
				wishlist.setDefault(Boolean.TRUE);
				modelService.save(wishlist);
				final List<Wishlist2EntryModel> wishListEntries = wishlist.getEntries();
				for (final Wishlist2EntryModel wishlist2EntryModel : wishListEntries)
				{
					final ProductModel product = wishlist2EntryModel.getProduct();
					if (product.getCode().equals(productCode))
					{
						return OshCoreConstants.AVAILABLE;
					}

				}
				break;
			}
		}

		getOshWishlistFacade().createAndUpdateWishList(productCode, name);
		return OshCoreConstants.CREATED;
	}

	@RequestMapping(value = "/createAndUpdateWishList.json", method = RequestMethod.GET)
	public String createUpdateWishlist(@RequestParam(value = "productCode") final String productCode, final Model model)
	{

		final List<Wishlist2Model> listOfWishList = userService.getCurrentUser().getWishlist();
		model.addAttribute("productCode", productCode);

		if (listOfWishList != null && !listOfWishList.isEmpty())
		{

			model.addAttribute("listOfWishList", listOfWishList);
			return ControllerConstants.Views.Fragments.AddToWishList.CreateUpdateMultipleWishListPopup;

		}
		else
		{
			return ControllerConstants.Views.Fragments.AddToWishList.CreateNewWishListPopup;

		}
	}
}
