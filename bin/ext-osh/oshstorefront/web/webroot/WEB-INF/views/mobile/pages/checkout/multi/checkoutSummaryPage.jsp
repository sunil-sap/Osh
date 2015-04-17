<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/mobile/cart"%>
<%@ taglib prefix="checkout-cart" tagdir="/WEB-INF/tags/mobile/checkout"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/mobile/checkout/multi"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/mobile/checkout/multi"%>
<spring:url value="/checkout/multi/placeOrder" var="placeOrderUrl" />
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.cart.js"></script>
	</jsp:attribute>
	<jsp:body>
		<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" currentStep="4" stepName="confirmOrder" />
		<cms:slot var="feature" contentSlot="${slots['Section1']}"><cms:component component="${feature}" /></cms:slot>
		<div class="ui-grid-a" data-theme="d" data-role="content">
			<div class="ui-grid-a" data-theme="b">
				<checkout:summaryFlow deliveryAddress="${deliveryAddress}" deliveryMode="${deliveryMode}" paymentInfo="${paymentInfo}" requestSecurityCode="${requestSecurityCode}" />
			</div>
		</div>
		<div data-theme="d">
			<form:form action="${placeOrderUrl}" id="placeOrderForm1" commandName="placeOrderForm">
				<common:errors />
				<form:input type="hidden" name="securityCode" class="securityCodeClass" maxlength="5" path="securityCode" />
				<h6 class="descriptionHeadline">
					<spring:theme code="text.headline.terms" text="Agree with the terms and conditions" />
				</h6>
				<div class="checkoutOverviewItems">
					<div class="ui-grid-a" data-theme="b">
						<checkout-cart:summaryCartItems cartData="${cartData}" showAllItems="true" />
					</div>
					<div class="ui-grid-a" data-theme="b">
						<cart:cartPromotions cartData="${cartData}" />
					</div>
					<div class="ui-grid-a" data-theme="b">
						<cart:ajaxCartTotals />
					</div>
				</div>
				<span class="termsCheck">
					<form:checkbox id="Terms1" name="Terms1" path="termsCheck" data-theme="d" />
					<label for="Terms1"><spring:theme code="checkout.summary.placeOrder.readTermsAndConditions" /></label>
				</span>
				<ul data-theme="c" data-content-theme="c" class="checkoutSummarySubmit">
					<li>
						<div class="ui-grid-a">
							<div class="ui-block-a">
								<c:url value="/" var="homeUrl" />
								<a href="${homeUrl}" data-role="button" data-theme="d" data-icon="delete" class="ignoreIcon">
									<spring:theme code="text.button.cancel" />
								</a>
							</div>
							<div class="ui-block-b">
								<button type="submit" data-icon="arrow-r" data-theme="b" data-iconpos="right" class="positive right pad_right place-order placeOrderWithSecurityCode">
									<spring:theme code="mobile.checkout.confirmOrder" />
								</button>
							</div>
						</div>
					</li>
				</ul>
			</form:form>
			<div id="bottom-banner" class="homebanner">
				<cms:slot var="feature" contentSlot="${slots.Section2}">
					<div class="span-24 section1 advert">
						<cms:component component="${feature}" />
					</div>
				</cms:slot>
			</div>
		</div>
	</jsp:body>
</template:page>
