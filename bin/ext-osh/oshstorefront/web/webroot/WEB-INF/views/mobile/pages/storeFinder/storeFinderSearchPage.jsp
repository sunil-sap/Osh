<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/mobile/store"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScriptsBeforeJspBody">
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.ui.map.full.min.3.0.rc1.js"></script>
	</jsp:attribute>
	<jsp:attribute name="pageScriptsAfterJspBody">
		<%-- Google maps API --%>
		<c:if test="${not empty googleApiVersion}">
			<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?v=${googleApiVersion}&key=${googleApiKey}&sensor=false"></script>
		</c:if>
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.storefinder.js"></script>
	</jsp:attribute>
	<jsp:body>
		<common:globalMessages />
		<store:storesMap storeSearchPageData="${storeSearchPageData}" />
		<div class="accmob-StoreList" data-theme="e" data-content-theme="c">
			<store:storeListForm storeSearchPageData="${storeSearchPageData}" locationQuery="${locationQuery}" showMoreUrl="${showMoreUrl}" />
		</div>
		<div data-theme="e" data-content-theme="c">
			<store:storeSearch />
		</div>
		<div id="bottom-banner" class="homebanner">
			<cms:slot var="feature" contentSlot="${slots['Section2']}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
	</jsp:body>
</template:page>
