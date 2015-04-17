<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>

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
			<div class="acc_middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav />
				</div>

				<div class="span-20password multicheckout">
					<div class="item_container_holder_password">
						<div class="title_holder">
							<div class="title">
								<div class="title-top">
									<span></span>
								</div>
							</div>
							<h2>
								<spring:theme code="text.account.profile.updatePasswordForm"
									text="Update Password" />
							</h2>
						</div>
						<div class="item_container">
							<p>
								<spring:theme code="text.account.profile.updatePassword"
									text="Please use this form to update your account password" />
							</p>
							<p class="required">
								<spring:theme code="form.required"
									text="Fields marked * are required" />
							</p>
							<form:form action="update-password" method="post"
								commandName="updatePasswordForm" autocomplete="off"
								id="changePassword">
								<dl>
									<spring:theme code="profile.currentPassword"></spring:theme>
									<br />
									<form:password id="currentPassword" path="currentPassword"
										cssStyle="text password" mandatory="true"
										onblur="$('#error-msg-password').hide();" />
									<br />
									<div id="globalMessages">
										<common:globalMessages />
									</div>
									<span class="error-msg" id="error-msg-password"
										style="display: none"><spring:message
											code="register.currentpassword.invalid" /></span>
									<br />
									<spring:theme code="profile.newPassword"></spring:theme>
									<br />
									<form:password id="newPassword" path="newPassword"
										cssStyle="text password strength" mandatory="true"
										onblur="$('#error-msg-newpassword').hide();" />
									<br />
									<span class="error-msg" id="error-msg-newpassword"
										style="display: none"><spring:message
											code="register.pwd.invalid" /></span>
									<br />
									<spring:theme code="profile.checkNewPassword"></spring:theme>
									<br />
									<form:password id="checkNewPassword" path="checkNewPassword"
										cssStyle="text password" mandatory="true"
										onblur="$('#error-msg-conform-password').hide();"/>
									<br />
									<span class="error-msg" id="error-msg-conform-password"
										style="display: none"><spring:message
											code="register.conform.password.invalid" /></span>
									<span class="error-msg" id="error-msg-conform-password-mismatch"
										style="display: none"><spring:message
											code="register.conform.password.mismatch" /></span>
											<br/>
								</dl>
								<span style="display: block; clear: both;"> <ycommerce:testId
										code="profilePage_SaveUpdatePasswordButton">
										<button id="submitpassword" class="form">
											<spring:theme code="text.account.profile.updatePasswordForm"
												text="Update Password" />
										</button>
									</ycommerce:testId>
								</span>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
<script type="text/javascript">
	$(document).ready(function() {

		$("#currentPassword , #newPassword").val('');
		$('#submitpassword').click(function(e) {
			e.preventDefault();
			var allfilled = true;
			if (!$("#currentPassword").val()) {
				$("#error-msg-password").show();
				$("#globalMessages").hide();
				allfilled = false;
			}
			if(!$("#newPassword").val() || !$("#checkNewPassword").val())
			{	
				if (!$("#newPassword").val()) {
					//$("#error-msg-newpassword").text('Please enter Password again');
					$("#error-msg-newpassword").show();
					allfilled = false;
				}
				if (!$("#checkNewPassword").val()) {
					//$("#error-msg-conform-password").text('Please enter Password again');
					$("#error-msg-conform-password").show();
					allfilled = false;
				}
				$("#globalMessages").hide();
			}
			
			else if( $("#newPassword").val().length < 6 || $("#checkNewPassword").val().length < 6)
				{
				if( $("#newPassword").val().length < 6 )
					{
						$("#error-msg-newpassword").show(); 
						$("#globalMessages").hide();
					}
					if( $("#checkNewPassword").val().length < 6 )
					{
						if ($("#checkNewPassword").val().toLowerCase() != $("#newPassword").val().toLowerCase()) {
						$("#error-msg-conform-password-mismatch").show();
						$("#error-msg-conform-password").hide();
						}
						/* else
							{
							$("#error-msg-conform-password").show();
							$("#error-msg-conform-password-mismatch").hide();
							} */
					
						$("#globalMessages").hide();
					}
					allfilled = false;			
			    }
			else if ($("#checkNewPassword").val().toLowerCase() != $("#newPassword").val().toLowerCase()) {
				$("#error-msg-conform-password-mismatch").show();
				allfilled = false;
			}
			if (allfilled) {
				$("#changePassword").submit();
			}

		});
	});
	
</script>