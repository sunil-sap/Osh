/**
 * 
 */
package com.hybris.osh.facades.product.data;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;


/**
 * 
 */
public class OshVariantOptionData extends VariantOptionData
{

	private String code;
	private String Name;
	private String colour;
	private String size;
	private PriceData regPriceData;
	private PriceData salePriceData;
	private PriceData mapPriceData;
	private boolean newSKU;
	private boolean freeShipping;
	private boolean recall;
	private boolean whiteGlove;
	private boolean sizeChart;
	private boolean mapPriceType;
	private String availabilityInd;


	/**
	 * @return the availabilityInd
	 */
	public String getAvailabilityInd()
	{
		return availabilityInd;
	}

	/**
	 * @param availabilityInd
	 *           the availabilityInd to set
	 */
	public void setAvailabilityInd(final String availabilityInd)
	{
		this.availabilityInd = availabilityInd;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return Name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		Name = name;
	}

	/**
	 * @return the colour
	 */
	public String getColour()
	{
		return colour;
	}

	/**
	 * @param colour
	 *           the colour to set
	 */
	public void setColour(final String colour)
	{
		this.colour = colour;
	}

	/**
	 * @return the size
	 */
	public String getSize()
	{
		return size;
	}

	/**
	 * @param size
	 *           the size to set
	 */
	public void setSize(final String size)
	{
		this.size = size;
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

	public boolean isNewSKU()
	{
		return newSKU;
	}

	public void setNewSKU(final boolean newSKU)
	{
		this.newSKU = newSKU;
	}

	public boolean isFreeShipping()
	{
		return freeShipping;
	}

	public void setFreeShipping(final boolean freeShipping)
	{
		this.freeShipping = freeShipping;
	}

	public boolean isRecall()
	{
		return recall;
	}

	public void setRecall(final boolean recall)
	{
		this.recall = recall;
	}

	public boolean isWhiteGlove()
	{
		return whiteGlove;
	}

	public void setWhiteGlove(final boolean whiteGlove)
	{
		this.whiteGlove = whiteGlove;
	}

	public boolean isSizeChart()
	{
		return sizeChart;
	}

	public void setSizeChart(final boolean sizeChart)
	{
		this.sizeChart = sizeChart;
	}

	public boolean isMapPriceType()
	{
		return mapPriceType;
	}

	public void setMapPriceType(final boolean mapPriceType)
	{
		this.mapPriceType = mapPriceType;
	}



}
