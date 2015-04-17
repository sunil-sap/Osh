/**
 * 
 */
package com.hybris.osh.core.populators.loyalty;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.hybris.osh.core.populators.loyalty.ISCLog.Transaction;
import com.hybris.osh.core.populators.loyalty.ISCLog.Transaction.LineItem;
import com.hybris.osh.core.populators.loyalty.ISCLog.Transaction.Shopper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class LoyaltyOrderExportPopulator
{

	private final Logger LOG = Logger.getLogger(LoyaltyOrderExportPopulator.class);

	private static final String SFTPHOSTNAME = "loyalty.sftp.host";
	private static final String SFTPUSERNAME = "loylaty.sftp.user";
	private static final String SFTPPASSWORD = "loylaty.sftup.password";
	private static final String SFTPDESTFOLDER = "loylaty.sftp.destfolder";
	private static final String SFTPLOCALFOLDER = "loylaty.sftp.localfolder";

	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;



	@SuppressWarnings("boxing")
	public void populateOrderResults(final List<OrderModel> list) throws JAXBException
	{
		final File xmlFile = generateXMl(list);
		sendFile(xmlFile);

	}

	@SuppressWarnings("boxing")
	private File generateXMl(final List<OrderModel> list) throws JAXBException
	{
		double orderDiscounts = 0;
		//double orderTotalDiscounts = 0;
		double orderSubtotal = 0;
		double orderTotal = 0;
		final int tenders = 3;
		final ObjectFactory factory = new ObjectFactory();
		final ISCLog iscLog = factory.createISCLog();
		final List<com.hybris.osh.core.populators.loyalty.ISCLog.Transaction> transactionList = iscLog.getTransaction();
		for (final AbstractOrderModel order : list)
		{
			//orderTotalDiscounts = order.getTotalDiscounts();
			final List<DiscountModel> orderDiscountsList = order.getDiscounts();

			for (int i = 0; i < orderDiscountsList.size(); i++)
			{
				final DiscountModel orderDiscount = orderDiscountsList.get(i);
				orderDiscounts = orderDiscounts + orderDiscount.getValue();
			}

			final Transaction transaction = factory.createISCLogTransaction();

			transaction.setTransactionID(order.getCode());
			transaction.setReceiptDateTime(new Date());

			final Shopper shopper = factory.createISCLogTransactionShopper();
			final CustomerModel customer = (CustomerModel) order.getUser();
			shopper.setShopperID(customer.getLoyaltyNumber());
			transaction.setShopper(shopper);

			final List<LineItem> lineItemList = transaction.getLineItem();
			final List<AbstractOrderEntryModel> entries = order.getEntries();

			for (int i = 0; i < entries.size(); i++)
			{
				final AbstractOrderEntryModel entry = entries.get(i);
				final LineItem lineItem = factory.createISCLogTransactionLineItem();

				lineItem.setSale(factory.createISCLogTransactionLineItemSale());
				final LineItem.Sale sale = lineItem.getSale();
				sale.setItemSKU(entry.getProduct().getCode());
				sale.setQuantity(entry.getQuantity());
				sale.setLineNumber(i + 1);
				sale.setExtendedAmount(entry.getBasePrice());
				lineItemList.add(lineItem);
			}

			for (int i = 1; i <= tenders; i++)
			{
				orderSubtotal = order.getSubtotal();
				orderTotal = order.getTotalPrice();
				final LineItem lineItem = factory.createISCLogTransactionLineItem();
				lineItem.setTender(factory.createISCLogTransactionLineItemTender());
				final LineItem.Tender tender = lineItem.getTender();
				if (i == 1)
				{
					tender.setTenderType("4");
					tender.setAmount(orderTotal);
				}
				if (i == 2)
				{
					tender.setTenderType("97");
					tender.setAmount(orderSubtotal - orderDiscounts);
				}
				if (i == 3)
				{
					tender.setTenderType("99");
					tender.setAmount(order.getTotalPrice());
				}

				lineItemList.add(lineItem);
			}

			transactionList.add(transaction);

		}
		final JAXBContext jaxbContext = JAXBContext.newInstance(ISCLog.class);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		final StringWriter sw = new StringWriter();
		marshaller.marshal(iscLog, sw);
		final Date date = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		final String formattedDate = sdf.format(date);
		final String fileLocation = configurationService.getConfiguration().getString(SFTPLOCALFOLDER);
		final File file = new File(fileLocation + "\\OSH_transactions_" + formattedDate + ".xml");
		marshaller.marshal(iscLog, file);
		sendFile(file);
		LOG.info("*****Loyalty Export File" + file.getName() + "generated*****");
		return file;
	}

	private void sendFile(final File file)
	{
		LOG.info("*** Sending File****");
		final String SFTPHOST = configurationService.getConfiguration().getString(SFTPHOSTNAME);
		final int SFTPPORT = 22;
		final String SFTPUSER = configurationService.getConfiguration().getString(SFTPUSERNAME);
		final String SFTPPASS = configurationService.getConfiguration().getString(SFTPPASSWORD);
		final String SFTPWORKINGDIR = configurationService.getConfiguration().getString(SFTPDESTFOLDER);
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
			session.setPassword(SFTPPASS);
			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LOG.info("***Loyalty SFTP Connected****");
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(SFTPWORKINGDIR);
			channelSftp.put(new FileInputStream(file), file.getName());
			LOG.info("***FILE : " + file.getName() + " Sent");
			channelSftp.disconnect();
			LOG.info(" Loylaty SFTP Connected" + channelSftp.isConnected());
			channelSftp.exit();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
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
