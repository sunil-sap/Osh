<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.cart.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div class="item_container_holder">
			<div id="globalMessages" data-theme="b">
				<common:globalMessages />
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section1']}"><cms:component component="${feature}" /></cms:slot>
			<div data-role="content">
				<c:url value="/register/checkout/newcustomer" var="registerAndCheckoutAction" />
				<user:register actionNameKey="checkout.login.registerAndCheckout" action="${registerAndCheckoutAction}" />
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section5']}"><cms:component component="${feature}" /></cms:slot>
		</div>
	</jsp:body>
</template:page>
