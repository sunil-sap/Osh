<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script type="text/javascript" src="${commonResourcePath}/js/accmob.account.js"></script>
	</jsp:attribute>
	<jsp:body>
		<nav:myaccountNav />
		<div class="item_container_holder" data-theme="e">
			<div id="globalMessages" data-theme="e">
				<common:globalMessages />
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section1']}">
				<cms:component component="${feature}" />
			</cms:slot>
			<h3>
				<spring:theme code="text.account.profile" text="Profile" />
			</h3>
			<p></p>
			<div class="item_container">
				<p class="continuous-text"><spring:theme code="text.account.profile.updateForm" text="Please use this form to update your personal details" /></p>
				<p class="continuous-text"><spring:theme code="form.required" text="Fields marked * are required" /></p>
				<c:url value="/my-account/update-email" var="updateEmailUrl" />
				<form:form action="${updateEmailUrl}" method="post" commandName="updateEmailForm" data-ajax="false">
					<common:errors />
					<ul class="mFormList" data-inset="true">
						<li><formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="text" mandatory="true" /></li>
						<li><formElement:formInputBox idKey="profile.checkEmail" labelKey="profile.checkEmail" path="chkEmail" inputCSS="text" mandatory="true" /></li>
						<li><formElement:formPasswordBox idKey="profile.pwd" labelKey="profile.pwd" path="password" inputCSS="text" mandatory="true" /></li>
						<li>
							<fieldset class="ui-grid-a doubleButton">
								<div class="ui-block-a">
									<c:url value="/my-account/profile" var="profileUrl" />
									<a href="${profileUrl}" data-role="button" data-theme="d" data-icon="delete" class="ignoreIcon">
										<spring:theme code="text.button.cancel" />
									</a>
								</div>
								<div class="ui-block-b">
									<button class="form" data-theme="c" data-icon="check">
										<spring:theme code="text.button.save" />
									</button>
								</div>
							</fieldset>
						</li>
					</ul>
				</form:form>
			</div>
			<cms:slot var="feature" contentSlot="${slots['Section2']}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
	</jsp:body>
</template:page>
