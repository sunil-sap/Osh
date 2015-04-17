<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="productsharelink"
	tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>

<template:page pageTitle="${pageTitle}">
	<link rel="stylesheet" type="text/css" media="print"
		href="${request.contextPath}/_ui/desktop/osh/css/profile_print.css" />
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
		        <common:globalMessages/>
		    </div>
			
			<div class="logoforprint">
				<a href="#"> <img border="0"
					src="${request.contextPath}/_ui/desktop/osh/images/main_logo.png"
					alt="">
				</a>
			</div>
			<div class="acc_middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav selected="profile" />
				</div>
				<div class="span-20 last multicheckout acc_profile">
					<div class="item_container_holder1">
						<div class="title_holder">
							<div class="title">
								<div class="title-top">
									<span></span>
								</div>
							</div>
							<h2>
								<spring:theme code="text.account.profile" text="Profile" />
							</h2>
						</div>
						<div class="item_container">
							<div id="profile_table">
								<table class="">
									<tr>

									</tr>
									<%-- <tr>
										<td><spring:theme code="profile.title" text="Title" />:</td>
										<td>${fn:escapeXml(title.name)}</td>
									</tr> --%>
									<tr>
										<td><spring:theme code="profile.firstName"
												text="First name" />:</td>
										<td>${customerData.firstName}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.lastName"
												text="Last name" />:</td>
										<td>${customerData.lastName}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.Address" text="Address" />:
										</td>
										<td>${customerData.defaultShippingAddress.line1}&nbsp;
											${customerData.defaultShippingAddress.line2}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.city" text="City" />:</td>
										<td>${customerData.defaultShippingAddress.town}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.zipcode" text="Zipcode" />:
										</td>
										<td>${customerData.defaultShippingAddress.postalCode}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.email"
												text="Email-Address" />:</td>
										<td>${customerData.uid}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.phoneNumber"
												text="Phone Number" />:</td>
										<td>${customerData.phone}</td>
									</tr>
									<tr>
										<td><spring:theme code="profile.birthDay" text="Birthday" />:</td>

										<c:choose>
											<c:when
												test="${not empty customerData.day || not empty customerData.month }">
												<td>${customerData.month}/${customerData.day}</td>
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
									</tr>
									<tr>
										<td><spring:theme code="profile.loyaltyNumber"
												text="Club Orchard Number" />:</td>
										<td>${customerData.loyaltyNumber}</td>
									</tr>
									<tr>
										<td></td>
										<td></td>
									</tr>
								</table>
							</div>
							<%-- <input type="checkbox" value="newsletter" name="newsletter" disabled="disabled"   ${customerData.storenewletter ? 'checked="checked"' : ''}>
							<spring:theme code="text.account.signup" /> --%>
							<%-- <br> <input type="checkbox" value="promotion" disabled="disabled"
								name="promotion"  ${customerData.specialoffer ? 'checked="checked"' : ''}>
							<spring:theme code="text.account.promotion" /> 
							<br> <input type="checkbox" value="information" disabled="disabled"
								name="information" ${customerData.cluborchardinfo ? 'checked="checked"' : ''}>
							<spring:theme code="text.account.information" /> --%>
							<ul class="updates">
								<br />
								<li><a href="update-profile" style="color: blue"><spring:theme
											code="text.account.profile.updatePersonalDetails"
											text="Update personal details" /></a></li>
								<br />
								<li><a href="update-email" style="color: blue"><spring:theme
											code="text.account.profile.updateEmail" text="Update email" /></a></li>
							</ul>
							<hr>
							<div class="mystore">
								<div class="img">
										<img
											src="${request.contextPath}/_ui/desktop/osh/images/store_locator/store-front.jpg"
											alt="${textAccountProfile}" title="${textAccountProfile}" />
										<%-- <store:storeImage store="${storeData}" format="store" /> --%>
									</div>

								<div class="contain_store">
									<span><b>My Store: </b></span><br> <span><b>${fn:escapeXml(storeData.name)}
									</b></span><br> <span>${fn:escapeXml(storeData.address.line1)}&nbsp;${fn:escapeXml(storeData.address.line2)}</span><br>
									<span>${fn:escapeXml(storeData.address.town)},
										${fn:escapeXml(storeData.address.country.name)}
										,${fn:escapeXml(storeData.address.postalCode)}</span><br> <span>${fn:escapeXml(storeData.address.phone)}</span>
								</div>
							</div>
							<hr>
							<c:if test="${not empty customerData.loyaltyNumber}">
								<div class="last_upper">
									<div class="last_logo" style="margin-top: 13px;">
										<img
											src="${request.contextPath}/_ui/desktop/osh/images/cartpage//u40_normal.png"
											alt="${textAccountProfile}" title="${textAccountProfile}" />
									</div>
									<div class="lastupper_middle" style="margin-top: 13px;">
										<span><b>${customerData.name}</b></span><br> <span>${customerData.loyaltyNumber}</span><br>
									</div>
								</div>
								<br clear="all">
								<div class="last_down">
									<p>
										<span class="pointText"><spring:theme
												code="text.account.point" />&nbsp;&nbsp;</span> <span
											class="pointNumber"> ${customerPoints}</span> <span
											class="point_doller_text">250 <spring:theme
												code="text.account.points" /> = $5
										</span> <br> <span class="pointText"> </span> <br> <span
											class="pointText"><spring:theme
												code="text.account.you" /> </span> <span class="pointNumber1">${remainingPoints}</span>
										<span class="pointText"> <spring:theme
												code="text.account.certificate" />
										</span>
									</p>

								</div>
							</c:if>
							<c:if test="${empty customerData.loyaltyNumber}">
								<div class="last_upper">
									<div class="last_logo" style="margin-top: 13px;">
										<img
											src="${request.contextPath}/_ui/desktop/osh/images/cartpage//u40_normal.png"
											alt="${textAccountProfile}" title="${textAccountProfile}" />
									</div>
									<div class="lastupper_right">
										<a href="http://cluborchard.osh.com" target="_blank"
											style="color: blue"><spring:theme
												code="text.account.member" /></a>
									</div>
									<div class="lastupper_middle" style="margin-top: 13px;">
										<spring:theme code="text.account.loyalty.advice.msg" />
									</div>
								</div>
							</c:if>

						</div>
					</div>
				</div>
				<div class="middle_right1">
					<product:profileShareLinks />
				</div>
			</div>
		</div>
	</div>
</template:page>
