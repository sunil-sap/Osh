<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="single-checkout" tagdir="/WEB-INF/tags/desktop/checkout/single" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/checkout/single/summary/getCheckoutCart.json" var="getCheckoutCartUrl" />
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="posData" required="true" type="de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData" %>
<%@ attribute name="storepickup" required="true" type="java.lang.Boolean" %>
<%@ attribute name="shipping" required="true" type="java.lang.Boolean" %>
<%@ attribute name="surcharge" required="true" type="java.lang.Boolean" %>

<script type="text/javascript">
/*<![CDATA[*/
	$.blockUI.defaults.overlayCSS = {};
	$.blockUI.defaults.css = {};
	var fromPayment = false;
	function refreshCartTotals(checkoutCartData)
	{
		$('#cart_totals_div').html($('#cartTotalsTemplate').tmpl(checkoutCartData));
	}
	
	function refreshPage(checkoutCartData)
	{
		var shipping= true;
		//update delivery address, delivery method and payment sections, and cart totals section
		refreshPaymentDetailsSection(checkoutCartData);
		refreshDeliveryAddressSection(checkoutCartData);
		if(shipping && !fromPayment)
			{
			refreshDeliveryMethodSection(checkoutCartData);
			fromPayment = false;
			}
		refreshCartTotals(checkoutCartData);
		updatePlaceOrderButton();
		//$(".giftdata").show();
	}

	function getCheckoutCartDataAndRefreshPage()
	{
		
		$.getJSON('${getCheckoutCartUrl}', function(checkoutCartData) {refreshPage(checkoutCartData);});
		$('.giftData1').show();
	}
	

/*]]>*/
</script>
<c:choose>
<c:when test="${shipping}">
<div class="formsection">
	<single-checkout:summaryFlowDeliveryAddress deliveryAddress="${deliveryAddress}" />	
</div>

</c:when>
<c:otherwise>
<div class="formsection">
 	 <h2><span class="step">1</span><spring:theme code="checkout.summary.store.pickup.address"/></h2>
              <div class="bopisborderbox">
                <div class="cartformwarp">
                  <div class="cartformwarp_inner">
                    <p>${posData.name}</p>
                    <p>${posData.address.line1}</p>
                    <p>${posData.address.town}, ${posData.address.postalCode}</p>
                    <p>${posData.address.phone}</p>
                  </div>
                  <a class="linkgetdirection" href="${request.contextPath}${posData.url}">Get Direction</a></div>
              </div>
</div>
</c:otherwise>
</c:choose>
<c:choose>
<c:when test="${shipping}">
<div class="formsection">
<%-- <c:set var="deliveryModeCode"  value="${cartData.deliveryMode.code}"></c:set> --%>
	<single-checkout:summaryFlowDeliveryMode deliveryMode="${deliveryMode}" deliveryModeCode="${cartData.deliveryMode.code}" surcharge="${surcharge}"/>
</div>
</c:when>
<c:otherwise>
<div class="formsection">
              <h2><span class="step">2</span><spring:theme code="checkout.summary.deliveryMode.shippingmethod"/></h2>
              <div class="bopisborderbox">
                <div class="cartformwarp">
                  <div class="cartformwarp_inner">
                    <p><spring:theme code="osh.product.page.product.pickupinstore"/></p>
                  </div>
                </div>
              </div>
</div>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${!shipping and storepickup}">
<div class="formsection">
	<single-checkout:summaryFlowPayment paymentInfo="${paymentInfo}" deliveryAddress="${deliveryAddress}" shipping="${shipping}" storepickup="${storepickup}"/>
</div>
</c:when>
<c:otherwise>
<div class="formsection">
	<single-checkout:summaryFlowPayment paymentInfo="${paymentInfo}" deliveryAddress="${deliveryAddress}"  shipping="${shipping}" storepickup="${storepickup}"/>
</div>
</c:otherwise>
</c:choose>


<div class="clearb"></div>

<c:if test="${shipping and storepickup}">
<div>
   <single-checkout:storePickUpAddress />          
</div>
</c:if>
 




