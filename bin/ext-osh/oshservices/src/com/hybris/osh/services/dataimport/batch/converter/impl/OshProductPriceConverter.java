/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.converter.impl;

import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexRowFilter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.NullImpexRowFilter;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.enterprisedt.util.debug.Logger;
import com.hybris.osh.services.dataimport.batch.translator.CustomBatchColumnTranslator;
import com.hybris.osh.services.dataimport.batch.translator.CustomBatchMultipleColumnTranslator;
import com.hybris.osh.services.keyword.OshProductService;


/**
 * 
 *
 */
public class OshProductPriceConverter implements ImpexConverter
{
	private static Logger LOG = Logger.getLogger(OshImpexConverter.class);

	private static final char PLUS_CHAR = '+';
	private static final char SEQUENCE_CHAR = 'S';
	private static final String EMPTY_STRING = "";
	private static final char BRACKET_END = '}';
	private static final char BRACKET_START = '{';

	//custom fields created for osh
	private int linesToSkip;
	private String fieldSeperator;

	private String header;
	private String impexRow;
	private String type;
	private ImpexRowFilter rowFilter = new NullImpexRowFilter();

	private Map<String, CustomBatchColumnTranslator> customColumnTranslators = new HashMap<String, CustomBatchColumnTranslator>();
	private Map<String, CustomBatchMultipleColumnTranslator> customMultipleColumnTranslators = new HashMap<String, CustomBatchMultipleColumnTranslator>();
	private UserService userService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private OshProductService oshProductService;


	@Override
	public String convert(final Map<Integer, String> row, final Long sequenceId)
	{
		userService.setCurrentUser(userService.getAdminUser());
		String result = EMPTY_STRING;
		if (!MapUtils.isEmpty(row))
		{
			final StringBuilder builder = new StringBuilder();
			int copyIdx = 0;
			int idx = impexRow.indexOf(BRACKET_START);
			while (idx > -1)
			{
				final int endIdx = impexRow.indexOf(BRACKET_END, idx);
				if (endIdx < 0)
				{
					throw new SystemException("Invalid row syntax [brackets not closed]: " + impexRow);
				}
				builder.append(impexRow.substring(copyIdx, idx));
				final char nextChar = impexRow.charAt(idx + 1);
				if (nextChar == SEQUENCE_CHAR)
				{
					builder.append(sequenceId);
				}
				else if (customColumnTranslators.containsKey(String.valueOf(nextChar)))
				{
					translateColumnValue(nextChar, idx, endIdx, row, builder);
				}
				else
				{
					buildColumnValue(nextChar, idx, endIdx, row, builder);
				}
				copyIdx = endIdx + 1;
				idx = impexRow.indexOf(BRACKET_START, endIdx);
			}
			if (copyIdx < impexRow.length())
			{
				builder.append(impexRow.substring(copyIdx));
			}
			result = builder.toString();
		}
		return result;
	}


	protected void translateColumnValue(final char nextChar, final int idx, final int endIdx, final Map<Integer, String> row,
			final StringBuilder builder)
	{
		Integer mapIdx = null;
		try
		{
			mapIdx = Integer.valueOf(impexRow.substring(idx + 2, endIdx));
		}
		catch (final NumberFormatException e)
		{
			throw new SystemException("Invalid custom row syntax [invalid column number]: " + impexRow, e);
		}
		final String colValue = row.get(mapIdx);
		if (StringUtils.isBlank(colValue))
		{
			throw new IllegalArgumentException("Missing value for custom column " + mapIdx);
		}

		if (checkProductNotNull(row.get(Integer.valueOf(4))))
		{
			String newColValue;
			if (colValue != null)
			{
				if (colValue.contains(";"))
				{
					newColValue = colValue.replaceAll(";", "&#59");
					builder.append(getCustomColumnTranslators().get(String.valueOf(nextChar)).translate(newColValue));
				}
				if (colValue.startsWith("\""))
				{
					colValue.replace("\"", "&#34");
				}
				else
				{
					//call the translator
					builder.append(getCustomColumnTranslators().get(String.valueOf(nextChar)).translate(colValue));
				}
			}
		}

	}



	protected void buildColumnValue(final char nextChar, final int idx, final int endIdx, final Map<Integer, String> row,
			final StringBuilder builder)
	{
		final boolean mandatory = nextChar == PLUS_CHAR;
		Integer mapIdx = null;
		try
		{
			mapIdx = Integer.valueOf(impexRow.substring(mandatory ? idx + 2 : idx + 1, endIdx));
		}
		catch (final NumberFormatException e)
		{
			throw new SystemException("Invalid row syntax [invalid column number]: " + impexRow, e);
		}
		final String colValue = row.get(mapIdx);
		if (mandatory && StringUtils.isBlank(colValue))
		{
			throw new IllegalArgumentException("Missing value for " + mapIdx);
		}
		if (checkProductNotNull(row.get(Integer.valueOf(4))))
		{
			String newColValue;
			if (colValue != null)
			{
				if (colValue.contains(";"))
				{
					newColValue = colValue.replaceAll(";", "&#59");
					builder.append(newColValue);
				}
				else if (colValue.startsWith("\""))
				{
					colValue.replace("\"", "&#34");
				}
				else
				{
					builder.append(colValue);
				}
			}
		}

	}

	/**
	 * @param code
	 */
	private boolean checkProductNotNull(final String code)
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("oshProductCatalog", "Staged");
		final ProductModel product = oshProductService.getProduct(catalogVersion, code);
		return product != null ? true : false;
	}

	public Map<String, CustomBatchColumnTranslator> getCustomColumnTranslators()
	{
		return customColumnTranslators;
	}

	public void setCustomColumnTranslators(final Map<String, CustomBatchColumnTranslator> customColumnTranslators)
	{
		this.customColumnTranslators = customColumnTranslators;
	}

	/**
	 * @see de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter#filter(java.util.Map)
	 */
	@Override
	public boolean filter(final Map<Integer, String> row)
	{
		return rowFilter.filter(row);
	}

	@Override
	public String getHeader()
	{
		return header;
	}

	@Required
	public void setHeader(final String header)
	{
		Assert.hasText(header);
		this.header = header;
	}

	@Required
	public void setImpexRow(final String impexRow)
	{
		Assert.hasText(impexRow);
		this.impexRow = impexRow;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the linesToSkip
	 */
	public int getLinesToSkip()
	{
		return linesToSkip;
	}

	/**
	 * @param linesToSkip
	 *           the linesToSkip to set
	 */
	public void setLinesToSkip(final int linesToSkip)
	{
		this.linesToSkip = linesToSkip;
	}

	/**
	 * @return the fieldSeperator
	 */
	public String getFieldSeperator()
	{
		return fieldSeperator;
	}

	/**
	 * @param fieldSeperator
	 *           the fieldSeperator to set
	 */
	public void setFieldSeperator(final String fieldSeperator)
	{
		this.fieldSeperator = fieldSeperator;
	}

	/**
	 * @param rowFilter
	 *           the rowFilter to set
	 */
	public void setRowFilter(final ImpexRowFilter rowFilter)
	{
		Assert.notNull(rowFilter);
		this.rowFilter = rowFilter;
	}

	/**
	 * @return the customMultipleColumnTranslators
	 */
	public Map<String, CustomBatchMultipleColumnTranslator> getCustomMultipleColumnTranslators()
	{
		return customMultipleColumnTranslators;
	}

	public void setCustomMultipleColumnTranslators(
			final Map<String, CustomBatchMultipleColumnTranslator> customMultipleColumnTranslators)
	{
		this.customMultipleColumnTranslators = customMultipleColumnTranslators;
	}

	protected static char getPlusChar()
	{
		return PLUS_CHAR;
	}

	protected static char getSequenceChar()
	{
		return SEQUENCE_CHAR;
	}

	protected static String getEmptyString()
	{
		return EMPTY_STRING;
	}

	protected static char getBracketEnd()
	{
		return BRACKET_END;
	}

	protected static char getBracketStart()
	{
		return BRACKET_START;
	}

	protected String getImpexRow()
	{
		return impexRow;
	}

	protected ImpexRowFilter getRowFilter()
	{
		return rowFilter;
	}

}
