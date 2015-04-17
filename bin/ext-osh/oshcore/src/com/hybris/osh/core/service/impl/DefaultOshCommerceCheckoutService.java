/**
 * 
 */
package com.hybris.osh.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCheckoutService;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.OshOrderDao;
import com.hybris.osh.core.service.OshCommerceCheckoutService;
import com.hybris.osh.payment.services.OshPaymentService;


/**
 * 
 */
public class DefaultOshCommerceCheckoutService extends DefaultCommerceCheckoutService implements OshCommerceCheckoutService
{

	protected static final Logger LOG = Logger.getLogger(DefaultOshCommerceCheckoutService.class);

	@Autowired
	private UserService userService;
	@Autowired
	private OshPaymentService oshPaymentService;
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	@Resource(name = "oshOrderDao")
	private OshOrderDao oshOrderDao;
	private static final String ORDER_CONFIRMATION_BCC_EMAIL = "order.confirmation.notification.bcc.emailid";


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.OshCommerceCheckoutService#setStoreAddress(de.hybris.platform.core.model.order.CartModel
	 * , de.hybris.platform.core.model.user.AddressModel)
	 */
	@Override
	public void setStoreAddress(final CartModel cartModel)
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final PointOfServiceModel pos = customerModel.getMyStore();
		if (pos != null && pos.getAddress() != null)
		{
			cartModel.setStoreAddress(pos.getAddress());
			cartModel.setOrderType(pos.getName());
			cartModel.setStoreID(pos.getStoreID());
			getModelService().save(cartModel);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.OshCommerceCheckoutService#setGiftAddress(de.hybris.platform.core.model.order.CartModel
	 * , boolean, java.lang.String)
	 */
	@Override
	public void setGiftOrder(final CartModel cartModel, final boolean gift, final String giftMessage)
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		cartModel.setGift(gift);
		cartModel.setGiftMessage(giftMessage);
		cartModel.setNewDeliveryCost(cartModel.getDeliveryCost());
		getModelService().save(cartModel);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.OshCommerceCheckoutService#setStoreName(de.hybris.platform.core.model.order.CartEntryModel
	 * , java.lang.String)
	 */
	@Override
	public void setStoreName(final CartEntryModel cartEntryModel, final String storeName)
	{
		cartEntryModel.setOrderType(storeName);
		getModelService().save(cartEntryModel);

	}

	@Override
	public String getPaymentProvider()
	{
		// Lookup the payment provider name to use
		return getConfigurationService().getConfiguration().getString("payment.service-provider.name", "Cybersource");
	}

	@SuppressWarnings("boxing")
	@Override
	public PaymentTransactionEntryModel authorizePayment(final CartModel cartModel, final String securityCode,
			final String paymentProvider)
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(cartModel.getPaymentInfo(), "Payment information on cart cannot be null");
		Double totalPrice;
		/*
		 * final boolean isTaxPromo = false; final boolean isStatePromo = false; final boolean isSingleTaxPromotion =
		 * true; final boolean isOtherPromotion = false; final Set<PromotionResultModel> promotions =
		 * cartModel.getAllPromotionResults();
		 */
		/*
		 * if (promotions != null && !promotions.isEmpty()) { for (final PromotionResultModel promotionResultModel :
		 * promotions) { if (promotionResultModel.getPromotion() instanceof OshTaxPromotionModel) { isTaxPromo = true; }
		 * else { isSingleTaxPromotion = false; } if (promotionResultModel.getPromotion() instanceof
		 * OshStatePromotionModel) { isStatePromo = true; } if (promotions.size() < 5 && promotions.size() > 2) { if
		 * (!(promotionResultModel.getPromotion() instanceof OshStatePromotionModel)) { isOtherPromotion = true; } }
		 * 
		 * }
		 * 
		 * } if (isTaxPromo && !isSingleTaxPromotion) { totalPrice =
		 * Double.valueOf(cartModel.getTotalPrice().doubleValue()); } else { totalPrice =
		 * Double.valueOf(cartModel.getTotalPrice().doubleValue() + cartModel.getTotalTax().doubleValue()); } if
		 * (isStatePromo && isTaxPromo && !isOtherPromotion) { totalPrice =
		 * Double.valueOf(cartModel.getTotalPrice().doubleValue() + cartModel.getTotalTax().doubleValue()); } else if
		 * (isStatePromo && isTaxPromo && isOtherPromotion) { totalPrice =
		 * Double.valueOf(cartModel.getTotalPrice().doubleValue()); }
		 */

		totalPrice = cartModel.getTotalPrice().doubleValue() + cartModel.getTotalTax().doubleValue();
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		totalPrice = Double.valueOf(Helper.roundCurrencyValue(ctx, ctx.getCurrency(), totalPrice.doubleValue()).doubleValue());
		final BigDecimal totalPriceBD = BigDecimal.valueOf(totalPrice != null ? totalPrice.doubleValue() : 0d);

		return authorizePaymentAmount(cartModel, securityCode, paymentProvider, totalPriceBD);
	}

	@Override
	protected PaymentTransactionEntryModel authorizePaymentAmount(final CartModel cartModel, final String securityCode,
			final String paymentProvider, final BigDecimal amount)
	{
		PaymentTransactionEntryModel transactionEntryModel = null;
		final PaymentInfoModel paymentInfo = cartModel.getPaymentInfo();
		if (paymentInfo instanceof CreditCardPaymentInfoModel
				&& StringUtils.isNotBlank(((CreditCardPaymentInfoModel) paymentInfo).getSubscriptionId()))
		{

			final Currency currency = getI18nService().getBestMatchingJavaCurrency(cartModel.getCurrency().getIsocode());
			final String merchantTransactionCode = cartModel.getMerchantReferenceCode();
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Merchant Refrence Code " + merchantTransactionCode + " for cart " + cartModel.getCode());
			}
			try
			{

				transactionEntryModel = oshPaymentService.authorize(merchantTransactionCode, amount, currency,
						cartModel.getDeliveryAddress(), ((CreditCardPaymentInfoModel) paymentInfo).getSubscriptionId(), securityCode,
						paymentProvider);

				if (transactionEntryModel != null
						&& TransactionStatus.ACCEPTED.name().equals(transactionEntryModel.getTransactionStatus()))
				{

					if (!cartModel.isOrderAuthorized())
					{

						cartModel.setOrderAuthorized(true);
						final PaymentTransactionModel paymentTransaction = transactionEntryModel.getPaymentTransaction();
						paymentTransaction.setOrder(cartModel);
						getModelService().saveAll(cartModel, paymentTransaction);

					}

					else
					{
						cartModel.setOrderAuthorized(false);

					}

				}

			}
			catch (final Exception e)
			{
				LOG.debug("Merchant Refrence Code " + merchantTransactionCode + " for cart " + cartModel.getCode(), e);
			}
		}

		return transactionEntryModel;
	}

	@Override
	public OrderModel placeOrder(final CartModel cartModel, final SalesApplication salesApplication) throws InvalidCartException
	{

		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(salesApplication, "sales  can not be null");
		try
		{
			commerceCartService.recalculateCart(cartModel);
		}
		catch (final CalculationException e)
		{
			// YTODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		//Commented this code because two promotions are not getting applied simultaneously due to this
		/*
		 * if (!Boolean.TRUE.equals(cartModel.getCalculated())) { throw new
		 * IllegalArgumentException("Cart model must be calculated"); }
		 */
		final CustomerModel customer = (CustomerModel) cartModel.getUser();
		AddressModel storeAddress = null;
		validateParameterNotNull(customer, "Customer model cannot be null");

		normalizeEntriesNumbers(cartModel);
		if (customer.getMyStore() != null)
		{
			storeAddress = customer.getMyStore().getAddress();
		}
		final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
		if (orderModel != null)
		{
			// Store the current site and store on the order
			orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
			orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
			orderModel.setDate(new Date());

			if (!cartModel.getOrderType().equalsIgnoreCase(OshCoreConstants.ONLINE))
			{
				orderModel.setDeliveryAddress(storeAddress);
			}
			if (salesApplication != null)
			{
				orderModel.setSalesApplication(salesApplication);
			}
			getModelService().saveAll(customer, orderModel);

			// clear the promotionResults that where cloned from cart PromotionService.transferPromotionsToOrder will copy them over bellow.
			orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

			if (cartModel.getPaymentInfo() != null)
			{
				orderModel.setPaymentAddress(cartModel.getPaymentInfo().getBillingAddress());
				orderModel.getPaymentInfo().setBillingAddress(cartModel.getPaymentInfo().getBillingAddress());
				getModelService().save(orderModel.getPaymentInfo());
			}
			getModelService().save(orderModel);

			// Transfer promotions to the order
			getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);

			// Calculate the order now that it has been copied
			try
			{
				getCalculationService().calculateTotals(orderModel, false);
			}
			catch (final CalculationException ex)
			{
				LOG.error("Failed to calculate order [" + orderModel + "]", ex);
			}

			getModelService().refresh(orderModel);
			getModelService().refresh(customer);
			getOrderService().submitOrder(orderModel);
		}

		return orderModel;

	}


	@Override
	public String[] collectBCCAddressForEmail()
	{
		final String orderConfBCCEmail = getConfigurationService().getConfiguration().getString(ORDER_CONFIRMATION_BCC_EMAIL);
		Assert.notNull(orderConfBCCEmail, "EmailID For Failed Payment Transaction Can Not Be Null");

		final String[] emailIds = orderConfBCCEmail.split(",");
		return emailIds;
	}


}
