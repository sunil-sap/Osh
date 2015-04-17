<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">
<style>
.content_left.placeorderpage {
	margin-left: 162px; 
	margin-top: -572px;
}
</style>
	<link rel="stylesheet" type="text/css" media="print"
		href="${request.contextPath}/_ui/desktop/osh/css/order_details_print.css" />
	<div class="middleContent">
		<div class="logoforprint">
			<a href="#"> <img border="0"
				src="${request.contextPath}/_ui/desktop/osh/images/main_logo.png"
				alt="">
			</a>
		</div>
		<div class="innermiddleContent">
			<div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div class="clearb"></div>
			<%-- <div id="globalMessages">
				<common:globalMessages />
			</div> --%>

			<div class="middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav />
				</div>
				<div class="content_left placeorderpage">
					<div class="span-20password multicheckout" style="width: 770px;">
						<c:choose>
							<c:when test="${not empty posData}">
								<div class="formsection">
									<order:storePickItem order="${orderData}" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="formsection">
									<order:deliveryAddressItem order="${orderData}" />
								</div>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${not empty orderData.deliveryMode}">
								<div class="formsection">
									<order:deliveryMethodItem order="${orderData}" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="formsection">
									<h2 style="margin-bottom: 5px;">
										<spring:theme
											code="checkout.summary.deliveryMode.shippingmethod" />
									</h2>

									<div class="borderbox">
										<div class="cartformwarp">
											<div class="cartformwarp_inner_orderdetail">
												<p>
													<spring:theme code="pick.up.store" />
												</p>
												<c:if test="${not empty order.giftMessage}">
													<div class="giftmessage">
														<b><spring:theme code="text.gift.message" /></b>
														<p>${order.giftMessage}</p>
													</div>
												</c:if>
											</div>
										</div>
									</div>


								</div>
							</c:otherwise>
						</c:choose>
						<div class="formsection">
							<c:if test="${not empty orderData.paymentInfo}">
								<order:paymentMethodItem order="${orderData}" />
							</c:if>
						</div>

						<div class="clearb"></div>
					</div>
					<div class="shoppingbaglist_orderdetail">
						<hr>
						<order:accountOrderDetailsItem order="${orderData}" />
						<hr>
						<div class="promotionwrapper" style="margin-left: 20px;">
							<h3>Received Promotions</h3>
						</div>
						<order:receivedPromotions order="${orderData}" />
						<order:orderTotalsItem order="${orderData}" />
						<span
							style="display: block; clear: both; float: left; margin-left: 19px;"
							class="printPage">
							<button class="form" onclick="window.print()">
								<spring:theme code="checkout.confirmation.print" />
							</button> <span><spring:theme
									code="checkout.confirmation.print.message" /></span>
						</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>