
/**
 * CustomEntitiesWrapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.hybris.osh.storefront.loyalty.services.client.loyaltyapi;
            

            /**
            *  CustomEntitiesWrapper bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class CustomEntitiesWrapper
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = CustomEntitiesWrapper
                Namespace URI = http://www.loyaltylab.com/loyaltyapi/
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for Shopper
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Shopper localShopper ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localShopperTracker = false ;

                           public boolean isShopperSpecified(){
                               return localShopperTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.Shopper
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Shopper getShopper(){
                               return localShopper;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Shopper
                               */
                               public void setShopper(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Shopper param){
                            localShopperTracker = param != null;
                                   
                                            this.localShopper=param;
                                    

                               }
                            

                        /**
                        * field for Redemption
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Redemption localRedemption ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRedemptionTracker = false ;

                           public boolean isRedemptionSpecified(){
                               return localRedemptionTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.Redemption
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Redemption getRedemption(){
                               return localRedemption;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Redemption
                               */
                               public void setRedemption(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Redemption param){
                            localRedemptionTracker = param != null;
                                   
                                            this.localRedemption=param;
                                    

                               }
                            

                        /**
                        * field for RewardProduct
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RewardProduct localRewardProduct ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRewardProductTracker = false ;

                           public boolean isRewardProductSpecified(){
                               return localRewardProductTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.RewardProduct
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RewardProduct getRewardProduct(){
                               return localRewardProduct;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RewardProduct
                               */
                               public void setRewardProduct(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RewardProduct param){
                            localRewardProductTracker = param != null;
                                   
                                            this.localRewardProduct=param;
                                    

                               }
                            

                        /**
                        * field for CustomEntities
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfAPICustomEntity localCustomEntities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCustomEntitiesTracker = false ;

                           public boolean isCustomEntitiesSpecified(){
                               return localCustomEntitiesTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.ArrayOfAPICustomEntity
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfAPICustomEntity getCustomEntities(){
                               return localCustomEntities;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CustomEntities
                               */
                               public void setCustomEntities(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfAPICustomEntity param){
                            localCustomEntitiesTracker = param != null;
                                   
                                            this.localCustomEntities=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.loyaltylab.com/loyaltyapi/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":CustomEntitiesWrapper",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "CustomEntitiesWrapper",
                           xmlWriter);
                   }

               
                   }
                if (localShopperTracker){
                                            if (localShopper==null){
                                                 throw new org.apache.axis2.databinding.ADBException("Shopper cannot be null!!");
                                            }
                                           localShopper.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Shopper"),
                                               xmlWriter);
                                        } if (localRedemptionTracker){
                                            if (localRedemption==null){
                                                 throw new org.apache.axis2.databinding.ADBException("Redemption cannot be null!!");
                                            }
                                           localRedemption.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Redemption"),
                                               xmlWriter);
                                        } if (localRewardProductTracker){
                                            if (localRewardProduct==null){
                                                 throw new org.apache.axis2.databinding.ADBException("RewardProduct cannot be null!!");
                                            }
                                           localRewardProduct.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","RewardProduct"),
                                               xmlWriter);
                                        } if (localCustomEntitiesTracker){
                                            if (localCustomEntities==null){
                                                 throw new org.apache.axis2.databinding.ADBException("CustomEntities cannot be null!!");
                                            }
                                           localCustomEntities.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","CustomEntities"),
                                               xmlWriter);
                                        }
                    xmlWriter.writeEndElement();
               

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.loyaltylab.com/loyaltyapi/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
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
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localShopperTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "Shopper"));
                            
                            
                                    if (localShopper==null){
                                         throw new org.apache.axis2.databinding.ADBException("Shopper cannot be null!!");
                                    }
                                    elementList.add(localShopper);
                                } if (localRedemptionTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "Redemption"));
                            
                            
                                    if (localRedemption==null){
                                         throw new org.apache.axis2.databinding.ADBException("Redemption cannot be null!!");
                                    }
                                    elementList.add(localRedemption);
                                } if (localRewardProductTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "RewardProduct"));
                            
                            
                                    if (localRewardProduct==null){
                                         throw new org.apache.axis2.databinding.ADBException("RewardProduct cannot be null!!");
                                    }
                                    elementList.add(localRewardProduct);
                                } if (localCustomEntitiesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "CustomEntities"));
                            
                            
                                    if (localCustomEntities==null){
                                         throw new org.apache.axis2.databinding.ADBException("CustomEntities cannot be null!!");
                                    }
                                    elementList.add(localCustomEntities);
                                }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static CustomEntitiesWrapper parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            CustomEntitiesWrapper object =
                new CustomEntitiesWrapper();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"CustomEntitiesWrapper".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (CustomEntitiesWrapper)com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Shopper").equals(reader.getName())){
                                
                                                object.setShopper(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Shopper.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Redemption").equals(reader.getName())){
                                
                                                object.setRedemption(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Redemption.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","RewardProduct").equals(reader.getName())){
                                
                                                object.setRewardProduct(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RewardProduct.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","CustomEntities").equals(reader.getName())){
                                
                                                object.setCustomEntities(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfAPICustomEntity.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                  
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    