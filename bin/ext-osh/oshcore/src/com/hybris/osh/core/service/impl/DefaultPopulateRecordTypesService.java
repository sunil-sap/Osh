package com.hybris.osh.core.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.OshOrderDao;
import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.core.model.CreditPaymentTransactionModel;
import com.hybris.osh.core.price.facade.OshPriceFacade;
import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;
import com.hybris.osh.core.service.PopulateRecordTypesService;


/**
 * 
 */
public class DefaultPopulateRecordTypesService implements PopulateRecordTypesService
{

	public StringBuilder recordData;
	public static Double linenum;
	@Resource(name = "oshPriceFacade")
	private OshPriceFacade oshPriceFacade;
	public static int totalTransactions = 0;
	public static double totalAmount = 0.0;
	public static double returnDiscount = 0.0;
	public static double totalTax = 0.0;
	public static long orderQty = 0L;
	public static double unitDiscount = 0.0;
	public static String unitShipping = null;
	public static double totalReturnTax = 0.0;
	public static String orderType = null;
	//public static double totalDiscount = 0.0;
	public static double orderTNDTotal = 0.0;
	public static boolean isTaxPromo = false;

	static private final Logger LOG = Logger.getLogger(DefaultPopulateRecordTypesService.class);

	public boolean isEmptyReturnOrder = true;
	public boolean isPartialFulfilled = false;
	boolean isReturnInfoEmpty = true;
	boolean isShipped = false;
	Map<String, Object> retunInfo = null;
	private static final String CASH_REGISTER_NO = "tlog.file.initial.cash.register.no";
	private static final String TRANS_NO = "tlog.file.initial.transaction.no";
	/* private static final String TIME_ZONE = "tlog.file.hdr.transaction.date.timezone"; */
	final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	private OshOrderDao oshOrderDao;
	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public String createHDRRecords(final OrderModel order, final Map<String, Object> retunInfo)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		isEmptyReturnOrder = order.getReturnRequests().isEmpty();
		if (!isEmptyReturnOrder)
		{
			if (retunInfo != null)
			{
				isReturnInfoEmpty = retunInfo.isEmpty();
				if (isReturnInfoEmpty)
				{
					isShipped = true;
				}
				else
				{
					isShipped = false;
				}
			}
			else
			{
				isShipped = true;
			}
			if (!isEmptyReturnOrder)
			{
				setTotalCapturedAmount(order);
			}
		}
		else
		{
			isShipped = true;
		}

		String storeId = null;
		if (order.getStatus() == OrderStatus.PARTIAL_FULFILLED || order.getStatus() == OrderStatus.COMPLETED)
		{
			isPartialFulfilled = true;
		}
		recordData = new StringBuilder();
		recordData.append("HDR|");

		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			storeId = order.getStoreID();
			recordData.append(order.getStoreID());
		}
		else
		{
			storeId = "0799";
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|");
		final Date date = order.getCreationtime();
		if (isEmptyReturnOrder || isReturnInfoEmpty)
		{
			orderType = "SALE";
			recordData.append("|SALE");
		}
		else
		{
			orderType = "RETURN";
			recordData.append("|RETURN");
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final SimpleDateFormat registerDate = new SimpleDateFormat("MMddyyyy");
		/*
		 * final String timeZone = configurationService.getConfiguration().getString(TIME_ZONE); if (timeZone != null &&
		 * !timeZone.isEmpty()) { sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		 * registerDate.setTimeZone(TimeZone.getTimeZone(timeZone)); }
		 */

		if (!isEmptyReturnOrder && !isReturnInfoEmpty)
		{
			if (order.getReturnTransactionDate() != null)
			{
				recordData.append("|" + sdf.format(order.getReturnTransactionDate()));
			}
			else
			{
				recordData.append("|" + sdf.format(date));
			}
		}
		else
		{
			recordData.append("|" + sdf.format(date));
		}



		if (!isEmptyReturnOrder && !isReturnInfoEmpty)
		{
			recordData.append("|" + order.getCode());
			recordData.append("|" + sdf.format(order.getCreationtime()) + "|");
		}
		else
		{
			recordData.append("|||");
		}
		String transactionDate = null;
		if (order.getTransactionDate() == null)
		{
			transactionDate = registerDate.format(date);
			order.setTransactionDate(transactionDate);
		}
		else
		{
			transactionDate = order.getTransactionDate();
		}
		final String transactionNo = String.format("%04d", Long.valueOf(order.getTransNo()));
		final String formattedStoreId = String.format("%04d", Long.valueOf(storeId));
		final String registerTransNo = formattedStoreId + order.getCashRegisterNo() + transactionNo + transactionDate + "N";

		if (isEmptyReturnOrder)
		{
			recordData.append(registerTransNo);
		}
		recordData.append("|" + order.getCode() + "|");
		if (!isEmptyReturnOrder && !isReturnInfoEmpty)
		{
			recordData.append(registerTransNo);
		}
		recordData.append("|N");
		recordData.append("|");
		if (order.getLoyaltyVoucher() != null && !order.getLoyaltyVoucher().isEmpty())
		{
			recordData.append(order.getLoyaltyVoucher());
		}
		if (order.getGiftMessage() == null)
		{
			recordData.append("||");
		}
		else
		{
			final String giftMessage = order.getGiftMessage().replaceAll("[\r\n]+", " ");
			recordData.append("|" + giftMessage + "|");
		}


		/*
		 * if (isTaxPromo) { recordData.append(0.0); }
		 * 
		 * else
		 * 
		 * { recordData.append(order.getTotalDiscounts()); }
		 */

		recordData.append("|");
		final CustomerModel customer = (CustomerModel) order.getUser();
		if (customer.getLoyaltyNumber() != null)
		{
			recordData.append(customer.getLoyaltyNumber());
		}
		recordData.append("|");
		recordData.append("|" + createEmptyFields(19, 50));
		totalTransactions++;
		LOG.info("HDR RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		modelService.save(order);
		modelService.refresh(order);
		return recordData.toString();
	}

	@Override
	public String createCSTBillingRecords(final OrderModel orderModel)
	{
		final AbstractOrderModel order = orderModel;
		Assert.notNull(order, "Parameter Cannot Be NULL");
		final AddressModel billingAddress = order.getPaymentAddress();
		Assert.notNull(billingAddress, "Billing Address Cannot Be NULL for Order:" + order.getCode());

		recordData = new StringBuilder();
		recordData.append("CST|");
		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			recordData.append(order.getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|100000.1|");

		if (order.getUser() != null)
		{
			final CustomerModel customer = (CustomerModel) order.getUser();
			if (customer.getCustomerAccNO() != null)
			{
				recordData.append(customer.getCustomerAccNO());
			}
		}
		recordData.append("|" + billingAddress.getLine1());
		recordData.append("|" + billingAddress.getLine2());
		recordData.append("|" + billingAddress.getTown());
		recordData.append("|" + billingAddress.getRegion().getIsocode());
		recordData.append("|" + billingAddress.getPostalcode());
		recordData.append("|" + billingAddress.getCountry().getIsocode());
		recordData.append("|" + billingAddress.getEmail());
		if (billingAddress.getPhone1() == null)
		{
			recordData.append("|");
		}
		else
		{
			recordData.append("|" + billingAddress.getPhone1());
		}
		recordData.append("|" + billingAddress.getFirstname());
		recordData.append("|" + billingAddress.getLastname());
		recordData.append("|" + createEmptyFields(17, 50));
		LOG.info("CST BILLING RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		return recordData.toString();
	}

	@Override
	public String createCSTShippingRecords(final OrderModel order)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		final AddressModel shippingAddress = order.getDeliveryAddress();
		Assert.notNull(shippingAddress, "Shipping Address Cannot Be NULL for Order " + order.getCode());

		recordData = new StringBuilder();
		recordData.append("CST|");
		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			recordData.append(order.getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|100000.2");
		if (order.getOrderType().equals("online"))
		{
			recordData.append("|");

			if (order.getUser() != null)
			{
				final CustomerModel customer = (CustomerModel) order.getUser();
				if (customer.getCustomerAccNO() != null)
				{
					recordData.append(customer.getCustomerAccNO());
				}
			}
			recordData.append("|" + shippingAddress.getLine1());
			recordData.append("|" + shippingAddress.getLine2());
			recordData.append("|" + shippingAddress.getTown());
			recordData.append("|" + shippingAddress.getRegion().getIsocode());
			recordData.append("|" + shippingAddress.getPostalcode());
			recordData.append("|" + shippingAddress.getCountry().getIsocode());
			recordData.append("|" + shippingAddress.getEmail());
			if (shippingAddress.getPhone1() == null)
			{
				recordData.append("|");
			}
			else
			{
				recordData.append("|" + shippingAddress.getPhone1());
			}
			recordData.append("|" + shippingAddress.getFirstname());
			recordData.append("|" + shippingAddress.getLastname());
			recordData.append("|" + createEmptyFields(17, 50));
		}
		else
		{
			recordData.append("|" + createEmptyFields(5, 50));
		}
		LOG.info("CST SHIPPING RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		return recordData.toString();
	}

	@Override
	public String createSKURecords(final ConsignmentEntryModel consignmentEntry)
	{
		Assert.notNull(consignmentEntry, "Parameter Cannot Be NULL");
		final AbstractOrderEntryModel orderEntry = consignmentEntry.getOrderEntry();
		Assert.notNull(orderEntry, "OrderEntry Cannot Be NULL");
		final ProductModel product = orderEntry.getProduct();
		final List<PriceRow> priceRows = oshPriceFacade.getPriceInfo(product);
		recordData = new StringBuilder();
		recordData.append("SKU|");
		if (orderEntry.getOrder().getStoreID() != null && !orderEntry.getOrder().getStoreID().isEmpty())
		{
			recordData.append(orderEntry.getOrder().getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + orderEntry.getOrder().getCashRegisterNo());
		recordData.append("|" + orderEntry.getOrder().getTransNo());
		//recordData.append("|STYLE_ID");
		recordData.append("|||" + product.getCode());
		recordData.append("|" + orderEntry.getQuantity());
		recordData.append("|" + orderEntry.getBasePrice());
		//recordData.append("|" + orderEntry.getEntryNumber());
		recordData.append("||" + createEmptyFields(11, 14));
		//recordData.append("|N");
		recordData.append("|");
		for (final PriceRow priceinfo : priceRows)
		{
			if (priceinfo.getUg().getCode().equals("Osh_RegularPrice") && priceinfo.getPrice() != null)
			{
				recordData.append("|" + priceinfo.getPrice());
			}
		}
		//recordData.append("|SURCHARE_SKU_ID");
		recordData.append("|");
		recordData.append("|0");
		/*
		 * recordData.append("|SHIPPING_SURCHARE_TAX1"); recordData.append("|SHIPPING_SURCHARE_TAX2");
		 * recordData.append("|Shipped_date"); recordData.append("|Extra_Charge_amount");
		 * recordData.append("|Extra_Charge_type"); recordData.append("|INTERNAL_SKU_ID"); recordData.append("|Size_ID");
		 * recordData.append("|Dimension_ID"); recordData.append("|DISPOSITION_CODE");
		 * recordData.append("|DISPOSITION_CODE_DESCRIPTION"); recordData.append("|INTERNAL_LOCATION_ID");
		 */
		recordData.append("|" + createEmptyFields(19, 50));
		consignmentEntry.setTlogPerformed(true);
		modelService.save(consignmentEntry);
		modelService.refresh(consignmentEntry);
		LOG.info("SKU RECORD FOR ORDER " + orderEntry.getOrder().getCode());
		LOG.info(recordData.toString());
		return recordData.toString();
	}

	@Override
	public String createRTNRecord(final AbstractOrderEntryModel orderEntry)
	{
		Assert.notNull(orderEntry, "OrderEntry Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("RTN|");
		if (orderEntry.getOrder().getStoreID() != null && !orderEntry.getOrder().getStoreID().isEmpty())
		{
			recordData.append(orderEntry.getOrder().getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + orderEntry.getOrder().getCashRegisterNo());
		recordData.append("|" + orderEntry.getOrder().getTransNo());
		recordData.append("|" + createEmptyFields(5, 50));
		return recordData.toString();
	}

	@Override
	public String createReturnSKURecords(final ReturnEntryModel returnEntry)
	{

		final AbstractOrderEntryModel orderEntry = returnEntry.getOrderEntry();
		Assert.notNull(orderEntry, "OrderEntry Cannot Be NULL");
		final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
		Assert.notNull(consignmentEntry, "ConsignmentEntry Cannot Be NULL");
		final ProductModel product = orderEntry.getProduct();
		final List<PriceRow> priceRows = oshPriceFacade.getPriceInfo(product);
		recordData = new StringBuilder();
		recordData.append("SKU|");
		if (orderEntry.getOrder().getStoreID() != null && !orderEntry.getOrder().getStoreID().isEmpty())
		{
			recordData.append(orderEntry.getOrder().getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + orderEntry.getOrder().getCashRegisterNo());
		recordData.append("|" + orderEntry.getOrder().getTransNo());
		//recordData.append("|STYLE_ID");
		recordData.append("|||" + product.getCode());
		recordData.append("|" + returnEntry.getExpectedQuantity());
		recordData.append("|" + orderEntry.getBasePrice());
		//recordData.append("|" + orderEntry.getEntryNumber());
		recordData.append("||" + createEmptyFields(11, 14));
		//recordData.append("|N");
		recordData.append("|");
		for (final PriceRow priceinfo : priceRows)
		{
			if (priceinfo.getUg().getCode().equals("Osh_RegularPrice") && priceinfo.getPrice() != null)
			{
				recordData.append("|" + priceinfo.getPrice());
			}
		}
		recordData.append("|");
		recordData.append("|0");
		recordData.append("|" + createEmptyFields(19, 50));
		consignmentEntry.setTlogPerformed(true);
		modelService.save(consignmentEntry);
		modelService.refresh(consignmentEntry);
		LOG.info("SKU RECORD FOR RETURN ORDER " + orderEntry.getOrder().getCode());
		LOG.info(recordData.toString());
		return recordData.toString();
	}





	@Override
	public String createLDSRecords(final OrderModel order, final Map<String, Object> retunInfo)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("LDS|");
		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			recordData.append(order.getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("||");
		//final double totalDiscount = getProductsDiscountsAmount(order);
		final double totalDiscount = unitDiscount;
		final double nst = nstAmountCalculation(retunInfo);
		//nst = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), nst).doubleValue();
		/*
		 * if ((order.getDiscounts() != null) && (!order.getDiscounts().isEmpty())) {
		 * recordData.append(order.getDiscounts().get(0).getCode()); }
		 */

		if (isTaxPromo)
		{
			recordData.append("NST");
		}



		if (!isEmptyReturnOrder && returnDiscount != 0.0)
		{
			final double remainDiscount = (returnDiscount - nst);
			if (remainDiscount <= 0.0)
			{
				recordData.append("|" + "0.0");
			}
			else
			{
				recordData.append("|" + roundOff(remainDiscount));
			}
		}
		else if (!isTaxPromo && isPartialFulfilled && totalDiscount != 0.0)
		{
			recordData.append("|-" + unitDiscount);
		}
		else if (isTaxPromo && isPartialFulfilled && totalDiscount != 0.0)
		{
			if ((unitDiscount - nst) <= 0.0)
			{
				recordData.append("|0.0");
			}
			else
			{
				final double remainDiscount = (unitDiscount - nst);
				/*
				 * if(nst > unitDiscount) { recordData.append("|" + roundOff(remainDiscount)); } else {
				 *///remainDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), remainDiscount).doubleValue();
				recordData.append("|-" + roundOff(remainDiscount));
				//recordData.append("|-" + remainDiscount);
				/* } */
			}

		}
		else
		{
			recordData.append("|" + totalDiscount);

		}

		/*
		 * if (unitDiscount == 0) { recordData.append("|");
		 * 
		 * } else {
		 */

		//}
		/*
		 * recordData.append("|CUST_EVENT_NO"); recordData.append("|DISC_TYPE"); recordData.append("|DISC_CODE");
		 * recordData.append("|DISC_DESC");
		 */
		recordData.append("|||||");
		if (isTaxPromo)
		{
			//recordData.append("-" + order.getTaxAmount());

			if (retunInfo != null && !retunInfo.isEmpty())
			{
				recordData.append(roundOff(nst));
			}
			else
			{
				recordData.append("-" + roundOff(nst));
			}
		}

		recordData.append("|" + createEmptyFields(13, 50));

		LOG.info("LDS RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		return recordData.toString();
	}

	@Override
	public String createTAXRecords(final OrderModel order)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("TAX|");
		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			recordData.append(order.getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|");
		//recordData.append("|TAX_CD");
		recordData.append("||");

		if (isTaxPromo)
		{
			recordData.append("|0.0");
		}
		else
		{
			if (totalReturnTax != 0.0)
			{
				//recordData.append("|" + (Math.round(totalReturnTax * 100) / 100D));
				recordData.append("|" + roundOff(totalReturnTax));
			}
			else
			{
				if (isPartialFulfilled)
				{
					//recordData.append("|" + (Math.round(totalTax * 100) / 100D));
					recordData.append("|" + roundOff(totalTax));
				}
				else
				{
					//recordData.append("|" + (Math.round(order.getTaxAmount() * 100) / 100D));
					recordData.append("|" + roundOff(order.getTaxAmount()));
				}
			}
		}
		/*
		 * recordData.append("|UNIT_VAT"); recordData.append("|SHIPPING_VAT");
		 */
		recordData.append("|" + createEmptyFields(9, 50));

		LOG.info("TAX RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());

		return recordData.toString();
	}

	@Override
	public String createSHPRecords(final OrderModel order)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		recordData = new StringBuilder();
		recordData.append("SHP|");
		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			recordData.append(order.getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		//totalDiscount = getProductsDiscountsAmount(order);
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|||");
		if (unitShipping == null)
		{
			unitShipping = "0.0";
		}
		if (order.getOrderType().equals("online") && isShipped)
		{
			recordData.append(unitShipping);
		}
		if (!isEmptyReturnOrder && !isReturnInfoEmpty && !isShipped)
		{
			recordData.append("0.0");
		}
		//recordData.append("|" + order.getDeliveryMode().getCode());
		/*
		 * if (isEmptyReturnOrder) { recordData.append("||" + order.getDeliveryCost()); }
		 */
		/*
		 * if (!isEmptyReturnOrder) { recordData.append("||" + unitShipping * orderQty); }
		 */
		//totalShippingCost = totalShippingCost + order.getDeliveryCost().doubleValue();
		recordData.append("|" + createEmptyFields(8, 50));
		LOG.info("SHP RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		return recordData.toString();
	}

	@Override
	public String createTNDRecords(final OrderModel order)
	{

		Assert.notNull(order, "Parameter Cannot Be NULL");
		final CreditCardPaymentInfoModel ccpaymentInfo = (CreditCardPaymentInfoModel) order.getPaymentInfo();

		recordData = new StringBuilder();
		recordData.append("TND|");
		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			recordData.append(order.getStoreID());
		}
		else
		{
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|");
		if (ccpaymentInfo.getType() == CreditCardType.VISA)
		{
			recordData.append("|VI");
			recordData.append("|V");
		}
		if (ccpaymentInfo.getType() == CreditCardType.MASTERCARD)
		{
			recordData.append("|M");
			recordData.append("|M");
		}
		if (ccpaymentInfo.getType() == CreditCardType.AMEX)
		{
			recordData.append("|A");
			recordData.append("|A");
		}
		if (ccpaymentInfo.getType() == CreditCardType.DISCOVER)
		{
			recordData.append("|DI");
			recordData.append("|D");
		}
		orderTNDTotal = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), orderTNDTotal).doubleValue();
		totalAmount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), totalAmount).doubleValue();
		if (orderTNDTotal != 0.0)
		{
			//recordData.append("|" + getTotalAutherizedAmount(order));
			recordData.append("|" + orderTNDTotal);
		}
		else
		{
			recordData.append("|" + totalAmount);
		}
		recordData.append("|" + ccpaymentInfo.getCode());
		recordData.append("|" + ccpaymentInfo.getNumber());
		final String month = String.format("%02d", Long.valueOf(ccpaymentInfo.getValidToMonth()));
		recordData.append("|" + month + "-" + ccpaymentInfo.getValidToYear());
		/*
		 * recordData.append("|" + ccpaymentInfo.getNumber()); recordData.append("|Transaction_Description");
		 * recordData.append("|GE_Auth_Status"); recordData.append("|GE_Promo_Code");
		 */
		recordData.append("|" + createEmptyFields(12, 50));
		resetReturnValues();
		LOG.info("TND RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		return recordData.toString();

	}

	@Override
	public String createSCRRecords(final List<OrderModel> orders)
	{
		//Assert.notNull(orders, "Parameter Cannot Be NULL");
		//Assert.notEmpty(orders, "No Orders Found");
		recordData = new StringBuilder();
		recordData.append("SCR|");

		/*
		 * if (!order.getStore().getUid().equals("osh")) { recordData.append(order.getStore()); }
		 */

		recordData.append("0799|000");
		recordData.append("|0000");
		recordData.append("|");
		recordData.append("|" + totalTransactions);
		/*
		 * recordData.append("|" + totqlQty); recordData.append("|" + totalShippingCost); recordData.append("|TOTAL_TAX");
		 * recordData.append("|TENDER_TOTAL"); recordData.append("|TRANSDATE");
		 * recordData.append("|TOTAL_MEMBERSHIP_DISCOUNT"); recordData.append("|TOTAL_SHIPPING_BEFORE_DISCOUNT");
		 * recordData.append("|EXTENDED_TOTAL"); recordData.append("|TOTAL_TAX1"); recordData.append("|TOTAL_TAX2");
		 */
		recordData.append("|" + createEmptyFields(7, 50));
		totalTransactions = 0;
		LOG.info("SCR RECORD FOR ORDER ");
		LOG.info(recordData.toString());
		return recordData.toString();
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
	public boolean setCashRegiserNo(final CartModel cart)
	{
		Long cashRegisterNo = Long.valueOf(configurationService.getConfiguration().getLong(CASH_REGISTER_NO));
		Long transNo = Long.valueOf(configurationService.getConfiguration().getLong(TRANS_NO));
		Assert.notNull(cashRegisterNo, "Cash Register No Must not be null");
		Assert.notNull(transNo, "Transaction No Must not be null");
		final List<OrderModel> orders = getOshOrderDao().getAllOrders();
		Long size;

		if (orders.isEmpty())
		{
			size = 0 + transNo;
			transNo = transNo + 0;
		}
		else
		{
			size = orders.size() + transNo;
		}
		if (size >= 9999)
		{
			transNo = size % 9999;
			final Long rem = size / 9999;
			cashRegisterNo = cashRegisterNo + rem;
		}
		else
		{
			transNo = Long.valueOf(transNo + size);
		}
		cart.setCashRegisterNo(cashRegisterNo.toString());
		cart.setTransNo(transNo.toString());
		modelService.save(cart);
		modelService.refresh(cart);
		return true;
	}

	/**
	 * @return the oshOrderDao
	 */
	public OshOrderDao getOshOrderDao()
	{
		return oshOrderDao;
	}

	/**
	 * @param oshOrderDao
	 *           the oshOrderDao to set
	 */
	public void setOshOrderDao(final OshOrderDao oshOrderDao)
	{
		this.oshOrderDao = oshOrderDao;
	}



	public double getTotalAutherizedAmount(final OrderModel source)
	{
		double authTotal = 0.0d;
		authTotal = source.getTotalPrice() + source.getTotalTax();
		return authTotal;
	}


	@Override
	public double getProductsDiscountsAmount(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		final long totalQty = 0L;
		double unitDisc = 0.0d;
		final List<AbstractOrderEntryModel> entries = source.getEntries();
		if (!entries.isEmpty())
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				//	totalQty = totalQty + entry.getQuantity().longValue();

				final ConsignmentEntryModel coEntry = entry.getConsignmentEntries().iterator().next();
				if (entry.getDiscountPrice() != null && coEntry.getEntryLevelStatus() != ConsignmentEntryStatus.CANCELLED
						&& coEntry.getEntryLevelStatus() != ConsignmentEntryStatus.PENDING && !coEntry.isTlogPerformed())
				{
					unitDisc = entry.getBasePrice().doubleValue() - entry.getDiscountPrice().doubleValue();
					discounts = discounts + (unitDisc * entry.getQuantity().longValue());
				}
				/*
				 * final List<DiscountValue> discountValues = entry.getDiscountValues(); if (discountValues != null) { for
				 * (final DiscountValue dValue : discountValues) { discounts += dValue.getAppliedValue(); } }
				 */
			}
			if (discounts != 0.0)
			{
				final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
				unitDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), discounts).doubleValue();
				//unitShipping = source.getDeliveryCost().doubleValue() / totalQty;
			}
		}
		final Set<PromotionResultModel> promotions = source.getAllPromotionResults();
		for (final PromotionResultModel promotionResultModel : promotions)
		{
			if (promotionResultModel.getPromotion() instanceof OshTaxPromotionModel)
			{
				isTaxPromo = true;
			}
		}
		return discounts;
	}

	@Override
	public List<String> createAllSKURecords(final ConsignmentModel consignment, final List<String> poStatus)
	{
		Assert.notNull(consignment, "Consignment Must Not Be Null");
		final List<String> skuRecords = new ArrayList<String>();
		final Set<ConsignmentEntryModel> consignmentEntry = consignment.getConsignmentEntries();
		for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntry)
		{
			final ConsignmentEntryStatus entryStatus = consignmentEntryModel.getEntryLevelStatus();

			if (!poStatus.contains(entryStatus.getCode()) && (!consignmentEntryModel.isTlogPerformed()))
			{
				skuRecords.add(createSKURecords(consignmentEntryModel));
				calculateShippingCharges(consignmentEntryModel);
				LOG.info("CREATING SKU ENTRY RECORD FOR " + consignmentEntryModel.getOrderEntry().getProduct().getCode());
			}
		}
		return skuRecords;
	}

	public void calculateShippingCharges(final ConsignmentEntryModel consignmentEntryModel)
	{
		final ProductModel product = consignmentEntryModel.getOrderEntry().getProduct();
		final ConsignmentModel consignment = consignmentEntryModel.getConsignment();
		final AbstractOrderModel order = consignment.getOrder();
		final int size = order.getConsignments().size();
		if (!product.getCode().startsWith("8") && consignment.getCode().contains(OshCoreConstants.ONLINE))
		{
			unitShipping = order.getDeliveryCost().toString();
		}
		if (product.getCode().startsWith("8") && consignment.getCode().contains(OshCoreConstants.DROPSHIP))
		{
			if (size == 1)
			{
				if (consignment.getStatus().getCode().equals(ConsignmentStatus.PARTIAL_FULFILLED.getCode())
						|| consignment.getStatus().getCode().equals(ConsignmentStatus.SHIPPED.getCode()))
				{
					unitShipping = order.getDeliveryCost().toString();
				}

				final PaymentTransactionEntryModel transactionEntry = consignmentEntryModel.getTransactionModel();
				if (transactionEntry.getType() == PaymentTransactionType.CAPTURE && transactionEntry.getAmount() != null)
				{
					unitShipping = order.getDeliveryCost().toString();
				}
			}
		}

	}

	@Override
	public Map<String, Object> createEligibleReturnEntries(final List<ReturnRequestModel> reurnRequests)
	{
		final List<ReturnEntryModel> returnOrderEntry = new ArrayList<ReturnEntryModel>();
		final Map<String, Object> returnEntryInfo = new HashMap<String, Object>();
		long returnTotalQty = 0L;
		double unitDisc = 0.0d;
		double discounts = 0.0d;
		for (final ReturnRequestModel returnRequestModel : reurnRequests)
		{
			if (!returnRequestModel.isTlogPerformed())
			{
				for (final ReturnEntryModel returnEntryModel : returnRequestModel.getReturnEntries())
				{
					if (returnEntryModel.getStatus() == ReturnStatus.RECEIVED)
					{
						returnOrderEntry.add(returnEntryModel);
						final AbstractOrderEntryModel orderEntry = returnEntryModel.getOrderEntry();

						//return amount need to remove
						final long qty = returnEntryModel.getExpectedQuantity().longValue();
						if (orderEntry.getDiscountPrice() != null)
						{
							unitDisc = orderEntry.getBasePrice().doubleValue() - orderEntry.getDiscountPrice().doubleValue();
							discounts = discounts + (unitDisc * qty);
						}
						final double amount = qty * orderEntry.getBasePrice().longValue();
						totalAmount = totalAmount + amount;
						final double tax = orderEntry.getTaxAmount().doubleValue();
						if (tax != 0.0)
						{

							totalReturnTax = totalReturnTax + (orderEntry.getTaxPerUnit().doubleValue() * qty);
						}
						returnTotalQty = returnTotalQty + qty;
					}
				}
			}

		}
		if (!returnOrderEntry.isEmpty())
		{
			//returnDiscount = discounts;
			final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
			returnDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), discounts).doubleValue();
			returnEntryInfo.put("returnOrderEntries", returnOrderEntry);
			orderQty = returnTotalQty;
		}
		return returnEntryInfo;
	}

	@Override
	public void calculateOrderAmount(final OrderModel order)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		for (final AbstractOrderEntryModel entry : entries)
		{
			orderQty = orderQty + entry.getQuantity().longValue();
			final ConsignmentEntryModel coEntry = entry.getConsignmentEntries().iterator().next();
			if (entry.getTaxPerUnit() != null && coEntry.getEntryLevelStatus() != ConsignmentEntryStatus.CANCELLED
					&& coEntry.getEntryLevelStatus() != ConsignmentEntryStatus.PENDING && !coEntry.isTlogPerformed())
			{
				totalTax = totalTax + (entry.getTaxPerUnit().doubleValue() * entry.getQuantity().longValue());
			}
		}
	}

	public void setTotalCapturedAmount(final OrderModel order)
	{
		final PaymentTransactionModel paymentTransactionModel = order.getPaymentTransactions().iterator().next();
		final List<PaymentTransactionEntryModel> paymentEntries = paymentTransactionModel.getEntries();

		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			final ConsignmentEntryModel entryModel = entry.getConsignmentEntries().iterator().next();
			if (entryModel.isTransactionDone())
			{
				final PaymentTransactionEntryModel payment = entryModel.getTransactionModel();
				final String code = payment.getCode();
			}
		}
		for (final PaymentTransactionEntryModel entry : paymentEntries)
		{
			if (entry.getType() == PaymentTransactionType.CAPTURE && entry.getAmount() != null)
			{

				totalAmount = totalAmount + entry.getAmount().doubleValue();
			}
		}

	}

	@Override
	public double getLastTransactionAmount(final OrderModel order, final Date triggerDate)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		PaymentTransactionModel paymentTransaction = null;
		if (order.getPaymentTransactions() != null && !(order.getPaymentTransactions().isEmpty()))
		{
			paymentTransaction = order.getPaymentTransactions().get(0);
		}
		List<PaymentTransactionEntryModel> transactionEntries = null;
		double total = 0.0d;
		if (orderType.equals("SALE"))
		{
			transactionEntries = oshOrderDao.getCapturedTransactionEntries(paymentTransaction.getPk(), triggerDate);
		}
		if (orderType.equals("RETURN"))
		{
			transactionEntries = oshOrderDao.getReturnedTransactionEntries(paymentTransaction.getPk(), triggerDate);
		}
		for (final PaymentTransactionEntryModel transactionEntry : transactionEntries)
		{
			if (transactionEntry.getAmount() != null)
			{
				total = total + transactionEntry.getAmount().doubleValue();
			}
		}
		orderTNDTotal = total;

		return orderTNDTotal;
	}

	public double nstAmountCalculation(final Map<String, Object> retunInfo)
	{
		if (retunInfo != null && !retunInfo.isEmpty())
		{
			if (isTaxPromo)
			{
				return totalReturnTax;
			}
			else
			{
				return 0.0;
			}

		}
		else
		{
			return totalTax;
		}

	}

	public void resetReturnValues()
	{
		totalAmount = 0.0;
		returnDiscount = 0.0;
		totalTax = 0.0;
		orderQty = 0L;
		unitDiscount = 0.0;
		unitShipping = null;
		totalReturnTax = 0.0;
		isTaxPromo = false;
	}

	protected double roundOff(final Double test2)
	{

		final DecimalFormat df = new DecimalFormat(".000");
		final String test1 = df.format(test2);
		if (test1 != null)
		{
			String str = test1.toString();
			if (!str.equals(".") && str.contains("."))
			{

				//final int subStr = str.indexOf(".");


				str = str.substring(str.indexOf(".") + 3);
				final int dec = Integer.valueOf(str);
				return roundOffBasedOnDecimal(dec, test1);

			}

		}
		return Double.valueOf(test1);
	}

	protected double roundOffBasedOnDecimal(final int dec, final String test)
	{
		double roundedOff = 0.0;
		if (dec > 5)
		{
			roundedOff = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), Double.valueOf(test)).doubleValue();
			return roundedOff;
		}
		else
		{
			String str = test.toString();
			str = str.substring(0, str.indexOf(".") + 3);
			return Double.valueOf(str);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.PopulateRecordTypesService#createHDRRecordsForCredit(de.hybris.platform.core.model
	 * .order.OrderModel)
	 */
	@Override
	public String createHDRRecordForCreditOrder(final OrderModel order)
	{
		Assert.notNull(order, "Parameter Cannot Be NULL");
		isEmptyReturnOrder = order.getReturnRequests().isEmpty();
		if (!isEmptyReturnOrder)
		{
			if (retunInfo != null)
			{
				isReturnInfoEmpty = retunInfo.isEmpty();
				if (isReturnInfoEmpty)
				{
					isShipped = true;
				}
				else
				{
					isShipped = false;
				}
			}
			else
			{
				isShipped = true;
			}
			if (!isEmptyReturnOrder)
			{
				setTotalCapturedAmount(order);
			}
		}
		else
		{
			isShipped = true;
		}

		String storeId = null;
		if (order.getStatus() == OrderStatus.PARTIAL_FULFILLED || order.getStatus() == OrderStatus.COMPLETED)
		{
			isPartialFulfilled = true;
		}
		recordData = new StringBuilder();
		recordData.append("HDR|");

		if (order.getStoreID() != null && !order.getStoreID().isEmpty())
		{
			storeId = order.getStoreID();
			recordData.append(order.getStoreID());
		}
		else
		{
			storeId = "0799";
			recordData.append("0799");
		}
		recordData.append("|" + order.getCashRegisterNo());
		recordData.append("|" + order.getTransNo());
		recordData.append("|");
		final Date date = order.getCreationtime();
		//if (isEmptyReturnOrder || isReturnInfoEmpty)
		orderType = "CREDIT";
		recordData.append("|CREDIT");

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final SimpleDateFormat registerDate = new SimpleDateFormat("MMddyyyy");
		/*
		 * final String timeZone = configurationService.getConfiguration().getString(TIME_ZONE); if (timeZone != null &&
		 * !timeZone.isEmpty()) { sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		 * registerDate.setTimeZone(TimeZone.getTimeZone(timeZone)); }
		 */

		if (!isEmptyReturnOrder && !isReturnInfoEmpty)
		{
			if (order.getReturnTransactionDate() != null)
			{
				recordData.append("|" + sdf.format(order.getReturnTransactionDate()));
			}
			else
			{
				recordData.append("|" + sdf.format(date));
			}
		}
		else
		{
			recordData.append("|" + sdf.format(date));
		}



		if (!isEmptyReturnOrder && !isReturnInfoEmpty)
		{
			recordData.append("|" + order.getCode());
			recordData.append("|" + sdf.format(order.getCreationtime()) + "|");
		}
		else
		{
			recordData.append("|||");
		}
		String transactionDate = null;
		if (order.getTransactionDate() == null)
		{
			transactionDate = registerDate.format(date);
			order.setTransactionDate(transactionDate);
		}
		else
		{
			transactionDate = order.getTransactionDate();
		}
		final String transactionNo = String.format("%04d", Long.valueOf(order.getTransNo()));
		final String formattedStoreId = String.format("%04d", Long.valueOf(storeId));
		final String registerTransNo = formattedStoreId + order.getCashRegisterNo() + transactionNo + transactionDate + "N";

		if (isEmptyReturnOrder)
		{
			recordData.append(registerTransNo);
		}
		recordData.append("|" + order.getCode() + "|");
		if (!isEmptyReturnOrder && !isReturnInfoEmpty)
		{
			recordData.append(registerTransNo);
		}
		recordData.append("|N");
		recordData.append("|");
		if (order.getLoyaltyVoucher() != null && !order.getLoyaltyVoucher().isEmpty())
		{
			recordData.append(order.getLoyaltyVoucher());
		}
		if (order.getGiftMessage() == null)
		{
			recordData.append("||");
		}
		else
		{
			final String giftMessage = order.getGiftMessage().replaceAll("[\r\n]+", " ");
			recordData.append("|" + giftMessage + "|");
		}


		/*
		 * if (isTaxPromo) { recordData.append(0.0); }
		 * 
		 * else
		 * 
		 * { recordData.append(order.getTotalDiscounts()); }
		 */

		recordData.append("|");
		final CustomerModel customer = (CustomerModel) order.getUser();
		if (customer.getLoyaltyNumber() != null)
		{
			recordData.append(customer.getLoyaltyNumber());
		}

		recordData.append("|");

		final List<CreditPaymentTransactionModel> trans = (List<CreditPaymentTransactionModel>) order.getCreditPaymentTransaction();
		if (!trans.isEmpty())
		{
			double shippingCreditAmount = 0.0;
			double otherCreditAmount = 0.0;
			//final CreditPaymentTransactionModel transEntries = trans.get(trans.size() - 1);

			for (final CreditPaymentTransactionModel transEntry : trans)
			{
				if (transEntry.getOshShippingType() != null && !transEntry.isTlogPerformed()
						&& transEntry.getTransactionStatus() != null
						&& transEntry.getTransactionStatus().toString().equals(OshCoreConstants.ACCEPTED))
				{
					if (transEntry.getOshShippingType().toString().equals(OshCoreConstants.SHIPPING))
					{
						shippingCreditAmount += transEntry.getCreditAmount();
					}

					if (transEntry.getOshShippingType().toString().equals(OshCoreConstants.OTHER))
					{
						otherCreditAmount += transEntry.getCreditAmount();
					}

					transEntry.setTlogPerformed(true);
					modelService.save(transEntry);
					modelService.refresh(transEntry);
				}
			}


			//18th field
			recordData.append("|" + shippingCreditAmount);

			// entry in 19th field
			recordData.append("|" + otherCreditAmount);
		}


		recordData.append("|" + createEmptyFields(21, 50));
		totalTransactions++;
		order.setCreditTlog(false);
		LOG.info("HDR RECORD FOR ORDER " + order.getCode());
		LOG.info(recordData.toString());
		modelService.save(order);
		modelService.refresh(order);
		return recordData.toString();
	}

}