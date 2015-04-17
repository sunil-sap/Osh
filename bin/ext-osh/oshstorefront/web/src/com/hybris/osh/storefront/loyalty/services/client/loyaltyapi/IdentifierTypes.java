/**
 * IdentifierTypes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */


package com.hybris.osh.storefront.loyalty.services.client.loyaltyapi;


/**
 * IdentifierTypes bean class
 */
@SuppressWarnings(
{ "unchecked", "unused" })
public class IdentifierTypes implements org.apache.axis2.databinding.ADBBean
{

	public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
			"http://www.loyaltylab.com/loyaltyapi/", "IdentifierTypes", "ns1");



	/**
	 * field for IdentifierTypes
	 */


	protected java.lang.String localIdentifierTypes;

	private static java.util.HashMap _table_ = new java.util.HashMap();

	// Constructor

	public IdentifierTypes(final java.lang.String value, final boolean isRegisterValue)
	{
		localIdentifierTypes = value;
		if (isRegisterValue)
		{

			_table_.put(localIdentifierTypes, this);

		}

	}

	public static final java.lang.String _ShopperId = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ShopperId");

	public static final java.lang.String _EmailAddress = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("EmailAddress");

	public static final java.lang.String _LoyaltyCard = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("LoyaltyCard");

	public static final java.lang.String _PhoneNumber = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("PhoneNumber");

	public static final java.lang.String _RetailerShopperId = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("RetailerShopperId");

	public static final java.lang.String _UserName = org.apache.axis2.databinding.utils.ConverterUtil.convertToString("UserName");

	public static final IdentifierTypes ShopperId = new IdentifierTypes(_ShopperId, true);

	public static final IdentifierTypes EmailAddress = new IdentifierTypes(_EmailAddress, true);

	public static final IdentifierTypes LoyaltyCard = new IdentifierTypes(_LoyaltyCard, true);

	public static final IdentifierTypes PhoneNumber = new IdentifierTypes(_PhoneNumber, true);

	public static final IdentifierTypes RetailerShopperId = new IdentifierTypes(_RetailerShopperId, true);

	public static final IdentifierTypes UserName = new IdentifierTypes(_UserName, true);


	public java.lang.String getValue()
	{
		return localIdentifierTypes;
	}

	@Override
	public boolean equals(final java.lang.Object obj)
	{
		return (obj == this);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public java.lang.String toString()
	{

		return localIdentifierTypes.toString();


	}





	/**
	 * 
	 * @param parentQName
	 * @param factory
	 * @return org.apache.axiom.om.OMElement
	 */
	public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
			final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException
	{



		final org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
		return factory.createOMElement(dataSource, MY_QNAME);

	}

	public void serialize(final javax.xml.namespace.QName parentQName, final javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
	{
		serialize(parentQName, xmlWriter, false);
	}

	public void serialize(final javax.xml.namespace.QName parentQName, final javax.xml.stream.XMLStreamWriter xmlWriter,
			final boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
	{


		//We can safely assume an element has only one type associated with it

		final java.lang.String namespace = parentQName.getNamespaceURI();
		final java.lang.String _localName = parentQName.getLocalPart();

		writeStartElement(null, namespace, _localName, xmlWriter);

		// add the type details if this is used in a simple type
		if (serializeType)
		{
			final java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.loyaltylab.com/loyaltyapi/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
			{
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":IdentifierTypes",
						xmlWriter);
			}
			else
			{
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "IdentifierTypes", xmlWriter);
			}
		}

		if (localIdentifierTypes == null)
		{

			throw new org.apache.axis2.databinding.ADBException("IdentifierTypes cannot be null !!");

		}
		else
		{

			xmlWriter.writeCharacters(localIdentifierTypes);

		}

		xmlWriter.writeEndElement();


	}

	private static java.lang.String generatePrefix(final java.lang.String namespace)
	{
		if (namespace.equals("http://www.loyaltylab.com/loyaltyapi/"))
		{
			return "ns1";
		}
		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/**
	 * Utility method to write an element start tag.
	 */
	private void writeStartElement(java.lang.String prefix, final java.lang.String namespace, final java.lang.String localPart,
			final javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
	{
		final java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
		if (writerPrefix != null)
		{
			xmlWriter.writeStartElement(namespace, localPart);
		}
		else
		{
			if (namespace.length() == 0)
			{
				prefix = "";
			}
			else if (prefix == null)
			{
				prefix = generatePrefix(namespace);
			}

			xmlWriter.writeStartElement(prefix, localPart, namespace);
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}
	}

	/**
	 * Util method to write an attribute with the ns prefix
	 */
	private void writeAttribute(final java.lang.String prefix, final java.lang.String namespace, final java.lang.String attName,
			final java.lang.String attValue, final javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{
		if (xmlWriter.getPrefix(namespace) == null)
		{
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}
		xmlWriter.writeAttribute(namespace, attName, attValue);
	}

	/**
	 * Util method to write an attribute without the ns prefix
	 */
	private void writeAttribute(final java.lang.String namespace, final java.lang.String attName, final java.lang.String attValue,
			final javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
	{
		if (namespace.equals(""))
		{
			xmlWriter.writeAttribute(attName, attValue);
		}
		else
		{
			registerPrefix(xmlWriter, namespace);
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}
	}


	/**
	 * Util method to write an attribute without the ns prefix
	 */
	private void writeQNameAttribute(final java.lang.String namespace, final java.lang.String attName,
			final javax.xml.namespace.QName qname, final javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{

		final java.lang.String attributeNamespace = qname.getNamespaceURI();
		java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
		if (attributePrefix == null)
		{
			attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
		}
		java.lang.String attributeValue;
		if (attributePrefix.trim().length() > 0)
		{
			attributeValue = attributePrefix + ":" + qname.getLocalPart();
		}
		else
		{
			attributeValue = qname.getLocalPart();
		}

		if (namespace.equals(""))
		{
			xmlWriter.writeAttribute(attName, attributeValue);
		}
		else
		{
			registerPrefix(xmlWriter, namespace);
			xmlWriter.writeAttribute(namespace, attName, attributeValue);
		}
	}

	/**
	 * method to handle Qnames
	 */

	private void writeQName(final javax.xml.namespace.QName qname, final javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{
		final java.lang.String namespaceURI = qname.getNamespaceURI();
		if (namespaceURI != null)
		{
			java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
			if (prefix == null)
			{
				prefix = generatePrefix(namespaceURI);
				xmlWriter.writeNamespace(prefix, namespaceURI);
				xmlWriter.setPrefix(prefix, namespaceURI);
			}

			if (prefix.trim().length() > 0)
			{
				xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
			else
			{
				// i.e this is the default namespace
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}

		}
		else
		{
			xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
		}
	}

	private void writeQNames(final javax.xml.namespace.QName[] qnames, final javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{

		if (qnames != null)
		{
			// we have to store this data until last moment since it is not possible to write any
			// namespace data after writing the charactor data
			final java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
			java.lang.String namespaceURI = null;
			java.lang.String prefix = null;

			for (int i = 0; i < qnames.length; i++)
			{
				if (i > 0)
				{
					stringToWrite.append(" ");
				}
				namespaceURI = qnames[i].getNamespaceURI();
				if (namespaceURI != null)
				{
					prefix = xmlWriter.getPrefix(namespaceURI);
					if ((prefix == null) || (prefix.length() == 0))
					{
						prefix = generatePrefix(namespaceURI);
						xmlWriter.writeNamespace(prefix, namespaceURI);
						xmlWriter.setPrefix(prefix, namespaceURI);
					}

					if (prefix.trim().length() > 0)
					{
						stringToWrite.append(prefix).append(":")
								.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
					else
					{
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				else
				{
					stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
				}
			}
			xmlWriter.writeCharacters(stringToWrite.toString());
		}

	}


	/**
	 * Register a namespace prefix
	 */
	private java.lang.String registerPrefix(final javax.xml.stream.XMLStreamWriter xmlWriter, final java.lang.String namespace)
			throws javax.xml.stream.XMLStreamException
	{
		java.lang.String prefix = xmlWriter.getPrefix(namespace);
		if (prefix == null)
		{
			prefix = generatePrefix(namespace);
			final javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
			while (true)
			{
				final java.lang.String uri = nsContext.getNamespaceURI(prefix);
				if (uri == null || uri.length() == 0)
				{
					break;
				}
				prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
			}
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}
		return prefix;
	}



	/**
	 * databinding method to get an XML representation of this object
	 * 
	 */
	public javax.xml.stream.XMLStreamReader getPullParser(final javax.xml.namespace.QName qName)
			throws org.apache.axis2.databinding.ADBException
	{




		//We can safely assume an element has only one type associated with it
		return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME, new java.lang.Object[]
		{ org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
				org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIdentifierTypes) }, null);

	}



	/**
	 * Factory class that keeps the parse method
	 */
	public static class Factory
	{



		public static IdentifierTypes fromValue(final java.lang.String value) throws java.lang.IllegalArgumentException
		{
			final IdentifierTypes enumeration = (IdentifierTypes)

			_table_.get(value);


			if ((enumeration == null) && !((value == null) || (value.equals(""))))
			{
				throw new java.lang.IllegalArgumentException();
			}
			return enumeration;
		}

		public static IdentifierTypes fromString(final java.lang.String value, final java.lang.String namespaceURI)
				throws java.lang.IllegalArgumentException
		{
			try
			{

				return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));


			}
			catch (final java.lang.Exception e)
			{
				throw new java.lang.IllegalArgumentException();
			}
		}

		public static IdentifierTypes fromString(final javax.xml.stream.XMLStreamReader xmlStreamReader,
				final java.lang.String content)
		{
			if (content.indexOf(":") > -1)
			{
				final java.lang.String prefix = content.substring(0, content.indexOf(":"));
				final java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
				return IdentifierTypes.Factory.fromString(content, namespaceUri);
			}
			else
			{
				return IdentifierTypes.Factory.fromString(content, "");
			}
		}


		/**
		 * static method to create the object Precondition: If this object is an element, the current or next start
		 * element starts this object and any intervening reader events are ignorable If this object is not an element, it
		 * is a complex type and the reader is at the event just after the outer start element Postcondition: If this
		 * object is an element, the reader is positioned at its end element If this object is a complex type, the reader
		 * is positioned at the end element of its outer element
		 */
		public static IdentifierTypes parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
		{
			IdentifierTypes object = null;
			// initialize a hash map to keep values
			final java.util.Map attributeMap = new java.util.HashMap();
			final java.util.List extraAttributeList = new java.util.ArrayList<org.apache.axiom.om.OMAttribute>();


			final int event;
			java.lang.String nillableValue = null;
			java.lang.String prefix = "";
			java.lang.String namespaceuri = "";
			try
			{

				while (!reader.isStartElement() && !reader.isEndElement())
				{
					reader.next();
				}




				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				final java.util.Vector handledAttributes = new java.util.Vector();



				while (!reader.isEndElement())
				{
					if (reader.isStartElement() || reader.hasText())
					{

						nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
						if ("true".equals(nillableValue) || "1".equals(nillableValue))
						{
							throw new org.apache.axis2.databinding.ADBException("The element: " + "IdentifierTypes" + "  cannot be null");
						}


						final java.lang.String content = reader.getElementText();

						if (content.indexOf(":") > 0)
						{
							// this seems to be a Qname so find the namespace and send
							prefix = content.substring(0, content.indexOf(":"));
							namespaceuri = reader.getNamespaceURI(prefix);
							object = IdentifierTypes.Factory.fromString(content, namespaceuri);
						}
						else
						{
							// this seems to be not a qname send and empty namespace incase of it is
							// check is done in fromString method
							object = IdentifierTypes.Factory.fromString(content, "");
						}


					}
					else
					{
						reader.next();
					}
				} // end of while loop




			}
			catch (final javax.xml.stream.XMLStreamException e)
			{
				throw new java.lang.Exception(e);
			}

			return object;
		}

	}//end of factory class



}
