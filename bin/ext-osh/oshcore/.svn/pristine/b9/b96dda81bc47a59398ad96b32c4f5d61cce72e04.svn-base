package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.core.dao.OshOrderDao;
import com.hybris.osh.core.service.DropshipFileCreatorService;
import com.hybris.osh.core.service.DropshipPOFileCreatorService;
import com.hybris.osh.core.service.OshSFTPService;


/**
 * Implementation class for DropshipFileCreatorService
 * 
 */
public class DefaultDropshipFileCreatorService implements DropshipFileCreatorService
{
	private OshOrderDao oshOrderDao;
	private ModelService modelService;

	@Resource(name = "dropshipPOFileCreatorService")
	private DropshipPOFileCreatorService dropshipPOFileCreatorService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	static private final Logger LOG = Logger.getLogger(DefaultDropshipFileCreatorService.class);

	private OshSFTPService oshSFTPService;

	private static final String PO_DEST_PATH = "po.file.sftp.destination";
	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_PORT = "tlog.file.sftp.port";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";

	@Override
	public boolean createDropshipInputFile(final Date triggerDate)
	{
		try
		{
			final String path = Config.getString("tlog.files", "${HYBRIS_DATA_DIR}/Tlog");
			LOG.info("path is :" + path);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(triggerDate);
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final String currentDate = sdf.format(calendar.getTime());
			final File dir = new File(path + "/po/poPending/" + currentDate + calendar.get(calendar.HOUR));
			dir.mkdirs();
			boolean isPoDone = false;
			final String fileName = "po_pending_" + calendar.get(calendar.HOUR) + "_" + calendar.get(calendar.MINUTE) + "_"
					+ calendar.get(calendar.SECOND) + ".txt";
			final File file = new File(dir + "/" + fileName);
			final File finalFile = new File(path + "/" + currentDate + "/" + fileName);
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			if (finalFile.exists())
			{
				finalFile.delete();
			}
			file.createNewFile();
			LOG.info("The file is created");
			calendar.add(Calendar.DATE, -1);
			final List<OrderModel> orders = oshOrderDao.getPoRelatedOrders(calendar.getTime());
			for (final OrderModel orderModel : orders)
			{
				if (orderModel.getConsignments() != null && !orderModel.getConsignments().isEmpty() && !orderModel.isPoPerformed()
				/* && orderModel.getCancelDate() == null */)
				{
					final AbstractOrderModel order = orderModel;
					if (order.getPaymentAddress() != null && order.getDeliveryAddress() != null)
					{
						/*
						 * final List<ConsignmentEntryModel> entries =
						 * getDropshipPOFileCreatorService().getEligiblePOConsignment( orderModel.getConsignments());
						 */
						bw.write(getDropshipPOFileCreatorService().createPOHDRRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOCSTBilingRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOCSTShippingRecords(orderModel));
						bw.newLine();

						for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
						{

							/*
							 * if (!((consignmentModel.getStatus() == ConsignmentStatus.PENDING) &&
							 * (consignmentModel.getOrder().getStatus() == OrderStatus.PARTIAL_FULFILLED) && (!consignmentModel
							 * .isSentToDropship()))) {
							 */
							final List<String> records = getDropshipPOFileCreatorService().createSKULevelRecords(consignmentModel);
							for (final String record : records)
							{
								bw.write(record);
								bw.newLine();
							}
							/* } */
						}
						bw.write(getDropshipPOFileCreatorService().createPOSHPRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOSHPTAXRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOTOTRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOTNDRecords(orderModel));
						bw.newLine();
						orderModel.setPoPerformed(true);
						modelService.save(orderModel);
						modelService.refresh(orderModel);
						isPoDone = true;
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Billing Address Or Delivery Address can not be null for Order:-" + order.getCode());
						}
					}
				}

			}
			bw.close();
			if (isPoDone)
			{
				sftpPoFile(file);
			}
			return true;
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean createDropshipConfirmationFile(final Date triggerDate)
	{
		try
		{

			/**
			 * Create folder if dont exist
			 */
			String path = Config.getString("tlog.files", "${HYBRIS_DATA_DIR}/Tlog");
			path = path.replaceAll("\\\\", "/");
			LOG.info("path is :" + path);

			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(triggerDate);
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final String currentDate = sdf.format(calendar.getTime());
			final File dir = new File(path + "/po/poConf/" + currentDate + calendar.get(calendar.HOUR));
			dir.mkdirs();
			boolean isPoConfDone = false;
			final String fileName = "po_confirmed_" + calendar.get(calendar.HOUR) + "_" + calendar.get(calendar.MINUTE) + "_"
					+ calendar.get(calendar.MINUTE) + ".txt";
			final File file = new File(dir + "/" + fileName);
			final File finalFile = new File(path + "/" + currentDate + "/" + fileName);
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			if (finalFile.exists())
			{
				finalFile.delete();
			}
			file.createNewFile();
			LOG.info("The file is created");
			calendar.add(Calendar.DATE, -1);
			final List<OrderModel> orders = oshOrderDao.getPoConfirmationOrders(calendar.getTime());
			for (final OrderModel orderModel : orders)
			{
				if (orderModel.getConsignments() != null && !orderModel.getConsignments().isEmpty())
				{
					final AbstractOrderModel order = orderModel;
					if (order.getPaymentAddress() != null && order.getDeliveryAddress() != null)
					{
						bw.write(getDropshipPOFileCreatorService().createPOHDRRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOCSTBilingRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOCSTShippingRecords(orderModel));
						bw.newLine();

						for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
						{

							final List<String> records = getDropshipPOFileCreatorService().createCancelSKULevelRecords(consignmentModel);
							for (final String record : records)
							{
								bw.write(record);
								bw.newLine();
							}
						}
						bw.write(getDropshipPOFileCreatorService().createPOSHPRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOSHPTAXRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOTOTRecords(orderModel));
						bw.newLine();
						bw.write(getDropshipPOFileCreatorService().createPOTNDRecords(orderModel));
						bw.newLine();
						orderModel.setPoConfirmPerformed(true);
						modelService.save(orderModel);
						modelService.refresh(orderModel);
						isPoConfDone = true;
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Billing Address Or Delivery Address can not be null for Order:-" + order.getCode());
						}
					}
				}
			}

			bw.close();
			if (isPoConfDone)
			{
				sftpPoFile(file);
			}
			return true;
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public OshOrderDao getOshOrderDao()
	{
		return oshOrderDao;
	}

	public void setOshOrderDao(final OshOrderDao oshOrderDao)
	{
		this.oshOrderDao = oshOrderDao;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the dropshipPOFileCreatorService
	 */
	public DropshipPOFileCreatorService getDropshipPOFileCreatorService()
	{
		return dropshipPOFileCreatorService;
	}

	/**
	 * @param dropshipPOFileCreatorService
	 *           the dropshipPOFileCreatorService to set
	 */
	public void setDropshipPOFileCreatorService(final DropshipPOFileCreatorService dropshipPOFileCreatorService)
	{
		this.dropshipPOFileCreatorService = dropshipPOFileCreatorService;
	}

	@Override
	public boolean sftpPoFile(final File poFile)
	{
		Assert.notNull(poFile, "Tlog file cannot be null");
		final String sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		final String sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		final String sftpdestPath = configurationService.getConfiguration().getString(PO_DEST_PATH);
		Assert.notNull(sftpdestPath, "SFTP Destination Path Can Not BE NULL");

		final String privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		getOshSFTPService().sendFile(poFile, sftpHost, sftpUser, null, privateKey, sftpdestPath);
		return true;
	}

	@Override
	public Date findPOTriggerDate(final List<TriggerModel> triggers)
	{
		final Calendar cal = Calendar.getInstance();
		Date triggerDate = cal.getTime();


		if (triggers != null && !triggers.isEmpty())
		{
			final TriggerModel trigger = triggers.get(0);
			final CronJobModel cronJob = trigger.getCronJob();
			if (cronJob.getIntegrationDate() != null)
			{

				cal.setTime(cronJob.getIntegrationDate());
				triggerDate = cal.getTime();
			}
		}
		else
		{
			triggerDate = cal.getTime();
		}
		return triggerDate;
	}

	/**
	 * @return the oshSFTPService
	 */
	public OshSFTPService getOshSFTPService()
	{
		return oshSFTPService;
	}

	/**
	 * @param oshSFTPService
	 *           the oshSFTPService to set
	 */
	public void setOshSFTPService(final OshSFTPService oshSFTPService)
	{
		this.oshSFTPService = oshSFTPService;
	}
}
