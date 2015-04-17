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

import de.hybris.platform.acceleratorservices.enums.UiExperienceLevel;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import com.hybris.osh.cockpits.cmscockpit.session.impl.DefaultLiveEditBrowserArea;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;

/**
 * Controller of (live edit) preview of a site.
 * <p/>
 * <b>Note:</b><br/>
 * We using this component for displaying a preview of particular web application within <b>cmscockpit</b> in following
 * modes:
 * <ul>
 * <li>displaying preview of particular page in <b>WCMS Page perspective</b></li>
 * <li>displaying live edit session in <b>Live Edit perspective</b>
 * </ul>
 */
public class DefaultLiveEditView
{
	private static final Logger LOG = Logger.getLogger(DefaultLiveEditView.class);

	protected static final String ON_INIT_EVENT = "onInit";
	protected static final String CALLBACK_EVENT = "callback";
	protected static final String URL_CHANGE_EVENT = "urlChange";
	protected static final String LIVE_EDIT_BROWSER_SCLASS = "liveEditBrowser";
	protected static final String ON_INVALIDATE_LATER_EVENT = "onInvalidateLater";

	private transient Div rootDiv = null;
	private transient Iframe contentFrame = null;
	private transient Div welcomePanel = null;
	private transient DefaultLiveEditPopupEditDialog popupEditorDialogDefault = null;

	private final DefaultLiveEditViewModel model;

	public DefaultLiveEditView(final DefaultLiveEditViewModel model)
	{
		this.model = model;
		initialize();
	}

	public DefaultLiveEditView(final DefaultLiveEditViewModel model, final Div welcomePanel)
	{

		this.model = model;
		this.welcomePanel = welcomePanel;
		initialize();
	}

	/**
	 * Initialize live edit view (i.e. creates content frame, loads welcome panel - if any, send init event)
	 * <p/>
	 * <b>Note:</b> <br/>
	 * Invoked only once - during class instantiation
	 */
	protected void initialize()
	{
		rootDiv = new Div();
		UITools.maximize(rootDiv);
		rootDiv.setStyle("background-color:white");

		contentFrame = new Iframe();
		UITools.maximize(contentFrame);
		contentFrame.setSclass(LIVE_EDIT_BROWSER_SCLASS);
		contentFrame.setVisible(getModel().isContentVisible());

		rootDiv.appendChild(contentFrame);
		loadWelcomePanel(rootDiv);

		//add all required events handling
		addEventListeners();

		Events.echoEvent(ON_INIT_EVENT, contentFrame, null);
	}

	/**
	 * Adds all required events handling i.e:
	 * <ul>
	 * <li>user special events</li>
	 * <li>init events</li>
	 * <li>invalidate event</li>
	 * <ul>
	 */
	protected void addEventListeners()
	{
		contentFrame.addEventListener(Events.ON_USER, getUserEventListener());
		contentFrame.addEventListener(ON_INIT_EVENT, getIniEventListener());
		contentFrame.addEventListener(ON_INVALIDATE_LATER_EVENT, getInvalidateListener());
	}

	/**
	 * Returns the ZK component of the site preview.
	 *
	 * @return ZK component of the site preview
	 */
	public HtmlBasedComponent getViewComponent()
	{
		if (this.rootDiv == null)
		{
			initialize();
		}
		return this.rootDiv;
	}

	/**
	 * Returns content frame component - for access it from outside
	 *
	 * @return content frame component
	 */
	public Iframe getContentFrame()
	{
		return this.contentFrame;
	}

	public DefaultLiveEditViewModel getModel()
	{
		return this.model;
	}

	public void setWelcomePanel(final Div welcomePanel)
	{
		this.welcomePanel = welcomePanel;
	}

	/**
	 * Returns event listener for handling init events
	 * <p/>
	 * <b>Note:</b> <br/>
	 * This is used for proper initialize live edit view - within preview mode we to update view in order to make content
	 * visible. When we are within preview mode then {@link de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel#getCurrentPreviewData()} return
	 * <code>null</code>
	 *
	 * @return instance of {@link org.zkoss.zk.ui.event.EventListener}
	 * @see #ON_INIT_EVENT
	 */
	protected EventListener getIniEventListener()
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD:ZK specific
			{
				update();
			}
		};
	}

	/**
	 * Returns event listener for handling invalidate events
	 * <p/>
	 * <b>Note:</b> <br/>
	 * This is used for invalidate frame content
	 *
	 * @return instance of {@link EventListener}
	 * @see #ON_INVALIDATE_LATER_EVENT
	 */
	protected EventListener getInvalidateListener()
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD:ZK specific
			{
				contentFrame.invalidate();
			}
		};
	}

	/**
	 * Returns event listener for handling user events
	 * <p/>
	 * <b>Note:</b> <br/>
	 * This is used for handling user events such like:
	 * <ul>
	 * <li>displaying popup dialog with proper content editors</li>
	 * <li>reacts when user change URL within content frame (this is used for keeping <b>cmscockpit</b> up-to-date with
	 * current URL from content frame)</li>
	 * </ul>
	 *
	 * @return instance of {@link EventListener}
	 */
	protected EventListener getUserEventListener()
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD:ZK specific
			{
				//early exit when prerequisites aren't matched
				if (!(event.getData() instanceof String[]))
				{
					return;
				}
				final String passedAttributes[] = (String[]) event.getData();
				if (CALLBACK_EVENT.equals(passedAttributes[0]))
				{
					popupEditorDialogDefault = new DefaultLiveEditPopupEditDialog(passedAttributes, model.getCurrentPreviewData()
							.getCatalogVersions(), DefaultLiveEditView.this);
					rootDiv.appendChild(popupEditorDialogDefault);

					LOG.info("Received callback event (show popup editor) from element [id = " + passedAttributes[1] + "]");
				}
				else if (URL_CHANGE_EVENT.equals(passedAttributes[0]))
				{
					contentFrame.setVisible(true);
					//final String currentUrl = extractRequestPath(passedAttributes[1]);
					//getModel().setCurrentUrl(currentUrl);
					final UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
					if (!getModel().isPreviewDataValid())
					{
						final Notification notification = new Notification(Labels.getLabel("cmscockpit.liveditsession.expired"),
								Labels.getLabel("cmscockpit.liveditsession.expired.description"));
						currentPerspective.getNotifier().setNotification(notification);

						final UIBrowserArea currentBrowserArea = currentPerspective.getBrowserArea();
						if (currentBrowserArea instanceof DefaultLiveEditBrowserArea)
						{
							final DefaultLiveEditBrowserArea liveEditBrowserArea = ((DefaultLiveEditBrowserArea) currentBrowserArea);
							liveEditBrowserArea.fireModeChange(false);
						}
					}
					final CmsUrlChangeEvent cmsUrlChangeEvent = new CmsUrlChangeEvent(currentPerspective,
							extractRequestPath(passedAttributes[1]), passedAttributes[2], passedAttributes[3], passedAttributes[4]);
					UISessionUtils.getCurrentSession().sendGlobalEvent(cmsUrlChangeEvent);
				}
			}
		};

	}

	/**
	 * Refreshes welcome panel when it is available
	 * <p/>
	 * <b>Note:</b><br/>
	 * This is disjoint with content frame i.e. when we displaying welcome page we also hide content frame at once and
	 * contrarywise
	 */
	protected void refreshWelcomePanel()
	{
		if (this.welcomePanel != null)
		{
			this.welcomePanel.setVisible(getModel().isWelcomePanelVisible());
			if (!rootDiv.getChildren().contains(this.welcomePanel))
			{
				rootDiv.appendChild(this.welcomePanel);
			}
		}
	}

	/**
	 * Provides a proper URL for preview frame
	 * <p/>
	 * <b>Note:</b> The URL that is set for preview frame is different for live edit and preview mode.
	 */
	protected void refreshContentFrame()
	{
		contentFrame.setVisible(getModel().isContentVisible());
		if (getModel().isContentVisible())
		{
			final String generatedUrl = getModel().computeFinalUrl();
			if (getModel().getSite() != null && StringUtils.isBlank(getModel().getSite().getPreviewURL())
					|| StringUtils.isBlank(generatedUrl))
			{
				try
				{
					Messagebox.show(Labels.getLabel("site_url_empty"), Labels.getLabel("general.warning"), Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
				catch (final InterruptedException e)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Errors occurred while showing message box!", e);
					}
				}
			}
			else
			{
				contentFrame.setSrc(generatedUrl);
				if (getModel() != null)
				{
					final PreviewDataModel previewDataModel = getModel().getCurrentPreviewData();
					if (previewDataModel != null && previewDataModel.getUiExperience() != null)
					{
						if (UiExperienceLevel.MOBILE.getCode().equalsIgnoreCase(previewDataModel.getUiExperience().getCode()))
						{
							contentFrame.setWidth("320px");
						}
						else
						{
							contentFrame.setWidth("100%");
						}
					}

				}

				Events.echoEvent(ON_INVALIDATE_LATER_EVENT, contentFrame, null);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Current url : " + getModel().getCurrentUrl());
				}
			}
		}
	}

	/**
	 * Performs general update.
	 */
	public void update()
	{
		refreshWelcomePanel();
		refreshContentFrame();
	}

	/**
	 * Performs update according to given item modifications
	 *
	 * @param item               modified item
	 * @param modifiedProperties modified properties of the item
	 */
	public void updateItem(final TypedObject item, final Set<PropertyDescriptor> modifiedProperties, final Object reason)
	{
		//TODO assuming here that only one popup can be open, since equals below doesn't work for some reason
		if (!(reason instanceof DefaultLiveEditPopupEditDialog))
		{
			if (this.popupEditorDialogDefault != null && this.popupEditorDialogDefault.isVisible() && !this.popupEditorDialogDefault.equals(reason))
			{
				popupEditorDialogDefault.update();
			}
		}
	}

	/**
	 * Extracts request path from long URL (request path with query search)
	 *
	 * @param longUrl request path with query search
	 * @return extracted request path
	 */
	protected String extractRequestPath(final String longUrl)
	{
		String ret = StringUtils.EMPTY;
		if (!longUrl.contains(CMSFilter.PREVIEW_TOKEN))
		{
			final String[] urlParts = longUrl.split("[\\?&]" + CMSFilter.PREVIEW_TICKET_ID_PARAM);
			ret = urlParts[0];
		}
		return ret;
	}

	/**
	 * Loads welcome panel if any
	 *
	 * @param parent parent component
	 * @return current welcome panel component
	 */
	protected HtmlBasedComponent loadWelcomePanel(final HtmlBasedComponent parent)
	{
		if (welcomePanel != null && getModel().isWelcomePanelVisible())
		{
			parent.appendChild(welcomePanel);
		}
		return welcomePanel;
	}
}
