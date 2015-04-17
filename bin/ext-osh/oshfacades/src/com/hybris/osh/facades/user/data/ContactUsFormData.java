/**
 * 
 */
package com.hybris.osh.facades.user.data;

/**
 * This class is used to convert form value in data
 * 
 */
public class ContactUsFormData
{
	private String emailAddress;

	private String firstname;

	/**
	 * @return the firstname
	 */
	public String getFirstname()
	{
		return firstname;
	}

	/**
	 * @param firstname
	 *           the firstname to set
	 */
	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname()
	{
		return lastname;
	}

	/**
	 * @param lastname
	 *           the lastname to set
	 */
	public void setLastname(final String lastname)
	{
		this.lastname = lastname;
	}

	private String lastname;

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress()
	{
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *           the emailAddress to set
	 */
	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the emailContent
	 */
	public String getEmailContent()
	{
		return emailContent;
	}

	/**
	 * @param emailContent
	 *           the emailContent to set
	 */
	public void setEmailContent(final String emailContent)
	{
		this.emailContent = emailContent;
	}

	/**
	 * @return the storeName
	 */
	public String getStoreName()
	{
		return storeName;
	}

	/**
	 * @param storeName
	 *           the storeName to set
	 */
	public void setStoreName(final String storeName)
	{
		this.storeName = storeName;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject
	 *           the subject to set
	 */
	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	private String emailContent;
	private String storeName;
	private String subject;


}
