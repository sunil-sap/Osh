/**
 * 
 */
package com.hybris.osh.core.process.email.actions;

import de.hybris.platform.acceleratorservices.email.CMSEmailPageService;
import de.hybris.platform.acceleratorservices.email.EmailGenerationService;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageTemplateModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.process.email.context.EmailContextFactory;
import de.hybris.platform.acceleratorservices.process.strategies.ProcessContextResolutionStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.util.mail.MailUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.osh.core.service.OshCommerceReturnService;


/**
 * This class is created to add the BCC field in the return order email
 */
public abstract class GenerateReturnOrderEmailAction extends AbstractSimpleDecisionAction
{
	private static final Logger LOG = Logger.getLogger(GenerateReturnOrderEmailAction.class);

	private CMSEmailPageService cmsEmailPageService;
	private String frontendTemplateName;
	private ProcessContextResolutionStrategy contextResolutionStrategy;
	@Resource(name = "emailContextFactory")
	private EmailContextFactory emailContextFactory;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "rendererService")
	private RendererService rendererService;
	@Resource(name = "emailService")
	private EmailService emailService;

	private OshCommerceReturnService oshCommerceReturnService;

	protected CMSEmailPageService getCmsEmailPageService()
	{
		return cmsEmailPageService;
	}

	private BaseSiteService baseSiteService;

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Required
	public void setCmsEmailPageService(final CMSEmailPageService cmsEmailPageService)
	{
		this.cmsEmailPageService = cmsEmailPageService;
	}

	protected String getFrontendTemplateName()
	{
		return frontendTemplateName;
	}

	@Required
	public void setFrontendTemplateName(final String frontendTemplateName)
	{
		this.frontendTemplateName = frontendTemplateName;
	}

	protected ProcessContextResolutionStrategy getContextResolutionStrategy()
	{
		return contextResolutionStrategy;
	}

	@Required
	public void setContextResolutionStrategy(final ProcessContextResolutionStrategy contextResolutionStrategy)
	{
		this.contextResolutionStrategy = contextResolutionStrategy;
	}



	public abstract EmailGenerationService getEmailGenerationService();

	@Override
	public Transition executeAction(final BusinessProcessModel businessProcessModel) throws RetryLaterException
	{
		final CatalogVersionModel contentCatalogVersion = getContextResolutionStrategy().getContentCatalogVersion(
				businessProcessModel);

		if (contentCatalogVersion != null)
		{
			final EmailPageModel emailPageModel = getCmsEmailPageService().getEmailPageForFrontendTemplate(
					getFrontendTemplateName(), contentCatalogVersion);

			if (emailPageModel != null)
			{
				final EmailMessageModel emailMessageModel = generate(businessProcessModel, emailPageModel);
				if (emailMessageModel != null)
				{
					final List<EmailMessageModel> emails = new ArrayList<EmailMessageModel>();
					emails.addAll(businessProcessModel.getEmails());
					emails.add(emailMessageModel);
					businessProcessModel.setEmails(emails);

					getModelService().save(businessProcessModel);

					LOG.info("Email message generated");
					return Transition.OK;
				}
				else
				{
					LOG.error("Failed to generate email message");
				}
			}
			else
			{
				LOG.error("Could not retrieve email page model for " + getFrontendTemplateName() + " and "
						+ contentCatalogVersion.getMnemonic() + ", cannot generate email content");
			}
		}
		else
		{
			LOG.error("Could not resolve the content catalog version, cannot generate email content");
		}

		return Transition.NOK;
	}

	public EmailMessageModel generate(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
			throws RuntimeException
	{
		ServicesUtil.validateParameterNotNull(emailPageModel, "EmailPageModel cannot be null");
		Assert.isInstanceOf(EmailPageTemplateModel.class, emailPageModel.getMasterTemplate(),
				"MasterTemplate associated with EmailPageModel should be EmailPageTemplate");

		final EmailPageTemplateModel emailPageTemplateModel = (EmailPageTemplateModel) emailPageModel.getMasterTemplate();
		final RendererTemplateModel bodyRenderTemplate = emailPageTemplateModel.getHtmlTemplate();
		Assert.notNull(bodyRenderTemplate, "HtmlTemplate associated with MasterTemplate of EmailPageModel cannot be null");
		final RendererTemplateModel subjectRenderTemplate = emailPageTemplateModel.getSubject();
		Assert.notNull(subjectRenderTemplate, "Subject associated with MasterTemplate of EmailPageModel cannot be null");

		final EmailMessageModel emailMessageModel;
		final AbstractEmailContext emailContext = emailContextFactory.create(businessProcessModel, emailPageModel,
				bodyRenderTemplate);

		if (emailContext == null)
		{
			LOG.error("Failed to create email context");
			throw new RuntimeException("Failed to create email context");
		}
		else
		{
			final StringWriter subject = new StringWriter();
			rendererService.render(subjectRenderTemplate, emailContext, subject);

			final StringWriter body = new StringWriter();
			rendererService.render(bodyRenderTemplate, emailContext, body);

			emailMessageModel = createEmailMessageForOrderConfirmation(subject.toString(), body.toString(), emailContext,
					businessProcessModel);
		}

		return emailMessageModel;
	}

	protected EmailMessageModel createEmailMessageForOrderConfirmation(final String emailSubject, final String emailBody,
			final AbstractEmailContext emailContext, final BusinessProcessModel businessProcessModel)
	{

		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		final EmailAddressModel toAddress = emailService.getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
				emailContext.getToDisplayName());
		toEmails.add(toAddress);

		final List<EmailAddressModel> bccEmails = new ArrayList<EmailAddressModel>();

		final String[] bccEmailIds = oshCommerceReturnService.collectBCCAddressForEmail();

		for (final String bccEmailId : bccEmailIds)
		{
			final EmailAddressModel bccId = emailService.getOrCreateEmailAddressForEmail(bccEmailId, bccEmailId);
			bccEmails.add(bccId);
		}
		final EmailAddressModel fromAddress = emailService.getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());

		final EmailMessageModel emailMessageModel = createEmailMessageForOrderConfirmation(toEmails,
				new ArrayList<EmailAddressModel>(), bccEmails, fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
				null);
		return emailMessageModel;

	}

	public EmailMessageModel createEmailMessageForOrderConfirmation(final List<EmailAddressModel> toAddresses,
			final List<EmailAddressModel> ccAddresses, final List<EmailAddressModel> bccAddresses,
			final EmailAddressModel fromAddress, final String replyToAddress, final String subject, final String body,
			final List<EmailAttachmentModel> attachments)
	{
		// Do all validation now before creating the message
		if (toAddresses == null || toAddresses.isEmpty())
		{
			throw new IllegalArgumentException("toAddresses must not be empty");
		}
		if (fromAddress == null)
		{
			throw new IllegalArgumentException("fromAddress must not be null");
		}
		if (subject == null || subject.isEmpty())
		{
			throw new IllegalArgumentException("subject must not be empty");
		}
		if (body == null || body.isEmpty())
		{
			throw new IllegalArgumentException("body must not be empty");
		}
		validateEmailAddress(replyToAddress, "replyToAddress");

		final EmailMessageModel emailMessageModel = getModelService().create(EmailMessageModel.class);
		emailMessageModel.setToAddresses(toAddresses);
		emailMessageModel.setCcAddresses(ccAddresses);
		emailMessageModel.setBccAddresses(bccAddresses);
		emailMessageModel.setFromAddress(fromAddress);
		emailMessageModel.setReplyToAddress((replyToAddress != null && !replyToAddress.isEmpty()) ? replyToAddress : fromAddress
				.getEmailAddress());
		emailMessageModel.setSubject(subject);
		emailMessageModel.setBody(body);
		emailMessageModel.setAttachments(attachments);
		getModelService().save(emailMessageModel);
		return emailMessageModel;
	}

	protected void validateEmailAddress(final String address, final String type)
	{
		try
		{
			if (address != null && !address.isEmpty())
			{
				MailUtils.validateEmailAddress(address, type);
			}
		}
		catch (final EmailException ex)
		{
			throw new IllegalArgumentException(type, ex);
		}
	}

	/**
	 * @return the oshCommerceReturnService
	 */
	public OshCommerceReturnService getOshCommerceReturnService()
	{
		return oshCommerceReturnService;
	}

	/**
	 * @param oshCommerceReturnService
	 *           the oshCommerceReturnService to set
	 */
	public void setOshCommerceReturnService(final OshCommerceReturnService oshCommerceReturnService)
	{
		this.oshCommerceReturnService = oshCommerceReturnService;
	}

}
