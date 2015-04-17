package com.hybris.osh.returns.strategy.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.returns.strategy.ReturnableCheck;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.Iterator;
import java.util.Set;

public class DefaultOshConsignmentBasedReturnableCheck extends AbstractBusinessService
implements ReturnableCheck {

	public boolean perform(final OrderModel order, final AbstractOrderEntryModel orderentry, final long returnQuantity)
    {
        if(returnQuantity < 1L || orderentry.getQuantity().longValue() < returnQuantity)
		{
			return false;
		}
        final Set consignments = BasecommerceManager.getInstance().getConsignments((Order)getModelService().toPersistenceLayer(order));
        boolean isReturnable = false;
        if(!consignments.isEmpty())
        {
            for(final Iterator iterator = consignments.iterator(); iterator.hasNext();)
            {
				final Consignment consignment = (Consignment) iterator.next();
                if(consignment.getStatus().getCode().equals(ConsignmentStatus.SHIPPED.getCode()) || consignment.getStatus().getCode().equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode())||consignment.getStatus().getCode().equals(ConsignmentStatus.PICKEDUP_AT_STORE.getCode()))
                {
                    final Set entries = consignment.getConsignmentEntries();
                    for(final Iterator iterator1 = entries.iterator(); iterator1.hasNext();)
                    {
                        final ConsignmentEntry entry = (ConsignmentEntry)iterator1.next();
                        if(modelService.toModelLayer(entry.getOrderEntry()).equals(orderentry))
								{
									isReturnable = entry.getShippedQuantityAsPrimitive() >= returnQuantity;
								}
                    }

                }
            }

        } else
        {
            return false;
        }
        return isReturnable;
    }

}
