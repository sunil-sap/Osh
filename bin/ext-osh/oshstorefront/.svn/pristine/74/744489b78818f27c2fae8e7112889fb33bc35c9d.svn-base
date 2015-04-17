<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.imagesloaded.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.cycle.all.2.9999.5.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.ui.touch-punch.min.0.2.2.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.product.js"></script>
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
		<div class="span-4 zone_a advert">
			<cms:slot var="feature" contentSlot="${slots.Section1}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
		<h2 class="productHeadline">
			<div class="productTitle">${product.name}</div>
		</h2>
		<div class="productRatingHolder">
			<product:productAverageReviewDetailPage product="${product}" />
		</div>
		<product:productImage product="${product}" format="product" />
		<product:productAddToCartPanel product="${product}" />
		<!--  Product Description -->
		<c:if test="${not empty product.description}">
			<div class="item_container_holder continuous-text" id="productDescription">${product.summary}</div>
		</c:if>
		<h6 class="descriptionHeadline">
			<spring:theme code="text.headline.productinfo" text="Click to get more information about product overview, product review or delivery method" />
		</h6>
		<div data-role="collapsible-set" class="productAccordeon">
			<product:productOverview product="${product}" />
			<product:productReviews product="${product}" showingReviews="${showingReviews}" totalReviews="${totalReviews}" />
			<!-- Delivery -->
			<cms:slot var="tabs" contentSlot="${slots.Tabs}">
				<cms:component component="${tabs}" />
			</cms:slot>
		</div>
		<div class="productAccordeon">
			<!-- Accessories / Related Products -->
			<cms:slot var="comp" contentSlot="${slots.Accessories}">
				<cms:component component="${comp}" />
			</cms:slot>
		</div>
		<div class="span-4 zone_a advert">
			<cms:slot var="feature" contentSlot="${slots.Section5}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
		<%-- AddThis Button BEGIN --%>
		<hr />
		<div class="addthis_toolbox addthis_default_style addthis_32x32_style">
			<div>
				<a class="addthis_button_preferred_1"></a>
				<a class="addthis_button_preferred_2"></a>
				<a class="addthis_button_preferred_3"></a>
				<ycommerce:testId code="addthis_button">
					<a class="addthis_button_compact"></a>
				</ycommerce:testId>
				<a class="addthis_counter addthis_bubble_style"></a>
			</div>
		</div>
		<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4f28754e346e1aeb"></script>
		<%-- AddThis Button END --%>
	</jsp:body>
</template:page>
<nav:popupMenu />
