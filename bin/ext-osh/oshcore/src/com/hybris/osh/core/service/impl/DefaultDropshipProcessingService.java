package com.hybris.osh.core.service.impl;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.hybris.osh.core.dao.OshOrderDao;
import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.core.logger.OshIntegrationLogger;
import com.hybris.osh.core.service.DropshipProcessingService;
import com.hybris.osh.core.service.OshSFTPService;


/**
 * implementation class of DropshipProcessingService
 * 
 */
public class DefaultDropshipProcessingService implements DropshipProcessingService
{
	private BusinessProcessService businessProcessService;

	private OshOrderDao oshOrderDao;
	private ModelService modelService;
	public static OrderProcessModel cancelOrderProcessModel = null;
	public static ConsignmentProcessModel shippedOrderProcessModel = null;

	@Resource
	private ConfigurationService configurationService;


	@Resource
	private OshSFTPService oshSFTPService;

	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_PORT = "tlog.file.sftp.port";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";
	private static final String TO_FOLDER = "polling.file.sftp.to.folder";
	private static final String POSTATUS_FOLDER = "po.file.sftp.postatus.destination";
	private static final String LOCAL_POSTATUS_FOLDER = "polling.file.sftp.postatus.folder";
	private static final String ARCHIVE_FOLDER = "pom.sftp.file.archive.folder";
	private static final String POSTAUS_FILE_NAME = "po.file.sftp.postatus.file.name";
	private static final String FILE_EXT = "po.file.sftp.postatus.file.ext";
	public static String sftpHost = null;
	public static String sftpUser = null;
	public static String poSourceFolder = null;
	public static String poLocalStatusFolder = null;
	public static String privateKey = null;
	public static String archiveFolderPath = null;
	public static OrderCancelRecordModel orderModifiedRecord = null;
	public static ConsignmentEntryModel consignmentEntry = null;
	public static Collection<OrderEntryModificationRecordEntryModel> entryCancelEntry = null;
	public static Collection<OrderModificationRecordEntryModel> cancelEntry = null;
	public static OrderCancelRecordEntryModel orderCancelRecordEntry = null;

	@Override
	public Map<Integer, List<String>> dropshipFeedReader(final List<File> poFiles)
	{
		final Map<Integer, List<String>> poStatusData = new HashMap<Integer, List<String>>();
		int poDataCount = 0;
		try
		{
			String sCurrentLine;
			for (final File file : poFiles)
			{
				if (!file.getName().contains("control_"))
				{
					BufferedReader br = null;
					br = new BufferedReader(new FileReader(file));
					List<String> recordData = new ArrayList<String>();

					boolean firstHDRCheck = false;
					while ((sCurrentLine = br.readLine()) != null)
					{
						final String[] data = sCurrentLine.split("\\|");

						if (data[0].equals("HDR") && firstHDRCheck)
						{
							poStatusData.put(poDataCount, recordData);
							recordData = new ArrayList<String>();
							poDataCount++;
						}

						if (data[0] != null && (data[0].contains("HDR") || data[0].contains("SKU")))
						{
							recordData.add(sCurrentLine.trim());
							firstHDRCheck = true;
						}

					}
					if ((sCurrentLine = br.readLine()) == null)
					{
						poStatusData.put(poDataCount, recordData);
						poDataCount++;
						br.close();
					}
				}
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		return poStatusData;
	}

	@Override
	public void dropshipSettlementProcessTrigger(final Map<String, String> orderNoAndTrackingIdMap)
	{
		for (final Map.Entry<String, String> entry : orderNoAndTrackingIdMap.entrySet())
		{
			final OrderModel orderModel = getOshOrderDao().getOrderFromOrderCode(entry.getKey());

			for (final ConsignmentModel consignmentModel : orderModel.getConsignments())
			{
				if (consignmentModel.getCode().contains("dropship-"))
				{
					/**
					 * Assuming that all the entries of this dropship consignment will be going for the transaction we are
					 * making the status of all the consignment entries as Shipped and applying the same tracking id on all
					 * the entries as all of them will be going in same pacakge.
					 */
					for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
					{
						consignmentEntry.setEntryLevelStatus(ConsignmentEntryStatus.SHIPPED);
						consignmentEntry.setTrackingId(entry.getValue());
						modelService.save(consignmentEntry);
						modelService.refresh(consignmentEntry);
					}
					/**
					 * 
					 * Triggering consignment fulfillment process which is used for doing the settlement on the consignment.
					 */
					final ConsignmentProcessModel subProcess = (ConsignmentProcessModel) businessProcessService.createProcess(
							orderModel.getCode() + Math.random() + "_", "consignmentFulfilmentSubprocess");
					subProcess.setConsignment(consignmentModel);
					modelService.save(subProcess);
					businessProcessService.startProcess(subProcess);
				}
			}


		}

	}

	@Override
	public void getDropshipModifiedOrder(final Map<Integer, List<String>> PoStatusData)
	{
		Assert.notEmpty(PoStatusData, "No Eligible PO-Status file present");
		OrderModel order = null;
		for (final Entry<Integer, List<String>> poData : PoStatusData.entrySet())
		{
			final String hdrRecord = poData.getValue().get(0);
			final String[] hdrData = hdrRecord.split("\\|");
			if (hdrData[1] != null & !hdrData[1].isEmpty())
			{
				final String orderCode = String.format("%08d", Long.valueOf(hdrData[1]));
				order = oshOrderDao.getOrderFromOrderCode(orderCode);
			}

			if (order != null)
			{
				populateOrderWithStatusRecord(order, poData.getValue());
				OshIntegrationLogger.info("Dropship Consignment Updated Successfully");
			}
			else
			{
				OshIntegrationLogger.error("No Dropship Order Found");
			}
		}

	}

	@Override
	public void populateOrderWithStatusRecord(final AbstractOrderModel order, final List<String> statusData)
	{
		Assert.notEmpty(statusData, "Po Status Data for order can not be empty");
		//final String[] hdrData = statusData.get(0).split("\\|");
		//populateHDRRecord(hdrData, order);
		String status = null;
		orderModifiedRecord = modelService.create(OrderCancelRecordModel.class);
		entryCancelEntry = new HashSet<OrderEntryModificationRecordEntryModel>();
		orderCancelRecordEntry = modelService.create(OrderCancelRecordEntryModel.class);
		cancelEntry = new HashSet<OrderModificationRecordEntryModel>();
		for (final String data : statusData)
		{
			final String[] poStatusData = data.split("\\|");
			if (poStatusData[0].equalsIgnoreCase("HDR"))
			{
				populateHDRRecord(poStatusData, order);
			}
			else
			{
				status = populateSKURecord(poStatusData, order);
			}

		}
		/*
		 * if (status != null && status.equals("SHIPPED")) {
		 * businessProcessService.startProcess(shippedOrderProcessModel); }
		 */
		if (status != null && status.equals("CANCELLED"))
		{
			businessProcessService.startProcess(cancelOrderProcessModel);
		}
	}

	public void populateHDRRecord(final String[] hdrData, final AbstractOrderModel order)
	{
		Assert.notEmpty(hdrData);
		order.setChargeCreditCard(true);
		order.setExternalOrderId(hdrData[9]);
		modelService.save(order);
		modelService.refresh(order);
	}

	public String populateSKURecord(final String[] skuData, final AbstractOrderModel order)
	{
		Assert.notEmpty(skuData);
		Date date = null;
		final Set<ConsignmentModel> consignment = order.getConsignments();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		for (final ConsignmentModel consignmentModel : consignment)
		{
			boolean isShipped = false;
			boolean isCancelled = false;
			if (consignmentModel.getCode().contains("dropship-") && skuData[0].equalsIgnoreCase("SKU"))
			{
				for (final ConsignmentEntryModel entry : consignmentModel.getConsignmentEntries())
				{
					final ProductModel product = entry.getOrderEntry().getProduct();
					if (product.getCode().equals(skuData[3]))
					{
						if (skuData[5] != null && !skuData[5].isEmpty())
						{
							if (skuData[5].equals("4"))
							{
								order.setTlogPerformed(false);
								entry.setEntryLevelStatus(ConsignmentEntryStatus.SHIPPED);
								isShipped = true;
							}
							if (skuData[5].equals("5"))
							{
								consignmentEntry = entry;
								entry.setEntryLevelStatus(ConsignmentEntryStatus.CANCELLED);
								isCancelled = true;
							}
						}
						if (skuData[8] != null && !skuData[8].isEmpty())
						{
							entry.setShippedQuantity(Long.parseLong(skuData[8]));
						}
						if (skuData[9] != null && !skuData[9].isEmpty())
						{
							entry.setCancelQty(Integer.parseInt(skuData[9]));
						}
						if (skuData[10] != null && !skuData[10].isEmpty())
						{
							entry.setTrackingId(skuData[10]);
						}

					}
					modelService.save(entry);
					modelService.refresh(entry);
				}

				try
				{
					if (skuData[6] != null && !skuData[6].isEmpty())
					{
						date = dateFormat.parse(skuData[6]);
						consignmentModel.setShippingDate(date);
					}
					if (skuData[7] != null && !skuData[7].isEmpty())
					{
						date = dateFormat.parse(skuData[7]);

						order.setCancelDate(date);
					}
					if (skuData[10] != null && !skuData[10].isEmpty())
					{
						if (consignmentModel.getTrackingID() != null && !consignmentModel.getTrackingID().isEmpty())
						{
							final String trakingId = consignmentModel.getTrackingID();
							final StringBuilder sb = new StringBuilder();
							sb.append(trakingId + "\n" + skuData[10]);
							consignmentModel.setTrackingID(sb.toString());
						}
						else
						{
							consignmentModel.setTrackingID(skuData[10]);
						}
					}

				}

				catch (final ParseException e)
				{
					OshIntegrationLogger.error("Parce Exception" + e.getMessage());
				}
				modelService.save(consignmentModel);
				modelService.refresh(consignmentModel);
				modelService.save(order);
				modelService.refresh(order);
				if (isShipped)
				{
					final List<ConsignmentProcessModel> processes = oshOrderDao.getProcessForConsignment(consignmentModel);
					if (processes != null && processes.isEmpty())
					{
						OshIntegrationLogger.info("Process will take few time to complete the process");
						shippedOrderProcessModel = (ConsignmentProcessModel) businessProcessService.createProcess(order.getCode()
								+ Math.random() + "_", "consignmentFulfilmentSubprocess");
						shippedOrderProcessModel.setConsignment(consignmentModel);

						modelService.save(shippedOrderProcessModel);
						businessProcessService.startProcess(shippedOrderProcessModel);
					}
				}
				if (isCancelled)
				{
					createCancelRecord(consignmentModel);
					OshIntegrationLogger.info("Process will take some time to complete the process");
					cancelOrderProcessModel = (OrderProcessModel) businessProcessService.createProcess("orderCancellationEmailProcess"
							+ System.currentTimeMillis(), "orderCancellationEmailProcess");
					cancelOrderProcessModel.setOrder((OrderModel) order);
					//businessProcessService.startProcess(cancelOrderProcessModel);
					return "CANCELLED";
				}

			}
		}
		return null;


	}

	public void createCancelRecord(final ConsignmentModel consignment)
	{

		//	final OrderCancelRecord orderCancelRecord = modelService.create(OrderCancelRecord.class);

		for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
		{
			if (entry.getCancelQty() != null && consignmentEntry != null && entry.equals(consignmentEntry))
			{
				final OrderHistoryEntryModel orderHistory = modelService.create(OrderHistoryEntryModel.class);
				final Calendar calender = Calendar.getInstance();

				final OrderEntryCancelRecordEntryModel orderEntryCancelRecordEntry = modelService
						.create(OrderEntryCancelRecordEntryModel.class);


				final OrderModel order = (OrderModel) consignment.getOrder();

				if (order.getModificationRecords() != null && !order.getModificationRecords().isEmpty())
				{
					orderModifiedRecord = (OrderCancelRecordModel) order.getModificationRecords().iterator().next();
					final Set<OrderModificationRecordEntryModel> orderModifiedEntries = new HashSet<OrderModificationRecordEntryModel>(
							orderModifiedRecord.getModificationRecordEntries());
					cancelEntry = orderModifiedEntries;

				}

				orderModifiedRecord.setOrder((OrderModel) entry.getOrderEntry().getOrder());

				orderHistory.setDescription("Product " + entry.getOrderEntry().getProduct().getCode() + " with quantity "
						+ entry.getOrderEntry().getQuantity() + " cancelled for order: " + entry.getOrderEntry().getOrder().getCode());

				orderHistory.setTimestamp(new Date());
				if (orderHistory.getOrder() == null)
				{
					orderHistory.setOrder((OrderModel) entry.getOrderEntry().getOrder());

				}
				modelService.save(orderHistory);
				modelService.refresh(orderHistory);
				/*
				 * final Long reqCancelQty = entry.getQuantity() - entry.getCancelQty();
				 * 
				 * if (reqCancelQty.longValue() <= 0L) {
				 * orderCancelRecordEntry.setCancelResult(OrderCancelEntryStatus.FULL); } else {
				 */
				orderCancelRecordEntry.setCancelResult(OrderCancelEntryStatus.PARTIAL);
				/* } */
				orderCancelRecordEntry.setCancelReason(CancelReason.OTHER);
				orderCancelRecordEntry.setCode(calender.getTimeInMillis() + "_" + entry.getOrderEntry().getOrder().getCode());

				if (entry.getOrderEntry().getQuantity().intValue() >= entry.getCancelQty().intValue())
				{
					orderEntryCancelRecordEntry.setCancelRequestQuantity(entry.getCancelQty());
				}
				else
				{
					orderEntryCancelRecordEntry.setCancelRequestQuantity(Integer.valueOf(0));
				}

				orderEntryCancelRecordEntry.setCancelReason(CancelReason.OTHER);
				orderEntryCancelRecordEntry.setOrderEntry((OrderEntryModel) entry.getOrderEntry());
				orderEntryCancelRecordEntry.setOriginalOrderEntry((OrderEntryModel) entry.getOrderEntry());
				orderEntryCancelRecordEntry.setCode(entry.getOrderEntry().getPk() + "_" + calender.getTimeInMillis());
				orderEntryCancelRecordEntry.setModificationRecordEntry(orderCancelRecordEntry);


				//final Collection<OrderEntryModificationRecordEntryModel> entryCancelEntry = new HashSet<OrderEntryModificationRecordEntryModel>();

				entryCancelEntry.add(orderEntryCancelRecordEntry);
				orderCancelRecordEntry.setOrderEntriesModificationEntries(entryCancelEntry);

				orderCancelRecordEntry.setModificationRecord(orderModifiedRecord);
				if (orderCancelRecordEntry.getTimestamp() == null)
				{
					orderCancelRecordEntry.setTimestamp(new Date());
				}
				orderCancelRecordEntry.setStatus(OrderModificationEntryStatus.INPROGRESS);
				if (orderCancelRecordEntry.getOriginalVersion() == null)
				{
					orderCancelRecordEntry.setOriginalVersion(orderHistory);
				}
				modelService.save(orderCancelRecordEntry);
				modelService.refresh(orderCancelRecordEntry);

				//need to remove
				orderEntryCancelRecordEntry.setShippingAmt(Double.valueOf(0));

				modelService.save(orderEntryCancelRecordEntry);
				modelService.refresh(orderEntryCancelRecordEntry);


				cancelEntry.add(orderCancelRecordEntry);
				orderModifiedRecord.setModificationRecordEntries(cancelEntry);
				modelService.save(orderModifiedRecord);
				modelService.refresh(orderModifiedRecord);
			}
		}
	}

	public void recursiveDelete(final File file)
	{
		if (!file.exists())
		{
			return;
		}
		if (file.isDirectory())
		{
			for (final File f : file.listFiles())
			{
				recursiveDelete(f);
			}
			file.delete();
		}
		else
		{
			file.delete();
		}
	}

	public OshOrderDao getOshOrderDao()
	{
		return oshOrderDao;
	}

	@Override
	public boolean retrivePoStatusFiles()
	{
		sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		poSourceFolder = configurationService.getConfiguration().getString(POSTATUS_FOLDER);
		Assert.notNull(poSourceFolder, "SFTP Destination Path Can Not BE NULL");

		poLocalStatusFolder = configurationService.getConfiguration().getString(LOCAL_POSTATUS_FOLDER);
		Assert.notNull(poLocalStatusFolder, "PO Status Folder Can Not BE NULL");

		privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		archiveFolderPath = configurationService.getConfiguration().getString(ARCHIVE_FOLDER);
		Assert.notNull(archiveFolderPath, "SFTP Private Key Can Not BE NULL");

		final String toLocalFolder = configurationService.getConfiguration().getString(TO_FOLDER);
		Assert.notNull(toLocalFolder, "PO Status Folder Can Not BE NULL");

		final String file = configurationService.getConfiguration().getString(POSTAUS_FILE_NAME);
		Assert.notNull(file, "PO Status File Name Cannot Be Null");

		final String extenstion = configurationService.getConfiguration().getString(FILE_EXT);
		Assert.notNull(file, "PO Status File Extenstion Cannot Be Null");

		final File poFolder = new File(poLocalStatusFolder);
		recursiveDelete(poFolder);

		final File pomFolder = new File(toLocalFolder);

		if (!pomFolder.exists())
		{
			pomFolder.mkdir();
		}
		//poFolder.mkdir();
		final StringBuilder poStatusFileName = new StringBuilder(file);
		final StringBuilder archivePath = new StringBuilder(archiveFolderPath);

		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String currentDate = sdf.format(calendar.getTime());

		archivePath.append(calendar.getTimeInMillis());
		poStatusFileName.append("_" + currentDate);
		poStatusFileName.append(extenstion);

		return oshSFTPService.retriveFile(sftpHost, sftpUser, null, privateKey, poSourceFolder, toLocalFolder,
				archivePath.toString(), poLocalStatusFolder);

	}

	@Override
	public List<File> getPoStatusFiles()
	{
		final String poStatusFolder = configurationService.getConfiguration().getString(LOCAL_POSTATUS_FOLDER);
		Assert.notNull(poStatusFolder, "PO Status Folder Can Not BE NULL");

		final File[] files = new File(poStatusFolder).listFiles();
		final StringBuilder fileName = new StringBuilder("po_status_");
		final List<File> result = findCompletlyCopiedFiles(files, fileName.toString());

		oshSFTPService.archiveFoldersFile(sftpHost, sftpUser, null, privateKey, poLocalStatusFolder, result, archiveFolderPath,
				poSourceFolder);
		return result;
	}

	public List<File> findCompletlyCopiedFiles(final File[] files, final String fileName)
	{
		final List<File> controlFiles = new ArrayList<File>();
		final List<File> results = new ArrayList<File>();
		final List<File> removeFile = new ArrayList<File>();
		for (final File file : files)
		{
			if (file.isFile() && (file.getName().endsWith(".dat")))
			{
				if (file.getName().startsWith("control_"))
				{
					controlFiles.add(file);
				}
				results.add(file);
			}
		}
		for (final File file : controlFiles)
		{
			final String[] filename = file.getName().split(".dat");
			final String[] controlfile = filename[0].split("control_");
			final String controlnum = controlfile[1];
			final String fileNamingFormat = fileName + controlnum;

			for (final File result : results)
			{
				if (result.getName().startsWith(fileNamingFormat))
				{
					removeFile.add(result);
				}
			}
			removeFile.add(file);
		}
		/*
		 * for (final File file : removeFile) { results.remove(file); }
		 */
		return removeFile;
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
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}
