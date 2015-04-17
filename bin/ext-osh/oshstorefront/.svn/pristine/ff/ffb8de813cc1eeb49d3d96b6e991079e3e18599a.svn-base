<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.cart.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div data-theme="e" class="item_container_holder">
			<h3>
				<spring:theme code="checkout.multi.addressDetails" text="Address Details" />
			</h3>
			<p></p>
			<p class="continuous-text"> <spring:theme code="checkout.multi.addEditform" text="Please use this form to add/edit an address." /> </p>
			<p class="continuous-text"> <spring:theme code="form.required" text="Fields marked * are required" /></p>
			<form:form method="post" commandName="addressForm">
				<common:errors />
				<form:hidden path="addressId" />
				<ul class="mFormList">
					<li>
						<formElement:formSelectBox idKey="address.title" labelKey="address.title" path="titleCode" mandatory="true" skipBlank="false" 
							skipBlankMessageKey="address.title.pleaseSelect" items="${titles}" selectedValue="${addressForm.titleCode}" />
					</li>
					<li><formElement:formInputBox idKey="address.firstName" labelKey="address.firstName" path="firstName" inputCSS="text" mandatory="true" /></li>
					<li><formElement:formInputBox idKey="address.surname" labelKey="address.surname" path="lastName" inputCSS="text" mandatory="true" /></li>
					<li><formElement:formInputBox idKey="address.line1" labelKey="address.line1" path="line1" inputCSS="text" mandatory="true" /></li>
					<li><formElement:formInputBox idKey="address.line2" labelKey="address.line2" path="line2" inputCSS="text" mandatory="false" /></li>
					<li><formElement:formInputBox idKey="address.townCity" labelKey="address.townCity" path="townCity" inputCSS="text" mandatory="true" /></li>
					<li><formElement:formInputBox idKey="address.postcode" labelKey="address.postcode" path="postcode" inputCSS="text" mandatory="true" /></li>
					<li>
						<formElement:formSelectBox idKey="address.country" labelKey="address.country" path="countryIso" mandatory="true" skipBlank="false"
							skipBlankMessageKey="address.selectCountry" items="${countries}" itemValue="isocode" selectedValue="${addressForm.countryIso}" />
					</li>
					<c:if test="${ not addressForm.defaultAddress}">
						<li>
							<formElement:formCheckbox idKey="address.default" labelKey="address.default" path="defaultAddress" inputCSS="add-address-left-input"
								labelCSS="add-address-left-label" mandatory="false" />
						</li>
					</c:if>
					<li>
						<div class="ui-grid-a">
							<div class="ui-block-a">
								<c:if test="${not noAddress}">
									<a data-rel="back" class="form" type="button" data-theme="d" data-icon="delete">
										<spring:theme code="checkout.multi.cancel" text="Cancel" />
									</a>
								</c:if>
							</div>
							<div class="ui-block-b">
								<button class="form" data-theme="c" data-icon="check">
									<spring:theme code="text.button.save" text="Save" />
								</button>
							</div>
						</div>
					</li>
				</ul>
			</form:form>
		</div>
	</jsp:body>
</template:page>
