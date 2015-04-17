
/**
 * LoyaltyLabAPICallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.hybris.osh.storefront.loyalty.services.client.loyaltyapi;

    /**
     *  LoyaltyLabAPICallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class LoyaltyLabAPICallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public LoyaltyLabAPICallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public LoyaltyLabAPICallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for sendCommunicationToShoppers method
            * override this method for handling normal response from sendCommunicationToShoppers operation
            */
           public void receiveResultsendCommunicationToShoppers(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.SendCommunicationToShoppersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendCommunicationToShoppers operation
           */
            public void receiveErrorsendCommunicationToShoppers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createEventInstance method
            * override this method for handling normal response from createEventInstance operation
            */
           public void receiveResultcreateEventInstance(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateEventInstanceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createEventInstance operation
           */
            public void receiveErrorcreateEventInstance(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addTransaction method
            * override this method for handling normal response from addTransaction operation
            */
           public void receiveResultaddTransaction(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AddTransactionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addTransaction operation
           */
            public void receiveErroraddTransaction(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for scoreShopper method
            * override this method for handling normal response from scoreShopper operation
            */
           public void receiveResultscoreShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ScoreShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from scoreShopper operation
           */
            public void receiveErrorscoreShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShoppersByPhoneNumberFragment method
            * override this method for handling normal response from getShoppersByPhoneNumberFragment operation
            */
           public void receiveResultgetShoppersByPhoneNumberFragment(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShoppersByPhoneNumberFragmentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShoppersByPhoneNumberFragment operation
           */
            public void receiveErrorgetShoppersByPhoneNumberFragment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRewardProductById method
            * override this method for handling normal response from getRewardProductById operation
            */
           public void receiveResultgetRewardProductById(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRewardProductByIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRewardProductById operation
           */
            public void receiveErrorgetRewardProductById(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperRedemptionsByShopperId method
            * override this method for handling normal response from getShopperRedemptionsByShopperId operation
            */
           public void receiveResultgetShopperRedemptionsByShopperId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRedemptionsByShopperIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperRedemptionsByShopperId operation
           */
            public void receiveErrorgetShopperRedemptionsByShopperId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCustomAttribute method
            * override this method for handling normal response from getCustomAttribute operation
            */
           public void receiveResultgetCustomAttribute(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCustomAttributeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCustomAttribute operation
           */
            public void receiveErrorgetCustomAttribute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for redeemOffer method
            * override this method for handling normal response from redeemOffer operation
            */
           public void receiveResultredeemOffer(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RedeemOfferResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from redeemOffer operation
           */
            public void receiveErrorredeemOffer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRewards method
            * override this method for handling normal response from getRewards operation
            */
           public void receiveResultgetRewards(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRewardsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRewards operation
           */
            public void receiveErrorgetRewards(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCurrentPointPrices method
            * override this method for handling normal response from getCurrentPointPrices operation
            */
           public void receiveResultgetCurrentPointPrices(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCurrentPointPricesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCurrentPointPrices operation
           */
            public void receiveErrorgetCurrentPointPrices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRegisteredCard method
            * override this method for handling normal response from createRegisteredCard operation
            */
           public void receiveResultcreateRegisteredCard(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateRegisteredCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRegisteredCard operation
           */
            public void receiveErrorcreateRegisteredCard(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for adjustShopperPointsCustomAttributes method
            * override this method for handling normal response from adjustShopperPointsCustomAttributes operation
            */
           public void receiveResultadjustShopperPointsCustomAttributes(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AdjustShopperPointsCustomAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from adjustShopperPointsCustomAttributes operation
           */
            public void receiveErroradjustShopperPointsCustomAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCurrentPointPrice method
            * override this method for handling normal response from getCurrentPointPrice operation
            */
           public void receiveResultgetCurrentPointPrice(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCurrentPointPriceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCurrentPointPrice operation
           */
            public void receiveErrorgetCurrentPointPrice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getReferenceObjectsFromSearchableCustomEntity method
            * override this method for handling normal response from getReferenceObjectsFromSearchableCustomEntity operation
            */
           public void receiveResultgetReferenceObjectsFromSearchableCustomEntity(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetReferenceObjectsFromSearchableCustomEntityResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getReferenceObjectsFromSearchableCustomEntity operation
           */
            public void receiveErrorgetReferenceObjectsFromSearchableCustomEntity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTierPrograms method
            * override this method for handling normal response from getTierPrograms operation
            */
           public void receiveResultgetTierPrograms(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTierProgramsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTierPrograms operation
           */
            public void receiveErrorgetTierPrograms(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTotalPointsEarnedByRetailerShopperId method
            * override this method for handling normal response from getTotalPointsEarnedByRetailerShopperId operation
            */
           public void receiveResultgetTotalPointsEarnedByRetailerShopperId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTotalPointsEarnedByRetailerShopperIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTotalPointsEarnedByRetailerShopperId operation
           */
            public void receiveErrorgetTotalPointsEarnedByRetailerShopperId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getReferenceObjectFromUniqueCustomEntity method
            * override this method for handling normal response from getReferenceObjectFromUniqueCustomEntity operation
            */
           public void receiveResultgetReferenceObjectFromUniqueCustomEntity(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetReferenceObjectFromUniqueCustomEntityResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getReferenceObjectFromUniqueCustomEntity operation
           */
            public void receiveErrorgetReferenceObjectFromUniqueCustomEntity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperAuthenticationTokenByRetailerID method
            * override this method for handling normal response from getShopperAuthenticationTokenByRetailerID operation
            */
           public void receiveResultgetShopperAuthenticationTokenByRetailerID(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperAuthenticationTokenByRetailerIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperAuthenticationTokenByRetailerID operation
           */
            public void receiveErrorgetShopperAuthenticationTokenByRetailerID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCustomQuestionsForShopper method
            * override this method for handling normal response from getCustomQuestionsForShopper operation
            */
           public void receiveResultgetCustomQuestionsForShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCustomQuestionsForShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCustomQuestionsForShopper operation
           */
            public void receiveErrorgetCustomQuestionsForShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTotalPointsEarnedByEmailAddress method
            * override this method for handling normal response from getTotalPointsEarnedByEmailAddress operation
            */
           public void receiveResultgetTotalPointsEarnedByEmailAddress(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTotalPointsEarnedByEmailAddressResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTotalPointsEarnedByEmailAddress operation
           */
            public void receiveErrorgetTotalPointsEarnedByEmailAddress(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for associatePurchase method
            * override this method for handling normal response from associatePurchase operation
            */
           public void receiveResultassociatePurchase(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AssociatePurchaseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from associatePurchase operation
           */
            public void receiveErrorassociatePurchase(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperTransactionsWithCompleteProductInfo method
            * override this method for handling normal response from getShopperTransactionsWithCompleteProductInfo operation
            */
           public void receiveResultgetShopperTransactionsWithCompleteProductInfo(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperTransactionsWithCompleteProductInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperTransactionsWithCompleteProductInfo operation
           */
            public void receiveErrorgetShopperTransactionsWithCompleteProductInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRewardCatalogById method
            * override this method for handling normal response from getRewardCatalogById operation
            */
           public void receiveResultgetRewardCatalogById(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRewardCatalogByIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRewardCatalogById operation
           */
            public void receiveErrorgetRewardCatalogById(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRewardCatalogsByRewardProductId method
            * override this method for handling normal response from getRewardCatalogsByRewardProductId operation
            */
           public void receiveResultgetRewardCatalogsByRewardProductId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRewardCatalogsByRewardProductIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRewardCatalogsByRewardProductId operation
           */
            public void receiveErrorgetRewardCatalogsByRewardProductId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addShopperToProgram method
            * override this method for handling normal response from addShopperToProgram operation
            */
           public void receiveResultaddShopperToProgram(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AddShopperToProgramResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addShopperToProgram operation
           */
            public void receiveErroraddShopperToProgram(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendCommunicationToShopper method
            * override this method for handling normal response from sendCommunicationToShopper operation
            */
           public void receiveResultsendCommunicationToShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.SendCommunicationToShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendCommunicationToShopper operation
           */
            public void receiveErrorsendCommunicationToShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getEventDefinitions method
            * override this method for handling normal response from getEventDefinitions operation
            */
           public void receiveResultgetEventDefinitions(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetEventDefinitionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getEventDefinitions operation
           */
            public void receiveErrorgetEventDefinitions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperRedemptionsOrderItemsByDate method
            * override this method for handling normal response from getShopperRedemptionsOrderItemsByDate operation
            */
           public void receiveResultgetShopperRedemptionsOrderItemsByDate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRedemptionsOrderItemsByDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperRedemptionsOrderItemsByDate operation
           */
            public void receiveErrorgetShopperRedemptionsOrderItemsByDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPlcAvailablePointsByDate method
            * override this method for handling normal response from getPlcAvailablePointsByDate operation
            */
           public void receiveResultgetPlcAvailablePointsByDate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetPlcAvailablePointsByDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPlcAvailablePointsByDate operation
           */
            public void receiveErrorgetPlcAvailablePointsByDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCSRAuthenticationToken method
            * override this method for handling normal response from getCSRAuthenticationToken operation
            */
           public void receiveResultgetCSRAuthenticationToken(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCSRAuthenticationTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCSRAuthenticationToken operation
           */
            public void receiveErrorgetCSRAuthenticationToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for adjustShopperPointsWithRedemptionCustomAttributeCheck method
            * override this method for handling normal response from adjustShopperPointsWithRedemptionCustomAttributeCheck operation
            */
           public void receiveResultadjustShopperPointsWithRedemptionCustomAttributeCheck(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AdjustShopperPointsWithRedemptionCustomAttributeCheckResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from adjustShopperPointsWithRedemptionCustomAttributeCheck operation
           */
            public void receiveErroradjustShopperPointsWithRedemptionCustomAttributeCheck(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendCommunicationToEmailAddress method
            * override this method for handling normal response from sendCommunicationToEmailAddress operation
            */
           public void receiveResultsendCommunicationToEmailAddress(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.SendCommunicationToEmailAddressResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendCommunicationToEmailAddress operation
           */
            public void receiveErrorsendCommunicationToEmailAddress(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for referFriend method
            * override this method for handling normal response from referFriend operation
            */
           public void receiveResultreferFriend(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ReferFriendResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from referFriend operation
           */
            public void receiveErrorreferFriend(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointBalanceByBalanceType method
            * override this method for handling normal response from getShopperPointBalanceByBalanceType operation
            */
           public void receiveResultgetShopperPointBalanceByBalanceType(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceByBalanceTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointBalanceByBalanceType operation
           */
            public void receiveErrorgetShopperPointBalanceByBalanceType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pointsPurchaseWithPointGroup method
            * override this method for handling normal response from pointsPurchaseWithPointGroup operation
            */
           public void receiveResultpointsPurchaseWithPointGroup(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.PointsPurchaseWithPointGroupResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pointsPurchaseWithPointGroup operation
           */
            public void receiveErrorpointsPurchaseWithPointGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addRewardProductToCatalog method
            * override this method for handling normal response from addRewardProductToCatalog operation
            */
           public void receiveResultaddRewardProductToCatalog(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AddRewardProductToCatalogResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addRewardProductToCatalog operation
           */
            public void receiveErroraddRewardProductToCatalog(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addShopperToTier method
            * override this method for handling normal response from addShopperToTier operation
            */
           public void receiveResultaddShopperToTier(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AddShopperToTierResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addShopperToTier operation
           */
            public void receiveErroraddShopperToTier(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointsExpiring method
            * override this method for handling normal response from getShopperPointsExpiring operation
            */
           public void receiveResultgetShopperPointsExpiring(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointsExpiringResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointsExpiring operation
           */
            public void receiveErrorgetShopperPointsExpiring(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for adjustShopperPointsWithExpirationDate method
            * override this method for handling normal response from adjustShopperPointsWithExpirationDate operation
            */
           public void receiveResultadjustShopperPointsWithExpirationDate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AdjustShopperPointsWithExpirationDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from adjustShopperPointsWithExpirationDate operation
           */
            public void receiveErroradjustShopperPointsWithExpirationDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPurchaseAwardForReturns method
            * override this method for handling normal response from getPurchaseAwardForReturns operation
            */
           public void receiveResultgetPurchaseAwardForReturns(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetPurchaseAwardForReturnsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPurchaseAwardForReturns operation
           */
            public void receiveErrorgetPurchaseAwardForReturns(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enrollShopper method
            * override this method for handling normal response from enrollShopper operation
            */
           public void receiveResultenrollShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.EnrollShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enrollShopper operation
           */
            public void receiveErrorenrollShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPlcAvailablePointsByDateByPointGroup method
            * override this method for handling normal response from getPlcAvailablePointsByDateByPointGroup operation
            */
           public void receiveResultgetPlcAvailablePointsByDateByPointGroup(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetPlcAvailablePointsByDateByPointGroupResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPlcAvailablePointsByDateByPointGroup operation
           */
            public void receiveErrorgetPlcAvailablePointsByDateByPointGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperOffers method
            * override this method for handling normal response from getShopperOffers operation
            */
           public void receiveResultgetShopperOffers(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperOffersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperOffers operation
           */
            public void receiveErrorgetShopperOffers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRewardProduct method
            * override this method for handling normal response from createRewardProduct operation
            */
           public void receiveResultcreateRewardProduct(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateRewardProductResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRewardProduct operation
           */
            public void receiveErrorcreateRewardProduct(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getStores method
            * override this method for handling normal response from getStores operation
            */
           public void receiveResultgetStores(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetStoresResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getStores operation
           */
            public void receiveErrorgetStores(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for redeemReward method
            * override this method for handling normal response from redeemReward operation
            */
           public void receiveResultredeemReward(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RedeemRewardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from redeemReward operation
           */
            public void receiveErrorredeemReward(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createAndScoreShopper method
            * override this method for handling normal response from createAndScoreShopper operation
            */
           public void receiveResultcreateAndScoreShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateAndScoreShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAndScoreShopper operation
           */
            public void receiveErrorcreateAndScoreShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperByRetailerID method
            * override this method for handling normal response from getShopperByRetailerID operation
            */
           public void receiveResultgetShopperByRetailerID(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByRetailerIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperByRetailerID operation
           */
            public void receiveErrorgetShopperByRetailerID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPointPrices method
            * override this method for handling normal response from getPointPrices operation
            */
           public void receiveResultgetPointPrices(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetPointPricesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPointPrices operation
           */
            public void receiveErrorgetPointPrices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importTransaction method
            * override this method for handling normal response from importTransaction operation
            */
           public void receiveResultimportTransaction(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ImportTransactionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importTransaction operation
           */
            public void receiveErrorimportTransaction(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for shopperSignIn method
            * override this method for handling normal response from shopperSignIn operation
            */
           public void receiveResultshopperSignIn(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ShopperSignInResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from shopperSignIn operation
           */
            public void receiveErrorshopperSignIn(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unEnrollShopper method
            * override this method for handling normal response from unEnrollShopper operation
            */
           public void receiveResultunEnrollShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UnEnrollShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unEnrollShopper operation
           */
            public void receiveErrorunEnrollShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperAuthenticationToken method
            * override this method for handling normal response from getShopperAuthenticationToken operation
            */
           public void receiveResultgetShopperAuthenticationToken(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperAuthenticationTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperAuthenticationToken operation
           */
            public void receiveErrorgetShopperAuthenticationToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperByUserName method
            * override this method for handling normal response from getShopperByUserName operation
            */
           public void receiveResultgetShopperByUserName(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByUserNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperByUserName operation
           */
            public void receiveErrorgetShopperByUserName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateRewardProduct method
            * override this method for handling normal response from updateRewardProduct operation
            */
           public void receiveResultupdateRewardProduct(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateRewardProductResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateRewardProduct operation
           */
            public void receiveErrorupdateRewardProduct(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointBalance method
            * override this method for handling normal response from getShopperPointBalance operation
            */
           public void receiveResultgetShopperPointBalance(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointBalance operation
           */
            public void receiveErrorgetShopperPointBalance(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointBalanceByPointGroup method
            * override this method for handling normal response from getShopperPointBalanceByPointGroup operation
            */
           public void receiveResultgetShopperPointBalanceByPointGroup(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceByPointGroupResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointBalanceByPointGroup operation
           */
            public void receiveErrorgetShopperPointBalanceByPointGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTransactionsWithTenders method
            * override this method for handling normal response from getTransactionsWithTenders operation
            */
           public void receiveResultgetTransactionsWithTenders(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTransactionsWithTendersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTransactionsWithTenders operation
           */
            public void receiveErrorgetTransactionsWithTenders(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for adjustShopperPoints method
            * override this method for handling normal response from adjustShopperPoints operation
            */
           public void receiveResultadjustShopperPoints(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AdjustShopperPointsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from adjustShopperPoints operation
           */
            public void receiveErroradjustShopperPoints(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShoppersByPhoneNumber method
            * override this method for handling normal response from getShoppersByPhoneNumber operation
            */
           public void receiveResultgetShoppersByPhoneNumber(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShoppersByPhoneNumberResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShoppersByPhoneNumber operation
           */
            public void receiveErrorgetShoppersByPhoneNumber(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateShopper method
            * override this method for handling normal response from updateShopper operation
            */
           public void receiveResultupdateShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateShopper operation
           */
            public void receiveErrorupdateShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateCustomAttributes method
            * override this method for handling normal response from updateCustomAttributes operation
            */
           public void receiveResultupdateCustomAttributes(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateCustomAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateCustomAttributes operation
           */
            public void receiveErrorupdateCustomAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getProfilesByShopperId method
            * override this method for handling normal response from getProfilesByShopperId operation
            */
           public void receiveResultgetProfilesByShopperId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetProfilesByShopperIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProfilesByShopperId operation
           */
            public void receiveErrorgetProfilesByShopperId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTotalPointsEarnedByShopperId method
            * override this method for handling normal response from getTotalPointsEarnedByShopperId operation
            */
           public void receiveResultgetTotalPointsEarnedByShopperId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTotalPointsEarnedByShopperIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTotalPointsEarnedByShopperId operation
           */
            public void receiveErrorgetTotalPointsEarnedByShopperId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCodesForShopper method
            * override this method for handling normal response from getCodesForShopper operation
            */
           public void receiveResultgetCodesForShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCodesForShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCodesForShopper operation
           */
            public void receiveErrorgetCodesForShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pointsPurchase method
            * override this method for handling normal response from pointsPurchase operation
            */
           public void receiveResultpointsPurchase(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.PointsPurchaseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pointsPurchase operation
           */
            public void receiveErrorpointsPurchase(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getProductBySKU method
            * override this method for handling normal response from getProductBySKU operation
            */
           public void receiveResultgetProductBySKU(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetProductBySKUResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProductBySKU operation
           */
            public void receiveErrorgetProductBySKU(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getReferedFriends method
            * override this method for handling normal response from getReferedFriends operation
            */
           public void receiveResultgetReferedFriends(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetReferedFriendsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getReferedFriends operation
           */
            public void receiveErrorgetReferedFriends(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRewardCatalogByRetailerCatalogId method
            * override this method for handling normal response from getRewardCatalogByRetailerCatalogId operation
            */
           public void receiveResultgetRewardCatalogByRetailerCatalogId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRewardCatalogByRetailerCatalogIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRewardCatalogByRetailerCatalogId operation
           */
            public void receiveErrorgetRewardCatalogByRetailerCatalogId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRewardProductByUniqueSKU method
            * override this method for handling normal response from getRewardProductByUniqueSKU operation
            */
           public void receiveResultgetRewardProductByUniqueSKU(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRewardProductByUniqueSKUResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRewardProductByUniqueSKU operation
           */
            public void receiveErrorgetRewardProductByUniqueSKU(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for redeemShopperRewardCerificate method
            * override this method for handling normal response from redeemShopperRewardCerificate operation
            */
           public void receiveResultredeemShopperRewardCerificate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RedeemShopperRewardCerificateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from redeemShopperRewardCerificate operation
           */
            public void receiveErrorredeemShopperRewardCerificate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changePointStateByLoyaltyLabId method
            * override this method for handling normal response from changePointStateByLoyaltyLabId operation
            */
           public void receiveResultchangePointStateByLoyaltyLabId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ChangePointStateByLoyaltyLabIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changePointStateByLoyaltyLabId operation
           */
            public void receiveErrorchangePointStateByLoyaltyLabId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCodesWithSkuForShopper method
            * override this method for handling normal response from getCodesWithSkuForShopper operation
            */
           public void receiveResultgetCodesWithSkuForShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCodesWithSkuForShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCodesWithSkuForShopper operation
           */
            public void receiveErrorgetCodesWithSkuForShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperByMergedVictimID method
            * override this method for handling normal response from getShopperByMergedVictimID operation
            */
           public void receiveResultgetShopperByMergedVictimID(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByMergedVictimIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperByMergedVictimID operation
           */
            public void receiveErrorgetShopperByMergedVictimID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopper method
            * override this method for handling normal response from getShopper operation
            */
           public void receiveResultgetShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopper operation
           */
            public void receiveErrorgetShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperRedemptionsByDate method
            * override this method for handling normal response from getShopperRedemptionsByDate operation
            */
           public void receiveResultgetShopperRedemptionsByDate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRedemptionsByDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperRedemptionsByDate operation
           */
            public void receiveErrorgetShopperRedemptionsByDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mergeShoppers method
            * override this method for handling normal response from mergeShoppers operation
            */
           public void receiveResultmergeShoppers(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.MergeShoppersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mergeShoppers operation
           */
            public void receiveErrormergeShoppers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTransactionsByDate method
            * override this method for handling normal response from getTransactionsByDate operation
            */
           public void receiveResultgetTransactionsByDate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTransactionsByDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTransactionsByDate operation
           */
            public void receiveErrorgetTransactionsByDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendCommunicationToEmailAddresses method
            * override this method for handling normal response from sendCommunicationToEmailAddresses operation
            */
           public void receiveResultsendCommunicationToEmailAddresses(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.SendCommunicationToEmailAddressesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendCommunicationToEmailAddresses operation
           */
            public void receiveErrorsendCommunicationToEmailAddresses(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mergeAccounts method
            * override this method for handling normal response from mergeAccounts operation
            */
           public void receiveResultmergeAccounts(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.MergeAccountsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mergeAccounts operation
           */
            public void receiveErrormergeAccounts(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRewardRedemptionByPointGroup method
            * override this method for handling normal response from createRewardRedemptionByPointGroup operation
            */
           public void receiveResultcreateRewardRedemptionByPointGroup(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateRewardRedemptionByPointGroupResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRewardRedemptionByPointGroup operation
           */
            public void receiveErrorcreateRewardRedemptionByPointGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShoppersByRegisteredCard method
            * override this method for handling normal response from getShoppersByRegisteredCard operation
            */
           public void receiveResultgetShoppersByRegisteredCard(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShoppersByRegisteredCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShoppersByRegisteredCard operation
           */
            public void receiveErrorgetShoppersByRegisteredCard(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperRegisteredCards method
            * override this method for handling normal response from getShopperRegisteredCards operation
            */
           public void receiveResultgetShopperRegisteredCards(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRegisteredCardsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperRegisteredCards operation
           */
            public void receiveErrorgetShopperRegisteredCards(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateCustomQuestionForShopper method
            * override this method for handling normal response from updateCustomQuestionForShopper operation
            */
           public void receiveResultupdateCustomQuestionForShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateCustomQuestionForShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateCustomQuestionForShopper operation
           */
            public void receiveErrorupdateCustomQuestionForShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCustomEntitiesByReferenceTag method
            * override this method for handling normal response from getCustomEntitiesByReferenceTag operation
            */
           public void receiveResultgetCustomEntitiesByReferenceTag(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetCustomEntitiesByReferenceTagResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCustomEntitiesByReferenceTag operation
           */
            public void receiveErrorgetCustomEntitiesByReferenceTag(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointBalanceByDate method
            * override this method for handling normal response from getShopperPointBalanceByDate operation
            */
           public void receiveResultgetShopperPointBalanceByDate(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceByDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointBalanceByDate operation
           */
            public void receiveErrorgetShopperPointBalanceByDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRegisteredCard method
            * override this method for handling normal response from getRegisteredCard operation
            */
           public void receiveResultgetRegisteredCard(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetRegisteredCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRegisteredCard operation
           */
            public void receiveErrorgetRegisteredCard(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createUnregisteredShopper method
            * override this method for handling normal response from createUnregisteredShopper operation
            */
           public void receiveResultcreateUnregisteredShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateUnregisteredShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createUnregisteredShopper operation
           */
            public void receiveErrorcreateUnregisteredShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTransactions method
            * override this method for handling normal response from getTransactions operation
            */
           public void receiveResultgetTransactions(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTransactionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTransactions operation
           */
            public void receiveErrorgetTransactions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createShopperWithCard method
            * override this method for handling normal response from createShopperWithCard operation
            */
           public void receiveResultcreateShopperWithCard(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateShopperWithCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createShopperWithCard operation
           */
            public void receiveErrorcreateShopperWithCard(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for removeRewardProductFromCatalog method
            * override this method for handling normal response from removeRewardProductFromCatalog operation
            */
           public void receiveResultremoveRewardProductFromCatalog(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RemoveRewardProductFromCatalogResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeRewardProductFromCatalog operation
           */
            public void receiveErrorremoveRewardProductFromCatalog(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reversePurchaseAwardForReturns method
            * override this method for handling normal response from reversePurchaseAwardForReturns operation
            */
           public void receiveResultreversePurchaseAwardForReturns(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ReversePurchaseAwardForReturnsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reversePurchaseAwardForReturns operation
           */
            public void receiveErrorreversePurchaseAwardForReturns(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointBalanceByPointState method
            * override this method for handling normal response from getShopperPointBalanceByPointState operation
            */
           public void receiveResultgetShopperPointBalanceByPointState(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceByPointStateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointBalanceByPointState operation
           */
            public void receiveErrorgetShopperPointBalanceByPointState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changePointStateByExternalId method
            * override this method for handling normal response from changePointStateByExternalId operation
            */
           public void receiveResultchangePointStateByExternalId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ChangePointStateByExternalIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changePointStateByExternalId operation
           */
            public void receiveErrorchangePointStateByExternalId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperByRegisteredCard method
            * override this method for handling normal response from getShopperByRegisteredCard operation
            */
           public void receiveResultgetShopperByRegisteredCard(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByRegisteredCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperByRegisteredCard operation
           */
            public void receiveErrorgetShopperByRegisteredCard(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperByEmail method
            * override this method for handling normal response from getShopperByEmail operation
            */
           public void receiveResultgetShopperByEmail(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByEmailResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperByEmail operation
           */
            public void receiveErrorgetShopperByEmail(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperByID method
            * override this method for handling normal response from getShopperByID operation
            */
           public void receiveResultgetShopperByID(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperByID operation
           */
            public void receiveErrorgetShopperByID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTotalPointsEarnedByLoyaltyCardNumber method
            * override this method for handling normal response from getTotalPointsEarnedByLoyaltyCardNumber operation
            */
           public void receiveResultgetTotalPointsEarnedByLoyaltyCardNumber(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetTotalPointsEarnedByLoyaltyCardNumberResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTotalPointsEarnedByLoyaltyCardNumber operation
           */
            public void receiveErrorgetTotalPointsEarnedByLoyaltyCardNumber(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for authenticateUser method
            * override this method for handling normal response from authenticateUser operation
            */
           public void receiveResultauthenticateUser(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticateUserResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from authenticateUser operation
           */
            public void receiveErrorauthenticateUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateCustomEntity method
            * override this method for handling normal response from updateCustomEntity operation
            */
           public void receiveResultupdateCustomEntity(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateCustomEntityResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateCustomEntity operation
           */
            public void receiveErrorupdateCustomEntity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRewardRedemptionByPointGroupAndCustomAttributes method
            * override this method for handling normal response from createRewardRedemptionByPointGroupAndCustomAttributes operation
            */
           public void receiveResultcreateRewardRedemptionByPointGroupAndCustomAttributes(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateRewardRedemptionByPointGroupAndCustomAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRewardRedemptionByPointGroupAndCustomAttributes operation
           */
            public void receiveErrorcreateRewardRedemptionByPointGroupAndCustomAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPrograms method
            * override this method for handling normal response from getShopperPrograms operation
            */
           public void receiveResultgetShopperPrograms(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperProgramsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPrograms operation
           */
            public void receiveErrorgetShopperPrograms(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperOffersExtended method
            * override this method for handling normal response from getShopperOffersExtended operation
            */
           public void receiveResultgetShopperOffersExtended(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperOffersExtendedResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperOffersExtended operation
           */
            public void receiveErrorgetShopperOffersExtended(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperPointBalanceByRetailerID method
            * override this method for handling normal response from getShopperPointBalanceByRetailerID operation
            */
           public void receiveResultgetShopperPointBalanceByRetailerID(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceByRetailerIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperPointBalanceByRetailerID operation
           */
            public void receiveErrorgetShopperPointBalanceByRetailerID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateShopperOfferStatus method
            * override this method for handling normal response from updateShopperOfferStatus operation
            */
           public void receiveResultupdateShopperOfferStatus(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateShopperOfferStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateShopperOfferStatus operation
           */
            public void receiveErrorupdateShopperOfferStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRewardRedemption method
            * override this method for handling normal response from createRewardRedemption operation
            */
           public void receiveResultcreateRewardRedemption(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateRewardRedemptionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRewardRedemption operation
           */
            public void receiveErrorcreateRewardRedemption(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for adjustShopperPointsWithExpirationDateCustomAttributes method
            * override this method for handling normal response from adjustShopperPointsWithExpirationDateCustomAttributes operation
            */
           public void receiveResultadjustShopperPointsWithExpirationDateCustomAttributes(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AdjustShopperPointsWithExpirationDateCustomAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from adjustShopperPointsWithExpirationDateCustomAttributes operation
           */
            public void receiveErroradjustShopperPointsWithExpirationDateCustomAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateShopperPointsBalanceWithCustomAttributes method
            * override this method for handling normal response from updateShopperPointsBalanceWithCustomAttributes operation
            */
           public void receiveResultupdateShopperPointsBalanceWithCustomAttributes(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateShopperPointsBalanceWithCustomAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateShopperPointsBalanceWithCustomAttributes operation
           */
            public void receiveErrorupdateShopperPointsBalanceWithCustomAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createShopper method
            * override this method for handling normal response from createShopper operation
            */
           public void receiveResultcreateShopper(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.CreateShopperResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createShopper operation
           */
            public void receiveErrorcreateShopper(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getShopperRedemptionsByLoyaltyProgramId method
            * override this method for handling normal response from getShopperRedemptionsByLoyaltyProgramId operation
            */
           public void receiveResultgetShopperRedemptionsByLoyaltyProgramId(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRedemptionsByLoyaltyProgramIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShopperRedemptionsByLoyaltyProgramId operation
           */
            public void receiveErrorgetShopperRedemptionsByLoyaltyProgramId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importTransactions method
            * override this method for handling normal response from importTransactions operation
            */
           public void receiveResultimportTransactions(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ImportTransactionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importTransactions operation
           */
            public void receiveErrorimportTransactions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateCustomAttribute method
            * override this method for handling normal response from updateCustomAttribute operation
            */
           public void receiveResultupdateCustomAttribute(
                    com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.UpdateCustomAttributeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateCustomAttribute operation
           */
            public void receiveErrorupdateCustomAttribute(java.lang.Exception e) {
            }
                


    }
    