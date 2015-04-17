/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.oshcscockpit.editor;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.config.AvailableValuesProvider;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UICockpitArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UndoTools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.core.model.CreditPaymentTransactionModel;


public class OshEditorHelper extends EditorHelper
{

	public static interface LanguageAwareEditorListener extends EditorListener
	{
		public abstract void actionPerformed(String s, String s1);

		public abstract void valueChanged(Object obj, String s);
	}

	public OshEditorHelper()
	{
	}

	public static CreateContext applyReferenceRelatedAttributes(final ReferenceUIEditor referenceEditor,
			final PropertyDescriptor pd, final Map parameters, final TypedObject currentObject, Object currentValue,
			final String isoCode, final UICockpitArea cockpitArea, final UISession session)
	{
		final SearchType rootSearchType = null;
		final SearchType rootType = getRootSearchType(pd, session);
		if (!StringUtils.isEmpty((String) parameters.get("allowCreate")))
		{
			referenceEditor.setAllowCreate(Boolean.valueOf((String) parameters.get("allowCreate")));
		}
		else
		{
			referenceEditor.setAllowCreate(Boolean.TRUE);
		}
		if (rootType != null)
		{
			referenceEditor.setRootType(rootType);
			referenceEditor.setRootSearchType(rootSearchType);
		}
		else
		{
			LOG.warn((new StringBuilder(
					"Could not set root type for reference editor (Reason: No root search type could be retrieved for property descriptor '"))
					.append(pd).append("').").toString());
		}
		if ((currentValue instanceof Collection) && !((Collection) currentValue).isEmpty()
				&& (((Collection) currentValue).iterator().next() instanceof TypedObject))
		{
			currentValue = session.getTypeService().wrapItems((Collection) currentValue);
		}
		else if (!(currentValue instanceof TypedObject))
		{
			currentValue = session.getTypeService().wrapItem(currentValue);
		}
		final CreateContext createContext = new CreateContext(rootType, currentObject, pd, isoCode);
		createContext.setExcludedTypes(parseTemplateCodes((String) parameters.get("excludeCreateTypes"), session.getTypeService()));
		createContext
				.setAllowedTypes(parseTemplateCodes((String) parameters.get("restrictToCreateTypes"), session.getTypeService()));
		return createContext;
	}

	@SuppressWarnings("unused")
	private static boolean checkSingle(final PropertyDescriptor propertyDescriptor)
	{
		final de.hybris.platform.cockpit.model.meta.PropertyDescriptor.Multiplicity multi = propertyDescriptor.getMultiplicity();
		return multi == null || multi.equals(de.hybris.platform.cockpit.model.meta.PropertyDescriptor.Multiplicity.SINGLE);
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist) throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, ((String) (null)));
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final EditorListener editorListener,
			final String editorCode) throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, null, editorListener);
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode)
			throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, ((Map) (null)));
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode, final Map params)
			throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, false);
	}


	//overloaded for customer credit
	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode, final Map params,
			final String str) throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, false, str);
	}


	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode, final Map params,
			final boolean focus, final String str) throws IllegalArgumentException
	{
		if (propDescr == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing property descriptor.");
		}
		if (parent == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing parent component.");
		}
		if (valueContainer == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing value container.");
		}
		/*
		 * if (propDescr.isLocalized()) { renderLocalizedEditor(item, propDescr, parent, valueContainer, autoPersist,
		 * editorCode, params, focus); }
		 */
		else
		{
			renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, null, focus, str);
		}
	}



	public static void renderSingleEditor(final TypedObject item, final PropertyDescriptor propDescr,
			final HtmlBasedComponent parent, final ObjectValueContainer valueContainer, final boolean autoPersist,
			final String editorCode, final Map params, final String isoCode, final boolean focus, final String str)
	{
		final UIEditor editor = getUIEditor(propDescr, editorCode);
		final de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer
				.getValue(propDescr, isoCode);


		final EditorListener editorListener = new EditorListener()
		{
			@Override
			public void actionPerformed(final String actionCode)
			{
				if ("open_external_clicked".equals(actionCode))
				{
					final Object value = editor.getValue();
					if ((value instanceof Collection) && (editor instanceof ReferenceUIEditor))
					{
						ObjectTemplate type = TypeTools.getValueTypeAsObjectTemplate(propDescr, UISessionUtils.getCurrentSession()
								.getTypeService());
						if (type != null
								&& UISessionUtils.getCurrentSession().getTypeService().getBaseType("VariantProduct")
										.isAssignableFrom(type.getBaseType())
								&& item != null
								&& UISessionUtils.getCurrentSession().getTypeService().getBaseType("Product")
										.isAssignableFrom(item.getType()) && (propDescr instanceof ItemAttributePropertyDescriptor)
								&& ((ItemAttributePropertyDescriptor) propDescr).isSingleAttribute())
						{
							@SuppressWarnings("deprecation")
							final Object variantTypeObject = TypeTools.getObjectAttributeValue(item,
									de.hybris.platform.variants.constants.GeneratedVariantsConstants.Attributes.Product.VARIANTTYPE,
									UISessionUtils.getCurrentSession().getTypeService());
							if ((variantTypeObject instanceof TypedObject)
									&& UISessionUtils.getCurrentSession().getTypeService().getBaseType("ComposedType")
											.isAssignableFrom(((TypedObject) variantTypeObject).getType()))
							{
								@SuppressWarnings("deprecation")
								final String variantTypeCode = (String) TypeTools.getObjectAttributeValue(
										(TypedObject) variantTypeObject, "code", UISessionUtils.getCurrentSession().getTypeService());
								type = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(variantTypeCode);
							}
							else
							{
								type = null;
							}
						}
						final UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
						if ((perspective instanceof BaseUICockpitPerspective) && type != null)
						{
							final Map newParameters = new HashMap();
							if (params != null)
							{
								newParameters.putAll(params);
							}
							newParameters.put("propertyDescriptor", propDescr);
							if (((Collection) value).isEmpty() || !(((Collection) value).iterator().next() instanceof TypedObject))
							{
								((BaseUICockpitPerspective) perspective).openReferenceCollectionInBrowserContext(Collections.EMPTY_LIST,
										type, item, newParameters);
							}
							else
							{
								((BaseUICockpitPerspective) perspective).openReferenceCollectionInBrowserContext((Collection) value,
										type, item, newParameters);
							}
						}
					}
				}
				else if ("escape_pressed".equals(actionCode))
				{
					editor.setValue(valueHolder.getOriginalValue());

				}
			}

			@Override
			public void valueChanged(Object value)
			{
				if ((value instanceof Collection)
						&& de.hybris.platform.cockpit.model.meta.PropertyDescriptor.Multiplicity.SET
								.equals(propDescr.getMultiplicity()))
				{
					value = new HashSet((Collection) value);
				}
				valueHolder.setLocalValue(value);
				if (autoPersist && item != null)
				{
					try
					{
						EditorHelper.persistValues(item, valueContainer,
								Collections.singletonMap("eventSource", params.get("eventSource")));
					}
					catch (final ValueHandlerException e)
					{
						OshEditorHelper.LOG.error("error saving value", e);
					}
				}
				else
				{
					valueHolder.stored();
				}
			}

		};
		renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, isoCode, focus,
				editorListener, str);
	}


	public static void renderSingleEditor(final TypedObject item, final PropertyDescriptor propDescr,
			final HtmlBasedComponent parent, final ObjectValueContainer valueContainer, final boolean autoPersist,
			final String editorCode, final Map params, final String isoCode, final boolean focus,
			final EditorListener editorListener, final String str)
	{
		final TypedObject orderEntry = (TypedObject) valueContainer.getObject();
		final OrderModel orderModel = (OrderModel) orderEntry.getObject();

		final Collection<CreditPaymentTransactionModel> ListCreditPaymentTransactionModel = orderModel
				.getCreditPaymentTransaction();
		CreditPaymentTransactionModel creditPaymentTransactionModel = null;
		for (final CreditPaymentTransactionModel itrCreditPaymentTransactionModel : ListCreditPaymentTransactionModel)
		{
			creditPaymentTransactionModel = itrCreditPaymentTransactionModel;
		}



		final Double refundAmount = new Double(0.0);
		/*
		 * if (creditPaymentTransactionModel.getCreditAmount() != null) {
		 * 
		 * refundAmount = creditPaymentTransactionModel.getCreditAmount();
		 * 
		 * }
		 */
		/*
		 * final ConsignmentEntryModel consignmentEntryModel = orderentryModel.getConsignmentEntries().iterator().next();
		 * final ConsignmentModel consignmentModel = consignmentEntryModel.getConsignment(); consignmentModel.getCode();
		 */

		if (StringUtils.isNotBlank(isoCode) && !checkLanguageVisibility(item, isoCode))
		{
			final Label l = new Label(Labels.getLabel("editor.cataloglanguage.hidden", new String[]
			{ isoCode }));
			l.setStyle("color:#ccc;");
			l.setParent(parent);
			return;
		}
		final UIEditor editor = getUIEditor(propDescr, editorCode);
		if (editor != null)
		{
			boolean editable = false;
			if (item == null || UISessionUtils.getCurrentSession().getModelService().isNew(item.getObject()))
			{
				final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
				editable = getUIAccessRightService().isWritable(
						typeService.getObjectType(typeService.getTypeCodeFromPropertyQualifier(propDescr.getQualifier())), propDescr,
						true);
			}
			else
			{
				editable = getUIAccessRightService().isWritable(item.getType(), item, propDescr, false);
			}
			if (editable && propDescr.isLocalized())
			{
				final Set writeableLangIsos = UISessionUtils.getCurrentSession().getSystemService().getAllWriteableLanguageIsos();
				editable = writeableLangIsos != null && writeableLangIsos.contains(isoCode);
			}
			editor.setEditable(editable);


			if (editor instanceof ListUIEditor)
			{
				setAvailableValuesInListEditor((ListUIEditor) editor, propDescr, params);
			}



			final Object currentValue = null;

			final Map customParameters = new HashMap();
			if (params != null)
			{
				customParameters.putAll(params);
			}
			customParameters.put("attributeQualifier", propDescr.getQualifier());
			customParameters.put("parentObject", item);
			if (editor instanceof ReferenceUIEditor)
			{
				final SearchType rootSearchType = null;
				final SearchType rootType = getRootSearchType(propDescr, UISessionUtils.getCurrentSession());
				if (params != null && !StringUtils.isEmpty((String) params.get("allowCreate")))
				{
					((ReferenceUIEditor) editor).setAllowCreate(Boolean.valueOf((String) params.get("allowCreate")));
				}
				else
				{
					((ReferenceUIEditor) editor).setAllowCreate(Boolean.TRUE);
				}
				if (rootType != null)
				{
					((ReferenceUIEditor) editor).setRootType(rootType);
					((ReferenceUIEditor) editor).setRootSearchType(rootSearchType);
				}
				else
				{
					LOG.warn((new StringBuilder(
							"Could not set root type for reference editor (Reason: No root search type could be retrieved for property descriptor '"))
							.append(propDescr).append("').").toString());
				}
				CreateContext createContext = null;
				if (params != null)
				{
					createContext = (CreateContext) params.get("createContext");
					if (createContext == null)
					{
						createContext = applyReferenceRelatedAttributes((ReferenceUIEditor) editor, propDescr, params, item,
								currentValue, isoCode, null, UISessionUtils.getCurrentSession());
					}
					if (createContext != null)
					{
						createContext.setLangIso(isoCode);
						createContext.setBaseType(rootType);
						try
						{
							customParameters.put("createContext", createContext.clone());
						}
						catch (final CloneNotSupportedException e)
						{
							LOG.error(e, e);
						}
					}
				}
			}

			final HtmlBasedComponent editorView = editor.createViewComponent(currentValue, customParameters, editorListener);
			parent.appendChild(editorView);
			if (focus)
			{
				editor.setFocus(editorView, true);
			}
		}
		else
		{
			LOG.warn("UIEditor could not be retrieved");
		}
	}




	//////////////


	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode, final Map params,
			final boolean focus) throws IllegalArgumentException
	{
		if (propDescr == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing property descriptor.");
		}
		if (parent == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing parent component.");
		}
		if (valueContainer == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing value container.");
		}
		if (propDescr.isLocalized())
		{
			renderLocalizedEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus);
		}
		else
		{
			renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, null, focus);
		}
	}

	public static void renderSingleEditor(final TypedObject item, final PropertyDescriptor propDescr,
			final HtmlBasedComponent parent, final ObjectValueContainer valueContainer, final boolean autoPersist,
			final String editorCode, final Map params, final String isoCode, final boolean focus)
	{
		final UIEditor editor = getUIEditor(propDescr, editorCode);
		final de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer
				.getValue(propDescr, isoCode);


		final EditorListener editorListener = new EditorListener()
		{
			@Override
			public void actionPerformed(final String actionCode)
			{
				if ("open_external_clicked".equals(actionCode))
				{
					final Object value = editor.getValue();
					if ((value instanceof Collection) && (editor instanceof ReferenceUIEditor))
					{
						ObjectTemplate type = TypeTools.getValueTypeAsObjectTemplate(propDescr, UISessionUtils.getCurrentSession()
								.getTypeService());
						if (type != null
								&& UISessionUtils.getCurrentSession().getTypeService().getBaseType("VariantProduct")
										.isAssignableFrom(type.getBaseType())
								&& item != null
								&& UISessionUtils.getCurrentSession().getTypeService().getBaseType("Product")
										.isAssignableFrom(item.getType()) && (propDescr instanceof ItemAttributePropertyDescriptor)
								&& ((ItemAttributePropertyDescriptor) propDescr).isSingleAttribute())
						{
							@SuppressWarnings("deprecation")
							final Object variantTypeObject = TypeTools.getObjectAttributeValue(item,
									de.hybris.platform.variants.constants.GeneratedVariantsConstants.Attributes.Product.VARIANTTYPE,
									UISessionUtils.getCurrentSession().getTypeService());
							if ((variantTypeObject instanceof TypedObject)
									&& UISessionUtils.getCurrentSession().getTypeService().getBaseType("ComposedType")
											.isAssignableFrom(((TypedObject) variantTypeObject).getType()))
							{
								@SuppressWarnings("deprecation")
								final String variantTypeCode = (String) TypeTools.getObjectAttributeValue(
										(TypedObject) variantTypeObject, "code", UISessionUtils.getCurrentSession().getTypeService());
								type = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(variantTypeCode);
							}
							else
							{
								type = null;
							}
						}
						final UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
						if ((perspective instanceof BaseUICockpitPerspective) && type != null)
						{
							final Map newParameters = new HashMap();
							if (params != null)
							{
								newParameters.putAll(params);
							}
							newParameters.put("propertyDescriptor", propDescr);
							if (((Collection) value).isEmpty() || !(((Collection) value).iterator().next() instanceof TypedObject))
							{
								((BaseUICockpitPerspective) perspective).openReferenceCollectionInBrowserContext(Collections.EMPTY_LIST,
										type, item, newParameters);
							}
							else
							{
								((BaseUICockpitPerspective) perspective).openReferenceCollectionInBrowserContext((Collection) value,
										type, item, newParameters);
							}
						}
					}
				}
				else if ("escape_pressed".equals(actionCode))
				{
					editor.setValue(valueHolder.getOriginalValue());

				}
			}

			@Override
			public void valueChanged(Object value)
			{
				if ((value instanceof Collection)
						&& de.hybris.platform.cockpit.model.meta.PropertyDescriptor.Multiplicity.SET
								.equals(propDescr.getMultiplicity()))
				{
					value = new HashSet((Collection) value);
				}
				valueHolder.setLocalValue(value);
				if (autoPersist && item != null)
				{
					try
					{
						EditorHelper.persistValues(item, valueContainer,
								Collections.singletonMap("eventSource", params.get("eventSource")));
					}
					catch (final ValueHandlerException e)
					{
						OshEditorHelper.LOG.error("error saving value", e);
					}
				}
				else
				{
					valueHolder.stored();
				}
			}

		};
		renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, isoCode, focus, editorListener);
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode, final Map params,
			final EditorListener editorListener) throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, editorListener, false);
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final boolean autoPersist, final String editorCode, final Map params,
			final EditorListener editorListener, final boolean focus) throws IllegalArgumentException
	{
		if (propDescr == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing property descriptor.");
		}
		if (parent == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing parent component.");
		}
		if (valueContainer == null)
		{
			throw new IllegalArgumentException("Can not create editor. Reason: Missing value container.");
		}
		if (propDescr.isLocalized())
		{
			renderLocalizedEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus, editorListener);
		}
		else
		{
			renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, null, focus, editorListener);
		}
	}

	public static void createEditor(final TypedObject item, final PropertyDescriptor propDescr, final HtmlBasedComponent parent,
			final ObjectValueContainer valueContainer, final EditorListener editorListener, final boolean autoPersist)
			throws IllegalArgumentException
	{
		createEditor(item, propDescr, parent, valueContainer, autoPersist, editorListener, ((String) (null)));
	}



	public static void persistValues(final TypedObject item, final ObjectValueContainer valueContainer)
			throws ValueHandlerException
	{
		persistValues(item, valueContainer, Collections.EMPTY_MAP);
	}

	public static void persistValues(final TypedObject item, final ObjectValueContainer valueContainer, final Map params)
			throws ValueHandlerException
	{
		ObjectValueHandler valueHandler;
		for (final Iterator iterator = UISessionUtils.getCurrentSession().getValueHandlerRegistry()
				.getValueHandlerChain(item.getType()).iterator(); iterator.hasNext(); valueHandler.storeValues(valueContainer))
		{
			valueHandler = (ObjectValueHandler) iterator.next();
		}

		final Set modifiedProperties = new HashSet();
		for (final Iterator iterator1 = valueContainer.getAllValues().iterator(); iterator1.hasNext();)
		{
			final de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder value = (de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder) iterator1
					.next();
			if (value.isModified())
			{
				modifiedProperties.add(value.getPropertyDescriptor());
			}
		}

		final Object source = params.get("eventSource");
		if (CollectionUtils.isNotEmpty(modifiedProperties))
		{
			UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), new ItemChangeUndoableOperation(
					item, valueContainer), null);
			UISessionUtils.getCurrentSession().sendGlobalEvent(new ItemChangedEvent(source, item, modifiedProperties, params));
		}
		valueContainer.stored();
	}

	public static ObjectTemplate processVariantTypeCheck(final ObjectTemplate type, final TypedObject object,
			final PropertyDescriptor propertyDescriptor, final TypeService typeService)
	{
		ObjectTemplate ret = null;
		if (type != null && typeService.getBaseType("VariantProduct").isAssignableFrom(type.getBaseType()) && object != null
				&& typeService.getBaseType("Product").isAssignableFrom(object.getType())
				&& (propertyDescriptor instanceof ItemAttributePropertyDescriptor)
				&& ((ItemAttributePropertyDescriptor) propertyDescriptor).isSingleAttribute())
		{
			@SuppressWarnings("deprecation")
			final Object variantTypeObject = TypeTools.getObjectAttributeValue(object,
					de.hybris.platform.variants.constants.GeneratedVariantsConstants.Attributes.Product.VARIANTTYPE, typeService);
			if ((variantTypeObject instanceof TypedObject)
					&& typeService.getBaseType("ComposedType").isAssignableFrom(((TypedObject) variantTypeObject).getType()))
			{
				@SuppressWarnings("deprecation")
				final String variantTypeCode = (String) TypeTools.getObjectAttributeValue((TypedObject) variantTypeObject, "code",
						typeService);
				ret = typeService.getObjectTemplate(variantTypeCode);
			}
		}
		else
		{
			ret = type;
		}
		return ret;
	}

	public static void renderSingleEditor(final TypedObject item, final PropertyDescriptor propDescr,
			final HtmlBasedComponent parent, final ObjectValueContainer valueContainer, final boolean autoPersist,
			final String editorCode, final Map params, final String isoCode, final boolean focus, final EditorListener editorListener)
	{
		final TypedObject orderEntry = (TypedObject) valueContainer.getObject();
		final ConsignmentModel consignmentModel = (ConsignmentModel) orderEntry.getObject();


		if (StringUtils.isNotBlank(isoCode) && !checkLanguageVisibility(item, isoCode))
		{
			final Label l = new Label(Labels.getLabel("editor.cataloglanguage.hidden", new String[]
			{ isoCode }));
			l.setStyle("color:#ccc;");
			l.setParent(parent);
			return;
		}
		final UIEditor editor = getUIEditor(propDescr, editorCode);
		if (editor != null)
		{
			boolean editable = false;
			if (item == null || UISessionUtils.getCurrentSession().getModelService().isNew(item.getObject()))
			{
				final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
				editable = getUIAccessRightService().isWritable(
						typeService.getObjectType(typeService.getTypeCodeFromPropertyQualifier(propDescr.getQualifier())), propDescr,
						true);
			}
			else
			{
				editable = getUIAccessRightService().isWritable(item.getType(), item, propDescr, false);
			}
			if (editable && propDescr.isLocalized())
			{
				final Set writeableLangIsos = UISessionUtils.getCurrentSession().getSystemService().getAllWriteableLanguageIsos();
				editable = writeableLangIsos != null && writeableLangIsos.contains(isoCode);
			}
			editor.setEditable(editable);

			if (editor instanceof ListUIEditor)
			{
				setAvailableValuesInOrderStatusListEditor((ListUIEditor) editor, propDescr, params, consignmentModel);
			}



			Object currentValue = "";
			final Map customParameters = new HashMap();
			if (params != null)
			{
				customParameters.putAll(params);
			}
			customParameters.put("attributeQualifier", propDescr.getQualifier());
			customParameters.put("parentObject", item);
			if (editor instanceof ReferenceUIEditor)
			{
				final SearchType rootSearchType = null;
				final SearchType rootType = getRootSearchType(propDescr, UISessionUtils.getCurrentSession());
				if (params != null && !StringUtils.isEmpty((String) params.get("allowCreate")))
				{
					((ReferenceUIEditor) editor).setAllowCreate(Boolean.valueOf((String) params.get("allowCreate")));
				}
				else
				{
					((ReferenceUIEditor) editor).setAllowCreate(Boolean.TRUE);
				}
				if (rootType != null)
				{
					((ReferenceUIEditor) editor).setRootType(rootType);
					((ReferenceUIEditor) editor).setRootSearchType(rootSearchType);
				}
				else
				{
					LOG.warn((new StringBuilder(
							"Could not set root type for reference editor (Reason: No root search type could be retrieved for property descriptor '"))
							.append(propDescr).append("').").toString());
				}
				if ((currentValue instanceof Collection) && !((Collection) currentValue).isEmpty()
						&& (((Collection) currentValue).iterator().next() instanceof TypedObject))
				{
					currentValue = UISessionUtils.getCurrentSession().getTypeService().wrapItems((Collection) currentValue);
				}
				else if (!(currentValue instanceof String))
				{
					currentValue = UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentValue);
				}
				CreateContext createContext = null;
				if (params != null)
				{
					createContext = (CreateContext) params.get("createContext");
					if (createContext == null)
					{
						createContext = applyReferenceRelatedAttributes((ReferenceUIEditor) editor, propDescr, params, item,
								currentValue, isoCode, null, UISessionUtils.getCurrentSession());
					}
					if (createContext != null)
					{
						createContext.setLangIso(isoCode);
						createContext.setBaseType(rootType);
						try
						{
							customParameters.put("createContext", createContext.clone());
						}
						catch (final CloneNotSupportedException e)
						{
							LOG.error(e, e);
						}
					}
				}
			}

			final HtmlBasedComponent editorView = editor.createViewComponent(currentValue, customParameters, editorListener);
			parent.appendChild(editorView);
			if (focus)
			{
				editor.setFocus(editorView, true);
			}
		}
		else
		{
			LOG.warn("UIEditor could not be retrieved");
		}
	}


	public static List getAvailableValues(final Map params, final PropertyDescriptor propertyDescriptor)
	{
		List result = null;
		if (params != null)
		{
			final Object param = params.get("allowedValuesList");
			if (param instanceof AvailableValuesProvider)
			{
				result = new ArrayList(UISessionUtils.getCurrentSession().getTypeService()
						.wrapItems(((AvailableValuesProvider) param).getAvailableValues(propertyDescriptor)));
			}
			else if (param instanceof String)
			{
				final AvailableValuesProvider provider = (AvailableValuesProvider) SpringUtil.getBean((String) param);
				result = new ArrayList(UISessionUtils.getCurrentSession().getTypeService()
						.wrapItems(provider.getAvailableValues(propertyDescriptor)));
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static void setAvailableValuesInOrderStatusListEditor(final ListUIEditor editor,
			final PropertyDescriptor propertyDescriptor, final Map params, final ConsignmentModel consignmentModel)
	{
		List availableValues = getAvailableValues(params, propertyDescriptor);
		if (availableValues == null)
		{
			availableValues = UISessionUtils.getCurrentSession().getTypeService().getAvailableValues(propertyDescriptor);
		}

		if (consignmentModel.getCode().contains("online-") || consignmentModel.getCode().contains("dropship-"))
		{
			availableValues.remove(ConsignmentStatus.READY_FOR_PICKUP);
			availableValues.remove(ConsignmentStatus.PARTIAL_RETURNED);
			availableValues.remove(ConsignmentStatus.PARTIAL_FULFILLED);
			availableValues.remove(ConsignmentStatus.CANCELLED);
			availableValues.remove(ConsignmentStatus.RETURNED);
			availableValues.remove(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL);
			availableValues.remove(ConsignmentStatus.WAITING);
			availableValues.remove(ConsignmentStatus.PICKPACK);
			availableValues.remove(ConsignmentStatus.READY);
			availableValues.remove(ConsignmentStatus.COMPLETE);
			availableValues.remove(ConsignmentStatus.PICKEDUP_AT_STORE);
		}

		if (consignmentModel.getCode().contains("store-"))
		{
			availableValues.remove(ConsignmentEntryStatus.SHIPPED);
			availableValues.remove(ConsignmentStatus.PARTIAL_RETURNED);
			availableValues.remove(ConsignmentStatus.PARTIAL_FULFILLED);
			availableValues.remove(ConsignmentStatus.CANCELLED);
			availableValues.remove(ConsignmentStatus.RETURNED);
			availableValues.remove(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL);
			availableValues.remove(ConsignmentStatus.WAITING);
			availableValues.remove(ConsignmentStatus.PICKPACK);
			availableValues.remove(ConsignmentStatus.READY);
			availableValues.remove(ConsignmentStatus.SHIPPED);
			availableValues.remove(ConsignmentStatus.COMPLETE);
		}

		editor.setAvailableValues(filterValues(propertyDescriptor, availableValues));
	}

	public static TypedObject getCatalogVersionIfPresent(final TypedObject item)
	{
		TypedObject ret = null;
		try
		{
			final CatalogTypeService catTypeService = (CatalogTypeService) SpringUtil.getBean("catalogTypeService");
			final String code = item.getType().getCode();
			if (catTypeService.isCatalogVersionAwareType(code))
			{
				final String catVerAttr = catTypeService.getCatalogVersionContainerAttribute(item.getType().getCode());
				@SuppressWarnings("deprecation")
				final Object value = TypeTools.getObjectAttributeValue(item, catVerAttr, UISessionUtils.getCurrentSession()
						.getTypeService());
				if (value instanceof TypedObject)
				{
					ret = (TypedObject) value;
				}
			}
		}
		catch (final Exception e)
		{
			LOG.debug((new StringBuilder("Could not get catalogVersion for ")).append(item).append(", reason: ").toString(), e);
		}
		return ret;
	}

	public static List removeHiddenLanguages(final TypedObject item, final Collection languages)
	{
		if (item == null || CollectionUtils.isEmpty(languages))
		{
			return new ArrayList(languages);
		}
		final List ret = new ArrayList();
		final TypedObject catalogVersion = getCatalogVersionIfPresent(item);
		if (catalogVersion != null && (catalogVersion.getObject() instanceof CatalogVersionModel))
		{
			final Collection catLanguages = ((CatalogVersionModel) catalogVersion.getObject()).getLanguages();
			if (catLanguages == null || catLanguages.isEmpty())
			{
				ret.addAll(languages);
			}
			else
			{
				for (final Iterator iterator = catLanguages.iterator(); iterator.hasNext();)
				{
					final LanguageModel languageModel = (LanguageModel) iterator.next();
					if (languages.contains(languageModel.getIsocode()))
					{
						ret.add(languageModel.getIsocode());
					}
				}

			}
		}
		else
		{
			ret.addAll(languages);
		}
		return ret;
	}

	private static final Logger LOG = Logger.getLogger(OshEditorHelper.class);
	public static final String EVENT_SOURCE = "eventSource";
	public static final String ALLOWED_VALUES_LIST = "allowedValuesList";

}
