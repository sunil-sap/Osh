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
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.cart.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div id="globalMessages" data-theme="b">
			<common:globalMessages />
		</div>
		<cms:slot var="feature" contentSlot="${slots['Section1']}"><cms:component component="${feature}" /></cms:slot>
		<div class="item_container_holder" data-content-theme="d" data-theme="e">
			<div class="ui-grid-a" data-theme="b">
				<h3>
					<spring:theme code="text.account.order.yourOrder {0}" text="Your Order {0}" arguments="${orderData.code}" />
				</h3>
				<ul class="mFormList">
					<c:if test="${not empty orderData.status}">
						<li><spring:theme code="text.account.order.morderStatus" text="Order Status: {0}" arguments="${orderData.statusDisplay}" /></li>
					</c:if>
					<li><spring:theme code="text.account.order.orderPlaced" text="Placed on {0}" arguments="${orderData.created}" /></li>
					<li><spring:theme code="text.account.order.thankyou" text="Thank you for your order!" /></li>
				</ul>
			</div>
			<div class="span-20 last" data-theme="b">
				<div class="ui-grid-a" data-theme="d">
					<h2>
						<spring:theme code="text.account.order.summary" text="A summary of your order is below:" />
					</h2>
				</div>
				<div class="span-20 last delivery_stages" data-theme="b">
					<order:deliveryAddressItem order="${orderData}" />
					<order:deliveryMethodItem order="${orderData}" />
					<order:paymentMethodItem order="${orderData}" />
				</div>
				<div class="orderOverviewItems">
					<h6 class="descriptionHeadline">
						<spring:theme code="text.headline.orderitems" text="All information about your order items" />
					</h6>
					<div class="ui-grid-a" data-theme="b">
						<order:orderDetailsItem order="${orderData}" />
					</div>
					<div class="ui-grid-a" data-theme="b">
						<order:receivedPromotions order="${orderData}" />
					</div>
					<div class="ui-grid-a" data-theme="b">
						<order:orderTotalsItem order="${orderData}" />
					</div>
				</div>
			</div>
			<ul class="mFormList" data-theme="c" data-content-theme="c">
				<li>
					<div class="ui-grid-a doubleButton">
						<div class="ui-block-a" style="width: 38%;">
							<c:url value="/" var="homeUrl" />
							<a href="${homeUrl}" data-role="button" data-theme="d" data-icon="arrow-l" data-iconpos="left" class="ignoreIcon">
								<spring:theme code="cart.page.shop" />
							</a>
						</div>
						<div class="ui-block-b" style="width: 58%;">
							<c:url value="/my-account/orders" var="accountOrdersUrl" />
							<a href="${accountOrdersUrl}" data-role="button" data-theme="d" data-icon="arrow-r" data-iconpos="right" class="ignoreIcon">
								<spring:theme code="text.account.orderHistory" text="Order History" />
							</a>
						</div>
					</div>
				</li>
			</ul>
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
