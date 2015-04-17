<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>

<c:url value="/checkout/multi/add-payment-method" var="addPaymentMethodUrl"/>
<c:url value="/checkout/multi/summary" var="checkoutSummaryUrl"/>

<template:page pageTitle="${pageTitle}">

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
	
	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" currentStep="3" stepName="paymentMethod"/>
	
	<div class="span-20 last">
		<div class="span-20 last">
			<div class="span-10 first checkout_multi_a complete">
				<div class="item_container_holder">
					<div class="title_holder">
						<div class="title">
							<div class="title-top">
								<span></span>
							</div>
						</div>
						<h2>
							<span></span>
							<spring:theme code="checkout.multi.deliveryAddress.stepHeader.done"/>
						</h2>
					</div>
					<div class="item_container delivery_addresses_list">
						<multi-checkout:selectedDeliveryAddressDetails deliveryAddress="${deliveryAddress}"/>
					</div>
				</div>
			</div>
			<div class="span-10 last checkout_multi_b complete">
				<div class="item_container_holder">
					<div class="title_holder">
						<div class="title">
							<div class="title-top">
								<span></span>
							</div>
						</div>
						<h2>
							<span></span>
							<spring:theme code="checkout.multi.deliveryMethod.stepHeader.done"/>
						</h2>
					</div>
					<div class="item_container delivery_method_list">
						<multi-checkout:selectedDeliveryMethodDetails deliveryMethod="${deliveryMethod}"/>
					</div>
				</div>
			</div>
		</div>
		<div class="span-20 last">
			<div class="item_container_holder">
				<div class="title_holder">
					<div class="title">
						<div class="title-top">
							<span></span>
						</div>
					</div>
					<h2><spring:theme code="checkout.multi.paymentMethod.savedCards.stepHeader"/></h2>
				</div>
				<div class="item_container">
					<div class="left item_container">
						<p><spring:theme code="checkout.multi.paymentMethod.savedCards.selectSavedCardOrEnterNew"/></p>
					</div>
					<div class="right item_container">
						<ycommerce:testId code="choosePaymentMethod_enterNewPaymentDetails_button">
							<a href="${addPaymentMethodUrl}">
								<button type="submit" class="form enter_new_payment_button">
									<spring:theme code="checkout.multi.paymentMethod.savedCards.enterNewPaymentDetails"/>
								</button>
							</a>
						</ycommerce:testId>
					</div>
					<multi-checkout:paymentMethodSelector paymentMethods="${paymentMethods}" selectedPaymentMethodId="${selectedPaymentMethodId}"/>
				</div>
			</div>
		</div>
		<c:if test="${not empty selectedPaymentMethodId}">
			<div class="item_container">
				<ycommerce:testId code="choosePaymentMethod_continue_link">
					<a href="${checkoutSummaryUrl}" class="positive right">
						<spring:theme code="checkout.multi.paymentMethod.continue"/>
					</a>
				</ycommerce:testId>
			</div>
		</c:if>
		
		<multi-checkout:checkoutOrderDetails cartData="${cartData}"/>
		
	</div>
</template:page>