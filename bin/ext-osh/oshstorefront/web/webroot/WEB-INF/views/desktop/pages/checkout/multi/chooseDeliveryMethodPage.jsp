<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/checkout/multi/select-delivery-method" var="continueToPaymentMethodUrl"/>

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
	
	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" currentStep="2" stepName="deliveryMethod"/>

	<div class="span-20 last">
		<div class="span-20 last">
			<div class="span-9 first checkout_multi_a complete ">
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
			<div class="span-11 checkout_multi_b last">
				<form:form id="selectDeliveryMethodForm" action="${continueToPaymentMethodUrl}" method="get">
					<div class="item_container_holder">
						<div class="title_holder">
							<div class="title">
								<div class="title-top">
									<span></span>
								</div>
							</div>
							<h2><span></span><spring:theme code="checkout.multi.deliveryMethod.stepHeader"/></h2>
						</div>
						<div class="item_container delivery_method_list">
							<p><spring:theme code="checkout.multi.deliveryMethod.selectDeliveryMethodMessage"/></p>
							<multi-checkout:deliveryMethodSelector deliveryMethods="${deliveryMethods}" selectedDeliveryMethodId="${selectedDeliveryMethodId}"/>
						</div>
					</div>
					<c:if test="${not empty selectedDeliveryMethodId}">
						<button id="chooseDeliveryMethod_continue_button" class="positive right pad_right">
							<spring:theme code="checkout.multi.deliveryMethod.continue" text="Continue"/>
						</button>
					</c:if>
				</form:form>
			</div>
		</div>	
		<multi-checkout:checkoutOrderDetails cartData="${cartData}"/>
	</div>
</template:page>