/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.cockpits.components.liveedit;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cmscockpit.components.contentbrowser.ContentEditorRenderer;
import de.hybris.platform.cmscockpit.components.contentbrowser.DefaultContentEditorRenderer;
import de.hybris.platform.cmscockpit.components.contentbrowser.message.StatusPanelComponent;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.commons.jalo.CommonsManager;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * Represents reference selector modal dialog - container for advanced search component.
 * <p/>
 * <b>Note:</b> <br/>
 * Represents a popup dialog within we display a proper editors for particular CMS component
 */
public class DefaultLiveEditPopupEditDialog extends Window
{
	private static final Logger LOG = Logger.getLogger(DefaultLiveEditPopupEditDialog.class);

	protected static final String CONTENT_EDITOR_CONF = "contentEditor";
	protected static final String LIVE_POPUP_MAIN_PANEL = "livePopupMainPanel";
	protected static final String LIVE_POPUP_CONTAINER = "livePopupContainer";

	private DefaultLiveEditView liveEditView = null;
	private TypedObject currentContentElement = null;
	private ObjectTemplate lastRootItem = null;
	private ObjectValueContainer valueContainer = null;
	private Collection<CatalogVersionModel> catalogVersions = null;
	private String[] currentAttributes = null;
	private ContentEditorRenderer editorRenderer = null;

	private CMSComponentService cmsComponentService = null;
	private UIConfigurationService uiConfigurationService = null;
	private ContentEditorConfiguration configuration = null;

	private transient Div scrollableContainer = null;
	private final transient StatusPanelComponent statusComponent = new StatusPanelComponent();

	protected DefaultLiveEditPopupEditDialog(final String[] currentAttributes, final Collection<CatalogVersionModel> catalogVersions,
	                                         final DefaultLiveEditView liveEditView) throws InterruptedException
	{
		super();
		this.currentAttributes = currentAttributes;
		this.catalogVersions = catalogVersions;
		this.liveEditView = liveEditView;
		this.initialize();
	}

	/**
	 * Creates a proper popup dialog and injects necessary editors
	 * <p/>
	 * <b>Note:</b> Here also we inject appropriate editors for current CMS Component
	 *
	 * @throws InterruptedException
	 */
	private void initialize() throws InterruptedException
	{
		this.setSizable(false);
		this.setClosable(true);
		this.setSclass(LIVE_POPUP_CONTAINER);
		this.setBorder("none");
		this.setPosition("center");
		this.setMode("modal");
		this.setTitle(Labels.getLabel("cmscocpit.livedit.popup.title"));

		final Div mainContainer = new Div();
		this.appendChild(mainContainer);
		AbstractCMSComponentModel currentComponent = null;
		try
		{
			if (StringUtils.isNotEmpty(currentAttributes[1]))
			{
				currentComponent = getComponentService().getAbstractCMSComponent(currentAttributes[1], catalogVersions);
			}
		}
		catch (final CMSItemNotFoundException e)
		{ //TODO: handle it in proper way
			LOG.error(e);
		}
		if (currentComponent != null)
		{
			currentContentElement = UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentComponent);

			mainContainer.setWidth("100%");
			scrollableContainer = new Div();
			scrollableContainer.setStyle("overflow-y:auto");
			scrollableContainer.setSclass(LIVE_POPUP_MAIN_PANEL);
			scrollableContainer.setHeight("500px");

			//renders a content editors
			scrollableContainer.appendChild(statusComponent);
			renderEditor(scrollableContainer, currentContentElement);
			mainContainer.appendChild(scrollableContainer);

			this.addEventListener(Events.ON_CLOSE, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception //NOPMD:ZK Specific
				{
					detach();
					clearValueContainer();
					liveEditView.update();
				}
			});

			final Label noteAutoPersist = new Label(Labels.getLabel("cmscockpit.note_auto_persist"));
			noteAutoPersist.setStyle("font-size:11px;font-style:italic");
			mainContainer.appendChild(noteAutoPersist);
		}
		else
		{
			//display an error
			this.setWidth("400px");
			mainContainer.setWidth("100%");
			scrollableContainer = new Div();
			scrollableContainer.setStyle("overflow-y:auto");
			scrollableContainer.setHeight("250px");
			scrollableContainer.appendChild(new Label(Labels.getLabel("liveedit.popup.element.notfound", new String[]
					{currentAttributes[1]})));
			mainContainer.appendChild(scrollableContainer);
		}

		final Div buttonDiv = new Div();
		buttonDiv.setStyle("margin-top:15px;text-align: right;");
		final Button okButton = new Button(Labels.getLabel("general.ok"));
		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		{
			final String id = "LiveEditPopupOKBtn_EA_";
			UITools.applyTestID(okButton, id);
		}
		okButton.setParent(buttonDiv);
		okButton.setSclass("btnblue");
		okButton.setDisabled(false);
		okButton.setTooltiptext(Labels.getLabel("general.ok"));
		okButton.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception //NOPMD:ZK Specific
			{
				DefaultLiveEditPopupEditDialog.this.detach();
				liveEditView.update();

			}
		});
		mainContainer.appendChild(buttonDiv);
	}

	/**
	 * General update method
	 */
	public void update()
	{
		UITools.detachChildren(scrollableContainer);
		UITools.detachChildren(statusComponent);
		scrollableContainer.appendChild(statusComponent);
		renderEditor(scrollableContainer, currentContentElement);
	}

	/**
	 * Renders editor for passed element
	 *
	 * @param parent   parent component
	 * @param rootItem passed element
	 */
	protected void renderEditor(final HtmlBasedComponent parent, final TypedObject rootItem)
	{
		if (rootItem != null)
		{
			// get corresponding object template
			final ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService()
					.getObjectTemplate(rootItem.getType().getCode());

			if (isReloadNeeded(objectTemplate))
			{
				this.lastRootItem = objectTemplate;

				// load configuration
				this.configuration = getContentEditorConfiguration(objectTemplate);

				if (configuration == null)
				{
					LOG.error("Aborting rendering of content element editor. Reason: Configuration could not be loaded.");
					return;
				}
				else
				{
					// create renderer
					editorRenderer = new DefaultContentEditorRenderer(configuration, true);
				}
			}
			if (editorRenderer == null)
			{
				LOG.warn("Aborting rendering of content element editor. Content element editor renderer could not be loaded.");
			}
			else if (configuration == null)
			{
				LOG.warn("Aborting rendering of content element editor. Reason: Configuration not available.");
			}
			else
			{
				try
				{
					// get (velocity) parsed template
					final String templateString = getParsedVelocityTemplateString(configuration.getCockpitTemplate(), rootItem);

					if (StringUtils.isBlank(templateString))
					{
						LOG.warn("Aborting rendering of content element editor. Reason: No cockpit template could be loaded.");
					}
					else
					{
						// render editor
						editorRenderer.renderContentEditor(rootItem, templateString, getValueContainer(rootItem), parent,
								Collections.singletonMap("locationInfo", this));
					}
				}
				catch (final IllegalArgumentException iae)
				{
					LOG.error("Rendering of editor failed. Reason: '" + iae.getMessage() + "'.", iae);
				}
			}
		}
		else
		{
			LOG.warn("Rendering of editor aborted. Reason: Root item of section model not a 'TypedObject'.");
		}
	}

	/**
	 * Checks whether we have to reload a editor
	 *
	 * @param objectTemplate
	 * @return flag that indicates whether we should reload editors
	 */
	private boolean isReloadNeeded(final ObjectTemplate objectTemplate)
	{
		return this.lastRootItem == null || !this.lastRootItem.equals(objectTemplate) || this.editorRenderer == null
				|| this.configuration == null;
	}

	/**
	 * Computes a velocity template
	 *
	 * @param velocityTemplate - raw velocity template
	 * @param rootItem         - passed element
	 *                         <p/>
	 *                         <b>Note:</b>
	 * @return a new (computed) velocity template
	 */
	protected String getParsedVelocityTemplateString(final String velocityTemplate, final TypedObject rootItem)
	{
		String templateString = null;
		try
		{
			final StringWriter writer = new StringWriter();
			final Properties velocityEngineProperties = new Properties();
			final VelocityEngine velocityEngine = CommonsManager.getInstance().getVelocityEngine(velocityEngineProperties);
			final VelocityContext velocityContext = new VelocityContext();

			final String label = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(rootItem);

			// add velocity parameters
			velocityContext.put("label", StringEscapeUtils.escapeXml(label));

			// render template
			velocityEngine.evaluate(velocityContext, writer, "CMS Content editor section component", velocityTemplate);
			templateString = writer.toString();
		}
		catch (final Exception e)
		{
			LOG.error("Velocity template MELTDOWN!!!", e);
		}
		return templateString;
	}

	/**
	 * Gets a proper value container for passed element
	 *
	 * @param rootItem passed element
	 * @return value container
	 */
	protected ObjectValueContainer getValueContainer(final TypedObject rootItem)
	{
		if (valueContainer == null)
		{
			if (rootItem != null)
			{
				valueContainer = TypeTools.createValueContainer(rootItem, rootItem.getType().getPropertyDescriptors(), UISessionUtils
						.getCurrentSession().getSystemService().getAvailableLanguageIsos(), true);
			}
			else
			{
				LOG.warn("No value container and no valid root item available.");
			}
		}
		return valueContainer;
	}

	/**
	 * Clears current value container
	 */
	protected void clearValueContainer()
	{
		this.valueContainer = null;
	}

	/**
	 * Retrieves a content editor configuration
	 *
	 * @param objectTemplate current object template
	 * @return proper configuration
	 */
	protected ContentEditorConfiguration getContentEditorConfiguration(final ObjectTemplate objectTemplate)
	{
		return getUIConfigurationService().getComponentConfiguration(objectTemplate, CONTENT_EDITOR_CONF,
				ContentEditorConfiguration.class);
	}

	/**
	 * Retrieves a configuration service
	 *
	 * @return configuration service
	 */
	protected UIConfigurationService getUIConfigurationService()
	{
		if (uiConfigurationService == null)
		{
			uiConfigurationService = (UIConfigurationService) SpringUtil.getBean("uiConfigurationService");
		}
		return uiConfigurationService;
	}

	/**
	 * Retrieves a component service
	 *
	 * @return component service
	 */
	protected CMSComponentService getComponentService()
	{
		if (this.cmsComponentService == null)
		{
			this.cmsComponentService = (CMSComponentService) SpringUtil.getBean("cmsComponentService");
		}
		return this.cmsComponentService;
	}
}
