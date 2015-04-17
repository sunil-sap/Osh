<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url value="${previousPage}" var="continueShoppingUrl" />
<c:url value="/cart/clearSession" var="clearSession" /> 

<template:page pageTitle="${pageTitle}">

	<div id="middleContent">
		<div class="innermiddleContent">
			<div id="breadcrumb" class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>

			<div id="top_fullwidth_banner">
				<cms:slot var="feature" contentSlot="${slots.ES_Globle}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>

			<div id="top_fullwidth_banner" style="width: 960px; height: 120px;">
				<cms:slot var="feature" contentSlot="${slots.ES_StoreLocatorTop}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>


			<div class="tabbanner2">
				<div class="middlemainheader">
					<span><b><spring:theme code="mobile.storelocator.title" /></b></span>
				</div>
			</div>

			<div class="Store_Locatorwrapper">

				<store:storeSearch errorNoResults="${errorNoResults}" />
				<%-- <store:storesMap storeSearchPageData="${storeSearchPageData}" /> --%>

				<div class="gmap_mainwrapper">
					<store:storeListForm storeSearchPageData="${storeSearchPageData}"
						locationQuery="${locationQuery}" showMoreUrl="${showMoreUrl}" />
				</div>

				<c:if
					test="${storeSearchPageData eq null || empty storeSearchPageData.results}">
					<div id="globalMessages">
						<h3 style="color: red;">
							<common:globalMessages />
						</h3>
					</div>

					<div class="sl_contentwrapper">
						<div class="img_wrapper" style="width: 320px; height: 290px;">
							<div>
								<cms:slot var="feature" contentSlot="${slots.LeftBanner}">
									<cms:component component="${feature}" />
								</cms:slot>
							</div>
						</div>
						<div class="sl_content_write">
							<cms:slot var="feature" contentSlot="${slots.StoreFinder}">
								<cms:component component="${feature}" />
							</cms:slot>
							<div class="link_sshopmore"><a href="#" class="previousPage"><spring:theme code="shop.more"/></a></div>
						</div>
					</div>

				</c:if>

				<div class="top_fullwidth_banner">
					<cms:slot var="feature" contentSlot="${slots.ES_StoreLocator}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>

			</div>
		</div>
	</div>
</template:page>

<script type="text/javascript">	

$('.previousPage').click(function(){		
	$.ajax({
		url: '${clearSession}',
		success: function(){
			window.location = "${continueShoppingUrl}";
		}
	});
});
</script>