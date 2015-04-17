/**
 * 
 */
package com.hybris.osh.facades.tax.impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.SystemConfig;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.service.OshTaxService;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.tax.OshTaxFacade;
import com.hybris.osh.facades.user.data.OshAddressData;


public class OshTaxFacadeImpl implements OshTaxFacade
{

	protected static final Logger LOG = Logger.getLogger(OshTaxFacadeImpl.class);

	private static final String NEXUS = "osh.tax.nexus";

	private KeyGenerator keyGenerator;

	@Resource(name = "oshTaxService")
	private OshTaxService oshTaxService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public double calculateTax(final CartData cart)
	{
		final String nexus = configurationService.getConfiguration().getString(NEXUS);
		final PlatformConfig platformConfig = ConfigUtil
				.getPlatformConfig(de.hybris.platform.cybersource.adapter.impl.ExecutorFactoryImpl.class);
		final SystemConfig systemConfig = ConfigUtil.getSystemConfig(platformConfig.getPlatformHome().getAbsolutePath());
		final String configDir = systemConfig.getConfigDir().getAbsolutePath();
		final String keysDirectory = configDir + "//security//";
		final Properties cybersourceProperties = new Properties();
		loadingPropertiesFromClassloader("/cybs.properties", cybersourceProperties, true);
		cybersourceProperties.setProperty("keysDirectory", keysDirectory);
		OshAddressData oshBillingAddress = null;
		if (cart.getPaymentInfo() != null)
		{
			oshBillingAddress = (OshAddressData) cart.getPaymentInfo().getBillingAddress();
		}
		final OshAddressData oshShippingAddress = (OshAddressData) cart.getDeliveryAddress();

		final HashMap request = new HashMap();
		HashMap response = new HashMap();
		request.put("merchantID", "orchard");
		request.put("taxService_run", "true");
		final String merchantTransactionCode = (String) keyGenerator.generate();
		request.put("merchantReferenceCode", merchantTransactionCode);
		request.put("taxService_nexus", nexus);
		if (oshBillingAddress != null)
		{
			//set Billing information 
			request.put("billTo_city", oshBillingAddress.getTown());
			request.put("billTo_country", oshBillingAddress.getCountry().getIsocode());
			request.put("billTo_postalCode", oshBillingAddress.getPostalCode());
			request.put("billTo_state", oshBillingAddress.getState().getIsocode());
		}

		//request.put("billTo_street1", oshBillingAddress.getLine1());
		if (oshShippingAddress != null)
		{
			//set the Shipping information //shipTo_city shipTo_country shipTo_postalCode shipTo_state shipTo_street1
			request.put("shipTo_city", oshShippingAddress.getTown());
			request.put("shipTo_country", oshShippingAddress.getCountry().getIsocode());
			request.put("shipTo_postalCode", oshShippingAddress.getPostalCode());
			request.put("shipTo_state", oshShippingAddress.getState().getIsocode());
			//request.put("shipTo_street1", oshShippingAddress.getLine1());
		}

		if (cart.getEntries() != null && !cart.getEntries().isEmpty())
		{
			int i = 0;
			for (final OrderEntryData entry : cart.getEntries())
			{
				/*
				 * final double totalPrice = ((OshOrderEntryData) entry).getDiscountPrice().getValue().doubleValue()
				 * entry.getQuantity().doubleValue();
				 */
				final double totalPrice = ((OshOrderEntryData) entry).getProductDiscountPrice().getValue().doubleValue();
				System.out.println("............................" + totalPrice * entry.getQuantity().longValue());
				request.put("item_" + i++ + "_unitPrice", Double.valueOf(totalPrice).toString());
			}
		}

		response = oshTaxService.calculateTax(request, cybersourceProperties);

		addCartOnEntry(cart, response);
		return parseReply(response);

	}

	/**
	 * @param taxreply
	 * @return double
	 */
	private double parseReply(final HashMap taxreply)
	{
		if (!taxreply.isEmpty() && taxreply.get("taxReply_totalTaxAmount") != null)
		{
			final double taxAmount = Double.parseDouble((String) taxreply.get("taxReply_totalTaxAmount"));
			//final TaxValue taxValue = new TaxValue("VAT_Full", taxAmount, true, "USD");
			final CartModel cart = cartService.getSessionCart();
			/*
			 * cart.setTotalTaxValues(Collections.singletonList(taxValue)); cart.setTotalTax(Double.valueOf(taxAmount));
			 * cart.setTaxAmount(Double.valueOf(taxAmount));
			 */
			cart.setMerchantReferenceCode((String) taxreply.get("merchantReferenceCode"));
			modelService.save(cart);

			try
			{
				commerceCartService.recalculateCart(cart);
			}
			catch (final CalculationException e)
			{

				LOG.error(e.getMessage());
			}
			return taxAmount;
		}

		return 0;
	}

	/**
	 * @param cartData
	 * @param taxreply
	 */
	private void addCartOnEntry(final CartData cartData, final HashMap taxreply)
	{

		final CartModel cartModel = cartService.getSessionCart();
		int i = 0;
		double taxAmount = 0.0;
		double taxPerUnit = 0.0;
		double totalTax = 0.0;

		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			final CartEntryModel cartEntryModel = cartService.getEntryForNumber(cartModel, orderEntryData.getEntryNumber()
					.intValue());
			final String taxPerEntry = (String) taxreply.get("taxReply_item_" + i + "_totalTaxAmount");
			if (!taxreply.isEmpty() && taxPerEntry != null)
			{
				taxPerUnit = Double.valueOf(taxPerEntry).doubleValue();
				taxAmount = taxPerUnit * orderEntryData.getQuantity().doubleValue();

				//taxPerUnit = Math.round((taxAmount / orderEntryData.getQuantity().doubleValue()) * 10000) / 10000D;
				final TaxValue taxValue = new TaxValue(cartEntryModel.getProduct().getCode(), taxPerUnit, true, taxPerUnit, cartModel
						.getCurrency().getIsocode());
				final List<TaxValue> taxValues = new ArrayList<TaxValue>();
				taxValues.add(taxValue);
				cartEntryModel.setTaxValues(taxValues);
				cartEntryModel.setTaxAmount(Double.valueOf(taxAmount));
				cartEntryModel.setTaxPerUnit(Double.valueOf(taxPerUnit));
				modelService.save(cartEntryModel);
				modelService.refresh(cartEntryModel);
				totalTax = totalTax + taxAmount;
			}

			i += 1;
		}
		cartModel.setTotalTax(Double.valueOf(totalTax));
		//	cartModel.setTaxAmount(Double.valueOf(totalTax));
		modelService.save(cartModel);
		modelService.refresh(cartModel);

	}

	private void loadingPropertiesFromClassloader(final String path, final Properties prop, final boolean failOnError)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug((new StringBuilder("Loading ")).append(path).append(" ...").toString());
		}
		try
		{
			final InputStream stream = this.getClass().getResourceAsStream(path);
			prop.load(stream);
			stream.close();
		}
		catch (final Exception e)
		{
			if (failOnError)
			{
				throw new RuntimeException((new StringBuilder("cannot load ")).append(path).append(" from ear-classloader!")
						.toString(), e);
			}
		}
	}

	public KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}



}
