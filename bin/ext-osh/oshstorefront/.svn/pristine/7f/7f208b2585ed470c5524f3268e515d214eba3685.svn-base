<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">
	<div class="middleContent">
		<div class="innermiddleContent">
			<div class="acc_breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div class="acc_banner">
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div id="globalMessages">
				<common:globalMessages />
			</div>
			<div class="acc_middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav />
				</div>
				<div class="acc_middlemaincontent">
					<div class="middlemainheader"></div>
					<div class="acc_middlemainheaderbanner">
						<span> <cms:slot var="feature"
								contentSlot="${slots.CatagoryNameSection}">
								<cms:component component="${feature}" />
							</cms:slot>
						</span>
					</div>

					<div class="middlewrapper">
						<div class="uppermiddle">
							<div class="profile">
								<c:url value="/my-account/profile" var="encodedUrl" />
								<div class="left_img">
									<span> <spring:message code="text.account.profile"
											var="textAccountProfile" /> <a href="${encodedUrl}"><img
											src="${request.contextPath}/_ui/desktop/osh/images/cartpage/icon-cust-acc-profile.png"
											alt="${textAccountProfile}" title="${textAccountProfile}" /></a>
									</span>
								</div>
								<div class="right_contain" style="float: left">
									<h2>
										<a href="${encodedUrl}"><spring:theme
												code="text.account.profile" text="Profile" /></a>
									</h2>
									<ul>
										<ycommerce:testId code="myAccount_options_profile_groupbox">
											<c:url value="/my-account/update-profile" var="encodedUrl" />
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.profile.updatePersonalDetails"
														text="Update personal details" /></a></li>
											<c:url value="/my-account/update-password" var="encodedUrl" />
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.profile.changePassword"
														text="Change your password" /></a></li>
										</ycommerce:testId>
									</ul>
								</div>
							</div>
							<div class="address_book">
								<c:url value="/my-account/address-book" var="encodedUrl" />
								<div class="left_img" style="width: 70px; float: left">
									<span> <spring:message code="text.account.addressBook"
											var="textAccountAddressBook" /> <a href="${encodedUrl}"><img
											src="${request.contextPath}/_ui/desktop/osh/images/cartpage/icon-cust-acc-address.png"
											alt="${textAccountAddressBook}"
											title="${textAccountAddressBook}" /></a>
									</span>
								</div>
								<div class="right_contain" style="float: left">
									<h2>
										<a href="${encodedUrl}"><spring:theme
												code="text.account.addressBook" text="Address Book" /></a>
									</h2>
									<ul>
										<ycommerce:testId
											code="myAccount_options_addressBook_groupbox">
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.addressBook.manageDeliveryAddresses"
														text="Manage your delivery addresses" /></a></li>
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.addressBook.setDefaultDeliveryAddress"
														text="Set default delivery address" /></a></li>
										</ycommerce:testId>
									</ul>
								</div>
							</div>
							<div class="payment_detail">
								<c:url value="/my-account/payment-details" var="encodedUrl" />
								<div class="left_img" style="width: 70px; float: left">
									<span> <spring:message
											code="text.account.paymentDetails"
											var="textAccountPaymentDetails" /> <a href="${encodedUrl}"><img
											src="${request.contextPath}/_ui/desktop/osh/images/cartpage/icon-cust-acc-details.png"
											alt="${textAccountPaymentDetails}"
											title="${textAccountPaymentDetails}" /></a>
									</span>
								</div>
								<div class="right_contain" style="float: left">
									<h2>
										<a href="${encodedUrl}"><spring:theme
												code="text.account.paymentDetails" text="Payment Details" /></a>
									</h2>
									<ul>
										<ycommerce:testId
											code="myAccount_options_paymentDetails_groupbox">
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.paymentDetails.managePaymentDetails"
														text="Manage your payment details" /></a></li>
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.paymentDetails.setDefaultPaymentDetails"
														text="Set default payment details" /></a></li>
										</ycommerce:testId>
									</ul>
								</div>
							</div>
							<div class="order_history">
								<c:url value="/my-account/orders" var="encodedUrl" />
								<div class="left_img">
									<span> <spring:message code="text.account.orderHistory"
											var="textAccountOrderHistory" /> <a href="${encodedUrl}"><img
											src="${request.contextPath}/_ui/desktop/osh/images/cartpage/icon-cust-acc-history.png"
											alt="${textAccountOrderHistory}"
											title="${textAccountOrderHistory}" /></a>
									</span>
								</div>
								<div class="right_contain" style="float: left">
									<h2>
										<a href="${encodedUrl}"><spring:theme
												code="text.account.orderHistory" text="Order History" /></a>
									</h2>
									<ul>
										<ycommerce:testId
											code="myAccount_options_orderHistory_groupbox">
											<li><a href="${encodedUrl}"><spring:theme
														code="text.account.viewOrderHistory"
														text="View order history" /></a></li>
										</ycommerce:testId>
									</ul>
								</div>
							</div>
						</div>
						<div class="acc_lowermiddle">
							<cms:slot var="feature"
								contentSlot="${slots.AccountBottomSection}">
								<cms:component component="${feature}" />
							</cms:slot>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
