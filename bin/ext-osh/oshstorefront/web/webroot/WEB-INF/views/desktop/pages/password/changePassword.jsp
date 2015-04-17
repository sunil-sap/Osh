<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<script type="text/javascript" charset="utf-8">
$(document).ready(function(){
	
	$('#login').validate({
		rules : {
			confirmnewpassword : {
				equalTo : "#newpassword"
			}
		},
		messages : {
			repassword : "Please Enter Correct Password"
		}
	});
	
});
</script>
<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
			<div class="mainmiddleContent">
				<div class="leftmaincontent">
					<div class="left_img">
					<!-- <img width="156" height="555" align="left"/> -->
						<cms:slot var="feature" contentSlot="${slots.SideContent}">
							<cms:component component="${feature}" />
						</cms:slot>	
					</div>
				</div>
				<div class="middlemaincontent">
					<div class="middlemainheaderbanner">
						<span> <cms:slot var="feature"
								contentSlot="${slots.ChangePwdNameSection}">
								<cms:component component="${feature}" />
							</cms:slot>
						</span>
					</div>
					<div>
					<div class="clearb"></div>
						<div class="middle_form">
							<div class="loginbox2">
								<h2><spring:theme code="osh.changePwd.page.change.password"/></h2>
								<div class="clearb"></div>
								<div class="formwrap">
									<form id="login" name="login" novalidate="novalidate">
										<p><spring:theme code="osh.changePwd.page.msg.temporary.password"/></p>
										<div class="red_note">
											<p><spring:theme code="osh.changePwd.page.msg.fields.required"/></p>
										</div>
										<p><spring:theme code="osh.changePwd.page.msg.email.address"/></p>
										<input id="email" class="email required textfield" type="text"
											name="email" value="">
										<p><spring:theme code="osh.changePwd.page.msg.req.tempory.password"/></p>
										<input class="temporary required password textfield"
											type="password" name="password" value="">
										<p><spring:theme code="osh.changePwd.page.msg.req.new.password"/></p>
										<input class="new password required textfield" type="password"
											name="newpassword" id="newpassword" value="">
										<p><spring:theme code="osh.changePwd.page.msg.req.confirm.password"/></p>
										<input class="confnew password textfield" type="password"
											name="confirmnewpassword" id="confirmnewpassword" value="">
										<br clear="all">
										<div class="clearb"></div>
										<div class="btn2 floatl">
											<input id="submit" type="submit" value="Save">
										</div>
									</form>
									<div class="clearb"></div>
								</div>
								</div>
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
