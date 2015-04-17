<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<template:page pageTitle="${pageTitle}">
	<div class="item_container_holder_error">
		<h2>
			<spring:theme code="system.error.page.not.found" />
		</h2>
		<div id="globalMessages">
			<common:globalMessages />
		</div>
		<cms:slot var="feature" contentSlot="${slots['Section1']}">
			<cms:component component="${feature}" />
		</cms:slot>
		<div>
			<c:if test="${not empty message}">
				<c:out value="${message}" />
			</c:if>
		</div>
		<cms:slot var="feature" contentSlot="${slots['Section2']}">
			<cms:component component="${feature}" />
		</cms:slot>
	</div>
</template:page>
