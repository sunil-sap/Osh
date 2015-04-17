<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="checkout-cart" tagdir="/WEB-INF/tags/mobile/checkout"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/mobile/cart"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/mobile/checkout/multi"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<c:url value="/checkout/multi/choose-payment-method" var="choosePaymentMethodUrl" />
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.cart.js"></script>
	</jsp:attribute>
	<jsp:body>
		<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" currentStep="2" stepName="deliveryMethod" />
		<div class="ui-grid-a item_container_holder" data-theme="b" data-role="content">
			<div class="ui-grid-a" data-theme="b">
				<h3 class="infotext">
					<spring:theme code="checkout.multi.deliveryMethod.selectDeliveryMethodMessage" />
				</h3>
				<multi-checkout:deliveryMethodSelector deliveryMethods="${deliveryMethods}" selectedDeliveryMethodId="${selectedDeliveryMethodId}" />
			</div>
		</div>
		<div class="checkoutOverviewItems">
			<div data-role="collapsible" data-collapsed="true" data-content-theme="b" data-theme="d">
				<div class="checkoutOverviewItemsHeadline">
					<spring:theme code="checkout.multi.deliveryAddress" />
				</div>
				<div class="ui-grid-a" data-theme="d">
					<multi-checkout:deliveryAddressDetails deliveryAddress="${deliveryAddress}" isSelected="true" url="choose-delivery-address" />
				</div>
			</div>
			<div class="ui-grid-a" data-theme="b">
				<checkout-cart:summaryCartItems cartData="${cartData}" />
			</div>
			<div class="ui-grid-a" data-theme="b">
				<cart:cartPromotions cartData="${cartData}" />
			</div>
			<div class="ui-grid-a" data-theme="b">
				<cart:cartTotals cartData="${cartData}" />
			</div>
		</div>
	</jsp:body>
</template:page>
