<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div class="item_container" data-theme="e">
			<div id="globalMessages" data-theme="e">
				<common:globalMessages />
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section1']}">
				<cms:component component="${feature}" />
			</cms:slot>
			<div class="item_container_holder">
				<h3 class="accountHeadline">
					<spring:theme code="text.account.account" text="Account" />
				</h3>
			</div>
			<h6 class="descriptionHeadline">
				<spring:theme code="text.headline.profile" text="Click here to view your profile, address book, payment details or order history" />
			</h6>
			<ul class="accountNavigation" data-role="listview" data-inset="true" data-theme="e" data-dividertheme="b">
				<c:url value="/my-account" var="myAccountUrl" />
				<c:url value="/my-account/address-book" var="addressBookUrl" />
				<c:url value="/my-account/payment-details" var="paymentDetailsUrl" />
				<c:url value="/my-account/orders" var="orderHistoryUrl" />
				<li><a href="${myAccountUrl}/profile"><spring:theme code="text.account.profile" text="Profile" /></a></li>
				<li><a href="${addressBookUrl}"><spring:theme code="text.account.addressBook" text="Address Book" /></a></li>
				<li><a href="${paymentDetailsUrl}"><spring:theme code="text.account.paymentDetails" text="Payment Details" /></a></li>
				<li><a href="${orderHistoryUrl}"><spring:theme code="text.account.orderHistory" text="Order History" /></a></li>
			</ul>
		</div>
		<div id="bottom-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots['Section2']}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
	</jsp:body>
</template:page>
