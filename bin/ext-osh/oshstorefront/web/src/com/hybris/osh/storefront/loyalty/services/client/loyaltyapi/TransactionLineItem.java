
/**
 * TransactionLineItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.hybris.osh.storefront.loyalty.services.client.loyaltyapi;
            

            /**
            *  TransactionLineItem bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class TransactionLineItem
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = TransactionLineItem
                Namespace URI = http://www.loyaltylab.com/loyaltyapi/
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for DollarRevenue
                        */

                        
                                    protected java.math.BigDecimal localDollarRevenue ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.math.BigDecimal
                           */
                           public  java.math.BigDecimal getDollarRevenue(){
                               return localDollarRevenue;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DollarRevenue
                               */
                               public void setDollarRevenue(java.math.BigDecimal param){
                            
                                            this.localDollarRevenue=param;
                                    

                               }
                            

                        /**
                        * field for ProductId
                        */

                        
                                    protected int localProductId ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getProductId(){
                               return localProductId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ProductId
                               */
                               public void setProductId(int param){
                            
                                            this.localProductId=param;
                                    

                               }
                            

                        /**
                        * field for Quantity
                        */

                        
                                    protected int localQuantity ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getQuantity(){
                               return localQuantity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Quantity
                               */
                               public void setQuantity(int param){
                            
                                            this.localQuantity=param;
                                    

                               }
                            

                        /**
                        * field for OrderDateTime
                        */

                        
                                    protected java.util.Calendar localOrderDateTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getOrderDateTime(){
                               return localOrderDateTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OrderDateTime
                               */
                               public void setOrderDateTime(java.util.Calendar param){
                            
                                            this.localOrderDateTime=param;
                                    

                               }
                            

                        /**
                        * field for SequenceNumber
                        */

                        
                                    protected java.lang.String localSequenceNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSequenceNumberTracker = false ;

                           public boolean isSequenceNumberSpecified(){
                               return localSequenceNumberTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSequenceNumber(){
                               return localSequenceNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SequenceNumber
                               */
                               public void setSequenceNumber(java.lang.String param){
                            localSequenceNumberTracker = param != null;
                                   
                                            this.localSequenceNumber=param;
                                    

                               }
                            

                        /**
                        * field for ShipDateTime
                        */

                        
                                    protected java.util.Calendar localShipDateTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getShipDateTime(){
                               return localShipDateTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ShipDateTime
                               */
                               public void setShipDateTime(java.util.Calendar param){
                            
                                            this.localShipDateTime=param;
                                    

                               }
                            

                        /**
                        * field for SKU
                        */

                        
                                    protected java.lang.String localSKU ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSKUTracker = false ;

                           public boolean isSKUSpecified(){
                               return localSKUTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSKU(){
                               return localSKU;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SKU
                               */
                               public void setSKU(java.lang.String param){
                            localSKUTracker = param != null;
                                   
                                            this.localSKU=param;
                                    

                               }
                            

                        /**
                        * field for LoyaltyLabOrderDetailId
                        */

                        
                                    protected int localLoyaltyLabOrderDetailId ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLoyaltyLabOrderDetailId(){
                               return localLoyaltyLabOrderDetailId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoyaltyLabOrderDetailId
                               */
                               public void setLoyaltyLabOrderDetailId(int param){
                            
                                            this.localLoyaltyLabOrderDetailId=param;
                                    

                               }
                            

                        /**
                        * field for OriginalLoyaltyLabOrderDetailId
                        */

                        
                                    protected int localOriginalLoyaltyLabOrderDetailId ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getOriginalLoyaltyLabOrderDetailId(){
                               return localOriginalLoyaltyLabOrderDetailId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OriginalLoyaltyLabOrderDetailId
                               */
                               public void setOriginalLoyaltyLabOrderDetailId(int param){
                            
                                            this.localOriginalLoyaltyLabOrderDetailId=param;
                                    

                               }
                            

                        /**
                        * field for IsEligibleForAward
                        */

                        
                                    protected boolean localIsEligibleForAward ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getIsEligibleForAward(){
                               return localIsEligibleForAward;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param IsEligibleForAward
                               */
                               public void setIsEligibleForAward(boolean param){
                            
                                            this.localIsEligibleForAward=param;
                                    

                               }
                            

                        /**
                        * field for CreateDateTime
                        */

                        
                                    protected java.util.Calendar localCreateDateTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getCreateDateTime(){
                               return localCreateDateTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CreateDateTime
                               */
                               public void setCreateDateTime(java.util.Calendar param){
                            
                                            this.localCreateDateTime=param;
                                    

                               }
                            

                        /**
                        * field for LastModifiedDateTime
                        */

                        
                                    protected java.util.Calendar localLastModifiedDateTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLastModifiedDateTime(){
                               return localLastModifiedDateTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LastModifiedDateTime
                               */
                               public void setLastModifiedDateTime(java.util.Calendar param){
                            
                                            this.localLastModifiedDateTime=param;
                                    

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
                           namespacePrefix+":TransactionLineItem",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "TransactionLineItem",
                           xmlWriter);
                   }

               
                   }
               
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "DollarRevenue", xmlWriter);
                             

                                          if (localDollarRevenue==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("DollarRevenue cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDollarRevenue));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "ProductId", xmlWriter);
                             
                                               if (localProductId==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("ProductId cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductId));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "Quantity", xmlWriter);
                             
                                               if (localQuantity==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("Quantity cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQuantity));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "OrderDateTime", xmlWriter);
                             

                                          if (localOrderDateTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("OrderDateTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderDateTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localSequenceNumberTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "SequenceNumber", xmlWriter);
                             

                                          if (localSequenceNumber==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("SequenceNumber cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSequenceNumber);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "ShipDateTime", xmlWriter);
                             

                                          if (localShipDateTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("ShipDateTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShipDateTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localSKUTracker){
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "SKU", xmlWriter);
                             

                                          if (localSKU==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("SKU cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSKU);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "LoyaltyLabOrderDetailId", xmlWriter);
                             
                                               if (localLoyaltyLabOrderDetailId==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("LoyaltyLabOrderDetailId cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoyaltyLabOrderDetailId));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "OriginalLoyaltyLabOrderDetailId", xmlWriter);
                             
                                               if (localOriginalLoyaltyLabOrderDetailId==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("OriginalLoyaltyLabOrderDetailId cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginalLoyaltyLabOrderDetailId));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "IsEligibleForAward", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("IsEligibleForAward cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsEligibleForAward));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "CreateDateTime", xmlWriter);
                             

                                          if (localCreateDateTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("CreateDateTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateDateTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.loyaltylab.com/loyaltyapi/";
                                    writeStartElement(null, namespace, "LastModifiedDateTime", xmlWriter);
                             

                                          if (localLastModifiedDateTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("LastModifiedDateTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastModifiedDateTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
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

                
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "DollarRevenue"));
                                 
                                        if (localDollarRevenue != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDollarRevenue));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("DollarRevenue cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "ProductId"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductId));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "Quantity"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQuantity));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "OrderDateTime"));
                                 
                                        if (localOrderDateTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderDateTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("OrderDateTime cannot be null!!");
                                        }
                                     if (localSequenceNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "SequenceNumber"));
                                 
                                        if (localSequenceNumber != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSequenceNumber));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("SequenceNumber cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "ShipDateTime"));
                                 
                                        if (localShipDateTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShipDateTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("ShipDateTime cannot be null!!");
                                        }
                                     if (localSKUTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "SKU"));
                                 
                                        if (localSKU != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSKU));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("SKU cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "LoyaltyLabOrderDetailId"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoyaltyLabOrderDetailId));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "OriginalLoyaltyLabOrderDetailId"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginalLoyaltyLabOrderDetailId));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "IsEligibleForAward"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsEligibleForAward));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "CreateDateTime"));
                                 
                                        if (localCreateDateTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateDateTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("CreateDateTime cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/",
                                                                      "LastModifiedDateTime"));
                                 
                                        if (localLastModifiedDateTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastModifiedDateTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("LastModifiedDateTime cannot be null!!");
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
        public static TransactionLineItem parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            TransactionLineItem object =
                new TransactionLineItem();

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
                    
                            if (!"TransactionLineItem".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (TransactionLineItem)com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","DollarRevenue").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"DollarRevenue" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDollarRevenue(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDecimal(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","ProductId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"ProductId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setProductId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","Quantity").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"Quantity" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setQuantity(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","OrderDateTime").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"OrderDateTime" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOrderDateTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","SequenceNumber").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"SequenceNumber" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSequenceNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","ShipDateTime").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"ShipDateTime" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setShipDateTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","SKU").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"SKU" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSKU(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","LoyaltyLabOrderDetailId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"LoyaltyLabOrderDetailId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoyaltyLabOrderDetailId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","OriginalLoyaltyLabOrderDetailId").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"OriginalLoyaltyLabOrderDetailId" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOriginalLoyaltyLabOrderDetailId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","IsEligibleForAward").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"IsEligibleForAward" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setIsEligibleForAward(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","CreateDateTime").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"CreateDateTime" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCreateDateTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.loyaltylab.com/loyaltyapi/","LastModifiedDateTime").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"LastModifiedDateTime" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLastModifiedDateTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
           
    