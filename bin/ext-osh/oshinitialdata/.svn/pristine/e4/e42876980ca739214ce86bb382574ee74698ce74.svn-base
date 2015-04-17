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
package com.hybris.osh.initialdata.setup;

import de.hybris.platform.acceleratorservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.setup.CoreSystemSetup;
import com.hybris.osh.initialdata.constants.OshInitialDataConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 * 
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = OshInitialDataConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	private static final Logger LOG = Logger.getLogger(InitialDataSystemSetup.class);

	private static final String IMPORT_OSH_DATA = "importOshData";
	private static final String IMPORT_SHIPPING_DATA = "importShippingData";
	private static final String OSH_IMPORT_FOLDER = "oshinitialdata";

	private String productCatalogNameForRerun = null; //null indicates no rerun needed

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_OSH_DATA, "Import Initial Data for OSH", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SHIPPING_DATA, "Import Shipping Data for OSH", false));
		params.add(createBooleanSystemSetupParameter(CoreSystemSetup.ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs for OSH",
				true));
		// Add more Parameters here as your require

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.INIT)
	public void createEssentialData(final SystemSetupContext context)
	{
		//Load Shipping Charges Table
		if (getBooleanSystemSetupParameter(context, IMPORT_SHIPPING_DATA))
		{
			importImpexFile(context, "/oshinitialdata/import/productCatalogs/oshProductCatalog/shippingCharges.impex");
		}
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		if (getBooleanSystemSetupParameter(context, IMPORT_OSH_DATA))
		{
			logInfo(context, "Importing osh site content...");
			importCommonData(context, OSH_IMPORT_FOLDER);
			importStoreInitialData(context, OSH_IMPORT_FOLDER, OshCoreConstants.OSH_STORE, OshCoreConstants.OSH_STORE,
					Collections.singletonList(OshCoreConstants.OSH_STORE));
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/synchronization-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/dropshipConfirmationFile-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/dropshipInputFile-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/tlogEOD-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/tlogHourly-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/dropshipFeedProcessing-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/dropshipFeedProcessing-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/image-magick-media-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/image-magick-product-media-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/remove-original-image.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/image-magick-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/osh-SFTP-Detail.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/online-inventory-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/product-feed-jprocessing-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/price-feed-processing-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/store-composite-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/cheetah-Mail-job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/cart-Removal-Job.impex", true);
			importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/Check-Feed-FileExistance-jobs.impex", true);


		}
	}

	/**
	 * Use this method to import a standard setup store.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 * @param storeName
	 *           the name of the store
	 * @param productCatalog
	 *           the name of the product catalog
	 * @param contentCatalogs
	 *           the list of content catalogs
	 */
	protected void importStoreInitialData(final SystemSetupContext context, final String importDirectory, final String storeName,
			final String productCatalog, final List<String> contentCatalogs)
	{
		logInfo(context, "Begin importing store [" + storeName + "]");

		//importImpexFile(context, "/" + OSH_IMPORT_FOLDER + "/import/common/synchronization-job.impex", true);

		importProductCatalog(context, importDirectory, productCatalog);

		for (final String contentCatalog : contentCatalogs)
		{
			importContentCatalog(context, importDirectory, contentCatalog);
		}

		//logInfo(context, "Begin importing advanced personalization rules for [" + storeName + "]");
		//importImpexFile(context, importRoot + "/stores/" + storeName + "/btg.impex", false);

		//create jobs
		synchronizeProductCatalog(context, productCatalog, false);
		for (final String contentCatalog : contentCatalogs)
		{
			synchronizeContentCatalog(context, contentCatalog, false);
		}
		assignDependent(context, productCatalog, contentCatalogs);
		//perform jobs
		synchronizeProductCatalog(context, productCatalog, true);

		logInfo(context, "Begin SOLR re-index [" + productCatalog + "]");
		executeSolrIndexerCronJob(productCatalog + "Index", true);
		logInfo(context, "Done SOLR re-index [" + productCatalog + "]");

		for (final String contentCatalog : contentCatalogs)
		{
			synchronizeContentCatalog(context, contentCatalog, true);
		}


		//	logInfo(context, "Begin importing store [" + storeName + "]");

		//logInfo(context, "Done importing store [" + storeName + "]");

		if (getBooleanSystemSetupParameter(context, CoreSystemSetup.ACTIVATE_SOLR_CRON_JOBS))
		{
			logInfo(context, "Activating SOLR index job for [" + productCatalog + "]");
			activateSolrIndexerCronJobs(productCatalog + "Index");
		}
	}

	protected void importProductCatalog(final SystemSetupContext context, final String importDirectory, final String catalogName)
	{
		logInfo(context, "Begin importing Product Catalog [" + catalogName + "]");

		// Load Units
		//importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
		//		+ "ProductCatalog/classifications-units.impex", false);

		// Load Categories
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/categories.impex", true);
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/categories_en.impex", true);
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/categories-media.impex", true);
		/*
		 * importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName +
		 * "ProductCatalog/categories-classifications.impex", true);
		 */

		// Load Suppliers
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/suppliers.impex", true);
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/suppliers-media.impex", true);

		// Load Products
		importImpexFile(context,
				"/" + importDirectory + "/import/productCatalogs/" + catalogName + "ProductCatalog/products.impex", true);
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/variant-products.impex", false);

		/*
		 * importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName +
		 * "ProductCatalog/products-media.impex", true);
		 */

		//importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
		//		+ "ProductCatalog/products-media.impex", false);
		/*
		 * importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName +
		 * "ProductCatalog/products-classifications.impex", true);
		 */

		// Load Products Relations
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/products-relations.impex", true);

		// Load Prices
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/products-prices.impex", true);


		// Load Promotions
		//importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
		//		+ "ProductCatalog/promotions.impex", false);

		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/media-conversion-formats.impex", true);

		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName + "ProductCatalog/solr.impex",
				true);

		// Load Stock Levels
		importImpexFile(context, "/" + importDirectory + "/import/productCatalogs/" + catalogName
				+ "ProductCatalog/products-stocklevels.impex", true);
	}

	protected void synchronizeProductCatalog(final SystemSetupContext context, final String catalogName, final boolean sync)
	{
		logInfo(context, "Begin synchronizing Product Catalog [" + catalogName + "] - "
				+ (sync ? "synchronizing" : "initializing job"));

		createProductCatalogSyncJob(context, catalogName + "ProductCatalog");

		if (sync)
		{
			final PerformResult syncCronJobResult = executeCatalogSyncJob(context, catalogName + "ProductCatalog");

			if (isSyncRerunNeeded(syncCronJobResult))
			{
				this.productCatalogNameForRerun = catalogName;
				logInfo(context, "Product catalog sync has issues. Setting the flag to re-run the sync");
			}
		}

		logInfo(context, "Done " + (sync ? "synchronizing" : "initializing job") + " Product Catalog [" + catalogName + "]");
	}


	protected void importContentCatalog(final SystemSetupContext context, final String importDirectory, final String catalogName)
	{
		logInfo(context, "Begin importing Content Catalog [" + catalogName + "]");

		final String importRoot = "/" + importDirectory + "/import";

		importImpexFile(context, importRoot + "/contentCatalogs/" + catalogName + "ContentCatalog/cms-content.impex", true);
		//importImpexFile(context, importRoot + "/contentCatalogs/" + catalogName + "ContentCatalog/cms-mobile-content.impex", false);
		//importImpexFile(context, importRoot + "/contentCatalogs/" + catalogName + "ContentCatalog/email-content.impex", false);

		logInfo(context, "Done importing Content Catalog [" + catalogName + "]");
	}

	protected void synchronizeContentCatalog(final SystemSetupContext context, final String catalogName, final boolean sync)
	{
		logInfo(context, "Begin synchronizing Content Catalog [" + catalogName + "] - "
				+ (sync ? "synchronizing" : "initializing job"));

		createContentCatalogSyncJob(context, catalogName + "ContentCatalog");

		if (sync)
		{
			executeCatalogSyncJob(context, catalogName + "ContentCatalog");
		}

		if (sync && StringUtils.isNotEmpty(this.productCatalogNameForRerun))
		{
			logInfo(context, "Rerunnig product catalog synchronization for  [" + this.productCatalogNameForRerun + "]");
			if (isSyncRerunNeeded(executeCatalogSyncJob(context, this.productCatalogNameForRerun + "ProductCatalog")))
			{
				logInfo(context, "Rerunnig product catalog synchronization for [" + this.productCatalogNameForRerun
						+ "], failed   please consult logs for more details.");
			}
			this.productCatalogNameForRerun = null;
		}

		logInfo(context, "Done " + (sync ? "synchronizing" : "initializing job") + " Content Catalog [" + catalogName + "]");


	}

	/**
	 * Imports Common Data
	 */
	protected void importCommonData(final SystemSetupContext context, final String importDirectory)
	{
		logInfo(context, "Importing Common Data...");

		final String importRoot = "/" + importDirectory + "/import";

		importImpexFile(context, importRoot + "/common/user-groups.impex", false);
		//importImpexFile(context, importRoot + "/common/promotions.impex", false);

		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		if (extensionNames.contains("cmscockpit"))
		{
			importImpexFile(context, importRoot + "/cockpits/cmscockpit/cmscockpit-users.impex");
		}

		if (extensionNames.contains("productcockpit"))
		{
			importImpexFile(context, importRoot + "/cockpits/productcockpit/productcockpit-users.impex");
		}

		if (extensionNames.contains("cscockpit"))
		{
			importImpexFile(context, "/" + importDirectory + "/import" + "/stores/" + "osh" + "/points-of-service-media.impex",
					false);
			importImpexFile(context, "/" + importDirectory + "/import" + "/stores/" + "osh" + "/points-of-service.impex", false);
			importImpexFile(context, importRoot + "/cockpits/cscockpit/cscockpit-users.impex");
			//importing restrictions
			importImpexFile(context, importRoot + "/cockpits/cscockpit/restrictions.impex");

			importImpexFile(context, importRoot + "/cockpits/cscockpit/projectdata_ui_components.impex");
		}

		/*
		 * if (extensionNames.contains("reportcockpit")) { importImpexFile(context, importRoot +
		 * "/cockpits/reportcockpit/reportcockpit-users.impex"); importImpexFile(context, importRoot +
		 * "/cockpits/reportcockpit/reportcockpit-mcc-links.impex"); }
		 */
	}


	private void assignDependent(final SystemSetupContext context, final String dependsOnProduct,
			final List<String> dependentContents)
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

}
