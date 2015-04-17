<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div class="item_container_holder">
			<div id="globalMessages" data-theme="b">
				<common:globalMessages/>
			</div>
			<div id="top-banner" class="homebanner">
				<cms:slot var="feature" contentSlot="${slots.Section1}">
					<div class="span-24 section1 advert">
						<cms:component component="${feature}"/>
					</div>
				</cms:slot>
			</div>
			<h6 class="descriptionHeadline">
				<spring:theme code="text.headline.register" text="Click here to register a new customer"/>
			</h6>
			<div class="registerNewCustomerLinkHolder" data-theme="e" data-content-theme="e">
				<c:url value="/register" var="registerUrl"/>
				<a href="${registerUrl}" data-role="button" data-theme="c">
					<spring:theme code="register.new.customer"/>
				</a>
			</div>
			<div class="fakeHR"></div>
			<div class="loginLinkHolder">
				<c:url value="/j_spring_security_check" var="loginAction"/>
				<user:login actionNameKey="login.login" action="${loginAction}"/>
			</div>
			<div id="bottom-banner" class="homebanner">
				<cms:slot var="feature" contentSlot="${slots.Section5}">
					<div class="span-24 section1 advert">
						<cms:component component="${feature}"/>
					</div>
				</cms:slot>
			</div>
		</div>
	</jsp:body>
</template:page>
