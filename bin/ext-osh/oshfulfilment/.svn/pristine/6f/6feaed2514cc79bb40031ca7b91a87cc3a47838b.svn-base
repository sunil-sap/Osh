package com.hybris.osh.ordercancel.impl.executors;

import org.apache.log4j.Logger;

import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.OrderCancelWarehouseAdapter;
import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.impl.executors.NotificationServiceAdapterDependent;
import de.hybris.platform.ordercancel.impl.executors.WarehouseAdapterDependent;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class OshWarehouseProcessingCancelRequestExecutor 
	implements OrderCancelRequestExecutor, NotificationServiceAdapterDependent
{

    public OshWarehouseProcessingCancelRequestExecutor()
    {
    }

    public void processCancelRequest(OrderCancelRequest orderCancelRequest, OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        orderStatusChangeStrategy.changeOrderStatusAfterCancelOperation(cancelRequestRecordEntry, true);
        if(notificationServiceAdapter == null)
            LOG.info((new StringBuilder("order: ")).append(orderCancelRequest.getOrder().getCode()).append(" is being cancelled").toString());
        else
            notificationServiceAdapter.sendCancelPendingNotifications(cancelRequestRecordEntry);
    }

    public ModelService getModelService()
    {
        return modelService;
    }

    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
    
    public OrderCancelNotificationServiceAdapter getNotificationServiceAdapter()
    {
        return notificationServiceAdapter;
    }

    public void setNotificationServiceAdapter(OrderCancelNotificationServiceAdapter notificationServiceAdapter)
    {
        this.notificationServiceAdapter = notificationServiceAdapter;
    }

    public OrderStatusChangeStrategy getOrderStatusChangeStrategy()
    {
        return orderStatusChangeStrategy;
    }

    public void setOrderStatusChangeStrategy(OrderStatusChangeStrategy orderStatusChangeStrategy)
    {
        this.orderStatusChangeStrategy = orderStatusChangeStrategy;
    }

    private static final Logger LOG = Logger.getLogger(OshWarehouseProcessingCancelRequestExecutor.class);
    private ModelService modelService;
    private OrderStatusChangeStrategy orderStatusChangeStrategy;
    private OrderCancelNotificationServiceAdapter notificationServiceAdapter;
}

