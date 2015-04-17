package com.hybris.osh.test.warehouse.impl;



import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.fulfilmentservices.impl.DefaultCalculateOrderStatusService;
import com.hybris.osh.warehouse.impl.DefaultOshWarehouseService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JUnit test suite for{@link DefaultOshWarehouseService} 
 *
 */

public class DefaultOshWarehouseServiceTest
{
	
	
	@InjectMocks
    DefaultOshWarehouseService defaultOshWarehouseService=new DefaultOshWarehouseService();
	
	private final long consignment=12345L;
	
	@Mock
	private ConsignmentEntryModel consignmentEntryModel;
	
	@Mock
	private AbstractOrderEntryModel abstractOrderEntryModel;
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private ConsignmentModel consignmentModel;
	
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
	 * In this method we tested IllegalArgumentException for prepareConsignment() method.
	 */
	
	@Test(expected=IllegalArgumentException.class)
	public void testPrepareConsignmentInvalidArgument()
	{
		defaultOshWarehouseService.prepareConsignment(null);
	}
	
	
	
	@Test
	public void testPrepareConsignmentValid()
	{
		given(modelService.get(PK.fromLong(consignment))).willReturn(consignmentModel);
	}
	
	/**  
	 * In this method we vtested IllegalArgumentException for shipConsignment() method.
	 */
	
	@Test(expected=IllegalArgumentException.class)
	public void testShipConsignmentInvalidArgument()
	{
		defaultOshWarehouseService.shipConsignment(null);
	}
	
	@Test
	public void testShipConsignmentValid()
	{
		final long qty=10;
		
	    final String code = "online-" ;
		when(consignmentModel.getCode()).thenReturn(code);
		final Set<ConsignmentEntryModel> consignmentEntryModels=new HashSet<ConsignmentEntryModel>();
		final ConsignmentEntryModel consignmentEntryModel1=new ConsignmentEntryModel();
		final ConsignmentEntryModel consignmentEntryModel2=new ConsignmentEntryModel();
		consignmentEntryModels.add(consignmentEntryModel1);
		consignmentEntryModels.add(consignmentEntryModel2);
		when(consignmentModel.getConsignmentEntries()).thenReturn(consignmentEntryModels);
		when(consignmentEntryModel.isTransactionDone()).thenReturn(true);
		when(consignmentEntryModel.getOrderEntry()).thenReturn(abstractOrderEntryModel);
		when(abstractOrderEntryModel.getQuantity()).thenReturn(qty);
		verify(consignmentEntryModel, times(0)).setShippedQuantity(qty);
		verify(modelService,times(0)).save(consignmentEntryModel);
		
	}
	
	

}
