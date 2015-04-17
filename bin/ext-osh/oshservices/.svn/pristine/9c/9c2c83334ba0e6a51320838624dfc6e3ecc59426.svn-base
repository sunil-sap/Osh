package com.hybris.osh.services.keyword;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;


public interface OshKeywordService
{

	/**
	 * Returns the Keyword for the specified keyword value and <code>CatalogVersion</code>.
	 * 
	 * @param catalogVersion
	 *           The <code>CatalogVersion</code> the <code>Keyword</code> belongs to.
	 * @param keywordValue
	 *           The value of the searched <code>Keyword</code>.
	 * @return The matching <code>Keyword</code>.
	 * 
	 * @throws UnknownIdentifierException
	 *            when keyword not found.
	 * @throws AmbiguousIdentifierException
	 *            when more then one found.
	 */
	KeywordModel getKeyword(final CatalogVersionModel catalogVersion, final String keywordValue);


}
