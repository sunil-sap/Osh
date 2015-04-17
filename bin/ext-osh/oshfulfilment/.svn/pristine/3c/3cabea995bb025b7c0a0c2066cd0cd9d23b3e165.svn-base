package com.hybris.osh.test.orderstatus.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.BDDMockito.given;

import com.hybris.osh.fulfilmentservices.impl.DefaultCalculateOrderStatusService;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

/**
 * JUnit test suite for{@link DefaultCalculateOrderStatusService} 
 *
 */
public class DefaultCalculateOrderStatusServiceTest {
	
	
	@InjectMocks
	DefaultCalculateOrderStatusService defaultCalculateOrderStatusService=new DefaultCalculateOrderStatusService();
	
	@Mock
	private ConsignmentModel consignmentModel;
	
	@Mock
	private OrderModel orderModel;
	
	@Mock
	private de.hybris.platform.basecommerce.enums.ConsignmentStatus consignmentStatus;
	
	
	
	
	/**  
	 * This method will initialize the mock service and annotated with @before so that perform first, that is must.If we do not initialize @before then
	 * you can not mock any thing.
	 */
		@Before
		public void setUp() throws Exception
		{
			MockitoAnnotations.initMocks(this);
		}
	
		/**  
		 * In this method we tested IllegalArgumentException for setOrderStatus() method.
		 */
	
		@Test(expected=IllegalArgumentException.class)
		public void testSetOrderStatusIllegalArgument()
		{
			defaultCalculateOrderStatusService.setOrderStatus(null);
		}
		
		/**  
		 * valid test cases for setOrderStatus() method.
		 */			
		
		@Test
		public void  testSetOrderStatusValid()
		{
			
			given((OrderModel)consignmentModel.getOrder()).willReturn(orderModel);
			
			
		}
		

	

}
