<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/mobile/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav" %>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<nav:myaccountNav/>
		<div class="item_container_holder" data-content-theme="d" data-theme="e">
			<div id="globalMessages" data-theme="e">
				<common:globalMessages/>
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section1']}">
				<cms:component component="${feature}"/>
			</cms:slot>
			<div class="ui-grid-a" data-theme="b">
				<h3>
					<spring:theme code="text.account.order.yourOrder {0}" text="Your Order {0}" arguments="${orderData.code}"/>
				</h3>
				<ul class="mFormList">
					<c:if test="${not empty orderData.status}">
						<li><spring:theme code="text.account.order.thankyou" text="Thank you for your order!"/></li>
						<li><spring:theme code="text.account.order.orderPlaced" text="Placed on {0}" arguments="${orderData.created}"/></li>
						<li class="orderStatusHighlight"><spring:theme code="text.account.order.morderStatus" text="Order Status: {0}" arguments="${orderData.statusDisplay}"/></li>
					</c:if>
				</ul>
			</div>
			<div class="span-20 last" data-theme="b">
				<div class="span-20 last delivery_stages" data-theme="b">
					<div class="ui-grid-a" data-theme="d">
						<spring:theme code="text.account.order.summary" text="A summary of your order is below:"/>
					</div>
					<order:deliveryAddressItem order="${orderData}"/>
					<order:deliveryMethodItem order="${orderData}"/>
					<order:paymentMethodItem order="${orderData}"/>
				</div>
				<div class="productItemListHolder">
					<div class="ui-grid-a ">
						<order:accountOrderDetailsItem order="${orderData}"/>
					</div>
				</div>
				<div class="ui-grid-a">
					<order:receivedPromotions order="${orderData}"/>
				</div>
				<div class="ui-grid-a">
					<order:orderTotalsItem order="${orderData}"/>
				</div>
			</div>
			<ul class="mFormList" data-theme="c" data-content-theme="c">
				<li>
					<div class="ui-grid-a right">
						<c:url value="/my-account/orders" var="ordersUrl"/>
						<a href="${ordersUrl}" data-role="button" data-theme="d" data-icon="arrow-l" class="ignoreIcon">
							<spring:theme code="text.account.orderHistory" text="Order History"/>
						</a>
					</div>
				</li>
			</ul>
			<div id="bottom-banner" class="homebanner">
				<cms:slot var="feature" contentSlot="${slots.Section2}">
					<div class="span-24 section1 advert">
						<cms:component component="${feature}"/>
					</div>
				</cms:slot>
			</div>
		</div>
	</jsp:body>
</template:page>
