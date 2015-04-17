<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.facets.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.productlisting.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/waypoints.min.1.1.5.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div class="accmob-navigationHolder">
			<div class="accmob-navigationContent">
				<div id="breadcrumb" class="accmobBackLink">
					<nav:breadcrumb breadcrumbs="${breadcrumbs}" />
				</div>
			</div>
		</div>
		<div id="globalMessages">
			<common:globalMessages />
		</div>
		<div id="top-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots.Section2}">
				<div class="span-24 section1 advert">
					<cms:component component="${feature}" />
				</div>
			</cms:slot>
		</div>
		<div class="searchTopHolder">
			<nav:searchTermAndSortingBar pageData="${searchPageData}" top="true" showSearchTerm="true" />
		</div>
		<div>
			<ul data-role="listview" id="resultsList" data-inset="true" data-theme="e" data-dividertheme="b">
				<c:forEach items="${searchPageData.results}" var="product">
					<product:productListerItem product="${product}" />
				</c:forEach>
			</ul>
		</div>
		<div id="bottom-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots.Section4}">
				<div class="span-24 section1 advert">
					<cms:component component="${feature}" />
				</div>
			</cms:slot>
		</div>
	</jsp:body>
</template:page>
<nav:facetNavRefinementsJQueryTemplates pageData="${searchPageData}" />
<nav:resultsListJQueryTemplates />
<nav:popupMenu />
