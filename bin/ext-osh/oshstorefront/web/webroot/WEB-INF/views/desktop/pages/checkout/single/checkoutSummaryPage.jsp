<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout"%>
<%@ taglib prefix="single-checkout"
	tagdir="/WEB-INF/tags/desktop/checkout/single"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">


<head>
<%-- Meta Content --%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="description" content="${metaDescription}"/>
	<meta name="keywords" content="${metaKeywords}"/>
	<meta name="robots" content="${metaRobots}"/>
<template:javaScript />
<template:checkoutLogin />
<%-- <template:styleSheets /> --%>
</head>
<body>
<spring:url value="/checkout/single/placeOrder" var="placeOrderUrl" />
<img src='${commonResourcePath}/images/spinner.gif' style="display: none;" id="loadingimg"/>
<c:set value="${not empty paymentInfo}" var="paymentInfoOk" />
<c:set
	value="${not empty paymentInfo and not empty paymentInfo.billingAddress}"
	var="billingAddressOk" />
<c:set value="${not empty deliveryMode}" var="deliveryModeOk" />
<c:set value="${not empty deliveryAddress}" var="deliveryAddressOk" />

<script type="text/javascript">
/*<![CDATA][*/
           
         function showMessage(){
        	 if(${deliveryAddressOk==true && deliveryModeOk==true && billingAddressOk==true}){
        		 $(".note_text").show();
        		 
        	 }
         }
         $(document).ready(function() {
        	 showMessage();
     	});
        

/*]]>*/
</script>
<script type="text/javascript">
/*<![CDATA[*/

	function placeOrderWithSecurityCode()
 {
  
	$("#loginmainBody").block({ message: $("#loadingimg") }); 
  document.getElementById("placeorderid").disabled=true;
  document.getElementById("placeorderid1").disabled=true;
  var securityCode = $("#SecurityCode").val();
  if(securityCode=="Cvv No")
   {
   securityCode="";   
   }
  
  var giftMessage=$("#giftMessage").val();
  var isGift=$("#isgift").val();
  
  if(isGift==null || isGift=="")
   {
   $(".giftClass").val(false);
   }
  
  else   {
   $(".giftClass").val(isGift);
   }
  
  
  
  $(".giftMessageClass").val(giftMessage);   
  document.getElementById("placeOrderForm1").submit();
 }
	
	$(document).ready(function(){
		$("#Terms1").click(function() {
			var terms1enable = $('#Terms1').attr("checked");
			if(terms1enable == undefined || terms1enable == 'false'){
				$('#Terms2').attr("checked",false);
			} else {
				$('#Terms2').attr("checked",true);
			}
		});
		
		$("#Terms2").click(function() {
			var terms2enable = $('#Terms2').attr("checked");
			if(terms2enable == undefined || terms2enable == 'false')
			{				
				$('#Terms1').attr("checked",false);
			} else {
				$('#Terms1').attr("checked",true);
			}
		});
	});

	$(document).ready(function() {
		updatePlaceOrderButton();
	});

	function updatePlaceOrderButton()
	{
		var deliveryAddress = $("#checkout_summary_deliveryaddress_div").hasClass("complete");
		var deliveryMode = $("#checkout_summary_deliverymode_div").hasClass("complete");
		var paymentDetails = $("#checkout_summary_payment_div").hasClass("complete");

		if (deliveryAddress && deliveryMode && paymentDetails)
		{
			$(".place-order").removeAttr('disabled');
		}
		else
		{
			$(".place-order").attr('disabled', true);
		}
	}
/*]]>*/
</script>
<div id="loginmainBody" class="check_login_main">
	<div id="mainContent">
		<div id="checkout_header">
			<div id="headerLogo" style="height: 60px">
				<c:url value="/" var="homeUrl" />
				<a href="${homeUrl}"> <cms:slot var="feature"
						contentSlot="${slots.CheckoutSiteLogo}">
						<cms:component component="${feature}" />
					</cms:slot>
				</a>
			</div>
			<div class="right_text">
				<cms:slot var="feature" contentSlot="${slots.CheckoutRight}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
		</div>
		<div id="loginmiddleContent">
			<div class="innermiddleContent">
				<div class="checkoutwrapper_checkoutsummary">

					<label class="error"><common:globalMessages /></label>

					<%-- <div id="globalMessages">
					
					<common:globalMessages />
					<label class="error"> </label>
					</div> --%>
					<div class="content_left">
						<div class="span-20 last">
							<div class="span-20 last">
								<single-checkout:summaryFlow cartData="${cartData}"
									posData="${pos}" storepickup="${storepickup}"
									shipping="${shipping}" surcharge="${surcharge}"/>
									
								<div class="note_text"style="padding-top: 11px;padding-right: 27px;">
									<p>
										<spring:theme code="checkout.summary.verifyorder" />
									</p>
									<div class="clearb"></div>
									<form:form action="${placeOrderUrl}" id="placeOrderForm1"
										commandName="placeOrderForm">

										<form:input type="hidden" name="gift" class="giftClass"
											path="gift" />
										<br />
										<form:input type="hidden" name="giftMessage"
											class="giftMessageClass" path="giftMessage" />
										<br />
										<!-- <div> -->
										<c:choose>
										<c:when test="${outOfStock}">
										<button type="submit2" class="secure_checkout" disabled="disabled"
											onclick="#">
											<spring:theme code="checkout.summary.placeOrder" />
										</button>
										</c:when>
										<c:otherwise>
										<button type="submit" class="secure_checkout"
										id ="placeorderid"	onclick="placeOrderWithSecurityCode();return false;">
											<spring:theme code="checkout.summary.placeOrder" />
										</button>
										</c:otherwise>
										</c:choose>
										<!-- </div> -->
									</form:form>
								</div>
							</div>
						</div>
						<br />
						<hr>
						<div class="shoppingbaglist">
							<checkout:summaryCartItems cartData="${cartData}" />
						</div>
						<hr>
						
					<div class="promotionwrapper" style="margin-top: 12px;">					
						<div id="accordian">			
							<div class="accordiancontent">
							<c:choose>
							<c:when test="${empty cartData.voucherCode}">
							<div class="accordianhead checkoutsummary_Promotion_Box">
								<span><spring:theme code="question.promotion.code"/></span>
							</div>
							<div class = "viewpromotion">
								<form id="voucherForm" action="${request.contextPath}/checkout/single/applyVoucher" method="get" onsubmit="return validateForm()" >
									<c:choose>
									<c:when test="${!empty cartData.voucherCode}">
									<input type="text" name="voucherCode" disabled="disabled"/>
									<input class="button " type="submit" value="Apply" disabled="disabled"/>
									</c:when>
									<c:otherwise>
									<input type="text" name="voucherCode" class="voucher_code_search" id="voucherCode"/>
									<input class="button voucher_code_button" type="submit" value="Apply" />
									<span id="errorMsg" style="display: none;"> <br><spring:theme code="enter.voucher.code"/></span>
									</c:otherwise>
									</c:choose>
						    	</form>
								</div>
							</c:when>
							<c:otherwise>
							<div class ="appliedPromotion">
							     <div class="accordianhead active_promotion">
										<spring:message code="cart.voucher.info1" /><br/>
										<spring:message code="cart.voucher.info2" />
							     </div>
						     <form id="voucherForm" action="${request.contextPath}/checkout/single/releaseVoucher" method="get" >
							  <spring:message code="cart.voucher.yourVoucherCode" />
								<c:choose>
								<c:when test="${cartData.voucherCode eq 'LOYALTYVOUCHER' }">
								${loyaltyVoucher}
								</c:when>
								<c:otherwise>
								 ${cartData.voucherCode}
								</c:otherwise>
								</c:choose>
								<input class="button" type="submit" value="Release Voucher" />
							</form>
						   </div>
							</c:otherwise>
							</c:choose>
							</div>
						</div>
					</div>
											
						
						<div class="order_summerywrapper ">
							<cart:ajaxCartTotals />
						</div>
						<div class="order_total totalPrice">
							<span>Order Total:</span> <span class="ordertotalvalue"> 
							<format:price priceData="${cartData.totalAmount}"/> </span>
						</div>
						<!-- <div class="secure_checkout">
							<a href="#">Place Order</a>
						</div> -->
						<div class="note_text"style="padding-top: 53px;padding-right: 0px;">
									<p>
										<spring:theme code="checkout.summary.verifyorder" />
									</p>
									<!-- <div class="clearb"></div> -->
									<form:form action="${placeOrderUrl}" id="placeOrderForm1"
										commandName="placeOrderForm">

										<form:input type="hidden" name="gift" class="giftClass"
											path="gift" />
										<form:input type="hidden" name="giftMessage"
											class="giftMessageClass" path="giftMessage" />
										<c:choose>
										<c:when test="${outOfStock}">
										<button type="submit" class="secure_checkout"
											onclick="" disabled="disabled">
											<spring:theme code="checkout.summary.placeOrder" />
										</button>
										</c:when>
										<c:otherwise>
										<button type="submit" class="secure_checkout"
											id ="placeorderid1" onclick="placeOrderWithSecurityCode();return false;">
											<spring:theme code="checkout.summary.placeOrder" />
										</button>
										</c:otherwise>
										</c:choose>

									</form:form>
								</div>
						<div class="span-12" style="margin-top: -20px;">
<%-- 							<cart:cartPromotions cartData="${cartData}" />
 --%>						</div>
						<div></div>
					</div>
					<div class="content_right">
						<div class="bannerwrap">
							<cms:slot var="feature"
								contentSlot="${slots.CheckoutSummaryRight}">
								<cms:component component="${feature}" />
							</cms:slot>
						</div>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
		<div id="checkout_footer">
			<div class="copyrightsbox overflow">
				<div class="">
					<!-- <span> -->
					<div class="checkoutbottomimage">
						<cms:slot var="feature" contentSlot="${slots.CheckoutFooterRight}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
					<cms:slot var="feature" contentSlot="${slots.CheckoutFooterLeft}">
						<cms:component component="${feature}" />
					</cms:slot>
					<!-- </span> -->
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">	

function validateForm()
{
	if($('#voucherCode').val()=='')
  	{
		$('#errorMsg').show();
		return false;
	 }
		else
		{
			$('#errorMsg').hide();
		}	
}

$('form').submit(function(){
	$('#loginmiddleContent').block({ message: "" });
});
</script>
</body>
</html>