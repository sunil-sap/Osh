package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.core.service.DropshipPOFileCreatorService;


/**
 * 
 */
public class DefaultDropshipPOFileCreatorService implements DropshipPOFileCreatorService
{
	public StringBuilder recordData;

	@Resource(name = "modelService")
	private ModelService modelService;


	@Override
	public String createPOHDRRecords(final OrderModel order)
	{
		//Assert.notNull(consignment, "Parameter Cannot Be NULL");
		//final OrderModel order = (OrderModel) consignment.getOrder();
		Assert.notNull(order, "Order Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("HDR|");
		recordData.append(order.getCode() + "|");
		recordData.append("WebOrder|");
		if (order.getStatus() == OrderStatus.PENDING)
		{
			recordData.append("2");
		}
		if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.PARTIAL_FULFILLED)
		{
			recordData.append("4");
		}
		if (order.getStatus() == OrderStatus.CANCELLED)
		{
			recordData.append("5");
		}

		recordData.append("|N|");
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		recordData.append(sdf.format(order.getCreationtime()) + "|");
		recordData.append("||");
		if (order.getCancelDate() != null)
		{
			recordData.append(sdf.format(order.getCancelDate()));
		}
		recordData.append("|||||");
		if (order.getGiftMessage() != null)
		{
			recordData.append(order.getGiftMessage());
		}
		recordData.append("|" + createEmptyFields(15, 34));
		if (order.getExternalOrderId() != null && !order.getExternalOrderId().isEmpty())
		{
			recordData.append("|" + order.getExternalOrderId());
		}
		else
		{
			recordData.append("|");
		}
		recordData.append("|" + createEmptyFields(36, 50));
		return recordData.toString();
	}

	@Override
	public String createPOCSTBilingRecords(final OrderModel order)
	{

		Assert.notNull(order, "Order Cannot Be NULL");
		final AddressModel billingAddress = order.getPaymentAddress();

		Assert.notNull(billingAddress, "Billing Address Cannot Be NULL be null for " + order.getCode());

		recordData = new StringBuilder();
		recordData.append("CST|");
		recordData.append(order.getCode() + "|");
		if (order.getUser() != null)
		{
			final CustomerModel customer = (CustomerModel) order.getUser();
			if (customer.getCustomerAccNO() != null)
			{
				recordData.append(customer.getCustomerAccNO());
			}
		}
		recordData.append("|");
		recordData.append(billingAddress.getFirstname() + "|");
		if (billingAddress.getMiddlename() != null)
		{
			recordData.append(billingAddress.getMiddlename());
		}
		recordData.append("|" + billingAddress.getLastname() + "|");
		recordData.append("|" + billingAddress.getLine1() + "|");
		if (billingAddress.getLine2() != null)
		{
			recordData.append(billingAddress.getLine2());
		}
		recordData.append("|");
		recordData.append(billingAddress.getTown() + "|");
		recordData.append(billingAddress.getRegion().getIsocode() + "|");
		recordData.append(billingAddress.getPostalcode() + "|");
		recordData.append(billingAddress.getCountry().getIsocode() + "|");
		if (billingAddress.getPhone1() != null)
		{
			recordData.append(billingAddress.getPhone1());
		}
		recordData.append("||" + billingAddress.getEmail() + "|||");
		if (order.getUser() != null)
		{
			final CustomerModel customer = (CustomerModel) order.getUser();
			if (customer.getCustomerAccNO() != null)
			{
				recordData.append(customer.getCustomerAccNO());
			}
		}
		else
		{
			recordData.append("|");
		}
		recordData.append("|" + createEmptyFields(20, 50));
		return recordData.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.DropshipPOFileCreatorService#createPOCSTShippingRecords(de.hybris.platform.core.model
	 * .order.OrderModel)
	 */
	@Override
	public String createPOCSTShippingRecords(final OrderModel order)
	{
		Assert.notNull(order, "Order Cannot Be NULL");
		final AddressModel billingAddress = order.getDeliveryAddress();
		Assert.notNull(billingAddress, "Shipping Address Cannot Be NULL for " + order.getCode());

		recordData = new StringBuilder();
		recordData.append("CST|");
		recordData.append(order.getCode() + "|");
		if (order.getOrderType().equals("online"))
		{
			recordData.append(billingAddress.getFirstname() + " " + billingAddress.getLastname() + "|");
			recordData.append(billingAddress.getLine1() + "|");
			if (billingAddress.getLine2() != null)
			{
				recordData.append(billingAddress.getLine2());
			}
			recordData.append("|");
			recordData.append(billingAddress.getTown() + "|");
			recordData.append(billingAddress.getRegion().getIsocode() + "|");
			recordData.append(billingAddress.getPostalcode() + "|");
			recordData.append(billingAddress.getCountry().getIsocode() + "|");
			if (billingAddress.getPhone1() != null)
			{
				recordData.append(billingAddress.getPhone1());
			}
			recordData.append("|||");
			recordData.append(createEmptyFields(13, 50));
		}
		else
		{
			recordData.append(createEmptyFields(3, 50));
		}
		return recordData.toString();
	}

	@Override
	public String createPOSKURecords(final OrderEntryModel orderEntry)
	{
		int entryNumber;
		entryNumber = orderEntry.getEntryNumber() + 1;
		Assert.notNull(orderEntry, "OrderEntry Cannot Be NULL");
		final OrderModel order = orderEntry.getOrder();
		final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
		final ProductModel product = orderEntry.getProduct();
		Assert.notNull(order, "Order Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("SKU");
		recordData.append("|" + order.getCode());
		recordData.append("|" + entryNumber + "|");
		recordData.append("|" + product.getCode() + "|");
		recordData.append("|" + consignmentEntry.getQuantity() + "||");
		if (consignmentEntry.getCancelQty() != null)
		{
			recordData.append(consignmentEntry.getCancelQty());
		}
		else
		{
			recordData.append("0");
		}
		recordData.append("|" + orderEntry.getBasePrice());
		recordData.append("|" + createEmptyFields(11, 20));
		if (consignmentEntry.getTrackingId() != null && !consignmentEntry.getTrackingId().isEmpty())
		{
			recordData.append("|" + consignmentEntry.getTrackingId());
		}
		else
		{
			recordData.append("|");
		}
		recordData.append("|" + createEmptyFields(22, 50));
		consignmentEntry.setPoPerformed(true);
		modelService.save(consignmentEntry);
		modelService.refresh(consignmentEntry);
		return recordData.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.DropshipPOFileCreatorService#createPOSHPRecords(de.hybris.platform.core.model.order
	 * .OrderModel)
	 */
	@Override
	public String createPOSHPRecords(final OrderModel order)
	{
		Assert.notNull(order, "Order Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("SHP");
		recordData.append("|" + order.getCode());
		if (order.getOrderType().equals("online"))
		{
			final String shippingMethodId = order.getDeliveryMode().getCode();
			final String shippingId[] = shippingMethodId.split("-");
			recordData.append("|");
			if (0 < shippingId.length && shippingId[0] != null && !shippingId[0].isEmpty())
			{
				recordData.append(shippingId[0]);
			}

			recordData.append("|" + order.getDeliveryMode().getDescription());
		}
		else
		{
			recordData.append("||");
		}
		//for shipping cost and Tax 1 name field
		recordData.append("|0.00||");

		//for Tax 1 and Tax 2 name field
		recordData.append("0.00||");

		//for Tax 2 , Shipping discount field and tax group id
		recordData.append("0.00|0.00||");

		//net shipping amount, shipping vat	
		recordData.append("0.00|0.00||||");

		//surcharge Tax1, Tax2, Surcharge amt order 
		recordData.append("0.00|0.00|0.00|");

		recordData.append("|" + createEmptyFields(21, 50));
		return recordData.toString();
	}

	@Override
	public String createPOSHPTAXRecords(final OrderModel order)
	{
		Assert.notNull(order, "Order Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("SHPTAX|");
		recordData.append(createEmptyFields(2, 50));
		return recordData.toString();

	}

	@Override
	public String createPOTOTRecords(final OrderModel order)
	{
		Assert.notNull(order, "Order Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("TOT");
		recordData.append("|" + order.getCode());
		recordData.append("|" + createEmptyFields(3, 50));
		return recordData.toString();
	}


	@Override
	public String createPOTNDRecords(final OrderModel order)
	{
		Assert.notNull(order, "Order Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("TND");
		recordData.append("|" + createEmptyFields(2, 50));
		return recordData.toString();
	}

	@Override
	public List<String> createSKULevelRecords(final ConsignmentModel consignment)
	{
		final List<String> skuRecords = new ArrayList<String>();
		for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
		{
			skuRecords.add(createPOSKURecords((OrderEntryModel) entry.getOrderEntry()));
		}
		return skuRecords;
	}

	@Override
	public List<String> createCancelSKULevelRecords(final ConsignmentModel consignment)
	{
		final List<String> skuRecords = new ArrayList<String>();
		for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
		{
			if (entry.getCancelQty() != null && entry.getCancelQty().intValue() != 0 && !entry.isPoConfirmPerformed())
			{
				skuRecords.add(createPOSKURecords((OrderEntryModel) entry.getOrderEntry()));
				entry.setPoConfirmPerformed(true);
				modelService.save(entry);
				modelService.refresh(entry);
			}
		}
		return skuRecords;
	}

	public StringBuilder createEmptyFields(final int startIndex, final int endIndex)
	{
		final StringBuilder emptyFields = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++)
		{
			emptyFields.append("|");
		}
		return emptyFields;
	}

	@Override
	public List<ConsignmentEntryModel> getEligiblePOConsignment(final Set<ConsignmentModel> consignments)
	{
		Assert.notEmpty(consignments, "Consignments Must not be empty");

		final List<ConsignmentEntryModel> entries = new ArrayList<ConsignmentEntryModel>();
		for (final ConsignmentModel consignmentModel : consignments)
		{
			final Set<ConsignmentEntryModel> consignmentEntries = consignmentModel.getConsignmentEntries();
			for (final ConsignmentEntryModel entry : consignmentEntries)
			{
				final Integer cancelQty = entry.getCancelQty();
				if (entry.getEntryLevelStatus() == ConsignmentEntryStatus.PENDING && !entry.isPoPerformed() && cancelQty == 0)
				{
					entries.add(entry);
				}
			}

		}
		return entries;
	}
}
