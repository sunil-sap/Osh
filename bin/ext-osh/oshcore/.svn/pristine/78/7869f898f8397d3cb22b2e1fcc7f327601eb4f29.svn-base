/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.dao.FindMediaContainerDao;
import com.hybris.osh.core.service.CreateMediaImpexService;
import com.hybris.osh.core.service.OshSFTPService;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;


public class DefaultCreateMediaImpexService implements CreateMediaImpexService
{
	private final Logger Log = Logger.getLogger(DefaultCreateMediaImpexService.class);

	private String importHeader;
	private String mediaHeader;
	private String mediaContHeader;
	private String productHeaderForAltCont;
	private String productHeaderForExistingCont;
	private ImportService importService;
	private String productHeader;
	private String removeOriginalImageHeader;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	private FindMediaContainerDao findMediaContainerDao;
	@Resource(name = "oshSFTPService")
	private OshSFTPService oshSFTPService;
	private ProductDao productDao;

	private static final String SFTPSOURCE = "cheetah.mail.sftp.sourcefolder";
	private static final String MEDIAPATH = "media.sftp.sourcefolder";
	private static final String MEDIA_IMPEX_NAME = "imagemagick.media.impex.name";
	private static final String PRODUCT_IMPEX_NAME = "imagemagick.product.media.impex.name";
	private static final String TRANSFER_IMAGES_PATH = "image.magick.transfer.files.path";
	private static final String SFTP_USER = "polling.file.sftp.user";
	private static final String SFTP_PORT = "tlog.file.sftp.port";
	private static final String SFTP_HOST = "polling.file.sftp.host";
	private static final String SFTP_KEY = "polling.file.sftp.key.path";
	private static final String ARCHIVE_PATH = "image.magick.transfer.files.archive.path";
	private static final String TO_LOCAL_IMAGES = "image.magick.media.sftp.sourcefolder";
	private static final String REMOVE_MEDIA_FILE = "imagemagick.remove.original.media.name";
	private static final String TRANSFER_ARCHIVE_PATH = "pom.sftp.file.archive.folder";

	public static long creationTime;
	public static String productMediaPath;

	public boolean importImpexFile(final File file)
	{
		try
		{
			final ImportResult result = getImportService().importData(
					new StreamBasedImpExResource(new FileInputStream(file.getPath()), "UTF-8"));
			if (result.isFinished())
			{
				if (result.isSuccessful())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (final FileNotFoundException e)
		{
			Log.info(file.getPath() + " File Import Fail", e);
			return false;
		}
	}

	@Override
	public boolean creatMediaImpex()
	{
		final Date date = new Date();
		final Calendar calendar = Calendar.getInstance();
		creationTime = calendar.getTimeInMillis();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String formattedDate = sdf.format(date);
		final File file;
		final List<File> files = createListOfFiles();
		if ((files != null) && (!files.isEmpty()))
		{
			try
			{
				final String sftpSource = configurationService.getConfiguration().getString(SFTPSOURCE);
				final File parentDir = new File(sftpSource + "\\" + formattedDate);
				parentDir.mkdirs();
				final String mediaFile = configurationService.getConfiguration().getString(MEDIA_IMPEX_NAME);
				String fileName = mediaFile + "_" + formattedDate + OshCoreConstants.IMPEX_EXT;

				file = new File(parentDir, fileName);

				final BufferedWriter outputFile = new BufferedWriter(new FileWriter(file));
				file.createNewFile();
				outputFile.append(getImportHeader());
				outputFile.append(getMediaHeader());
				for (final File image : files)
				{
					final String[] imageName = image.getName().split(OshCoreConstants.JPG_EXT);
					outputFile.append(";96Wx96H;B-");
					outputFile.append(imageName[0] + "_" + creationTime + ";");
					outputFile.append(OshCoreConstants.IMPORT_FILE + image.getPath() + ";;;" + OshCoreConstants.IMAGES_FOLDER);
					outputFile.newLine();
				}

				outputFile.append(getMediaContHeader());
				for (final File image : files)
				{
					final String[] imageName = image.getName().split(OshCoreConstants.JPG_EXT);
					outputFile.append(";B-" + imageName[0] + ";B-");
					outputFile.append(imageName[0] + "_" + creationTime + ";");
					Log.info(image + " associating with media group");

					try
					{
						final JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(new FileInputStream(image));
						final BufferedImage bimg = jpegDecoder.decodeAsBufferedImage();
						final int width = bimg.getWidth();

						if (width >= 515)
						{
							outputFile.append(";ImageMagickConversionGroup");
						}
						else
						{
							outputFile.append(";LessThan515x515Group");
						}

						outputFile.newLine();
					}
					catch (final ImageFormatException e)
					{
						Log.info("Image Format Not Supported");
						outputFile.append(";ImageMagickConversionGroup");
						outputFile.newLine();
					}
					catch (final Exception e)
					{
						Log.info("Image Format Not Supported");
						outputFile.append(";ImageMagickConversionGroup");
						outputFile.newLine();
					}

				}
				outputFile.close();

				final String productMedia = configurationService.getConfiguration().getString(PRODUCT_IMPEX_NAME);
				fileName = productMedia + "_" + formattedDate + OshCoreConstants.IMPEX_EXT;

				final File productFile = new File(parentDir, fileName);
				createProductsMediaImpex(productFile);
			}

			catch (final IOException e)
			{
				Log.error("Fail To Create Impex", e);
				return false;
			}

			return importImpexFile(file);
		}
		return false;

	}

	@Override
	public boolean createProductsMediaImpex(final File file) throws IOException
	{
		final LinkedHashMap<String, List<String>> listsOfContainers = sortMediaContainers();
		List<String> mediaContainer;
		final BufferedWriter output = new BufferedWriter(new FileWriter(file));
		file.createNewFile();
		productMediaPath = file.getPath();
		output.append(getImportHeader());

		try
		{
			if (!listsOfContainers.isEmpty())
			{
				for (final Entry<String, List<String>> mediaContainers : listsOfContainers.entrySet())
				{
					if (mediaContainers.getKey().contains(OshCoreConstants.NEW_MEDIA_CONTAINER)
							&& (!mediaContainers.getValue().isEmpty()))
					{
						output.append(getProductHeader());
						output.newLine();
						mediaContainer = mediaContainers.getValue();
						for (final String productName : mediaContainer)
						{
							output.append(";" + productName + ";");
							output.append(productName + "_" + creationTime + "_" + OshCoreConstants.PICTURE_SIZE + ";");
							output.append(productName + "_" + creationTime + "_" + OshCoreConstants.THUMBNAIL_SIZE + ";");
							output.append(productName + ";");
							output.newLine();
						}
					}
					if (mediaContainers.getKey().contains(OshCoreConstants.EXISTING_MEDIA_CONTAINER)
							&& (!mediaContainers.getValue().isEmpty()))
					{
						output.append(getProductHeaderForExistingCont());
						output.newLine();
						mediaContainer = mediaContainers.getValue();
						for (final String productName : mediaContainer)
						{
							output.append(";" + productName + ";");
							output.append(productName + "_" + creationTime + "_" + OshCoreConstants.PICTURE_SIZE + ";");
							output.append(productName + "_" + creationTime + "_" + OshCoreConstants.THUMBNAIL_SIZE + ";");
							output.newLine();
						}
					}
					if (mediaContainers.getKey().contains(OshCoreConstants.NEW_ALT_MEDIA_CONTAINER)
							&& (!mediaContainers.getValue().isEmpty()))
					{
						output.append(getProductHeaderForAltCont());
						output.newLine();
						mediaContainer = mediaContainers.getValue();
						for (final String productName : mediaContainer)
						{
							final String[] productNames = productName.split(OshCoreConstants.ALT_IMAGES);

							output.append(";" + productNames[0] + ";");
							output.append(productName + ";");

							output.newLine();
						}
					}
				}

				output.close();
			}
		}
		catch (final IOException e)
		{
			Log.error("Fail To Create Product Impex", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean loadProductMediaImpex()
	{
		try
		{
			if ((productMediaPath != null) && (!productMediaPath.isEmpty()))
			{
				final ImportResult result = getImportService().importData(
						new StreamBasedImpExResource(new FileInputStream(productMediaPath), "UTF-8"));
				if (result.isFinished())
				{
					if (result.isSuccessful())
					{

						Log.info("Product Impex Is Successfully Imported");

					}
					else
					{
						Log.info("Product Impex Is Not Successfully Imported");
					}
				}
				else
				{
					Log.info("Product Impex Is Loading");
				}

			}
		}
		catch (final FileNotFoundException e)
		{
			Log.info(productMediaPath + " File Import Fail", e);
			return false;
		}
		return true;

	}

	@Override
	public LinkedHashMap<String, List<String>> sortMediaContainers()
	{
		final LinkedHashMap<String, List<String>> mapOfMediaContainers = new LinkedHashMap<String, List<String>>();
		final ArrayList<String> existingMediaCont = new ArrayList<String>();
		final ArrayList<String> newMediaCont = new ArrayList<String>();
		final ArrayList<String> newAltMediaCont = new ArrayList<String>();
		final ArrayList<String> newContainer = createListOfImages();

		final HashMap<String, Boolean> presentContr = findPresentMediaContainer(newContainer);

		for (final String newImage : newContainer)
		{
			if (presentContr.containsKey(newImage) && (presentContr.get(newImage).booleanValue()))
			{
				if (!newImage.contains(OshCoreConstants.ALT_IMAGES))
				{
					existingMediaCont.add(newImage);
				}
			}
			else
			{
				if (newImage.contains(OshCoreConstants.ALT_IMAGES))
				{
					newAltMediaCont.add(newImage);
				}
				else
				{
					newMediaCont.add(newImage);
				}
			}
		}
		mapOfMediaContainers.put(OshCoreConstants.NEW_MEDIA_CONTAINER, newMediaCont);
		mapOfMediaContainers.put(OshCoreConstants.EXISTING_MEDIA_CONTAINER, existingMediaCont);
		mapOfMediaContainers.put(OshCoreConstants.NEW_ALT_MEDIA_CONTAINER, newAltMediaCont);

		return mapOfMediaContainers;
	}

	@Override
	public List<File> createListOfFiles()
	{
		final List<File> results = new ArrayList<File>();

		final String mediaSource = configurationService.getConfiguration().getString(MEDIAPATH);
		final File[] files = new File(mediaSource).listFiles();
		if (files != null)
		{
			for (final File file : files)
			{
				if (file.isFile() && (file.getName().endsWith(OshCoreConstants.JPG_EXT)))
				{
					results.add(file);
				}
				else
				{
					Log.info("Images are not placed in this location:- " + mediaSource);
				}
			}
		}
		return results;
	}

	public HashMap<String, Boolean> findPresentMediaContainer(final ArrayList<String> newContainer)
	{
		final HashMap<String, Boolean> ContainerMap = new HashMap<String, Boolean>();
		final List<MediaContainerModel> presentContainer = getFindMediaContainerDao().findMediaContainer();
		if ((!newContainer.isEmpty()) && (!presentContainer.isEmpty()))
		{
			for (final MediaContainerModel presentCont : presentContainer)
			{
				if (newContainer.contains(presentCont.getQualifier()))
				{
					ContainerMap.put(presentCont.getQualifier(), Boolean.TRUE);
				}
				else
				{
					ContainerMap.put(presentCont.getQualifier(), Boolean.FALSE);
				}
			}
		}
		return ContainerMap;
	}

	@Override
	public boolean moveMedias()
	{
		try
		{
			final String mediaSource = configurationService.getConfiguration().getString(MEDIAPATH);
			final File oldFolder = new File(mediaSource);
			//to delete folder after doing and image magick
			if (oldFolder.exists())
			{
				Log.info("Going to delete Images");
				recursiveDelete(oldFolder);
			}

			//below is the code to archive the images

			/*
			 * final File newFile = new File(mediaSource + "_" + creationTime); oldFolder.renameTo(newFile);
			 */

			oldFolder.mkdirs();
		}
		catch (final Exception e)
		{
			Log.info("Resized Image Cannot be moved", e);
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<String> createListOfImages()
	{
		final String mediaSource = configurationService.getConfiguration().getString(MEDIAPATH);
		final ArrayList<String> results = new ArrayList<String>();
		final File[] files = new File(mediaSource).listFiles();
		for (final File file : files)
		{
			if (file.isFile() && (file.getName().endsWith(OshCoreConstants.JPG_EXT)))
			{
				final String[] fileName = file.getName().split(OshCoreConstants.JPG_EXT);
				results.add("B-" + fileName[0]);
			}
		}
		return results;
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


	@Override
	public boolean copyImagesInImageMagickPath()
	{

		final String mediaPath = configurationService.getConfiguration().getString(MEDIAPATH);
		Assert.notNull(mediaPath, "Image magick Media Path Folder Can Not BE NULL");

		final File file = new File(mediaPath);
		if (file.exists())
		{
			recursiveDelete(file);
		}

		final String sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		final String sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		final String transferImageFolder = configurationService.getConfiguration().getString(TRANSFER_IMAGES_PATH);
		Assert.notNull(transferImageFolder, "Transfer Box Image Path Can Not BE NULL");


		final String archivePath = configurationService.getConfiguration().getString(ARCHIVE_PATH);
		Assert.notNull(archivePath, "Archive Path Can Not BE NULL");


		final String toLocalImagePath = configurationService.getConfiguration().getString(TO_LOCAL_IMAGES);
		Assert.notNull(toLocalImagePath, "Archive Path Can Not BE NULL");

		final String privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");
		return oshSFTPService.retriveFile(sftpHost, sftpUser, null, privateKey, transferImageFolder, toLocalImagePath, archivePath,
				mediaPath);
	}

	@Override
	public boolean removeOriginalImagesFromSftp()
	{
		final String sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		final String sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		final String transferImageFolder = configurationService.getConfiguration().getString(TRANSFER_IMAGES_PATH);
		Assert.notNull(transferImageFolder, "Transfer Box Image Path Can Not BE NULL");

		final String toLocalImagePath = configurationService.getConfiguration().getString(TO_LOCAL_IMAGES);
		Assert.notNull(toLocalImagePath, "Archive Path Can Not BE NULL");

		final String privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		return oshSFTPService.removeOriginalImages(sftpHost, sftpUser, null, privateKey, transferImageFolder);
	}

	@Override
	public boolean removeArchiveFoldersFromSftp()
	{
		final String sftpHost = configurationService.getConfiguration().getString(SFTP_HOST);
		Assert.notNull(sftpHost, "SFTP Host Can Not BE NULL");

		final String sftpUser = configurationService.getConfiguration().getString(SFTP_USER);
		Assert.notNull(sftpUser, "SFTP User Can Not BE NULL");

		final String transferArchiveFolder = configurationService.getConfiguration().getString(TRANSFER_ARCHIVE_PATH);
		Assert.notNull(transferArchiveFolder, "Transfer Box Image Path Can Not BE NULL");

		final String privateKey = configurationService.getConfiguration().getString(SFTP_KEY);
		Assert.notNull(privateKey, "SFTP Private Key Can Not BE NULL");

		return oshSFTPService.removeArchiveFoldersFromSftp(sftpHost, sftpUser, null, privateKey, transferArchiveFolder);
	}


	@Override
	public boolean removeOriginalImages()
	{
		final Calendar cal = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String formattedDate = sdf.format(cal.getTime());
		final String sftpSource = configurationService.getConfiguration().getString(SFTPSOURCE);
		final File parentDir = new File(sftpSource + "\\" + formattedDate);
		parentDir.mkdirs();
		final String mediaFile = configurationService.getConfiguration().getString(REMOVE_MEDIA_FILE);
		final String fileName = mediaFile + "_" + formattedDate + OshCoreConstants.IMPEX_EXT;
		BufferedWriter outputFile = null;
		File file = null;
		final List<File> files = createListOfFiles();
		try
		{
			file = new File(parentDir, fileName);
			file.createNewFile();
			outputFile = new BufferedWriter(new FileWriter(file));
			outputFile.append(getImportHeader());
			outputFile.append(getRemoveOriginalImageHeader());
			for (final File mediaName : files)
			{
				final String[] imageName = mediaName.getName().split(OshCoreConstants.JPG_EXT);
				outputFile.append(";B-");
				outputFile.append(imageName[0] + "_" + creationTime + ";;;" + OshCoreConstants.IMAGES_FOLDER);
				outputFile.newLine();
			}

		}
		catch (final IOException e)
		{
			Log.error("Failed to create remove original media impex", e);
			return false;
		}
		finally
		{
			try
			{
				if (outputFile != null)
				{
					outputFile.close();
					importImpexFile(file);
					Log.info("Original Images Are Deleted");
					return moveMedias();

				}
			}
			catch (final IOException e)
			{
				Log.error("Failed To Close the file", e);
				return moveMedias();
			}
		}
		return false;
	}


	@Override
	public boolean checkProductExistance()
	{
		try
		{
			final String mediaSource = configurationService.getConfiguration().getString(MEDIAPATH);
			final File[] files = new File(mediaSource).listFiles();
			if (files != null)
			{
				for (final File file : files)
				{
					if (file.isFile() && (file.getName().endsWith(OshCoreConstants.JPG_EXT)))
					{
						final String[] fileName = file.getName().split(OshCoreConstants.JPG_EXT);
						final String rowFileName = fileName[0];
						String productName = fileName[0];
						if (rowFileName.contains(OshCoreConstants.ALT_IMAGES))
						{
							final String[] rowFileNames = rowFileName.split(OshCoreConstants.ALT_IMAGES);
							productName = rowFileNames[0];
						}
						final String productCode = "B-" + productName;
						final List<ProductModel> productModels = getProductDao().findProductsByCode(productCode);
						if (productModels == null || productModels.isEmpty())
						{
							final File filePath = file.getAbsoluteFile();
							filePath.delete();

						}
					}
					else
					{
						Log.info("Images are not in correct format" + mediaSource);
					}
				}
				return true;
			}
		}
		catch (final Exception e)
		{
			Log.error("Failed to check product existance", e);
			return false;
		}
		return false;
	}

	/**
	 * @return the importHeader
	 */
	public String getImportHeader()
	{
		return importHeader;
	}

	/**
	 * @param importHeader
	 *           the importHeader to set
	 */
	public void setImportHeader(final String importHeader)
	{
		this.importHeader = importHeader;
	}

	/**
	 * @return the medianContHeader
	 */
	public String getMediaContHeader()
	{
		return mediaContHeader;
	}

	/**
	 * @param medianContHeader
	 *           the medianContHeader to set
	 */
	public void setMediaContHeader(final String mediaContHeader)
	{
		this.mediaContHeader = mediaContHeader;
	}

	/**
	 * @return the mediaHeader
	 */
	public String getMediaHeader()
	{
		return mediaHeader;
	}

	/**
	 * @param mediaHeader
	 *           the mediaHeader to set
	 */
	public void setMediaHeader(final String mediaHeader)
	{
		this.mediaHeader = mediaHeader;
	}

	/**
	 * @return the importService
	 */
	public ImportService getImportService()
	{
		return importService;
	}

	/**
	 * @param importService
	 *           the importService to set
	 */
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	/**
	 * @return the productHeader
	 */
	public String getProductHeader()
	{
		return productHeader;
	}

	/**
	 * @param productHeader
	 *           the productHeader to set
	 */
	public void setProductHeader(final String productHeader)
	{
		this.productHeader = productHeader;
	}

	/**
	 * @return the findMediaContainerDao
	 */
	public FindMediaContainerDao getFindMediaContainerDao()
	{
		return findMediaContainerDao;
	}

	/**
	 * @param findMediaContainerDao
	 *           the findMediaContainerDao to set
	 */
	public void setFindMediaContainerDao(final FindMediaContainerDao findMediaContainerDao)
	{
		this.findMediaContainerDao = findMediaContainerDao;
	}

	/**
	 * @return the productHeaderForExistingCont
	 */
	public String getProductHeaderForExistingCont()
	{
		return productHeaderForExistingCont;
	}

	/**
	 * @param productHeaderForExistingCont
	 *           the productHeaderForExistingCont to set
	 */
	public void setProductHeaderForExistingCont(final String productHeaderForExistingCont)
	{
		this.productHeaderForExistingCont = productHeaderForExistingCont;
	}

	/**
	 * @return the productHeaderForAltCont
	 */
	public String getProductHeaderForAltCont()
	{
		return productHeaderForAltCont;
	}

	/**
	 * @param productHeaderForAltCont
	 *           the productHeaderForAltCont to set
	 */
	public void setProductHeaderForAltCont(final String productHeaderForAltCont)
	{
		this.productHeaderForAltCont = productHeaderForAltCont;
	}

	/**
	 * @return the removeOriginalImageHeader
	 */
	public String getRemoveOriginalImageHeader()
	{
		return removeOriginalImageHeader;
	}

	/**
	 * @param removeOriginalImageHeader
	 *           the removeOriginalImageHeader to set
	 */
	public void setRemoveOriginalImageHeader(final String removeOriginalImageHeader)
	{
		this.removeOriginalImageHeader = removeOriginalImageHeader;
	}

	public ProductDao getProductDao()
	{
		return productDao;
	}

	/**
	 * @param productDao
	 *           the productDao to set
	 */
	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}
}
