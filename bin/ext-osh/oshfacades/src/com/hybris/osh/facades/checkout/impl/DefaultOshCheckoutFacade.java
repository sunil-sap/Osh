package com.hybris.osh.facades.checkout.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.commerceservices.converter.Converters;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.impl.DefaultOshDeliveryModeDao;
import com.hybris.osh.core.delivery.impl.DefaultOshDeliveryService;
import com.hybris.osh.core.model.OSHShippingModel;
import com.hybris.osh.core.model.OshVariantProductModel;
import com.hybris.osh.core.service.GuestCustomerAccountService;
import com.hybris.osh.core.service.OshCommerceCheckoutService;
import com.hybris.osh.core.service.OshCommerceCommonI18NService;
import com.hybris.osh.core.service.PopulateRecordTypesService;
import com.hybris.osh.facades.checkout.OshCheckoutFacade;
import com.hybris.osh.facades.user.data.OshAddressData;
import com.hybris.osh.facades.user.data.StateData;
import com.hybris.osh.facades.voucher.impl.OshVoucherFacade;
import com.hybris.osh.payment.services.OshPaymentService;


public class DefaultOshCheckoutFacade extends DefaultCheckoutFacade implements OshCheckoutFacade
{

	protected static final Logger LOG = Logger.getLogger(DefaultOshCheckoutFacade.class);
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "oshPaymentService")
	private OshPaymentService oshPaymentService;


	@Resource(name = "oshVoucherFacade")
	private OshVoucherFacade oshVoucherFacade;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;

	@Resource(name = "stockService")
	private StockService stockService;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "defaultOshDeliveryService")
	private DefaultOshDeliveryService defaultOshDeliveryService;

	@Resource(name = "defaultOshDeliveryModeDao")
	private DefaultOshDeliveryModeDao defaultOshDeliveryModeDao;

	@Resource(name = "populateRecordTypesService")
	private PopulateRecordTypesService populateRecordTypesService;

	@Resource(name = "guestCustomerAccountService")
	private GuestCustomerAccountService guestCustomerAccountService;

	/**
	 * @return the oshPaymentService
	 */
	public OshPaymentService getOshPaymentService()
	{
		return oshPaymentService;
	}

	/**
	 * @param oshPaymentService
	 *           the oshPaymentService to set
	 */
	public void setOshPaymentService(final OshPaymentService oshPaymentService)
	{
		this.oshPaymentService = oshPaymentService;
	}

	/**
	 * @return the cartService
	 */
	@Override
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Override
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	private Converter<CreditCardType, CardTypeData> cardTypeConverter;


	private OshCommerceCheckoutService oshCommerceCheckoutService;

	public OshCommerceCommonI18NService getOSHCommerceCommonI18NService()
	{
		if (commerceCommonI18NService instanceof OshCommerceCommonI18NService)
		{
			return (OshCommerceCommonI18NService) commerceCommonI18NService;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.facades.checkout.OshCheckoutFacade#getStates(de.hybris.platform.core.model.c2l.CountryModel)
	 */

	@Override
	public List<CardTypeData> getSupportedCardTypes()
	{
		final List<CreditCardType> cardTypes = new ArrayList();
		cardTypes.add(CreditCardType.VISA);
		cardTypes.add(CreditCardType.MASTERCARD);
		cardTypes.add(CreditCardType.AMEX);
		cardTypes.add(CreditCardType.DISCOVER);
		return Converters.convertAll(cardTypes, getCardTypeConverter());
	}


	@Override
	public CCPaymentInfoData createPaymentSubscription(final CCPaymentInfoData paymentInfoData)
	{
		validateParameterNotNullStandardMessage("paymentInfoData", paymentInfoData);
		final OshAddressData billingAddressData = (OshAddressData) paymentInfoData.getBillingAddress();
		validateParameterNotNullStandardMessage("billingAddress", billingAddressData);

		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final UserModel currentUser = getUserService().getCurrentUser();
			if (cartModel.getUser().equals(currentUser))
			{
				final CardInfo cardInfo = new CardInfo();
				cardInfo.setCardHolderFullName(paymentInfoData.getAccountHolderName());
				cardInfo.setCardNumber(paymentInfoData.getCardNumber());
				final CreditCardType cardType = (CreditCardType) getEnumerationService().getEnumerationValue(
						CreditCardType.class.getSimpleName(), paymentInfoData.getCardType());
				cardInfo.setCardType(cardType);
				cardInfo.setExpirationMonth(Integer.valueOf(paymentInfoData.getExpiryMonth()));
				cardInfo.setExpirationYear(Integer.valueOf(paymentInfoData.getExpiryYear()));
				cardInfo.setIssueNumber(paymentInfoData.getIssueNumber());

				final BillingInfo billingInfo = new BillingInfo();
				billingInfo.setCity(billingAddressData.getTown());
				if (billingAddressData.getCountry() != null)
				{
					billingInfo.setCountry(billingAddressData.getCountry().getIsocode());
				}
				billingInfo.setFirstName(billingAddressData.getFirstName());
				billingInfo.setLastName(billingAddressData.getLastName());
				billingInfo.setPhoneNumber(billingAddressData.getPhone());
				billingInfo.setPostalCode(billingAddressData.getPostalCode());
				billingInfo.setStreet1(billingAddressData.getLine1());
				billingInfo.setStreet2(billingAddressData.getLine2());
				billingInfo.setState(billingAddressData.getState().getIsocode());
				billingInfo.setEmail(billingAddressData.getEmail());

				final CreditCardPaymentInfoModel ccPaymentInfoModel = getCustomerAccountService().createPaymentSubscription(
						(CustomerModel) currentUser, cardInfo, billingInfo, billingAddressData.getTitleCode(), getPaymentProvider(),
						paymentInfoData.isSaved());
				if (ccPaymentInfoModel != null)
				{
					return getCreditCardPaymentInfoConverter().convert(ccPaymentInfoModel);
				}
			}
		}

		return null;
	}



	@Override
	public CCPaymentInfoData createPaymentSubscriptionWithoutCart(final CCPaymentInfoData paymentInfoData)
	{
		validateParameterNotNullStandardMessage("paymentInfoData", paymentInfoData);
		final OshAddressData billingAddressData = (OshAddressData) paymentInfoData.getBillingAddress();
		validateParameterNotNullStandardMessage("billingAddress", billingAddressData);


		final UserModel currentUser = getUserService().getCurrentUser();

		final CardInfo cardInfo = new CardInfo();
		cardInfo.setCardHolderFullName(paymentInfoData.getAccountHolderName());
		cardInfo.setCardNumber(paymentInfoData.getCardNumber());
		final CreditCardType cardType = (CreditCardType) getEnumerationService().getEnumerationValue(
				CreditCardType.class.getSimpleName(), paymentInfoData.getCardType());
		cardInfo.setCardType(cardType);
		cardInfo.setExpirationMonth(Integer.valueOf(paymentInfoData.getExpiryMonth()));
		cardInfo.setExpirationYear(Integer.valueOf(paymentInfoData.getExpiryYear()));
		cardInfo.setIssueNumber(paymentInfoData.getIssueNumber());

		final BillingInfo billingInfo = new BillingInfo();
		billingInfo.setCity(billingAddressData.getTown());
		if (billingAddressData.getCountry() != null)
		{
			billingInfo.setCountry(billingAddressData.getCountry().getIsocode());
		}
		billingInfo.setFirstName(billingAddressData.getFirstName());
		billingInfo.setLastName(billingAddressData.getLastName());
		billingInfo.setPhoneNumber(billingAddressData.getPhone());
		billingInfo.setPostalCode(billingAddressData.getPostalCode());
		billingInfo.setStreet1(billingAddressData.getLine1());
		billingInfo.setStreet2(billingAddressData.getLine2());
		billingInfo.setState(billingAddressData.getState().getIsocode());
		billingInfo.setPhoneNumber(billingAddressData.getPhone());
		final CreditCardPaymentInfoModel ccPaymentInfoModel = getCustomerAccountService().createPaymentSubscription(
				(CustomerModel) currentUser, cardInfo, billingInfo, billingAddressData.getTitleCode(), getPaymentProvider(),
				paymentInfoData.isSaved());
		if (ccPaymentInfoModel != null)
		{
			return getCreditCardPaymentInfoConverter().convert(ccPaymentInfoModel);
		}

		return null;
	}


	@Override
	public List<StateData> getDefaultDeliveryStates()
	{
		return getStates(OshCoreConstants.DEFAULT_COUNTRY_CODE);
	}


	public static class StateComparator extends AbstractComparator<RegionModel>
	{
		public static final StateComparator INSTANCE = new StateComparator();

		@Override
		protected int compareInstances(final RegionModel state1, final RegionModel state2)
		{
			final int result = (state1.getName() != null && state2.getName() != null) ? state1.getName().compareToIgnoreCase(
					state2.getName()) : BEFORE;
			return result;
		}
	}

	protected List<RegionModel> sortStates(final Collection<RegionModel> states)
	{
		final List<RegionModel> result = new ArrayList<RegionModel>(states);
		Collections.sort(result, StateComparator.INSTANCE);
		return result;
	}


	@Override
	public List<StateData> getStates(final CountryModel countryModel)
	{
		final List<StateData> states = new ArrayList<StateData>();

		final List<RegionModel> regions = sortStates(getOSHCommerceCommonI18NService().getRegionsByCountry(countryModel));
		for (final RegionModel region : regions)
		{
			final StateData stateData = new StateData();
			stateData.setIsocode(region.getIsocode());
			stateData.setName(region.getName());
			stateData.setCountryIsoCode(region.getCountry().getIsocode());
			states.add(stateData);
		}

		return states;
	}

	@Override
	public List<StateData> getStates(final String countryCode)
	{
		final CountryModel countryModel = getCommonI18NService().getCountry(countryCode);
		if (countryModel != null)
		{
			return getStates(countryModel);
		}

		return Collections.emptyList();
	}




	@Override
	public void setGiftOrder(final boolean gift, final String giftMessage)
	{
		final CartModel cartModel = getCart();
		getOshCommerceCheckoutService().setGiftOrder(cartModel, gift, giftMessage);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.facades.checkout.OshCheckoutFacade#setStoreAddressIfAvailable(boolean, java.lang.String)
	 */
	@Override
	public void setStoreAddressIfAvailable()
	{
		final CartModel cartModel = getCart();
		getOshCommerceCheckoutService().setStoreAddress(cartModel);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hybris.osh.facades.checkout.OshCheckoutFacade#setStoreName(de.hybris.platform.core.model.order.CartEntryModel,
	 * java.lang.String)
	 */
	@Override
	public void setStoreName(final CartEntryModel cartEntryModel, final String storeName)
	{
		getOshCommerceCheckoutService().setStoreName(cartEntryModel, storeName);
	}

	@Override
	protected Converter<CreditCardType, CardTypeData> getCardTypeConverter()
	{
		return cardTypeConverter;
	}

	@Override
	public void setCardTypeConverter(final Converter<CreditCardType, CardTypeData> cardTypeConverter)
	{
		this.cardTypeConverter = cardTypeConverter;
	}

	/**
	 * @return the oshCommerceCheckoutService
	 */
	public OshCommerceCheckoutService getOshCommerceCheckoutService()
	{
		return oshCommerceCheckoutService;
	}

	/**
	 * @param oshCommerceCheckoutService
	 *           the oshCommerceCheckoutService to set
	 */
	public void setOshCommerceCheckoutService(final OshCommerceCheckoutService oshCommerceCheckoutService)
	{
		this.oshCommerceCheckoutService = oshCommerceCheckoutService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.facades.checkout.OshCheckoutFacade#reverseAuthorization()
	 */
	@Override
	public String reverseAuthorization()
	{
		final CartModel cartRelease = cartService.getSessionCart();
		PaymentTransactionEntryModel txnResultEntry = null;
		final List<PaymentTransactionModel> txns = cartRelease.getPaymentTransactions();
		if (!(txns.isEmpty()))
		{
			//final PaymentTransactionModel txn = txns.iterator().next();
			final PaymentTransactionModel txn = txns.get(txns.size() - 1);
			final List<PaymentTransactionEntryModel> txnEntries = txn.getEntries();
			final PaymentTransactionEntryModel txnEntry = txnEntries.iterator().next();
			final Currency currency = Currency.getInstance(txnEntry.getCurrency().getIsocode());
			final String merchantTransactionCode = txnEntry.getCode();

			txnResultEntry = oshPaymentService.fullReverseAuth(txn, merchantTransactionCode, txnEntry.getRequestId(), currency,
					txnEntry.getAmount(), txnEntry.getPaymentTransaction().getPaymentProvider());
			if (txnResultEntry != null)
			{
				return txnResultEntry.getTransactionStatus();
			}
		}
		return null;
	}

	@Override
	public List<? extends DeliveryModeData> getSupportedDeliveryModes()
	{
		final List<DeliveryModeData> result = new ArrayList<DeliveryModeData>();
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final Collection<DeliveryModeModel> supportedDeliveryModes = defaultOshDeliveryService
					.getSupportedDeliveryModesForOrder(cartModel);


			for (final DeliveryModeModel deliveryModeModel : supportedDeliveryModes)
			{
				result.add(convert(deliveryModeModel));
			}
		}

		oshVoucherFacade.updateVoucher();
		return result;
	}

	/**
	 * Calculate valid zipcode which is present in Shipping Charges table.
	 */
	@Override
	public boolean isValidZipcode(final String state, final String postalcode)
	{
		Long maxZipcode = null;
		Long minZipcode = null;
		final List<OSHShippingModel> minMaxRangeModel = defaultOshDeliveryService.findMinMaxRangeOfZipcode(state);
		final ArrayList<Long> minZipcodeList = new ArrayList<Long>();
		final ArrayList<Long> maxZipcodeList = new ArrayList<Long>();
		for (final OSHShippingModel oshShippingModel : minMaxRangeModel)
		{
			if (oshShippingModel.getZipcodeFrom() != null && oshShippingModel.getZipcodeTo() != null)
			{
				if (!minZipcodeList.contains(oshShippingModel.getZipcodeFrom()))
				{
					minZipcodeList.add(Long.valueOf(oshShippingModel.getZipcodeFrom()));
				}
				if (!maxZipcodeList.contains(oshShippingModel.getZipcodeTo()))
				{
					maxZipcodeList.add(Long.valueOf(oshShippingModel.getZipcodeTo()));
				}

			}

		}
		if (!maxZipcodeList.isEmpty())
		{
			maxZipcode = Collections.max(maxZipcodeList);
		}
		if (!minZipcodeList.isEmpty())
		{
			minZipcode = Collections.min(minZipcodeList);
		}
		final Long zipcode = Long.valueOf(postalcode);
		if (zipcode != null && minZipcode != null && maxZipcode != null)
		{
			if (zipcode.longValue() >= minZipcode.longValue() && zipcode.longValue() <= maxZipcode.longValue())
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setCashRegisterNo(final CartModel cart)
	{
		return getPopulateRecordTypesService().setCashRegiserNo(cart);
	}

	@Override
	public boolean setCustomerID(final CartModel cart)
	{
		final CustomerModel customer = (CustomerModel) cart.getUser();
		return guestCustomerAccountService.setCustomerAccNo(customer);
	}

	/**
	 * @return the populateRecordTypesService
	 */
	public PopulateRecordTypesService getPopulateRecordTypesService()
	{
		return populateRecordTypesService;
	}

	/**
	 * @param populateRecordTypesService
	 *           the populateRecordTypesService to set
	 */
	public void setPopulateRecordTypesService(final PopulateRecordTypesService populateRecordTypesService)
	{
		this.populateRecordTypesService = populateRecordTypesService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.osh.facades.checkout.OshCheckoutFacade#checkStoreStock(java.lang.String)
	 */
	@Override
	public boolean checkStoreStock(final String productCode, final String storeName)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		WarehouseModel wareHouseModel = null;
		int storeStock = 0;
		if (storeName != null)
		{
			wareHouseModel = warehouseService.getWarehouseForCode(storeName);
		}
		try
		{
			if (wareHouseModel != null)
			{
				storeStock = stockService.getStockLevelAmount(productModel, wareHouseModel);

			}
		}
		catch (final StockLevelNotFoundException e)
		{
			storeStock = 0;
		}
		return storeStock != 0 ? false : true;
	}

	@Override
	public boolean isWeightForHomeDirect()
	{
		final CartModel cartModel = getCart();
		double totalWeight = 0.0;
		if (cartModel != null)
		{
			if (cartModel.getDeliveryAddress() != null)
			{
				final String state = cartModel.getDeliveryAddress().getRegion().getIsocode();
				final String zipcode = cartModel.getDeliveryAddress().getPostalcode();
				final double maxWeight = defaultOshDeliveryService.maxWeight(state, zipcode);

				for (final AbstractOrderEntryModel orderEntryModel : cartModel.getEntries())
				{
					//for online only
					double weight = 0.0;
					if (orderEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE))
					{
						final OshVariantProductModel model = (OshVariantProductModel) orderEntryModel.getProduct();
						if (model.getWeight() != null)
						{
							weight = model.getWeight().doubleValue();

						}
						weight *= orderEntryModel.getQuantity().doubleValue();
					}
					totalWeight += weight;
				}
				return maxWeight < totalWeight;
			}
		}
		return false;
	}
}