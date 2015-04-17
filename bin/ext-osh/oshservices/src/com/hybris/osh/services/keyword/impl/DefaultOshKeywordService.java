/**
 * 
 */
package com.hybris.osh.services.keyword.impl;

import de.hybris.platform.catalog.daos.KeywordDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import com.hybris.osh.services.keyword.OshKeywordService;


/**
 * 
 *
 */
public class DefaultOshKeywordService implements OshKeywordService
{
	private KeywordDao keywordDao;

	@Override
	public KeywordModel getKeyword(final CatalogVersionModel catalogVersion, final String keywordValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("catalogVersion", catalogVersion);
		ServicesUtil.validateParameterNotNullStandardMessage("keyword", keywordValue);

		final List<KeywordModel> res = keywordDao.getKeywords(catalogVersion, keywordValue);

		return res.isEmpty() ? null : res.get(0);

	}

	/**
	 * @return the keywordDao
	 */
	public KeywordDao getKeywordDao()
	{
		return keywordDao;
	}

	/**
	 * @param keywordDao
	 *           the keywordDao to set
	 */
	public void setKeywordDao(final KeywordDao keywordDao)
	{
		this.keywordDao = keywordDao;
	}
}
