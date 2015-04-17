package com.hybris.osh.core.valueproviders;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.hybris.osh.core.constants.OshCoreConstants;



public class OshSaleProductValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;
	private ModelService modelService;

	@Override
	public Collection getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model)
			throws FieldValueProviderException
	{
		final Collection fieldValues = new ArrayList();
		ProductModel product = null;

		if (model instanceof ProductModel)
		{
			product = (ProductModel) model;
		}


		if (indexedProperty.isCurrency())
		{
			final Collection<CurrencyModel> currencies = indexConfig.getCurrencies();
			for (final CurrencyModel currency : currencies)
			{
				fieldValues.addAll(createFieldValue(product, currency, indexedProperty));
			}
		}
		else
		{
			fieldValues.addAll(createFieldValue(product, null, indexedProperty));
		}
		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final CurrencyModel currency, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				currency == null ? null : currency.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}


	protected List<FieldValue> createFieldValue(final ProductModel productModel, final CurrencyModel currency,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		//final Collection<VariantProductModel> vpm = productModel.getVariants();
		//final VariantProduct product = getModelService().getSource(vpm.toArray()[0]);
		final Product product = modelService.getSource(productModel);

		final SessionContext sessionContext = OrderManager.getInstance().getSession().getSessionContext();

		final Europe1PriceFactory priceFactory = (Europe1PriceFactory) OrderManager.getInstance().getPriceFactory();
		final List<PriceRow> priceRows = (List<PriceRow>) priceFactory.getProductPriceRowsFast(sessionContext, product, null);
		//final PriceInformation price = null;

		for (final PriceRow priceRow : priceRows)
		{
			final String customerPriceGroup = priceRow.getUg().getCode();
			if (customerPriceGroup != null)
			{
				if (customerPriceGroup.equalsIgnoreCase(OshCoreConstants.SALE_PRICE) && calculatePrice(priceRow))
				{
					addFieldValues(fieldValues, indexedProperty, currency, "Sale");
				}
			}
		}

		return fieldValues;
	}

	/**
	 * @param priceValue
	 * @return boolean
	 */
	private boolean calculatePrice(final PriceRow priceValue)
	{

		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
		Date startDate = null;
		Date endDate = null;
		Date currentDate = new Date();
		if (priceValue.getStartTime() != null)
		{
			try
			{
				startDate = sdf.parse(sdf.format(priceValue.getStartTime()));
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage());
			}

		}
		if (priceValue.getEndTime() != null)
		{
			try
			{
				endDate = sdf.parse(sdf.format(priceValue.getEndTime()));
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage());
			}

		}
		try
		{
			currentDate = sdf.parse(sdf.format(currentDate));
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage());
		}


		if (currentDate.after(startDate) && currentDate.before(endDate))
		{
			return true;
		}
		else
		{
			return false;
		}

	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the fieldNameProvider
	 */
	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	/**
	 * @param fieldNameProvider
	 *           the fieldNameProvider to set
	 */
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}


}
