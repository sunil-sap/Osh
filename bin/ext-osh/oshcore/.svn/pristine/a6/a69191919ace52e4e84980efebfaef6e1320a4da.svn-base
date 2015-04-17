/**
 * 
 */
package com.hybris.osh.core.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Junit test case class for calculation
 * 
 */
public class DefaultOshCalculationServiceTest
{
	@Mock
	AbstractOrderModel abstractOrderModel = new AbstractOrderModel();


	boolean recalculate;
	int digits = 0;
	final double taxAdjustmentFactor = 0.0d;
	Map taxValueMap = new HashMap();



	@InjectMocks
	DefaultOshCalculationService calculationService = new DefaultOshCalculationService();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void calculateTotalTaxValuesOrderNullTest()
	{
		calculationService.calculateTotalTaxValues(null, recalculate, digits, taxAdjustmentFactor, taxValueMap);
	}



	@Test(expected = IllegalArgumentException.class)
	public void calculateTotalTaxValueMapNullTest()
	{
		calculationService.calculateTotalTaxValues(abstractOrderModel, false, digits, taxAdjustmentFactor, null);
	}




	@Test
	public void calculateTotalTaxValuesTest()
	{
		when(abstractOrderModel.getTotalTax()).thenReturn(Double.valueOf(30.0));
		final Double result = calculationService.calculateTotalTaxValues(abstractOrderModel, false, 0, 0.0d, taxValueMap);
		assertNotNull("Results CANNOT be null.", abstractOrderModel.getTotalTax());
		Assert.assertEquals(Double.valueOf(30.0), result);



	}
}
