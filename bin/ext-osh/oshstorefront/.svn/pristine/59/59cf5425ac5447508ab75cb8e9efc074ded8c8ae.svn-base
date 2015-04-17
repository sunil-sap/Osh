<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="wishlist" tagdir="/WEB-INF/tags/desktop/wishlist"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${store.name} | ${siteName}">
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
			<div class="middlemaincontent">
				<div class="middlewrapper">
					<div class="middle_upper">
						<h2>
							<b><spring:theme code="subscribe.unsubscribe.cheetah.mail.enter.value.message"/></b>
						</h2>
						<form id="guestlogin" name="guestlogin" novalidate="novalidate">
							<br clear="all">
							<p>EmailId</p>
							<input id="emailId" class="email required textfield" type="text"
								name="email2" value=""> <br clear="all"> <br
								clear="all">
							<div class="clearb"></div>
							<div class="btn2 floatl">
								<input id="emailUnSubscriber"type="submit" name="Checkout as a Guest"
									value="Un Subscribe">
							</div>
						</form>
					</div>
				</div>

				<div id="globalMessages">
					<common:globalMessages />
				</div>
			</div>
		</div>
</template:page>

<script type="text/javascript">

$('#emailUnSubscriber')
.click(
		function(e) {
			e.preventDefault();
			var allowSubmit = true;
			
			 var emailId=$("#emailId").val();
				var emailRegex = new RegExp(
				/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
				var email1 = emailRegex.test(emailId);
				var subscriber=false;
				if(emailId!="" && !email1)
					{
					alert("Enter Valid Email Address");
					}
				else
					{
				$.ajax(
					{					
						url :	"${request.contextPath}/subscribe/cheetah/unsubscribe",
						type : 'POST',
						data : {emailId : emailId,subscriber : subscriber},
						success : function(data){
							if(data){
								alert("You Succesfully UnSubscriberd For Osh News");
							}
							else
								{
								alert("You Succesfully UnSubscriberd For Osh News");
								}
						}
					});
					}
		});
		</script>