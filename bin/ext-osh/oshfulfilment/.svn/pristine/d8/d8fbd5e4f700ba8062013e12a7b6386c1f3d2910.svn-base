package com.hybris.osh.payment.services;



import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.math.BigDecimal;
import java.util.Currency;


/**
 *
 */
public interface OshPaymentService
{

	/**
	 * @deprecated Method authorize is deprecated
	 */

	@Deprecated
	public abstract PaymentTransactionEntryModel authorize(OrderModel ordermodel, CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel authorize(String s, BigDecimal bigdecimal, Currency currency,
			AddressModel addressmodel, String s1) throws AdapterException;

	public abstract PaymentTransactionEntryModel authorize(String s, BigDecimal bigdecimal, Currency currency,
			AddressModel addressmodel, String s1, String s2, String s3) throws AdapterException;

	public abstract PaymentTransactionEntryModel authorize(String s, BigDecimal bigdecimal, Currency currency,
			AddressModel addressmodel, AddressModel addressmodel1, CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel authorize(PaymentTransactionModel paymenttransactionmodel, BigDecimal bigdecimal,
			Currency currency, AddressModel addressmodel, String s, String s1) throws AdapterException;

	public abstract PaymentTransactionEntryModel authorize(PaymentTransactionModel paymenttransactionmodel, BigDecimal bigdecimal,
			Currency currency, AddressModel addressmodel, String s) throws AdapterException;

	public abstract PaymentTransactionEntryModel authorize(PaymentTransactionModel paymenttransactionmodel, BigDecimal bigdecimal,
			Currency currency, AddressModel addressmodel, AddressModel addressmodel1, CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel capture(PaymentTransactionModel paymenttransactionmodel) throws AdapterException;

	public abstract PaymentTransactionEntryModel refundCreditAmount(PaymentTransactionModel paymenttransactionmodel) throws AdapterException;

	public abstract PaymentTransactionEntryModel cancel(PaymentTransactionEntryModel paymenttransactionentrymodel)
			throws AdapterException;

	public abstract PaymentTransactionEntryModel refundFollowOn(PaymentTransactionModel paymenttransactionmodel,
			BigDecimal bigdecimal) throws AdapterException;
	
	public abstract PaymentTransactionEntryModel partialRefundFollowOn(ConsignmentEntryModel consignmentEntry,
			BigDecimal bigdecimal) throws AdapterException;


	/**
	 * @deprecated Method refundStandalone is deprecated
	 */

	@Deprecated
	public abstract PaymentTransactionEntryModel refundStandalone(StandaloneRefundRequest standalonerefundrequest)
			throws AdapterException;

	public abstract PaymentTransactionEntryModel refundStandalone(String s, BigDecimal bigdecimal, Currency currency,
			AddressModel addressmodel, CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel partialCapture(PaymentTransactionModel paymenttransactionmodel,
			BigDecimal bigdecimal) throws AdapterException;

	public abstract PaymentTransactionModel getPaymentTransaction(String s);

	public abstract PaymentTransactionEntryModel getPaymentTransactionEntry(String s);

	public abstract void attachPaymentInfo(PaymentTransactionModel paymenttransactionmodel, UserModel usermodel,
			CardInfo cardinfo, BigDecimal bigdecimal);

	public abstract NewSubscription createSubscription(PaymentTransactionModel paymenttransactionmodel, AddressModel addressmodel,
			CardInfo cardinfo) throws AdapterException;

	public abstract NewSubscription createSubscription(String s, String s1, Currency currency, AddressModel addressmodel,
			CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel updateSubscription(String s, String s1, String s2, AddressModel addressmodel,
			CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel getSubscriptionData(String s, String s1, String s2, BillingInfo billinginfo,
			CardInfo cardinfo) throws AdapterException;

	public abstract PaymentTransactionEntryModel deleteSubscription(String s, String s1, String s2) throws AdapterException;

	public PaymentTransactionEntryModel fullReverseAuth(PaymentTransactionModel paymenttransactionmodel,
			final String merchantTransactionCode, final String requestId,
			final Currency currency, final BigDecimal totalAmount, final String paymentProvider);
}