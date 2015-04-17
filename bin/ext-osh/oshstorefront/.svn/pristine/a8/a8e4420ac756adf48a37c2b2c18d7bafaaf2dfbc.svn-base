<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:url value="/checkout/multi/placeOrder" var="placeOrderUrl" />

<template:page pageTitle="${pageTitle}">

<script type="text/javascript">
/*<![CDATA[*/
	function placeOrderWithSecurityCode()
	{
		<c:if test="${requestSecurityCode}">
			$(".securityCodeClass").val($("#SecurityCode").val());
		</c:if>
		document.getElementById("placeOrderForm1").submit();
	}
	
	$(document).ready(function(){
		$("#Terms1").click(function() {
			var terms1disabled = $('#Terms1').attr("checked");			
			if(terms1disabled == 'checked' || terms1disabled == true){
				$('#Terms2').attr("checked",true);	
			} else {
				$('#Terms2').attr("checked",false);
			}
		});
		
		$("#Terms2").click(function() {
			var terms2disabled = $('#Terms2').attr("checked");			
			if(terms2disabled == 'checked' || terms2disabled == true){
				$('#Terms1').attr("checked",true);				
			} else {
				$('#Terms1').attr("checked",false);
			}
		});
	})
/*]]>*/
</script>
	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
	</div>

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

	<div class="span-4 side-content-slot advert">
		<cms:slot var="feature" contentSlot="${slots.SideContent}">
			<cms:component component="${feature}"/>
		</cms:slot>
	</div>
	
	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" currentStep="4" stepName="confirmOrder"/>
	
	<div class="span-20 last">

		<div class="span-20 last">
			<multi-checkout:summaryFlow deliveryAddress="${deliveryAddress}" deliveryMode="${deliveryMode}" paymentInfo="${paymentInfo}" requestSecurityCode="${requestSecurityCode}"/>
		</div>

		<div class="span-20 last place-order-top">
			<form:form action="${placeOrderUrl}" id="placeOrderForm1" commandName="placeOrderForm">
				<c:if test="${requestSecurityCode}">
					<form:input type="hidden" name="securityCode" class="securityCodeClass" path="securityCode"/>
				</c:if>
				<button type="submit" class="positive right pad_right place-order" onclick="placeOrderWithSecurityCode();return false;">
					<spring:theme code="checkout.summary.placeOrder"/>
				</button>
				<dl class="terms right">
					<dt class="left">
						<form:checkbox id="Terms1" name="Terms1" path="termsCheck" />
						<label for="Terms1"><spring:theme code="checkout.summary.placeOrder.readTermsAndConditions"/></label>
					</dt>
					<dd></dd>
				</dl>
			</form:form>
		</div>

		<div class="span-20 last">
			<checkout:summaryCartItems cartData="${cartData}"/>
		</div>

		<div class="span-12">
			<cart:cartPromotions cartData="${cartData}"/>
		</div>

		<div class="span-8 right last place-order-cart-total">
			<cart:ajaxCartTotals/>
		</div>

		<div class="span-20 place-order-bottom">
			<%-- Dummy second form at bottom of page - submits the placeOrderForm1 above. --%>
			<form>
				<button type="submit" class="positive right pad_right place-order" onclick="placeOrderWithSecurityCode();return false;">
					<spring:theme code="checkout.summary.placeOrder"/>
				</button>
				<dl class="terms right">
					<dt class="left">
						<input type="checkbox" id="Terms2" name="Terms2" value="Terms">
						<label for="Terms2"><spring:theme code="checkout.summary.placeOrder.readTermsAndConditions"/></label>
					</dt>
					<dd></dd>
				</dl>
			</form>
		</div>
	</div>
</template:page>