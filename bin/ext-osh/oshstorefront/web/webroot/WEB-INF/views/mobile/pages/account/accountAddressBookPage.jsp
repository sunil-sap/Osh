<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<nav:myaccountNav />
		<div class="item_container_holder" data-content-theme="d" data-theme="e">
			<h3><spring:theme code="text.account.addressBook" text="Address Book" /></h3>
			<p></p>
			<div class="item_container">
				<div id="globalMessages">
					<common:globalMessages />
				</div>
				<cms:slot var="feature" contentSlot="${slots['Section1']}">
					<cms:component component="${feature}" />
				</cms:slot>
				<h6 class="descriptionHeadline">
					<spring:theme code="text.headline.addaddress" text="Click to add an address" />
				</h6>
				<a data-role="button" href="add-address" data-theme="c" data-icon="plus">
					<spring:theme code="text.account.addressBook.addAddress" text="Add new address" />
				</a>
				<c:if test="${not empty addressData}">
					<p></p>
					<c:forEach items="${addressData}" var="address" varStatus="rowCounter">
						<ul class="ui-li ui-li-static ui-body-b">
							<li>
								<div class="ui-grid-a" data-theme="b" data-role="content">
									<div class="ui-block-a" style="width: 100%">
										<ul>
											<li>
												<c:choose>
													<c:when test="${not address.defaultAddress}">
														<c:url value="/my-account/set-default-address/${address.id}" var="setDefaultAddressUrl" />
														<a href="${setDefaultAddressUrl}" data-role="button" data-theme="d" id="edit">
															<spring:theme code="text.setDefault" text="Set as default" />
														</a>
													</c:when>
													<c:otherwise>
														<spring:theme code="text.account.addressBook.yourDefaultAddress" text="Default" />
													</c:otherwise>
												</c:choose>
											</li>
											<li>${address.title}&nbsp;${address.firstName}&nbsp;${address.lastName}</li>
											<li>${address.line1}</li>
											<c:if test="${fn:length(address.line2) > 0}">
												<li>${address.line2}</li>
											</c:if>
											<li>${address.town}</li>
											<li>${address.postalCode}</li>
											<li>${address.country.name}</li>
											<li>
												<fieldset class="ui-grid-a">
													<div class="ui-block-a">
														<a href="#" addressId="${address.id}" data-role="button" data-rel="dialog" data-theme="d" data-icon="delete" id="remove-${address.id}" class="remove_address"
															data-message='<spring:theme code="text.address.remove.confirm"/>' data-headerText='<spring:theme code="text.headertext"/>'>
															<spring:theme code="text.remove" text="Remove" />
														</a>
													</div>
													<div class="ui-block-b">
														<c:url value="/my-account/edit-address/${address.id}" var="editAddressUrl" />
														<a href="${editAddressUrl}" data-role="button" data-theme="c" data-icon="check">
															<spring:theme code="text.edit" text="Edit" />
														</a>
													</div>
												</fieldset>
											</li>
										</ul>
									</div>
								</div>
							</li>
						</ul>
						<c:if test="${not rowCounter.last}">
							<div class="fakeHR"></div>
						</c:if>
					</c:forEach>
				</c:if>
			</div>
			<div id="bottom-banner" class="homebanner">
				<cms:slot var="feature" contentSlot="${slots.Section2}">
					<div class="span-24 section1 advert">
						<cms:component component="${feature}" />
					</div>
				</cms:slot>
			</div>
		</div>
	</jsp:body>
</template:page>
