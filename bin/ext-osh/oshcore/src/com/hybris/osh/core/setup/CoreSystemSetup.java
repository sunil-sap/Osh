/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.core.setup;

import de.hybris.platform.acceleratorservices.setup.AbstractSystemSetup;
import de.hybris.platform.cockpit.systemsetup.CockpitImportConfig;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.validation.services.ValidationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.hybris.osh.core.constants.OshCoreConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 * 
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = OshCoreConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	public static final String IMPORT_SITES = "importSites";
	public static final String IMPORT_SYNC_CATALOGS = "syncProducts&ContentCatalogs";
	public static final String IMPORT_COCKPIT_COMPONENTS = "cockpitComponents";
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	public static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	public static final String OSH = "osh";

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/oshcore/import/common/essential-data.impex");
		importImpexFile(context, "/oshcore/import/common/countries.impex");
		importImpexFile(context, "/oshcore/import/common/delivery-modes.impex");
		importImpexFile(context, "/oshcore/import/common/states.impex");
		importImpexFile(context, "/oshcore/import/common/mcc-sites-links.impex");
		importImpexFile(context, "/oshcore/import/common/themes.impex");
	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_SITES, "Import Sites", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SYNC_CATALOGS, "Sync Products & Content Catalogs", false));
		params.add(createBooleanSystemSetupParameter(IMPORT_COCKPIT_COMPONENTS, "Import Cockpit Components", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", false));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean importSites = getBooleanSystemSetupParameter(context, IMPORT_SITES);
		final boolean importCockpitComponents = getBooleanSystemSetupParameter(context, IMPORT_COCKPIT_COMPONENTS);
		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);

		if (importSites)
		{

			final ImportCatalogAwareDataStrategy osh = new ImportCatalogAwareDataStrategy(context)
			{

				@Override
				String getProductCatalogName()
				{
					return OSH;
				}

				@Override
				List<String> getContentCatalogNames()
				{
					return Arrays.asList(OSH);
				}

				@Override
				List<String> getStoreNames()
				{
					return Arrays.asList(OSH);
				}

			};
			osh.importAllData();

			final ValidationService validation = getBeanForName("validationService");
			validation.reloadValidationEngine();
		}

		final List<String> extensionNames = getExtensionNames();

		if (importAccessRights && extensionNames.contains("cmscockpit"))
		{
			importImpexFile(context, "/oshcore/import/cockpits/cmscockpit/cmscockpit-users.impex");
			importImpexFile(context, "/oshcore/import/cockpits/cmscockpit/cmscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("btgcockpit"))
		{
			importImpexFile(context, "/oshcore/import/cockpits/cmscockpit/btgcockpit-users.impex");
			importImpexFile(context, "/oshcore/import/cockpits/cmscockpit/btgcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("productcockpit"))
		{
			importImpexFile(context, "/oshcore/import/cockpits/productcockpit/productcockpit-users.impex");
			importImpexFile(context, "/oshcore/import/cockpits/productcockpit/productcockpit-access-rights.impex");
			importImpexFile(context, "/oshcore/import/cockpits/productcockpit/productcockpit-constraints.impex");
		}

		if (importAccessRights && extensionNames.contains("cscockpit"))
		{
			importImpexFile(context, "/oshcore/import/cockpits/cscockpit/cscockpit-users.impex");
			importImpexFile(context, "/oshcore/import/cockpits/cscockpit/cscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("reportcockpit"))
		{
			importImpexFile(context, "/oshcore/import/cockpits/reportcockpit/reportcockpit-users.impex");
			importImpexFile(context, "/oshcore/import/cockpits/reportcockpit/reportcockpit-access-rights.impex");
		}

		if (importCockpitComponents)
		{
			final CockpitImportConfig config = getBeanForName("cockpitImportConfig");
			config.importCockpitConfig(context);
		}
	}


	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}


	/**
	 * Abstraction for a import process which should be adopted to use a dependent CatalogVersionSyncJob
	 * 
	 */
	private abstract class ImportCatalogAwareDataStrategy
	{

		private final SystemSetupContext context;

		public ImportCatalogAwareDataStrategy(final SystemSetupContext context)
		{
			this.context = context;
		}

		abstract String getProductCatalogName();

		abstract List<String> getContentCatalogNames();

		abstract List<String> getStoreNames();

		private boolean isSyncCatalog()
		{
			return getBooleanSystemSetupParameter(context, IMPORT_SYNC_CATALOGS);
		}



		public void importAllData()
		{
			createProductCatalog(getProductCatalogName());//

			for (final String contentCatalogName : getContentCatalogNames())
			{

				createContentCatalog(contentCatalogName);
			}

			assignDependent(getProductCatalogName(), getContentCatalogNames());

			for (final String contentCatalogName : getContentCatalogNames())
			{

				if (isSyncCatalog())
				{
					syncContentCatalog(contentCatalogName);
				}
			}


			if (isSyncCatalog())
			{
				syncProductCatalog(getProductCatalogName());
			}

			createAndActivateSolrIndex(getProductCatalogName());

			for (final String storeName : getStoreNames())
			{
				createStore(storeName);
			}


		}

		private void createAndActivateSolrIndex(final String catalogName)
		{
			logInfo(context, "Begin SOLR index setup [" + catalogName + "]");

			createSolrIndexerCronJobs(catalogName + "Index");

			//importImpexFile(context, "/oshcore/import/productCatalogs/" + catalogName + "ProductCatalog/solrtrigger.impex");

			if (getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS))
			{
				executeSolrIndexerCronJob(catalogName + "Index", true);
				activateSolrIndexerCronJobs(catalogName + "Index");
			}

			logInfo(context, "Done SOLR index setup [" + catalogName + "]");
		}

		private void createStore(final String storeName)
		{
			logInfo(context, "Begin importing store [" + storeName + "]");

			importImpexFile(context, "/oshcore/import/stores/" + storeName + "/store.impex");
			importImpexFile(context, "/oshcore/import/stores/" + storeName + "/site.impex");

			logInfo(context, "Done importing store [" + storeName + "]");
		}

		/**
		 * fires product catalog with synchronization with respect for a dependent CatalogVersionSyncJob
		 */
		private void syncProductCatalog(final String productCatalogName)
		{
			executeCatalogSyncJob(context, productCatalogName + "ProductCatalog");
		}

		/**
		 * fires content catalog with synchronization with respect for a depends on CatalogVersionSyncJob
		 * 
		 */
		private void syncContentCatalog(final String contentCatalogName)
		{
			executeCatalogSyncJob(context, contentCatalogName + "ContentCatalog");
		}

		private void assignDependent(final String dependsOnProduct, final List<String> dependentContents)
		{
			if (CollectionUtils.isNotEmpty(dependentContents) && StringUtils.isNotBlank(dependsOnProduct))
			{
				final Set<String> dependentSyncJobsNames = new HashSet<String>();
				for (final String content : dependentContents)
				{
					dependentSyncJobsNames.add(content + "ContentCatalog");
				}

				getSetupSyncJobService().assignDependentSyncJobs(dependsOnProduct + "ProductCatalog", dependentSyncJobsNames);
			}
		}

		private void createContentCatalog(final String contentCatalogName)
		{
			logInfo(context, "Begin importing catalog [" + contentCatalogName + "]");

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/catalog.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/cms-content.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/email-content.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/cartPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/categoryPage.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/loginPage.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/changePasswordPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/checkoutLoginPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/homePage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/homepage2.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/loginPage.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/errorPageNotFoundPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/productDetailPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/productListingPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/searchEmptyPage.impex", true);
			importImpexFile(context,
					"/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/searchGridPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/account-homepage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/storeLocator.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/singleStepCheckoutSummaryPage.impex", true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/landingpage-template.impex", true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/siteMapPage.impex",
					true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/wishlist-loginPage.impex", true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/orderConfirmationPage.impex", true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/cheetahMailSubscriber.impex", true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/cheetah_mail_page.impex", true);

			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/contactUsPage.impex",
					true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName
					+ "ContentCatalog/supplier_partnersPage.impex", true);
			importImpexFile(context, "/oshcore/import/contentCatalogs/" + contentCatalogName + "ContentCatalog/blank-Page.impex",
					true);


			createContentCatalogSyncJob(context, contentCatalogName + "ContentCatalog");

		}

		private void createProductCatalog(final String productCatalogName)
		{
			logInfo(context, "Begin importing catalog [" + productCatalogName + "]");

			importImpexFile(context, "/oshcore/import/productCatalogs/" + productCatalogName + "ProductCatalog/catalog.impex", true);
			createProductCatalogSyncJob(context, productCatalogName + "ProductCatalog");
		}
	}
}
