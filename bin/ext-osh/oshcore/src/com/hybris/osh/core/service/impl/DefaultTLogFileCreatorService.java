package com.hybris.osh.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.OshOrderDao;
import com.hybris.osh.core.logger.OshIntegrationLogger;
import com.hybris.osh.core.model.CreditPaymentTransactionModel;
import com.hybris.osh.core.service.OshSFTPService;
import com.hybris.osh.core.service.PopulateRecordTypesService;
import com.hybris.osh.core.service.TLogFileCreatorService;


/**
 * Implementation class for TLogFileCreatorService
 * 
 */
public class DefaultTLogFileCreatorService implements TLogFileCreatorService
{
	private OshOrderDao oshOrderDao;
	private ModelService modelService;
	private PopulateRecordTypesService populateRecordTypesService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	private OshSFTPService oshSFTPService;
	private static final String TLOG_DESTINATION = "tlog.file.sftp.destination";
	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_PORT = "tlog.file.sftp.port";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";
	private static final String TO_FOLDER = "polling.file.sftp.to.folder";
	private static final String POSTATUS_FOLDER = "po.file.sftp.postatus.destination";
	private static final String TRANS_FAILED_EMAIL = "payment.failed.notification.emailid";
	static private final Logger LOG = Logger.getLogger(DefaultTLogFileCreatorService.class);



	@Override
	public boolean lastHourOrders(final Date triggerDate)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(triggerDate);
		cal.add(cal.DATE, -2);

		final List<OrderModel> Orders = oshOrderDao.getLastHourOrders(cal.getTime());
		final List<OrderModel> creditOrders = oshOrderDao.getCreditOrders();

		validateParameterNotNullStandardMessage("Orders", Orders);
		BufferedWriter bw = null;
		File file = null;
		boolean isTlogRecord = false;
		boolean isCreditable = false;
		try
		{
			final String path = Config.getString("tlog.files", "${HYBRIS_DATA_DIR}" + File.separator + "polling");
			OshIntegrationLogger.info("path is :" + path);
			final Calendar calendar = Calendar.getInstance();
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final String currentDate = sdf.format(calendar.getTime());
			final File dir = new File(path + File.separator + "tlog" + File.separator + currentDate);
			dir.mkdirs();
			final String fileName = "eodtlog_" + currentDate + "_" + calendar.get(calendar.HOUR) + "_"
					+ calendar.get(calendar.MINUTE) + "_" + calendar.get(calendar.SECOND) + ".dat";
			file = new File(dir + File.separator + fileName);
			//final File finalFile = new File(dir + File.separator + fileName);
			final FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			/*
			 * if (finalFile.exists()) { finalFile.delete(); }
			 */

			file.createNewFile();

			final List<String> poStatus = new ArrayList<String>();

			poStatus.add("PENDING");
			poStatus.add("CANCELLED");
			poStatus.add("READY_FOR_PICKUP");

			//final OrderModel orderModel = Orders.iterator().next();


			for (final OrderModel orderModel : Orders)
			{

				final boolean isNoReturnRequest = orderModel.getReturnRequests().isEmpty();
				boolean isReturnInfoEmpty = true;
				Map<String, Object> retunInfo = null;

				OshIntegrationLogger.info("TLOG File Creation Started on " + orderModel.getCode());

				if (!isNoReturnRequest)
				{
					retunInfo = populateRecordTypesService.createEligibleReturnEntries(orderModel.getReturnRequests());
					isReturnInfoEmpty = retunInfo.isEmpty();
				}
				final Set<ConsignmentModel> consignments = orderModel.getConsignments();
				if (consignments != null && !consignments.isEmpty() && (!orderModel.isTlogPerformed() || !isReturnInfoEmpty)
						&& !(orderModel.getPaymentTransactions() == null || orderModel.getPaymentTransactions().isEmpty()))
				{
					if (orderModel.getStatus() == OrderStatus.PARTIAL_FULFILLED || orderModel.getStatus() == OrderStatus.COMPLETED)
					{
						OshIntegrationLogger.info("Performing Tlog on " + orderModel.getCode() + " having order status "
								+ orderModel.getStatus().getCode());
						populateRecordTypesService.calculateOrderAmount(orderModel);
						populateRecordTypesService.getProductsDiscountsAmount(orderModel);
					}
					bw.write(populateRecordTypesService.createHDRRecords(orderModel, retunInfo));
					bw.newLine();

					bw.write(populateRecordTypesService.createCSTBillingRecords(orderModel));
					bw.newLine();
					bw.write(populateRecordTypesService.createCSTShippingRecords(orderModel));
					bw.newLine();

					if (isReturnInfoEmpty)
					{
						for (final ConsignmentModel consignmentModel : consignments)
						{
							if (consignmentModel.getStatus() != ConsignmentStatus.PENDING
									|| consignmentModel.getStatus() != ConsignmentStatus.READY_FOR_PICKUP)
							{
								OshIntegrationLogger.info("STARTING TO CREATE ALL SKU RECORDS");
								final List<String> skuRecords = populateRecordTypesService
										.createAllSKURecords(consignmentModel, poStatus);
								for (final String skuRecord : skuRecords)
								{
									bw.write(skuRecord);
									bw.newLine();
								}
							}
						}
					}
					else
					{
						final List<ReturnEntryModel> returnEntries = (List<ReturnEntryModel>) retunInfo.get("returnOrderEntries");
						for (final ReturnEntryModel returnEntryModel : returnEntries)
						{
							OshIntegrationLogger.info("STARTING TO CREATE ALL SKU RECORDS FOR RETURN ORDERS");
							bw.write(populateRecordTypesService.createRTNRecord(returnEntryModel.getOrderEntry()));
							bw.newLine();
							bw.write(populateRecordTypesService.createReturnSKURecords(returnEntryModel));
							bw.newLine();
						}
						final ReturnRequestModel returnRequest = returnEntries.get(0).getReturnRequest();
						returnRequest.setTlogPerformed(true);
						modelService.save(returnRequest);
						modelService.refresh(returnRequest);
					}

					bw.write(populateRecordTypesService.createSHPRecords(orderModel));
					bw.newLine();
					bw.write(populateRecordTypesService.createLDSRecords(orderModel, retunInfo));
					bw.newLine();
					populateRecordTypesService.getLastTransactionAmount(orderModel, triggerDate);
					bw.write(populateRecordTypesService.createTAXRecords(orderModel));
					bw.newLine();
					bw.write(populateRecordTypesService.createTNDRecords(orderModel));
					bw.newLine();

					//bw.flush();
					isTlogRecord = true;
					orderModel.setTlogPerformed(true);

					OshIntegrationLogger.info("TLOG PERFORMED DONE");
					modelService.save(orderModel);
					modelService.refresh(orderModel);
				}
			}

			if ((creditOrders != null) && !creditOrders.isEmpty())
			{
				for (final OrderModel orderModel : creditOrders)
				{
					final List<CreditPaymentTransactionModel> trans = (List<CreditPaymentTransactionModel>) orderModel
							.getCreditPaymentTransaction();
					if (trans != null && !trans.isEmpty())
					{
						for (final CreditPaymentTransactionModel transEntry : trans)
						{
							if (transEntry.getOshShippingType() != null && !transEntry.isTlogPerformed()
									&& transEntry.getTransactionStatus() != null && !transEntry.getTransactionStatus().isEmpty()
									&& transEntry.getTransactionStatus().toString().equals(OshCoreConstants.ACCEPTED))
							{
								isCreditable = true;
								bw.write(populateRecordTypesService.createHDRRecordForCreditOrder(orderModel));
								bw.newLine();
							}
						}
					}
				}
			}

			if (isTlogRecord || isCreditable)
			{
				bw.write(populateRecordTypesService.createSCRRecords(Orders));
				bw.newLine();
				LOG.info("TLOG PERFORMED GOING TO SEND FILE IN SFTP LOCATION");
				isTlogRecord = true;
			}

		}
		catch (final IOException e)
		{
			e.printStackTrace();
			LOG.error("Tlog FIle Can not be created", e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			OshIntegrationLogger.error("Tlog FIle Can not be created" + e.getMessage());
		}
		finally
		{
			try
			{
				if (bw != null)
				{
					LOG.info("Going to close the files");
					bw.close();
					if (file != null && isTlogRecord)
					{

						sftpTlogFile(file);
						LOG.info("Both Files SFTP to the Transfer box");
						isTlogRecord = false;
						return true;
					}

				}
			}
			catch (final IOException e)
			{
				e.printStackTrace();
				OshIntegrationLogger.error("Tlog FIle Can not be created " + e.getMessage());
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				OshIntegrationLogger.error("Tlog FIle Can not be created " + e.getMessage());
			}

		}
		return false;
	}

	@Override
	public boolean tLogEODFileCreator()
	{
		try
		{
			final String content = "orderNo|orderType|productCode|Quantity|LoyaltyNo|LoyaltyVoucherNumber";
			final String path = Config.getString("tlog.files", "${HYBRIS_DATA_DIR}/Tlog");
			OshIntegrationLogger.info("path is :" + path);
			final Date date = new Date();
			final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			final String currentDate = sdf.format(date);
			final File dir = new File(path + "/" + currentDate + "/tlogEod");
			dir.mkdirs();
			final String fileName = "eodtlog_" + currentDate + ".txt";
			final File file = new File(dir + "/" + fileName + "updating");
			final File finalFile = new File(dir + "/" + fileName);
			// if file doesn't exists, then create it
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			if (finalFile.exists())
			{
				finalFile.delete();
			}

			file.createNewFile();
			bw.write(content + "\n");
			final List<ConsignmentModel> consignmentModels = oshOrderDao.getConsignmentModelsOfNonDropshipAndPending();
			if (consignmentModels.size() > 0)
			{
				for (final ConsignmentModel consignmentModel : consignmentModels)
				{
					final OrderModel orderModel = (OrderModel) consignmentModel.getOrder();
					String loyaltyVoucher = "";
					String customerLoyaltyNo = "";
					final CustomerModel customer = (CustomerModel) orderModel.getUser();
					if (orderModel.getLoyaltyVoucher() != null)
					{
						loyaltyVoucher = orderModel.getLoyaltyVoucher();
					}
					if (customer.getLoyaltyNumber() != null)
					{
						customerLoyaltyNo = customer.getLoyaltyNumber();
					}
					final String orderCode = consignmentModel.getOrder().getCode();
					for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
					{
						final OrderEntryModel orderentry = (OrderEntryModel) consignmentEntry.getOrderEntry();
						final StringBuffer strBuffer = new StringBuffer();
						strBuffer.append(orderCode + "|" + orderentry.getOrderType() + "|" + orderentry.getProduct().getCode() + "|"
								+ orderentry.getQuantity() + "|" + customerLoyaltyNo + "|" + loyaltyVoucher);
						bw.newLine();
						bw.write(strBuffer.toString());
					}
				}
			}
			bw.close();
			file.renameTo(finalFile);
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
	public boolean tLogHourlyFileCreator()
	{
		try
		{
			final Calendar cal = Calendar.getInstance();
			final String content = "orderNo|orderType|productCode|Quantity|LoyaltyNo|LoyaltyVoucherNumber";
			final String path = Config.getString("tlog.files", "${HYBRIS_DATA_DIR}/Tlog");
			OshIntegrationLogger.info("path is :" + path);
			final Date date = new Date();
			final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			final String currentDate = sdf.format(date);
			final File dir = new File(path + "/" + currentDate + "/tlogHourly");
			final String fileName = "tlog_" + currentDate + cal.get(Calendar.HOUR_OF_DAY) + ".txt";
			final File file = new File(dir + "/" + fileName + "updating");
			final File finalFile = new File(dir + "/" + fileName);
			dir.mkdirs();
			// if file doesn't exists, then create it
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			if (finalFile.exists())
			{
				finalFile.delete();
			}
			file.createNewFile();
			OshIntegrationLogger.info("The file is created");
			bw.write(content + "\n");
			final List<ConsignmentModel> consignmentModels = oshOrderDao.getNonDropshipAndPendingConsignmentModelsBetweenAnHour();
			if (consignmentModels.size() > 0)
			{
				for (final ConsignmentModel consignmentModel : consignmentModels)
				{
					final String orderCode = consignmentModel.getOrder().getCode();
					final OrderModel orderModel = (OrderModel) consignmentModel.getOrder();
					String loyaltyVoucher = "";
					String customerLoyaltyNo = "";
					final CustomerModel customer = (CustomerModel) orderModel.getUser();
					if (orderModel.getLoyaltyVoucher() != null)
					{
						loyaltyVoucher = orderModel.getLoyaltyVoucher();
					}
					if (customer.getLoyaltyNumber() != null)
					{
						customerLoyaltyNo = customer.getLoyaltyNumber();
					}
					for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
					{
						final OrderEntryModel orderentry = (OrderEntryModel) consignmentEntry.getOrderEntry();
						final StringBuffer strBuffer = new StringBuffer();
						strBuffer.append(orderCode + "|" + orderentry.getOrderType() + "|" + orderentry.getProduct().getCode() + "|"
								+ orderentry.getQuantity() + "|" + customerLoyaltyNo + "|" + loyaltyVoucher);
						bw.newLine();
						bw.write(strBuffer.toString());
					}
				}
			}
			bw.close();
			file.renameTo(finalFile);
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
	public String[] getEmailIdForFailedTransaction()
	{
		final String transFailedEmail = configurationService.getConfiguration().getString(TRANS_FAILED_EMAIL);
		Assert.notNull(transFailedEmail, "EmailID For Failed Payment Transaction Can Not Be Null");

		final String[] emailIds = transFailedEmail.split(",");
		return emailIds;

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
	 * @return the populateRecordTypesService
	 */
	public PopulateRecordTypesService getPopulateRecordTypesService()
	{
		return populateRecordTypesService;
	}

	/**
	 * @param populateRecordTypesService
	 *           the populateRecordTypesService to set
	 */
	public void setPopulateRecordTypesService(final PopulateRecordTypesService populateRecordTypesService)
	{
		this.populateRecordTypesService = populateRecordTypesService;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.service.TLogFileCreatorService#sftpTlogFile(java.io.File)
	 */
	@Override
	public boolean sftpTlogFile(final File tlogFile)
	{
		Assert.notNull(tlogFile, "Tlog file cannot be null");
		final String sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		final String sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		final String sftpdestPath = configurationService.getConfiguration().getString(TLOG_DESTINATION);
		Assert.notNull(sftpdestPath, "SFTP Destination Path Can Not BE NULL");

		final String poSourceFolder = configurationService.getConfiguration().getString(POSTATUS_FOLDER);
		Assert.notNull(poSourceFolder, "SFTP Destination Path Can Not BE NULL");


		final String privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		final String toLocalFolder = configurationService.getConfiguration().getString(TO_FOLDER);
		Assert.notNull(toLocalFolder, "PO Status Folder Can Not BE NULL");
		//getOshSFTPService().retriveFile(sftpHost, sftpUser, null, privateKey, poSourceFolder, toLocalFolder);

		OshIntegrationLogger.info("SFTP PARAMETERS " + sftpHost + " " + " sftpUser" + " sftpdestPath" + sftpdestPath
				+ " poSourceFolder " + poSourceFolder + " toLocalFolder " + toLocalFolder);
		getOshSFTPService().sendFile(tlogFile, sftpHost, sftpUser, null, privateKey, sftpdestPath);
		return true;
	}

	@Override
	public Date findTlogTriggerDate(final List<TriggerModel> triggers)
	{

		Date triggerDate = null;
		final Calendar cal = Calendar.getInstance();
		if (triggers == null || triggers.isEmpty())
		{

			triggerDate = cal.getTime();
		}
		else
		{
			final TriggerModel trigger = triggers.get(0);
			triggerDate = trigger.getActivationTime();
			final CronJobModel cronJob = trigger.getCronJob();
			if (cronJob.getIntegrationDate() != null)
			{
				cal.setTime(cronJob.getIntegrationDate());
				cal.add(cal.DATE, 1);
				triggerDate = cal.getTime();
			}

			return triggerDate;
		}
		return triggerDate;
	}

}
