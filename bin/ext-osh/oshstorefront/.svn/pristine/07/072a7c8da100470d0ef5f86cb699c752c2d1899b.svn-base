<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

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
					<span><cms:slot var="feature"
							contentSlot="${slots.SideContent}">
							<cms:component component="${feature}" />
						</cms:slot></span>
				<div class="helpchat">
						<span><cms:slot var="feature" contentSlot="${slots.ES_Search_LeftContent}">
							<cms:component component="${feature}" />
						</cms:slot></span>
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
						<c:if
							test="${(searchPageData.pagination.totalNumberOfResults)gt 0}">
							<h1>
								<spring:theme code="search.searchfor" />
								&nbsp; "<span class="search_keyword">${searchPageData.freeTextSearch}</span>"
								<span class="search_matches">(${searchPageData.pagination.totalNumberOfResults}
								</span>&nbsp;
								<spring:theme code="search.matches" />
								)
							</h1>
						</c:if>
					</div>
					<div class="search_no_result">
						<cms:slot var="comp" contentSlot="${slots.MiddleContent}">
							<cms:component component="${comp}" />
						</cms:slot>
					</div>
					
				</div>
				<div class="middlemainbottombanner search_bottom">
					<cms:slot var="comp" contentSlot="${slots.BottomContent}">
						<cms:component component="${comp}" />
					</cms:slot>
				</div>
			</div>
		</div>
		</div>
</template:page>