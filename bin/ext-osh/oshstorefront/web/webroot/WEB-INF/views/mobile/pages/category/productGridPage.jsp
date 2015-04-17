<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common" %>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.facets.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.productlisting.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/waypoints.min.1.1.5.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div id="globalMessages">
			<common:globalMessages/>
		</div>
		<div class="accmob-navigationHolder">
			<div class="accmob-navigationContent">
				<div id="breadcrumb" class="accmobBackLink">
					<nav:breadcrumb breadcrumbs="${breadcrumbs}"/>
				</div>
			</div>
		</div>
		<div id="top-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots.Section2}">
				<div class="span-24 section1 advert">
					<cms:component component="${feature}"/>
				</div>
			</cms:slot>
		</div>
		<c:if test="${not empty searchPageData.results}">
			<div class="sortingBar item_container_holder">
				<nav:searchTermAndSortingBar pageData="${searchPageData}" top="true" showSearchTerm="false"/>
			</div>
		</c:if>
		<nav:pagination searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"/>
		<div class="span-24 productResultsGrid">
			<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
				<c:choose>
					<c:when test="${status.first}">
						<div class="ui-grid-a">
							<div class='ui-block-a left'>
								<product:productListerGridItem product="${product}"/>
							</div>
							<c:if test="${status.last}">
						</div>
						</c:if>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${(status.count % 2) == 0}">
								<div class='ui-block-b right'>
									<product:productListerGridItem product="${product}"/>
								</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="ui-grid-a">
									<div class='ui-block-a left'>
										<product:productListerGridItem product="${product}"/>
									</div>
								<c:if test="${status.last}">
								</div></c:if>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
		<nav:pagination searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"/>
		<div id="bottom-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots.Section4}">
				<div class="span-24 section1 advert">
					<cms:component component="${feature}"/>
				</div>
			</cms:slot>
		</div>
	</jsp:body>
</template:page>
<nav:facetNavRefinementsJQueryTemplates pageData="${searchPageData}"/>
<nav:resultsListJQueryTemplates/>
<nav:popupMenu/>