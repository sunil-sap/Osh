package com.hybris.osh.fulfilmentservices.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.fulfilmentservices.CalculateOrderStatusService;


/**
 *
 */
public class DefaultCalculateOrderStatusService implements CalculateOrderStatusService
{
	@Resource(name = "modelService")
	ModelService modelService;
	private static final Logger LOG = Logger.getLogger(DefaultCalculateOrderStatusService.class);

	@Override
	public void setOrderStatus(final ConsignmentModel consignment)
	{
		validateParameterNotNull(consignment, "Consignment model cannot be null");
		final OrderModel orderModel = (OrderModel) consignment.getOrder();
		String OnlineConsignmentStatus = null;
		String onlineDropshipStatus = null;
		String dropshipConsignmentStatus = null;
		final int noOfconsignments = orderModel.getConsignments().size();
		String BopisConsignmentStatus = null;
		final Calendar calendar = Calendar.getInstance();

		for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
		{
			if (consignmentModel.getCode().contains("online-"))
			{
				OnlineConsignmentStatus = consignmentModel.getStatus().getCode();
			}
			else if (consignmentModel.getCode().contains("dropship-"))
			{
				dropshipConsignmentStatus = consignmentModel.getStatus().getCode();
			}

			else
			{
				BopisConsignmentStatus = consignmentModel.getStatus().getCode();
			}

		}
		if (OnlineConsignmentStatus != null && dropshipConsignmentStatus != null)
		{
			if (OnlineConsignmentStatus.equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode())
					|| dropshipConsignmentStatus.equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode()))
			{
				onlineDropshipStatus = ConsignmentStatus.PARTIAL_FULFILLED.getCode();
			}
			else if (OnlineConsignmentStatus.equals(ConsignmentStatus.SHIPPED.getCode())
					&& dropshipConsignmentStatus.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				onlineDropshipStatus = ConsignmentStatus.SHIPPED.getCode();
			}
			else if (OnlineConsignmentStatus.equals(ConsignmentStatus.SHIPPED.getCode())
					&& !dropshipConsignmentStatus.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				onlineDropshipStatus = ConsignmentStatus.PARTIAL_FULFILLED.getCode();
			}
			else if (!OnlineConsignmentStatus.equals(ConsignmentStatus.SHIPPED.getCode())
					&& dropshipConsignmentStatus.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				onlineDropshipStatus = ConsignmentStatus.PARTIAL_FULFILLED.getCode();
			}
			else if (OnlineConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode())
					&& dropshipConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode()))
			{
				onlineDropshipStatus = ConsignmentStatus.CANCELLED.getCode();
			}
			else if ((OnlineConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode()) && dropshipConsignmentStatus
					.equals(ConsignmentStatus.PENDING.getCode()))
					|| (OnlineConsignmentStatus.equals(ConsignmentStatus.PENDING.getCode()) && dropshipConsignmentStatus
							.equals(ConsignmentStatus.CANCELLED.getCode())))
			{
				onlineDropshipStatus = ConsignmentStatus.PENDING.getCode();
			}
			else
			{
				onlineDropshipStatus = ConsignmentStatus.PENDING.getCode();
			}

		}
		else if ((OnlineConsignmentStatus == null && dropshipConsignmentStatus != null)
				|| (OnlineConsignmentStatus != null && dropshipConsignmentStatus == null))
		{
			if (dropshipConsignmentStatus != null)
			{
				onlineDropshipStatus = dropshipConsignmentStatus;
			}
			else
			{
				onlineDropshipStatus = OnlineConsignmentStatus;
			}

		}


		//checking if the order is only online
		if (onlineDropshipStatus != null && BopisConsignmentStatus == null)
		{
			if (onlineDropshipStatus.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				orderModel.setStatus(OrderStatus.COMPLETED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] COMPLETED");
				}
				return;
			}
			else if (onlineDropshipStatus.equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.PARTIAL_FULFILLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}
			else if (onlineDropshipStatus.equals(ConsignmentStatus.CANCELLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.CANCELLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] CANCELLED");
				}
				return;
			}
		}
		//checking if the order is only BOPIS
		else if (BopisConsignmentStatus != null && OnlineConsignmentStatus == null)
		{
			if (BopisConsignmentStatus.equals(ConsignmentStatus.PICKEDUP_AT_STORE.getCode()))
			{
				orderModel.setStatus(OrderStatus.COMPLETED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}
			else if (BopisConsignmentStatus.equals(ConsignmentStatus.READY_FOR_PICKUP.getCode()))
			{
				orderModel.setStatus(OrderStatus.READY_FOR_PICKUP);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] READY_FOR_PICKUP");
				}
				return;
			}
			else if (BopisConsignmentStatus.equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.PARTIAL_FULFILLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}
			else if (BopisConsignmentStatus.equals(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL.getCode()))
			{
				orderModel.setStatus(OrderStatus.READY_FOR_PICKUP_PARTIAL);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] READY_FOR_PICKUP_PARTIAL");
				}
				return;
			}
			else if (BopisConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.CANCELLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] CANCELLED");
				}
				return;
			}
		}
		else
		//if the order is not only online or not only BOPIS and not only dropship then its Online+BOPIS+dropship
		{
			if (BopisConsignmentStatus.equals(ConsignmentStatus.PICKEDUP_AT_STORE.getCode())
					&& onlineDropshipStatus.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				orderModel.setStatus(OrderStatus.COMPLETED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] COMPLETED");
				}
				return;
			}
			else if (BopisConsignmentStatus.equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode())
					|| onlineDropshipStatus.equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.PARTIAL_FULFILLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}
			else if (BopisConsignmentStatus.equals(ConsignmentStatus.PICKEDUP_AT_STORE.getCode())
					&& onlineDropshipStatus.equals(ConsignmentStatus.PENDING.getCode()))
			{
				orderModel.setStatus(OrderStatus.PARTIAL_FULFILLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}
			else if ((BopisConsignmentStatus.equals(ConsignmentStatus.READY_FOR_PICKUP.getCode())
					|| BopisConsignmentStatus.equals(ConsignmentStatus.PENDING.getCode()) || BopisConsignmentStatus
						.equals(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL.getCode()))
					&& onlineDropshipStatus.equals(ConsignmentStatus.SHIPPED.getCode()))
			{
				orderModel.setStatus(OrderStatus.PARTIAL_FULFILLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}

			else if (BopisConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode())
					&& onlineDropshipStatus.equals(ConsignmentStatus.CANCELLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.CANCELLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] CANCELLED");
				}
				return;
			}
			else if ((BopisConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode()) && onlineDropshipStatus
					.equals(ConsignmentStatus.PENDING.getCode()))
					|| (BopisConsignmentStatus.equals(ConsignmentStatus.PENDING.getCode()) && onlineDropshipStatus
							.equals(ConsignmentStatus.CANCELLED.getCode())))
			{
				orderModel.setStatus(OrderStatus.PENDING);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PENDING");
				}
				return;
			}

			else if ((BopisConsignmentStatus.equals(ConsignmentStatus.PICKEDUP_AT_STORE.getCode()) && onlineDropshipStatus
					.equals(ConsignmentStatus.CANCELLED.getCode()))
					|| (BopisConsignmentStatus.equals(ConsignmentStatus.CANCELLED.getCode()) && onlineDropshipStatus
							.equals(ConsignmentStatus.SHIPPED.getCode())))
			{
				orderModel.setStatus(OrderStatus.PARTIAL_FULFILLED);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PARTIAL_FULFILLED");
				}
				return;
			}
			else if ((BopisConsignmentStatus.equals(ConsignmentStatus.READY_FOR_PICKUP.getCode())
					|| BopisConsignmentStatus.equals(ConsignmentStatus.PENDING.getCode()) || BopisConsignmentStatus
						.equals(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL.getCode()))
					&& onlineDropshipStatus.equals(ConsignmentStatus.CANCELLED.getCode()))
			{
				orderModel.setStatus(OrderStatus.PENDING);
				modelService.save(orderModel);
				modelService.refresh(orderModel);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Order [" + orderModel.getCode() + "] PENDING");
				}
				return;
			}

		}


	}

}
