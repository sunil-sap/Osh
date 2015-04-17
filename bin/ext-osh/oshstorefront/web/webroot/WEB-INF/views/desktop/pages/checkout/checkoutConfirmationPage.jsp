<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
	<link rel="stylesheet" type="text/css" media="print"
		href="${request.contextPath}/_ui/desktop/osh/css/print.css" />

	<div class="upperbreadcum_img">
		<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
			<cms:component component="${feature}" />
		</cms:slot>
	</div>
	<div class="logoforprint">
		<a href="#"> <img border="0"
			src="${request.contextPath}/_ui/desktop/osh/images/main_logo.png"
			alt="">
		</a>
	</div>


	<div id="globalMessages">
		<h3 class="error-msg">
			<b><common:globalMessages /></b>
		</h3>
	</div>
	<div class="checkoutwrapper">
		<div class="middlemainheader">
			<span style="margin-left: 20px;"><spring:theme code="checkout.orderConfirmation.thankYou"
					arguments="${orderData.code}" /></span>
		</div>
		<p style="margin-left: 20px;">
			<spring:message code="checkout.confirmation.message1" />
			<span class="useremailid">&nbsp;<b>${email}</b></span>
		</p>
		<div class="content_left placeorderpage" style="margin-left: -15px;">

			<div id="globalMessages">
				
				
			</div>
			<sec:authorize ifAllGranted="ROLE_GUESTCUSTOMERGROUP">
				<div class="guestcreateaccount" style="margin-right: 10px;">
					<order:guestOrderConfirmationForm order="${orderData}" />
				</div>
			</sec:authorize>
			<div class="content_left placeorderpage" style="margin-left: -15px; ">
				<sec:authorize
					access="hasRole('ROLE_CUSTOMERGROUP') and !hasRole('ROLE_GUESTCUSTOMERGROUP')"
					ifAllGranted="ROLE_CUSTOMERGROUP">
					<ycommerce:testId code="header_LoggedUser">
						<%-- <div class="img_thanks">
							<div class="img_club_orchard">
								<img
									src="${request.contextPath}/_ui/desktop/osh/images/cartpage/u40_normal.png"
									alt="${textAccountProfile}" title="${textAccountProfile}" />
							</div>
							<div id="thankyoumessage">
								<spring:theme
									code="checkout.confirmation.clubOrchardPointMessage"
									arguments="$0.00" />
							</div>
						</div> --%>
					</ycommerce:testId>
				</sec:authorize>
				<c:choose>
					<c:when test="${shipping}">
						<div class="formsection" style="left: 46px;">
							<order:deliveryAddressItem order="${orderData}" />
						</div>
					</c:when>
					<c:otherwise>
						<div class="formsection" style="left: 46px;">
							<order:storePickupAddress posData="${pos}" />
						</div>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${shipping}">
						<div class="formsection" style="left: 43px;">
							<order:deliveryMethodItem order="${orderData}" />
						</div>
					</c:when>
					<c:otherwise>
						<div class="formsection" style="left: 43px;">
							<h2 style="margin-bottom: 5px;">
								<spring:theme
									code="checkout.summary.deliveryMode.shippingmethod" />
							</h2>
							<div class="borderbox">
								<div class="cartformwarp">
									<div class="cartformwarp_inner_orderdetail">
										<p><spring:theme code="pick.up.store"/></p>
										<div></div>
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

				<div class="formsection" style="left: 40px;">
					<order:paymentMethodItem order="${orderData}" />
				</div>

				<div class="clearb"></div>
				<c:if test="${shipping and storepickup}">
					<div class="formsection">
						<order:storePickupAddress posData="${pos}" />
					</div>
				</c:if>

				
				<hr style="width: 770px; margin-left: 46px;"/>
				<div class="shoppingbaglist" style="margin-left: 30px;">
					<order:orderDetailsItem order="${orderData}" />

				<hr style="width: 770px; margin-left: 16px;"/>
								
				   <order:receivedPromotions order="${orderData}" />

					<order:orderTotalsItem order="${orderData}" containerCSS="positive" />

					<div style="margin: 0px 0px 15px 20px;" class="printpage">
						<input class="print_button" type="button" value="Print"
							onclick="window.print()" /><label class="checkboxtext">&nbsp;<spring:message
								code="checkout.confirmation.print.message" /></label>
					</div>

				</div>
		
			</div>

		</div>
		<div class="content_right_orderConfirmation" style="margin-right: 10px;">
			<div class="bannnerwrap">
				<cms:slot var="feature" contentSlot="${slots.RightBannerSection}">
					<cms:component component="${feature}" />
				</cms:slot>

			</div>
			<div class="guest_ES_ClubOrchard_img" style="margin-left: -2px;">
				<cms:slot var="feature" contentSlot="${slots.ES_ClubOrchardSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
		</div>

	</div>
	<div class="clearfix"></div>



</template:page>
