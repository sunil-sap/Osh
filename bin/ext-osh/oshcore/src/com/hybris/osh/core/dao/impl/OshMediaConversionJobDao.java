/**
 *
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.mediaconversion.constants.MediaConversionConstants;
import de.hybris.platform.mediaconversion.job.DefaultMediaConversionJobDao;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.mediaconversion.model.job.MediaConversionCronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.util.Assert;


public class OshMediaConversionJobDao extends DefaultMediaConversionJobDao
{
	@Resource(name = "cronJobService")
	CronJobService cronJobService;

	@Resource(name = "configurationService")
	ConfigurationService configurationService;

	public static String IMPORT_JOB = "cronjob.images.import.job";

	@Override
	public Collection<List<PK>> queryFormatsPerContainerToConvert(final MediaConversionCronJobModel cronJob)
	{
		final Map<String, Object> params = this.queryParams(cronJob);
		// params are set by super class
		@SuppressWarnings("deprecation")
		final Date importJobTime = importJobDate();
		params.put("modifiedTime", importJobTime);
		final FlexibleSearchQuery query = new FlexibleSearchQuery( //

				"SELECT un.container, un.format " + //
						"FROM ( " + //

						// medias that are missing:
						"{{" + "SELECT sub.container, sub.format FROM ({{" + //
						"SELECT {a." + MediaContainerModel.PK + "} AS container, " + //
						"{f." + ConversionMediaFormatModel.PK + "} AS format, " + //
						"{m." + MediaModel.PK + "} AS media " + //
						"FROM {" + MediaContainerModel._TYPECODE + " AS a " + //
						"LEFT JOIN " + MediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL + " AS tf " + //
						"ON {a." + MediaContainerModel.CONVERSIONGROUP + "} = {tf." + LinkModel.SOURCE + "} " + //
						"JOIN " + ConversionMediaFormatModel._TYPECODE + " AS f " + //
						"ON ({a." + MediaContainerModel.CONVERSIONGROUP + "} IS NULL " + //
						"OR {tf." + LinkModel.TARGET + "} = {f." + ConversionMediaFormatModel.PK + "}) " + //
						"LEFT JOIN " + MediaModel._TYPECODE + " AS m " + //
						"ON {m." + MediaModel.MEDIACONTAINER + "} = {a." + MediaContainerModel.PK + "} " + //
						"AND {m." + MediaModel.MEDIAFORMAT + "} = {f." + ConversionMediaFormatModel.PK + "} " + //

						"} " + //
						"WHERE 1 = 1 " + //

						// options
						(params.containsKey(DefaultMediaConversionJobDao.MEDIAFORMATS_PARAMETER) ? //
						"AND {f." + ConversionMediaFormatModel.PK + "} IN " + //
								"(?" + DefaultMediaConversionJobDao.MEDIAFORMATS_PARAMETER + ") "
								: "") + //
						(params.containsKey(DefaultMediaConversionJobDao.CATALOGVERSION_PARAMETER) ? //
						"AND {a." + MediaContainerModel.CATALOGVERSION + "} = " + //
								"?" + DefaultMediaConversionJobDao.CATALOGVERSION_PARAMETER + " "
								: "") + //

						"}}) sub " + //deleted AS - not used in ORACLE
						"WHERE sub.media IS NULL " +

						"}} " + //
						"UNION ALL " + //
						"{{" + //
						this.outdatedMediaQuery(params) + "}} " + //
						") un " + // deleted AS - not used in ORACLE
						"ORDER BY un.container", //

				params);
		query.setResultClassList(Arrays.asList(new Class[]
		{ PK.class, PK.class }));

		System.out.println("NO Affected Media Containers...."
				+ this.getFlexibleSearchService().<List<PK>> search(query).getResult().size());
		return this.getFlexibleSearchService().<List<PK>> search(query).getResult();
	}

	Map queryParams(final MediaConversionCronJobModel cronJob)
	{
		final Map params = new TreeMap();
		if (cronJob.getIncludedFormats() != null && !cronJob.getIncludedFormats().isEmpty())
		{
			params.put("formats", cronJob.getIncludedFormats());
		}
		if (cronJob.getCatalogVersion() != null)
		{
			params.put("catVersion", cronJob.getCatalogVersion());
		}
		return params;
	}

	private String outdatedMediaQuery(final Map params)
	{
		return (new StringBuilder(
				"SELECT {ua.pk} AS container, {cm.mediaFormat} AS format FROM {MediaContainer AS ua JOIN Media as cm ON {cm.mediaContainer} = {ua.pk} JOIN ConversionMediaFormat as cmf ON {cm.mediaFormat} = {cmf.pk} LEFT JOIN Media as om ON {om.mediaContainer} = {ua.pk} AND {om.pk} = {cm.original} } WHERE {cm.originalDataPK} IS NOT NULL AND ({om.dataPK} IS NULL OR {cm.originalDataPK} <> {om.dataPK}) AND {ua.modifiedTime} > ?modifiedTime "))
				.append(params.containsKey("formats") ? "AND {cm.mediaFormat} IN (?formats) " : "")
				.append(params.containsKey("catVersion") ? "AND {ua.catalogVersion} = ?catVersion " : "").toString();
	}

	private Date importJobDate()
	{
		final String imagesImportJob = getConfigurationService().getConfiguration().getString(IMPORT_JOB);
		Assert.notNull(imagesImportJob, "Image Import Job Can Not BE NULL");

		final CronJobModel importJob = cronJobService.getCronJob(imagesImportJob);
		final Date date;
		if (importJob != null)
		{
			date = importJob.getModifiedtime();
		}
		else
		{
			final Calendar cal = Calendar.getInstance();
			cal.add(cal.HOUR, -3);
			date = cal.getTime();
		}
		return date;
	}

	/**
	 * @return the cronJobService
	 */
	public CronJobService getCronJobService()
	{
		return cronJobService;
	}

	/**
	 * @param cronJobService
	 *           the cronJobService to set
	 */
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}