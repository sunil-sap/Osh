/**
 * 
 */
package com.hybris.osh.facades.order.data;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;



/**
 * 
 */
public class OshOrderData extends OrderData
{
	private String orderType;
	private boolean gift;
	private String giftMessage;
	private AddressData storeAddress;
	private String isBopisPresent;
	private PriceData totalAmount;
	private boolean showShipping;
	private boolean showDiscounts;
	private String voucherCode;
	private String creationTime;


	public String getCreationTime()
	{
		return creationTime;
	}


	public void setCreationTime(final String creationTime)
	{
		this.creationTime = creationTime;
	}


	public boolean isShowDiscounts()
	{
		return showDiscounts;
	}


	public void setShowDiscounts(final boolean showDiscounts)
	{
		this.showDiscounts = showDiscounts;
	}

	public boolean isShowShipping()
	{
		return showShipping;
	}

	public void setShowShipping(final boolean showShipping)
	{
		this.showShipping = showShipping;
	}

	public String getIsBopisPresent()
	{
		return isBopisPresent;
	}

	public void setIsBopisPresent(final String isBopisPresent)
	{
		this.isBopisPresent = isBopisPresent;
	}

	public String getIsOnlinePresent()
	{
		return isOnlinePresent;
	}

	public void setIsOnlinePresent(final String isOnlinePresent)
	{
		this.isOnlinePresent = isOnlinePresent;
	}

	private String loyaltyVoucher;

	private String isOnlinePresent;




	/**
	 * @return the orderType
	 */
	public String getOrderType()
	{
		return orderType;
	}

	/**
	 * @return the loyaltyVoucher
	 */
	public String getLoyaltyVoucher()
	{
		return loyaltyVoucher;
	}

	/**
	 * @param loyaltyVoucher
	 *           the loyaltyVoucher to set
	 */
	public void setLoyaltyVoucher(final String loyaltyVoucher)
	{
		this.loyaltyVoucher = loyaltyVoucher;
	}

	/**
	 * @param orderType
	 *           the orderType to set
	 */
	public void setOrderType(final String orderType)
	{
		this.orderType = orderType;
	}

	/**
	 * @return the gift
	 */
	public boolean isGift()
	{
		return gift;
	}

	/**
	 * @param gift
	 *           the gift to set
	 */
	public void setGift(final boolean gift)
	{
		this.gift = gift;
	}

	/**
	 * @return the giftMessage
	 */
	public String getGiftMessage()
	{
		return giftMessage;
	}

	/**
	 * @param giftMessage
	 *           the giftMesaage to set
	 */
	public void setGiftMesaage(final String giftMessage)
	{
		this.giftMessage = giftMessage;
	}

	/**
	 * @return the storeAddress
	 */
	public AddressData getStoreAddress()
	{
		return storeAddress;
	}

	/**
	 * @param storeAddress
	 *           the storeAddress to set
	 */
	public void setStoreAddress(final AddressData storeAddress)
	{
		this.storeAddress = storeAddress;
	}

	/**
	 * @return the totalAmount
	 */
	public PriceData getTotalAmount()
	{
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *           the totalAmount to set
	 */
	public void setTotalAmount(final PriceData totalAmount)
	{
		this.totalAmount = totalAmount;
	}


	/**
	 * @return the voucherCode
	 */
	public String getVoucherCode()
	{
		return voucherCode;
	}


	/**
	 * @param voucherCode
	 *           the voucherCode to set
	 */
	public void setVoucherCode(final String voucherCode)
	{
		this.voucherCode = voucherCode;
	}

}
