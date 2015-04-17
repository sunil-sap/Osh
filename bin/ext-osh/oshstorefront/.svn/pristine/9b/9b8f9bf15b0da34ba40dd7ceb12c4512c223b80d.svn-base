<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<nav:myaccountNav/>
		<div data-theme="e" class="item_container_holder">
			<div id="globalMessages" data-theme="e">
				<common:globalMessages/>
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section1']}">
				<cms:component component="${feature}"/>
			</cms:slot>
			<H3>
				<spring:theme code="text.account.orderHistory" text="Order History"/>
			</H3>
			<p>
			<div data-theme="c">
				<h6 class="descriptionHeadline">
					<spring:theme code="text.headline.orders" text="View your orders"/>
				</h6>
				<c:if test="${not empty searchPageData.results}">
					<p>
						<spring:theme code="text.account.orderHistory.viewOrders" text="View your orders"/>
					</p>
					<nav:pagination searchPageData="${searchPageData}" searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}" 
						msgKey="text.account.orderHistory.mobile.page"/>
					<ul data-role="listview" data-inset="true" data-theme="d" data-dividertheme="d">
						<c:forEach items="${searchPageData.results}" var="order">
							<li>
								<c:url value="/my-account/order/${order.code}" var="myAccountOrderDetailsUrl"/>
								<a href="${myAccountOrderDetailsUrl}">
									<div class='ui-grid-a'>
										<div class='ui-block-a'>
											<H3>${order.code}</H3>
											<p>
												<fmt:formatDate pattern="dd/MM/yy" value="${order.placed}"/>
											</p>
										</div>
										<div class='ui-block-b'>
											<H3 class="continuous-text">${order.statusDisplay}</H3>
										</div>
									</div>
								</a></li>
						</c:forEach>
					</ul>
					<nav:pagination searchPageData="${searchPageData}" searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}"
						 msgKey="text.account.orderHistory.mobile.page"/>
				</c:if>
				<c:if test="${empty searchPageData.results}">
					<p>
						<spring:theme code="text.account.orderHistory.noOrders" text="You have no orders"/>
					</p>
				</c:if>
			</div>
			<div id="bottom-banner" class="homebanner">
				<cms:slot var="feature" contentSlot="${slots.Section2}">
					<div class="span-24 section1 advert">
						<cms:component component="${feature}"/>
					</div>
				</cms:slot>
			</div>
		</div>
	</jsp:body>
</template:page>
