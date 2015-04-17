<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<c:set var="themeMsgKey"
	value="${not empty msgKey ? msgKey : 'search.page'}" />
<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>

			<div class="mainmiddleContent">
				<div class="leftmaincontent">

					<div class="leftnav">
						<nav:categoryNav pageData="${searchPageData}" />
					</div>

					<div class="helpchat">
						<cms:slot var="feature" contentSlot="${slots.ES_HowToSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
					<div>
						<cms:slot var="feature"
							contentSlot="${slots.ES_ProjectGuidesSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
					<div class="helpchat">
						<cms:slot var="feature"
							contentSlot="${slots.ES_CatagorySaleSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
				</div>

				<div class="middlemaincontent">

					<div class="middlemainheaderbanner">
						<span><cms:slot var="feature"
								contentSlot="${slots.CatagoryNameSection}">
								<cms:component component="${feature}" />
							</cms:slot></span>
					</div>

					<div class="searchfor">
						<h1>
							<spring:theme code="search.searchfor" />
							&nbsp; "<span class="search_keyword">${searchPageData.freeTextSearch}</span>"
							<span class="search_matches">(${searchPageData.pagination.totalNumberOfResults}&nbsp;&nbsp;<spring:theme
									code="search.matches" />)
							</span>
						</h1>
					</div>
					<div class="search_no_result">
						<p>
							<nav:searchSpellingSuggestion
								spellingSuggestion="${searchPageData.spellingSuggestion}" />
						</p>
					</div>
					<div class="toppagination">
						<nav:pagination top="true" maxPageLimit="${maxPageLimit}"
							requestShowAll="${isShowAllRequested}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="${searchPageData.currentQuery.url}"
							pageUrl="${pageUrl}" />
					</div>

					<div class="productList">
						<c:forEach items="${searchPageData.results}" var="product"
							varStatus="status">
							<div class="product-item" style="height:253px;">
								<product:productListerGridItem product="${product}" />
							</div>
						</c:forEach>
					</div>
					<div class="bottompagination">
						<nav:pagination top="false" maxPageLimit="${maxPageLimit}"
							requestShowAll="${isShowAllRequested}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="${searchPageData.currentQuery.url}"
							pageUrl="${pageUrl}" />
					</div>

				</div>
				<div class="middlemainbottombanner search_bottom">
					<span><cms:slot var="feature"
							contentSlot="${slots.ES_Search_Banner}">
							<cms:component component="${feature}" />
						</cms:slot></span>
				</div>
			</div>
		</div>
	</div>
</template:page>
