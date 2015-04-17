package com.hybris.osh.payment.services.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.request.CaptureRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.DeleteSubscriptionRequest;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.commands.request.SubscriptionDataRequest;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.commands.result.SubscriptionDataResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.commands.result.VoidResult;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.payment.methods.CardPaymentService;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.payment.strategy.PaymentInfoCreatorStrategy;
import de.hybris.platform.payment.strategy.TransactionCodeGenerator;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hybris.osh.command.request.FullReversalRequest;
import com.hybris.osh.command.result.FullReversalResult;
import com.hybris.osh.core.model.CreditPaymentTransactionModel;
import com.hybris.osh.payment.service.OshCardPaymentService;
import com.hybris.osh.payment.services.OshPaymentService;





public class DefaultOshPaymentService extends DefaultPaymentServiceImpl implements OshPaymentService
{

	private CardPaymentService cardPaymentService;
	private CommonI18NService commonI18NService;
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	private TransactionCodeGenerator merchantTransactionCode;
	private PaymentInfoCreatorStrategy paymentInfoCreator;
	private OshCardPaymentService oshCardPaymentService;

	@Override
	public PaymentInfoCreatorStrategy getPaymentInfoCreator()
	{
		return paymentInfoCreator;
	}

	@Override
	public void setPaymentInfoCreator(final PaymentInfoCreatorStrategy paymentInfoCreator)
	{
		this.paymentInfoCreator = paymentInfoCreator;
	}

	@Override
	public TransactionCodeGenerator getTransactionCodeGenerator()
	{
		return merchantTransactionCode;
	}

	@Override
	public void setTransactionCodeGenerator(final TransactionCodeGenerator merchantTransactionCode)
	{
		this.merchantTransactionCode = merchantTransactionCode;
	}

	@Override
	public void setCardPaymentService(final CardPaymentService cardPaymentService)
	{
		this.cardPaymentService = cardPaymentService;
	}

	@Override
	public CardPaymentService getCardPaymentService()
	{
		return cardPaymentService;
	}

	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public PaymentTransactionEntryModel authorize(final String merchantTransactionCode, final BigDecimal amount,
			final Currency currency, final AddressModel deliveryAddress, final String subscriptionID)
	{
		final BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setCode(merchantTransactionCode);
		return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, null, null);
	}

	@Override
	public PaymentTransactionEntryModel authorize(final String merchantTransactionCode, final BigDecimal amount,
			final Currency currency, final AddressModel deliveryAddress, final String subscriptionID, final String cv2,
			final String paymentProvider) throws AdapterException
	{
		final BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setCode(merchantTransactionCode);
		return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, cv2, paymentProvider);
	}

	@Override
	public PaymentTransactionEntryModel authorize(final String merchantTransactionCode, final BigDecimal amount,
			final Currency currency, final AddressModel deliveryAddress, final AddressModel paymentAddress, final CardInfo card)
	{
		final BillingInfo shippingInfo = createBillingInfo(deliveryAddress, paymentAddress, card);
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setCode(merchantTransactionCode);
		return authorizeInternal(transaction, amount, currency, shippingInfo, card, null, null, null);
	}

	@Override
	public PaymentTransactionEntryModel authorize(final PaymentTransactionModel transaction, final BigDecimal amount,
			final Currency currency, final AddressModel deliveryAddress, final AddressModel paymentAddress, final CardInfo card)
			throws AdapterException
	{
		final BillingInfo shippingInfo = createBillingInfo(deliveryAddress, paymentAddress, card);
		return authorizeInternal(transaction, amount, currency, shippingInfo, card, null, null, null);
	}

	@Override
	public PaymentTransactionEntryModel authorize(final PaymentTransactionModel transaction, final BigDecimal amount,
			final Currency currency, final AddressModel deliveryAddress, final String subscriptionID, final String paymentprovider)
			throws AdapterException
	{
		final BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
		return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, null, paymentprovider);
	}

	@Override
	public PaymentTransactionEntryModel authorize(final PaymentTransactionModel transaction, final BigDecimal amount,
			final Currency currency, final AddressModel deliveryAddress, final String subscriptionID) throws AdapterException
	{
		final BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
		return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, null, null);
	}

	private PaymentTransactionEntryModel authorizeInternal(final PaymentTransactionModel transaction, final BigDecimal amount,
			final Currency currency, final BillingInfo shippingInfo, final CardInfo card, final String subscriptionID,
			final String cv2, final String paymentProvider) throws AdapterException
	{
		final String newEntryCode = getNewEntryCode(transaction);
		AuthorizationResult result;
		if (subscriptionID == null)
		{
			result = oshCardPaymentService.authorize(new AuthorizationRequest(transaction.getCode(), card, currency, amount,
					shippingInfo));
		}
		else
		{
			/**
			 * modified for payment transaction in cs cockpit
			 */
			result = oshCardPaymentService.authorize(new SubscriptionAuthorizationRequest(transaction.getCode(), subscriptionID,
					currency, amount, shippingInfo, cv2, transaction.getPaymentProvider()!=null?transaction.getPaymentProvider():paymentProvider));
		}
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		transaction.setPaymentProvider(result.getPaymentProvider());
		modelService.save(transaction);
		//made changes for partial cancel order
		//	final PaymentTransactionEntryModel entry = transaction.getEntries().iterator().next();
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		entry.setAmount(result.getTotalAmount());
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.AUTHORIZATION);
		entry.setTime(result.getAuthorizationTime() != null ? result.getAuthorizationTime() : new Date());
		entry.setPaymentTransaction(transaction);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
			
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(newEntryCode);
		if (subscriptionID != null)
		{
			entry.setSubscriptionID(subscriptionID);
		}
		modelService.save(entry);
		modelService.refresh(transaction);
		return entry;
	}

	@Override
	protected String getNewEntryCode(final PaymentTransactionModel transaction)
	{
		if (transaction.getEntries() == null)
		{
			return (new StringBuilder(String.valueOf(transaction.getCode()))).toString();
		}
		else
		{
			return (new StringBuilder(String.valueOf(transaction.getCode()))).append("-")
					.append(transaction.getEntries().size() + Math.random()).toString();
		}
	}

	private BillingInfo createBillingInfo(final AddressModel deliveryAddress, final AddressModel paymentAddress,
			final CardInfo card)
	{
		if (card != null && card.getBillingInfo() == null && paymentAddress != null)
		{
			final BillingInfo billingInfo = new BillingInfo();
			billingInfo.setCity(paymentAddress.getTown());
			if (paymentAddress.getCountry() != null)
			{
				billingInfo.setCountry(paymentAddress.getCountry().getIsocode());
			}
			billingInfo.setEmail(paymentAddress.getEmail());
			billingInfo.setFirstName(paymentAddress.getFirstname());
			billingInfo.setLastName(paymentAddress.getLastname());
			billingInfo.setPhoneNumber(paymentAddress.getPhone1());
			billingInfo.setPostalCode(paymentAddress.getPostalcode());
			if (paymentAddress.getRegion() != null)
			{
				billingInfo.setState(paymentAddress.getRegion().getName());
			}
			billingInfo.setStreet1(paymentAddress.getStreetname());
			billingInfo.setStreet2(paymentAddress.getStreetnumber());
			card.setBillingInfo(billingInfo);
		}
		BillingInfo shippingInfo = null;
		if (deliveryAddress == null)
		{
			if (card != null)
			{
				shippingInfo = card.getBillingInfo();
			}
		}
		else
		{
			shippingInfo = new BillingInfo();
			shippingInfo.setCity(deliveryAddress.getTown());
			if (deliveryAddress.getCountry() != null)
			{
				shippingInfo.setCountry(deliveryAddress.getCountry().getIsocode());
			}
			shippingInfo.setEmail(deliveryAddress.getEmail());
			shippingInfo.setFirstName(deliveryAddress.getFirstname());
			shippingInfo.setLastName(deliveryAddress.getLastname());
			shippingInfo.setPhoneNumber(deliveryAddress.getPhone1());
			shippingInfo.setPostalCode(deliveryAddress.getPostalcode());
			if (deliveryAddress.getRegion() != null)
			{
				shippingInfo.setState(deliveryAddress.getRegion().getName());
			}
			shippingInfo.setStreet1(deliveryAddress.getStreetname());
			shippingInfo.setStreet2(deliveryAddress.getStreetnumber());
		}
		return shippingInfo;
	}

	private BillingInfo createBillingInfo(final AddressModel address)
	{
		if (address == null)
		{
			return null;
		}
		final BillingInfo billingInfo = new BillingInfo();
		billingInfo.setCity(address.getTown());
		if (address.getCountry() != null)
		{
			billingInfo.setCountry(address.getCountry().getIsocode());
		}
		billingInfo.setEmail(address.getEmail());
		billingInfo.setFirstName(address.getFirstname());
		billingInfo.setLastName(address.getLastname());
		billingInfo.setPhoneNumber(address.getPhone1());
		billingInfo.setPostalCode(address.getPostalcode());
		if (address.getRegion() != null)
		{
			billingInfo.setState(address.getRegion().getName());
		}
		billingInfo.setStreet1(address.getStreetname());
		billingInfo.setStreet2(address.getStreetnumber());
		return billingInfo;
	}

	/**
	 * @deprecated Method authorize is deprecated
	 */

	@Override
	@Deprecated
	public PaymentTransactionEntryModel authorize(final OrderModel order, final CardInfo card) throws AdapterException
	{
		final String mtc = merchantTransactionCode.generateCode(order.getCode());
		final PaymentTransactionEntryModel transactionEntry = authorize(mtc, new BigDecimal(order.getTotalPrice().doubleValue()),
				Currency.getInstance(order.getCurrency().getIsocode()), order.getDeliveryAddress(), order.getPaymentAddress(), card);
		transactionEntry.getPaymentTransaction().setOrder(order);
		return transactionEntry;
	}

	@Override
	public PaymentTransactionEntryModel capture(final PaymentTransactionModel transaction) throws AdapterException
	{
		PaymentTransactionEntryModel auth = null;
		for (final Iterator iterator = transaction.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
			{
				auth = pte;
				break;
			}
		}

		if (auth == null)
		{
			throw new AdapterException("Could not capture without authorization");
		}
		final String newEntryCode = getNewEntryCode(transaction);
		final CaptureResult result = oshCardPaymentService.capture(new CaptureRequest(transaction.getCode(), transaction
				.getRequestId(), transaction.getRequestToken(), Currency.getInstance(auth.getCurrency().getIsocode()), auth
				.getAmount(), transaction.getPaymentProvider()));
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		entry.setAmount(result.getTotalAmount());
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.CAPTURE);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(transaction);
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(newEntryCode);
		modelService.save(entry);
		return entry;
	}

	
	public PaymentTransactionEntryModel refundCreditAmount(final PaymentTransactionModel paymentTransactionModel) throws AdapterException
	{
		final String newEntryCode = getNewEntryCode(paymentTransactionModel);

		List<CreditPaymentTransactionModel> ListCreditPaymentTransactionModel=(List)paymentTransactionModel.getOrder().getCreditPaymentTransaction();
		CreditPaymentTransactionModel creditPaymentTransactionModel=ListCreditPaymentTransactionModel.get(ListCreditPaymentTransactionModel.size()-1);
		final Double refundAmount = creditPaymentTransactionModel.getCreditAmount();
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		PaymentTransactionEntryModel authEntry = null;
		for(PaymentTransactionEntryModel pte : paymentTransactionModel.getEntries() )
		{
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION)&& pte.getTransactionStatus().equals("ACCEPTED"))
			{
				authEntry = pte;
			}

		}
		StandaloneRefundRequest request=new StandaloneRefundRequest(paymentTransactionModel.getCode(), authEntry.getSubscriptionID(), createBillingInfo(paymentTransactionModel.getOrder().getDeliveryAddress()), createCCDetails(paymentTransactionModel.getOrder().getPaymentInfo()), Currency
				.getInstance(commonI18NService.getCurrency("USD").getIsocode()), BigDecimal.valueOf(refundAmount));
	
		try{
		final RefundResult result = oshCardPaymentService.oshRefundStandalone(request,paymentTransactionModel.getPaymentProvider());
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.REFUND_STANDALONE);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(paymentTransactionModel);
		entry.setAmount( BigDecimal.valueOf(refundAmount));
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().equals(TransactionStatusDetails.SUCCESFULL)){
			entry.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(newEntryCode);
		}
		catch(Exception e)
		{
			/*if (result.getCurrency() != null)
			{
				entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
			}*/
			entry.setType(PaymentTransactionType.REFUND_STANDALONE);
			entry.setTime(new Date());
			entry.setPaymentTransaction(paymentTransactionModel);
			entry.setAmount( BigDecimal.valueOf(refundAmount));
			entry.setRequestId(authEntry.getRequestId());
			entry.setRequestToken(authEntry.getRequestToken());
			entry.setTransactionStatus(TransactionStatus.REJECTED.toString());
			entry.setTransactionStatusDetails(TransactionStatus.REJECTED.toString());
			entry.setCode(newEntryCode);
		}
		
		modelService.save(entry);
		modelService.refresh(entry);

	return entry;
	}
	
	
	

	@Override
	public PaymentTransactionEntryModel cancel(final PaymentTransactionEntryModel transaction) throws AdapterException
	{
		final VoidResult result = oshCardPaymentService.voidCreditOrCapture(new VoidRequest(transaction.getPaymentTransaction().getCode(), transaction
				.getRequestId(), transaction.getRequestToken(), transaction.getPaymentTransaction().getPaymentProvider()));
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.CANCEL);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(transaction.getPaymentTransaction());
		entry.setRequestId(result.getRequestId());
		entry.setAmount(result.getAmount());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(getNewEntryCode(transaction.getPaymentTransaction()));
		modelService.save(entry);
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel refundFollowOn(final PaymentTransactionModel transaction, final BigDecimal amount)
			throws AdapterException
	{
		PaymentTransactionEntryModel auth = null;
		for (final Iterator iterator = transaction.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.PARTIAL_CAPTURE))
			{
				auth = pte;
				break;
			}
		}

		if (auth == null)
		{
			throw new AdapterException("Could not refund follow-on without authorization");
		}
		final String newEntryCode = getNewEntryCode(transaction);
		final RefundResult result = oshCardPaymentService.refundFollowOn(new FollowOnRefundRequest(transaction.getCode(), auth
				.getRequestId(), auth.getRequestToken(), Currency.getInstance(auth.getCurrency().getIsocode()), amount, transaction
				.getPaymentProvider()));
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.REFUND_FOLLOW_ON);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(transaction);
		entry.setAmount(result.getTotalAmount());
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(newEntryCode);
		final List<PaymentTransactionEntryModel> entries = new ArrayList<PaymentTransactionEntryModel>(transaction.getEntries());
		entries.add(entry);
		transaction.setEntries(entries);
		modelService.save(transaction);
		modelService.save(entry);
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel partialRefundFollowOn(final ConsignmentEntryModel consignmentEntry,final BigDecimal amount)
			throws AdapterException
	{
		final PaymentTransactionEntryModel paymentTransactionEntryModel = consignmentEntry.getTransactionModel();
		if (paymentTransactionEntryModel == null)
		{
			throw new AdapterException("Could not refund follow-on without authorization");
		}
		final PaymentTransactionModel paymentTransactionModel = paymentTransactionEntryModel.getPaymentTransaction();
		final String newEntryCode = getNewEntryCode(paymentTransactionModel);
		final RefundResult result = oshCardPaymentService.refundFollowOn(new FollowOnRefundRequest(paymentTransactionModel.getCode(),
				paymentTransactionEntryModel.getRequestId(), paymentTransactionEntryModel.getRequestToken(), Currency
						.getInstance(paymentTransactionEntryModel.getCurrency().getIsocode()), amount, paymentTransactionModel
						.getPaymentProvider()));
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.REFUND_FOLLOW_ON);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(paymentTransactionModel);
		entry.setAmount(result.getTotalAmount());
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(newEntryCode);

		modelService.save(entry);
		return entry;
	}

	/**
	 * @deprecated Method refundStandalone is deprecated
	 */

	@Override
	@Deprecated
	public PaymentTransactionEntryModel refundStandalone(final StandaloneRefundRequest request) throws AdapterException
	{
		final RefundResult result = oshCardPaymentService.refundStandalone(request);
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		transaction.setPaymentProvider(result.getPaymentProvider());
		modelService.save(transaction);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.REFUND_STANDALONE);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(transaction);
		entry.setAmount(result.getTotalAmount());
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setCode(getNewEntryCode(transaction));
		modelService.save(entry);
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel refundStandalone(final String merchantTransactionCode, final BigDecimal amount,
			final Currency currency, final AddressModel paymentAddress, final CardInfo card) throws AdapterException
	{
		final BillingInfo billTo = createBillingInfo(paymentAddress);
		final StandaloneRefundRequest request = new StandaloneRefundRequest(merchantTransactionCode, billTo, card, currency, amount);
		final RefundResult result = oshCardPaymentService.refundStandalone(request);
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		transaction.setPaymentProvider(result.getPaymentProvider());
		modelService.save(transaction);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.REFUND_STANDALONE);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(transaction);
		entry.setAmount(result.getTotalAmount());
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setCode(getNewEntryCode(transaction));
		modelService.save(entry);
		return entry;
	}




	@Override
	public PaymentTransactionEntryModel partialCapture(final PaymentTransactionModel transaction, final BigDecimal amount)
			throws AdapterException
	{
		PaymentTransactionEntryModel auth = null;
		int capturesSize = 1;
		for (final Iterator iterator = transaction.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
			{
				auth = pte;
			}
			else if (pte.getType().equals(PaymentTransactionType.PARTIAL_CAPTURE))
			{
				capturesSize++;
			}
		}

		if (auth == null)
		{
			throw new AdapterException("Could not capture partially without authorization");
		}
		final String newEntryCode = getNewEntryCode(transaction);
		//auth.getPaymentTransaction();

		final CaptureResult result = oshCardPaymentService.partialCapture(new PartialCaptureRequest(transaction.getCode(), transaction
				.getRequestId(), transaction.getRequestToken(), Currency.getInstance(auth.getCurrency().getIsocode()), amount,
				Integer.toString(capturesSize), transaction.getPaymentProvider()));
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		entry.setAmount(result.getTotalAmount());
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(PaymentTransactionType.PARTIAL_CAPTURE);
		entry.setTime(result.getRequestTime() != null ? result.getRequestTime() : new Date());
		entry.setPaymentTransaction(transaction);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(newEntryCode);
		modelService.save(entry);
		return entry;
	}

	@Override
	public PaymentTransactionModel getPaymentTransaction(final String code)
	{
		final PaymentTransactionModel example = new PaymentTransactionModel();
		example.setCode(code);
		return flexibleSearchService.getModelByExample(example);
	}

	@Override
	public PaymentTransactionEntryModel getPaymentTransactionEntry(final String code)
	{
		final PaymentTransactionEntryModel example = new PaymentTransactionEntryModel();
		example.setCode(code);
		return flexibleSearchService.getModelByExample(example);
	}

	@Override
	public void attachPaymentInfo(final PaymentTransactionModel paymentTransactionModel, final UserModel userModel,
			final CardInfo cardInfo, final BigDecimal amount)
	{
		getPaymentInfoCreator().attachPaymentInfo(paymentTransactionModel, userModel, cardInfo, amount);
	}

	@Override
	public NewSubscription createSubscription(final PaymentTransactionModel transaction, final AddressModel paymentAddress,
			final CardInfo card) throws AdapterException
	{
		PaymentTransactionEntryModel auth = null;
		for (final Iterator iterator = transaction.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
			{
				auth = pte;
				break;
			}
		}

		if (auth == null)
		{
			throw new AdapterException("Could not create a subscription without authorization");
		}
		else
		{
			final String newEntryCode = getNewEntryCode(transaction);
			final CreateSubscriptionRequest request = new CreateSubscriptionRequest(newEntryCode, createBillingInfo(paymentAddress),
					Currency.getInstance(auth.getCurrency().getIsocode()), card, transaction.getRequestId(),
					transaction.getRequestToken(), transaction.getPaymentProvider());
			final SubscriptionResult result = oshCardPaymentService.createSubscription(request);
			final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
					.create(PaymentTransactionEntryModel.class);
			entry.setType(PaymentTransactionType.CREATE_SUBSCRIPTION);
			entry.setRequestId(result.getRequestId());
			entry.setRequestToken(result.getRequestToken());
			entry.setTime(new Date());
			entry.setPaymentTransaction(transaction);
			entry.setTransactionStatus(result.getTransactionStatus().toString());
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
			entry.setCode(newEntryCode);
			modelService.save(entry);
			final NewSubscription newSubscription = new NewSubscription();
			newSubscription.setTransactionEntry(entry);
			newSubscription.setSubscriptionID(result.getSubscriptionID());
			return newSubscription;
		}
	}

	@Override
	public NewSubscription createSubscription(final String merchantTransactionCode, final String paymentProvider,
			final Currency currency, final AddressModel paymentAddress, final CardInfo card) throws AdapterException
	{
		final CreateSubscriptionRequest request = new CreateSubscriptionRequest(merchantTransactionCode,
				createBillingInfo(paymentAddress), currency, card, null, null, paymentProvider);
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setCode(merchantTransactionCode);
		final SubscriptionResult result = oshCardPaymentService.createSubscription(request);
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		transaction.setPaymentProvider(paymentProvider);
		modelService.save(transaction);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		entry.setType(PaymentTransactionType.CREATE_SUBSCRIPTION);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTime(new Date());
		entry.setPaymentTransaction(transaction);
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if (result.getTransactionStatusDetails() != null)
		{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setCode(getNewEntryCode(transaction));
		modelService.save(entry);
		final NewSubscription newSubscription = new NewSubscription();
		newSubscription.setTransactionEntry(entry);
		newSubscription.setSubscriptionID(result.getSubscriptionID());
		return newSubscription;
	}

	@Override
	public PaymentTransactionEntryModel updateSubscription(final String merchantTransactionCode, final String subscriptionID,
			final String paymentProvider, final AddressModel paymentAddress, final CardInfo card) throws AdapterException
	{
		if (paymentAddress == null && card == null)
		{
			return null;
		}
		else
		{
			final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
			transaction.setCode(merchantTransactionCode);
			final SubscriptionResult result = oshCardPaymentService.updateSubscription(new UpdateSubscriptionRequest(
					getTransactionCodeGenerator().generateCode(transaction.getCode()), subscriptionID, paymentProvider,
					createBillingInfo(paymentAddress), card));
			transaction.setRequestId(result.getRequestId());
			transaction.setRequestToken(result.getRequestToken());
			transaction.setPaymentProvider(paymentProvider);
			modelService.save(transaction);
			final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
					.create(PaymentTransactionEntryModel.class);
			entry.setType(PaymentTransactionType.UPDATE_SUBSCRIPTION);
			entry.setTime(new Date());
			entry.setPaymentTransaction(transaction);
			entry.setRequestId(result.getRequestId());
			entry.setRequestToken(result.getRequestToken());
			entry.setTransactionStatus(result.getTransactionStatus().toString());
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
			entry.setCode(getNewEntryCode(transaction));
			entry.setSubscriptionID(subscriptionID);
			modelService.save(entry);
			return entry;
		}
	}

	@Override
	public PaymentTransactionEntryModel getSubscriptionData(final String merchantTransactionCode, final String subscriptionID,
			final String paymentProvider, final BillingInfo billingInfo, final CardInfo card) throws AdapterException
	{
		if (billingInfo == null && card == null)
		{
			return null;
		}
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setCode(merchantTransactionCode);
		final SubscriptionDataResult result = oshCardPaymentService.getSubscriptionData(new SubscriptionDataRequest(
				getTransactionCodeGenerator().generateCode(transaction.getCode()), subscriptionID, paymentProvider));
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		transaction.setPaymentProvider(paymentProvider);
		modelService.save(transaction);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		entry.setType(PaymentTransactionType.GET_SUBSCRIPTION_DATA);
		entry.setTime(new Date());
		entry.setPaymentTransaction(transaction);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setCode(getNewEntryCode(transaction));
		modelService.save(entry);
		if (billingInfo != null)
		{
			billingInfo.copy(result.getBillingInfo());
		}
		if (card != null)
		{
			card.copy(result.getCard());
		}
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel deleteSubscription(final String merchantTransactionCode, final String subscriptionID,
			final String paymentProvider) throws AdapterException
	{
		final PaymentTransactionModel transaction = (PaymentTransactionModel) modelService.create(PaymentTransactionModel.class);
		transaction.setCode(merchantTransactionCode);
		final SubscriptionResult result = oshCardPaymentService.deleteSubscription(new DeleteSubscriptionRequest(
				getTransactionCodeGenerator().generateCode(transaction.getCode()), subscriptionID, paymentProvider));
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		transaction.setPaymentProvider(paymentProvider);
		modelService.save(transaction);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		entry.setType(PaymentTransactionType.DELETE_SUBSCRIPTION);
		entry.setTime(new Date());
		entry.setPaymentTransaction(transaction);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setCode(getNewEntryCode(transaction));
		entry.setSubscriptionID(subscriptionID);
		modelService.save(entry);
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel fullReverseAuth(final PaymentTransactionModel transaction,
			final String merchantTransactionCode, final String requestId, final Currency currency, final BigDecimal totalAmount,
			final String paymentProvider)
	{

		PaymentTransactionEntryModel auth = null;
		for (final Iterator iterator = transaction.getEntries().iterator(); iterator.hasNext();)
		{
			final PaymentTransactionEntryModel pte = (PaymentTransactionEntryModel) iterator.next();
			if (pte.getType().equals(PaymentTransactionType.AUTHORIZATION)&&  pte.getTransactionStatus().equals("ACCEPTED"))
			{
				auth = pte;
			}
		}
		if (auth == null)
		{
			throw new AdapterException("Could not capture without authorization");
		}
		final String newEntryCode = getNewEntryCode(transaction);

		transaction
				.setCode(merchantTransactionCode);
		final FullReversalResult result = oshCardPaymentService.FullReversal(new FullReversalRequest(merchantTransactionCode, auth
				.getRequestId(), auth.getAmount(), currency, paymentProvider));
		transaction.setRequestId(result.getRequestId());
		transaction.setRequestToken(result.getRequestToken());
		modelService.save(transaction);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) modelService
				.create(PaymentTransactionEntryModel.class);
		//final PaymentTransactionEntryModel entry = transaction.getEntries().iterator().next();
		entry.setPaymentTransaction(transaction);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(commonI18NService.getCurrency(result.getCurrency().getCurrencyCode()));
		}

		entry.setType(PaymentTransactionType.CANCEL);
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		if(result.getTransactionStatusDetails()!=null && result.getTransactionStatusDetails().name().equalsIgnoreCase("SUCCESFULL")){
			entry.setTransactionStatusDetails("SUCCESSFUL");
		}else{
			entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		}
		entry.setAmount(result.getAmount());
		entry.setCode(getNewEntryCode(transaction));
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTime(result.getRequestTime());
		final List<PaymentTransactionEntryModel> entries = new ArrayList<PaymentTransactionEntryModel>(transaction.getEntries());
		entries.add(entry);
		transaction.setEntries(entries);
		modelService.save(transaction);
		modelService.save(entry);
		return entry;

	}


	@Override
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the oshCardPaymentService
	 */
	public OshCardPaymentService getOshCardPaymentService()
	{
		return oshCardPaymentService;
	}

	/**
	 * @param oshCardPaymentService
	 *           the oshCardPaymentService to set
	 */
	public void setOshCardPaymentService(final OshCardPaymentService oshCardPaymentService)
	{
		this.oshCardPaymentService = oshCardPaymentService;
	}
	public CardInfo createCCDetails(final PaymentInfoModel paymentInfo) 
	{
		try{
		final CreditCardPaymentInfoModel ccPaymentinfoModel=(CreditCardPaymentInfoModel)paymentInfo;
		final CardInfo cardInfo = new CardInfo();

		cardInfo.setCardHolderFullName(ccPaymentinfoModel.getCcOwner());
		cardInfo.setCardNumber(ccPaymentinfoModel.getNumber());
		cardInfo.setCardType(ccPaymentinfoModel.getType());
		cardInfo.setExpirationMonth(Integer.valueOf(ccPaymentinfoModel.getValidToMonth()));
		cardInfo.setExpirationYear(Integer.valueOf(ccPaymentinfoModel.getValidToYear()));
		cardInfo.setIssueNumber(ccPaymentinfoModel.getIssueNumber()+"");
		return cardInfo;
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			
		}
		return null;
	
	}

}
