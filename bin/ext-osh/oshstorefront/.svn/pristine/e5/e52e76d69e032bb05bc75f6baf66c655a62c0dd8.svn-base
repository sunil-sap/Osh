<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<nav:myaccountNav/>
		<div class="item_container_holder" data-content-theme="d" data-theme="e">
			<div id="globalMessages" data-theme="e">
				<common:globalMessages/>
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section1']}">
				<cms:component component="${feature}"/>
			</cms:slot>
			<div data-content-theme="d" data-theme="e">
				<h3><spring:theme code="text.account.addressBook.addressDetails" text="Address Details"/></h3>
				<p>
				<div class="item_container">
					<p class="continuous-text">
						<spring:theme code="text.account.addressBook.addEditform" text="Please use this form to add/edit an address."/>
					</p>
					<p class="continuous-text">
						<spring:theme code="form.required" text="Fields marked * are required"/>
					</p>
					<c:url value="/my-account/add-address" var="formActionUrl"/>
					<c:if test="${not empty addressForm.addressId}">
						<c:url value="/my-account/edit-address/${addressForm.addressId}" var="formActionUrl"/>
					</c:if>
					<form:form action="${formActionUrl}" method="post" commandName="addressForm" data-ajax="false">
						<common:errors/>
						<ul class="mFormList" data-theme="d" data-content-theme="d">
							<form:hidden path="addressId"/>
							<li>
								<formElement:formSelectBox idKey="address.title" labelKey="address.title" path="titleCode" mandatory="true"
									skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect" items="${titleData}" selectedValue="${addressForm.titleCode}"/>
							</li>
							<li>
								<formElement:formInputBox idKey="address.firstName" labelKey="address.firstName" path="firstName" inputCSS="text" mandatory="true"/>
							</li>
							<li>
								<formElement:formInputBox idKey="address.surname" labelKey="address.surname" path="lastName" inputCSS="text" mandatory="true"/>
							</li>
							<li>
								<formElement:formInputBox idKey="address.line1" labelKey="address.line1" path="line1" inputCSS="text" mandatory="true"/>
							</li>
							<li>
								<formElement:formInputBox idKey="address.line2" labelKey="address.line2" path="line2" inputCSS="text" mandatory="false"/>
							</li>
							<li>
								<formElement:formInputBox idKey="address.townCity" labelKey="address.townCity" path="townCity" inputCSS="text" mandatory="true"/>
							</li>
							<li>
								<formElement:formInputBox idKey="address.postcode" labelKey="address.postcode" path="postcode" inputCSS="text" mandatory="true"/>
							</li>
							<li>
								<formElement:formSelectBox idKey="address.country" labelKey="address.country" path="countryIso" mandatory="true" skipBlank="false"
									skipBlankMessageKey="address.selectCountry" items="${countryData}" itemValue="isocode" selectedValue="${addressForm.countryIso}"/>
							</li>
							<c:if test="${ not addressForm.defaultAddress}">
								<li data-theme="d">
									<formElement:formCheckbox idKey="address.default" labelKey="address.default" path="defaultAddress" 
										inputCSS="add-address-left-input" labelCSS="add-address-left-label" mandatory="false"/>
								</li>
							</c:if>
							<li>
								<fieldset class="ui-grid-a doubleButton">
									<div class="ui-block-a">
										<c:url value="/my-account/address-book" var="accountAddressBookUrl"/>
										<a href="${accountAddressBookUrl}" data-role="button" data-theme="d" data-icon="delete" class="ignoreIcon">
											<spring:theme code="text.button.cancel"/>
										</a>
									</div>
									<div class="ui-block-b">
										<ycommerce:testId code="accountEditAddress_SaveAddress_button">
											<button class="form" data-theme="c" data-icon="check" data-ajax="false">
												<spring:theme code="text.button.save"/>
											</button>
										</ycommerce:testId>
									</div>
								</fieldset>
							</li>
						</ul>
					</form:form>
				</div>
				</p>
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
