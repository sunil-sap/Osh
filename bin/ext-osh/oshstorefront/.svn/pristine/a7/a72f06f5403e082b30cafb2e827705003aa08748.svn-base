<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/desktop/user"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<template:checkoutLogin />
<html>
<title></title>
<head>
<script type="text/javascript" charset="utf-8">
$(document).ready(function(){
	$('#login').validate();
});
</script>
</head>

<body>
	
	<div id="loginmainBody" class="check_login_main">
		<div id="mainContent">
			<div id="checkout_header">
				<div id="headerLogo">
				<c:url value="/" var="homeUrl" />
					<a href="${homeUrl}">
					<cms:slot var="feature" contentSlot="${slots.CheckoutSiteLogo}">
						<cms:component component="${feature}" />
					</cms:slot>
					</a>
				</div>
				<div class="right_text">
					<cms:slot var="feature" contentSlot="${slots.CheckoutRight}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
			</div>
			<div id="loginmiddleContent">
				<div class="innermiddleContent">
					<div class="loginwrapper_checkoutlogin">
						<div class="loginbox1_checkoutlogin">
							<user:guestLogin />	
						</div>
						<div class="loginbox2_checkoutlogin">
							<c:url value="/checkout/j_spring_security_check"
								var="loginAndCheckoutActionUrl" />
							<c:url value="/checkout/j_spring_security_check" var="loginAndCheckoutAction" />
							<user:checkoutLogin actionNameKey="checkout.login.loginAndCheckout" action="${loginAndCheckoutAction}" />
					   </div>
				</div>
			
			<div id="checkout_footer">
				<div class="copyrightsbox overflow">
					<div class="">
			
					<div class="checkoutbottomimage">
							<cms:slot var="feature" 
								contentSlot="${slots.CheckoutFooterRight}">
								<cms:component component="${feature}" />
							</cms:slot>	
					</div>
					 	<cms:slot var="feature" contentSlot="${slots.CheckoutFooterLeft}">
							<cms:component component="${feature}" />
						</cms:slot>			
					
					</div>
				</div>
			</div>
		</div>
		</div>
		</div>
</div>
</body>
</html>