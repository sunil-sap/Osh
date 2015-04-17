<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="category" tagdir="/WEB-INF/tags/mobile/category"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>
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
		<div id="top-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots['Section2']}">
				<div class="section2 advert">
					<cms:component component="${feature}" />
				</div>
			</cms:slot>
		</div>
		<h6 class="descriptionHeadline">
			<spring:theme code="text.headline.refinements" text="Choose the relevance or add refinements" />
		</h6>
		<div class="productResultsList">
			<c:if test="${empty searchPageData.results}">
				<ul id="categoryResultsList" data-role="listview" data-inset="true" data-theme="e" data-content-theme="e" class="mainNavigation">
					<category:categoryList pageData="${searchPageData}" />
				</ul>
			</c:if>
			<c:if test="${not empty searchPageData.results}">
				<div class="sortingBar item_container_holder">
					<nav:searchTermAndSortingBar pageData="${searchPageData}" top="true" showSearchTerm="false" />
				</div>
			</c:if>
			<c:if test="${not empty searchPageData.results}">
				<ul id="resultsList" data-role="listview" data-inset="true" data-theme="e" data-content-theme="e" class="mainNavigation">
					<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
						<product:productListerItem product="${product}" />
					</c:forEach>
				</ul>
			</c:if>
		</div>
		<cms:slot var="feature" contentSlot="${slots['Section4']}">
			<div class="span-4 section4 advert last">
				<cms:component component="${feature}" />
			</div>
		</cms:slot>
	</jsp:body>
</template:page>
<nav:facetNavRefinementsJQueryTemplates pageData="${searchPageData}" />
<nav:resultsListJQueryTemplates />
<nav:popupMenu />
