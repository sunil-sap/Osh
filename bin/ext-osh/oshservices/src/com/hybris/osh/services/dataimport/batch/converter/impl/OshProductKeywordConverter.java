/**
 * 
 */
package com.hybris.osh.services.dataimport.batch.converter.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.services.keyword.OshKeywordService;



/**
 * 
 *
 */
public class OshProductKeywordConverter extends OshImpexConverter
{
	@Autowired
	private ProductService productService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private OshKeywordService keywordService;
	@Autowired
	private BaseSiteService siteService;


	private static Logger LOG = Logger.getLogger(OshProductKeywordConverter.class);

	@Override
	protected void buildColumnValue(final char nextChar, final int idx, final int endIdx, final Map<Integer, String> row,
			final StringBuilder builder)
	{
		final boolean mandatory = nextChar == getPlusChar();
		Integer mapIdx = null;
		try
		{
			mapIdx = Integer.valueOf(getImpexRow().substring(mandatory ? idx + 2 : idx + 1, endIdx));
		}
		catch (final NumberFormatException e)
		{
			throw new SystemException("Invalid row syntax [invalid column number]: " + getImpexRow(), e);
		}
		final String colValue = row.get(mapIdx);
		final BaseSiteModel baseSiteModel = siteService.getBaseSiteForUID("osh");
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("oshProductCatalog", "Staged");
		final String productCode = "B_" + row.get(Integer.valueOf(0));
		final String keywords = row.get(Integer.valueOf(12));
		final String[] keywordarray = keywords.split(",");
		final List<KeywordModel> lst = new ArrayList<KeywordModel>();
		final List<String> keywordList = Arrays.asList(keywordarray);

		for (final String str : keywordList)
		{
			final String keyword = str.replaceAll("\"", "");
			KeywordModel keywordModel = keywordService.getKeyword(catalogVersion, keyword);
			if (keywordModel == null)
			{
				keywordModel = modelService.create(KeywordModel.class);
				keywordModel.setKeyword(keyword);
				keywordModel.setLanguage(baseSiteModel.getDefaultLanguage());
				keywordModel.setCatalogVersion(catalogVersion);
				modelService.save(keywordModel);
				modelService.refresh(keywordModel);
			}
			lst.add(keywordModel);
		}

		final ProductModel product = productService.getProductForCode(catalogVersion, productCode);
		product.setKeywords(lst);
		modelService.save(product);


		builder.append("");
	}
}
