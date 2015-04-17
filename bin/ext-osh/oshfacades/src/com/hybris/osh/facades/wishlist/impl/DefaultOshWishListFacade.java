package com.hybris.osh.facades.wishlist.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;

import com.hybris.osh.facades.wishlist.OshWishListFacade;


/**
 * Implementation class for OshWishListFacade
 * 
 */
public class DefaultOshWishListFacade implements OshWishListFacade
{
	private UserService userService;
	private ProductService productService;
	private Wishlist2Service wishlistService;
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.wishlist.OshWishListFacade#addToWishList(java.lang.String)
	 */
	@Override
	public String addToWishList(final String productCode)
	{
		final UserModel userModel = getUserService().getCurrentUser();

		final ProductModel productModel = getProductService().getProductForCode(productCode);
		if (!getWishlistService().hasDefaultWishlist(userModel))
		{
			getModelService().save(
					getWishlistService().createDefaultWishlist(userModel, "DefaultWishList", "This is default wishlist"));
		}


		addWishlistEntry(userModel, productModel, Integer.valueOf(2), Wishlist2EntryPriority.HIGH, "Must have", false);
		return productCode;
	}

	@Override
	public String createAndUpdateWishList(final String productCode, final String wishListName)
	{
		final UserModel userModel = getUserService().getCurrentUser();
		final List<Wishlist2Model> wishlists = getWishlistService().getWishlists(userModel);


		final ProductModel productModel = getProductService().getProductForCode(productCode);
		if (!getWishlistService().hasDefaultWishlist(userModel))
		{
			getModelService().save(getWishlistService().createDefaultWishlist(userModel, wishListName, "This is default wishlist"));
			addWishlistEntry(userModel, productModel, Integer.valueOf(2), Wishlist2EntryPriority.HIGH, "Must have", false);
		}
		else
		{
			for (final Wishlist2Model wishlist : wishlists)
			{
				if (wishlist.getName().equals(wishListName))
				{
					addWishlistEntry(userModel, productModel, Integer.valueOf(2), Wishlist2EntryPriority.HIGH, "Must have", true);
					return productCode;
				}

			}
			final Wishlist2Model defaultWishList = getWishlistService().getDefaultWishlist(userModel);
			if (defaultWishList != null)
			{
				defaultWishList.setDefault(Boolean.FALSE);
				getModelService().save(defaultWishList);
				getModelService().refresh(defaultWishList);
				getModelService().save(
						getWishlistService().createDefaultWishlist(userModel, wishListName, "This is default wishlist"));
				addWishlistEntry(userModel, productModel, Integer.valueOf(2), Wishlist2EntryPriority.HIGH, "Must have", false);
			}
		}

		return productCode;
	}


	private void addWishlistEntry(final UserModel userModel, final ProductModel productModel, Integer desired,
			final Wishlist2EntryPriority priority, final String comment, final boolean forceNewEntry)
	{
		if (!forceNewEntry)
		{
			final Wishlist2Model wishlist2Model = getWishlistService().getDefaultWishlist(userModel);
			try
			{
				final Wishlist2EntryModel wishlist2EntryModel = getWishlistService().getWishlistEntryForProduct(productModel,
						wishlist2Model);
				if (null != wishlist2EntryModel)
				{
					getWishlistService().removeWishlistEntry(wishlist2Model, wishlist2EntryModel);
					desired = Integer.valueOf(desired.intValue() + wishlist2EntryModel.getDesired().intValue());
					wishlist2EntryModel.setDesired(desired);
					getWishlistService().addWishlistEntry(wishlist2Model, wishlist2EntryModel);
				}
			}
			catch (final Exception e)
			{

				getWishlistService().addWishlistEntry(userModel, productModel, desired, priority, comment);
			}
		}
		else
		{
			getWishlistService().addWishlistEntry(userModel, productModel, desired, priority, comment);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.wishlist.OshWishListFacade#getWishList()
	 */
	@Override
	public Wishlist2Model getWishList()
	{
		Wishlist2Model wishList = new Wishlist2Model();
		final UserModel userModel = getUserService().getCurrentUser();

		if (!getWishlistService().hasDefaultWishlist(userModel))
		{
			wishList = getWishlistService().createDefaultWishlist(userModel, "DefaultWishList", "This is default wishlist");
		}
		else
		{
			wishList = getWishlistService().getDefaultWishlist(userModel);
		}

		return wishList;

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.wishlist.OshWishListFacade#removeFromWishList(java.lang.String)
	 */
	@Override
	public void removeFromWishList(final String productCode)
	{
		final UserModel userModel = getUserService().getCurrentUser();

		final ProductModel productModel = getProductService().getProductForCode(productCode);
		if (getWishlistService().hasDefaultWishlist(userModel))
		{
			getWishlistService().removeWishlistEntryForProduct(productModel, getWishList());
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.facades.wishlist.OshWishListFacade#createNewWishList(java.lang.String)
	 */
	@Override
	public void createNewWishList(final String name)
	{
		final UserModel userModel = getUserService().getCurrentUser();
		if (getWishlistService().hasDefaultWishlist(userModel))
		{
			final Wishlist2Model wishList = getWishlistService().getDefaultWishlist();
			wishList.setDefault(Boolean.FALSE);
			getModelService().save(wishList);
		}
		getModelService().save(getWishlistService().createDefaultWishlist(userModel, name, "This is default wishlist"));
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the wishlistService
	 */
	public Wishlist2Service getWishlistService()
	{
		return wishlistService;
	}

	/**
	 * @param wishlistService
	 *           the wishlistService to set
	 */
	public void setWishlistService(final Wishlist2Service wishlistService)
	{
		this.wishlistService = wishlistService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




}
