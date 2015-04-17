/**
 * 
 */
package com.hybris.osh.storefront.forms;

import de.hybris.platform.core.model.product.ProductModel;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;



/**
 * Send a mail to friend
 * 
 */
public class SendEmailForm
{
	private String to_emailadd;
	private String subject;
	private String from_email;
	private String notes;
	private ProductModel productModel;

	@NotBlank(message = "{register.email.invalid}")
	@Size(min = 1, max = 255, message = "{register.email.invalid}")
	@Email(message = "{register.email.invalid}")
	public String getTo_emailadd()
	{
		return to_emailadd;
	}

	public void setTo_emailadd(final String to_emailadd)
	{
		this.to_emailadd = to_emailadd;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	@NotBlank(message = "{register.email.invalid}")
	@Size(min = 1, max = 255, message = "{register.email.invalid}")
	@Email(message = "{register.email.invalid}")
	public String getFrom_email()
	{
		return from_email;
	}

	public void setFrom_email(final String from_email)
	{
		this.from_email = from_email;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	public ProductModel getProductModel()
	{
		return productModel;
	}

	public void setProductModel(final ProductModel productModel)
	{
		this.productModel = productModel;
	}




}
