/**
 *
 */
package com.hybris.osh.core.jobs;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 * @author ubuntu
 *
 */
public class CategoryLinksCronjobs extends AbstractJobPerformable<CronJobModel>
{

	private static final Logger LOG = Logger.getLogger(CategoryLinksCronjobs.class);

	private CategoryService categoryService;
	private CatalogService catalogService;
	@Resource
	private ModelService modelService;
	@Resource
	private MediaService mediaService;


	@Resource
	private CMSNavigationService cmsNavigationService;
	final static private String CATALOG = "oshProductCatalog";
	final static private String CATALOG_VERSION = "Online";


	@Resource
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	/**
	 * @param catalogService
	 *           the catalogService to set
	 */
	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}

	@SuppressWarnings("deprecation")
	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		String log = "\nCategoryLinksCronjob has changed following category Links visiblity status \nCategoryCode\tCMSLinkComponentCode :-\t Boolean";
		/*
		 * final LogFileModel logFileModel = modelService.create(LogFileModel.class);
		 * logFileModel.setFolder(mediaService.getFolder("cronjob")); logFileModel.setOwner(cronJob);
		 * logFileModel.setCode(new Date().toString() + "LogFile.zip"); final MediaConversionService
		 * logFileModel.setURL("/" + logFileModel.getCode()); logFileModel.setRealFileName(logFileModel.getCode());
		 * logFileModel.setSubFolderPath("hc4/h38"); final Collection<LogFileModel> logFileModels = new
		 * ArrayList<LogFileModel>(); logFileModels.addAll(cronJob.getLogFiles());
		 */

		final Collection<CategoryModel> listCategoryModel = categoryService.getRootCategoriesForCatalogVersion(catalogService
				.getCatalogVersion(CATALOG, CATALOG_VERSION));
		for (final CategoryModel categoryModel : listCategoryModel)
		{
			for (final CategoryModel subCategoryModel : categoryModel.getAllSubcategories())
			{
				if (isApprovedProducts(subCategoryModel) /* && isLeafRoot(subCategoryModel) */)
				{
					// make cms link to enable
					for (final CMSLinkComponentModel cmsLinkComponentModel : subCategoryModel.getLinkComponents())
					{
						if (cmsLinkComponentModel.getVisible().booleanValue() == false)
						{
							log = log + "\n " + subCategoryModel.getCode() + "\t" + cmsLinkComponentModel.getUid() + "\t Enabled";
							cmsLinkComponentModel.setVisible(true);
							modelService.save(cmsLinkComponentModel);
							modelService.refresh(cmsLinkComponentModel);
						}
					}
				}
				else
				{
					// make cms link to disable
					for (final CMSLinkComponentModel cmsLinkComponentModel : subCategoryModel.getLinkComponents())
					{
						if (cmsLinkComponentModel.getVisible().booleanValue() == true)
						{
							log = log + "\n " + subCategoryModel.getCode() + "\t\t" + cmsLinkComponentModel.getUid() + "\t Disabled";
							cmsLinkComponentModel.setVisible(false);
							modelService.save(cmsLinkComponentModel);
							modelService.refresh(cmsLinkComponentModel);
						}
					}
				}
			}
		}

		//LOG.setLevel(Level.OFF);
		LOG.info(log);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private boolean isApprovedProducts(final CategoryModel categoryModel)
	{
		final Collection<ProductModel> category = categoryModel.getProducts();
		for (final ProductModel product : category)
		{
			if (product.getApprovalStatus().equals(ArticleApprovalStatus.APPROVED))
			{
				return true;
			}
		}
		final Collection<CategoryModel> subCategoryModels = categoryModel.getAllSubcategories();
		for (final CategoryModel categoryModels : subCategoryModels)
		{
			final Collection<ProductModel> productModels = categoryModels.getProducts();
			for (final ProductModel productModel : productModels)
			{
				if (productModel.getApprovalStatus().equals(ArticleApprovalStatus.APPROVED))
				{
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * private boolean isLeafRoot(final CategoryModel categoryModel) { final List<ProductModel> productModel =
	 * categoryModel.getProducts(); if (productModel.isEmpty()) { if (categoryModel.getLinkComponents().size() == 0) {
	 * return false; } } return true; }
	 */
}
