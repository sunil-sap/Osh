/**
 * 
 */
package com.hybris.osh.facades.order.data;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;


/**
 * 
 */
public class OshOrderEntryData extends OrderEntryData
{
	private String orderType;
	private int storeStock;
	private int reserveStock;
	private StockLevelStatus stockLevelStatus;
	private String promotionMessage;
	private PriceData regPriceData;
	private PriceData salePriceData;
	private PriceData mapPriceData;
	private PriceData promotionAmount;
	private PriceData discountPrice;
	private PriceData productDiscountPrice;

	/**
	 * @return the discountPrice
	 */
	public PriceData getDiscountPrice()
	{
		return discountPrice;
	}

	/**
	 * @param discountPrice
	 *           the discountPrice to set
	 */
	public void setDiscountPrice(final PriceData discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public PriceData getPromotionAmount()
	{
		return promotionAmount;
	}

	public void setPromotionAmount(final PriceData promotionAmount)
	{
		this.promotionAmount = promotionAmount;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType()
	{
		return orderType;
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
	 * @return the storeStock
	 */
	public int getStoreStock()
	{
		return storeStock;
	}

	/**
	 * @param storeStock
	 *           the storeStock to set
	 */
	public void setStoreStock(final int storeStock)
	{
		this.storeStock = storeStock;
	}

	/**
	 * @return the regPriceData
	 */
	public PriceData getRegPriceData()
	{
		return regPriceData;
	}

	/**
	 * @param regPriceData
	 *           the regPriceData to set
	 */
	public void setRegPriceData(final PriceData regPriceData)
	{
		this.regPriceData = regPriceData;
	}

	/**
	 * @return the salePriceData
	 */
	public PriceData getSalePriceData()
	{
		return salePriceData;
	}

	/**
	 * @param salePriceData
	 *           the salePriceData to set
	 */
	public void setSalePriceData(final PriceData salePriceData)
	{
		this.salePriceData = salePriceData;
	}

	/**
	 * @return the mapPriceData
	 */
	public PriceData getMapPriceData()
	{
		return mapPriceData;
	}

	/**
	 * @param mapPriceData
	 *           the mapPriceData to set
	 */
	public void setMapPriceData(final PriceData mapPriceData)
	{
		this.mapPriceData = mapPriceData;
	}

	/**
	 * @return the promotionMessage
	 */
	public String getPromotionMessage()
	{
		return promotionMessage;
	}

	/**
	 * @param promotionMessage
	 *           the promotionMessage to set
	 */
	public void setPromotionMessage(final String promotionMessage)
	{
		this.promotionMessage = promotionMessage;
	}

	public int getReserveStock()
	{
		return reserveStock;
	}

	public void setReserveStock(final int reserveStock)
	{
		this.reserveStock = reserveStock;
	}

	/**
	 * @return the stockLevelStatus
	 */
	public StockLevelStatus getStockLevelStatus()
	{
		return stockLevelStatus;
	}

	/**
	 * @param stockLevelStatus
	 *           the stockLevelStatus to set
	 */
	public void setStockLevelStatus(final StockLevelStatus stockLevelStatus)
	{
		this.stockLevelStatus = stockLevelStatus;
	}

	/**
	 * @return the productDiscountPrice
	 */
	public PriceData getProductDiscountPrice()
	{
		return productDiscountPrice;
	}

	/**
	 * @param productDiscountPrice
	 *           the productDiscountPrice to set
	 */
	public void setProductDiscountPrice(final PriceData productDiscountPrice)
	{
		this.productDiscountPrice = productDiscountPrice;
	}
}
