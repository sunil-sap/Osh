<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="metaDescription" required="false"%>
<%@ attribute name="metaKeywords" required="false"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>
<%@ attribute name="pageScriptsBeforeJspBody" required="false" fragment="true"%>
<%@ attribute name="pageScriptsAfterJspBody" required="false" fragment="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/shared/analytics"%>
<%@ taglib prefix="debug" tagdir="/WEB-INF/tags/shared/debug"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="${currentLanguage.isocode}">
<head>
	<title>
		${not empty pageTitle ? pageTitle : not empty cmsPage.title ? cmsPage.title : 'Accelerator Title'}
	</title>
	<%-- Provide some hints to mobile browser --%>
	<meta name="HandheldFriendly" content="True"/>
	<meta name="MobileOptimized" content="320"/>
	<meta name="viewport" content="width=device-width, target-densitydpi=160, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
	<%-- Meta Content --%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="description" content="${metaDescription}"/>
	<meta name="keywords" content="${metaKeywords}"/>
	<meta name="robots" content="${metaRobots}"/>
	<meta charset="utf-8"/>
	<link rel="schema.DC" href="http://purl.org/dc/elements/1.1/"/>
	<meta name="format-detection" content="telephone=no"/>
	<%-- Favourite Icon --%>
	<spring:theme code="img.favIcon" text="/" var="favIconPath"/>
	<c:url value="${favIconPath}" var="faviconURL"/>
	<link rel="shortcut icon" href="${faviconURL}" type="image/x-icon"/>
	<%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
	<template:styleSheets/>
	<%-- Inject any additional CSS required by the page --%>
	<jsp:invoke fragment="pageCss"/>
	<analytics:googleAnalytics/>
	<analytics:jirafe/>
</head>
<cms:body cssClass="language-${currentLanguage.isocode}" liveEditCssPath="${commonResourcePath}/css/hybris.cms.live.edit.css" liveEditJsPath="${commonResourcePath}/js/hybris.cms.live.edit.js">
	<%-- Load JavaScript required by the site --%>
	<template:javaScript/>
	<%-- Inject any additional JavaScript required by the page included before page body --%>
	<jsp:invoke fragment="pageScriptsBeforeJspBody"/>
	<%-- Inject the page body here --%>
	<jsp:doBody/>
	<%-- Inject any additional JavaScript required by the page included after page body --%>
	<jsp:invoke fragment="pageScripts"/>
</cms:body>
<jsp:invoke fragment="pageScriptsAfterJspBody"/>
<debug:debugFooter/>
</html>
