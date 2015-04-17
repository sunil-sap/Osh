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

<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<%-- <div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div> --%>
		<div class="innermiddleContent">
			
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div class="middlemaincontent">
				<div class="middlewrapper">
					<div class="middle_upper" id="cheetahSubscriber">
						<h2>
							<b><spring:message code="subscribe.cheetah.mail.enter.value.message"/></b>
						</h2>
						<h3><spring:message code="subscribe.cheetah.mail.profile"/></h3>

							<p><spring:message code="subscribe.cheetah.mail.email.address"/>
							<input id="emailId" class="email required textfield"
								type="text" name="email2" value="${emailId}" style="margin-left: 11px;"/>
								</p>
							<p><spring:message code="subscribe.cheetah.mail.email.postal.code"/>
							<input id="postalCode" class="email required textfield"
								type="text" name="postalCode2" style="margin-left: 40px;" />
								</p>
								
							<br clear="all">
							<br clear="all">
							<div class="clearb"></div>
							<div class="btn2 floatl">
								<input id="emailUnSubscriber" type="submit" 
									name="Checkout as a Guest" value="Subscribe">
							</div>
						<%-- </form:form> --%>
					</div>
				</div>
				<div id="globalMessages">
					<common:globalMessages />
				</div>
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

						var emailId = $("#emailId").val();
						var postalCode = $("#postalCode").val();
					    var postalCodeRegex = /^\d{5}$/;
					    var postCodeStatus=postalCodeRegex.test(postalCode)
						var emailRegex = new RegExp(
								/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
						var email1 = emailRegex.test(emailId);
						var subscriber = true;
						if (emailId != "" && !email1) {
							alert("Enter Valid Email Address");
						} 
						if (postalCode != "" && !postCodeStatus) {
							alert("Enter Valid Postal Code");
						} 						
						else {
							$.ajax({
								url : "${request.contextPath}/subscribe/cheetah/mail",
								type : 'POST',
								dataType : 'json',
								data : {emailId : emailId,subscriber : subscriber,postalCode : postalCode},
										success : function(data) {
											if (data) {
												$('#cheetahSubscriber').html("<spring:message code="subscribe.cheetah.mail.successfully.message" />");
											}
										}
									});
						}
					});
</script>