/**
 * 
 */
package com.hybris.osh.storefront.forms;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;



/**
 * Setter and getter for contact us email form
 * 
 */
public class ContactUsEmailForm
{

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

	private String contact_us_subject;

	/**
	 * @return the contact_us_subject
	 */
	public String getContact_us_subject()
	{
		return contact_us_subject;
	}

	/**
	 * @param contact_us_subject
	 *           the contact_us_subject to set
	 */
	public void setContact_us_subject(final String contact_us_subject)
	{
		this.contact_us_subject = contact_us_subject;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}

	/**
	 * @return the email_content
	 */

	@NotBlank(message = "{contactus.email.invalid}")
	@Size(min = 1, max = 255, message = "{contactus.email.invalid}")
	@Email(message = "{contactus.email.invalid}")
	public String getEmail_content()
	{
		return email_content;
	}

	/**
	 * @param email_content
	 *           the email_content to set
	 */
	public void setEmail_content(final String email_content)
	{
		this.email_content = email_content;
	}

	/**
	 * @return the store_name
	 */
	public String getStore_name()
	{
		return store_name;
	}

	/**
	 * @param store_name
	 *           the store_name to set
	 */
	public void setStore_name(final String store_name)
	{
		this.store_name = store_name;
	}

	private String email;
	private String email_content;
	private String store_name;


}
