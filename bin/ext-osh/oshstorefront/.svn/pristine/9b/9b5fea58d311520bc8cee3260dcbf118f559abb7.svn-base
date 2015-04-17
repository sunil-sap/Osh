<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<spring:theme code="updatePwd.title" var="title" />
<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<cms:slot var="feature" contentSlot="${slots['Section1']}">
		<cms:component component="${feature}" />
	</cms:slot>
	<user:updatePwd />
	<cms:slot var="feature" contentSlot="${slots['Section5']}">
		<cms:component component="${feature}" />
	</cms:slot>
</template:page>
