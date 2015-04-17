package com.hybris.osh.facades.wishlist;

import de.hybris.platform.wishlist2.model.Wishlist2Model;


/**
 * Facade for adding and retrieving items to and from wishlist
 * 
 */
public interface OshWishListFacade
{
	/*
	 * Responsible for adding the products to wishlist
	 */
	public String addToWishList(final String productCode);

	/*
	 * Used to get the wishlist for current user
	 */
	public Wishlist2Model getWishList();

	/*
	 * used to remove the entry from wish list for product
	 */
	public void removeFromWishList(final String productCode);

	/*
	 * used to create the new wishList
	 */
	public void createNewWishList(final String name);

	/*
	 * Responsible for adding the product to wishlist and to give name
	 */
	public String createAndUpdateWishList(final String productCode, final String wishListName);


}
