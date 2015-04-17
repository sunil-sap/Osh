
/**
 * PurchaseTransaction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.hybris.osh.storefront.loyalty.services.client.loyaltyapi;
            

            /**
            *  PurchaseTransaction bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class PurchaseTransaction
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = PurchaseTransaction
                Namespace URI = http://www.loyaltylab.com/loyaltyapi/
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for ShopperIdentifier
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ShopperIdentifier localShopperIdentifier ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localShopperIdentifierTracker = false ;

                           public boolean isShopperIdentifierSpecified(){
                               return localShopperIdentifierTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.ShopperIdentifier
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ShopperIdentifier getShopperIdentifier(){
                               return localShopperIdentifier;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ShopperIdentifier
                               */
                               public void setShopperIdentifier(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ShopperIdentifier param){
                            localShopperIdentifierTracker = param != null;
                                   
                                            this.localShopperIdentifier=param;
                                    

                               }
                            

                        /**
                        * field for TransactionId
                        */

                        
                                    protected java.lang.String localTransactionId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTransactionIdTracker = false ;

                           public boolean isTransactionIdSpecified(){
                               return localTransactionIdTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getTransactionId(){
                               return localTransactionId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TransactionId
                               */
                               public void setTransactionId(java.lang.String param){
                            localTransactionIdTracker = param != null;
                                   
                                            this.localTransactionId=param;
                                    

                               }
                            

                        /**
                        * field for StoreId
                        */

                        
                                    protected java.lang.String localStoreId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStoreIdTracker = false ;

                           public boolean isStoreIdSpecified(){
                               return localStoreIdTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getStoreId(){
                               return localStoreId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StoreId
                               */
                               public void setStoreId(java.lang.String param){
                            localStoreIdTracker = param != null;
                                   
                                            this.localStoreId=param;
                                    

                               }
                            

                        /**
                        * field for SalesAssociateId
                        */

                        
                                    protected java.lang.String localSalesAssociateId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSalesAssociateIdTracker = false ;

                           public boolean isSalesAssociateIdSpecified(){
                               return localSalesAssociateIdTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSalesAssociateId(){
                               return localSalesAssociateId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SalesAssociateId
                               */
                               public void setSalesAssociateId(java.lang.String param){
                            localSalesAssociateIdTracker = param != null;
                                   
                                            this.localSalesAssociateId=param;
                                    

                               }
                            

                        /**
                        * field for RegisterId
                        */

                        
                                    protected java.lang.String localRegisterId ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRegisterIdTracker = false ;

                           public boolean isRegisterIdSpecified(){
                               return localRegisterIdTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRegisterId(){
                               return localRegisterId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RegisterId
                               */
                               public void setRegisterId(java.lang.String param){
                            localRegisterIdTracker = param != null;
                                   
                                            this.localRegisterId=param;
                                    

                               }
                            

                        /**
                        * field for ReceiptDateTime
                        */

                        
                                    protected java.util.Calendar localReceiptDateTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localReceiptDateTimeTracker = false ;

                           public boolean isReceiptDateTimeSpecified(){
                               return localReceiptDateTimeTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getReceiptDateTime(){
                               return localReceiptDateTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ReceiptDateTime
                               */
                               public void setReceiptDateTime(java.util.Calendar param){
                            localReceiptDateTimeTracker = param != null;
                                   
                                            this.localReceiptDateTime=param;
                                    

                               }
                            

                        /**
                        * field for LineItems
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfLineItem localLineItems ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLineItemsTracker = false ;

                           public boolean isLineItemsSpecified(){
                               return localLineItemsTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.ArrayOfLineItem
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfLineItem getLineItems(){
                               return localLineItems;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LineItems
                               */
                               public void setLineItems(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfLineItem param){
                            localLineItemsTracker = param != null;
                                   
                                            this.localLineItems=param;
                                    

                               }
                            

                        /**
                        * field for Tenders
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfTender localTenders ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTendersTracker = false ;

                           public boolean isTendersSpecified(){
                               return localTendersTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.ArrayOfTender
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfTender getTenders(){
                               return localTenders;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tenders
                               */
                               public void setTenders(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfTender param){
                            localTendersTracker = param != null;
                                   
                                            this.localTenders=param;
                                    

                               }
                            

                        /**
                        * field for Coupons
                        */

                        
                                    protected com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfCoupon localCoupons ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCouponsTracker = false ;

                           public boolean isCouponsSpecified(){
                               return localCouponsTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.loyaltylab.www.loyaltyapi.ArrayOfCoupon
                           */
                           public  com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfCoupon getCoupons(){
                               return localCoupons;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Coupons
                               */
                               public void setCoupons(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfCoupon param){
                            localCouponsTracker = param != null;
                                   
                                            this.localCoupons=param;
                                    

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
                           namespacePrefix+":PurchaseTransaction",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "PurchaseTransaction",
                           xmlWriter);
                   }

               
                   }
                if (localShopperIdentifierTracker){
                                            if (localShopperIdentifier==null){
                                                 throw new org.apache.axis2.databinding.ADBException("ShopperIdentifier cannot be null!!");
                                            }
                                           localShopperIdentifier.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","ShopperIdentifier"),
                                               xmlWriter);
                                        } if (localTransactionIdTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "TransactionId", xmlWriter);
                             

                                          if (localTransactionId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("TransactionId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localTransactionId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localStoreIdTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "StoreId", xmlWriter);
                             

                                          if (localStoreId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("StoreId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localStoreId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSalesAssociateIdTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "SalesAssociateId", xmlWriter);
                             

                                          if (localSalesAssociateId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("SalesAssociateId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSalesAssociateId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRegisterIdTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "RegisterId", xmlWriter);
                             

                                          if (localRegisterId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("RegisterId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRegisterId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localReceiptDateTimeTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "ReceiptDateTime", xmlWriter);
                             

                                          if (localReceiptDateTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("ReceiptDateTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReceiptDateTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLineItemsTracker){
                                            if (localLineItems==null){
                                                 throw new org.apache.axis2.databinding.ADBException("LineItems cannot be null!!");
                                            }
                                           localLineItems.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","LineItems"),
                                               xmlWriter);
                                        } if (localTendersTracker){
                                            if (localTenders==null){
                                                 throw new org.apache.axis2.databinding.ADBException("Tenders cannot be null!!");
                                            }
                                           localTenders.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Tenders"),
                                               xmlWriter);
                                        } if (localCouponsTracker){
                                            if (localCoupons==null){
                                                 throw new org.apache.axis2.databinding.ADBException("Coupons cannot be null!!");
                                            }
                                           localCoupons.serialize(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Coupons"),
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

                 if (localShopperIdentifierTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "ShopperIdentifier"));
                            
                            
                                    if (localShopperIdentifier==null){
                                         throw new org.apache.axis2.databinding.ADBException("ShopperIdentifier cannot be null!!");
                                    }
                                    elementList.add(localShopperIdentifier);
                                } if (localTransactionIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "TransactionId"));
                                 
                                        if (localTransactionId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTransactionId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("TransactionId cannot be null!!");
                                        }
                                    } if (localStoreIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "StoreId"));
                                 
                                        if (localStoreId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStoreId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("StoreId cannot be null!!");
                                        }
                                    } if (localSalesAssociateIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "SalesAssociateId"));
                                 
                                        if (localSalesAssociateId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSalesAssociateId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("SalesAssociateId cannot be null!!");
                                        }
                                    } if (localRegisterIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "RegisterId"));
                                 
                                        if (localRegisterId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegisterId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("RegisterId cannot be null!!");
                                        }
                                    } if (localReceiptDateTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "ReceiptDateTime"));
                                 
                                        if (localReceiptDateTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReceiptDateTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("ReceiptDateTime cannot be null!!");
                                        }
                                    } if (localLineItemsTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "LineItems"));
                            
                            
                                    if (localLineItems==null){
                                         throw new org.apache.axis2.databinding.ADBException("LineItems cannot be null!!");
                                    }
                                    elementList.add(localLineItems);
                                } if (localTendersTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "Tenders"));
                            
                            
                                    if (localTenders==null){
                                         throw new org.apache.axis2.databinding.ADBException("Tenders cannot be null!!");
                                    }
                                    elementList.add(localTenders);
                                } if (localCouponsTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "Coupons"));
                            
                            
                                    if (localCoupons==null){
                                         throw new org.apache.axis2.databinding.ADBException("Coupons cannot be null!!");
                                    }
                                    elementList.add(localCoupons);
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
        public static PurchaseTransaction parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            PurchaseTransaction object =
                new PurchaseTransaction();

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
                    
                            if (!"PurchaseTransaction".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (PurchaseTransaction)com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","ShopperIdentifier").equals(reader.getName())){
                                
                                                object.setShopperIdentifier(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ShopperIdentifier.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","TransactionId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"TransactionId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTransactionId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","StoreId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"StoreId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStoreId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","SalesAssociateId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"SalesAssociateId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSalesAssociateId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","RegisterId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"RegisterId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRegisterId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","ReceiptDateTime").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"ReceiptDateTime" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setReceiptDateTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","LineItems").equals(reader.getName())){
                                
                                                object.setLineItems(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfLineItem.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Tenders").equals(reader.getName())){
                                
                                                object.setTenders(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfTender.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Coupons").equals(reader.getName())){
                                
                                                object.setCoupons(com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfCoupon.Factory.parse(reader));
                                              
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
           
    