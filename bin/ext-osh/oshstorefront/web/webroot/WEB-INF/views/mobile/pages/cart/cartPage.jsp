<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/mobile/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<spring:theme text="Your Shopping Cart" var="title" code="cart.page.title" />
<c:url value="/cart/checkout" var="checkoutUrl" />
<c:url value="${continueUrl}" var="continueShoppingUrl" />
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.cart.js"></script>
	</jsp:attribute>
	<jsp:body>
		<c:if test="${not empty message}">
			<br />
			<span class="errors"><spring:theme code="${message}" /></span>
		</c:if>
		<cms:slot var="feature" contentSlot="${slots['Section1']}">
			<cms:component component="${feature}" />
		</cms:slot>
		<c:if test="${not empty cartData.entries}">
			<br />
			<cart:cartItems cartData="${cartData}" />
			<br />
			<cart:cartPromotions cartData="${cartData}" />
			<cart:cartTotals cartData="${cartData}" />
			<fieldset class="ui-grid-a">
				<div class="ui-block-a">
					<a href="${continueShoppingUrl}" data-theme="d" data-role="button" data-icon="arrow-l">
						<spring:theme text="Continue Shopping" code="cart.page.shop" />
					</a>
				</div>
				<div class="ui-block-b">
					<a href="${checkoutUrl}" data-role="button" data-theme="b" data-icon="arrow-r" data-iconpos="right">
						<spring:theme code="checkout.checkout" />
					</a>
				</div>
			</fieldset>
		</c:if>
		<c:if test="${empty cartData.entries}">
			<div class="accmob-navigationHolder">
				<div class="accmob-navigationContent">
					<div id="breadcrumb" class="accmobBackLink">
						<nav:breadcrumb breadcrumbs="${breadcrumbs}" />
					</div>
				</div>
			</div>
			<cms:slot var="feature" contentSlot="${slots['MiddleContent']}">
				<cms:component component="${feature}" />
			</cms:slot>
			<cms:slot var="feature" contentSlot="${slots['BottomContent']}">
				<cms:component component="${feature}" />
			</cms:slot>
		</c:if>
		<c:if test="${not empty cartData.entries}">
			<cart:cartPotentialPromotions cartData="${cartData}" />
		</c:if>
		<cms:slot var="feature" contentSlot="${slots['Section2']}">
			<cms:component component="${feature}" />
		</cms:slot>
	</jsp:body>
</template:page>
<nav:popupMenu />
