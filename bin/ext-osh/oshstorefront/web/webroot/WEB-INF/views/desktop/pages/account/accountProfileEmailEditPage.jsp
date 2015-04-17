<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>

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
				<div class="span-20 last multicheckout editemail">
					<div class="item_container_holder1">
						<div class="title_holder">
							<div class="title">
								<div class="title-top">
									<span></span>
								</div>
							</div>
							<h2><spring:theme code="text.account.change.email.address" text="Profile"/></h2>
						</div>
			<div class="item_container">
				<p><spring:theme code="text.account.profile.updateEmailAddress" text="Enter your new email address and confirm with your password"/></p>
				<p class="required"><spring:theme code="form.required" text="Fields marked * are required"/></p>
				<form:form action="update-email" method="post" commandName="updateEmailForm" id="updateEmailForm">
					<dl>
						<formElement:formInputBox idKey="profile.email" labelKey="profile.current.email" path="chkEmail" inputCSS="text email" mandatory="true"/>
						<formElement:formInputBox idKey="profile.checkEmail"  labelKey="profile.checkEmail" path="email" inputCSS="text email" mandatory="true"/>
						<formElement:formPasswordBox idKey="profile.pwd" labelKey="profile.pwd" path="password" inputCSS="text" mandatory="true"/>
					</dl>
					<span style="display: block; clear: both;">
					<ycommerce:testId code="myAccount_profile_SaveUpdates_button">
						<button class="form" onclick="submitProfileForm()"><spring:theme code="text.account.profile.saveUpdates" text="Save Updates"/></button>
					</ycommerce:testId>
					</span>
				</form:form>
			</div>
		</div>
	  </div>
	</div>
  </div>
</div>
<script type="text/javascript">
function submitProfileForm(){
	$("#updateEmailForm").validate();
	if($("#updateEmailForm").valid()){
		$("#updateEmailForm").submit();
	}
	
}
</script>
</template:page>

