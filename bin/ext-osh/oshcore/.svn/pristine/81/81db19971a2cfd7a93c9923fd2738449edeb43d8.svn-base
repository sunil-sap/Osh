package com.hybris.osh.core.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * this class is to create media and media containers impex for the imagemagick
 */
public interface CreateMediaImpexService
{

	/**
	 * create impex files for media and media container for product images
	 * 
	 * @return true if media and media containers are successfully uploaded
	 */
	public boolean creatMediaImpex();

	/**
	 * This method will create impex file to associate media and media container for products based on the media
	 * container present in the system
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public boolean createProductsMediaImpex(final File file) throws IOException;

	/**
	 * 
	 * @return true if impex is successfully uploaded else return false
	 * 
	 */
	public boolean loadProductMediaImpex();


	/**
	 * this method will read all the images having .jpg extension in the particular folder and create list of it
	 * 
	 * @return list of the image names
	 */
	public ArrayList<String> createListOfImages();

	/**
	 * this method will read all the images having .jpg extension in the particular folder and create list of it
	 * 
	 * @return list of the images
	 */
	public List<File> createListOfFiles();

	/**
	 * this method will sort the new imges based on the media containers present in the system
	 * 
	 * @return
	 */


	public LinkedHashMap<String, List<String>> sortMediaContainers();

	/**
	 * After successfully imprting the impex files this method will archive the original imges
	 * 
	 * @return
	 */
	public boolean moveMedias();

	public boolean copyImagesInImageMagickPath();

	/**
	 * 
	 * Remove original images after image magick rendition
	 */
	public boolean removeOriginalImages();

	/**
	 * 
	 * @return
	 */
	public boolean removeOriginalImagesFromSftp();

	/**
	 * @return
	 */
	public boolean removeArchiveFoldersFromSftp();


	/**
	 * 
	 * Check if product exists as same name of image file name and remove if don't exist.
	 */
	public boolean checkProductExistance();

}
